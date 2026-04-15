package com.app.quantitymeasurementapp.auth;

import com.app.quantitymeasurementapp.config.AppProperties;
import com.app.quantitymeasurementapp.jwt.JwtService;
import com.app.quantitymeasurementapp.user.User;
import com.app.quantitymeasurementapp.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Called by Spring Security after successful Google OAuth2 login.
 *
 * Steps: 1. Extract Google user info from OAuth2User 2. Register or update user
 * in our DB 3. Generate JWT token 4. Redirect client to Angular callback route
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        User user = userService.registerOrUpdateUser(oAuth2User);
        logger.info("Google login successful for: {}", user.getEmail());

        String jwt = jwtService.generateToken(user);

        String redirectUrl = UriComponentsBuilder
                .fromHttpUrl(appProperties.getFrontendBaseUrl())
                .path("/auth/callback")
                .queryParam("token", jwt)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
