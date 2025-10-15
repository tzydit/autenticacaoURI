# Projeto de Atividades Kotlin com Firebase e Banco de Dados Local

Este é um projeto de aplicação Android desenvolvido em Kotlin que demonstra a integração com vários serviços de backend e bibliotecas de banco de dados, cumprindo duas atividades principais. A interface do utilizador foi construída inteiramente com Jetpack Compose.

## Funcionalidades Principais

* **Autenticação de Utilizadores:** Sistema completo de login e registo utilizando o **Firebase Authentication**.
* **Navegação entre Telas:** A aplicação utiliza o **Jetpack Navigation Compose** para gerir a transição entre a tela de login e as telas das atividades.
* **Atividade 1 (Banco de Dados Local):** Uma tela dedicada que demonstra a integração com um banco de dados local.
    * Utiliza a biblioteca **Exposed** como ORM para Kotlin.
    * Cria uma tabela, insere um registo e exibe o resultado no console (Logcat).
    * Usa o **H2 Database** como um banco de dados em memória para testes rápidos.
* **Atividade 2 (Banco de Dados na Nuvem):** A tela principal após o login, que demonstra a integração com o **Cloud Firestore**.
    * Permite adicionar novos documentos (estudantes) a uma coleção.
    * Permite listar todos os documentos da coleção e exibi-los diretamente na tela.

## Tecnologias Utilizadas

* **Linguagem:** Kotlin
* **Interface Gráfica:** Jetpack Compose
* **Backend & Autenticação:** Firebase (Authentication & Cloud Firestore)
* **Banco de Dados Local:** Exposed (ORM) & H2 Database
* **Navegação:** Jetpack Navigation Compose
* **Build System:** Gradle

## Estrutura do Projeto

O projeto está organizado em três telas principais:

1.  `AuthenticationScreen`: Responsável por gerir o login e o registo de novos utilizadores.
2.  `FirestoreScreen` (Atividade 2): A tela principal que o utilizador vê após o login. Demonstra as operações de leitura e escrita no Cloud Firestore.
3.  `DatabaseScreen` (Atividade 1): Uma tela secundária, acessível a partir da `FirestoreScreen`, que executa as operações no banco de dados local.

## Como Executar o Projeto

1.  **Clonar o Repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    ```

2.  **Abrir no Android Studio:**
    * Abra o Android Studio e selecione "Open an Existing Project".
    * Navegue até à pasta do projeto clonado e abra-o.

3.  **Configurar o Firebase:**
    * Vá à [consola do Firebase](https://console.firebase.google.com/) e crie um novo projeto.
    * Adicione uma aplicação Android ao seu projeto Firebase, seguindo as instruções para registar o nome do pacote (`br.com.uri.authenticationUri`).
    * Faça o download do ficheiro `google-services.json`.
    * Copie o ficheiro `google-services.json` para o diretório `app/` do seu projeto no Android Studio.
    * Na consola do Firebase, vá a **Authentication > Sign-in method** e ative o provedor **"E-mail/senha"**.
    * Vá a **Firestore Database**, clique em "Criar banco de dados" e inicie em **modo de teste**.

4.  **Sincronizar e Executar:**
    * O Android Studio deverá pedir para sincronizar o projeto com os ficheiros Gradle. Clique em "Sync Now".
    * Execute a aplicação num emulador ou num dispositivo físico.

