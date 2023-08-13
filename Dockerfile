FROM openjdk:11-jdk-buster

WORKDIR /app

COPY . .

ENTRYPOINT ["./mvnw", "spring-boot:run"]