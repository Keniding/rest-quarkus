package org.keniding.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador de excepciones para errores de validación de Bean Validation.
 * <p>
 * Esta clase intercepta las excepciones de tipo {@link ConstraintViolationException}
 * que se producen cuando fallan las validaciones de Bean Validation (anotaciones como
 * {@code @NotNull}, {@code @Size}, {@code @Min}, etc.) y las convierte en respuestas
 * HTTP estructuradas con formato JSON.
 * <p>
 * La anotación {@code @Provider} permite que JAX-RS registre automáticamente
 * este manejador de excepciones en el contexto de la aplicación.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    /**
     * Convierte una excepción de validación en una respuesta HTTP.
     * <p>
     * Este método transforma los errores de validación en una estructura JSON
     * que contiene:
     * <ul>
     *   <li>Un mensaje de error general</li>
     *   <li>Un mapa de campos con errores y sus mensajes de validación</li>
     * </ul>
     * <p>
     * La respuesta HTTP tendrá un código de estado 400 (Bad Request).
     *
     * @param exception La excepción de validación capturada
     * @return Una respuesta HTTP con los detalles de los errores de validación
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("error", "Validation Error");
        errorResponse.put("message", "Los datos proporcionados no son válidos");

        // Transforma las violaciones de restricciones en un mapa de campo -> mensaje de error
        Map<String, String> validationErrors = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        this::extractPropertyPath,
                        ConstraintViolation::getMessage,
                        // En caso de múltiples errores para el mismo campo, los concatena
                        (error1, error2) -> error1 + "; " + error2
                ));

        errorResponse.put("violations", validationErrors);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse).build();
    }

    /**
     * Extrae el nombre del campo desde la ruta de la propiedad en la violación.
     * <p>
     * Este método simplifica la ruta de la propiedad para mostrar solo el nombre
     * del campo que falló la validación, sin incluir la ruta completa del objeto.
     * <p>
     * Por ejemplo, convierte "person.address.zipCode" en "zipCode".
     *
     * @param violation La violación de restricción
     * @return El nombre del campo que falló la validación
     */
    private String extractPropertyPath(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        int lastDotIndex = propertyPath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return propertyPath.substring(lastDotIndex + 1);
        }
        return propertyPath;
    }
}
