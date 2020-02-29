IFROM openjdk:8-jre

MAINTAINER Cortex Ops Team <dev-ops@cortexp.com>

ADD target/classes/application.properties /app/application-local.properties
ADD target/classes/banner.txt /app/banner.txt
ADD target/classes/logback.xml /app/logback.xml
ADD target/oriontec-esper-1.0.0.jar /app/oriontec-esper-1.0.0.jar
ADD target/lib /app/lib
ADD start.sh /app/start.sh
WORKDIR /app
RUN mkdir -p /app/tmp
# Web port.
EXPOSE 2000
ENTRYPOINT ["./start.sh"]
