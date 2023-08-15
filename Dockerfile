FROM openjdk:11-jdk-buster

WORKDIR /app

COPY target/hfms.jar .

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "hfms.jar"]