# Project-Peaceland
## How to run project:

### On one terminal:
```bin/zookeeper-server-start.sh config/zookeeper.properties```

### On another terminal:
```bin/kafka-server-start.sh config/server.properties```

### On another terminal:
```bin/kafka-topics.sh --create --topic peaceWatcherReport --bootstrap-server localhost:9092```

### Then run , in each project in this order : producer, consumer , alerter, analyzer:
```sbt run```

