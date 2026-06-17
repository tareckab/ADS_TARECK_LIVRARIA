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
import java.io.IOException;
import java.io.Serializable;
import com.upf.livraria.controller.LoginController;
import jakarta.inject.Inject;


@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

     @Inject
private LoginController loginController;
        
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
        if ("00000000000".equals(cliente.getCpf()) && "123456".equals(cliente.getSenha())) {
            Cliente clienteAdmin = new Cliente();
            clienteAdmin.setId(0);
            clienteAdmin.setNome("admin");
            clienteAdmin.setCpf("00000000000");
            clienteAdmin.setSenha("123456");
            clienteAdmin.setAdmin(Boolean.TRUE);

            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("clienteLogado", clienteAdmin);
            return "/dashboard/index.xhtml?faces-redirect=true";
        }

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

    public Cliente getClienteLogado() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            return null;
        }
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session == null) {
            return null;
        }
        Object obj = session.getAttribute("clienteLogado");
        return (obj instanceof Cliente) ? (Cliente) obj : null;
    }

    public boolean isAdminLogado() {
        Cliente logado = getClienteLogado();
        return logado != null && Boolean.TRUE.equals(logado.getAdmin());
    }

    public void verificarAcessoAdmin() {
        if (!isAdminLogado()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                if (context != null) {
                    String ctxPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(ctxPath + "/dashboard/index.xhtml");
                    context.responseComplete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}