FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/container2-0.0.1-SNAPSHOT.jar container2.jar

EXPOSE 6001

CMD ["java", "-jar", "container2.jar"]