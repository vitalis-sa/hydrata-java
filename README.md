# 🌿 HyDrata — API Core (Java Advanced)

> Plataforma de Monitoramento Hídrico e Otimização de Irrigação  
> **Global Solution 2026/1 · FIAP · 2º Ano ADS**

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelagem de Dados](#modelagem-de-dados)
- [Lógica de Decisão — Rule Engine](#lógica-de-decisão--rule-engine)
- [Endpoints da API](#endpoints-da-api)
- [Integrações Externas](#integrações-externas)
- [Como Executar](#como-executar)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Exemplos de Requisição](#exemplos-de-requisição)
- [Links Importantes](#links-importantes)
- [Equipe](#equipe)

---

## Sobre o Projeto

O **HyDrata** é uma plataforma que ajuda o pequeno e médio produtor rural a tomar decisões de irrigação com base em dados reais — cruzando leituras de sensores ESP32 instalados no campo com previsões meteorológicas (Open-Meteo), nível de rios (ANA) e focos de queimada (INPE).

Esta API é o **coração do sistema**: responsável por receber telemetria via MQTT, consultar fontes externas, executar o motor de regras e gerar alertas com a decisão final:

```
IRRIGAR HOJE? ✅ SIM  ou  ❌ NÃO
```

A API também expõe o endpoint de dashboard (`/api/dashboard/{propriedadeId}`) como BFF para o app mobile.

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework base |
| Spring Data JPA | — | Persistência |
| Spring HATEOAS | — | Links hipermídia nos responses |
| Spring Validation | — | Validação de DTOs |
| Spring Integration MQTT | — | Recepção de telemetria IoT |
| Oracle JDBC (ojdbc11) | — | Driver do banco Oracle |
| SpringDoc OpenAPI | 2.8.9 | Documentação Swagger |
| MapStruct | 1.6.3 | Mapeamento Entity ↔ DTO |
| Lombok | — | Redução de boilerplate |
| Spring DevTools | — | Reload automático em dev |
| Eclipse Paho MQTT | 1.2.5 | Cliente MQTT |

---

## Arquitetura

```
ESP32 (campo)
    │ MQTT (hydrata/leitura/{idSensor})
    ▼
API Java — Spring Boot (porta 8080)
    │
    ├── client/          ← Integrações externas (ANA, INPE, Open-Meteo)
    ├── controller/      ← Endpoints REST
    ├── service/         ← Regras de negócio / rule engine
    ├── repository/      ← Spring Data JPA
    ├── entity/          ← Mapeamento Oracle
    ├── dto/             ← Request / Response
    ├── mapper/          ← MapStruct
    └── enums/
    │
    ▼
Oracle Database
    │
    ▼
App Mobile (React Native) ← consome esta API
```

### Fluxo do Motor de Regras

```
1. ESP32 publica leitura MQTT
       ↓
2. API recebe e persiste em LEITURA_CLIMA / LEITURA_LUZ
       ↓
3. @Scheduled a cada hora executa o rule engine
       ↓
4. Cruza: umidade_solo + previsão_chuva (Open-Meteo) + nível_rio (ANA mock) + focos (INPE mock)
       ↓
5. Gera registro em ALERTA com tipo, nível de risco e recomendação
       ↓
6. App mobile consome GET /api/dashboard/{id} → exibe decisão ao produtor
```

---

## Estrutura do Projeto

```
src/main/java/fiap/hydrata/
│
├── HydrataApplication.java
│
├── client/
│   ├── AnaHidroClient.java          # Mock ANA HidroWeb
│   ├── InpeBdqueimadasClient.java   # Mock INPE BDQueimadas
│   ├── OpenMeteoClient.java         # Integração real Open-Meteo
│   ├── DadosClimaResponse.java
│   └── NivelRioResponse.java
│
├── config/
│   └── CorsConfig.java              # CORS liberado para mobile/frontend
│
├── controller/
│   ├── AlertasController.java
│   ├── DadosExternosController.java
│   ├── DashboardController.java     # BFF para app mobile
│   ├── DispositivosIotController.java
│   ├── FontesExternasController.java
│   ├── LeiturasController.java
│   └── PropriedadesController.java
│
├── dto/
│   ├── request/    # AlertaRequest, DadoExternoRequest, DispositivoIotRequest...
│   └── response/   # AlertaResponse, DashboardResponse, LeituraClimaResponse...
│
├── entity/
│   ├── Alerta.java
│   ├── Cooperativa.java
│   ├── Coordenadas.java             # @Embeddable em Propriedade
│   ├── DadoExterno.java
│   ├── DispositivoIot.java
│   ├── FonteExterna.java            # Herança SINGLE_TABLE
│   ├── FonteExternaApi.java         # discriminator = "API_CLIMA"
│   ├── FonteExternaIot.java         # discriminator = "IOT"
│   ├── FonteExternaSatelital.java   # discriminator = "SATELITE"
│   ├── LeituraClima.java
│   ├── LeituraLuz.java
│   ├── Plano.java
│   ├── Produtor.java
│   ├── ProdutorCooperativa.java     # Chave composta N:N
│   ├── ProdutorCooperativaId.java   # @Embeddable chave composta
│   └── Propriedade.java
│
└── enums/
    ├── NivelRisco.java    # BAIXO, MEDIO, ALTO, CRITICO
    ├── StatusAlerta.java  # ATIVO, RESOLVIDO, CANCELADO
    └── StatusGeral.java   # ATIVO, INATIVO
```

---

## Modelagem de Dados

### Entidades e Relacionamentos

```
PRODUTOR ──1:N──► PROPRIEDADE ──1:1──► DISPOSITIVO_IOT ──1:N──► LEITURA_CLIMA
    │                   │                                   └──1:N──► LEITURA_LUZ
    └──N:N──► COOPERATIVA           │
    (via PRODUTOR_COOPERATIVA)      └──1:N──► ALERTA
                                         ▲
PLANO ──1:N──► PROPRIEDADE        DADO_EXTERNO
                              ◄──────────┘
FONTE_EXTERNA ──1:N──► DADO_EXTERNO
(API_CLIMA | IOT | SATELITE)
```

### Padrões de Modelagem Implementados

| Padrão JPA | Onde é Usado |
|---|---|
| `@Embedded` | `Coordenadas` dentro de `Propriedade` (latitude, longitude, cidade, estado) |
| `@EmbeddedId` | `ProdutorCooperativaId` — chave composta do N:N |
| `@Inheritance(SINGLE_TABLE)` | `FonteExterna` com subclasses `FonteExternaApi`, `FonteExternaIot`, `FonteExternaSatelital` |
| `@OneToOne` | `DispositivoIot` ↔ `Propriedade` (relação 1:1 com `unique = true`) |

### Enums de Domínio

```java
// Tipos de alerta permitidos
IRRIGAR | SECA | ENCHENTE | QUEIMADA | CRITICO | NORMALIZADO

// Níveis de risco
BAIXO | MEDIO | ALTO | CRITICO

// Status do alerta
ATIVO | RESOLVIDO | CANCELADO
```

---

## Lógica de Decisão — Rule Engine

O motor de regras é executado de forma agendada (`@Scheduled`) e cruza três fontes de dados:

```
┌────────────────┬────────────────────────────────────────────┬──────────────────────┐
│ Tipo de Alerta │ Condição                                   │ Nível de Risco       │
├────────────────┼────────────────────────────────────────────┼──────────────────────┤
│ IRRIGAR        │ umidade < 40% E sem chuva prevista         │ MÉDIO                │
│                │ E luminosidade ≤ 75% E rio NORMAL          │                      │
├────────────────┼────────────────────────────────────────────┼──────────────────────┤
│ NORMALIZADO    │ umidade > 70% OU chuva prevista            │ BAIXO                │
├────────────────┼────────────────────────────────────────────┼──────────────────────┤
│ ENCHENTE       │ nível_rio > média_histórica × 1.5          │ ALTO                 │
├────────────────┼────────────────────────────────────────────┼──────────────────────┤
│ QUEIMADA       │ foco < 30km E umidade < 30%               │ CRÍTICO              │
├────────────────┼────────────────────────────────────────────┼──────────────────────┤
│ SECA           │ solo crítico por mais de 3 dias            │ ALTO                 │
├────────────────┼────────────────────────────────────────────┼──────────────────────┤
│ CRITICO        │ Combinação de dois ou mais riscos          │ CRÍTICO              │
└────────────────┴────────────────────────────────────────────┴──────────────────────┘
```

> **Nota:** O rule engine **não processa a cada mensagem MQTT** — utiliza `@Scheduled` (ex: a cada hora) para evitar flood no banco de dados. Leituras brutas são persistidas em `LEITURA_CLIMA` / `LEITURA_LUZ`; apenas o resultado final vai para `ALERTA`.

---

## Endpoints da API

### 📊 Dashboard (BFF para o App Mobile)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/dashboard/{propriedadeId}` | Dados agregados: decisão de irrigação, umidade, clima, focos, nível do rio |

**Response 200:**
```json
{
  "ultimaDecisao": {
    "tipo": "IRRIGAR",
    "nivelRisco": "MEDIO",
    "mensagem": "Solo seco sem previsão de chuva.",
    "recomendacao": "Irrigue hoje antes das 10h.",
    "status": "ATIVO"
  },
  "umidadeAr": 32.5,
  "temperaturaMax": 29.0,
  "previsaoChuva": false,
  "nivelRioStatus": "NORMAL",
  "nomeRio": "Rio Monitorado (ANA)",
  "focosQueimada": 0,
  "ultimaLeitura": "2026-06-07T08:00:00"
}
```

---

### 🔔 Alertas

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/alertas` | Listar todos os alertas |
| `GET` | `/api/alertas/{id}` | Buscar por ID (com HATEOAS) |
| `GET` | `/api/alertas/propriedade/{propriedadeId}?tipo=IRRIGAR` | Alertas por propriedade, com filtro opcional por tipo |
| `POST` | `/api/alertas` | Criar alerta manualmente |
| `PUT` | `/api/alertas/{id}` | Atualizar alerta (ex: marcar como RESOLVIDO) |
| `DELETE` | `/api/alertas/{id}` | Remover alerta |

---

### 🏡 Propriedades

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/propriedades` | Listar todas |
| `GET` | `/api/propriedades/{id}` | Buscar por ID (com HATEOAS) |
| `POST` | `/api/propriedades` | Cadastrar propriedade |
| `PUT` | `/api/propriedades/{id}` | Atualizar |
| `DELETE` | `/api/propriedades/{id}` | Remover |

---

### 📡 Dispositivos IoT

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/dispositivos-iot` | Listar todos |
| `GET` | `/api/dispositivos-iot/{id}` | Buscar por ID (com HATEOAS) |
| `POST` | `/api/dispositivos-iot` | Cadastrar ESP32 |
| `PUT` | `/api/dispositivos-iot/{id}` | Atualizar |
| `DELETE` | `/api/dispositivos-iot/{id}` | Remover |

---

### 📈 Leituras (Histórico de Telemetria)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/leituras/historico/{propriedadeId}?dias=7` | Histórico de leituras de clima do sensor (padrão: 7 dias) |

---

### 🌐 Dados Externos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/dados-externos` | Listar |
| `GET` | `/api/dados-externos/{id}` | Buscar por ID (com HATEOAS) |
| `POST` | `/api/dados-externos` | Registrar dado externo |
| `PUT` | `/api/dados-externos/{id}` | Atualizar |
| `DELETE` | `/api/dados-externos/{id}` | Remover |

---

### 🔗 Fontes Externas

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/fontes-externas` | Listar |
| `GET` | `/api/fontes-externas/{id}` | Buscar por ID (com HATEOAS) |
| `POST` | `/api/fontes-externas` | Cadastrar fonte |
| `PUT` | `/api/fontes-externas/{id}` | Atualizar |
| `DELETE` | `/api/fontes-externas/{id}` | Remover |

---

### Formato Padrão de Erro

```json
{
  "status": 404,
  "erro": "Não encontrado",
  "mensagem": "Propriedade não existe",
  "timestamp": "2026-06-07T10:30:00"
}
```

---

## Integrações Externas

### Open-Meteo (real — sem autenticação)

```
GET https://api.open-meteo.com/v1/forecast
    ?latitude=-23.5&longitude=-46.6
    &current=temperature_2m,precipitation
```

Consumido via `OpenMeteoClient` com `RestClient`. Se `precipitacao > 0` → **NÃO IRRIGAR**.

### ANA HidroWebService (mock)

`AnaHidroClient` retorna dados simulados de nível do rio. Em produção, consumiria a API da ANA com token via e-mail.

### INPE BDQueimadas (mock)

`InpeBdqueimadasClient` retorna quantidade aleatória de focos. Em produção, consultaria o portal oficial do INPE com coordenadas da propriedade.

### MQTT — ESP32 no Campo

| Tópico | Payload |
|---|---|
| `hydrata/leitura/{idSensor}` | `{ "umidade": 32.5, "nivel": 120.0, "temp": 28.3 }` |
| `hydrata/status/{idSensor}` | `{ "status": "ATIVO", "wifi": "CONECTADO" }` |
| `hydrata/alerta/{idSensor}` | `{ "em_alerta": true, "tipo": "SOLO_SECO" }` |

---

## Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.9+
- Oracle Database 19c+ acessível
- (Opcional) Broker MQTT — ex: Eclipse Mosquitto

### 1. Clonar o repositório

```bash
git clone https://github.com/<org>/hydrata-java.git
cd hydrata-java
```

### 2. Configurar variáveis de ambiente

Crie um arquivo `src/main/resources/application.properties` com base no template abaixo (ou defina como variáveis de ambiente no container):

```properties
# Oracle
spring.datasource.url=jdbc:oracle:thin:@<host>:1521/<service>
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect

# MQTT
mqtt.broker.url=tcp://localhost:1883
mqtt.client.id=hydrata-api

# Open-Meteo (sem autenticação)
openmeteo.base-url=https://api.open-meteo.com/v1

# Servidor
server.port=8080
```

### 3. Build e execução

```bash
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

### 4. Acessar a documentação Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## Variáveis de Ambiente

| Variável | Descrição | Exemplo |
|---|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL do Oracle | `jdbc:oracle:thin:@db:1521/HYDRATA` |
| `SPRING_DATASOURCE_USERNAME` | Usuário Oracle | `hydrata_user` |
| `SPRING_DATASOURCE_PASSWORD` | Senha Oracle | `senha_secreta` |
| `MQTT_BROKER_URL` | URL do broker MQTT | `tcp://broker:1883` |
| `OPENMETEO_BASE_URL` | Base URL Open-Meteo | `https://api.open-meteo.com/v1` |
| `SERVER_PORT` | Porta da API | `8080` |

---

## Exemplos de Requisição

### Cadastrar Propriedade

```bash
curl -X POST http://localhost:8080/api/propriedades \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Fazenda São João",
    "areaHectares": 120.5,
    "produtorId": 1,
    "planoId": 2,
    "latitude": -22.9,
    "longitude": -47.1,
    "cidade": "Campinas",
    "estado": "SP"
  }'
```

### Consultar Dashboard (decisão de irrigação)

```bash
curl http://localhost:8080/api/dashboard/1
```

### Listar Alertas Ativos de uma Propriedade

```bash
curl "http://localhost:8080/api/alertas/propriedade/1?tipo=IRRIGAR"
```

### Marcar Alerta como Resolvido

```bash
curl -X PUT http://localhost:8080/api/alertas/5 \
  -H "Content-Type: application/json" \
  -d '{ "status": "RESOLVIDO" }'
```

### Histórico de Leituras (últimos 7 dias)

```bash
curl "http://localhost:8080/api/leituras/historico/1?dias=7"
```

---

## Links Importantes

| Recurso | URL |
|---|---|
| Swagger UI (local) | http://localhost:8080/swagger-ui.html |
| Swagger UI (Azure) | `[adicionar após deploy]` |
| Open-Meteo Docs | https://open-meteo.com/en/docs |
| ANA HidroWeb | https://www.snirh.gov.br/hidroweb |
| INPE BDQueimadas | https://queimadas.dgi.inpe.br/queimadas/bdqueimadas |
| Vídeo demonstração | [link](https://youtu.be/bpoYou45T6s) |
| Vídeo pitch | `[adicionar link]` |

---

## Equipe

| Nome | RM |
|---|---|
| Ana Flávia Camelo | RM561489 |
| Gustavo Kenji Terada | RM562745 |
| João Guilherme Carvalho Novaes | RM566234 |
| Pedro Chasci Puga | RM565154 |
| Lucas Figueiredo Vieira | RM561342 |

**Professor:** Marcel Stefan Wagner  
**Matéria/Disciplina:** Java Advanced

---

> **HyDrata** · Global Solution 2026/1 · FIAP · Análise e Desenvolvimento de Sistemas
