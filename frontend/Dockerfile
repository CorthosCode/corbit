FROM node:24-alpine as build-front

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:stable-alpine

COPY --from=build-front /dist /usr/share/nginx/html
COPY --from=build-front nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 8080

CMD [ "nginx", "-g", "daemon off;" ]