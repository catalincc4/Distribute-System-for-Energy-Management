FROM  maven:3.9.5 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests=true

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/user-management.war /app/user-management.war

ENTRYPOINT ["java","-jar", "user-management.war"]