package com.upf.livraria.controller;

import com.upf.livraria.entity.Livraria;
import com.upf.livraria.facade.LivrariaFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "livrariaBean")
@ViewScoped
public class LivrariaBean implements Serializable {

    @EJB
    private LivrariaFacade livrariaFacade;

    private List<Livraria> livrarias;
    private Livraria livrariaSelecionada;

    @PostConstruct
    public void init() {
        carregarLivrarias();
        livrariaSelecionada = new Livraria();
    }

    public void carregarLivrarias() {
        livrarias = livrariaFacade.listarTodos();
    }

    public void novo() {
        livrariaSelecionada = new Livraria();
    }

    public void salvar() {
        try {
            if (livrariaSelecionada.getId() == null) {
                livrariaFacade.salvar(livrariaSelecionada);
            } else {
                livrariaFacade.atualizar(livrariaSelecionada);
            }
            carregarLivrarias();
            livrariaSelecionada = new Livraria();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Livraria salva."));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void editar(Livraria livraria) {
        this.livrariaSelecionada = livraria;
    }
    
    
    public void remover(Livraria livraria) {
        try {
            livrariaFacade.remover(livraria.getId());
            carregarLivrarias();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Livraria removida."));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public List<Livraria> getLivrarias() { return livrarias; }
    public Livraria getLivrariaSelecionada() { return livrariaSelecionada; }
    public void setLivrariaSelecionada(Livraria livrariaSelecionada) { this.livrariaSelecionada = livrariaSelecionada; }
}