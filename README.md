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

:heavy_check_mark:_(COMMENT) Add setup instructions and provide some direction to run the whole  application: frontend to backend._

### Backend Setup

1. **Using Docker**:  
   You can run the backend using the `docker-compose.yml` file. This will start the application, but there may be a CORS issue.

2. **Without Docker (Local Setup)**:  
   To run the backend locally, first start the databases by running the `docker-compose.yml` from the `no-container` branch.  
   Then, manually start the services in the following order:
   - `config-service`
   - `discovery-service`
   - `gateway-service`
   - Then, start the other services as needed.

### Frontend Setup

1. **Using Docker**:  
   The frontend also has a Dockerfile. First, build the image with:  
   ```bash    docker build -t pxlnews .  ```

Then, run it using:
```bash docker run -p 80:80 pxlnews  ```

However, this may also result in a CORS issue.

Without Docker (Local Setup):
To avoid the CORS issue, you can manually run the frontend using the following command:


Notes on CORS Issues
Running the application locally should not trigger any CORS issues. After 20 hours of troubleshooting, I was unable to resolve the CORS problem when using Docker. If you run into CORS issues with Docker, it is recommended to run the frontend and backend locally (without Docker) to avoid such problems.




