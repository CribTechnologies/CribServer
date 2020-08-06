# Crib Server

This is the server-side application for Crib, which runs on Java and Spring Boot. The server is designed with an n-tier, monolithic architecture:
- Web Services Layer - WBL (Controllers)
- Business Logic Layer - BLL (Services)
- Data Access Layer - DAL (Repositories)

With Domain-Driven Design and Design Patterns in mind, the BLL is database platform-agnostic, or in other words, oblivious to the database platform used. The BLL accesses the DAL indirectly through a `RepositoryFactory`, which provides the platform-specific implementation for a specific repository.

## Web Services Layer
The web services layer's basic functions include:
- Mapping routes to methods
- Converting JSON request data to domain DTOs
- Performing data validation and cleaning

Each controller in the WSL contains a `@RestController` decorator from Spring Boot. Each method in each controller is mapped from a route and receives a validated custom `Request` object (which inherits `ControllerRequest`). It then returns a custom `Response` object (which inherits `ControllerResponse`). Thus, separation of concerns with the domain DTOs keeps the WSL and BSL loosely coupled.

## Business Logic Layer
The business logic layer's basic functions include:
- Holding business logic
- Accessing third-party libraries
- Aggregating data from repositories
- Managing threads for concurrent tasks

Each service in the BLL is provided to the WSL via a `ServiceFactory`, which provides a singleton implementation for a specific service. Services are grouped by their functions (e.g. authentication, user details, home details, etc.), as opposed to DTOs. Each service collects data from several repositories in the DAL.

## Data Access Layer
The data access layer's basic functions include:
- Querying the database
- Providing platform-agnostic implementations of repositories

Each concrete repository in the DAL extends an abstract base repository for a specific platform (e.g. `BaseFirestoreRepository<T>`). The base repository provides common functions for each repository, including `findById()`, `create()`, etc. Each base repository implements the interface `IRepository<T>`, which is platform-agnostic.

The concrete repositories also implement a platform-agnostic, DTO-specific repository interface (e.g. `IUserRepository`), so that the BLL does not have to be cognizant of the DAL's specific platform.

Each repository is provided to the BLL via a `RepositoryFactory`, which provides a singleton implementation of each repository. This factory controls which implementation of a certain "`IDTORepository`" is provided.