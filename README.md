# Color of Apes - Favorite Color Registration App

A Spring Boot web application for registering participants' favorite colors in a networking session. The application features a mobile-friendly UI and can be integrated with Arduino LED visualization.

## Features

- Mobile-friendly web interface
- Register participant name and favorite color
- View all registered participants and their colors
- Color preview functionality
- H2 in-memory database
- Ready for Arduino LED integration

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Open a web browser and visit: http://localhost:8080

## Database Access

The H2 console is available at: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:colordb
- Username: sa
- Password: (leave empty)

## Arduino Integration

The application is designed to be integrated with Arduino LED visualization. The Arduino integration code will be provided separately.
