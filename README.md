__Backup Schema__

1. Right click on "public" in datagrip
2. SQL Scripts > SQL Generator
3. Checkmark "Use create or replace syntax" AND "Ignore owner"
4. Save to schema.sql

__Docker__

https://hackernoon.com/dont-install-postgres-docker-pull-postgres-bee20e200198

To launch Postgres:

```sudo docker run --rm --name pg-docker -e POSTGRES_PASSWORD=$(echo $PSQLPASSWORD) -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres```

To stop:

```sudo docker sto <container id>```