# Java Project

Nom: MAOUCHI            
Prenom: Melina 


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
- Create a new database named `melina_project`.
- Update the database connection details in the application configuration file (utils).


### 1. Navigate to the Project Directory

```bash
cd melinamaouchii
```
### 2. Build the Project Using Maven

Run the following command to clean and package the project:

```bash
mvn clean package
```
This will generate a JAR file in the target directory:
Melina-jar-with-dependencies.jar
```bash
java --module-path "C:\Users\melin\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar target/Melina-jar-with-dependencies.jar
```

## Project Structure
The project is organized as follows:

```bash
JavaFx-project/
├── out/
│   ├── artifacts/
│   │   
│   │                                  
├── src/
│   ├── main/
│       ├── java/                  
│       │   ├── melina.maouchi.javafxproject/
│       │   │   ├── config/   
│       │   │   ├── controllers/ 
│               ├── models /   
│       │   │   ├── interfaces/
│       │   │   ├── utils/              
│       │   │         
│       └── resources/
│            └── views/            
│                        
├── target/
│    ├── Melina.jar
│    ├── Melina-jar-with-dependecies
│     
├── pom.xml                         
└── README.md                      
