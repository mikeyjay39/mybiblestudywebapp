FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080