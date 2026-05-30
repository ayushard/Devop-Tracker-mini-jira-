
# DevOps Project Management Dashboard

A production-style full-stack DevOps project management dashboard built with Spring Boot, React.js, and MySQL. The application provides lightweight Jira-style project planning, role-based access, task tracking, comments, attachments, activity logs, and a modern dashboard experience.

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Maven
- MySQL

### Frontend
- React.js with Vite
- React Router
- Axios
- Tailwind CSS
- Context API

## Features

- User registration and login with JWT authentication
- Role-based authorization for Admin and Developer users
- Admin project and task CRUD flows
- Developer task status updates, comments, and attachments
- Dashboard metrics and project progress chart
- Activity logs
- Search, filters, pagination, loading states, and API response standardization
- DTO-based REST API design with centralized exception handling
- Seeded demo data for immediate testing

## Demo Credentials

- Admin: `admin@devops.local` / `Admin@123`
- Developer: `developer1@devops.local` / `Dev@123`

## Folder Structure

```text
devop jira/
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/devops/dashboard/
│           │   ├── config/
│           │   ├── controller/
│           │   ├── dto/
│           │   ├── entity/
│           │   ├── exception/
│           │   ├── repository/
│           │   ├── security/
│           │   └── service/
│           └── resources/
│               ├── application.properties
│               └── schema.sql
├── frontend/
│   ├── package.json
│   ├── tailwind.config.js
│   ├── vite.config.js
│   └── src/
│       ├── components/
│       ├── context/
│       ├── hooks/
│       ├── pages/
│       ├── services/
│       └── utils/
└── README.md
```

## Backend Architecture

The Spring Boot API follows a layered structure:

- `controller/`: REST endpoints
- `service/`: business logic contracts and implementations
- `repository/`: JPA data access
- `dto/`: request and response payloads
- `entity/`: persistence models
- `security/`: JWT and authentication logic
- `config/`: CORS, security, and bootstrap seed configuration
- `exception/`: custom exceptions and global exception handling

## Database Schema

The schema is provided in [backend/src/main/resources/schema.sql](/C:/Users/AYUSH/Documents/devop%20jira/backend/src/main/resources/schema.sql).

### Main Tables

- `users`
- `user_roles`
- `projects`
- `tasks`
- `task_comments`
- `task_attachments`
- `activity_logs`

## API Endpoints

### Authentication
- `POST /api/auth/register`
- `POST /api/auth/login`

### Projects
- `GET /api/projects`
- `POST /api/projects`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`

### Tasks
- `GET /api/tasks`
- `POST /api/tasks`
- `PUT /api/tasks/{id}`
- `DELETE /api/tasks/{id}`
- `PUT /api/tasks/{id}/status`
- `POST /api/tasks/{id}/comments`
- `POST /api/tasks/{id}/attachments`

### Users
- `GET /api/users`
- `GET /api/users/developers`

### Dashboard
- `GET /api/dashboard/stats`
- `GET /api/dashboard/activities`

## Setup Instructions

### 1. Create the MySQL database

```sql
CREATE DATABASE devops_dashboard;
```

### 2. Configure backend credentials

Update [backend/src/main/resources/application.properties](/C:/Users/AYUSH/Documents/devop%20jira/backend/src/main/resources/application.properties) if your MySQL username, password, or host differ from the defaults:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

The API will start at [http://localhost:8080](http://localhost:8080).

### 4. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

The React app will start at [http://localhost:5173](http://localhost:5173).

## Sample Workflow

1. Log in as Admin.
2. Create or edit projects.
3. Create tasks and assign them to developers.
4. Log in as a developer and update task status.
5. Add task comments and upload attachments.
6. Monitor progress from the dashboard.

## Production Notes

- Replace the JWT secret in `application.properties` before deploying.
- Move uploaded files to cloud or object storage for production use.
- Add refresh tokens, stronger audit detail, and automated tests for a hardened release.
- Consider Docker Compose for local orchestration if you want one-command startup.

## Verification

The following checks were run successfully in this workspace:

- `mvn -q -DskipTests compile`
- `mvn -q -DskipTests test`
- `npm install`
- `npm run build`

# Devop-Tracker-mini-jira-

