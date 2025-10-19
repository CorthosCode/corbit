FROM quay.io/keycloak/keycloak:26.4.0

WORKDIR /app

COPY imports/ /opt/keycloak/data/import/

ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin
ENV KC_PROXY=edge
ENV KC_LOG_LEVEL=DEBUG

EXPOSE 8080

#ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]
#ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--hostname=nginx-service", "--http-enabled=true", "--import-realm"]