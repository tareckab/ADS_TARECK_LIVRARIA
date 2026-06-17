package com.upf.livraria.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "livraria")
public class Livraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "Endereco", length = 200)
    private String endereco;

    @Column(name = "qtd_livros")
    private Integer qtdLivros;
    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Livraria)) return false;
    Livraria l = (Livraria) o;
    return Objects.equals(this.id, l.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Integer getQtdLivros() { return qtdLivros; }
    public void setQtdLivros(Integer qtdLivros) { this.qtdLivros = qtdLivros; }
}