# 📝 Projeto To-Do List (Backend API)

---

## 🎯 Objetivo Geral

Este projeto consiste em uma API RESTful de backend robusta e segura para uma aplicação de To-Do List. Seu objetivo principal é fornecer uma base sólida para o gerenciamento eficiente de tarefas e subtarefas, permitindo aos usuários criar, organizar e acompanhar seus compromissos diários de forma segura e flexível.

---

## ✨ Funcionalidades

* **Gerenciamento de Usuários:**
    * Registro de novos usuários.
    * Autenticação de usuários via JWT (JSON Web Tokens).
* **Gerenciamento de Tarefas (Tasks):**
    * Criação de novas tarefas com título, descrição, data de expiração, prioridade e status.
    * Visualização de todas as tarefas de um usuário.
    * Atualização de informações de tarefas.
    * Exclusão de tarefas (com exclusão em cascata das subtarefas associadas).
* **Gerenciamento de Subtarefas (Subtasks):**
    * Criação de subtarefas associadas a uma tarefa principal.
    * Atualização de informações de subtarefas.
    * Exclusão de subtarefas.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem & Ambiente:** Java 17
* **Framework:** Spring Boot 3.x
* **Servidor Web:** Tomcat (com `spring-boot-starter-web`)
* **Acesso a Dados:** Spring Data JPA, Hibernate
* **Banco de Dados:** PostgreSQL
* **Segurança:** Spring Security, JJWT (io.jsonwebtoken)
* **Validação:** Spring Validation
* **Ferramentas de Desenvolvimento:** Spring Boot DevTools
* **Ferramentas de Construção:** Maven
* **Contêineres:** Docker, Docker Compose
* **Geração de Código:** Lombok

---

## ⚙️ Configuração e Execução

Siga os passos abaixo para configurar e rodar o projeto em seu ambiente local.

### Pré-requisitos

Certifique-se de ter os seguintes softwares instalados em sua máquina:

* **Java Development Kit (JDK) 17 ou superior**
* **Maven**
* **Git**
* **Docker e Docker Compose**
* Um editor de código (IDE como IntelliJ IDEA, VS Code, Eclipse)

### 0. Configurar o Git (Primeira Vez - Opcional)

Se este é um projeto novo ou você deseja enviá-lo para um repositório remoto pela primeira vez, siga estes passos para inicializar o Git e fazer o primeiro push.

1.  **Inicialize o Git no seu diretório de projeto:**

    ```bash
    git init
    ```

2.  **Adicione todos os arquivos ao controle de versão:**

    ```bash
    git add .
    ```

3.  **Crie seu primeiro commit:**

    ```bash
    git commit -m "feat: commit inicial do projeto To-Do List"
    ```

4.  **Adicione o repositório remoto (ex: GitHub, GitLab):**
    *Crie um novo repositório vazio na sua plataforma preferida (GitHub, GitLab, Bitbucket) e copie a URL do repositório remoto.*

    ```bash
    git remote add origin <URL_DO_SEU_REPOSITORIO_REMOTO>
    ```

    Exemplo: `git remote add origin https://github.com/seu-usuario/seu-projeto.git`

5.  **Renomeie a branch principal (opcional, mas recomendado):**

    ```bash
    git branch -M main
    ```

6.  **Envie seus arquivos para o repositório remoto:**

    ```bash
    git push -u origin main
    ```

### 1. Clonar o Repositório

Se você já tem o projeto em um repositório remoto e quer cloná-lo, use:

```bash
git clone [https://github.com/danielbellinijf/todolistJava.git](https://github.com/danielbellinijf/todolistJava.git)
cd todolistJava/todolist
```

### 2. Configurar Variáveis de Ambiente (`.env` a partir de `.env.example`)

O projeto utiliza variáveis de ambiente para a configuração do banco de dados e do JWT. Para manter suas informações sensíveis seguras e separadas do controle de versão, é fornecido um arquivo **`.env.example`** como um **template**. Você deve **criar uma cópia** desse arquivo, renomeá-la para `.env`, e então preenchê-la com suas próprias configurações específicas para o ambiente local.

**Passos:**

1.  **Crie o arquivo `.env` a partir do template:**
    No terminal, na pasta raiz do projeto (onde está o `docker-compose.yml`), execute:

    ```bash
    cp .env.example .env
    ```

