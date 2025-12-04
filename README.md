# Personal Blog

A simple blog application built with Spring Boot 4 and vanilla JavaScript.
Project from: https://roadmap.sh/projects/personal-blog

## Features

- 📝 Create, edit, and delete blog articles
- 🔐 User authentication with HTTP Basic Auth
- 👤 User registration
- 🛡️ Role-based authorization (USER/ADMIN)
- 📱 Responsive design
- 💾 H2 in-memory database

## Tech Stack

**Backend:**
- Java 25
- Spring Boot 4.0.0
- Spring Security
- Spring Data JPA
- H2 Database
- Gradle

**Frontend:**
- HTML5
- CSS3
- Vanilla JavaScript
- Fetch API

## Getting Started

### Prerequisites

- Java 25
- Gradle

### Running the Application

1. Clone the repository
```bash
git clone <repository-url>
cd blog
```

2. Run the application
```bash
./gradlew bootRun
```

3. Open your browser and navigate to `http://localhost:8080`

## API Endpoints

### Public Endpoints
- `GET /home` - List all articles
- `GET /article/{id}` - Get specific article
- `POST /auth/register` - Register new user

### Authenticated Endpoints
- `GET /admin` - Get paginated articles for admin (requires ADMIN role)
- `POST /new` - Create new article
- `PUT /edit/{id}` - Edit article (owner only)
- `DELETE /delete/{id}` - Delete article (owner only)

## Default Credentials

The application uses an in-memory H2 database. You'll need to register a user first at `/auth/register`.

For ADMIN access, manually insert a user with role "ADMIN" in the database.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/personalblog/
│   │       ├── article/
│   │       │   ├── Article.java
│   │       │   ├── ArticleController.java
│   │       │   └── ArticleRepository.java
│   │       ├── user/
│   │       │   ├── User.java
│   │       │   ├── UserRepository.java
│   │       │   ├── UserService.java
│   │       │   └── DatabaseUserDetailsService.java
│   │       ├── PersonalBlogApplication.java
│   │       └── SecurityConfig.java
│   └── resources/
│       ├── static/
│       │   ├── scripts/
│       │   │   ├── admin.js
│       │   │   ├── edit.js
│       │   │   ├── home.js
│       │   │   └── new.js
│       │   ├── styles/
│       │   │   ├── admin.css
│       │   │   ├── edit.css
│       │   │   ├── home.css
│       │   │   └── new.css
│       │   ├── admin.html
│       │   ├── edit.html
│       │   ├── home.html
│       │   └── new.html
│       ├── application.properties
│       └── schema.sql
└── test/
```

## Security

- Passwords are hashed using BCrypt
- HTTP Basic Authentication for API requests
- CSRF protection disabled for REST API
- Role-based access control (USER/ADMIN)

## License

This project is open source and available under the MIT License.
