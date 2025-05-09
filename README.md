# JG Cosmetics Store

A full-stack application for a cosmetics store built with Spring Boot and a frontend using HTML, CSS, and JavaScript.

##  Project Structure

```text
src/
├── main/
│   ├── java/com/jgcosmetics/store/    # Java backend (controllers, services, models)
│   └── resources/
│       ├── static/                    # HTML, CSS, JS frontend files
│       └── application.properties     # Spring Boot config
└── test/                              # Unit and integration tests
```

## Features

- User Sign-Up & Login
- Product listing and dynamic product details
- Cart with support for guests and logged-in users
- Checkout system
- Address management
- Order placement

## Technologies Used

- **Backend:** Java, Spring Boot, JPA, MySQL
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL (XAMPP)
- **Containerization:** Docker
- **Deployment:** Google Cloud Platform (GCE)

## How to Run Locally

1. **Clone the repo**:
   ```bash
   git clone https://github.com/faye-fayee/J-G-Cosmetics-Backend.git
   cd your-repo-name
   ```
2. **Setup the Database**:
   - Create a MySQL database and configure application.properties.
   - Run the SQL scripts if needed.
3. **Build the Project**:
   ```bash
   mvn clean install
   ```
5. **Access in Browser**:
   http://localhost:8080

## Docker Support

To run with Docker:
```bash
docker-compose --build
```
Make sure your application-docker.properties is correctly configured.

## License

This project is open for educational and personal use.  
Feel free to explore, learn from, and modify the code.  
Attribution is appreciated, but not required.
