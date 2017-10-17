# batch-minimal
Simple bundle with minimal dependencies to create a springframework batch based module.

Simple test case are done by working with files and a basic in memory database

## How-to

To launch the job:

```
cd path/to/your/project
java -cp build/classes:build/libs/* org.springframework.batch.core.launch.support.CommandLineJobRunner net.sinou.patterns.spring.batch.minimal.jobs.SimpleBatchConfiguration simpleBatchJob
```