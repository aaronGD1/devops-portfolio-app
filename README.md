# 🚀 Personal Portfolio Website

A full-stack personal portfolio built with **Spring Boot + Maven**, version-controlled with **Git**, and deployed via **Jenkins CI/CD**.

---

## 🧱 Tech Stack

| Layer       | Technology                                 |
|-------------|---------------------------------------------|
| Backend     | Java 17, Spring Boot 3.2, Spring Data JPA  |
| Frontend    | Thymeleaf, HTML5, CSS3, JavaScript         |
| Build Tool  | **Apache Maven**                            |
| Version Ctrl| **Git**                                     |
| CI/CD       | **Jenkins** (Pipeline-as-Code via Jenkinsfile) |
| Database    | H2 (dev) — swap to MySQL/PostgreSQL (prod) |

---

## ✨ Features

- 📸 Upload profile photo
- 📄 Upload and download resume (PDF/DOC)
- 🎓 Show CGPA, college, degree, graduation year
- ⚡ Skills displayed as interactive tags
- 🛠️ Projects with name + description
- 🏆 Certifications & Achievements
- 🔗 LinkedIn, GitHub, portfolio links
- 🌐 Clean, responsive design
- ✏️ In-browser edit form (no DB client needed)

---

## ⚙️ Prerequisites

- Java 17+
- Maven 3.8+
- Git
- Jenkins (for CI/CD)

---

## 🚀 Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/portfolio-app.git
cd portfolio-app

# 2. Build with Maven
mvn clean install

# 3. Run
mvn spring-boot:run

# 4. Open browser
open http://localhost:8080
```

First run opens a **setup page** — click "Create My Portfolio" and fill in your details!

---

## 🔧 Git Setup

```bash
# Initialize repo (if not cloned)
git init
git add .
git commit -m "feat: initial portfolio project"

# Push to GitHub/GitLab/Bitbucket
git remote add origin https://github.com/YOUR_USERNAME/portfolio-app.git
git branch -M main
git push -u origin main
```

---

## 🏗️ Jenkins Setup

### 1. Install Jenkins
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install jenkins
sudo systemctl start jenkins
# Access: http://localhost:8080
```

### 2. Required Jenkins Plugins
- Pipeline
- Git Plugin
- Maven Integration
- JUnit Plugin
- Workspace Cleanup

### 3. Create Pipeline Job
1. New Item → **Pipeline**
2. Under Pipeline → Definition: **Pipeline script from SCM**
3. SCM: **Git** → enter your repo URL
4. Script Path: `Jenkinsfile`
5. Save → **Build Now**

### Jenkins Pipeline Stages
```
📥 Checkout → 🔍 Validate → 📦 Build → 🧪 Tests → 📊 Package → 🚀 Deploy
```

---

## 🗄️ Switch to MySQL (Production)

1. Add MySQL dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/portfoliodb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.h2.console.enabled=false
```

---

## 📁 Project Structure

```
portfolio-app/
├── Jenkinsfile                          ← Jenkins CI/CD pipeline
├── pom.xml                              ← Maven build config
├── src/
│   ├── main/
│   │   ├── java/com/portfolio/
│   │   │   ├── PortfolioApplication.java
│   │   │   ├── controller/PortfolioController.java
│   │   │   ├── model/Portfolio.java
│   │   │   ├── repository/PortfolioRepository.java
│   │   │   └── service/PortfolioService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── index.html    ← Portfolio display
│   │           ├── form.html     ← Edit form
│   │           └── setup.html   ← First-time setup
│   └── test/
│       └── java/com/portfolio/
│           └── PortfolioApplicationTests.java
└── uploads/                             ← User uploaded files
```

---

## 📜 License
MIT
