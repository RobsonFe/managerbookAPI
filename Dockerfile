# Use a imagem base do OpenJDK
FROM openjdk:21-jdk

# Adicione metadados sobre a imagem
LABEL authors="ROBSON"

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR para o diretório de trabalho
COPY target/ManagerBookAPI-0.0.1-SNAPSHOT.jar app.jar

# Defina o comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
