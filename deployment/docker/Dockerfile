FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY core/build/libs/core-*.jar app.jar

RUN useradd -m -u 1000 java
USER 1000

EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "app.jar"]