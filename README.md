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
