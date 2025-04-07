package org.keniding.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keniding.model.Person;
import org.keniding.repository.PersonRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión de entidades Person.
 * <p>
 * Esta clase implementa la lógica de negocio relacionada con las operaciones
 * sobre entidades Person, delegando el acceso a datos al PersonRepository.
 * <p>
 * La anotación @ApplicationScoped significa que se creará una única instancia
 * de esta clase que será compartida por toda la aplicación.
 * Es similar al @Singleton en Spring, pero gestionado por CDI (Contexts and Dependency Injection).
 */
@ApplicationScoped
public class PersonService {
    /**
     * Repositorio para el acceso a datos de entidades Person.
     */
    private final PersonRepository personRepository;

    /**
     * Constructor que inicializa el servicio con su repositorio.
     * <p>
     * La anotación @Inject permite que CDI inyecte automáticamente
     * una instancia del PersonRepository.
     * <p>
     * Al inicializarse, carga datos de ejemplo si el repositorio está vacío.
     *
     * @param personRepository El repositorio de personas a utilizar
     */
    @Inject
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;

        initSampleData();
    }

    /**
     * Inicializa el repositorio con datos de ejemplo.
     * <p>
     * Este método solo carga datos si el repositorio está vacío,
     * evitando duplicar información en inicializaciones posteriores.
     */
    private void initSampleData() {
        if (personRepository.findAll().isEmpty()) {
            Person person1 = new Person();
            person1.setName("Ken");
            person1.setLastName("Iding");
            person1.setAge(20);
            person1.setHeight(1.70);
            person1.setWeight(60.0);
            person1.setBirthDate(new java.util.Date());
            personRepository.save(person1);

            Person person2 = new Person();
            person2.setName("Juan");
            person2.setLastName("Pérez");
            person2.setAge(25);
            person2.setHeight(1.80);
            person2.setWeight(70.0);
            person2.setBirthDate(new java.util.Date());
            personRepository.save(person2);
        }
    }

    /**
     * Recupera todas las personas almacenadas.
     *
     * @return Lista con todas las personas
     */
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    /**
     * Busca una persona por su ID.
     * <p>
     * A diferencia del repositorio que devuelve un Optional,
     * este método lanza una excepción si la persona no existe.
     *
     * @param id El identificador único de la persona
     * @return La persona encontrada
     * @throws NoSuchElementException si no existe una persona con el ID especificado
     */
    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Person not found with id " + id));
    }

    /**
     * Crea una nueva persona.
     * <p>
     * Establece el ID como null para asegurar que se genere uno nuevo,
     * evitando posibles conflictos con IDs existentes.
     *
     * @param person La persona a crear
     * @return La persona creada con su ID asignado
     */
    public Person create(Person person) {
        person.setId(null);
        return personRepository.save(person);
    }

    /**
     * Actualiza una persona existente.
     * <p>
     * Verifica que la persona exista antes de actualizarla y
     * asegura que se use el ID proporcionado.
     *
     * @param id El identificador de la persona a actualizar
     * @param person Los nuevos datos de la persona
     * @return La persona actualizada
     * @throws NoSuchElementException si no existe una persona con el ID especificado
     */
    public Person update(Long id, Person person) {
        if (!personRepository.existsById(id)) {
            throw new NoSuchElementException("Person not found with id " + person.getId());
        }
        person.setId(id);
        return personRepository.save(person);
    }

    /**
     * Elimina una persona por su ID.
     * <p>
     * Verifica que la operación de eliminación sea exitosa y
     * lanza una excepción si la persona no existe.
     *
     * @param id El identificador de la persona a eliminar
     * @throws NoSuchElementException si no existe una persona con el ID especificado
     */
    public void delete(Long id) {
        if (!personRepository.deleteById(id)) {
            throw new NoSuchElementException("Person not found with id " + id);
        }
    }
}
