# Kafka Chat
Simple Kafka/Java based IM client.

## Backend
The backend stack consists of the kafka zookeeper and broker and is containerized using docker.
The docker stack can be launched by running the command below from the same directory as the docker-compose.yml file.
```
docker compose up -d
```

## Front end
The front end is a simple Java swing application. The build system is Maven. The application can be compile and run with the following command:
```
mvn clean compile exec:java
```
And packed into a jar (including all dependencies) with:
```
mvn clean compile assembly:single
```