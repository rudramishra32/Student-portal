# Student Portal: simple project guide

## What the project does

This is a Java and Spring Boot REST API for managing students and departments. It provides HTTP endpoints to create, read, search, update, and delete students, and to create and list departments. It uses Spring Data JPA to store data in an H2 in-memory database.

## How one request works

1. A client (for example, Postman) sends an HTTP request to a controller URL.
2. The controller receives the request and calls a service method.
3. The service applies the application logic and calls a repository.
4. The repository uses JPA to read or write the database.
5. Spring returns the result as JSON.

## Files and folders

### Main Java source: `src/main/java/com/dbit/studentportal/`

- `StudentPortalApplication.java` — starts the Spring Boot application. `main` is the Java entry point; `@SpringBootApplication` enables Spring Boot setup and component scanning.
- `controller/StudentController.java` — defines student API URLs and HTTP methods. It handles listing, getting, searching, creating, updating, changing CGPA, and deleting students. It delegates work to `StudentService`.
- `controller/DepartmentController.java` — defines department API URLs for listing departments, getting one by ID, and creating one. It delegates work to `DepartmentService`.
- `service/StudentService.java` — contains student operations and rules: checks duplicate email on create/update, connects a student to an existing department, searches by name, finds students above a CGPA, and handles updates/deletes.
- `service/DepartmentService.java` — contains department operations: list all, find by ID, and create. A missing department is reported as a bad request through `IllegalArgumentException`.
- `repository/StudentRepository.java` — extends `JpaRepository`, which supplies common database operations. Its method names let Spring Data create queries for name search, minimum CGPA, email, and roll number. Some declared methods are not currently called by the service.
- `repository/DepartmentRepository.java` — extends `JpaRepository` to provide standard department database operations.
- `model/Student.java` — the student database entity and JSON data shape. It has student fields, validation annotations, timestamps, a department relationship, and a many-to-many courses relationship. `displayLabel` is `@Transient`, so it is calculated for output and is not stored as a database column.
- `model/Department.java` — the department database entity, with name/code and a one-to-many relationship to students. `@JsonIgnore` prevents serializing the reverse student list, which helps avoid circular JSON.
- `model/Course.java` — a course database entity with a many-to-many relationship back to students. There are no course API endpoints yet, and this class currently has no getters/setters or constructors.
- `exception/StudentNotFoundException.java` — custom runtime exception used when a requested student ID does not exist.
- `exception/ErrorResponse.java` — defines the JSON fields used to describe an API error: time, status, error, message, path, and optional details.
- `exception/GlobalExceptionHandler.java` — converts student-not-found, bad-request, and validation exceptions into consistent JSON responses.

### Configuration and request files

- `src/main/resources/application.properties` — configures the app name, H2 connection, JPA schema update behavior, SQL logging, and H2 browser console path (`/h2-console`). The H2 URL uses `mem`, so the database is in memory and data is not retained after the app stops.
- `pom.xml` — Maven project configuration. It sets Java 17 and lists Spring Boot web, JPA, validation, H2, and test dependencies. Maven uses this file to build and run the app.
- `requests.http` — sample HTTP requests that can be sent from IntelliJ's HTTP client. The `###` lines separate requests. Create departments first, list them, then use the actual returned department IDs in student requests.
- `postman/globals/workspace.globals.yaml` — Postman workspace globals file; currently it has no values.
- `.postman/resources.yaml` — Postman workspace resource configuration. It registers resources from the `postman/` folder.
- `src/test/java/com/dbit/studentportal/StudentPortalApplicationTests.java` — test source file generated with the project. Check its contents before claiming it tests particular API behavior.
- `mvnw` and `mvnw.cmd` — Maven wrapper scripts for running Maven without separately installing Maven.
- `.gitignore` — lists files Git should ignore; `.gitattributes` configures Git handling for files.
- `HELP.md` — generated Spring Boot help file.
- `.idea/` — IntelliJ project settings and local database connection metadata. These are IDE files, not part of the REST API. Do not edit database metadata just to remove the SQL error unless a specific setting/file is identified.
- `target/` — generated build output. Do not edit these copies directly; Maven recreates them from `src/`.

## API endpoints in this project

