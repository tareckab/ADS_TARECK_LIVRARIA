# Sistema de Livraria

Projeto desenvolvido utilizando Jakarta EE 10, JPA, JAX-RS e PostgreSQL para gerenciamento de livrarias, livros, clientes e reservas.

## Tecnologias Utilizadas
- Java 21
- Jakarta EE 10
- JPA (Jakarta Persistence)
- JSF facelets
- JAX-RS
- Maven
- PostgreSQL
- GlassFish Server

## Estrutura do Projeto
- controller
- entity
- resources
- view

## Modelo de Dados
Cliente:
- id
- nome
- idade
- cpf

Livraria:
- id
- nome
- endereco
- qtdLivros

Livro:
- id
- titulo
- autor
- paginas
- livraria

Livros Reservados:
- id
- livro
- cliente

## Relacionamentos
- Uma Livraria possui vários Livros.
- Um Cliente pode reservar vários Livros.
- Um Livro pode estar associado a uma reserva.

## Configuração
Datasource:
jdbc/livraria

## Execução
1. git clone <repositorio>
2. mvn clean package
3. Deploy do arquivo WAR no GlassFish

## Endpoints
Clientes:
GET, POST, PUT, DELETE

Livrarias:
GET, POST, PUT, DELETE

Livros:
GET, POST, PUT, DELETE

Reservas:
GET, POST, PUT, DELETE

Autor: Tareck
