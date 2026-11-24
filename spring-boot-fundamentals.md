# Spring Boot Fundamentals & Enterprise Architecture Guide

An interview-oriented, production-grade reference guide covering core Spring Boot internal mechanics, auto-configuration, enterprise security, HTTP request lifecycle, caching, exception handling, and development workflows.

## Table of Contents

- [1. How a Spring Boot Application Starts (Internal Lifecycle)](#1-how-a-spring-boot-application-starts-internal-lifecycle)
- [2. How Spring Boot Auto-Configuration Works (Deep Dive)](#2-how-spring-boot-auto-configuration-works-deep-dive)
- [3. Core Annotations Used in Enterprise Applications](#3-core-annotations-used-in-enterprise-applications)
- [4. Enterprise Exception Handling in Spring Boot](#4-enterprise-exception-handling-in-spring-boot)
- [5. Authentication & Authorization (Stateless JWT & RBAC)](#5-authentication--authorization-stateless-jwt--rbac)
- [6. Spring Security Internal Architecture & Filter Chain Flow](#6-spring-security-internal-architecture--filter-chain-flow)
- [7. HTTP Request Lifecycle: Client to DispatcherServlet (Deep Dive)](#7-http-request-lifecycle-client-to-dispatcherservlet-deep-dive)
- [8. Caching Implementation (In-Memory Caffeine & Distributed Redis)](#8-caching-implementation-in-memory-caffeine--distributed-redis)
- [9. Refresh Token Implementation & Rotation Strategy](#9-refresh-token-implementation--rotation-strategy)
- [10. Essential Maven Commands for Java Developers](#10-essential-maven-commands-for-java-developers)

---

## 1. How a Spring Boot Application Starts (Internal Lifecycle)

Understanding what happens behind `SpringApplication.run(Application.class, args)` is one of the most frequently asked questions in top product and MNC interviews.

### The Execution Lifecycle Flow

```
                      ┌─────────────────────────────────────────┐
                      │   SpringApplication.run(App.class, args)│
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 1. Initialize SpringApplication Instance│
                      │    - Determine WebApplicationType       │
                      │    - Load Initializers & Listeners      │
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 2. Prepare ConfigurableEnvironment      │
                      │    - Load application.yml, OS env, args │
                      │    - Fire ApplicationEnvironmentPrepared│
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 3. Print Banner (ASCII / Custom)        │
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 4. Create ApplicationContext            │
                      │    - AnnotationConfigServletWebServer   │
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 5. Prepare ApplicationContext           │
                      │    - Apply Initializers                 │
                      │    - Load Bean Definitions into Registry│
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 6. Refresh Context (context.refresh())  │
                      │    - Start Embedded Tomcat/Jetty Server │
                      │    - Instantiate Singletons (DI / Autowiring)
                      │    - Trigger Auto-Configurations        │
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 7. Execute Runners                      │
                      │    - CommandLineRunner / AppRunner      │
                      └────────────────────┬────────────────────┘
                                           │
                                           ▼
                      ┌─────────────────────────────────────────┐
                      │ 8. Application Ready & Running          │
                      └─────────────────────────────────────────┘
```

### Step-by-Step Internal Breakdown

#### Step 1: Instantiating `SpringApplication`
Before the application runs, the constructor of `SpringApplication` performs 4 initial setups:
1. **Determines the Web Application Type:** Evaluates the classpath:
   - `SERVLET` (`org.springframework.web.context.ConfigurableWebApplicationContext` present)
   - `REACTIVE` (Spring WebFlux present)
   - `NONE` (Standard console / batch app)
2. **Loads Initializers:** Reads `ApplicationContextInitializer` implementations configured in `META-INF/spring.factories` or auto-configuration imports.
3. **Loads Listeners:** Reads `ApplicationListener` implementations to broadcast startup lifecycle events.
4. **Identifies Main Application Class:** Scans stack traces to find the class containing `public static void main`.

#### Step 2: Preparing the Environment
- Instantiates a `ConfigurableEnvironment` (such as `StandardServletEnvironment`).
- Merges all configuration sources in order of precedence: Command line arguments $\to$ Servlet config $\to$ System properties $\to$ OS environment variables $\to$ `application-{profile}.yml` $\to$ `application.yml`.
- Broadcasts `ApplicationEnvironmentPreparedEvent`.

#### Step 3: Printing the Banner
- Displays the default Spring Boot banner or a custom `banner.txt` located in `src/main/resources`.

#### Step 4: Creating the `ApplicationContext`
Based on the detected application type:
- For standard Spring MVC web applications, it creates `AnnotationConfigServletWebServerApplicationContext`.

#### Step 5: Preparing the Context
- Binds the environment to the context.
- Applies all registered `ApplicationContextInitializer`s.
- Fires `ApplicationPreparedEvent`.
- Registers the primary source class (annotated with `@SpringBootApplication`) into the `BeanDefinitionRegistry`.

#### Step 6: Refreshing the Context (`context.refresh()`) — The Core Step
This delegates to the standard Spring Framework `AbstractApplicationContext.refresh()`:
1. **Post-Process Bean Factories:** Executes `BeanFactoryPostProcessor`s (processes `@Configuration` classes, imports, component scanning).
2. **Registers BeanPostProcessors:** Interceptors for bean instantiation (e.g. `@Autowired`, AOP proxies, `@Transactional`).
3. **Starts Embedded Web Server:** Initialises the embedded Tomcat/Jetty/Undertow container and binds the port (default `8080`).
4. **Instantiates Non-Lazy Singletons:** Creates all service, repository, and controller bean instances, resolving dependencies via Dependency Injection.
5. **Initializes Auto-Configuration Beans:** Evaluates condition annotations (`@ConditionalOnClass`, `@ConditionalOnMissingBean`).

#### Step 7: Executing Runners
Once the context is refreshed and the web server is listening, Spring Boot executes:
- Any bean implementing **`CommandLineRunner`** (`run(String... args)`).
- Any bean implementing **`ApplicationRunner`** (`run(ApplicationArguments args)`).

#### Step 8: Ready Event
- Fires `ApplicationReadyEvent`. The application is fully live and accepting traffic.

> **💡 Top Interview Question:** *Where in the startup flow does embedded Tomcat start?*  
> **Answer:** Embedded Tomcat is initialized during the **`context.refresh()`** phase inside `onRefresh()` of `ServletWebServerApplicationContext`, before non-lazy singletons are fully initialized.

[↑ Back to Table of Contents](#table-of-contents)

---

## 2. How Spring Boot Auto-Configuration Works (Deep Dive)

Auto-Configuration is Spring Boot's "convention-over-configuration" mechanism that automatically configures beans based on dependencies present on your classpath.

### The Magic: `@SpringBootApplication`

`@SpringBootApplication` is a meta-annotation composed of three critical annotations:

```java
@SpringBootConfiguration // Specialization of @Configuration for Boot
@EnableAutoConfiguration // Triggers the auto-configuration engine
@ComponentScan           // Scans current package and sub-packages for @Component, @Service, etc.
public @interface SpringBootApplication { ... }
```

---

### How `@EnableAutoConfiguration` Works Internally

```
@EnableAutoConfiguration
        │
        ▼
Imports: AutoConfigurationImportSelector.class
        │
        ▼
Reads descriptor file:
  - Spring Boot 3+: META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
  - Spring Boot 2.x: META-INF/spring.factories
        │
        ▼
Loads 130+ candidate AutoConfiguration classes (e.g., DataSourceAutoConfiguration, SecurityAutoConfiguration)
        │
        ▼
Applies @Conditional Filtering (Evaluates if classes, beans, or properties exist)
        │
        ▼
Registers remaining matching configurations as Beans into Spring ApplicationContext
```

1. **`AutoConfigurationImportSelector`:**  
   Implements `DeferredImportSelector`. It defers loading auto-configurations until all user-defined `@Configuration` classes are parsed first, ensuring user beans always take precedence!
2. **Scanning Configuration Imports:**  
   In Spring Boot 3+, it reads candidate configuration classes listed line-by-line inside:  
   `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
3. **Evaluation of Conditional Annotations:**  
   Spring Boot filters the 130+ candidate configurations using condition checks. Only configs whose conditions evaluate to `true` are loaded.

---

### Essential `@Conditional` Annotations

| Annotation | Description | Real-World Example |
|---|---|---|
| **`@ConditionalOnClass`** | Matches if specified class exists on the classpath | `DataSourceAutoConfiguration` checks for `DataSource.class` |
| **`@ConditionalOnMissingClass`** | Matches if specified class does NOT exist on classpath | Used for fallback configurations |
| **`@ConditionalOnBean`** | Matches only if a specific bean already exists in context | Used when a bean depends on another configured bean |
| **`@ConditionalOnMissingBean`** | Matches only if the bean has **NOT** been defined by user | **Crucial:** Allows developers to override default beans with custom ones |
| **`@ConditionalOnProperty`** | Matches based on configuration properties in `application.yml` | `@ConditionalOnProperty(prefix="app.feature", name="enabled", havingValue="true")` |
| **`@ConditionalOnWebApplication`** | Matches if app is a web application (Servlet or Reactive) | Applied to all MVC controllers/filters |
| **`@ConditionalOnExpression`** | SpEL (Spring Expression Language) based condition | Complex multi-condition evaluations |

#### Real-World Example: How `DataSourceAutoConfiguration` Works

```java
@AutoConfiguration
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@ConditionalOnMissingBean(type = "io.r2dbc.spi.ConnectionFactory")
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean // 👈 If you define your own DataSource @Bean, this default is ignored!
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}
```

### How to Debug Auto-Configuration in Interviews

Run your application with the `--debug` flag or set `debug: true` in `application.yml`. Spring Boot prints the **Auto-Configuration Report (ConditionEvaluationReport)**:
- **Positive matches:** Configurations that ran and why.
- **Negative matches:** Configurations that were skipped and which `@Conditional` check failed.
- **Exclusions:** Configurations explicitly excluded via `@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})`.

[↑ Back to Table of Contents](#table-of-contents)

---

## 3. Core Annotations Used in Enterprise Applications

### 1. Stereotype & Core Dependency Injection

- **`@Component`:** Generic archetype for any Spring-managed component.
- **`@Service`:** Specialization of `@Component` indicating business logic layer.
- **`@Repository`:** Specialization indicating DAO/data layer; automatically enables persistence exception translation (translates SQL exceptions into Spring's `DataAccessException`).
- **`@Controller`:** Marks class as Spring MVC controller returning view templates (JSP/Thymeleaf).
- **`@RestController`:** Combines `@Controller` + `@ResponseBody`. All methods return JSON/XML serialized response directly.
- **`@Autowired`:** Injects dependency by type. *(Note: Constructor injection is preferred over field injection).*
- **`@Qualifier("beanName")`:** Resolves ambiguity when multiple beans of the same type exist.
- **`@Primary`:** Gives higher preference to a bean when multiple candidates exist.
- **`@Value("${app.jwt.secret}")`:** Injects values from properties files.
- **`@ConfigurationProperties(prefix = "app")`:** Type-safe binding of nested YAML/properties structures into a Java POJO/Record.
- **`@Scope`:** Configures bean scope (`singleton` [default], `prototype`, `request`, `session`).
- **`@Lazy`:** Defers bean instantiation until first access.

---

### 2. Spring MVC & REST API

- **`@RequestMapping`:** Maps HTTP requests to handler classes or methods.
- **`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`:** HTTP verb-specific shortcuts.
- **`@PathVariable("id")`:** Extracts URI path segments (`/users/{id}`).
- **`@RequestParam("page")`:** Extracts query string parameters (`/users?page=1`).
- **`@RequestBody`:** Deserializes JSON HTTP request body into a Java object using Jackson.
- **`@RequestHeader("Authorization")`:** Extracts HTTP header values.
- **`@ResponseStatus(HttpStatus.CREATED)`:** Sets default HTTP status code for method response.
- **`@CrossOrigin`:** Configures Cross-Origin Resource Sharing (CORS) at controller/method level.

---

### 3. Spring Data JPA & Persistence

- **`@Entity` & `@Table(name = "users")`:** Maps Java class to a relational database table.
- **`@Id` & `@GeneratedValue(strategy = GenerationType.IDENTITY)`:** Specifies primary key and generation strategy.
- **`@Column(nullable = false, unique = true)`:** Customizes column mapping constraints.
- **`@OneToMany`, `@ManyToOne`, `@ManyToMany`:** Defines entity relationships.
- **`@JoinColumn(name = "department_id")`:** Specifies the foreign key column.
- **`@Transient`:** Tells JPA not to persist this field to the database.
- **`@Transactional(readOnly = true, rollbackFor = Exception.class)`:** Manages database transaction boundaries, commit, and rollback.
- **`@Modifying`:** Informs Spring Data JPA that a query will execute a `DML` (`INSERT`, `UPDATE`, `DELETE`) operation.
- **`@Query("SELECT u FROM User u WHERE u.email = :email")`:** Defines custom JPQL or native SQL queries.

---

### 4. Enterprise Security & Validation

- **`@EnableWebSecurity`:** Enables Spring Security configuration.
- **`@EnableMethodSecurity`:** Enables method-level authorization (replaces deprecated `@EnableGlobalMethodSecurity`).
- **`@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")`:** Evaluates security rule with SpEL before method execution.
- **`@Valid` / `@Validated`:** Triggers validation checks on `@RequestBody` or method parameters.
- **`@NotNull`, `@NotBlank`, `@NotEmpty`, `@Size`, `@Pattern`, `@Email`:** Jakarta Bean Validation constraints.

---

### 5. Asynchronous, Scheduling & Caching

- **`@EnableAsync` & `@Async`:** Executes method in a separate thread from a managed thread pool.
- **`@EnableScheduling` & `@Scheduled(cron = "0 0 * * * ?")`:** Executes periodic background tasks.
- **`@EnableCaching`, `@Cacheable`, `@CachePut`, `@CacheEvict`:** Declarative caching annotations.

[↑ Back to Table of Contents](#table-of-contents)

---

## 4. Enterprise Exception Handling in Spring Boot

In enterprise production services, you must never leak internal database stack traces to API consumers. All exceptions should return a standardized, machine-readable JSON structure.

### 1. Standard Error Response DTO

```java
package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors; // Populated only for validation failures
}
```

---

### 2. Custom Business Exceptions

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class BusinessRuleViolationException extends RuntimeException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
```

---

### 3. Global Exception Handler (`@RestControllerAdvice`)

```java
package com.example.exception;

import com.example.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Handle 400 Validation Errors (@Valid DTO failure)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Input parameters failed validation")
                .path(request.getRequestURI())
                .validationErrors(errors)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 3. Handle 403 Forbidden (Spring Security Access Denied)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("You do not have permission to access this resource")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 4. Fallback 500 Internal Server Error (Catches unhandled exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        // Log the actual exception for developers
        // log.error("Unhandled Exception at " + request.getRequestURI(), ex);

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred. Please contact system support.")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

> **Spring Boot 3 / RFC 7807 Note:** Spring Boot 3 introduces built-in support for the standard RFC 7807 **`ProblemDetail`** response class via `org.springframework.http.ProblemDetail`.

[↑ Back to Table of Contents](#table-of-contents)

---

## 5. Authentication & Authorization (Stateless JWT & RBAC)

- **Authentication (401 Unauthorized):** Verifies *who you are* (credentials check).
- **Authorization (403 Forbidden):** Verifies *what permissions/roles you have* to perform an action.

### Stateless JWT Workflow

```
[ Client ]                          [ Auth Controller ]                      [ Database ]
    │                                        │                                    │
    │ 1. POST /login (username, password)    │                                    │
    ├───────────────────────────────────────►│                                    │
    │                                        │ 2. Authenticate credentials        │
    │                                        ├───────────────────────────────────►│
    │                                        │ 3. User & roles verified           │
    │                                        │◄───────────────────────────────────┤
    │                                        │                                    │
    │                                        │ 4. Generate Access Token (15m)     │
    │                                        │    + Refresh Token (7d in DB)      │
    │ 5. Returns { accessToken, refreshToken}│                                    │
    │◄───────────────────────────────────────┤                                    │
    │                                        │                                    │
    │ 6. GET /api/orders                     │                                    │
    │    Header: Bearer <accessToken>        │                                    │
    ├───────────────────────────────────────►│                                    │
    │                                        │ 7. JwtFilter validates signature,  │
    │                                        │    extracts claims & sets SecurityContext
    │ 8. Returns 200 OK Response             │                                    │
    │◄───────────────────────────────────────┤                                    │
```

---

### Role-Based Access Control (RBAC) Implementation

#### 1. Enable Method Security

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize, @PostAuthorize
public class SecurityConfig { ... }
```

#### 2. Granular Controller Security with `@PreAuthorize`

```java
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    // Requires ROLE_ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers();
    }

    // Role check OR resource owner check using SpEL:
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 6. Spring Security Internal Architecture & Filter Chain Flow

Spring Security is built upon standard **Java EE Servlet Filters**. Understanding its internal mechanics is a major interview discriminator.

### High-Level Architecture Diagram

```
                 Incoming HTTP Request
                           │
                           ▼
                 [ Servlet Container ] (Tomcat)
                           │
                           ▼
               [ DelegatingFilterProxy ]  (Bridge between Servlet & Spring Context)
                           │
                           ▼
               [ FilterChainProxy ]       (Manages Security Filter Chains)
                           │
                           ▼
         ┌──────────────────────────────────────┐
         │         SecurityFilterChain          │
         │                                      │
         │ 1. CorsFilter                        │
         │ 2. CsrfFilter                        │
         │ 3. JwtAuthenticationFilter (Custom)  │
         │ 4. UsernamePasswordAuthFilter        │
         │ 5. ExceptionTranslationFilter        │
         │ 6. AuthorizationFilter               │
         └──────────────────┬───────────────────┘
                            │
                            ▼
                  [ DispatcherServlet ]
```

---

### Authentication Process Under the Hood

When an authentication request arrives:
1. **`JwtAuthenticationFilter` (Custom):** Extracts the `Authorization: Bearer <token>` header, verifies the cryptographic signature (using HMAC/RSA secret), and parses claims (Username, Roles).
2. **`UsernamePasswordAuthenticationToken`:** Creates an unauthenticated or authenticated token object implementing the `Authentication` interface.
3. **`AuthenticationManager` (`ProviderManager`):** Receives the token and delegates to one or more configured `AuthenticationProvider`s (e.g. `DaoAuthenticationProvider`).
4. **`UserDetailsService` & `PasswordEncoder`:** `DaoAuthenticationProvider` calls `userDetailsService.loadUserByUsername()` to fetch user record and compares passwords using `BCryptPasswordEncoder.matches()`.
5. **`SecurityContextHolder`:** Once authenticated, the fully populated `Authentication` object is stored in thread-local memory:
   ```java
   SecurityContextHolder.getContext().setAuthentication(authentication);
   ```

---

### Modern Spring Security Configuration (Spring Boot 3+ / Security 6+)

No more extending deprecated `WebSecurityConfigurerAdapter`! Modern configuration uses `@Bean SecurityFilterChain`:

```java
package com.example.config;

import com.example.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabled for stateless REST APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strength 10 by default
    }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 7. HTTP Request Lifecycle: Client to DispatcherServlet (Deep Dive)

The `DispatcherServlet` is the front-controller design pattern implementation at the heart of Spring MVC.

### Detailed Request Flow Diagram

```
 Client Request (HTTP GET /api/v1/orders/123)
      │
      ▼
 [ Web Server ] (Tomcat port 8080)
      │
      ▼
 [ Servlet Filter Chain ] (CorsFilter, SecurityFilter, LoggingFilter)
      │
      ▼
 ┌─────────────────────────────────────────────────────────────┐
 │                DispatcherServlet.doDispatch()               │
 │                                                             │
 │  1. HandlerMapping.getHandler()                             │
 │     └─► Finds HandlerExecutionChain (Controller + Interceptors)
 │                                                             │
 │  2. HandlerInterceptor.preHandle()                          │
 │     └─► Runs pre-processing (auth tokens, metrics)          │
 │                                                             │
 │  3. HandlerAdapter.handle()                                 │
 │     └─► RequestMappingHandlerAdapter                        │
 │         ├─► Argument Resolvers (@PathVariable, @RequestBody)│
 │         ├─► HttpMessageConverter (Jackson JSON deserializer) │
 │         └─► Invokes Controller Method (OrderController)     │
 │                                                             │
 │  4. Business Execution                                      │
 │     └─► Controller ──► Service Layer ──► Repository/DB      │
 │                                                             │
 │  5. HandlerInterceptor.postHandle()                         │
 │     └─► Runs post-processing before response rendering      │
 │                                                             │
 │  6. Message Conversion / View Resolution                    │
 │     └─► HttpMessageConverter serializes DTO to JSON         │
 │                                                             │
 │  7. HandlerInterceptor.afterCompletion()                    │
 │     └─► Final cleanup, logging request duration             │
 └──────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
                         Client Response (200 OK + JSON Payload)
```

### Step-by-Step Execution Mechanics

1. **Tomcat Socket Read:** Tomcat accepts the TCP socket, constructs `HttpServletRequest` and `HttpServletResponse`.
2. **Servlet Filters:** Passes request through all `javax.servlet.Filter` beans (CORS, Spring Security, GZIP compression).
3. **`DispatcherServlet.doDispatch()` Invocation:** The front controller takes charge of coordinating the request.
4. **`HandlerMapping`:** Queries all registered mapping strategies (`RequestMappingHandlerMapping`) to inspect `@RequestMapping` routes and determine which Controller class and method matches the URL.
5. **`HandlerExecutionChain` Creation:** Returns the matched Controller method bundled with all registered `HandlerInterceptor`s.
6. **`preHandle()` Interceptors:** Executes `interceptor.preHandle()`. If any returns `false`, execution halts immediately.
7. **`HandlerAdapter` Execution:** `RequestMappingHandlerAdapter` handles method invocation:
   - **`HandlerMethodArgumentResolver`:** Extracts annotations (`@PathVariable`, `@RequestParam`, `@RequestBody`) and prepares method arguments.
   - **`HttpMessageConverter` (Jackson `MappingJackson2HttpMessageConverter`):** Deserializes JSON request body into the Java DTO.
8. **Controller Execution:** Controller calls Service layer $\to$ Repository layer $\to$ Database, and returns the response DTO.
9. **`postHandle()` Interceptors:** Runs post-processing logic (if not an async request).
10. **Response Serialization:** For `@RestController`, Jackson converts the returned Java POJO into JSON text, writes it to the output stream, and sets HTTP status `200 OK` and `Content-Type: application/json`.
11. **`afterCompletion()` Interceptors:** Always executes (even if exceptions were thrown) for resource cleanup, diagnostic tracking, and metrics collection.

[↑ Back to Table of Contents](#table-of-contents)

---

## 8. Caching Implementation (In-Memory Caffeine & Distributed Redis)

Caching dramatically reduces database roundtrips for read-heavy operations.

### 1. Spring Cache Annotations

| Annotation | Behavior | Example Use Case |
|---|---|---|
| **`@EnableCaching`** | Activates Spring's caching infrastructure | Placed on `@Configuration` class |
| **`@Cacheable`** | Checks cache first; if found, returns cached value without executing method; if not, executes method and stores result | `getUserById(Long id)` |
| **`@CachePut`** | **Always** executes method and updates the cache with the new returned value | `updateUser(User user)` |
| **`@CacheEvict`** | Purges specific key or entire cache (`allEntries = true`) | `deleteUser(Long id)` |
| **`@Caching`** | Groups multiple caching operations together | Complex update triggering multiple evictions |

---

### 2. In-Memory Caching with Caffeine (Single-Instance Apps)

#### Dependencies (`pom.xml`):
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

#### Configuration (`application.yml`):
```yaml
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=5000,expireAfterWrite=10m,recordStats
```

---

### 3. Distributed Caching with Redis (Multi-Instance Microservices)

In a microservice cluster with multiple load-balanced instances, in-memory caches result in stale cache inconsistencies. A centralized Redis cache ensures all instances share identical state.

#### Dependencies (`pom.xml`):
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

#### Redis Cache Configuration with Custom TTL:

```java
package com.example.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // Default TTL 30 minutes
                .disableCachingNullValues()       // Prevent caching null objects
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .build();
    }
}
```

#### Production Service Caching Example:

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 1. Read: Caches result under "products" cache key matching ID
    @Cacheable(value = "products", key = "#id", unless = "#result == null")
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    // 2. Update: Updates DB AND updates the cache entry
    @CachePut(value = "products", key = "#productDTO.id")
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product = productRepository.save(toEntity(productDTO));
        return toDTO(product);
    }

    // 3. Delete: Purges entry from cache
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // 4. Clear all: Purges entire product cache
    @CacheEvict(value = "products", allEntries = true)
    public void evictAllProductCache() {
        // Method body empty; annotation triggers eviction
    }
}
```

> **🔥 Top Interview Gotcha: Self-Invocation Bypass:**  
> If method `A()` calls `@Cacheable` method `B()` in the **same class** (`this.B()`), caching **will NOT work**! Why? Because Spring Caching works through dynamic AOP proxies. Self-invocation bypasses the proxy and calls the internal method directly.

[↑ Back to Table of Contents](#table-of-contents)

---

## 9. Refresh Token Implementation & Rotation Strategy

### Why Do We Need Refresh Tokens?
- **Access Tokens:** Short-lived (e.g. 10–15 minutes). Sent with every API request. If intercepted, attacker window is minimal.
- **Refresh Tokens:** Long-lived (e.g. 7–30 days). Securely stored in database/Redis. Only sent to `/api/v1/auth/refresh` to obtain a new Access Token.

---

### Refresh Token Rotation (RTR) & Reuse Detection

**Refresh Token Rotation** is an industry standard: every time a refresh token is used, it is **invalidated and replaced with a brand-new refresh token**.

**Reuse Detection:** If an already invalidated refresh token is ever presented again, the system recognizes a token theft attack and immediately **revokes all active refresh tokens** for that user!

```
Normal Flow:
Client ──[Refresh Token A]──► Server ──► Validates, revokes Token A, issues Token B

Theft Scenario (Attacker steals Token A before client uses it):
Attacker ──[Token A]──► Server ──► Validates, revokes Token A, issues Token B to attacker
Legitimate Client ──[Token A]──► Server ──► REUSE DETECTED! Token A was already used!
                                            Server revokes ALL active tokens for this user!
```

---

### Implementation Code

#### 1. Entity Definition

```java
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    private boolean revoked;
}
```

#### 2. Refresh Token Service

```java
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public TokenRefreshResponse rotateRefreshToken(String requestToken) {
        RefreshToken token = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new BusinessRuleViolationException("Refresh token not found"));

        // Reuse detection check
        if (token.isRevoked()) {
            // Compromise detected! Revoke all tokens for this user
            refreshTokenRepository.revokeAllUserTokens(token.getUser().getId());
            throw new SecurityException("Refresh token reuse detected! All sessions terminated.");
        }

        // Check expiration
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BusinessRuleViolationException("Refresh token has expired. Please log in again.");
        }

        // 1. Revoke the used token (Rotation)
        token.setRevoked(true);
        refreshTokenRepository.save(token);

        // 2. Issue brand-new Refresh Token
        RefreshToken newRefreshToken = createRefreshToken(token.getUser().getId());

        // 3. Issue brand-new Access Token
        String newAccessToken = jwtService.generateAccessToken(token.getUser());

        return new TokenRefreshResponse(newAccessToken, newRefreshToken.getToken());
    }
}
```

#### 3. Controller Endpoint

```java
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = refreshTokenService.rotateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 10. Essential Maven Commands for Java Developers

Maven is the industry standard build lifecycle and dependency management tool for Spring Boot.

### 1. The Core Build Lifecycle

```
validate ──► compile ──► test ──► package ──► verify ──► install ──► deploy
```

- **`validate`:** Validates project structure and POM correctness.
- **`compile`:** Compiles Java source files (`src/main/java`) into bytecode in `target/classes`.
- **`test`:** Runs unit tests using Surefire Plugin against compiled classes.
- **`package`:** Packages compiled code into distributable format (`.jar` or `.war`).
- **`verify`:** Runs integration tests using Failsafe Plugin.
- **`install`:** Copies packaged artifact into your local repository (`~/.m2/repository`).
- **`deploy`:** Copies final artifact to remote artifact repository (Nexus / Artifactory / AWS CodeArtifact).

---

### 2. High-Frequency Daily Maven Commands

#### Compiling and Packaging
```bash
# Clean target folder and compile source code
mvn clean compile

# Fast build: Build executable JAR skipping tests (for quick local packaging)
mvn clean package -DskipTests

# Run full build with unit and integration tests
mvn clean verify
```

#### Running and Debugging Tests
```bash
# Run all unit tests
mvn test

# Run a single specific test class
mvn test -Dtest=UserServiceTest

# Run a single specific test method inside a test class
mvn test -Dtest=UserServiceTest#testCreateUser_Success

# Run multiple specific tests using wildcards
mvn test -Dtest=*ControllerTest
```

#### Running the Spring Boot Application
```bash
# Run Spring Boot app directly via Maven plugin
mvn spring-boot:run

# Run with a specific profile activated
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run with custom JVM arguments (e.g. debug port or memory flags)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx1024m -Xdebug"
```

---

### 3. Dependency Troubleshooting & "JAR Hell" Resolution

#### Finding Dependency Conflicts
```bash
# Print full hierarchical dependency tree (identifies transitive dependencies)
mvn dependency:tree

# Filter dependency tree to locate which library is bringing a specific JAR:
mvn dependency:tree -Dincludes=com.fasterxml.jackson.core:*

# Analyze unused declared dependencies and undeclared used dependencies
mvn dependency:analyze
```

#### How to Exclude a Conflicting Transitive Dependency in `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <!-- Exclude default Logback to use Log4j2 -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

---

### 4. Advanced Production Flags

| Command / Flag | Purpose |
|---|---|
| **`mvn clean install -U`** | Forces update of snapshot dependencies and releases from remote repository |
| **`mvn clean install -X`** | Enables full debug logging (indispensable when build fails with cryptic errors) |
| **`mvn clean install -T 4`** | Multithreaded build: compiles modules across 4 CPU threads for faster builds |
| **`mvn versions:display-dependency-updates`** | Scans and lists newer versions of all dependencies in `pom.xml` |
| **`mvn help:effective-pom`** | Displays fully merged POM containing all parent settings and default plugin configurations |

[↑ Back to Table of Contents](#table-of-contents)
