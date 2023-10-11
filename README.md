# Camel Quarkus JMS Fruits

## Exploration of 12 Factors

### Factor I. Codebase

> The code is versioned via Git -> Github

### Factor II. Dependencies

> Dependencies are managed by maven, referencing the repository *maven.repository.redhat.com*

Dependecies list in pom.xml file:

* camel-quarkus-jackson
* camel-quarkus-rest
* camel-quarkus-direct
* camel-quarkus-activemq
* quarkus-arc
* quarkus-resteasy

### Factor III. Config

> App's configuration available in ./src/main/resources/application.properties

```properties
camel.component.activemq.broker-url=${QUARKUS_BROKER_URL}
%dev.camel.component.activemq.broker-url=${DEV_QUARKUS_BROKER_URL}
fruits.port=80
%dev.fruits.port=8080
```

### Factor IV. Bacing services

> Bacing service (amq service) is treated as a parameter no vendor-specific configuration is applied

### Factor V. Build, release, run

> The application contains scripts that show each state.

```bash
## deploy-ocp.sh
# build stage
./mvnw clean package
rm -rf build/target
cp -r target build/.
oc new-build --strategy docker --binary --name jms-fruits 2>/dev/null
oc start-build jms-fruits --from-dir build --follow

#Deploy stage
oc new-app -i jms-fruits
   oc set env deployment/jms-fruits QUARKUS_BROKER_URL=tcp://amq-standalone.amq-cluster.svc.cluster.local:61616

oc create route edge --service=jms-fruits
```
### Factor VI. Processes

> The application is stateless, prepared to run as a process, container or openshift application.

```Dockerfile
## src/main/docker/Dockerfile.native
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.5
WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
COPY --chown=1001:root target/*-runner /work/application

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

### Factor VII. Port binding

> The application exposes the api on ports defined by configuration

```properties
fruits.port=80
%dev.fruits.port=8080
```

### Factor VIII. Concurrency

> The no-share policy of the application (as well as stateless) guarantee its ability to scale horizontally.

### Factor IX. Disposability

> The application is prepared to be natively build (quarkus - graalvm), which increases the speed it starts and stops.

### Factor X. Dev/prod parity

> The application is prepared to run as a container, and the parameters are defined with "profiles" which allow distinctions between environments


```properties
# "prod"
fruits.port=80
# "dev"
%dev.fruits.port=8080
```

### Factor XI. Logs

> The application is configured to yield logs on stdin and stdout (as streams and not files), it also has specific log configuration defining formats.

```properties
camel.component.log.exchange-formatter = #class:org.apache.camel.support.processor.DefaultExchangeFormatter
camel.component.log.exchange-formatter.show-exchange-pattern = false
camel.component.log.exchange-formatter.show-body-type = false
```

### Factor XII. Admin processes

> The application's admin processes are handled by maven and run separately but still in the same environment.