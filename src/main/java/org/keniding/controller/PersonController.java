package org.keniding.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keniding.model.Person;
import org.keniding.service.PersonService;

import java.util.List;

/**
 * Controlador REST para la gestión de entidades Person.
 * <p>
 * Esta clase expone endpoints HTTP para realizar operaciones CRUD sobre
 * entidades Person, siguiendo los principios de una API RESTful.
 * <p>
 * Todos los endpoints producen y consumen datos en formato JSON.
 */
@Path("/api/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {
    /**
     * Servicio que implementa la lógica de negocio para entidades Person.
     */
    private final PersonService personService;

    /**
     * Constructor que inicializa el controlador con su servicio.
     * <p>
     * La anotación @Inject permite que CDI inyecte automáticamente
     * una instancia del PersonService.
     *
     * @param personService El servicio de personas a utilizar
     */
    @Inject
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Recupera todas las personas.
     * <p>
     * Endpoint: GET /api/persons
     *
     * @return Respuesta HTTP con código 200 (OK) y la lista de personas en formato JSON
     */
    @GET
    public Response getPersons() {
        List<Person> persons = personService.findAll();
        return Response.status(Response.Status.OK)
                .entity(persons)
                .build();
    }

    /**
     * Recupera una persona por su ID.
     * <p>
     * Endpoint: GET /api/persons/{id}
     * <p>
     * Si la persona no existe, el servicio lanzará una excepción
     * que será manejada por un ExceptionMapper (si está configurado).
     *
     * @param id El identificador único de la persona
     * @return Respuesta HTTP con código 200 (OK) y los datos de la persona en formato JSON
     */
    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK)
                .entity(personService.findById(id))
                .build();
    }

    /**
     * Crea una nueva persona.
     * <p>
     * Endpoint: POST /api/persons
     * <p>
     * El cuerpo de la petición debe contener los datos de la persona en formato JSON.
     * No es necesario incluir el ID, ya que se generará automáticamente.
     *
     * @param person Los datos de la persona a crear
     * @return Respuesta HTTP con código 201 (Created) y los datos de la persona creada en formato JSON
     */
    @POST
    public Response createPerson(@Valid Person person) {
        Person createdPerson = personService.create(person);
        return Response.status(Response.Status.CREATED)
                .entity(createdPerson)
                .build();
    }

    /**
     * Actualiza una persona existente.
     * <p>
     * Endpoint: PUT /api/persons/{id}
     * <p>
     * El ID en la URL debe coincidir con la persona que se desea actualizar.
     * El cuerpo de la petición debe contener los datos actualizados en formato JSON.
     * <p>
     * Si la persona no existe, el servicio lanzará una excepción
     * que será manejada por un ExceptionMapper (si está configurado).
     *
     * @param id El identificador único de la persona a actualizar
     * @param person Los nuevos datos de la persona
     * @return Respuesta HTTP con código 200 (OK) y los datos actualizados de la persona en formato JSON
     */
    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, @Valid Person person) {
        Person updatedPerson = personService.update(id, person);
        return Response.status(Response.Status.OK)
                .entity(updatedPerson)
                .build();
    }

    /**
     * Elimina una persona por su ID.
     * <p>
     * Endpoint: DELETE /api/persons/{id}
     * <p>
     * Si la persona no existe, el servicio lanzará una excepción
     * que será manejada por un ExceptionMapper (si está configurado).
     *
     * @param id El identificador único de la persona a eliminar
     * @return Respuesta HTTP con código 204 (No Content) sin cuerpo
     */
    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
