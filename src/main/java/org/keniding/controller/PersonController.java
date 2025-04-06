package org.keniding.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.keniding.model.Person;

import java.util.Date;

@Path("/api/person")
public class PersonController {

    Person person = new Person();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson() {
        person.setName("ken");
        person.setLastName("iding");
        person.setAge(20);
        person.setHeight(1.70);
        person.setWeight(60.0);
        person.setBirthDate(new Date());
        return person;
    }

    @GET
    @Path("/persona2")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPerson2() {
        person.setName("Juan");
        person.setLastName("iding");
        person.setAge(20);
        person.setHeight(1.70);
        person.setWeight(60.0);
        person.setBirthDate(new Date());
        return person;
    }
}
