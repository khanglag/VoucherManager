# Sử dụng OpenJDK 17 hoặc 21 (kiểm tra phiên bản Java của bạn)
FROM eclipse-temurin:17-jdk

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file JAR vào container
COPY target/vouchermanager-0.0.1-SNAPSHOT.jar app.jar

# Lệnh chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
