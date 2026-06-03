package com.upf.livraria.controller;

import com.upf.livraria.entity.Livraria;
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

@Path("livrarias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivrariaController {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager entityManager;

    @GET
    public List<Livraria> listar() {
        return entityManager.createQuery("SELECT l FROM Livraria l", Livraria.class).getResultList();
    }

    @GET
    @Path("{id}")
    public Response buscar(@PathParam("id") Integer id) {
        Livraria livraria = entityManager.find(Livraria.class, id);
        if (livraria == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(livraria).build();
    }

    @POST
    @Transactional
    public Response criar(Livraria livraria) {
        entityManager.persist(livraria);
        return Response.status(Response.Status.CREATED).entity(livraria).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Integer id, Livraria dados) {
        Livraria livraria = entityManager.find(Livraria.class, id);
        if (livraria == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        livraria.setNome(dados.getNome());
        livraria.setEndereco(dados.getEndereco());
        livraria.setQtdLivros(dados.getQtdLivros());

        return Response.ok(livraria).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remover(@PathParam("id") Integer id) {
        Livraria livraria = entityManager.find(Livraria.class, id);
        if (livraria == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        entityManager.remove(livraria);
        return Response.noContent().build();
    }
}