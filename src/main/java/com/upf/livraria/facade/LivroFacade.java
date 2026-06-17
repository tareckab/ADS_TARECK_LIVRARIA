package com.upf.livraria.facade;

import com.upf.livraria.entity.Livro;
import com.upf.livraria.entity.Livraria;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class LivroFacade {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    /**
     * Salva um novo livro no banco de dados
     * @param livro o livro a ser salvo (não deve ser nulo)
     * @throws IllegalArgumentException se livro for nulo ou inválido
     */
    public void salvar(@Valid @NotNull(message = "Livro não pode ser nulo") Livro livro) {
        if (livro.getId() != null) {
            throw new IllegalArgumentException("Novo livro não deve ter ID definido");
        }
        if (livro.getLivraria() == null) {
            throw new IllegalArgumentException("Livraria é obrigatória");
        }
        // Garantir que a livraria associada seja uma referência gerenciada
        if (livro.getLivraria().getId() != null) {
            Livraria managed = em.getReference(Livraria.class, livro.getLivraria().getId());
            livro.setLivraria(managed);
        }
        em.persist(livro);
    }

    /**
     * Atualiza um livro existente
     * @param livro o livro a ser atualizado
     * @throws IllegalArgumentException se livro for nulo, sem ID ou livraria
     */
    public void atualizar(@Valid @NotNull(message = "Livro não pode ser nulo") Livro livro) {
        if (livro.getId() == null) {
            throw new IllegalArgumentException("Livro a atualizar deve ter ID definido");
        }
        if (livro.getLivraria() == null) {
            throw new IllegalArgumentException("Livraria é obrigatória");
        }
        // Verifica se o livro existe antes de atualizar
        Livro existing = em.find(Livro.class, livro.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Livro com ID " + livro.getId() + " não encontrado");
        }
        // Garantir que a livraria associada seja uma referência gerenciada
        if (livro.getLivraria().getId() != null) {
            Livraria managed = em.getReference(Livraria.class, livro.getLivraria().getId());
            livro.setLivraria(managed);
        }
        em.merge(livro);
    }

    /**
     * Remove um livro pelo ID
     * @param id o ID do livro a remover
     * @throws IllegalArgumentException se ID for nulo ou livro não existir
     */
    public void remover(@NotNull(message = "ID do livro não pode ser nulo") Integer id) {
        Livro livro = em.find(Livro.class, id);
        if (livro == null) {
            throw new IllegalArgumentException("Livro com ID " + id + " não encontrado");
        }
        em.remove(livro);
    }

    /**
     * Busca um livro pelo ID
     * @param id o ID do livro
     * @return o livro ou null se não encontrado
     */
    public Livro buscarPorId(Integer id) {
        return em.find(Livro.class, id);
    }

    /**
     * Lista todos os livros
     * @return lista de livros
     */
    public List<Livro> listarTodos() {
        // Faz JOIN FETCH para carregar a livraria associada e evitar problemas
        // de carregamento LAZY durante a renderização na view
        return em.createQuery("SELECT l FROM Livro l JOIN FETCH l.livraria ORDER BY l.titulo", Livro.class)
                 .getResultList();
    }
}