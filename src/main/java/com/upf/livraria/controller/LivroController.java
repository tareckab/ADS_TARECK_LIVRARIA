package com.upf.livraria.controller;

import com.upf.livraria.entity.Livro;
import com.upf.livraria.entity.Livraria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
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

@Path("livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivroController {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager entityManager;

    @GET
    public List<Livro> listar() {
        return entityManager.createQuery("SELECT l FROM Livro l", Livro.class).getResultList();
    }

    @GET
    @Path("{id}")
    public Response buscar(@PathParam("id") Integer id) {
        Livro livro = entityManager.find(Livro.class, id);
        if (livro == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(livro).build();
    }

    @POST
    @Transactional
    public Response criar(Livro livro) {
        livro.setLivraria(resolveLivraria(livro.getLivraria()));
        entityManager.persist(livro);
        return Response.status(Response.Status.CREATED).entity(livro).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Integer id, Livro dados) {
        Livro livro = entityManager.find(Livro.class, id);
        if (livro == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setPaginas(dados.getPaginas());
        livro.setLivraria(resolveLivraria(dados.getLivraria()));

        return Response.ok(livro).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remover(@PathParam("id") Integer id) {
        Livro livro = entityManager.find(Livro.class, id);
        if (livro == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        entityManager.remove(livro);
        return Response.noContent().build();
    }

    private Livraria resolveLivraria(Livraria livraria) {
        if (livraria == null || livraria.getId() == null) {
            return null;
        }

        Livraria existente = entityManager.find(Livraria.class, livraria.getId());
        if (existente == null) {
            throw new BadRequestException("Livraria informada nao existe.");
        }
        return existente;
    }
}