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

### Factor III. 

> App's configuration available in ./src/main/resources/application.properties

```properties
camel.component.activemq.broker-url=${QUARKUS_BROKER_URL}
```