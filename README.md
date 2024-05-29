# SCTasks Backend API

## Overview

This repository contains the backend API for the SCTasks project. The API is containerized using Docker and can be easily set up and run using Docker Compose.

## Prerequisites

Ensure you have Docker installed and running on your machine before proceeding:

- [Install Docker](https://docs.docker.com/get-docker/)

## Getting Started

Follow the steps below to launch the backend API:

### Step 1: Clone the Repository

Clone this repository to your local machine using the following command:

bash
git clone https://github.com/stephen18961/SCTasks

### Step 2: Open Command Prompt

Open your command prompt (cmd) on your computer.

### Step 3: Navigate to the Project Directory

Change the directory to the rest-api folder within the cloned repository:
cd SCTasks/rest-api

### Step 4: Build and Run the API

To build and run the API using Docker Compose, execute the following command:
docker-compose up --build

This command will:

1. Build the Docker images as specified in the docker-compose.yml file.
2. Start the containers as defined in the docker-compose.yml file.

### Accessing the API

Once the containers are up and running, you can access the API at http://localhost:5000.

## Stopping the API

To stop the API, run the following command in the command prompt:
docker-compose down

This will stop and remove the containers.

## Troubleshooting

If you encounter any issues, please refer to the Docker and Docker Compose documentation:

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

## Contributing

If you'd like to contribute to this project, please fork the repository and use a feature branch. Pull requests are welcome.
