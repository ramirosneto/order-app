# OrderApp

OrderApp é um aplicativo de gerenciamento de pedidos desenvolvido em Kotlin usando Jetpack Compose para a interface do usuário e Koin para injeção de dependência. Este projeto demonstra a arquitetura MVVM (Model-View-ViewModel) e o uso de coroutines e Flow para operações assíncronas.

## Funcionalidades

- Listagem de pedidos
- Adição de novos pedidos
- Exclusão de pedidos
- Tratamento de erros e exibição de mensagens de erro

## Estrutura do Projeto

- `MainActivity`: A atividade principal que configura a tela inicial.
- `MainViewModel`: O ViewModel que gerencia a lógica de negócios e a interação com o repositório.
- `OrderRepository`: O repositório que fornece métodos para acessar o banco de dados e realizar operações de CRUD.
- `OrderDatabase`: A classe de banco de dados que usa Room para persistência.
- `OrderDao`: A interface DAO que define métodos para acessar o banco de dados.
- `OrderMapper`: A classe que mapeia entidades de banco de dados para modelos de domínio e vice-versa.

## Dependências

- Jetpack Compose
- Koin para injeção de dependência
- Room para persistência
- Kotlin Coroutines e Flow para operações assíncronas

## Como Executar

1. Clone o repositório:
    ```sh
    git clone https://github.com/ramirosneto/orderapp.git
    ```
2. Abra o projeto no Android Studio.
3. Compile e execute o aplicativo em um emulador ou dispositivo físico.

## Testes

### Testes Unitários

Os testes unitários são escritos usando JUnit e Mockk. Eles cobrem a lógica do ViewModel e do Repositório.

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.