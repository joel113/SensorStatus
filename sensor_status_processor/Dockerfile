FROM frolvlad/alpine-oraclejdk8:slim

MAINTAINER Johannes Ehm <johannes.ehm@gmail.com>

VOLUME /tmp
ADD target/SensorProcessor-1.0.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]