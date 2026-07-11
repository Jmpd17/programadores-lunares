```markdown
# Decisiones del Proyecto

## Simulador de Misión Lunar Artemis II

**Proyecto:** Simulador de Misión Lunar Artemis II  
**Repositorio:** Programadores-Lunares

---

# Historial de Decisiones

## DT-001 - Lenguaje de Programación

Se seleccionó **Java 17** como lenguaje principal para el desarrollo del simulador debido a su compatibilidad con la biblioteca Orekit y su enfoque orientado a objetos.

---

## DT-002 - Biblioteca de Simulación

Se decidió utilizar **Orekit** para realizar los cálculos de mecánica orbital y propagación de órbitas, ya que ofrece herramientas especializadas para aplicaciones aeroespaciales.

---

## DT-003 - Simulación Inicial

Como primera etapa se implementó una simulación de una órbita terrestre baja (LEO) para validar el funcionamiento del sistema antes de avanzar hacia simulaciones más complejas.

---

## DT-004 - Arquitectura Modular

Se adoptó una arquitectura modular para separar las responsabilidades del sistema en diferentes componentes.

La implementación actual se divide en:

- **Main.java:** Punto de entrada del sistema.
- **OrekitConfig.java:** Configuración e inicialización de Orekit.
- **LEOSimulation.java:** Ejecución de la simulación orbital.

Esta separación facilita el mantenimiento y la incorporación de nuevas funcionalidades.

---

## DT-005 - Modelo de Arquitectura

Se adoptó el **Modelo de Arquitectura 4+1** para documentar la estructura del sistema desde diferentes perspectivas, permitiendo una mejor comprensión de su organización, funcionamiento y futuras ampliaciones.

---

## DT-006 - Gestión del Proyecto

Para el desarrollo y seguimiento del proyecto se utilizan las siguientes herramientas:

- **Git:** Control de versiones local.
- **GitHub:** Repositorio remoto para almacenar y compartir el código.
- **Maven:** Gestión de dependencias y construcción del proyecto.
- **Trello:** Planificación y seguimiento de las actividades del equipo.

---

# Uso de Inteligencia Artificial

La Inteligencia Artificial fue utilizada como herramienta de apoyo durante el desarrollo del proyecto, principalmente para facilitar tareas de investigación, organización y documentación.

## Actividades Asistidas

- Investigación sobre la biblioteca Orekit y conceptos de dinámica orbital.
- Organización de la documentación técnica del proyecto.
- Apoyo en la elaboración del Documento de Especificación de Requisitos (SRS).
- Apoyo en la elaboración del Documento de Arquitectura basado en el Modelo 4+1.
- Revisión de la estructura y organización de los documentos técnicos.
- Generación de ejemplos para facilitar la comprensión de conceptos relacionados con simulación orbital y arquitectura de software.

## Refinamientos Realizados

Todo el contenido generado con asistencia de Inteligencia Artificial fue revisado y adaptado por los integrantes del equipo antes de incorporarse al proyecto.

Entre los principales refinamientos realizados se encuentran:

- Corrección de la redacción técnica.
- Ajuste de conceptos relacionados con arquitectura de software.
- Reorganización de la estructura de la documentación.
- Adaptación del contenido a los lineamientos establecidos por la asignatura.
- Validación manual de la información técnica relacionada con Orekit y la simulación orbital.

## Consideraciones

La Inteligencia Artificial no fue utilizada para reemplazar el análisis, el diseño o las decisiones del equipo. Su uso estuvo orientado exclusivamente como herramienta de apoyo para investigación, organización y documentación. Todas las decisiones finales fueron tomadas por los integrantes del proyecto.

---

# Registro de Actualizaciones

## Entregable 0

- Creación del repositorio en GitHub.
- Diseño del distintivo del equipo.
- Configuración inicial de Orekit.
- Desarrollo de la primera simulación orbital LEO.
- Elaboración del registro inicial de riesgos.

---

## Entregable 1

- Elaboración del Documento de Especificación de Requisitos de Software (SRS).
- Definición de requisitos funcionales y no funcionales.
- Organización de la documentación técnica del proyecto.
- Actualización del archivo `decisiones.md`.

---

## Entregable 2

- Elaboración del Documento de Arquitectura de Software utilizando el Modelo 4+1.
- Documentación de la arquitectura del módulo de simulación orbital.
- Definición de la arquitectura modular del sistema.
- Actualización del registro de decisiones técnicas.
- Actualización de la documentación relacionada con el uso de Inteligencia Artificial.

---

# Observaciones

Este documento será actualizado de manera continua durante el desarrollo del proyecto. Cada decisión técnica, arquitectónica u organizacional será registrada con el objetivo de mantener un historial claro de la evolución del sistema y facilitar el seguimiento de las decisiones tomadas por el equipo.
```
