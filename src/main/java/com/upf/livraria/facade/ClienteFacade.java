package com.upf.livraria.facade;

import com.upf.livraria.entity.Cliente;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ClienteFacade {
    
    
    
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public void salvar(Cliente cliente) {
        em.persist(cliente);
    }

    public void atualizar(Cliente cliente) {
        em.merge(cliente);
    }

    public void remover(Integer id) {
        Cliente cliente = em.find(Cliente.class, id);
        if (cliente != null) {
            em.remove(cliente);
        }
    }

    public Cliente buscarPorId(Integer id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> listarTodos() {
        return em.createQuery("SELECT c FROM Cliente c ORDER BY c.nome", Cliente.class).getResultList();
    }

    public Cliente buscarPorCpf(String cpf) {
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE c.cpf = :cpf", Cliente.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Cliente buscarPorCpf(String cpf, String senha) {
        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c "
                    + "WHERE c.cpf = :cpf "
                    + "AND c.senha = :senha",
                    Cliente.class)
                    .setParameter("cpf", cpf)
                    .setParameter("senha", senha)
                    .getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}