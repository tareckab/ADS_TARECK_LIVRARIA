package com.upf.livraria.controller;

import com.upf.livraria.facade.ClienteFacade;
import com.upf.livraria.facade.LivroFacade;
import com.upf.livraria.facade.LivrariaFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
private int totalReservas;
private List<String> livrosReservados = new ArrayList<>();
private List<String> proximasDevolucoes = new ArrayList<>();

@PostConstruct
public void init() {
    totalClientes = clienteFacade.listarTodos().size();
    totalLivros = livroFacade.listarTodos().size();
    totalLivrarias = livrariaFacade.listarTodos().size();

    // TODO: trocar por dados reais quando criarmos o ReservaFacade
    totalReservas = 3;
    livrosReservados = List.of("Dom Casmurro", "1984", "O Hobbit");
    proximasDevolucoes = List.of("Dom Casmurro - 20/06", "1984 - 25/06");
}

public int getTotalReservas() { return totalReservas; }
public List<String> getLivrosReservados() { return livrosReservados; }
public List<String> getProximasDevolucoes() { return proximasDevolucoes; }
    
    
    
    public int getTotalClientes() { return totalClientes; }
    public int getTotalLivros() { return totalLivros; }
    public int getTotalLivrarias() { return totalLivrarias; }
}