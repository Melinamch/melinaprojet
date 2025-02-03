# JavaFX Project - Product Sales Management System

This is a JavaFX-based application designed to manage product sales. It provides functionalities such as product catalog management, order processing, client management, and billing. The application connects to a PostgreSQL database for data storage and retrieval.

## Prerequisites

Before running the project, ensure you have the following installed on your system:

- **Java Development Kit (JDK) 22**: The project is built using Java 22. You can download the JDK from the [official Oracle website](https://www.oracle.com/java/technologies/javase-downloads.html).
- **Apache Maven**: Maven is used for building and managing the project. Download it from the [Apache Maven website](https://maven.apache.org/download.cgi).
- **PostgreSQL**: The application uses PostgreSQL as the database. Download and install PostgreSQL from the [official PostgreSQL website](https://www.postgresql.org/download/).
- **Git** (optional): If you plan to clone the repository, you’ll need Git. Download it from the [Git website](https://git-scm.com/downloads).

## Setup

### 1. Clone the Repository
If you are using Git, clone the repository to your local machine:

## Set Up the PostgreSQL Database

- Install PostgreSQL and ensure the PostgreSQL service is running.
- Create a new database named `java_fx_project`.
- Update the database connection details in the application configuration file (if applicable).

## Project Structure

The project is organized as follows:

```bash
JavaFx-project/
├── out/
│   ├── artifacts/
│   │   └── JavaFx_project_jar2/
│   │       └── JavaFx-project.jar   # JAR file                          
├── src/
│   ├── main/
│   │   ├── java/                  # Java source files
│   │   │   ├── com.wora.javafxproject/
│   │   │   │   ├── config/   
│   │   │   │   ├── controllers/   
│   │   │   │   ├── models/
│   │   │   │   ├── utils/              
│   │   │   │   ├── repositories/      
│   │   └── resources/
│   │         └── views/            
│   └── test/                      
├── target/
├── pom.xml                         
└── README.md                      
