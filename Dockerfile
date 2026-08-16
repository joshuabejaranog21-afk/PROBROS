FROM nginx:latest

# Nginx config: maps extensionless URLs (/fire-damage-restoration) to .html files
COPY nginx.conf /etc/nginx/nginx.conf

# Copy all HTML, CSS, and JS files
COPY *.html /usr/share/nginx/html/
COPY *.css /usr/share/nginx/html/
COPY *.js /usr/share/nginx/html/

# Copy SEO files
COPY robots.txt sitemap.xml /usr/share/nginx/html/

# Copy static assets and uploads
COPY assets /usr/share/nginx/html/assets/
COPY uploads /usr/share/nginx/html/uploads/

EXPOSE 80
