FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
EXPOSE 8901
RUN  apt update --yes --allow-unauthenticated && apt upgrade --yes --allow-unauthenticated && apt install netcat-openbsd --yes --allow-unauthenticated
ADD target/*.jar app.jar
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
#ENTRYPOINT ["java","-jar","/app.jar"]