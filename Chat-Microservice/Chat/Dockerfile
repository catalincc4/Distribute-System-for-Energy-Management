FROM  maven:3.9.5 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests=true

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/chat-app.war /app/chat-app.war

ENTRYPOINT ["java","-jar", "chat-app.war"]