Base URL while the app runs locally: `http://localhost:8080`

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/v1/departments` | List departments |
| GET | `/api/v1/departments/{id}` | Get one department |
| POST | `/api/v1/departments` | Create a department |
| GET | `/api/v1/students` | List students |
| GET | `/api/v1/students/{id}` | Get one student |
| GET | `/api/v1/students/search?name=priya` | Search student names (case-insensitive substring) |
| GET | `/api/v1/students/top?minCgpa=8.0` | List students whose CGPA is at least the requested value; default is 8.0 |
| POST | `/api/v1/students` | Create a student |
| PUT | `/api/v1/students/{id}` | Update a student's supplied fields |
| PATCH | `/api/v1/students/{id}/cgpa?cgpa=9.0` | Change CGPA |
| DELETE | `/api/v1/students/{id}` | Delete a student |

For POST and PUT, send JSON and set `Content-Type: application/json`. For student creation, a department can be supplied like `"department": {"id": 1}`; use an ID returned by the department endpoint, not an assumed ID.

## Basic project questions and answers

**What is a REST API?**  
It lets a client communicate with an application using HTTP requests and URLs. This project returns data as JSON.

**What do GET, POST, PUT, PATCH, and DELETE mean here?**  
GET reads data, POST creates a record, PUT updates a student, PATCH changes only CGPA, and DELETE removes a student.

**What is a controller?**  
A controller connects a URL and HTTP method to Java code that handles the request.

**Why use a service layer?**  
It keeps application rules and operations separate from the HTTP URL handling in controllers and database access in repositories.

**What is a repository?**  
It is the database access layer. `JpaRepository` supplies common operations such as `findAll`, `findById`, `save`, and `deleteById`.

**What is JPA?**  
JPA maps Java objects such as `Student` and `Department` to database tables. Hibernate is the JPA implementation used by Spring Boot here.

**What is H2?**  
H2 is the database used for this project. The configured `jdbc:h2:mem:studentdb` is an in-memory database, so data is temporary.

**What do `@Entity`, `@Id`, and `@GeneratedValue` mean?**  
`@Entity` marks a class for database mapping, `@Id` marks its primary key, and `@GeneratedValue` asks the database to generate the ID.

**What is dependency injection?**  
Spring creates required classes and passes them into other classes. The controllers receive their services through constructors; services receive repositories the same way.

**What are `@GetMapping` and `@PostMapping`?**  
They connect a controller method to an HTTP method and path. For example, `@GetMapping` handles GET and `@PostMapping` handles POST.

**Why is a student connected to a department?**  
The `@ManyToOne` relationship means many students can belong to one department. A department's `@OneToMany` is the reverse side of that relationship.

**What do `@Valid`, `@NotBlank`, and `@Email` do?**  
`@Valid` asks Spring to check annotated request fields. `@NotBlank` requires a non-empty name; `@Email` checks that a non-null email has email format. The email field is not currently marked `@NotBlank`.

**What response codes does the API use?**  
Creating a department or student returns 201 Created. Successful reads and updates return 200 OK. A successful student deletion returns 204 No Content. The exception handler returns 404 for a missing student and 400 for handled bad requests or validation failures.

**What does `@CrossOrigin(origins = "http://localhost:3000")` do?**  
It allows browser requests from a frontend served at that local origin. Postman requests do not depend on browser CORS rules.

**What does `@Transactional` do?**  
It tells Spring to run a database operation in a transaction, so related database changes are treated as one unit.

**How do I test it?**  
Start the Spring Boot application, then send the requests in `requests.http` or Postman to `localhost:8080`. Create departments first and use their returned IDs when creating students.

## Things to know honestly before presenting

- The course relationship is modeled, but there are no course endpoints or course-management service yet.
- Several repository methods are prepared but currently unused (email lookup, roll-number check). Do not say they are active duplicate checks; the service currently checks duplicate email only.
- `DepartmentService.findById` uses `IllegalArgumentException`, which the global handler maps to HTTP 400. `StudentService.findById` uses `StudentNotFoundException`, mapped to HTTP 404.
- Student validation requires a nonblank name and checks email format when present. It does not require an email or validate CGPA range.
- Departments have create, list, and get-by-ID endpoints; update and delete department endpoints are not implemented.
