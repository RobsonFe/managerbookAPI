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

# Manager Books API

## Description

Manager Books API is a system that provides endpoints to manage books and users. The API allows you to create, update, list, delete, and fetch users and books by ID or name. Below is a list of available endpoints, their descriptions, and how to interact with them.

## Base URL

- **Server URL:** `http://localhost:8080`

## API Endpoints

### Users Endpoints

#### 1. Create a New User

- **URL:** `/api/v1/users/criar`
- **Method:** `POST`
- **Description:** Adds a new user to the system.
- **Request Body:**
  - `UserDTO` (contains `username`, `email`, `password`, and `confirmPassword`).
- **Responses:**
  - `201`: User created successfully.
  - `400`: Invalid data provided.
  - `500`: Server error.

#### 2. Update a User

- **URL:** `/api/v1/users/atualizar/{id}`
- **Method:** `PUT`
- **Description:** Updates information for an existing user.
- **Path Parameters:**
  - `id` (integer): ID of the user.
- **Request Body:**
  - `UserDTO`.
- **Responses:**
  - `200`: User updated successfully.
  - `400`: Invalid data provided.
  - `404`: User not found.
  - `500`: Server error.

#### 3. List All Users

- **URL:** `/api/v1/users/listar`
- **Method:** `GET`
- **Description:** Returns a list of all users.
- **Query Parameters:**
  - `page` (optional, integer): Page number (default: 0).
  - `size` (optional, integer): Number of records per page (default: 5).
- **Responses:**
  - `200`: List of users returned successfully.
  - `500`: Server error.

#### 4. Get a User by ID

- **URL:** `/api/v1/users/buscar/{id}`
- **Method:** `GET`
- **Description:** Returns the details of a specific user based on the provided ID.
- **Path Parameters:**
  - `id` (integer): ID of the user.
- **Responses:**
  - `200`: User found.
  - `404`: User not found.
  - `500`: Server error.

#### 5. Delete a User

- **URL:** `/api/v1/users/deletar/{id}`
- **Method:** `DELETE`
- **Description:** Removes a user from the system by ID.
- **Path Parameters:**
  - `id` (integer): ID of the user.
- **Responses:**
  - `204`: User deleted successfully.
  - `404`: User not found.
  - `500`: Server error.

---

### Books Endpoints

#### 1. Create a New Book

- **URL:** `/api/v1/books/criar`
- **Method:** `POST`
- **Description:** Adds a new book to the library.
- **Request Body:**
  - `BookDTO` (contains book details).
- **Responses:**
  - `201`: Book created successfully.
  - `400`: Invalid data provided.
  - `500`: Server error.

#### 2. Update a Book

- **URL:** `/api/v1/books/atualizar/{id}`
- **Method:** `PUT`
- **Description:** Updates information for an existing book.
- **Path Parameters:**
  - `id` (integer): ID of the book.
- **Request Body:**
  - `BookDTO`.
- **Responses:**
  - `200`: Book updated successfully.
  - `400`: Invalid data provided.
  - `404`: Book not found.
  - `500`: Server error.

#### 3. List All Books

- **URL:** `/api/v1/books/listar`
- **Method:** `GET`
- **Description:** Returns a list of all books in the library.
- **Query Parameters:**
  - `page` (optional, integer): Page number (default: 0).
  - `size` (optional, integer): Number of records per page (default: 5).
- **Responses:**
  - `200`: List of books returned successfully.
  - `500`: Server error.

#### 4. Get a Book by ID

- **URL:** `/api/v1/books/buscar/{id}`
- **Method:** `GET`
- **Description:** Returns the details of a specific book based on the provided ID.
- **Path Parameters:**
  - `id` (integer): ID of the book.
- **Responses:**
  - `200`: Book found.
  - `404`: Book not found.
  - `500`: Server error.

#### 5. Get a Book by Name

- **URL:** `/api/v1/books/find/{name}`
- **Method:** `GET`
- **Description:** Returns the details of a specific book based on the provided name.
- **Path Parameters:**
  - `name` (string): Name of the book.
- **Responses:**
  - `200`: Book found.
  - `404`: Book not found.
  - `500`: Server error.

#### 6. Delete a Book

- **URL:** `/api/v1/books/deletar/{id}`
- **Method:** `DELETE`
- **Description:** Removes a book from the library by ID.
- **Path Parameters:**
  - `id` (integer): ID of the book.
- **Responses:**
  - `204`: Book deleted successfully.
  - `404`: Book not found.
  - `500`: Server error.

---

## Data Models

### UserDTO

- `username` (string): Username of the user.
- `email` (string): Email of the user.
- `password` (string): User's password.
- `confirmPassword` (string): Confirmation of the password.

### BookDTO

- `title` (string): Title of the book.
- `author` (string): Author of the book.
- `publicationDate` (string): Publication date of the book.

### MessageResponseDTO

- `message` (string): Response message.

## License

This project is licensed under the [MIT License](LICENSE).

---

## Contact Information

- **Author:** [Robson Ferreira](https://github.com/RobsonFe)
- **Email:** [robson-ferreiradasilva@hotmail.com](mailto:robson-ferreiradasilva@hotmail.com)

## License

This project is licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0).
