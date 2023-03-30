# Getting Started

### Reference Documentation

Passos para executar a aplicação:

* Acessar a raiz do projecto e executar o comando `docker compose up -d` para subir os 
 seguintes serviços via docker:
  * Postgres
  * pgAdmin
  * Redis
  * Zookeeper
  * Kafka
* Login no pgAdmin através da url `localhost:5050` usuário:` admin@email.com` senha:`admin`
* Criar o database `dgs-lab` no schema postgres.
* Criar a tabela user. O script está na raiz em `db/script.sql`
* Este Microservice utiliza Spring Security com autenticação em memória, com usuário:`dgs` e senha:`dgs123`
* Somente a rota `/api/v1/hello/**` não utiliza basic authentication
* A collection para realizar testes está na raiz.

Para utilizar o recurso do Spring Cloud Openfeign, foi criado um outro microserviço que fará
requisição no endpoint `api/v1/hello`.

* [link do projeto client](https://github.com/fabribh/dgs-client)
