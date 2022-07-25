# spring-security-with-jwt

A Spring Boot 2.7.2 project that implements authentication and authorization using SecurityFilterChain
1. Creates a user table in MySQL database and inserts a record on app start
2. The password is BCrypted using BCryptPasswordEncoder
3. When a /login request comes, it generates a JWT token and returns as a response
4. For /hello request, Bearer token needs to be set in Authorization header
