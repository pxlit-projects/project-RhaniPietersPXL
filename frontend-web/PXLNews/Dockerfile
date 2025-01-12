FROM node:18.19-alpine AS build
WORKDIR /usr/src/app
COPY . .
RUN npm install
RUN npm run build

FROM nginx
COPY --from=build /usr/src/app/dist/pxlnews/browser /usr/share/nginx/html
COPY ./entrypoint.sh /usr/local/bin/entrypoint.sh
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
RUN chmod +x /usr/local/bin/entrypoint.sh

EXPOSE 80
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
