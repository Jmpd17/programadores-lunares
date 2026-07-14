<div align="center">

# 🚀 PROGRAMADORES LUNARES 🌖

<img width="1920" height="600" alt="SIMULADOR DE PROGRAMADORES LUNARES" src="https://github.com/user-attachments/assets/504c98ec-29a6-4ec1-bd84-ed2be2370764" />

## Artemis II Mission Simulator

<img src="https://readme-typing-svg.herokuapp.com?font=Orbitron&size=30&duration=3000&pause=1000&color=00D9FF&center=true&vCenter=true&width=900&lines=NASA+Mission+Simulation;Orekit+Orbital+Propagation;Trans-Lunar+Injection;Flight+Dynamics+Software" />

---

<img src="https://img.shields.io/badge/STATUS-IN%20DEVELOPMENT-00D9FF?style=for-the-badge&logo=github&logoColor=white"/>
<img src="https://img.shields.io/badge/JAVA-17-orange?style=for-the-badge&logo=openjdk"/>
<img src="https://img.shields.io/badge/OREKIT-SPACE%20DYNAMICS-blue?style=for-the-badge"/>
<img src="https://img.shields.io/badge/MISSION-ARTEMIS%20II-black?style=for-the-badge"/>

</div>

---

# 🌌 Descripción del proyecto

El **Simulador de Misión Lunar Artemis II** es un proyecto académico orientado al desarrollo de una plataforma de simulación espacial basada en conceptos de dinámica orbital e ingeniería de software.

El proyecto utiliza la biblioteca **Orekit** para representar órbitas, trayectorias y otros elementos relacionados con una misión lunar.

Para el **Entregable #3 – Hito E4**, se desarrolló un Spike o prueba de concepto de una **Trayectoria de Inyección Translunar — TLI**.

El prototipo fue desarrollado en Java, se ejecuta mediante consola y no incluye todavía una interfaz gráfica.

---

# 👨‍🚀 Tripulación y roles

| Integrante | Rol |
|---|---|
| Enmanuel Suriel | CDR |
| Jesús Polanco | FDO |
| Franklin Isaac Serrano | ARCH |
| John Mario Ventura Contreras | CAPCOM |
| Osvaldo Rafael Díaz Castro | REQ |

---

# 🎯 Objetivos del Entregable #3

- Desarrollar un prototipo de Trayectoria de Inyección Translunar utilizando Orekit.
- Implementar una trayectoria de sobrevuelo lunar en Java.
- Ejecutar la simulación mediante consola y sin interfaz gráfica.
- Calcular la distancia de la nave respecto a la Tierra.
- Calcular la distancia de la nave respecto a la Luna.
- Identificar el momento de mayor acercamiento lunar.
- Inspeccionar geométricamente la tendencia de retorno hacia la Tierra.
- Documentar las decisiones técnicas del prototipo.
- Organizar las evidencias de funcionamiento.
- Mantener actualizado el tablero de Trello.
- Preparar el material para la demostración ante el instructor.

---

# 💻 Tecnologías utilizadas

- Java 17.
- Apache Maven.
- Orekit.
- Git.
- GitHub.
- Visual Studio Code.
- IntelliJ IDEA.
- Trello.

---

# 🗂️ Estructura del proyecto

```text
programadores-lunares/
│
├── .gitignore
│
├── Docs/
│   ├── ArquitecturaSimulacion.md
│   ├── ArquitecturaSistemas.md
│   ├── Requisitos Tecnicos.md
│   ├── Riegos.md
│   ├── Simulacion Tecnica.md
│   ├── SpikeTLI.md
│   ├── VistaLogica.md
│   ├── VistaProcesos.md
│   └── decisiones.md
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── nasa/
│                   └── simulador/
│                       ├── Main.java
│                       ├── OrekitConfig.java
│                       ├── LEOSimulation.java
│                       └── TLIPrototype.java
│
├── Git.html
├── pom.xml
└── README.md
```

La carpeta `target/` no se incluye en el repositorio porque contiene archivos generados automáticamente por Maven y está excluida mediante `.gitignore`.

---

# 📚 Componentes principales

## `Main.java`

Es el punto de entrada del programa.

Se encarga de iniciar la aplicación y coordinar la ejecución de los componentes principales del simulador.

## `OrekitConfig.java`

Se encarga de configurar e inicializar la biblioteca Orekit.

Sus responsabilidades incluyen:

- Preparar el entorno requerido por Orekit.
- Gestionar la carga de datos disponibles.
- Permitir la ejecución de las simulaciones orbitales.

## `LEOSimulation.java`

Contiene la simulación inicial de una órbita terrestre baja o LEO.

Esta clase permitió comprobar la integración inicial de Java, Maven y Orekit.

## `TLIPrototype.java`

Contiene la lógica principal del Spike de Trayectoria de Inyección Translunar.

Sus responsabilidades incluyen:

- Definir las condiciones iniciales de la trayectoria.
- Representar una trayectoria altamente elíptica.
- Calcular la distancia de la nave respecto a la Tierra.
- Calcular la distancia de la nave respecto a la Luna.
- Detectar el momento de mayor acercamiento lunar.
- Registrar la máxima distancia respecto a la Tierra.
- Inspeccionar geométricamente la tendencia de retorno.
- Mostrar los resultados mediante consola.

---

# 🌙 Funcionamiento del Spike TLI

El Spike TLI representa una prueba de concepto de una trayectoria desde una órbita terrestre hacia una región cercana a la Luna.

El flujo general es:

```text
Main → OrekitConfig → TLIPrototype → Resultados en consola
```

Durante la ejecución:

