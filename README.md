# Fullstack Java Project

## Rhani Pieters (3AONC)

## Folder structure

- Readme.md
- _architecture_: this folder contains documentation regarding the architecture of your system.
- `docker-compose.yml` : to start the backend (starts all microservices)
- _backend-java_: contains microservices written in java
- _demo-artifacts_: contains images, files, etc that are useful for demo purposes.
- _frontend-web_: contains the Angular webclient

Each folder contains its own specific `.gitignore` file.  
**:warning: complete these files asap, so you don't litter your repository with binary build artifacts!**

## How to setup and run this application

### Backend Setup

1. **Using Docker**:  
   You can run the backend using the `docker-compose.yml` file on the main branch. This will start the application, but there is an issue with RabbitMQ.

2. **Without Docker (Local Setup)**:  
   To run the backend locally, first start the databases by running the `docker-compose.yml` from the `no-container` branch.  
   Then, manually start the services in the following order:
   - `config-service`
   - `discovery-service`
   - `gateway-service`
   - Then, start the other services as needed.

### Frontend Setup

#### 1. Using Docker:
To run the frontend using Docker, follow these steps:
1. Build the Docker image for the frontend:
   ```bash
   docker build -t pxlnews .
   ```
2. Run the Docker container:
   ```bash
   docker run -p 4200:80 pxlnews
   ```
   Note: You may encounter 'service not available' issues when running both frontend and backend in Docker because of RabbitMQ.

### Notes on CORS Issues

Running the application locally should generally not trigger issues. However, if you encounter problems when using Docker for both the frontend and backend, it's recommended to run the backend locally (i.e., run the backend using Docker and directly via Java). This approach should avoid any service-related problems.
