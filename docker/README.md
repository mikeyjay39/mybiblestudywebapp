## ELK

To access Kibana:

```http://localhost:5601/```

### Errors due to too little diskspace available

If encountering `error forbidden` when trying to created indexes
then do this:

```
curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_settings -d '{"index.blocks.read_only_allow_delete": null}'
```

If not getting any indexes from Elasticsearch then run these commands:

```
curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_cluster/settings -d '{ "transient": { "cluster.routing.allocation.disk.threshold_enabled": false } }'
curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_all/_settings -d '{"index.blocks.read_only_allow_delete": null}'
```

Elasticsearch indexes can be checked here:

[http://localhost:9200/_cat/indices?v](http://localhost:9200/_cat/indices?v
)


