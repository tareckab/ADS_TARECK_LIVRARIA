package com.upf.livraria.view;

import com.upf.livraria.entity.Cliente;
import com.upf.livraria.entity.Livro;
import com.upf.livraria.entity.Livraria;
import com.upf.livraria.entity.LivrosReservados;
import com.upf.livraria.facade.LivrariaFacade;
import com.upf.livraria.facade.LivroFacade;
import jakarta.ejb.EJB;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@ViewScoped
public class LivrariaView implements Serializable {

    private final List<Livraria> livrarias = new ArrayList<>();
    private final List<Livro> livros = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<LivrosReservados> reservas = new ArrayList<>();

    private Livraria livrariaForm;
    private Livro livroForm;
    private Cliente clienteForm;
    private LivrosReservados reservaForm;

    // seleção e filtro para a UI de livrarias
    private Livraria selectedLivraria;
    private String filtroNome;

    private Integer livroLivrariaId;
    private Integer reservaLivroId;
    private Integer reservaClienteId;

    private Integer proximoIdLivraria = 1;
    private Integer proximoIdLivro = 1;
    private Integer proximoIdCliente = 1;
    private Integer proximoIdReserva = 1;

    @EJB
    private LivrariaFacade livrariaFacade;

    @EJB
    private LivroFacade livroFacade;

    private boolean dbAtivo;

    @PostConstruct
    public void init() {
        novoLivraria();
        novoLivro();
        novoCliente();
        novaReserva();
        carregarDados();
    }

    private void seedData() {
        livrarias.clear();
        livros.clear();
        clientes.clear();
        reservas.clear();
        proximoIdLivraria = 1;
        proximoIdLivro = 1;
        proximoIdCliente = 1;
        proximoIdReserva = 1;

        Livraria central = criarLivraria("Livraria Central", "Rua das Letras, 123");
        Livraria norte = criarLivraria("Livraria Norte", "Av. dos Livros, 456");

        Livro domCasmurro = criarLivro("Dom Casmurro", "Machado de Assis", 256, central);
        Livro harry = criarLivro("Harry Potter e a Pedra Filosofal", "J. K. Rowling", 320, central);
        Livro cemAnos = criarLivro("Cem Anos de Solidão", "Gabriel García Márquez", 448, norte);

        atualizarQuantidadeLivrosDasLivrarias();

        Cliente ana = criarCliente("Ana Souza", 26, "12345678901");
        Cliente bruno = criarCliente("Bruno Lima", 32, "98765432100");

        criarReserva(domCasmurro, ana);
        criarReserva(cemAnos, bruno);
        criarReserva(harry, ana);
    }

    private Livraria criarLivraria(String nome, String endereco) {
        Livraria livraria = new Livraria();
        livraria.setId(proximoIdLivraria++);
        livraria.setNome(nome);
        livraria.setEndereco(endereco);
        livraria.setQtdLivros(0);
        livrarias.add(livraria);
        return livraria;
    }

    private Livro criarLivro(String titulo, String autor, Integer paginas, Livraria livraria) {
        Livro livro = new Livro();
        livro.setId(proximoIdLivro++);
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setPaginas(paginas);
        livro.setLivraria(livraria);
        livros.add(livro);
        return livro;
    }

    private Cliente criarCliente(String nome, Integer idade, String cpf) {
        Cliente cliente = new Cliente();
        cliente.setId(proximoIdCliente++);
        cliente.setNome(nome);
        cliente.setIdade(idade);
        cliente.setCpf(cpf);
        clientes.add(cliente);
        return cliente;
    }

    private LivrosReservados criarReserva(Livro livro, Cliente cliente) {
        LivrosReservados reserva = new LivrosReservados();
        reserva.setId(proximoIdReserva++);
        reserva.setLivro(livro);
        reserva.setCliente(cliente);
        reservas.add(reserva);
        return reserva;
    }

    public void novoLivraria() {
        livrariaForm = new Livraria();
    }

    public void carregarDados() {
        try {
            if (livrariaFacade != null && livroFacade != null) {
                carregarLivrarias();
                carregarLivros();
                atualizarQuantidadeLivrosDasLivrarias();
                dbAtivo = true;
                return;
            }
        } catch (Exception e) {
            // erro de carga, fallback para seed em memória
        }
        dbAtivo = false;
        seedData();
    }

