<div align="center">

#     🚀 PROGRAMADORES LUNARES 🌖
<img width="1920" height="600" alt="SIMULADOR DE PROGRAMADORES LUNARES" src="https://github.com/user-attachments/assets/504c98ec-29a6-4ec1-bd84-ed2be2370764" />

##   Artemis II Mission Simulator

<img src="https://readme-typing-svg.herokuapp.com?font=Orbitron&size=30&duration=3000&pause=1000&color=00D9FF&center=true&vCenter=true&width=900&lines=NASA+Mission+Simulation;Orekit+Orbital+Propagation;LEO+Trajectory+System;Flight+Dynamics+Software" />

---

<imgs src="https://img.shields.io/badge/STATUS-IN%20DEVELOPMENT-00D9FF?style=for-the-badge&logo=github&logoColor=white"/>
<imgs src="https://img.shields.io/badge/JAVA-17-orange?style=for-the-badge&logo=openjdk"/>
<imgs src="https://img.shields.io/badge/OREKIT-SPACE%20DYNAMICS-blue?style=for-the-badge"/>
<imgs src="https://img.shields.io/badge/MISSION-ARTEMIS%20II-black?style=for-the-badge"/>

---

</div>

# 🌌 Descripción del Proyecto

El **Simulador de Misión Lunar Artemis II** es un proyecto académico orientado al desarrollo de una plataforma de simulación espacial basada en tecnologías reales utilizadas en dinámica orbital y misiones aeroespaciales.

El objetivo principal es recrear procesos fundamentales de navegación y propagación orbital mediante el uso de la librería **Orekit**, permitiendo simular trayectorias espaciales, telemetría y futuras operaciones relacionadas con misiones lunares.

# 👨‍🚀 TRIPULACIÓN |ROL|

| Enmanuel Suriel  |CDR|

| Jesus Polanco |FDO|

| Franklin Isaac Serrano |ARCH|

| John Mario Ventura  |CAPCOM|

| Osvaldo Diaz |REQ|

# 🎯 Objetivos del Entregable #3 – Spike TLI

 Desarrollar un prototipo de Trayectoria de Inyección Translunar utilizando Orekit.

 Implementar una trayectoria de sobrevuelo lunar en Java.

 Ejecutar la simulación mediante consola, sin interfaz gráfica.

 Calcular la distancia de la nave respecto a la Tierra y la Luna.

 Identificar el momento de mayor acercamiento lunar.

 Inspeccionar geométricamente la tendencia de retorno hacia la Tierra.

 Actualizar la documentación técnica y las evidencias del proyecto.

 Mantener actualizado el tablero de Trello.


💻 Tecnologías utilizadas

- ☕ Java 17
- 📦 Apache Maven
- 🛰️ Orekit
- 🌐 GitHub
- 🔀 Git
- 💻 Visual Studio Code
- 🧠 IntelliJ IDEA
- 📋 Trello

 # 🗂️ Estructura del Proyecto

```text
programadores-lunares/
│
├── src/
│   ├── Docs/
│   │   ├── ArquitecturaSimulacion.md
│   │   ├── ArquitecturaSistemas.md
│   │   ├── Requisitos Tecnicos.md
│   │   ├── Riegos.md
│   │   ├── Simulacion Tecnica.md
│   │   ├── SpikeTLI.md
│   │   ├── VistaLogica.md
│   │   ├── VistaProcesos.md
│   │   └── decisiones.md
│   │
│   └── main/
│       └── java/
│           └── com/
│               └── nasa/
│                   └── simulador/
│                       ├── Main.java
│                       ├── OrekitConfig.java
│                       └── TLIPrototype.java
│
├── pom.xml
├── README.md
└── .gitignore

✔️ Funcionamiento general

El Spike TLI ejecuta una simulación orbital orientada a representar una trayectoria translunar.

Durante su funcionamiento, el prototipo permite observar información relacionada con:

La trayectoria de la nave desde una órbita terrestre.
El desplazamiento de la nave hacia la región lunar.
 La distancia de la nave respecto a la Tierra.
 La distancia de la nave respecto a la Luna.
 El momento de mayor acercamiento lunar.
 La máxima distancia orbital alcanzada.
 La tendencia de retorno de la nave hacia la Tierra.
 El resultado de la inspección geométrica del retorno libre.

Ejecución

El prototipo se ejecuta mediante consola desde el proyecto Java configurado con Maven.

Antes de ejecutarlo se debe verificar que:

Java se encuentre instalado correctamente.
 Maven esté disponible en el equipo.
 Las dependencias indicadas en el archivo `pom.xml` hayan sido descargadas.
 Los datos requeridos por Orekit estén correctamente configurados.
 La clase principal del prototipo pueda ejecutarse sin errores.

La trayectoria y los resultados de la simulación se muestran mediante la salida de consola.

 ✅Validación realizada

La revisión de requisitos confirmó que:

El Spike TLI fue desarrollado utilizando Orekit.
La trayectoria fue implementada en Java.
 La simulación se ejecuta sin interfaz gráfica.
 El prototipo demuestra un acercamiento a la región lunar.
 El sistema permite inspeccionar geométricamente una tendencia de retorno hacia la Tierra.

Demostración

El equipo realizará una demostración técnica ante el instructor.

Durante la demostración se ejecutará el prototipo, se mostrarán los resultados de la trayectoria mediante consola y se explicará el funcionamiento general de la prueba de concepto.


