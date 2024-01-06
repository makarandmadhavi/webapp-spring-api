# Spring Boot Application README

This README provides instructions for building, running, and testing a Spring Boot application using Java and Gradle. The application is designed to work with a MariaDB database, and it includes integration tests using Karate. Additionally, you can configure environment options through the `application.properties` file to set the database username, password, URL, and database name.

Demo7

## Prerequisites
test file change
Before you begin, ensure you have the following tools installed on your system:

- Java Development Kit (JDK) 17 or higher
- Gradle (https://gradle.org/)
- Docker (https://www.docker.com/)

## Getting Started

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/yourusername/spring-boot-app.git
   cd spring-boot-app
   ```

2. Configure the Database

    - Open the `application.properties` file located in the `src/main/resources` directory.
    - Modify the following properties to match your database setup:

      ```properties
      spring.datasource.url=jdbc:mariadb://localhost:3306/yourdb
      spring.datasource.username=yourusername
      spring.datasource.password=yourpassword

      ```
      Specify the complete file location to load users into accounts table
      ```properties
      application.config.users-csv-path= users.csv
      ```

      Replace `yourdb`, `yourusername`, and `yourpassword` with your actual database information.

## Building the Application

To build the Spring Boot application, use the following Gradle command:

```bash
gradle build
```

## Running the Application

You can run the application using Gradle or Docker Compose.

### Using Gradle

To run the application using Gradle, execute the following command:

```bash
gradle bootRun
```
Make sure the database is up before starting the application or it will fail to start throwing communication error

The application will start, and you can access it at `http://localhost:8080`. Make sure your MariaDB database is running and configured correctly as per the `application.properties` file.

### Using Docker Compose

A Docker Compose file is provided to start the MariaDB database required by the application. To use Docker Compose, follow these steps:

1. Ensure Docker is installed and running.

2. In the project root directory, execute the following command to start the database and the Spring Boot application:

   ```bash
   docker-compose up
   ```

   This will start the MariaDB database


## Testing the Application

This application includes integration tests using Karate. You can run these tests using Gradle:

```bash
gradle test
```

## SSL Certificate

To import SSL certificate in aws certificate manager use the following command 

```bash
aws acm import-certificate --profile <aws profile> \
  --certificate fileb://<certificate file> \
  --private-key fileb://<private key file>
```

For detailed documentation on writing and running Karate tests, refer to the [Karate documentation](https://github.com/intuit/karate).

## Docker Compose File Description

The `docker-compose.yml` file included in this repository is responsible for starting the MariaDB database container required by the application. For more information on Docker Compose, refer to the [Docker Compose documentation](https://docs.docker.com/compose/).

## License

This Spring Boot application is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

Test
Feel free to contribute to this project and improve its functionality. If you encounter any issues or have questions, please create an issue on the GitHub repository. Happy coding!
