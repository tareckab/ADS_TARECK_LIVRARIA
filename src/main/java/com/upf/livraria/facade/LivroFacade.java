package com.upf.livraria.facade;

import com.upf.livraria.entity.Livro;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LivroFacade {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public void salvar(Livro livro) {
        em.persist(livro);
    }

    public void atualizar(Livro livro) {
        em.merge(livro);
    }

    public void remover(Integer id) {
        Livro livro = em.find(Livro.class, id);
        if (livro != null) {
            em.remove(livro);
        }
    }

    public Livro buscarPorId(Integer id) {
        return em.find(Livro.class, id);
    }

    public List<Livro> listarTodos() {
        return em.createQuery("SELECT l FROM Livro l", Livro.class).getResultList();
    }
}