# Projeto Cuidaidoso

Este é um projeto Spring Boot com Thymeleaf para gerenciar cuidadores e clientes.

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- [Java JDK 11+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven 3.6+](https://maven.apache.org/download.cgi)
- [MySQL](https://www.mysql.com/downloads/)

## Configuração do Banco de Dados

1. Crie um banco de dados MySQL:

```sql
CREATE DATABASE cuidaidoso;
```

2. Atualize o arquivo `application.properties` com as configurações do seu banco de dados:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cuidaidoso
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

## Iniciando o Projeto

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/cuidaidoso.git
cd cuidaidoso
```

2. Abra o Springboot Dashboard no seu editor de texto
- Selecione o App **cuidaidoso** e clique em **Run**.

3. Compile o projeto usando Maven:

```bash
mvn clean install
```

4. Inicie a aplicação:

```bash
mvn spring-boot:run
```

5. Acesse a aplicação no navegador:

```
http://localhost:8080
```
