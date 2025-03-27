# Pet Adoption Management System - Server

## Overview

This is the server component of the Pet Adoption Management System, a client-server application that facilitates pet adoption processes. The system uses clustering algorithms to match potential adopters with suitable pets based on their preferences and pet characteristics.

## Architecture

The server follows a multi-tier architecture:

1. **Controller Layer**: Handles client requests and routes them to appropriate services
2. **Service Layer**: Contains business logic for pet matching and adoption processes
3. **Data Access Layer**: Manages data persistence through file-based storage
4. **Algorithmic Module**: Implements clustering algorithms for intelligent pet-adopter matching

![צילום מסך 2025-03-27 133200](https://github.com/user-attachments/assets/a4268da7-3fb3-4a99-9367-0886893e310f)



## Key Components

### Data Models (`dm` package)
- `Pet.java`: Represents a pet with attributes like species, breed, age, and gender
- `Adopter.java`: Represents a potential pet adopter with their preferences
- `Adoption.java`: Tracks the adoption process between a specific pet and adopter

### Controllers (`controller` package)
- `PetController.java`: Handles pet management operations
- `AdopterController.java`: Manages adopter profiles
- `AdoptionController.java`: Processes adoption requests and status updates
- `ControllerFactory.java`: Factory pattern implementation to create appropriate controllers
- `IController.java`: Interface defining common controller operations

### Data Access Objects (`dao` package)
- `PetFileDao.java`: File-based storage for pet data
- `AdopterFileDao.java`: File-based storage for adopter profiles
- `AdoptionFileDao.java`: File-based storage for adoption records
- `IDao.java`: Interface defining common data access operations

### Services (`service` package)
- `PetMatchingService.java`: Core service implementing pet-adopter matching logic
- `AdoptionService.java`: Manages the adoption lifecycle
- `ServiceLocator.java`: Service locator pattern implementation

### Server Components (`server` package)
- `Server.java`: TCP server implementation that listens for client connections
- `HandleRequest.java`: Processes individual client requests
- `Request.java`: Represents client requests
- `Response.java`: Represents server responses
- `ServerDriver.java`: Entry point for starting the server

### Clustering Algorithm (`AlgoClustering` package)
- `IAlgoClustering.java`: Interface for clustering algorithms
- `KMeansClusteringAlgo.java`: Implementation of K-Means clustering algorithm
- `HierarchicalClusteringAlgo.java`: Implementation of Hierarchical clustering algorithm

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Required libraries:
  - GSON (for JSON serialization/deserialization)
  - JUnit (for testing)

### Configuration
The server uses the following files in the `resources` directory for data persistence:
- `datasource_pets.txt`: Storage for pet data
- `datasource_adopters.txt`: Storage for adopter profiles
- `datasource_adoptions.txt`: Storage for adoption records

### Running the Server
1. Compile the project
2. Run the `ServerDriver` class
3. The server will start listening for client connections on port 34567

## API Endpoints

The server exposes the following API endpoints:

### Pet Management
- `pet/add`: Add a new pet
- `pet/update`: Update pet information
- `pet/get`: Retrieve pet details
- `pet/getAll`: Retrieve all pets
- `pet/delete`: Remove a pet
- `pet/match`: Find matching pets for an adopter

### Adopter Management
- `adopter/add`: Add a new adopter
- `adopter/update`: Update adopter information
- `adopter/get`: Retrieve adopter details
- `adopter/getAll`: Retrieve all adopters
- `adopter/delete`: Remove an adopter

### Adoption Management
- `adoption/create`: Create a new adoption request
- `adoption/update`: Update adoption status
- `adoption/complete`: Complete an adoption
- `adoption/get`: Retrieve adoption details
- `adoption/getAll`: Retrieve all adoptions
- `adoption/getByStatus`: Retrieve adoptions by status

## Communication Protocol

The server uses a JSON-based protocol for communication with clients. All requests and responses follow this format:

### Request Format
```json
{
  "headers": {
    "action": "controller/method"
  },
  "body": {
    // Request data
  }
}
```

### Response Format
```json
{
  "success": true/false,
  "message": "Operation completed successfully",
  "data": {
    // Response data
  }
}
```

## Testing

The server includes extensive testing classes in the `test` package:
- `ClusteringTests.java`: Tests for the clustering algorithms
- `AdoptionServiceTest.java`: Tests for the adoption service
- `PetMatchingServiceTest.java`: Tests for the pet matching service
- `TestClient.java`: A test client to verify server functionality

## Design Patterns

The system implements several design patterns:
- **Strategy Pattern**: Used in the clustering algorithms to allow different implementations
- **Factory Pattern**: Used in the controller layer to create appropriate controllers
- **Repository Pattern**: Used in the data access layer to abstract data storage
- **MVC Pattern**: Separation of model, view (client-side), and controller
- **Service Locator Pattern**: Used to locate and provide services throughout the application

## Contributors

This project was developed as part of the Programming in Internet Environments course at HIT - Holon Institute of Technology.
