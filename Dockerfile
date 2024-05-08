FROM openjdk:17-jdk-alpine
ARG JAR_FILE=*.jar
COPY ./target/ecommerce-marketplace-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
