package com.upf.livraria.controller;

import com.upf.livraria.entity.Cliente;
import com.upf.livraria.entity.Livro;
import com.upf.livraria.entity.LivrosReservados;
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

@Path("livros-reservados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivrosReservadosController {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager entityManager;

    @GET
    public List<LivrosReservados> listar() {
        return entityManager.createQuery("SELECT lr FROM LivrosReservados lr", LivrosReservados.class).getResultList();
    }

    @GET
    @Path("{id}")
    public Response buscar(@PathParam("id") Integer id) {
        LivrosReservados reserva = entityManager.find(LivrosReservados.class, id);
        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(reserva).build();
    }

    @POST
    @Transactional
    public Response criar(LivrosReservados reserva) {
        reserva.setLivro(resolveLivro(reserva.getLivro()));
        reserva.setCliente(resolveCliente(reserva.getCliente()));
        entityManager.persist(reserva);
        return Response.status(Response.Status.CREATED).entity(reserva).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Integer id, LivrosReservados dados) {
        LivrosReservados reserva = entityManager.find(LivrosReservados.class, id);
        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        reserva.setLivro(resolveLivro(dados.getLivro()));
        reserva.setCliente(resolveCliente(dados.getCliente()));

        return Response.ok(reserva).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remover(@PathParam("id") Integer id) {
        LivrosReservados reserva = entityManager.find(LivrosReservados.class, id);
        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        entityManager.remove(reserva);
        return Response.noContent().build();
    }

    private Livro resolveLivro(Livro livro) {
        if (livro == null || livro.getId() == null) {
            return null;
        }

        Livro existente = entityManager.find(Livro.class, livro.getId());
        if (existente == null) {
            throw new BadRequestException("Livro informado nao existe.");
        }
        return existente;
    }

    private Cliente resolveCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            return null;
        }

        Cliente existente = entityManager.find(Cliente.class, cliente.getId());
        if (existente == null) {
            throw new BadRequestException("Cliente informado nao existe.");
        }
        return existente;
    }
}