#!/bin/bash

# launch docker and redis
docker run --rm --name redis-docker -d -p 6379:6379 redis
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=$(echo $PSQLPASSWORD) -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres


# use this script to start firefox and go to the web app
# firefox http://mybiblestudywebapp.us-east-2.elasticbeanstalk.com/index.html &