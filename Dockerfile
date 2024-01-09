FROM maven:3.6.3-openjdk-8 as BUILDER

ARG VERSION=0.0.1-SNAPSHOT
RUN mkdir  /build
WORKDIR /build/
COPY ./pom.xml /build/
COPY ./src /build/src/

RUN mvn clean package

FROM openjdk:8-jre-slim
RUN mkdir /app
WORKDIR /app/

RUN apt-get update && \
    apt-get install -y libfreetype6

ENV LD_LIBRARY_PATH=/usr/local/openjdk-8/lib/amd64:$LD_LIBRARY_PATH

COPY --from=BUILDER ./build/target/automation-dashboard.jar /app/
CMD java -jar /app/automation-dashboard.jar