package org.keniding.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keniding.dto.PagedResponse;
import org.keniding.enums.ProductCategory;
import org.keniding.model.Product;
import org.keniding.service.ProductService;

import java.util.List;

/**
 * Controlador REST para la gestión de productos.
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    private final ProductService productService;

    @Inject
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Recupera productos con paginación y ordenamiento.
     *
     * @param page Número de página (empieza en 0)
     * @param size Tamaño de la página
     * @param sort Campo por el que ordenar
     * @param asc Orden ascendente (true) o descendente (false)
     * @param name Filtro por nombre (opcional)
     * @param category Filtro por categoría (opcional)
     * @return Respuesta paginada con productos
     */
    @GET
    public Response getProducts(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("name") String sort,
            @QueryParam("asc") @DefaultValue("true") boolean asc,
            @QueryParam("name") String name,
            @QueryParam("category") String category) {

        List<Product> products;
        long totalElements;

        if (name != null && !name.trim().isEmpty()) {
            products = productService.findByNameContaining(name);
            totalElements = products.size();
        } else if (category != null && !category.trim().isEmpty()) {
            try {
                ProductCategory categoryEnum = ProductCategory.valueOf(category.toUpperCase());
                products = productService.findByCategory(categoryEnum);
                totalElements = products.size();
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Categoría no válida: " + category)
                        .build();
            }
        } else {
            products = productService.findAll(page, size, sort, asc);
            totalElements = productService.count();
        }

        PagedResponse<Product> response = PagedResponse.of(products, totalElements, page, size);

        return Response.ok(response).build();
    }

    /**
     * Recupera un producto por su ID.
     *
     * @param id ID del producto
     * @return Producto encontrado
     */
    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        Product product = productService.findById(id);
        return Response.ok(product).build();
    }

    /**
     * Busca un producto por su SKU.
     *
     * @param sku SKU del producto
     * @return Producto encontrado o 404 si no existe
     */
    @GET
    @Path("/sku/{sku}")
    public Response getProductBySku(@PathParam("sku") String sku) {
        return productService.findBySku(sku)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto no encontrado con SKU: " + sku)
                        .build());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product Datos del producto a crear
     * @return Producto creado
     */
    @POST
    public Response createProduct(@Valid Product product) {
        Product createdProduct = productService.create(product);
        return Response.status(Response.Status.CREATED)
                .entity(createdProduct)
                .build();
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id ID del producto a actualizar
     * @param product Nuevos datos del producto
     * @return Producto actualizado
     */
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, @Valid Product product) {
        Product updatedProduct = productService.update(id, product);
        return Response.ok(updatedProduct).build();
    }

    /**
     * Elimina un producto (eliminación lógica).
     *
     * @param id ID del producto a eliminar
     * @return Respuesta sin contenido
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.noContent().build();
    }

    /**
     * Actualiza el stock de un producto.
     *
     * @param id ID del producto
     * @param quantity Cantidad a añadir (positivo) o restar (negativo)
     * @return Producto con stock actualizado
     */
    @PATCH
    @Path("/{id}/stock")
    public Response updateStock(
            @PathParam("id") Long id,
            @QueryParam("quantity") int quantity) {

        Product updatedProduct = productService.updateStock(id, quantity);
        return Response.ok(updatedProduct).build();
    }
}
