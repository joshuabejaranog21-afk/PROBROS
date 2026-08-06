FROM nginx:latest

# Remove default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Copy all HTML files
COPY *.html /usr/share/nginx/html/

# Copy static assets
COPY assets /usr/share/nginx/html/assets/
COPY uploads /usr/share/nginx/html/uploads/

EXPOSE 80
