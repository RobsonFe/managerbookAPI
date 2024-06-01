# Manager Book API

## Description

The Manager Book API is a project developed by me using Spring Boot, Spring JPA, Swagger, Docker, and MySQL. This API serves as a management system for books, allowing users to perform CRUD operations on authors and books.

## Technologies Used

- Java JDK 21
- Spring Boot 3
- MySQL
- Swagger with OpenAPI
- Docker

## Features

- **Spring Boot**: Provides a robust framework for building Java applications.
- **Spring JPA**: Simplifies data access with Spring applications using the Java Persistence API (JPA).
- **Swagger with OpenAPI**: Enables interactive API documentation and exploration.
- **Docker**: Facilitates containerization and deployment of applications.
- **MySQL**: Offers a reliable relational database management system for data storage and retrieval.

## Installation

To run this project locally, make sure you have Java JDK 21 and Docker installed. Follow these steps:

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd ManagerBookAPI
   ```

2. Build the Docker image:
   ```bash
   docker-compose build
   ```

3. Start the application:
   ```bash
   docker-compose up
   ```

4. Access the API documentation:
   Open your web browser and go to `http://localhost:8080/swagger-ui/index.html`.

## Getting Started

Once the application is running, you can use Swagger UI to explore and interact with the API endpoints. Here are some basic operations you can perform:

- Create, read, update, and delete authors and books.
- Use the provided API documentation to understand each endpoint and its parameters.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your suggested changes.

## License

This project is licensed under the [MIT License](LICENSE).

---

## Author
[Robson Ferreira](https://github.com/RobsonFe)
