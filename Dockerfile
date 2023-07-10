FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./target/demo5-0.0.1-SNAPSHOT.jar /app/demo5-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","demo5-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081