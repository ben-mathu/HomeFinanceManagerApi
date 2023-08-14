FROM openjdk:11-jdk-buster

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean install

ENTRYPOINT ["./mvnw", "spring-boot:run"]