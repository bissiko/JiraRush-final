# Використовуємо офіційний базовий образ JDK для запуску Java додатків
FROM openjdk:17-jdk-slim

# Встановлюємо змінну оточення для вказівки місцезнаходження JAR-файлу
ARG JAR_FILE=target/jira-1.0.jar

# Копіюємо JAR-файл у контейнер
COPY ${JAR_FILE} app.jar

# Виставляємо порт, на якому працюватиме додаток
EXPOSE 8080

# Команда для запуску Spring Boot додатку
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Command for build Docker
# docker build -t jirarush .

# Run Docker
# docker run -p 8080:8080 jirarush