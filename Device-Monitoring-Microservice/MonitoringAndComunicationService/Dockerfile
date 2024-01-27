FROM  maven:3.9.5 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests=true

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/monitoring-app.war /app/monitoring-app.war

ENTRYPOINT ["java","-jar", "monitoring-app.war"]