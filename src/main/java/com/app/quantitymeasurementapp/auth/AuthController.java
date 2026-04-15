package com.app.quantitymeasurementapp.auth;

import com.app.quantitymeasurementapp.config.AppProperties;
import com.app.quantitymeasurementapp.jwt.JwtService;
import com.app.quantitymeasurementapp.user.Role;
import com.app.quantitymeasurementapp.user.User;
import com.app.quantitymeasurementapp.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Auth endpoints for email/password and Google OAuth2 login.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Google OAuth2 login and user profile endpoints")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Register a new local user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank() || request.getPassword() == null
                || request.getPassword().isBlank() || request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Name, email and password are required"));
        }

        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "This email is already registered"));
        }

        User newUser = User.builder()
                .name(request.getName().trim())
                .email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile() != null && !request.getMobile().isBlank() ? request.getMobile().trim() : null)
                .role(Role.USER)
                .active(true)
                .build();

        User savedUser = userService.saveUser(newUser);
        String token = jwtService.generateToken(savedUser);

        return ResponseEntity.ok(toAuthResponse(savedUser, token));
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank() || request.getPassword() == null
                || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        User user = userService.findByEmail(request.getEmail().trim()).orElse(null);
        if (user == null || !user.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        if ("GOOGLE_OAUTH_USER".equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "This account uses Google login. Please continue with Google."));
        }

        boolean passwordMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());

        // Backward compatibility for existing plain-text local users already stored in DB.
        if (!passwordMatched && request.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user = userService.save(user);
            passwordMatched = true;
        }

        if (!passwordMatched) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        User updatedUser = userService.updateLastLogin(user);
        String token = jwtService.generateToken(updatedUser);
        return ResponseEntity.ok(toAuthResponse(updatedUser, token));
    }

    /**
     * Called after Google OAuth2 success. Returns full auth info including JWT
     * token. The token parameter is set by OAuth2SuccessHandler redirect.
     */
    @Operation(summary = "Auth success — returns JWT token after Google login")
    @GetMapping("/success")
    public ResponseEntity<AuthResponse> authSuccess(@RequestParam String token) {

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.badRequest().build();
        }

        String email = jwtService.extractEmail(token);
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(toAuthResponse(user, token));
    }

    /**
     * Returns the currently authenticated user's profile. Requires: Authorization:
     * Bearer <jwt>
     */
    @Operation(summary = "Get current logged-in user profile")
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Not authenticated. Send Authorization: Bearer <token>"));
        }

        return ResponseEntity.ok(Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail(),
                "pictureUrl", user.getPictureUrl() != null ? user.getPictureUrl() : "", "role", user.getRole().name(),
                "active", user.isActive(), "createdAt",
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : ""));
    }

    /**
     * Logout — client should delete their JWT. Server-side: no action needed for
     * stateless JWT.
     */
    @Operation(summary = "Logout — client deletes JWT token")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity
                .ok(Map.of("message", "Logged out successfully. Please delete your JWT token.", "status", "success"));
    }

    /**
     * Redirect to Google login. Spring Security handles the actual OAuth2 flow.
     */
    @Operation(summary = "Initiate Google login — redirects to Google OAuth2")
    @GetMapping("/login/google")
    public ResponseEntity<Map<String, String>> loginWithGoogle() {
        return ResponseEntity.ok(Map.of("message", "Open this URL in your browser to login with Google", "loginUrl",
                appProperties.getBaseUrl() + "/oauth2/authorization/google", "note",
                "After login you will be redirected to the Angular auth callback route"));
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(user.getId(), token, user.getEmail(), user.getName(), user.getPictureUrl(),
                user.getRole().name(), appProperties.getJwt().getExpirationMs());
    }
}
