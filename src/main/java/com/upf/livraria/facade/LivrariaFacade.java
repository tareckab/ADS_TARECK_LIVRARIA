package com.upf.livraria.facade;

import com.upf.livraria.entity.Livraria;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LivrariaFacade {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public void salvar(Livraria livraria) {
        em.persist(livraria);
    }

    public void atualizar(Livraria livraria) {
        em.merge(livraria);
    }

    public void remover(Integer id) {
        Livraria livraria = em.find(Livraria.class, id);
        if (livraria != null) {
            em.remove(livraria);
        }
    }

    public Livraria buscarPorId(Integer id) {
        return em.find(Livraria.class, id);
    }

    public List<Livraria> listarTodos() {
        return em.createQuery("SELECT l FROM Livraria l", Livraria.class).getResultList();
    }
}