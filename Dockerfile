FROM nginx:alpine
COPY *.html /usr/share/nginx/html/
COPY assets /usr/share/nginx/html/assets
COPY uploads /usr/share/nginx/html/uploads
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
