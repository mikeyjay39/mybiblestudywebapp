PGPASSWORD=$(echo $PSQLDBPASS) pg_dump -h localhost -p 5432 -U developer \
-d mybiblestudydb > /home/developer/my-bible-study-web-app/sql/backups/mybiblestudydb.dump