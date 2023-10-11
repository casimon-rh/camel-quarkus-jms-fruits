package com.redhat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class Routes extends RouteBuilder {
  private final List<Fruit> fruits = new CopyOnWriteArrayList<>(Arrays.asList(new Fruit("Apple")));
  private final List<String> jsmFruits = new CopyOnWriteArrayList<>(Arrays.asList());

  private static final String DIRECTGET = "direct:getFruits";
  private static final String DIRECTPOST = "direct:postFruits";

  @ConfigProperty(name = "fruits.port")
  String port;

  @Override
  public void configure() throws Exception {

    restConfiguration().bindingMode(RestBindingMode.json).port(port);
    rest("/fruits")
        .get("/").to(DIRECTGET)
        .post().type(Fruit.class).to(DIRECTPOST);

    from(DIRECTGET).setBody(e -> fruits);
    from(DIRECTPOST).process().body(Fruit.class, (Fruit f) -> fruits.add(f));

    rest("/jmsfruits")
        .get().to(DIRECTGET)
        .post().type(Fruit.class).to("direct:postJmsFruits");

    from("direct:postJmsFruits").marshal().json(JsonLibrary.Jackson)
        .to("activemq:myqueue").transform(constant("Sent!"));

    from("activemq:myqueue")
        .process().body(String.class, (String f) -> jsmFruits.add(f));

  }
}
