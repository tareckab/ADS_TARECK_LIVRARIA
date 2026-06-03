package com.upf.livraria.controller;

import com.upf.livraria.entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteController {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager entityManager;

    @GET
    public List<Cliente> listar() {
        return entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }

    @GET
    @Path("{id}")
    public Response buscar(@PathParam("id") Integer id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cliente).build();
    }

    @POST
    @Transactional
    public Response criar(Cliente cliente) {
        entityManager.persist(cliente);
        return Response.status(Response.Status.CREATED).entity(cliente).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Integer id, Cliente dados) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        cliente.setNome(dados.getNome());
        cliente.setIdade(dados.getIdade());
        cliente.setCpf(dados.getCpf());

        return Response.ok(cliente).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remover(@PathParam("id") Integer id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        entityManager.remove(cliente);
        return Response.noContent().build();
    }
}