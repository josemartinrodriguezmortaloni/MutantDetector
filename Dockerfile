# ==========================================
# Stage 1: Build
# Usamos una imagen con Gradle y JDK 21 preinstalado
# ==========================================
FROM gradle:jdk21 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos primero los archivos de configuración de dependencias
# Esto permite aprovechar la caché de Docker si no han cambiado las dependencias
COPY build.gradle settings.gradle ./

# Descargamos las dependencias (sin copiar el código fuente aún)
RUN gradle dependencies --no-daemon

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto y generamos el JAR (saltando tests para agilizar el build en deploy)
RUN gradle bootJar --no-daemon -x test

# ==========================================
# Stage 2: Run
# Usamos una imagen JRE (Java Runtime Environment) ligera basada en Alpine
# ==========================================
FROM eclipse-temurin:21-jre-alpine

# Instalamos utilidades básicas si fueran necesarias (opcional, buena práctica en Alpine)
RUN apk add --no-cache curl

# Establecemos el directorio de trabajo
WORKDIR /app

# Creamos un usuario sin privilegios (non-root) por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos el JAR generado en el stage anterior
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponemos el puerto (informativo para Docker, Render usa la variable PORT)
EXPOSE 8080

# Comando de inicio
# -Djava.security.egd=file:/dev/./urandom ayuda a iniciar más rápido en entornos con poca entropía
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

