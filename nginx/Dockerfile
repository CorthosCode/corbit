FROM nginx:stable-alpine

COPY ./nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
EXPOSE 9094
EXPOSE 9095

CMD ["nginx", "-g", "daemon off;"]