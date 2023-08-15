FROM openjdk:11-jdk-buster

WORKDIR /app

RUN chmod +x mvnw

RUN ./mvnw clean package

COPY target/hfms.jar .

ENTRYPOINT ["java", "-jar", "hfms.jar"]