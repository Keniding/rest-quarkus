package org.keniding.model;

import lombok.Data;

import java.util.Date;

@Data
public class Person {
    private String name;
    private String lastName;
    private int age;
    private Double height;
    private Double weight;
    private Date birthDate;
}
