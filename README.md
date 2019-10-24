__Backup Schema__

1. Right click on "public" in datagrip
2. SQL Scripts > SQL Generator
3. Checkmark "Use create or replace syntax" AND "Ignore owner"
4. Save to schema.sql

NOTE: After backing up the schema we need to delete the create uuid
functions at the end of schema.sql otherwise the import will fail.

For INSERT statements .sql file we need to remove the primary key columns
otherwise the sequences nextval() will not properly be incremented.

__SQL__

```
TRUNCATE TABLE table_name 
RESTART IDENTITY CASCADE;
```



__Docker__

https://hackernoon.com/dont-install-postgres-docker-pull-postgres-bee20e200198

To launch Postgres:

```
sudo docker run --rm --name pg-docker -e POSTGRES_PASSWORD=$(echo $PSQLPASSWORD) -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
```

To stop:

```
sudo docker sto <container id>
```

__MileStone__

Hit API with curl:

```
curl -X POST -H "Content-Type: application/json" -d '{"viewCode":"6e9e6366-f386-11e9-b633-0242ac110002","book":"Genesis","chapterNo":1}' localhost:8080/biblestudy
```