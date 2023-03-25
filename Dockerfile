# Build MySQL stage
FROM mysql:latest AS mysql_build
WORKDIR /docker-entrypoint-initdb.d/
COPY init.sql .

# Build application stage
FROM maven:3.8.4-openjdk-17-slim AS app_build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /app/src/
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=app_build /app/target/drone-delivery-0.0.1-SNAPSHOT.jar .
COPY --from=mysql_build /docker-entrypoint-initdb.d/ /docker-entrypoint-initdb.d/
ENTRYPOINT ["java", "-jar", "drone-delivery-0.0.1-SNAPSHOT.jar"]
