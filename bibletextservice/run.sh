#!/bin/bash

# launch DB in docker with non persistent store
sudo docker run --rm --name pg-docker-bibletext -e POSTGRES_PASSWORD=$(echo $PSQLPASSWORD) -d -p 5434:5432 postgres