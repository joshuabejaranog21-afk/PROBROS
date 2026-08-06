FROM nginx:alpine

# Copy all HTML files
COPY *.html /usr/share/nginx/html/

# Copy static assets
COPY assets /usr/share/nginx/html/assets/
COPY uploads /usr/share/nginx/html/uploads/

# Copy Nginx config
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
