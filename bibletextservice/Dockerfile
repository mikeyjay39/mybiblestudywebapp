FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
EXPOSE 8081
RUN  apt update --yes --allow-unauthenticated && apt upgrade --yes --allow-unauthenticated && apt install netcat-openbsd --yes --allow-unauthenticated
ADD target/*.jar app.jar
ADD src/main/resources/Kjvbookscsv.csv Kjvbookscsv.csv
ADD src/main/resources/Kjvtextcsv.csv Kjvtextcsv.csv
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh