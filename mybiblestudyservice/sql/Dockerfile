FROM postgres:12

MAINTAINER skoltun
COPY extensions.sql /docker-entrypoint-initdb.d/
COPY schema.sql /docker-entrypoint-initdb.d/
COPY backups/bookschapters.sql /docker-entrypoint-initdb.d/