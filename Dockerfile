FROM openjdk:11-jdk-buster

WORKDIR /app

COPY . .

RUN chmod +x mvnw

ENTRYPOINT ["./mvnw", "spring-boot:run"]