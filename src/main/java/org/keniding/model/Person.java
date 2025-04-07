package org.keniding.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    @Max(value = 120, message = "La edad no puede ser mayor a 120")
    private int age;

    @Positive(message = "La altura debe ser un valor positivo")
    @DecimalMax(value = "3.0", message = "La altura no puede ser mayor a 3 metros")
    private double height;

    @Positive(message = "El peso debe ser un valor positivo")
    @DecimalMax(value = "500.0", message = "EL peso no puede ser mayor a 500 kg")
    private double weight;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date birthDate;
}
