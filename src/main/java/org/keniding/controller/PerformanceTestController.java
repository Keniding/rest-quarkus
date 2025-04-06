package org.keniding.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.QueryParam;
import lombok.Data;
import org.keniding.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Path("/api/performance")
public class PerformanceTestController {

    private final Random random = new Random();
    private final String[] firstNames = {"Juan", "María", "Pedro", "Ana", "Luis", "Sofía", "Carlos", "Laura", "Miguel", "Elena"};
    private final String[] lastNames = {"García", "Rodríguez", "López", "Martínez", "González", "Pérez", "Sánchez", "Fernández", "Ramírez", "Torres"};

    @GET
    @Path("/persons")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPersons(@QueryParam("count") Integer count) {
        int size = (count != null && count > 0) ? count : 10000;

        List<Person> persons = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Person person = new Person();
            person.setName(firstNames[random.nextInt(firstNames.length)]);
            person.setLastName(lastNames[random.nextInt(lastNames.length)]);
            person.setAge(random.nextInt(80) + 18);
            person.setHeight(1.50 + random.nextDouble() * 0.50);
            person.setWeight(50.0 + random.nextDouble() * 50.0);
            person.setBirthDate(new Date(System.currentTimeMillis() - (person.getAge() * 365L * 24L * 60L * 60L * 1000L)));
            persons.add(person);
        }

        return persons;
    }

    @GET
    @Path("/large-object")
    @Produces(MediaType.APPLICATION_JSON)
    public LargeObject getLargeObject() {
        LargeObject obj = new LargeObject();
        obj.setId(random.nextLong());
        obj.setTimestamp(System.currentTimeMillis());

        int[] largeArray = new int[2_500_000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = random.nextInt();
        }
        obj.setData(largeArray);

        return obj;
    }

    @Data
    public static class LargeObject {
        private long id;
        private long timestamp;
        private int[] data;
    }
}