2.  **Edite o arquivo `.env`** que você acabou de criar e preencha as variáveis de acordo com suas necessidades. Certifique-se de substituir os valores `seu_usuario`, `sua_senha` e `SuaChaveSecretaUnicaAquiParaJWT` por seus dados reais:

    ```env
    # Configuração do banco PostgreSQL (usado pelo container do Postgres para criar o usuário/banco)
    POSTGRES_DB=todolistdb
    POSTGRES_USER=seu_usuario
    POSTGRES_PASSWORD=sua_senha

    # Configuração para a aplicação Spring Boot conectar no banco principal
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/todolistdb
    SPRING_DATASOURCE_USERNAME=seu_usuario
    SPRING_DATASOURCE_PASSWORD=sua_senha

    # Configuração para a aplicação Spring Boot conectar no banco de TESTE (usado em testes locais se aplicável)
    TEST_DB_URL=jdbc:postgresql://localhost:5432/todolistdb
    TEST_DB_USERNAME=seu_usuario
    TEST_DB_PASSWORD=sua_senha

    # Chave secreta base64 para assinar tokens JWT (SUBSTITUA por uma chave SEGURA e ÚNICA)
    # Exemplo de formato: 'UmaChaveSecretaMuitoLongaEComplexaParaJWTQueDeveTerPeloMenos256BitsParaSerSeguraEDecodeDeBase64Corretamente'
    # Você pode gerar uma string base64 aleatória com um gerador online ou via código.
    JWT_SECRET=SuaChaveSecretaUnicaAquiParaJWT

    # Tempo de expiração do token JWT em milissegundos (ex: 1 hora = 3600000)
    JWT_EXPIRATION=3600000
    ```

    **Importante sobre `JWT_SECRET`:** A chave secreta deve ser **longa, aleatória e segura** para garantir a integridade e segurança dos seus tokens JWT. **Não use a chave de exemplo**; gere uma própria. Certifique-se de que ela seja base64-encoded e tenha pelo menos 256 bits (o que geralmente significa uma string com pelo menos 44 caracteres após a codificação base64).

    **Atenção:** O arquivo `.env` contém informações sensíveis e **nunca deve ser committado** para o repositório Git. Certifique-se de que ele esteja listado no seu arquivo `.gitignore` para evitar vazamentos de dados.
    
## 3. Rodar a Aplicação com Docker Compose

O projeto está configurado para ser executado facilmente usando Docker e Docker Compose, o que garante que tanto a aplicação backend quanto o banco de dados PostgreSQL sejam iniciados em ambientes isolados e pré-configurados.

* **`Dockerfile`**: Este arquivo define como a imagem Docker da sua aplicação Spring Boot é construída. Ele instrui o Docker sobre o ambiente necessário (Java 17), como compilar o projeto Maven, e como empacotar a aplicação JAR para execução.
* **`docker-compose.yml`**: Este arquivo orquestra os múltiplos serviços do seu projeto (no caso, a aplicação `app` e o banco de dados `db`). Ele define as imagens a serem usadas, os mapeamentos de porta, as variáveis de ambiente e as dependências entre os serviços, simplificando o processo de inicialização de todo o ambiente de desenvolvimento.

Com o Docker e Docker Compose instalados e o arquivo `.env` configurado, você pode subir a aplicação e o banco de dados com um único comando:

```bash
docker compose up --build --force-recreate
```
* `--build`: Garante que a imagem Docker da sua aplicação (definida pelo `Dockerfile`) será reconstruída a partir do código-fonte mais recente.
* `--force-recreate`: Força a recriação dos contêineres, o que é útil para garantir que quaisquer novas configurações ou dependências sejam aplicadas e para evitar problemas de cache.

Este comando irá:

* Construir a imagem Docker da sua aplicação Spring Boot com base no `Dockerfile`.
* Iniciar um contêiner PostgreSQL (usando a imagem `postgres:15`).
* Iniciar o contêiner da sua aplicação Spring Boot, conectando-o automaticamente ao banco de dados PostgreSQL conforme configurado no `docker-compose.yml` e `.env`.

Aguarde até a aplicação ser iniciada com sucesso.

---

## 🔑 Autenticação (JWT)

Esta API utiliza JSON Web Tokens (JWT) para autenticação. Para acessar os endpoints protegidos, você precisará:

1.  **Registrar um Usuário:** Envie uma requisição para o endpoint de registro.
2.  **Fazer Login:** Após o registro, envie uma requisição para o endpoint de login com as credenciais do usuário. A resposta conterá um token JWT.
3.  **Usar o Token:** Para acessar endpoints protegidos, inclua o token JWT no cabeçalho `Authorization` de suas requisições, no formato `Bearer <seu_token_jwt>`.

## 🗺️ Endpoints da API

A API oferece os seguintes endpoints para gerenciamento de usuários, tarefas e subtarefas. Todos os endpoints que exigem autenticação devem receber um **token JWT válido** no cabeçalho `Authorization: Bearer <seu_token_jwt>`.

### Autenticação e Usuários

| Método | URI                 | Descrição                                         | Autenticação Necessária |
| :----- | :------------------ | :------------------------------------------------ | :---------------------- |
| `POST` | `/users/register`   | Registra um novo usuário na aplicação.            | Não                     |
| `POST` | `/users/login`      | Autentica um usuário e retorna um token JWT.      | Não                     |

### Tarefas (Tasks)

| Método | URI                      | Descrição                                                                 | Autenticação Necessária |
| :----- | :----------------------- | :------------------------------------------------------------------------ | :---------------------- |
| `POST` | `/tasks`                 | Cria uma nova tarefa para o usuário autenticado.                        | Sim                     |
| `GET`  | `/tasks`                 | Lista todas as tarefas do usuário autenticado, com filtros opcionais por status, prioridade e data de expiração. | Sim                     |
| `PUT`  | `/tasks/{taskId}/status` | Atualiza o status de uma tarefa específica por ID.                      | Sim                     |
| `DELETE`| `/tasks/{taskId}`        | Exclui uma tarefa por ID e todas as suas subtarefas associadas (cascata). | Sim                     |

