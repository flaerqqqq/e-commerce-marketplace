FROM ubuntu:latest
LABEL authors="vital"

ENTRYPOINT ["top", "-b"]