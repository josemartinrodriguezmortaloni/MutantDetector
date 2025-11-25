FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

# Instalar wget para healthcheck
RUN apk add --no-cache wget

WORKDIR /app
EXPOSE 8081

COPY --from=build /app/build/libs/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
