FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ENV PSQLDBNAME=postgres
ENV PSQLDBUSER=postgres
ENV PSQLDBPASS=password
ENV PSQLDBURL=jdbc:postgresql://localhost:5432/postgres
ENV PSQLDBDRIVER=org.postgresql.Driver
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080