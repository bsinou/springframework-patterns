# batch-minimal
Simple bundle with minimal dependencies to create a springframework batch based module.

Simple test case are done by working with files and a basic in memory database

## How-to

You have to have a MariaDB (10.1.3+ is better, v5.5.56+ is OK)  or a MySql server.

To launch the job on centOs7:

```
cd path/to/your/project
# build 
./gradlew build
# set up the Database, proceed carefully, it create a DB and a user for the app
java -cp build/classes:build/libs/* net.sinou.patterns.spring.batch.minimal.configuration.SetUpSqlEnv

# Launch the job manually
java -cp build/classes:build/libs/* org.springframework.batch.core.launch.support.CommandLineJobRunner net.sinou.patterns.spring.batch.minimal.jobs.SimpleBatchConfiguration simpleBatchJob pathToFolder=<an absolute path>
```