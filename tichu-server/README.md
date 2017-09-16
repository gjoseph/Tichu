This is a Dropwizard-based server application for running Tichu games.

Build
---

1. Run `mvn clean install` to build your application

Run
---
1. Start `java -jar target/tichu-server-1.0-SNAPSHOT.jar server` (optionally, pass a `config.yml` file as second argument)
1. The app is now running at `http://localhost:8080`
1. Health checks are available at `http://localhost:8081/healthcheck`
