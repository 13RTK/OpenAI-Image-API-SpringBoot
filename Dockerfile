 # 使用 openjdk:17 镜像作为基础镜像
FROM openjdk:17

# 复制 jar 文件到镜像中
COPY OpenAI-Image-0.0.1-SNAPSHOT.jar /OpenAI-Image-0.0.1-SNAPSHOT.jar

# 设置 jar 文件为可执行文件
RUN chmod +x /OpenAI-Image-0.0.1-SNAPSHOT.jar

# 运行 jar 文件
CMD ["java", "-jar", "/OpenAI-Image-0.0.1-SNAPSHOT.jar"]