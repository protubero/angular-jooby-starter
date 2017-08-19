FROM maven:3.5-jdk-8

RUN mkdir project
WORKDIR /project 

COPY . .

ENTRYPOINT ["bash"]