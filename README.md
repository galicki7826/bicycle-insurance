# Bicycle Insurance Premium Calculation Service

## Overview

This project provides a REST API for calculating insurance premiums for bicycles based on various risk factors. The service allows users to submit bicycle details, including make, model, year of manufacture, coverage type, and selected risks, to calculate a premium based on defined rules and Groovy scripts.

## Technologies Used

- **Java 17**: The primary programming language for the application, chosen for its performance, modern language features, and extensive support for building scalable services.
- **Spring Boot**: A framework that simplifies application setup, configuration, and deployment. Spring Boot provides robust support for building RESTful APIs and managing dependencies.
- **Spring Validation (Jakarta Validation)**: Used for validating incoming data in API requests, ensuring data integrity before processing.
- **Groovy**: A dynamic language that integrates seamlessly with Java, used here for writing flexible and customizable risk calculation scripts. This enables quick updates to business logic without altering the core Java code.
- **MapStruct**: A Java annotation-based code generator used for mapping between DTOs and entities. MapStruct simplifies object mapping, improving code readability and reducing boilerplate code.
- **Lombok**: A Java library that reduces boilerplate code for common methods like getters, setters, and constructors, making the code cleaner and more maintainable.
- **JUnit 5**: A modern testing framework for writing and running unit tests, providing comprehensive support for writing parameterized and assertion-based tests.
- **Mockito**: A mocking framework used alongside JUnit for creating mock objects in tests, allowing for isolated testing of each component without dependencies on external systems.
- **Spring Boot Test (MockMvc)**: A part of Spring's testing library that allows for testing of the REST API layer in isolation, using MockMvc to simulate HTTP requests and validate responses.
- **Swagger (OpenAPI)**: Provides interactive API documentation, making it easy for developers to understand and test the API. Swagger generates a UI for exploring endpoints, request structures, and response formats.
- **Docker**: Containerization tool used to package the application and its dependencies, ensuring consistent behavior across environments. Docker simplifies deployment and scaling of the application.
- **Maven**: A build automation tool used for managing project dependencies, building the application, and running tests. Maven ensures consistent dependency management and versioning.
- **Git**: Version control system used for tracking code changes, collaboration, and maintaining a history of the project.


## Approach

The service is built with a modular approach, separating core functionality into distinct classes to improve maintainability and flexibility. Key classes include:
- `InsuranceService`: Handles the main premium calculation logic by orchestrating data flow and interactions with Groovy scripts.
- `GroovyScriptEngine`: Executes Groovy scripts that calculate individual risk premiums for each bicycle.

## Groovy and Script Management

The premium calculation is powered by Groovy scripts, allowing flexible risk factor handling and easy updates without modifying core Java code. Each script calculates the premium for a specific risk type, making it easy to extend the logic by adding or updating scripts.

### `BaseScript` Extension

The project extends the `BaseScript` class to create a custom base class for Groovy scripts used in premium calculations. This provides a shared base that includes utilities, smoother parameter handling, improved readability, and centralized error handling across scripts. By standardizing functionality in the base class, the project enables consistent script behavior and makes it easier to handle complex calculations.

## Project Structure

- `controller`: Contains the `InsuranceController` for handling HTTP requests and routing them to appropriate services.
- `dto`: Defines Data Transfer Objects (DTOs) for requests and responses.
- `service`: Houses business logic, including premium calculations and script execution.
- `scripts`: Directory containing Groovy scripts for calculating premiums based on specific risks.
- `config`: Contains configuration files and beans.

## How to Run

### Prerequisites
- Java 17
- Maven

### Steps

1. Clone the repository.
2. Run `mvn clean install` to build the project.
3. Run `docker-compose up --build` to start the application.

The API is available at `http://localhost:8080/api/v1/calculate`.

## API Documentation

Swagger is used for API documentation, which can be accessed at:

http://localhost:8080/swagger-ui.html


### Endpoints

#### POST /api/v1/calculate
Calculates the total premium for a list of bicycles.

**Request Body**:
```json
{
  "bicycles": [
    {
      "make": "string",
      "model": "string",
      "coverage": "EXTRA or STANDARD",
      "manufactureYear": 2020,
      "sumInsured": 1000,
      "risks": ["THEFT", "DAMAGE"]
    }
  ]
}
```

## Responses

- **200 OK**: Returns a JSON with the total premium and a breakdown for each bicycle.
- **400 Bad Request**: Indicates invalid data or missing required fields.
- **500 Internal Server Error**: An unexpected error occurred on the server.

## Testing Strategy

- **Unit Tests**: Each individual component, such as `InsuranceService` and `GroovyScriptEngine`, is tested in isolation to verify their specific functionality.
- **Integration Tests**: Integration tests verify the API endpoints and ensure that the interactions between controllers and services are functioning as expected.
- **Mocking**: Mockito is used extensively to mock dependencies, isolating components during tests and ensuring that only the logic under test is verified.


## Future Improvements

- **Dynamic Risk Type Addition**: Consider allowing dynamic addition of risk types via configuration files or a database, rather than relying on file-based Groovy scripts.
- **Enhanced Error Handling**: Add more detailed error responses and specific HTTP status codes to cover different types of validation and processing errors.
- **Extended Documentation**: Expand API documentation to include more example requests and responses for each endpoint, and potentially include user guides for configuring custom risk calculations.

## API Documentation

API documentation is available through Swagger, which can be accessed at:

http://localhost:8080/swagger-ui.html


This documentation provides details on the available endpoints, expected request and response formats, and example values for testing.

## Additional Notes

### Extended BaseScript for Groovy Scripts
To streamline and standardize Groovy script execution for premium calculations, an extended base class, `BaseScript`, was created. This custom base script enhances Groovy script functionality by providing:
1. **Shared Utilities**: Common utility methods that are frequently needed across multiple scripts.
2. **Parameter Handling**: Simplified and consistent handling of input parameters, reducing boilerplate code in individual scripts.
3. **Centralized Error Handling**: Ensures consistent error handling across all scripts, allowing each individual script to focus solely on its calculation logic.

The use of `BaseScript` enables each Groovy script to concentrate on risk-specific premium calculation, while leveraging a shared set of utilities and error handling mechanisms. This approach reduces code duplication and improves maintainability as more risk types and scripts are added.

## Conclusion

The Bicycle Insurance Service is designed with extensibility and flexibility in mind. By utilizing Groovy scripts for risk-specific calculations and extending `BaseScript` for standard utilities, the service allows for quick updates and modifications to business logic without changing the core application code. The structured testing approach ensures reliability, and the Swagger documentation provides a clear interface for clients to interact with the API.


