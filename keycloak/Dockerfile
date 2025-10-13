FROM quay.io/keycloak/keycloak:26.4.0

WORKDIR /app

ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]