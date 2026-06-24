# JDBC Bank Database Viewer

A Java Swing desktop application that connects to a Microsoft Access bank database via JDBC and lets users browse and inspect its tables through a graphical interface.

## What It Does

- Connects to a Microsoft Access database (`BankDB.accdb`) using the UCanAccess JDBC driver
- Retrieves all user-defined tables in the database (system tables are filtered out)
- Presents a dropdown to select any table
- Loads the selected table's data into a JTable for viewing
- Displays connection status and total row count

## Tech Stack

| Component       | Technology                          |
|-----------------|-------------------------------------|
| Language        | Java                                |
| GUI             | Swing                               |
| Database access | JDBC via UCanAccess 5.0.1          |
| Build tool      | Maven                               |
| Database        | Microsoft Access (.accdb)           |

## Database Schema

The database contains a single user table:

### `AccountDetails`

| Column       | Type      | Description                        |
|--------------|-----------|------------------------------------|
| `info_id`    | Integer   | Primary key - account identifier   |
| `first_name` | Text      | Account holder's first name        |
| `last_name`  | Text      | Account holder's last name         |
| `balance`    | Currency  | Current account balance            |
| `debt`       | Currency  | Outstanding debt amount            |

The `BankDB.accdb` file included in this repo contains sample data.

## How to Build and Run

### Prerequisites

- Java Development Kit (JDK) 26 or later
- Maven 3.6+

### Build

```bash
mvn clean package
```

### Run

```bash
mvn exec:java
```

Or run the compiled JAR directly:

```bash
java -cp target/DatabaseAssignment-1.0-SNAPSHOT.jar;target/dependency/* com.mycompany.databaseassignment.BankDBViewer
```

> The application looks for `BankDB.accdb` in the current working directory. Run commands from the project root where the `.accdb` file is located.

## Project Structure

```
jdbc-bank-database/
├── src/
│   └── main/
│       └── java/
│           └── com/mycompany/databaseassignment/
│               ├── BankDBViewer.java      # Swing GUI - main entry point
│               └── DatabaseManager.java   # JDBC connection & query logic
├── BankDB.accdb                           # Sample Access database
├── pom.xml                                # Maven build configuration
└── README.md
```

## How It Works

1. **`DatabaseManager`** handles the JDBC connection to the Access database - it opens the connection via UCanAccess, queries table metadata, and fetches row data for a given table.

2. **`BankDBViewer`** is the Swing UI - it initializes the database connection on startup, populates a dropdown with available table names, and renders the selected table's contents in a `JTable` when the user clicks "Load Table".
