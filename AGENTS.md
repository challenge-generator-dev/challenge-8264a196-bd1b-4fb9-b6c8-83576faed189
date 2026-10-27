# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Integración de Framework en Aplicación Backend**.

| | |
|---|---|
| Tema | Sólida Experiencia en Frameworks |
| Nivel | advanced-l2 |
| Chapter | Backend |
| Especialidad | Java |
| Stack | Java / Spring Boot 3.5 |
| Patron arquitectonico | capas estándar con separación de dominio, aplicación e infraestructura (hexagonal adaptado) |
| Tiempo estimado | 20 horas |

## Receta del stack

Esqueleto obligatorio:

- `pom.xml en la raiz`
- `clase con @SpringBootApplication`
- `application.yml en src/main/resources`
- `capa de dominio con entidades y puertos`
- `capa de aplicacion con casos de uso`
- `capa de infraestructura con adaptadores y @RestController`

Trampas conocidas:

- TODA `<version>` del pom va con tres segmentos: la del parent (ej. `3.5.6`) y la de cada dependencia que la lleve (ej. Resilience4j `2.2.0`). `3.4` y `2.0` no existen como artefacto y el build muere resolviendo dependencias.
- Las dependencias que el parent POM gestiona van SIN `<version>`: `spring-boot-starter-web`, `-data-jpa`, `-validation`, `-test`, etc.
- Resilience4j publica un artefacto por linea de Spring Boot. Con Spring Boot 3 va `resilience4j-spring-boot3` con version de tres segmentos (ej. `2.2.0`, no `2.0`). `resilience4j-spring-boot2` es de Spring Boot 2 y rompe el arranque.
- Si usas anotaciones de validacion (`@NotNull`, `@Size`, `@Positive`) declara `spring-boot-starter-validation`: el starter web no las trae.
- El `spring-boot-maven-plugin` tiene que estar en `<build><plugins>` o no se empaqueta ejecutable.
- Spring Boot 3 usa `jakarta.*`, nunca `javax.*`.
- Cada archivo empieza con su `package` y con un `import` por cada clase del proyecto que viva en otro paquete. Usar `PaymentService` desde `infrastructure` sin `import com.x.application.PaymentService` no compila.

Dependencias:

- org.springframework.boot:spring-boot-starter-web 3.5.6
- org.springframework.boot:spring-boot-starter-data-jpa n/a
- org.springframework.boot:spring-boot-starter-validation n/a
- org.postgresql:postgresql 42.7.3
- ch.qos.logback:logback-classic 1.5.6
- org.springframework.boot:spring-boot-starter-actuator n/a
- org.springdoc:springdoc-openapi-starter-webmvc-ui 2.6.0
- org.springframework.boot:spring-boot-starter-test n/a
- org.mockito:mockito-core 5.12.0
- org.testcontainers:postgresql 1.20.1

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `mvn clean compile` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `mvn clean compile` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Configuración Inicial del Framework**: Configuración del framework que enruta solicitudes y controla variables de entorno.
- **Fase 2 — Implementación de Registros de Trazos**: Implementación de logs que rastrean la ejecución de la aplicación.
- **Fase 3 — Conexión con Base de Datos utilizando ORM**: Conexión de la aplicación con una base de datos utilizando un ORM.
- **Fase 4 — Optimización y Refactorización**: Código optimizado y refactorizado que mejora el rendimiento y la mantenibilidad de la aplicación.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Superficie de practica (NO completes)

Estos archivos SON el ejercicio de la persona. No los implementes; deja stubs. No toques la logica que el reto pide completar.

- [ ] `src/test/java/com/fintech/backend/application/service/TransactionServiceTest.java` — El topic pide TDD/pruebas: este archivo es el ejercicio.
- [ ] `src/test/java/com/fintech/backend/infrastructure/controller/TransactionControllerTest.java` — El topic pide TDD/pruebas: este archivo es el ejercicio.

## Lo que falta y tenes que completar

