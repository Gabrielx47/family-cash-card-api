# family-cash-card-api

API Restful para gerenciar cartões e movimentações familiares. Sistema construído com base no curso "Building a Restful Web Service" da Spring Academy.

<!-- Badges: (status do CI) (coverage) (licença) — substitua pelos links reais. -->

![Static Badge](https://img.shields.io/badge/Spring%20Boot%20-%20v3.5.3%20-%20black?style=for-the-badge&logo=springboot)      ![Static Badge](https://img.shields.io/badge/Gradle%20-%20-%20rgb(29%2C%20162%2C%20189)?style=for-the-badge&logo=gradle&logoColor=rgb(29%2C%20162%2C%20189))      ![Static Badge](https://img.shields.io/badge/H2%20Database%20-%20-%20%2309476b?style=for-the-badge&logo=h2database&logoColor=%2309476b&labelColor=d8d8d8&color=%2309476b)      ![Static Badge](https://img.shields.io/badge/Spring%20Security%20-%20-%20rgb(128%2C%20234%2C%20110)?style=for-the-badge&logo=springsecurity)      ![Static Badge](https://img.shields.io/badge/Java%20-%20-green?style=for-the-badge&logo=openjdk&logoColor=orange&labelColor=%2309476b&color=orange)


## Sumário
- [Sobre](#sobre)
- [Funcionalidades](#funcionalidades)
- [Arquitetura e Tecnologias](#arquitetura-e-tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
<!-- [Configuração](#configuração)-->
- [Executando](#executando)
- [Testes](#testes)
- [Endpoints / Documentação da API](#endpoints--documentação-da-api)
<!-- [Banco de Dados e Migrações](#banco-de-dados-e-migrações)
- [Deployment](#deployment)
- [Contribuição](#contribuição) -->
- [Roadmap](#roadmap)
- [Changelog](#changelog)
- [Licença](#licença)
- [Contato](#contato)

## Sobre
Este projeto oferece uma API REST para controlar cartões familiares, limites, transações e relatórios financeiros. 

## Funcionalidades
- Gerenciamento de cartões financeiros associados ao usuário autenticado.
- Criar cartões via `POST /cashcards`, armazenando o novo cartão com o proprietário identificado pela sessão do usuário.
- Consultar um cartão específico via `GET /cashcards/{id}`, retornando os detalhes quando o cartão pertence ao usuário autenticado.
- Listar todos os cartões do usuário via `GET /cashcards`, com suporte a paginação e ordenação por valor.
- Atualizar um cartão existente via `PUT /cashcards/{id}`, alterando o valor do cartão quando ele for encontrado e pertencente ao usuário.
- Excluir cartões via `DELETE /cashcards/{id}`, removendo o registro apenas se o cartão estiver associado ao mesmo usuário.
- Estrutura preparada para evoluir com movimentações, relatórios e saldo por cartão em próximas versões.

## Arquitetura e Tecnologias
- **Linguagem:** Java
- **Framework:** Spring Boot
- **Banco de dados:** H2 Database
- **ORM / Migrations:** JPA/Hibernate
- **Outras:** Layered Architecture, Gradle e Git

## Pré-requisitos
- Git
- Java 21

## Instalação
Clone o repositório e instale dependências:

```bash
git clone https://github.com/Gabrielx47/family-cash-card-api.git
cd family-cash-card-api/cashcard

./gradlew build
```

<!--
## Configuração
Copie o arquivo de exemplo de variáveis de ambiente e edite conforme necessário:

```bash
cp .env.example .env
# editar .env com as credenciais e configurações
```

Variáveis importantes:
- `DATABASE_URL` — string de conexão do banco
- `JWT_SECRET` — segredo para tokens (se usado)
- `PORT` — porta onde a API roda
-->

## Executando
Execução usando o Gradle Wrapper:

```bash
./gradlew bootRun
```
- Base URL da API: `http://localhost:5000`


## Testes
Executar testes unitários/integrados:

```bash
./gradlew test
```

## Endpoints / Documentação da API
Os endpoints atualmente disponíveis na API são os seguintes:

- `GET /cashcards/{id}` — retorna um cartão específico pertencente ao usuário autenticado.
- `POST /cashcards` — cria um novo cartão associado ao usuário autenticado.
- `GET /cashcards` — lista os cartões do usuário autenticado, com suporte a paginação e ordenação por valor.
- `PUT /cashcards/{id}` — atualiza o valor de um cartão existente pertencente ao usuário autenticado.
- `DELETE /cashcards/{id}` — remove um cartão existente, desde que seja do usuário autenticado.

Atualmente, a API não expõe documentação automatizada via Swagger/OpenAPI. A documentação pode ser complementada futuramente com uma camada de docs para esses endpoints.

<!--
## Banco de Dados e Migrações
Comandos úteis:

```bash
# criar migrações
npm run typeorm migration:generate -- -n nome_da_migration
# rodar migrações
npm run typeorm migration:run
```

Ou, com Docker:

```bash
docker compose run app npm run migration:run
```

## Deployment
Instruções básicas para deploy (ex: Docker, Kubernetes, serviço PaaS):

- Build da imagem: `docker build -t family-cash-card-api .`
- Push para registry e configurar ambiente de produção

## Contribuição
Instruções para contribuir:
- Abrir issue descrevendo a proposta
- Criar branch com nome `feat/<descrição>` ou `fix/<descrição>`
- Abrir Pull Request com descrição e testes
-->

## Roadmap
- Criação de uma interface web usando Vue.js.

## Changelog
Mantenha um registro das mudanças importantes (use `CHANGELOG.md` ou GitHub releases).

## Licença
Indique a licença do projeto (ex: MIT). Substitua pelo texto ou link apropriado.

## Contato
- Nome: Gabriel Mendonça
- Email: gabrieldssmendonca@gmail.com
- LinkedIn/GitHub: @gabriel--mendonca/Gabrielx47
