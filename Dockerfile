FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S devfiles && adduser -S devfiles -G devfiles
USER devfiles:devfiles

COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS="-XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar