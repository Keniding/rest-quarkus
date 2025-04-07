package org.keniding.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.keniding.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositorio para gestionar entidades Person.
 * <p>
 * Esta clase proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para objetos Person, almacenándolos en memoria.
 * <p>
 * La anotación @ApplicationScoped significa que se creará una única instancia
 * de esta clase que será compartida por toda la aplicación.
 * Es similar al @Singleton en Spring, pero gestionado por CDI (Contexts and Dependency Injection).
 */
@ApplicationScoped
public class PersonRepository {
    /**
     * Almacén en memoria para las entidades Person.
     * <p>
     * ConcurrentHashMap es una implementación thread-safe de la interfaz Map,
     * lo que permite accesos y modificaciones concurrentes sin problemas de sincronización.
     * Es más eficiente que un HashMap sincronizado porque solo bloquea segmentos
     * específicos durante las operaciones, no el mapa en general.
     */
    private final Map<Long, Person> personMap = new ConcurrentHashMap<>();

    /**
     * Generador de IDs secuenciales para las entidades Person.
     * <p>
     * AtomicLong proporciona operaciones atómicas sobre un valor long,
     * garantizando que no se generen IDs duplicados incluso en entornos multihilo.
     * Comienza en 1 y se incrementa cada vez que se crea una nueva entidad.
     */
    private final AtomicLong sequence = new AtomicLong(1);

    /**
     * Recupera todas las entidades Person almacenadas.
     * <p>
     * Se crea una nueva ArrayList a partir de los valores del mapa para evitar
     * que el cliente modifique directamente la colección interna.
     *
     * @return Lista con todas las personas almacenadas
     */
    public List<Person> findAll() {
        return new ArrayList<>(personMap.values());
    }

    /**
     * Busca una persona por su ID.
     *
     * @param id El identificador único de la persona
     * @return Un Optional que contiene la persona si existe, o vacío si no se encuentra
     */
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(personMap.get(id));
        }

    /**
     * Guarda una nueva persona o actualiza una existente.
     * <p>
     * Si la persona no tiene ID (es nueva), se le asigna uno automáticamente
     * usando el secuenciador atómico.
     *
     * @param person La persona a guardar
     * @return La persona guardada (con ID asignado si era nueva)
     */
    public Person save(Person person) {
        if (person.getId() == null) {
            person.setId(sequence.getAndIncrement());
    }
        personMap.put(person.getId(), person);
        return person;
    }

    /**
     * Actualiza una persona existente.
     *
     * @param person La persona a actualizar
     * @return La persona actualizada
     */
    public Person update(Person person) {
        personMap.replace(person.getId(), person);
        return person;
    }

    /**
     * Elimina una persona del repositorio.
     *
     * @param id El id de la persona a eliminar
     */
    public boolean deleteById(Long id) {
        return personMap.remove(id) != null;
}

    /**
     * Verifica si existe una persona con el ID especificado.
     *
     * @param id El identificador único a verificar
     * @return true si existe una persona con ese ID, false en caso contrario
     */
    public boolean existsById(Long id) {
        return personMap.containsKey(id);
    }
}
