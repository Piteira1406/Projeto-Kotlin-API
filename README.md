
# README para Projeto Ktor com MySQL

## Descrição do Projeto
Este projeto é uma aplicação web desenvolvida em Kotlin utilizando o framework Ktor. A aplicação oferece operações CRUD para `Users`, `Products` e `Categories`, integrando-se a uma base de dados MySQL.  

## Pré-requisitos
Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:
- [Kotlin 1.8+](https://kotlinlang.org/)
- [IntelliJ IDEA (com plugin de Kotlin)](https://www.jetbrains.com/idea/)
- [Gradle 7+](https://gradle.org/)
- [MySQL 8+](https://dev.mysql.com/downloads/mysql/)
- [Postman (opcional, para testar a API)](https://www.postman.com/)

---

## Configuração do Banco de Dados
1. **Instale e inicie o MySQL**.  
   ```bash
   sudo apt update
   sudo apt install mysql-server
   sudo systemctl start mysql
   ```

2. **Crie a base de dados e tabelas**.  
   Acesse o MySQL:  
   ```bash
   mysql -u root -p
   ```  
   Execute os seguintes comandos:  
   ```sql
   CREATE DATABASE ktor_demo;
   USE ktor_demo;

   CREATE TABLE users (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100),
     email VARCHAR(100)
   );

   CREATE TABLE products (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100),
     price DECIMAL(10, 2)
   );

   CREATE TABLE categories (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100)
   );
   ```

3. **Configure a conexão no projeto**.  
   No arquivo `src/main/kotlin/com/retech/mysql/DbConnection.kt`, ajuste as seguintes configurações:  
   ```kotlin
   val databaseUrl = "jdbc:mysql://localhost:3306/ktor_demo"
   val databaseUser = "root"
   val databasePassword = "YOUR_PASSWORD"
   ```

---

## Instalação e Execução
1. **Clone ou extraia o projeto**.  
   ```bash
   git clone <url-do-repositorio>
   cd KtorDemo
   ```

2. **Compile o projeto**.  
   ```bash
   ./gradlew build
   ```

3. **Execute a aplicação**.  
   ```bash
   ./gradlew run
   ```

4. **Acesse a API**.  
   A aplicação estará disponível em:  
   ```bash
   http://localhost:8080
   ```

---

## Testando a API
### Criar um usuário
```bash
POST http://localhost:8080/users
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@email.com"
}
```

### Listar usuários
```bash
GET http://localhost:8080/users
```

### Excluir um usuário
```bash
DELETE http://localhost:8080/users/1
```

---

## Testes Automatizados
Para rodar os testes:  
```bash
./gradlew test
```

Os relatórios de testes estarão disponíveis em `build/reports/tests/test/index.html`.

---

## Contribuição
1. Crie um Fork do projeto.  
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`).  
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova funcionalidade'`).  
4. Envie as alterações (`git push origin feature/nova-funcionalidade`).  
5. Abra um Pull Request.
