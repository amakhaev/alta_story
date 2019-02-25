# AlTa

### How to run
##### Prepare database
1. Install `sqlite3` tool
2. Navigate to `dao/src/main/resources`
3. Create file 'game.db3'
4. Open terminal then run commands
```
> sqlite3 game.db3;
> .read ./db_installation.sql
```

##### Run from Intellij IDEA
1. Create configuration to use `Main.java` class as startup.
2. Add VM options `-Djava.library.path=shared/libs/` 
