# Todo Application

A Spring Boot application for managing daily todos with a calendar view.

## Features

- Add, edit, delete, and mark todos as complete
- Calendar view for date selection
- Filter todos by date
- Responsive design with Bootstrap
- PostgreSQL database

## Technologies Used

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Bootstrap 5
- FullCalendar

## Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL

## Database Setup

1. Install PostgreSQL on your system
2. Create a new database:
```sql
CREATE DATABASE tododb;
```

3. Update the database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/YOUR_USERNAME/todoapp.git
```

2. Navigate to the project directory:
```bash
cd todoapp
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

5. Access the application:
- Main application: http://localhost:8080/todos

## Usage

1. View all todos on the main page
2. Use the calendar to select a specific date
3. Add new todos using the form on the left
4. Edit or delete existing todos
5. Mark todos as complete/incomplete

## License

This project is licensed under the MIT License. 