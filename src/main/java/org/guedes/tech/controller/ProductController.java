package org.guedes.tech.controller;

import java.security.InvalidParameterException;
import java.util.Map;
import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import org.guedes.tech.model.Product;
import org.guedes.tech.service.ProductService;

@AllArgsConstructor
@Path("/api/v1/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    private final ProductService productService;

    @GET
    public Response getProducts(
            @QueryParam("name") @DefaultValue("") String productName,
            @QueryParam("brand") @DefaultValue("") String brandName
    ) {
        try {
            return Response.ok(productService.getProducts(productName, brandName)).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        try {
            final var productByIdOpt = productService.findProductById(id);
            if (productByIdOpt.isEmpty()) {
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(productByIdOpt.get()).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @POST
    public Response create(Product product) {
        try {
            productService.create(product);
            return Response.noContent().build();
        } catch (Exception e) {
            if (e instanceof InvalidAttributesException) {
                return Response.status(Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
            }

            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long productId, Product product) {
        try {
            return Response.ok(productService.replace(productId, product)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                return Response.status(Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }

            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response update(@PathParam("id") long productId) {
        var isDeleted = productService.delete(productId);
        if (!isDeleted) {
            return Response.notModified().build();
        }
        return Response.noContent().build();
    }

}
