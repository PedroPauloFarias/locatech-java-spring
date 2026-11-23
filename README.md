# Locatech — API RESTful de locação de veículos

API desenvolvida como parte do curso **Spring MVC: APIs RESTful**, da Pós Tech FIAP + Alura. O projeto exercita a construção e a manutenção de uma API para cadastro de pessoas, veículos e aluguéis.

## Conteúdos e práticas aplicados

Ao longo do curso e da implementação, foram trabalhados:

- fundamentos de Spring MVC, Spring Boot e arquitetura em camadas;
- princípios REST e uso dos métodos HTTP `GET`, `POST`, `PUT` e `DELETE`;
- separação de responsabilidades entre controllers, services e repositories;
- acesso ao banco de dados com `JdbcClient` e SQL parametrizado;
- DTOs para entrada de dados e Bean Validation;
- tratamento centralizado de exceções e respostas HTTP;
- documentação OpenAPI/Swagger;
- versionamento de rotas;
- testes automatizados com Spring Boot e MockMvc.

## Funcionalidades

- Cadastro, listagem paginada, consulta, atualização e exclusão de pessoas e veículos.
- Criação, listagem paginada, consulta, atualização e exclusão de aluguéis.
- Validação de campos obrigatórios, e-mail, CPF e datas.
- Verificação da existência de pessoa e veículo antes de registrar um aluguel.
- Verificação de sobreposição de aluguéis para o mesmo veículo.
- Cálculo do valor do aluguel com base no valor diário do veículo e na duração do período.
- Respostas de erro padronizadas para validação (`400`), recurso não encontrado (`404`), conflito (`409`) e falhas inesperadas (`500`).

## Tecnologias

As versões abaixo correspondem à configuração atual do projeto:

- Java 21
- Spring Boot 4.1.2
- Spring MVC (`spring-boot-starter-webmvc`)
- Spring JDBC (`spring-boot-starter-jdbc`) e `JdbcClient`
- Bean Validation (`spring-boot-starter-validation`)
- H2 Database em memória
- Springdoc OpenAPI / Swagger UI 3.2.1
- Lombok
- Maven Wrapper

## Executar localmente

É necessário ter o Java 21 instalado. Na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

Para executar os testes:

```bash
./mvnw test
```

## Swagger e banco H2

Com a aplicação em execução:

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- Especificação OpenAPI em JSON: <http://localhost:8080/v3/api-docs>
- Console H2: <http://localhost:8080/h2-console>

O console H2 e as credenciais de conexão estão configurados em `src/main/resources/application.properties`. O banco usa a URL `jdbc:h2:mem:locatech`. Como ele é em memória, os dados são recriados ao reiniciar a aplicação; `src/main/resources/data.sql` insere os dados iniciais de exemplo.

## Endpoints

Os recursos disponíveis são:

| Recurso | Rotas principais |
|---|---|
| Pessoas | `/pessoas` |
| Veículos | `/veiculos` |
| Aluguéis | `/alugueis` |

Cada recurso possui operações de listagem paginada (`GET`), busca por ID (`GET /{id}`), criação (`POST`), atualização (`PUT /{id}`) e exclusão (`DELETE /{id}`), conforme documentado no Swagger.

As listagens recebem os parâmetros `page` (a partir de 1) e `size` (de 1 a 100). Por exemplo:

```text
GET http://localhost:8080/v1/veiculos?page=1&size=10
```

## Versionamento

As rotas originais permanecem disponíveis. Também é possível acessar os mesmos endpoints com os prefixos `/v1` e `/v2`, por exemplo:

```text
/pessoas
/v1/pessoas
/v2/pessoas
```

Nesta etapa, `/v1` e `/v2` compartilham a mesma implementação e o mesmo contrato. A separação permite que versões futuras evoluam sem remover imediatamente as rotas antigas; ainda não há diferenças funcionais entre v1 e v2.

## Organização do código

O código da aplicação está em `src/main/java/br/com/fiap/locatech/locatech`:

- `controllers`: endpoints HTTP e documentação OpenAPI;
- `services`: regras de negócio, validações de fluxo e cálculos;
- `repositores`: consultas SQL e persistência com `JdbcClient`;
- `entities`: modelos de domínio;
- `dtos`: contratos de entrada e saída;
- `validations`: validações personalizadas, incluindo CPF;
- `controllers/handlers`: tratamento centralizado de exceções.

## Uso de inteligência artificial

Foi utilizada inteligência artificial como apoio ao estudo, para esclarecer dúvidas, discutir alternativas de implementação e revisar código. As sugestões foram avaliadas no contexto do projeto e verificadas por compilação e testes; a ferramenta foi usada como apoio ao aprendizado, não como substituta da compreensão das decisões implementadas.
