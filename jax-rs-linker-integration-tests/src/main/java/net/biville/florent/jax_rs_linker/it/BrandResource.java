package net.biville.florent.jax_rs_linker.it;


import net.biville.florent.jax_rs_linker.model.Self;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/brand")
public class BrandResource {

    @Self
    @Path("/{id}")
    @GET
    public void getById(@PathParam("id") int id) {

    }
}
