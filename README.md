# Pet Adoption Management System - Server

## Overview

This is the server component of the Pet Adoption Management System, a client-server application that facilitates pet adoption processes. The system uses clustering algorithms to match potential adopters with suitable pets based on their preferences and pet characteristics.

## Architecture

The server follows a multi-tier architecture:

1. **Controller Layer**: Handles client requests and routes them to appropriate services
2. **Service Layer**: Contains business logic for pet matching and adoption processes
3. **Data Access Layer**: Manages data persistence through file-based storage
4. **Algorithmic Module**: Implements clustering algorithms for intelligent pet-adopter matching
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 600">
  <!-- Background -->
  <rect width="800" height="600" fill="#f8f9fa"/>
  
  <!-- Title -->
  <text x="400" y="30" font-family="Arial" font-size="20" font-weight="bold" text-anchor="middle">Pet Adoption System Architecture</text>
  
  <!-- Client -->
  <rect x="100" y="80" width="200" height="120" rx="10" fill="#e6f2ff" stroke="#0066cc" stroke-width="2"/>
  <text x="200" y="110" font-family="Arial" font-size="16" font-weight="bold" text-anchor="middle">Client Application</text>
  <text x="200" y="135" font-family="Arial" font-size="12" text-anchor="middle">JavaFX UI Components</text>
  <text x="200" y="155" font-family="Arial" font-size="12" text-anchor="middle">Client-side Services</text>
  <text x="200" y="175" font-family="Arial" font-size="12" text-anchor="middle">JSON Communication</text>
  
  <!-- Server -->
  <rect x="500" y="80" width="200" height="120" rx="10" fill="#e6f2ff" stroke="#0066cc" stroke-width="2"/>
  <text x="600" y="110" font-family="Arial" font-size="16" font-weight="bold" text-anchor="middle">Server</text>
  <text x="600" y="135" font-family="Arial" font-size="12" text-anchor="middle">Socket Connection Handling</text>
  <text x="600" y="155" font-family="Arial" font-size="12" text-anchor="middle">Request Processing</text>
  <text x="600" y="175" font-family="Arial" font-size="12" text-anchor="middle">Response Generation</text>
  
  <!-- Controller Layer -->
  <rect x="500" y="230" width="200" height="80" rx="10" fill="#e6fff2" stroke="#009933" stroke-width="2"/>
  <text x="600" y="260" font-family="Arial" font-size="16" font-weight="bold" text-anchor="middle">Controller Layer</text>
  <text x="600" y="285" font-family="Arial" font-size="12" text-anchor="middle">Request Routing &amp; Processing</text>
  
  <!-- Service Layer -->
  <rect x="500" y="340" width="200" height="80" rx="10" fill="#fff2e6" stroke="#ff8c1a" stroke-width="2"/>
  <text x="600" y="370" font-family="Arial" font-size="16" font-weight="bold" text-anchor="middle">Service Layer</text>
  <text x="600" y="395" font-family="Arial" font-size="12" text-anchor="middle">Business Logic &amp; Matching</text>
  
  <!-- Algorithmic Module -->
  <rect x="260" y="340" width="180" height="80" rx="10" fill="#f2e6ff" stroke="#9933cc" stroke-width="2"/>
  <text x="350" y="370" font-family="Arial" font-size="16" font-weight="bold" text-anchor="middle">Algorithmic Module</text>
  <text x="350" y="395" font-family="Arial" font-size="12" text-anchor="middle">Clustering Algorithms</text>
  
  <!-- Data Access Layer -->
  <rect x="500" y="450" width="200" height="80" rx="10" fill="#ffe6e6" stroke="#cc0000" stroke-width="2"/>
  <text x="600" y="480" font-family="Arial" font-size="16" font-weight="bold" text-anchor="middle">Data Access Layer</text>
  <text x="600" y="505" font-family="Arial" font-size="12" text-anchor="middle">File-based Persistence</text>
  
  <!-- File Storage -->
  <rect x="540" y="560" width="120" height="20" fill="#d9d9d9" stroke="#666666" stroke-width="1"/>
  <text x="600" y="575" font-family="Arial" font-size="12" text-anchor="middle">Data Files</text>
  
  <!-- Connection Lines -->
  <!-- Client to Server -->
  <path d="M 300 140 L 500 140" stroke="#666666" stroke-width="2" stroke-dasharray="5,5"/>
  <text x="400" y="130" font-family="Arial" font-size="12" text-anchor="middle">HTTP/Socket</text>
  <text x="400" y="150" font-family="Arial" font-size="12" text-anchor="middle">JSON</text>
  
  <!-- Server to Controller -->
  <path d="M 600 200 L 600 230" stroke="#666666" stroke-width="2"/>
  <polygon points="600,230 595,220 605,220" fill="#666666"/>
  
  <!-- Controller to Service -->
  <path d="M 600 310 L 600 340" stroke="#666666" stroke-width="2"/>
  <polygon points="600,340 595,330 605,330" fill="#666666"/>
  
  <!-- Service to Algorithmic Module -->
  <path d="M 500 380 L 440 380" stroke="#666666" stroke-width="2"/>
  <polygon points="440,380 450,375 450,385" fill="#666666"/>
  
  <!-- Service to Data Access -->
  <path d="M 600 420 L 600 450" stroke="#666666" stroke-width="2"/>
  <polygon points="600,450 595,440 605,440" fill="#666666"/>
  
  <!-- Data Access to Files -->
  <path d="M 600 530 L 600 560" stroke="#666666" stroke-width="2"/>
  <polygon points="600,560 595,550 605,550" fill="#666666"/>
  
  <!-- Legend -->
  <rect x="40" y="500" width="15" height="15" fill="#e6f2ff" stroke="#0066cc" stroke-width="1"/>
  <text x="63" y="512" font-family="Arial" font-size="12" text-anchor="start">Client/Server</text>
  
  <rect x="40" y="525" width="15" height="15" fill="#e6fff2" stroke="#009933" stroke-width="1"/>
  <text x="63" y="537" font-family="Arial" font-size="12" text-anchor="start">Controller Components</text>
  
  <rect x="40" y="550" width="15" height="15" fill="#fff2e6" stroke="#ff8c1a" stroke-width="1"/>
  <text x="63" y="562" font-family="Arial" font-size="12" text-anchor="start">Service Components</text>
  
  <rect x="40" y="575" width="15" height="15" fill="#f2e6ff" stroke="#9933cc" stroke-width="1"/>
  <text x="63" y="587" font-family="Arial" font-size="12" text-anchor="start">Algorithmic Components</text>
  
  <rect x="200" y="500" width="15" height="15" fill="#ffe6e6" stroke="#cc0000" stroke-width="1"/>
  <text x="223" y="512" font-family="Arial" font-size="12" text-anchor="start">Data Access Components</text>
  
  <rect x="200" y="525" width="15" height="15" fill="#d9d9d9" stroke="#666666" stroke-width="1"/>
  <text x="223" y="537" font-family="Arial" font-size="12" text-anchor="start">Storage</text>
</svg>
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
