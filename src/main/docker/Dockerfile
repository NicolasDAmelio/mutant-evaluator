FROM openjdk:8-jdk-alpine

RUN mkdir -p /opt/ml && \
 adduser -D -h /opt/ml ml && echo ml:ml | chpasswd && \
 chown -R ml:ml /opt/ml

ENV SPRING_PROFILES_ACTIVE prod

ENV LANG en_US.UTF-8

ADD ROOT.jar /opt/ml/root.jar

EXPOSE 8080 8081

USER ml
WORKDIR /opt/ml

ENTRYPOINT ["java", "-jar", "/opt/ml/root.jar"]