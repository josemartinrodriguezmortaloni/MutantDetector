# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
# Usamos Eclipse Temurin (distribución oficial recomendada de OpenJDK)
# Versión Alpine para mantener la imagen pequeña
FROM eclipse-temurin:21-jdk-alpine as build

# Copiar archivos necesarios para el build
WORKDIR /app
COPY . .

# Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# Ejecutar Gradle para compilar y generar el JAR ejecutable
RUN ./gradlew bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Usamos la versión JRE (Java Runtime Environment) que es más ligera para producción
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Documentar que la aplicación escucha en el puerto 8081
EXPOSE 8081

# Copiar el JAR generado en la ETAPA 1
COPY --from=build /app/build/libs/MutantDetector-0.0.1-SNAPSHOT.jar ./app.jar

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

