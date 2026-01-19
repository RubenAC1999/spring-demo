# Para usar Java 17
FROM eclipse-temurin:17-jdk

# Carpeta dentro del contenedor
WORKDIR /app

# Para copiar la app
COPY target/*.jar app.jar

# Puerto
EXPOSE 8080

# Para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]