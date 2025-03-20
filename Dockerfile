# Sử dụng JDK 17 làm base image
FROM eclipse-temurin:17-jdk

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Copy file JAR vào container
COPY target/vouchermanager-0.0.1-SNAPSHOT.jar app.jar

# Thiết lập biến môi trường PORT cho Cloud Run
ENV PORT=8080

# Expose cổng 8080
EXPOSE 8080

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]
