# Arquitectura del Sistema

## Simulador de Misión Lunar Artemis II

---

# 1. Introducción

El Simulador de Misión Lunar Artemis II es una aplicación desarrollada en Java que utiliza la biblioteca Orekit para realizar simulaciones orbitales y validar conceptos relacionados con la dinámica de vuelo espacial.

La arquitectura del sistema ha sido diseñada siguiendo un enfoque modular, permitiendo separar claramente la configuración del entorno, la ejecución de simulaciones y la generación de resultados. Esta estructura facilita el mantenimiento, la escalabilidad y la incorporación de nuevas funcionalidades en futuras fases del proyecto.

---

# 2. Estructura del Proyecto

PROGRAMADORES-LUNARES/
│
├── .gradle/
│   ├── 8.9/
│   ├── 8.14/
│   ├── 9.2.0/
│   ├── buildOutputCleanup/
│   ├── nb-cache/
│   └── vcs-1/
│
├── .idea/
│   ├── compiler.xml
│   ├── jarRepositories.xml
│   └── misc.xml
│
├── .vscode/
│   └── settings.json
│
├── src/
│   │
│   ├── Docs/
│   │
│   ├── LEOSimulation.java
│   ├── Main.java
│   └── OrekitConfig.java
│
├── target/
│   └── classes/
│       └── com/
│           └── nasa/
│               └── simulador/
│
├── .gitignore
├── build.gradle
├── Git.html
├── pom.xml
├── README.md
└── RaizappJava.iml

La estructura del proyecto está organizada en módulos especializados, donde cada componente cumple una responsabilidad específica dentro del sistema.

---

# 3. Descripción de Componentes

## Main.java

### Responsabilidades

* Actuar como punto de entrada de la aplicación.
* Inicializar el sistema.
* Coordinar el flujo general de ejecución.
* Invocar la configuración de Orekit.
* Ejecutar la simulación orbital.

Main.java funciona como el controlador principal encargado de coordinar la interacción entre los distintos módulos.

---

## OrekitConfig.java

### Responsabilidades

* Configurar la biblioteca Orekit.
* Cargar los archivos contenidos en la carpeta `orekit-data`.
* Inicializar los recursos necesarios para la simulación.
* Verificar la disponibilidad de los datos requeridos.

Debido a que Orekit depende de información astronómica y orbital externa, este módulo garantiza que el entorno esté correctamente preparado antes de iniciar cualquier simulación.

---

## LEOSimulation.java

### Responsabilidades

* Definir la órbita inicial.
* Ejecutar la propagación orbital.
* Calcular posiciones espaciales.
* Obtener resultados de trayectoria.
* Generar información para análisis posterior.

Este módulo representa el núcleo funcional del simulador y concentra la lógica principal de dinámica orbital.

---

## RiskReport.java

### Responsabilidades

* Registrar riesgos técnicos identificados durante el desarrollo.
* Documentar incidencias relacionadas con la simulación.
* Apoyar la gestión y seguimiento de riesgos del proyecto.

Este componente tiene una función de apoyo y documentación dentro del sistema.

---

# 4. Flujo General del Sistema

```text
Usuario
   │
   ▼
Main.java
   │
   ▼
OrekitConfig.java
   │
   ▼
LEOSimulation.java
   │
   ▼
Resultados
```

### Descripción del Flujo

1. El usuario ejecuta la aplicación.
2. Main.java inicia el sistema.
3. OrekitConfig.java configura la biblioteca Orekit y carga los datos necesarios.
4. LEOSimulation.java ejecuta la simulación orbital.
5. El sistema calcula y genera los resultados.
6. Los resultados son presentados al usuario.

**Nota:** El módulo RiskReport.java funciona como apoyo al proyecto y no forma parte directa del flujo principal de ejecución.

---

# 5. Tecnologías Utilizadas

| Tecnología         | Función                                                          |
| ------------------ | ---------------------------------------------------------------- |
| Java 17            | Lenguaje principal utilizado para el desarrollo del simulador.   |
| Orekit             | Biblioteca especializada para cálculos y simulaciones orbitales. |
| Maven              | Gestión de dependencias y construcción del proyecto.             |
| Git                | Control de versiones local.                                      |
| GitHub             | Almacenamiento remoto y colaboración del proyecto.               |
| Visual Studio Code | Editor de código utilizado durante el desarrollo.                |
| IntelliJ IDEA      | Entorno de desarrollo integrado para programación y pruebas.     |
| Trello             | Gestión de tareas y seguimiento de actividades del equipo.       |

---

# 6. Arquitectura General

```text
+----------------+
|    Usuario     |
+--------+-------+
         |
         v
+----------------+
|   Main.java    |
+--------+-------+
         |
         v
+----------------+
| OrekitConfig   |
+--------+-------+
         |
         v
+----------------+
| LEOSimulation  |
+--------+-------+
         |
         v
+----------------+
|  Resultados    |
+----------------+

        |
        +------------------+
                           |
                           v
                  +----------------+
                  |  RiskReport    |
                  +----------------+
```

La arquitectura modular permite mantener una clara separación de responsabilidades, facilitando el mantenimiento del sistema y futuras ampliaciones relacionadas con simulaciones más avanzadas.

---

# 7. Conclusión

La arquitectura propuesta para el Simulador de Misión Lunar Artemis II establece una base sólida para el desarrollo del proyecto. La separación de responsabilidades entre los distintos módulos mejora la organización del código, simplifica las tareas de mantenimiento y permite la incorporación de nuevas capacidades de simulación en etapas posteriores del desarrollo.
