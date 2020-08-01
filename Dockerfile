FROM openjdk:11-jre-slim
WORKDIR /usr/src/registry
COPY build/libs/udemy-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","udemy-0.0.1-SNAPSHOT.jar"]