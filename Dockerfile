FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY admin/pom.xml admin/
COPY admin/src admin/src
RUN cd admin && mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/admin/target/probros-admin-1.0.0.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
