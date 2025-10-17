FROM quay.io/keycloak/keycloak:26.4.0

WORKDIR /app

COPY imports/ /opt/keycloak/data/import/

ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin

EXPOSE 8080

#ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]
#ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--hostname=nginx-service", "--http-enabled=true", "--import-realm"]