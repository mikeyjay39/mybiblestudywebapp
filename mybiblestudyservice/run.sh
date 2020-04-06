#!/bin/bash

# launch docker and redis
docker run --rm --name redis-docker -d -p 6379:6379 redis:alpine
#docker run --rm --name kafka-docker -d -p 2181:2181 -p 9092:9092 spotify/kafka
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=$(echo $PSQLPASSWORD) -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
docker run --rm --name pg-docker-bibletext -e POSTGRES_PASSWORD=$(echo $PSQLPASSWORD) -d -p 5434:5432 postgres
#docker run --rm  --name kafka-docker -d -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=localhost --env ADVERTISED_PORT=9092 --env TOPICS=persistenceServiceTopic spotify/kafka


# use this script to start firefox and go to the web app
# firefox http://mybiblestudywebapp.us-east-2.elasticbeanstalk.com/index.html &