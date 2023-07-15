FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./target/demo5-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java","-jar","demo5-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081