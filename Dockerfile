FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /app/target/bicycle-insurance-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
