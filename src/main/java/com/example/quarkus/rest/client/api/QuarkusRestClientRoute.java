package com.example.quarkus.rest.client.api;

import com.example.quarkus.rest.client.model.CreatePerson;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Pattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class QuarkusRestClientRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/get/users")
                .get()
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .to("direct:getAllPersons");

        rest("/create/user")
                .post()
                .type(CreatePerson.class)
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .to("direct:createPerson");

        from("direct:getAllPersons")
                .log("Request For get Data")
                .toD("{{restservice.api.endpoint}}")
                .convertBodyTo(String.class)
                .log("Response Body_${body}");

        from("direct:createPerson")
                .marshal().json(JsonLibrary.Jackson)
                .log("Request Data_${body}")
                .toD("{{restservice.api.endpoint}}")
                .convertBodyTo(String.class);


    }
}
