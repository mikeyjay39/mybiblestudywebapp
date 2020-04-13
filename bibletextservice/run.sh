#!/bin/bash
echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_URI"
echo "********************************************************"
while ! `nc -z eurekasvr $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"


echo "********************************************************"
echo "Waiting for the database server to start on $PSQLDBURL"
echo "********************************************************"
while ! `nc -z postgres-bibles $DATABASESERVER_PORT`;
do
  sleep 3;
  echo "Checking for the database server to start on $PSQLDBURL";
  done
echo "******** Database Server has started "

echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z confsvr $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"


echo "********************************************************"
echo "Waiting for the kafka server to start on port $KAFKASERVER_PORT"
echo "********************************************************"
while ! `nc -z zookeeper $KAFKASERVER_PORT`; do sleep 10; done
echo "******* Kafka Server has started"

echo "********************************************************"
echo "Waiting for the REDIS server to start  on port $REDIS_PORT"
echo "********************************************************"
while ! `nc -z redis $REDIS_PORT`; do sleep 10; done
echo "******* REDIS has started"

echo "********************************************************"
echo "Starting BibleTextService with Configuration Service via Eureka :  $EUREKASERVER_URI:$SERVER_PORT"
echo "Using Kafka Server: $KAFKASERVER_URI"
echo "Using ZK    Server: $ZKSERVER_URI"
echo "********************************************************"
java -jar app.jar