FROM openjdk:8-jre-alpine

ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk \
    JAVA_OPTIONS=" -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 "

RUN mkdir -p /opt/jurix \
    && apk add --update --no-cache curl bash netcat-openbsd tzdata

COPY target/jurix-api-1.0.1.jar /opt/az/jurix-api.jar

WORKDIR /opt/jurix

CMD java -jar jurix-api.jar


