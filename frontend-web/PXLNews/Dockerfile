FROM node:18.19-alpine AS build
WORKDIR /usr/src/app
COPY . .
RUN npm install
RUN npm run build

FROM nginx
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/pxlnews /usr/share/nginx/html
EXPOSE 80
