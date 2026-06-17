package com.upf.livraria.view;

import com.upf.livraria.entity.Cliente;
import com.upf.livraria.facade.ClienteFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("clienteView")
@ViewScoped
public class ClienteView implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteFacade clienteFacade;

    private List<Cliente> clientes;
    private Cliente clienteForm;
    private String ordemCliente = "asc";

    @PostConstruct
    public void init() {
        carregarClientes();
        novoCliente();
    }

    private void carregarClientes() {
        try {
            clientes = clienteFacade.listarTodos();
        } catch (Exception e) {
            clientes = new ArrayList<>();
        }
        ordenarClientes(ordemCliente);
    }

    public void ordenarClientes(String ordem) {
        if (ordem != null && !ordem.isEmpty()) {
            ordemCliente = ordem;
        }
        if (clientes == null) {
            return;
        }
        clientes.sort((c1, c2) -> {
            String n1 = c1.getNome() == null ? "" : c1.getNome();
            String n2 = c2.getNome() == null ? "" : c2.getNome();
            return "desc".equalsIgnoreCase(ordemCliente)
                    ? n2.compareToIgnoreCase(n1)
                    : n1.compareToIgnoreCase(n2);
        });
    }

    public void novoCliente() {
        clienteForm = new Cliente();
    }

    public void resetarFormulario() {
        clienteForm = new Cliente();
    }

    public void editarCliente(Cliente cliente) {
        if (cliente != null) {
            clienteForm = cliente;
        }
    }

    public void salvarCliente() {
        try {
            if (clienteForm.getId() == null) {
                Cliente existente = clienteFacade.buscarPorCpf(clienteForm.getCpf());
                if (existente != null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF duplicado", "Já existe um cliente cadastrado com este CPF."));
                    return;
                }
                clienteFacade.salvar(clienteForm);
            } else {
                Cliente existente = clienteFacade.buscarPorCpf(clienteForm.getCpf());
                if (existente != null && !existente.getId().equals(clienteForm.getId())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF duplicado", "Já existe um cliente cadastrado com este CPF."));
                    return;
                }
                clienteFacade.atualizar(clienteForm);
            }
            carregarClientes();
            novoCliente();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente salvo com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao salvar cliente: " + e.getMessage()));
        }
    }

    public void excluirCliente(Cliente cliente) {
        try {
            if (cliente != null && cliente.getId() != null) {
                clienteFacade.remover(cliente.getId());
                carregarClientes();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente removido com sucesso."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao remover cliente: " + e.getMessage()));
        }
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public Cliente getClienteForm() {
        return clienteForm;
    }

    public void setClienteForm(Cliente clienteForm) {
        this.clienteForm = clienteForm;
    }
}
