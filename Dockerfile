# Etapa 1: Build de la aplicación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY Salud360/pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código y compilar
COPY Salud360/ .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final más liviana
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el JAR desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto (opcional)
EXPOSE 8080

# Comando por defecto
ENTRYPOINT ["java", "-jar", "app.jar"]