1. `Main` inicia el programa.
2. `OrekitConfig` prepara la configuración básica de Orekit.
3. `TLIPrototype` crea y propaga la trayectoria.
4. El programa calcula las distancias de la nave respecto a la Tierra y la Luna.
5. Se identifica el momento de mayor acercamiento lunar.
6. Se registra la máxima distancia alcanzada.
7. Se inspecciona si la distancia respecto a la Tierra comienza a disminuir.
8. Los resultados se muestran mediante consola.

---

# ▶️ Compilación y ejecución

## Requisitos previos

Antes de ejecutar el proyecto se debe tener instalado:

- Java 17 o superior.
- Apache Maven.
- Git.
- Un entorno de desarrollo como IntelliJ IDEA o Visual Studio Code.

## Clonar el repositorio

```bash
git clone https://github.com/Jmpd17/programadores-lunares.git
cd programadores-lunares
```

## Compilar el proyecto

Desde la carpeta principal del repositorio, ejecutar:

```bash
mvn clean package
```

La compilación debe finalizar sin errores y mostrar:

```text
BUILD SUCCESS
```

## Ejecutar el Spike

1. Abrir el proyecto en IntelliJ IDEA o Visual Studio Code.
2. Esperar que Maven descargue las dependencias definidas en `pom.xml`.
3. Abrir la clase:

```text
src/main/java/com/nasa/simulador/Main.java
```

4. Ejecutar el método `main`.
5. Revisar los resultados mostrados en la consola.

---

# 📊 Resultados del Spike TLI

Durante la ejecución documentada del prototipo se obtuvieron los siguientes resultados:

| Resultado | Valor |
|---|---:|
| Mayor acercamiento lunar | 22,445.02 km |
| Tiempo del mayor acercamiento | 132 horas |
| Distancia máxima a la Tierra | 404,870.37 km |
| Distancia final a la Tierra | 170,955.32 km |
| Duración total propagada | 240 horas |

La ejecución permitió comprobar:

- La salida de la nave desde la región terrestre.
- La aproximación a la región lunar.
- La identificación del mayor acercamiento lunar.
- La máxima distancia alcanzada respecto a la Tierra.
- La disminución posterior de la distancia terrestre.
- La tendencia de retorno hacia la Tierra.
- La finalización correcta mediante `BUILD SUCCESS`.

---

# ✅ Validación de requisitos

La implementación fue revisada por el responsable de requisitos del equipo.

Durante la validación se confirmó que:

- El Spike TLI utiliza Orekit.
- La trayectoria está implementada en Java.
- La simulación se ejecuta mediante consola.
- El prototipo funciona sin interfaz gráfica.
- El sistema demuestra un acercamiento a la región lunar.
- Se calcula la distancia respecto a la Tierra.
- Se calcula la distancia respecto a la Luna.
- Se identifica el momento de mayor acercamiento lunar.
- Se observa una tendencia de retorno hacia la Tierra.
- La geometría de retorno puede verificarse mediante inspección.

**Estado de validación: Aprobado para entrega dentro del alcance académico del prototipo.**

---

# ⚠️ Observaciones y limitaciones

La implementación corresponde a una prueba de concepto académica y presenta las siguientes limitaciones:

- No posee calidad de producción.
- No incluye todavía una interfaz gráfica.
- La posición de la Luna se representa mediante un modelo simplificado.
- No utiliza efemérides lunares reales.
- La verificación del retorno libre se realiza mediante inspección geométrica.
- No incorpora todavía una simulación gravitacional completa Tierra-Luna.
- Utiliza una propagación kepleriana básica.
- La órbita inicial utilizada es de aproximadamente 300 km.
- La referencia del proyecto para una versión posterior es cercana a 185 km.
- No incluye gravedad solar.
- No incluye consumo de combustible ni masa variable.
- No simula las condiciones reales de reentrada.

Los resultados no deben presentarse como una trayectoria operacional, certificada ni utilizada por la NASA.

---

# 📸 Evidencias del Entregable #3

Las evidencias organizadas para este entregable incluyen:

- Capturas de `Main.java`.
- Capturas de `OrekitConfig.java`.
- Capturas de `TLIPrototype.java`.
- Capturas de la ejecución mediante consola.
- Evidencia del mayor acercamiento lunar.
- Evidencia de la tendencia de retorno.
- Evidencia de `BUILD SUCCESS`.
- Documento `SpikeTLI.md`.
- Documento de validación de requisitos.
- Documento de arquitectura del módulo TLI.
- Archivo `decisiones.md` actualizado.
- Evidencia del tablero de Trello actualizado.
- Pull request con los cambios de documentación.

---

# 🧑‍🏫 Demostración

Durante la demostración ante el instructor, el equipo realizará las siguientes acciones:

1. Presentar brevemente el objetivo del Spike TLI.
2. Mostrar la estructura principal del proyecto.
3. Ejecutar el prototipo.
4. Mostrar los resultados en consola.
5. Identificar el mayor acercamiento lunar.
6. Explicar la tendencia de retorno hacia la Tierra.
7. Presentar las limitaciones del prototipo.
8. Mostrar las evidencias y el tablero de Trello actualizado.

---

# 🚀 Estado del Entregable #3

El Spike TLI fue desarrollado, ejecutado y validado dentro del alcance establecido para el Hito E4.

El repositorio contiene:

- Código fuente del prototipo.
- Documentación técnica.
- Decisiones del proyecto.
- Arquitectura del módulo TLI.
- Validación de requisitos.
- Evidencias de ejecución.
- Historial de cambios mediante Git y GitHub.
- Pull request para revisión antes de integrar los cambios en `master`.

**Estado actual: listo para revisión y demostración.**
