FROM gradle:jdk25-alpine AS builder

WORKDIR /gradle-builder

COPY . .

RUN gradle build -x test --no-daemon


FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=builder /gradle-builder/build/libs/corbit-auth-0.0.1.jar app.jar

ENV AUTH_PROFILE=keycloak

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]