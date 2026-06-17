package com.upf.livraria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "Autor", nullable = false, length = 100)
    private String autor;

    @Column(name = "Paginas")
    private Integer paginas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraria_id") // confirme o nome real dessa coluna FK no banco
    private Livraria livraria;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Integer getPaginas() { return paginas; }
    public void setPaginas(Integer paginas) { this.paginas = paginas; }
    public Livraria getLivraria() { return livraria; }
    public void setLivraria(Livraria livraria) { this.livraria = livraria; }
}