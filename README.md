# Ingress Jobs - Job Portal Backend

Welcome to **Ingress Jobs**, a powerful backend system designed for managing and retrieving structured job listings. Built with **Spring Boot**, this system scrapes job data from external sources and provides a robust API for querying job opportunities based on various filters.

---

## 🚀 Features
- Full-featured Job Listing API (CRUD)
- Filtering by location, job type, experience, industry, and keywords
- Sorting by posted date and salary
- Batch job loading from Djinni.co (scraper included)
- Enum-based categorization (Job Type, Experience Level, etc.)
- Structured DTOs for safe API interaction
- Modular project structure with clear responsibilities

---

## 🛠 Tech Stack
- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Jsoup** for web scraping
- **Lombok** for reducing boilerplate code
- **Maven** for project management

---

## 🧑‍💻 Getting Started

### Prerequisites
- Java 21+
- Maven
- PostgreSQL (or compatible DB)

### Setup Instructions
1. **Clone the repository**
```bash
git clone <your-repo-url>
cd job-portal-backend
```

2. **Configure the database**
   Edit `src/main/resources/application.yaml` and set your DB credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/job_portal
    username: your_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

3. **Run the application**
```bash
mvn spring-boot:run
```

API will be available at `http://localhost:8080/api/jobs`

---

## 🐳 Docker (optional)
*(To be added if Docker support is implemented)*

1. Create `Dockerfile` and `docker-compose.yml`
2. Run:
```bash
docker-compose up --build
```

---

## 📡 API Endpoints

### 📋 Get All Jobs
`GET /api/jobs`

### 🔎 Filter Jobs
`GET /api/jobs`
Request Body:
```Param
http://localhost:8080/api/v1/jobs?experienceLevels=MID&locations=Baku&sortByPostDate=true&sortInDescendingOrder=true&tags=1
```

### 🆕 Add a Job
`POST /api/jobs`

## 🕸 Djinni Job Scraper
- Scrapes jobs from https://djinni.co/jobs/
- Filters only **Remote** or **Azerbaijan**  jobs
- Converts them into internal job format
- Loads them via `JobLoader`

---

## 📂 Project Structure
```
src/main/java/org/ea/jobportalbackend
├── config          # Web config
├── controller      # API controllers
├── dto             # Request/response models
├── loader          # Djinni job scraper
├── mapper          # DTO ↔ Entity mappers
├── model           # Domain models
├── repository      # Spring Data JPA repos
├── service         # Business logic
```

---

## ✅ Testing
Basic Spring Boot test exists in `JobPortalBackendApplicationTests.java`.
Consider adding unit & integration tests for services and controllers.

---

## 📋 Logging
Consider adding `@Slf4j` to key services for production logging.

---

## 🤝 Contributing
Feel free to fork the repo and open pull requests. All improvements welcome!

---

## 📄 License
This project is open-sourced for educational use.

---

> ✨ **Created as part of the Ingress Bootcamp Assignment – April 2025**