    private void carregarLivrarias() {
        livrarias.clear();
        livrarias.addAll(livrariaFacade.listarTodos());
    }

    private void carregarLivros() {
        livros.clear();
        livros.addAll(livroFacade.listarTodos());
    }

    public void verLivrosDa(Livraria livraria) {
        this.selectedLivraria = livraria;
    }

    public Livraria getSelectedLivraria() {
        return selectedLivraria;
    }

    public void setSelectedLivraria(Livraria selectedLivraria) {
        this.selectedLivraria = selectedLivraria;
    }

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
    }

    public List<Livraria> getLivrariasFiltradas() {
        if (filtroNome == null || filtroNome.trim().isEmpty()) {
            return new ArrayList<>(livrarias);
        }
        String f = filtroNome.trim().toLowerCase();
        List<Livraria> resultado = new ArrayList<>();
        for (Livraria l : livrarias) {
            if (l.getNome() != null && l.getNome().toLowerCase().contains(f)) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    public void editarLivraria(Livraria livraria) {
        livrariaForm = copiarLivraria(livraria);
    }

    public void salvarLivraria() {
        try {
            if (dbAtivo) {
                if (livrariaForm.getId() == null) {
                    livrariaFacade.salvar(livrariaForm);
                } else {
                    livrariaFacade.atualizar(livrariaForm);
                }
                carregarLivrarias();
            } else {
                if (livrariaForm.getId() == null) {
                    livrariaForm.setId(proximoIdLivraria++);
                    livrarias.add(livrariaForm);
                } else {
                    substituirLivraria(livrariaForm);
                }
            }
            novoLivraria();
            mensagem("Livraria salva com sucesso.");
        } catch (Exception e) {
            mensagem("Erro ao salvar livraria: " + e.getMessage());
        }
    }

    public void excluirLivraria(Livraria livraria) {
        try {
            if (dbAtivo && livraria.getId() != null) {
                livrariaFacade.remover(livraria.getId());
                carregarLivros();
                carregarLivrarias();
            } else {
                livrarias.removeIf(item -> Objects.equals(item.getId(), livraria.getId()));
                livros.stream()
                        .filter(livro -> livro.getLivraria() != null && Objects.equals(livro.getLivraria().getId(), livraria.getId()))
                        .forEach(livro -> livro.setLivraria(null));
            }
            if (livrariaForm != null && Objects.equals(livrariaForm.getId(), livraria.getId())) {
                novoLivraria();
            }
            mensagem("Livraria removida.");
        } catch (Exception e) {
            mensagem("Erro ao remover livraria: " + e.getMessage());
        }
    }

    public void novoLivro() {
        livroForm = new Livro();
        livroLivrariaId = null;
    }

    public void editarLivro(Livro livro) {
        livroForm = copiarLivro(livro);
        livroLivrariaId = livro.getLivraria() == null ? null : livro.getLivraria().getId();
    }

    public void salvarLivro() {
        try {
            // garante que a livraria seja uma entidade gerenciada ou referência
            livroForm.setLivraria(resolveLivrariaPorId(livroLivrariaId));
            if (livroForm.getId() == null) {
                livroFacade.salvar(livroForm);
            } else {
                livroFacade.atualizar(livroForm);
            }
            // recarrega lista de livros e livrarias
            livros.clear();
            livros.addAll(livroFacade.listarTodos());
            livrarias.clear();
            livrarias.addAll(livrariaFacade.listarTodos());
            atualizarQuantidadeLivrosDasLivrarias();
            novoLivro();
            mensagem("Livro salvo com sucesso.");
        } catch (Exception e) {
            mensagem("Erro ao salvar livro: " + e.getMessage());
        }
    }

    public void excluirLivro(Livro livro) {
        try {
            if (dbAtivo && livro.getId() != null) {
                livroFacade.remover(livro.getId());
                carregarLivros();
                carregarLivrarias();
                atualizarQuantidadeLivrosDasLivrarias();
            } else {
                livros.removeIf(item -> Objects.equals(item.getId(), livro.getId()));
                reservas.removeIf(reserva -> reserva.getLivro() != null && Objects.equals(reserva.getLivro().getId(), livro.getId()));
                atualizarQuantidadeLivrosDasLivrarias();
            }
            if (livroForm != null && Objects.equals(livroForm.getId(), livro.getId())) {
                novoLivro();
            }
            mensagem("Livro removido.");
        } catch (Exception e) {
            mensagem("Erro ao remover livro: " + e.getMessage());
        }
    }

    public void novoCliente() {
        clienteForm = new Cliente();
    }

    public void editarCliente(Cliente cliente) {
        clienteForm = copiarCliente(cliente);
    }

    public void salvarCliente() {
        if (clienteForm.getId() == null) {
            clienteForm.setId(proximoIdCliente++);
            clientes.add(clienteForm);
        } else {
            substituirCliente(clienteForm);
        }
        novoCliente();
        mensagem("Cliente salvo com sucesso.");
    }

    public void excluirCliente(Cliente cliente) {
        clientes.removeIf(item -> Objects.equals(item.getId(), cliente.getId()));
        reservas.removeIf(reserva -> reserva.getCliente() != null && Objects.equals(reserva.getCliente().getId(), cliente.getId()));
        if (clienteForm != null && Objects.equals(clienteForm.getId(), cliente.getId())) {
            novoCliente();
        }
        mensagem("Cliente removido.");
    }

    public void novaReserva() {
        reservaForm = new LivrosReservados();
        reservaLivroId = null;
        reservaClienteId = null;
    }

    public void editarReserva(LivrosReservados reserva) {
        reservaForm = copiarReserva(reserva);
        reservaLivroId = reserva.getLivro() == null ? null : reserva.getLivro().getId();
        reservaClienteId = reserva.getCliente() == null ? null : reserva.getCliente().getId();
    }

    public void salvarReserva() {
        reservaForm.setLivro(resolveLivroPorId(reservaLivroId));
        reservaForm.setCliente(resolveClientePorId(reservaClienteId));
        if (reservaForm.getId() == null) {
            reservaForm.setId(proximoIdReserva++);
            reservas.add(reservaForm);
        } else {
            substituirReserva(reservaForm);
        }
        novaReserva();
        mensagem("Reserva salva com sucesso.");
    }

    public void excluirReserva(LivrosReservados reserva) {
        reservas.removeIf(item -> Objects.equals(item.getId(), reserva.getId()));
        if (reservaForm != null && Objects.equals(reservaForm.getId(), reserva.getId())) {
            novaReserva();
        }
        mensagem("Reserva removida.");
    }

    public List<Livraria> getLivrarias() {
        return livrarias;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<LivrosReservados> getReservas() {
        return reservas;
    }

    public Livraria getLivrariaForm() {
        return livrariaForm;
    }

    public void setLivrariaForm(Livraria livrariaForm) {
        this.livrariaForm = livrariaForm;
    }

    public Livro getLivroForm() {
        return livroForm;
    }

    public void setLivroForm(Livro livroForm) {
        this.livroForm = livroForm;
    }

    public Integer getLivroLivrariaId() {
        return livroLivrariaId;
    }

    public void setLivroLivrariaId(Integer livroLivrariaId) {
        this.livroLivrariaId = livroLivrariaId;
    }

    public Cliente getClienteForm() {
        return clienteForm;
    }

    public void setClienteForm(Cliente clienteForm) {
        this.clienteForm = clienteForm;
    }

    public LivrosReservados getReservaForm() {
        return reservaForm;
    }

    public void setReservaForm(LivrosReservados reservaForm) {
        this.reservaForm = reservaForm;
    }

    public Integer getReservaLivroId() {
        return reservaLivroId;
    }

    public void setReservaLivroId(Integer reservaLivroId) {
        this.reservaLivroId = reservaLivroId;
    }

    public Integer getReservaClienteId() {
        return reservaClienteId;
    }

    public void setReservaClienteId(Integer reservaClienteId) {
        this.reservaClienteId = reservaClienteId;
    }

    public List<Livro> getLivrosPorLivraria(Livraria livraria) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : livros) {
            if (livro.getLivraria() != null && Objects.equals(livro.getLivraria().getId(), livraria.getId())) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public List<Livro> getLivrosDaSelecionada() {
        if (selectedLivraria == null) {
            return new ArrayList<>();
        }
        return getLivrosPorLivraria(selectedLivraria);
    }

    public String getLivrosReservadosDoCliente(Cliente cliente) {
        StringBuilder builder = new StringBuilder();
        for (LivrosReservados reserva : reservas) {
            if (reserva.getCliente() != null && Objects.equals(reserva.getCliente().getId(), cliente.getId()) && reserva.getLivro() != null) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(reserva.getLivro().getTitulo());
            }
        }
        return builder.length() == 0 ? "Nenhum livro reservado" : builder.toString();
    }

    public String getNomeLivraria(Livro livro) {
        return livro.getLivraria() == null ? "Sem livraria" : livro.getLivraria().getNome();
    }

    public String getLivroReserva(LivrosReservados reserva) {
        return reserva.getLivro() == null ? "" : reserva.getLivro().getTitulo();
    }

    public String getClienteReserva(LivrosReservados reserva) {
        return reserva.getCliente() == null ? "" : reserva.getCliente().getNome();
    }

    private void substituirLivraria(Livraria atualizada) {
        for (int i = 0; i < livrarias.size(); i++) {
            if (Objects.equals(livrarias.get(i).getId(), atualizada.getId())) {
                livrarias.set(i, atualizada);
                return;
            }
        }
    }

    private void substituirLivro(Livro atualizado) {
        for (int i = 0; i < livros.size(); i++) {
            if (Objects.equals(livros.get(i).getId(), atualizado.getId())) {
                livros.set(i, atualizado);
                return;
            }
        }
    }

    private void substituirCliente(Cliente atualizado) {
        for (int i = 0; i < clientes.size(); i++) {
            if (Objects.equals(clientes.get(i).getId(), atualizado.getId())) {
                clientes.set(i, atualizado);
                return;
            }
        }
    }

    private void substituirReserva(LivrosReservados atualizada) {
        for (int i = 0; i < reservas.size(); i++) {
            if (Objects.equals(reservas.get(i).getId(), atualizada.getId())) {
                reservas.set(i, atualizada);
                return;
            }
        }
    }

    private Livraria copiarLivraria(Livraria livraria) {
        Livraria copia = new Livraria();
        copia.setId(livraria.getId());
        copia.setNome(livraria.getNome());
        copia.setEndereco(livraria.getEndereco());
        copia.setQtdLivros(livraria.getQtdLivros());
        return copia;
    }

    private Livro copiarLivro(Livro livro) {
        Livro copia = new Livro();
        copia.setId(livro.getId());
        copia.setTitulo(livro.getTitulo());
        copia.setAutor(livro.getAutor());
        copia.setPaginas(livro.getPaginas());
        return copia;
    }

    private Cliente copiarCliente(Cliente cliente) {
        Cliente copia = new Cliente();
        copia.setId(cliente.getId());
        copia.setNome(cliente.getNome());
        copia.setIdade(cliente.getIdade());
        copia.setCpf(cliente.getCpf());
        return copia;
    }

    private LivrosReservados copiarReserva(LivrosReservados reserva) {
        LivrosReservados copia = new LivrosReservados();
        copia.setId(reserva.getId());
        return copia;
    }

    private Livraria resolveLivrariaPorId(Integer id) {
        if (id == null) {
            return null;
        }
        return livrarias.stream().filter(item -> Objects.equals(item.getId(), id)).findFirst().orElse(null);
    }

    private Livro resolveLivroPorId(Integer id) {
        if (id == null) {
            return null;
        }
        return livros.stream().filter(item -> Objects.equals(item.getId(), id)).findFirst().orElse(null);
    }

    private Cliente resolveClientePorId(Integer id) {
        if (id == null) {
            return null;
        }
        return clientes.stream().filter(item -> Objects.equals(item.getId(), id)).findFirst().orElse(null);
    }

    private void atualizarQuantidadeLivrosDasLivrarias() {
        for (Livraria livraria : livrarias) {
            int total = 0;
            for (Livro livro : livros) {
                if (livro.getLivraria() != null && Objects.equals(livro.getLivraria().getId(), livraria.getId())) {
                    total++;
                }
            }
            livraria.setQtdLivros(total);
        }
    }

    private void mensagem(String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, null));
    }
}