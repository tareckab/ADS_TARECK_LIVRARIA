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
        livroSelecionado = new Livro();
    }

    public void salvar() {
        try {
            if (livroSelecionado.getId() == null) {
                livroFacade.salvar(livroSelecionado);
            } else {
                livroFacade.atualizar(livroSelecionado);
            }
            carregarLivros();
            livroSelecionado = new Livro();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Livro salvo."));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void editar(Livro livro) {
        this.livroSelecionado = livro;
    }

    public void remover(Livro livro) {
        try {
            livroFacade.remover(livro.getId());
            carregarLivros();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Livro removido."));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public List<Livro> getLivros() { return livros; }
    public List<Livraria> getLivrarias() { return livrarias; }
    public Livro getLivroSelecionado() { return livroSelecionado; }
    public void setLivroSelecionado(Livro livroSelecionado) { this.livroSelecionado = livroSelecionado; }
}