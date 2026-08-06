FROM nginx:latest
WORKDIR /usr/share/nginx/html
RUN rm -rf *
COPY *.html ./
COPY assets ./assets/
COPY uploads ./uploads/
EXPOSE 80
