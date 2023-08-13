FROM openjdk:11-jdk-buster

WORKDIR /app

COPY target/hfms-2.0.0.jar .

ENTRYPOINT ["java", "-jar", "hfms-2.0.0.jar"]