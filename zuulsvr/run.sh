#!/bin/bash
echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_URI"
echo "********************************************************"
while ! `nc -z eurekasvr $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z confsvr $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Starting Zuul Server with Configuration Service via Eureka :  $EUREKASERVER_URI:$SERVER_PORT"
echo "********************************************************"
java -jar app.jar