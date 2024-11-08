# Usar uma imagem base JDK para execução do JAR
FROM openjdk:17-slim
# Definir o diretório de trabalho
WORKDIR /app
# Copiar o JAR gerado para o diretório de trabalho no contêiner
COPY target/sistema-despesas-0.0.1-SNAPSHOT.jar app.jar
# Comando para executar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
