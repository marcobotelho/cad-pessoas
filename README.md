# Desafio Sinerji: Cadastro de pessoas (Marco Botelho Abreu)

## Uso de frameworks ou bibliotecas

- Aplicação desenvolvida na IDE Eclipse
- Framework utilizado foi Java Server Faces (JSF) com Primefaces
- Gerenciador da biblioteca utilizada foi Maven
- Servidor de aplicação web utilizado Apache Tomcat v9.0
- Banco de dados utilizados foi o Postgres para aplicação e H2 Database para teste
- Criado arquivo docker-compose.yml para iniciar o banco Postgres que será utilizada na aplicação
- Persistencia com o banco de dados foi utilizado Hibernate Jpa e Hibernate Validator
- Foi utilizado web service externo ViaCep para preenchimento automatico dos campos de endereço de acordo com o CEP
- Foi utilizada uma dependencia para tratamento do Json retornado pelo web service ViaCep
- Para implementação dos testes foram utilizados as bibliotecas JUnit e Mockito

## Instruções sobre como compilar e executar o projeto

- Iniciar banco de dados Postgres executando no terminal na raiz do projeto o comando “docker-compose up”
- Executar comando no terminal na raiz do projeto "mvn package" para geração do arquivo "cad-pessoas.war"
- Fazer o deploy do arquivo "cad-pessoas.war" no servidor Tomcat v9.0 e iniciar servidor
- Acessar url "[servidor]/cad-pessoas" se for local "http://localhost:8080/cad-pessoas" 

## Instruções sobre como executar os testes da solução

- Executar no terminal na raiz do projeto o comando "mvn test"
