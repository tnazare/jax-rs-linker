package fr.vidal.oss.jax_rs_linker.parser;

import fr.vidal.oss.jax_rs_linker.api.Self;
import fr.vidal.oss.jax_rs_linker.api.SubResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/person")
public class PersonResource {

    @Self
    @Path("/{id}")
    @GET
    public void getById(@PathParam("id") int id) {

    }

    @SubResource(PersonResource.class)
    @Path("/name/{firstName}")
    @GET
    public void getByFirstName(@PathParam("firstName") String firstName) {

    }
}
