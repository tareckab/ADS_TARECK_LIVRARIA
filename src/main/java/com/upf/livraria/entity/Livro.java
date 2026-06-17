package com.upf.livraria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "Título não pode exceder 150 caracteres")
    @Column(name = "Titulo", nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Size(max = 100, message = "Autor não pode exceder 100 caracteres")
    @Column(name = "Autor", nullable = false, length = 100)
    private String autor;

    @Min(value = 1, message = "Páginas deve ser maior que 0")
    @Column(name = "Paginas")
    private Integer paginas;

    @NotNull(message = "Livraria é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraria_id", nullable = false)
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