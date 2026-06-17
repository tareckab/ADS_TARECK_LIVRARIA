package com.upf.livraria.facade;

import com.upf.livraria.entity.Cliente;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ClienteFacade {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public void salvar(Cliente cliente) {
        em.persist(cliente);
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