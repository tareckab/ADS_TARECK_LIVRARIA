package com.upf.livraria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "reserva")
public class LivrosReservados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "IdLivro")
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "IdCliente")
    private Cliente cliente;
    
    
    private LocalDate dataReserva;

    private LocalDate dataDevolucao;

    private Boolean devolvido;
    
    public LocalDate getDataReserva() {
    return dataReserva;
}

public void setDataReserva(LocalDate dataReserva) {
    this.dataReserva = dataReserva;
}

public LocalDate getDataDevolucao() {
    return dataDevolucao;
}

public void setDataDevolucao(LocalDate dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
}

public Boolean getDevolvido() {
    return devolvido;
}

public void setDevolvido(Boolean devolvido) {
    this.devolvido = devolvido;
}
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}