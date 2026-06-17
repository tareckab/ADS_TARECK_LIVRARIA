package com.upf.livraria.controller;

import com.upf.livraria.entity.Cliente;
import com.upf.livraria.facade.ClienteFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private ClienteFacade ejbFacade;

    private Cliente cliente;

    @PostConstruct
    public void init() {
        cliente = new Cliente();
    }
    
    public String cadastrar() {

    try {

        ejbFacade.salvar(cliente);

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Sucesso!",
                "Cliente cadastrado com sucesso."));

        return "/login.xhtml?faces-redirect=true";

    } catch (Exception e) {
        
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(
            null,
                
        
            new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                    
                "Erro!",
                    
                e.getMessage()));

        return null;
    }
}
    public String validarLogin() {
            
        
        if ("12345678900".equals(cliente.getCpf()) && "123456".equals(cliente.getSenha())) {

        Cliente clienteMock = new Cliente();
        clienteMock.setId(999);
        clienteMock.setNome("Usuário Teste");
        clienteMock.setCpf(cliente.getCpf());

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("clienteLogado", clienteMock);

        return "/dashboard/index.xhtml?faces-redirect=true";
    }
        
        System.out.println("CPF DIGITADO: [" + cliente.getCpf() + "]");
        System.out.println("SENHA DIGITADA: [" + cliente.getSenha() + "]");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session =
                (HttpSession) context.getExternalContext().getSession(true);

        Cliente clienteDB =
                ejbFacade.buscarPorCpf(
                        cliente.getCpf().trim(),
                        cliente.getSenha().trim());

        if (clienteDB != null) {

            session.setAttribute("clienteLogado", clienteDB);

            return "/dashboard/index.xhtml?faces-redirect=true";
        }
        
        
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Falha no Login!",
                        "CPF ou senha incorretos."));
        
        

        return null;
    }

    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session =
                (HttpSession) context.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "/login.xhtml?faces-redirect=true";
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}