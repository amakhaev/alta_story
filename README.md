# AlTa

### How to run
##### Prepare database
1. Install `cassandra` database
2. Navigate to root folder
3. Open terminal then run commands
```
cqlsh -f dao/src/main/resources/db_keyspace.cql
```

##### Run from Intellij IDEA
1. Create configuration to use `Main.java` class as startup.
2. Add VM options `-Djava.library.path=shared/libs/ -XX:+UseG1GC -Xmx2g -Xms32m` 
