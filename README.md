### 📅 UC17: Spring Framework Integration - REST Services & JPA  

- **Description:**  
  Migrated the application to a Spring Boot REST service with embedded server, replacing JDBC with Spring Data JPA for ORM-based persistence.

- **Architecture:**  
  - Controller – Handles REST API requests  
  - Service – Business logic & transactions  
  - Repository – JPA-based data access  
  - Database – ORM using Hibernate  
  - Spring Boot – Auto-config + embedded Tomcat  

- **Implementation:**  
  - Built REST APIs using `@RestController`  
  - Replaced JDBC with Spring Data JPA  
  - Used DI (`@Autowired`), `@Transactional`  
  - Added exception handling & validation  
  - Integrated Swagger, Actuator, and testing (MockMvc)  
  - Optional Spring Security integration  

- **Example:**  
  `POST /quantity/add` → Returns calculated result stored via JPA

  [UC17 - Spring Backend](https://github.com/aryaman-0011/QuantityMeasurementApp/tree/feature/UC17-SpringBackend/src)

---

### 📅 UC18: Spring Security – Google Authentication & JWT  

- **Description:**  
  Secured the Spring Boot application using Spring Security with Google OAuth2 authentication and JWT-based authorization for stateless API access.

- **Architecture:**  
  - Security Layer – Handles authentication & authorization  
  - OAuth2 (Google) – User login via Google account  
  - JWT – Token-based authentication  
  - Controller – Secured REST endpoints  
  - Service – Business logic with role checks  

- **Implementation:**  
  - Integrated Spring Security  
  - Implemented Google OAuth2 login  
  - Generated & validated JWT tokens  
  - Secured APIs using filters and configurations  
  - Enabled role-based access control (RBAC)  

- **Example:**  
  Login via Google → Receive JWT → Access secured APIs with token

[UC18 - Spring Security](https://github.com/aryaman-0011/QuantityMeasurementApp/tree/feature/UC18-SpringSecurity/src)
