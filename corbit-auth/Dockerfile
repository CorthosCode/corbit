FROM gradle:jdk25 AS builder

WORKDIR /gradle-builder

COPY . .

RUN gradle build -x test --no-daemon


FROM openjdk:25-jdk-slim

WORKDIR /app

COPY --from=builder /gradle-builder/build/libs/corbit-auth-0.0.1.jar app.jar

ENV AUTH_PROFILE=basic

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]