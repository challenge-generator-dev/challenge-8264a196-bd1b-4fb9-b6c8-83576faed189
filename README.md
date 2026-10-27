# Integración de Framework en Aplicación Backend

El equipo de desarrollo de una empresa de fintech necesita integrar un marco de trabajo en una aplicación backend existente. La aplicación debe enrutar solicitudes, controlar variables de entorno, generar logs y conectarse a una base de datos utilizando un ORM. Los actores involucrados son el Desarrollador Backend, el Sistema de Monitoreo y la Base de Datos. La aplicación debe manejar un volumen pico de 10,000 transacciones por hora con una disponibilidad del 99.9%.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | Sólida Experiencia en Frameworks |
| **Nivel** | advanced-l2 |
| **Tipo** | practical |
| **Tiempo estimado** | 20 horas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Configuración Inicial del Framework

**Objetivo:** Configurar el framework para enrutar solicitudes y controlar variables de entorno.

**Tiempo estimado:** 5 horas

**Instrucciones:**

- Identificar las rutas principales de la aplicación.
- Configurar el framework para manejar las solicitudes entrantes.
- Implementar el control de variables de entorno para la aplicación.

**Entregable:** Configuración del framework que enruta solicitudes y controla variables de entorno.

<details>
<summary>Pistas de conocimiento</summary>

- Importancia de las rutas en una aplicación backend.
- Uso de variables de entorno para configurar la aplicación.

</details>

### Fase 2: Implementación de Registros de Trazos

**Objetivo:** Implementar la generación de logs para rastrear la ejecución de la aplicación.

**Tiempo estimado:** 5 horas

**Instrucciones:**

- Identificar los puntos clave de la aplicación donde se deben generar logs.
- Implementar la generación de logs utilizando el framework.
- Asegurar que los logs contengan información relevante para el diagnóstico de problemas.

**Entregable:** Implementación de logs que rastrean la ejecución de la aplicación.

<details>
<summary>Pistas de conocimiento</summary>

- Importancia de los logs para el diagnóstico y monitoreo de aplicaciones.
- Nivel de detalle necesario en los logs para facilitar la resolución de problemas.

</details>

### Fase 3: Conexión con Base de Datos utilizando ORM

**Objetivo:** Conectar la aplicación con una base de datos utilizando un ORM.

**Tiempo estimado:** 5 horas

**Instrucciones:**

- Configurar la conexión con la base de datos.
- Implementar el mapeo de entidades utilizando el ORM.
- Realizar operaciones CRUD en la base de datos.

**Entregable:** Conexión de la aplicación con una base de datos utilizando un ORM.

<details>
<summary>Pistas de conocimiento</summary>

- Ventajas de utilizar un ORM para interactuar con la base de datos.
- Consideraciones para el mapeo de entidades y la realización de operaciones CRUD.

</details>

### Fase 4: Optimización y Refactorización

**Objetivo:** Optimizar y refactorizar el código para mejorar el rendimiento y la mantenibilidad.

**Tiempo estimado:** 5 horas

**Instrucciones:**

- Identificar áreas de la aplicación que pueden ser optimizadas.
- Refactorizar el código para mejorar el rendimiento y la mantenibilidad.
- Realizar pruebas para asegurar que las optimizaciones no introducen errores.

**Entregable:** Código optimizado y refactorizado que mejora el rendimiento y la mantenibilidad de la aplicación.

<details>
<summary>Pistas de conocimiento</summary>

- Técnicas de optimización comunes en aplicaciones backend.
- Principios de refactorización para mejorar la calidad del código.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es un marco de trabajo y por qué es importante en el desarrollo de aplicaciones backend?
- **paraQueSirve**: ¿Para qué sirve la generación de logs en una aplicación backend?
- **comoSeUsa**: ¿Cómo se utiliza un ORM para interactuar con una base de datos en una aplicación backend?
- **erroresComunes**: ¿Cuáles son los errores comunes al configurar un framework y cómo se pueden evitar?
- **queDecisionesImplica**: ¿Qué decisiones implica la optimización y refactorización del código en una aplicación backend?

## Criterios de Evaluacion

- Configuración del framework que enruta solicitudes y controla variables de entorno.
- Implementación de logs que rastrean la ejecución de la aplicación.
- Conexión de la aplicación con una base de datos utilizando un ORM.
- Código optimizado y refactorizado que mejora el rendimiento y la mantenibilidad de la aplicación.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
mvn clean compile
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
