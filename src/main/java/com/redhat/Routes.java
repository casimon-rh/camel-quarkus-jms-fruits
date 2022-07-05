package com.redhat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

public class Routes extends RouteBuilder {
  private final List<Fruit> fruits = new CopyOnWriteArrayList<>(Arrays.asList(new Fruit("Apple")));
  private final List<String> jsmFruits = new CopyOnWriteArrayList<>(Arrays.asList());

  @Override
  public void configure() throws Exception {

    restConfiguration().bindingMode(RestBindingMode.json);
    rest("/fruits")
        .get().route().setBody(e -> fruits).endRest()
        .post().type(Fruit.class).route()
        .to("direct:postFruits")
        .endRest();

    from("direct:postFruits").process().body(Fruit.class, (Fruit f) -> fruits.add(f));

    rest("/jmsfruits")
        .get().route().setBody(e -> jsmFruits).endRest()
        .post().type(Fruit.class).route()
        .marshal().json(JsonLibrary.Jackson)
        .to("activemq:myqueue")
        .transform(constant("Sent!"))
        .endRest();

    from("activemq:myqueue")
      .process().body(String.class, (String f) -> jsmFruits.add(f));

  }
}