### Subtarefas (Subtasks)

| Método | URI                           | Descrição                                                                 | Autenticação Necessária |
| :----- | :---------------------------- | :------------------------------------------------------------------------ | :---------------------- |
| `POST` | `/subtasks/{taskId}`          | Cria uma nova subtarefa associada a uma tarefa principal (`taskId`).      | Sim                     |
| `PUT`  | `/subtasks/{subtaskId}/status`| Atualiza o status de uma subtarefa específica por ID.                     | Sim                     |
| `DELETE`| `/subtasks/{subtaskId}`       | Exclui uma subtarefa específica por ID.                                   | Sim                     |

## ✅ Cobertura de Testes

O projeto conta com uma suíte de testes abrangente, garantindo a qualidade e a robustez da API. Os testes são divididos em testes de unidade para a camada de serviço e testes de integração para a camada de controller, cobrindo os principais fluxos e cenários de erro.

### Testes de Unidade (UserServiceTest)

Os testes unitários para a classe `UserService` (localizados em `src/test/java/com/todolist/api/services/UserServiceTest.java`) focam na lógica de negócio e na manipulação de dados de usuários. Eles utilizam Mockito para isolar a camada de serviço de dependências externas, como o banco de dados.

* **`testSaveUserSuccess`**: Verifica se um novo usuário é registrado e salvo com sucesso quando o nome de usuário ainda não existe.
* **`testSaveUserConflict`**: Garante que uma exceção `UserConflictException` é lançada se houver uma tentativa de registrar um usuário com um nome de usuário já existente, impedindo a persistência duplicada.
* **`testFindUserByUsernameSuccess`**: Confirma que a busca por um usuário existente retorna o modelo de usuário correto.
* **`testFindUserByUsernameNotFound`**: Assegura que uma exceção `UserNotFoundException` é lançada quando um usuário não é encontrado pelo nome de usuário.
* **`testValidatePasswordCorrectly`**: Valida que a comparação de senhas retorna `true` quando a senha fornecida pelo usuário corresponde à senha criptografada.
* **`testValidatePasswordIncorrectly`**: Verifica que a comparação de senhas retorna `false` quando a senha fornecida não corresponde à senha criptografada.

### Testes de Integração (TaskControllerTest)

Os testes de integração para `TaskController` e `SubtaskController` (localizados em `src/test/java/com/todolist/api/controllers/TaskControllerTest.java`) avaliam o comportamento da API simulando requisições HTTP reais. Eles verificam a integração entre os controladores, serviços e a lógica de segurança (JWT).

* **`whenValidToken_thenCreateTaskSuccessfully`**: Testa a criação de uma tarefa com um token JWT válido, esperando um status `201 Created`.
* **`whenInvalidToken_thenReturnsUnauthorized`**: Garante que a tentativa de criar uma tarefa com um token inválido resulte em um status `401 Unauthorized`.
* **`whenInvalidToken_thenGetAllTasksReturnsUnauthorized`**: Verifica que a listagem de tarefas com um token inválido também retorna `401 Unauthorized`.
* **`whenValidToken_thenGetAllTasksSuccessfully`**: Confirma que a listagem de todas as tarefas funciona corretamente com um token válido, incluindo a estrutura de subtarefas.
* **`whenUpdateTaskStatusToCompletedAndHasIncompleteSubtasks_thenReturnsBadRequest`**: Assegura que uma requisição para completar uma tarefa com subtarefas incompletas resulta em um `400 Bad Request`, validando a regra de negócio.
* **`whenUpdateTaskStatusToCompletedAndAllSubtasksAreCompleted_thenReturnsOk`**: Testa o cenário em que uma tarefa é concluída com sucesso quando todas as suas subtarefas já estão completas, resultando em `200 OK`.
* **`whenUpdateSubtaskStatus_thenReturnsOk`**: Verifica a atualização do status de uma subtarefa para `CONCLUIDA` com sucesso.
* **`whenDeleteTask_thenReturnsNoContent`**: Testa a exclusão de uma tarefa existente, esperando um status `204 No Content`.
* **`whenDeleteNonExistentTask_thenReturnsNotFound`**: Garante que a tentativa de excluir uma tarefa inexistente resulte em um status `404 Not Found`.

## 🤝 Contribuindo

Contribuições são bem-vindas! Se você quiser contribuir com este projeto:

1.  Faça um fork do repositório.
2.  Crie uma nova branch (`git checkout -b feature/sua-feature`).
3.  Faça suas alterações e commite-as (`git commit -m 'feat: adiciona nova feature'`).
4.  Faça push para a branch (`git push origin feature/sua-feature`).
5.  Abra um Pull Request.

---

## 📄 Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](https://www.google.com/search?q=LICENSE) para mais detalhes.
