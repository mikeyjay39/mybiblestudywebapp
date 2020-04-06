FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
EXPOSE 8901
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]