# MongoDB + REST

Based on the spring.io tutorial [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)

## HowTo

### Pre-requisites

On centOS7, begin by installing MongoDB following these [instructions](https://www.digitalocean.com/community/tutorials/how-to-install-mongodb-on-centos-7) 

### Tests

Once the app has been started, it can be tested with:

```
# Discover endpoints
curl http://localhost:8080

# List posts 
curl http://localhost:8080/posts

# Add a post
curl -i -X POST -H "Content-Type:application/json" -d "{  \"title\" : \"FirstPost\",  \"body\" : \"Bla Bla Bla\" }" http://localhost:8080/posts
```
