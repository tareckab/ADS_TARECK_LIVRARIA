package com.upf.livraria.controller;

import com.upf.livraria.facade.ClienteFacade;
import com.upf.livraria.facade.LivroFacade;
import com.upf.livraria.facade.LivrariaFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {

    @EJB
    private ClienteFacade clienteFacade;

    @EJB
    private LivroFacade livroFacade;

    @EJB
    private LivrariaFacade livrariaFacade;

    private int totalClientes;
    private int totalLivros;
    private int totalLivrarias;

    @PostConstruct
    public void init() {
        totalClientes = clienteFacade.listarTodos().size();
        totalLivros = livroFacade.listarTodos().size();
        totalLivrarias = livrariaFacade.listarTodos().size();
    }

    public int getTotalClientes() { return totalClientes; }
    public int getTotalLivros() { return totalLivros; }
    public int getTotalLivrarias() { return totalLivrarias; }
}