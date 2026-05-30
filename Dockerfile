# 使用官方推荐的Eclipse Temurin Java 17镜像（更小、更安全、更稳定）
FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制 jar 包到容器（target目录下的jar文件）
COPY target/agv-scheduler-*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]