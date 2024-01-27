FROM  maven:3.9.5 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests=true

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/device-management.war /app/device-management.war

ENTRYPOINT ["java","-jar", "device-management.war"]