package com.upf.livraria.controller;

import com.upf.livraria.entity.Livro;
import com.upf.livraria.entity.Livraria;
import com.upf.livraria.facade.LivroFacade;
import com.upf.livraria.facade.LivrariaFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "livroBean")
@ViewScoped
public class LivroBean implements Serializable {

    @EJB
    private LivroFacade livroFacade;

    @EJB
    private LivrariaFacade livrariaFacade;

    private List<Livro> livros;
    private List<Livraria> livrarias;
    private Livro livroSelecionado;

    @PostConstruct
    public void init() {
        carregarLivros();
        livrarias = livrariaFacade.listarTodos();
        livroSelecionado = new Livro();
    }

    public void carregarLivros() {
        livros = livroFacade.listarTodos();
    }

    public void novo() {
        resetarFormulario();
    }

    private void resetarFormulario() {
        livroSelecionado = new Livro();
    }

    public void salvar() {
        try {
            // Validações de negócio antes de salvar
            if (livroSelecionado.getTitulo() == null || livroSelecionado.getTitulo().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Título é obrigatório"));
                return;
            }
            if (livroSelecionado.getAutor() == null || livroSelecionado.getAutor().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Autor é obrigatório"));
                return;
            }
            if (livroSelecionado.getLivraria() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Livraria é obrigatória"));
                return;
            }
            if (livroSelecionado.getPaginas() != null && livroSelecionado.getPaginas() < 1) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Páginas deve ser maior que 0"));
                return;
            }

            String acao;
            if (livroSelecionado.getId() == null) {
                livroFacade.salvar(livroSelecionado);
                acao = "criado";
            } else {
                livroFacade.atualizar(livroSelecionado);
                acao = "atualizado";
            }
            carregarLivros();
            resetarFormulario();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Livro " + acao + " com sucesso."));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao salvar livro: " + e.getMessage()));
        }
    }

    public void editar(Livro livro) {
        this.livroSelecionado = livro;
    }

    public void remover(Livro livro) {
        try {
            if (livro == null || livro.getId() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Livro inválido para remoção"));
                return;
            }
            livroFacade.remover(livro.getId());
            carregarLivros();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Livro removido com sucesso."));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao remover livro: " + e.getMessage()));
        }
    }

    public List<Livro> getLivros() { return livros; }
    public List<Livraria> getLivrarias() { return livrarias; }
    public Livro getLivroSelecionado() { return livroSelecionado; }
    public void setLivroSelecionado(Livro livroSelecionado) { this.livroSelecionado = livroSelecionado; }
}