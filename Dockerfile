FROM nginx:latest

# Copy all HTML and CSS files
COPY *.html /usr/share/nginx/html/
COPY *.css /usr/share/nginx/html/

# Copy static assets and uploads
COPY assets /usr/share/nginx/html/assets/
COPY uploads /usr/share/nginx/html/uploads/

EXPOSE 80
