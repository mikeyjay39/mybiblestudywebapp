FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
EXPOSE 5555
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]