### 1. Referencias colgando (55)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/fintech/backend/BackendApplication.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/application/service/TransactionService.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/config/DatabaseConfig.java` — `com.zaxxer.hikari`
      El import com.zaxxer.hikari.HikariConfig pertenece a com.zaxxer.hikari, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/config/DatabaseConfig.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/config/LoggingConfig.java` — `org.slf4j`
      El import org.slf4j.LoggerFactory pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/GlobalExceptionHandler.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/infrastructure/config/OpenApiConfig.java` — `io.swagger.v3`
      El import io.swagger.v3.oas.models.OpenAPI pertenece a io.swagger.v3, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/fintech/backend/infrastructure/controller/TransactionControllerTest.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/backend/BackendApplication.java` — `ApplicationProperties.getEnvironment`
      Se invoca `getEnvironment` sobre `ApplicationProperties`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/BackendApplication.java` — `ApplicationProperties.getVersion`
      Se invoca `getVersion` sobre `ApplicationProperties`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/BackendApplication.java` — `ApplicationProperties.getDatasource`
      Se invoca `getDatasource` sobre `ApplicationProperties`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.timestamp`
      Se invoca `timestamp` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java` — `Transaction.reference`
      Se invoca `reference` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/application/service/TransactionService.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/application/service/TransactionService.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `TransactionJpaRepository.save`
      Se invoca `save` sobre `TransactionJpaRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `TransactionJpaRepository.findById`
      Se invoca `findById` sobre `TransactionJpaRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `TransactionEntity.map`
      Se invoca `map` sobre `TransactionEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.timestamp`
      Se invoca `timestamp` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java` — `Transaction.reference`
      Se invoca `reference` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.timestamp`
      Se invoca `timestamp` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `Transaction.reference`
      Se invoca `reference` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `UpdateTransactionRequest.accountId`
      Se invoca `accountId` sobre `UpdateTransactionRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `UpdateTransactionRequest.type`
      Se invoca `type` sobre `UpdateTransactionRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `UpdateTransactionRequest.amount`
      Se invoca `amount` sobre `UpdateTransactionRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `UpdateTransactionRequest.reference`
      Se invoca `reference` sobre `UpdateTransactionRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `TransactionService.getAllTransactions`
      Se invoca `getAllTransactions` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `TransactionService.getTransactionsByAccountId`
      Se invoca `getTransactionsByAccountId` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `TransactionService.getTransactionsByAccountIdAndType`
      Se invoca `getTransactionsByAccountIdAndType` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `TransactionService.getTransactionsByAccountIdAndStatus`
      Se invoca `getTransactionsByAccountIdAndStatus` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `UpdateTransactionRequest.status`
      Se invoca `status` sobre `UpdateTransactionRequest`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `TransactionService.updateTransaction`
      Se invoca `updateTransaction` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java` — `TransactionService.deleteTransaction`
      Se invoca `deleteTransaction` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/backend/application/service/TransactionServiceTest.java` — `Transaction.isPresent`
      Se invoca `isPresent` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/backend/application/service/TransactionServiceTest.java` — `TransactionService.updateTransactionStatus`
      Se invoca `updateTransactionStatus` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/backend/application/service/TransactionServiceTest.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/backend/infrastructure/controller/TransactionControllerTest.java` — `TransactionService.updateTransactionStatus`
      Se invoca `updateTransactionStatus` sobre `TransactionService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/backend/infrastructure/controller/TransactionControllerTest.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (16)

- `pom.xml`
- `src/main/java/com/fintech/backend/BackendApplication.java`
- `src/main/resources/application.yml`
- `src/main/java/com/fintech/backend/domain/model/Transaction.java`
- `src/main/java/com/fintech/backend/domain/port/TransactionRepositoryPort.java`
- `src/main/java/com/fintech/backend/infrastructure/repository/TransactionJpaRepository.java`
- `src/main/java/com/fintech/backend/infrastructure/repository/TransactionEntity.java`
- `src/main/java/com/fintech/backend/application/service/TransactionService.java`
- `src/main/java/com/fintech/backend/infrastructure/adapter/TransactionJpaAdapter.java`
- `src/main/java/com/fintech/backend/infrastructure/config/DatabaseConfig.java`
- `src/main/java/com/fintech/backend/infrastructure/controller/TransactionController.java`
- `src/main/java/com/fintech/backend/infrastructure/config/LoggingConfig.java`
- `src/main/java/com/fintech/backend/infrastructure/adapter/GlobalExceptionHandler.java`
- `src/main/java/com/fintech/backend/infrastructure/config/OpenApiConfig.java`
- `src/test/java/com/fintech/backend/application/service/TransactionServiceTest.java`
- `src/test/java/com/fintech/backend/infrastructure/controller/TransactionControllerTest.java`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/fintech/backend`
- `src/main/java/com/fintech/backend/domain`
- `src/main/java/com/fintech/backend/domain/model`
- `src/main/java/com/fintech/backend/domain/port`
- `src/main/java/com/fintech/backend/application`
- `src/main/java/com/fintech/backend/application/service`
- `src/main/java/com/fintech/backend/infrastructure`
- `src/main/java/com/fintech/backend/infrastructure/adapter`
- `src/main/java/com/fintech/backend/infrastructure/config`
- `src/main/java/com/fintech/backend/infrastructure/controller`
- `src/main/java/com/fintech/backend/infrastructure/repository`
- `src/main/resources`
- `src/test/java/com/fintech/backend`

## Verificacion

```bash
mvn clean compile
```

El comando tiene que pasar SIN implementar los archivos de la superficie de practica: solo andamiaje.

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **capas estándar con separación de dominio, aplicación e infraestructura (hexagonal adaptado)**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Perfil: Chapter Backend, Especialidad Desarrollador, Tecnología Java, Advanced
- Brecha que el reto ataca: Experiencia comprobable en al menos un marco de trabajo (Framework) en su lenguaje de programación principal (por ejemplo, Spring Framework en Java o ASP .net core en C#) y puede realizar el enrutamiento de solicitudes, controlar las variables de entorno, generar registros de rastros (Logs) y conectarse con bases de datos utilizando un Mapeador Relacional de Objetos (ORM).
- Mision: El candidato es un desarrollador Backend Advanced con experiencia en estructuras de datos y patrones GRASP. Está listo para profundizar en frameworks como Spring Boot.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
