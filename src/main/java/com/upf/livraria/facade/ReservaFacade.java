package com.upf.livraria.facade;

import com.upf.livraria.entity.LivrosReservados;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ReservaFacade {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean livroJaReservado(Integer livroId) {

        String jpql =
            "SELECT r FROM LivrosReservados r " +
            "WHERE r.livro.id = :livroId " +
            "AND r.devolvido = false";

        return !entityManager.createQuery(jpql, LivrosReservados.class)
                .setParameter("livroId", livroId)
                .getResultList()
                .isEmpty();
        
    }  
        
        
        public void salvar(LivrosReservados reserva) {
            entityManager.persist(reserva);
}

public void atualizar(LivrosReservados reserva) {
    entityManager.merge(reserva);
}


public List<LivrosReservados> buscarPorCliente(Integer clienteId) {

    String jpql =
        "SELECT r FROM LivrosReservados r " +
        "WHERE r.cliente.id = :clienteId " +
        "AND r.devolvido = false";

    return entityManager
            .createQuery(jpql, LivrosReservados.class)
            .setParameter("clienteId", clienteId)
            .getResultList();
}
    }


