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

| Jesús Polanco  |FDO|

| John Ventura |CAPCOM|

|  Isaac Serrano |ARCH|

| Osvaldo  Díaz  |CAPCOM|

| Emmanuel  Suriel |REQ|

#  Objetivos Iniciales

✅ Integrar la librería Orekit  
✅ Realizar propagación orbital LEO  
✅ Verificar la cadena de herramientas  
✅ Documentar riesgos técnicos  
✅ Preparar arquitectura inicial del simulador  

#  💻Tecnologías Utilizadas

| Tecnología | Uso |

| Java 17 | Desarrollo principal |

| Orekit | Dinámica orbital |

| Maven | Gestión de dependencias |

| GitHub | Control de versiones |

| JetBrain | Entorno de desarrollo |


#  🗂️ Estructura del Proyecto

# 2. Estructura del Proyecto

```text
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
```

## Descripción General

La estructura del proyecto se encuentra organizada en directorios de configuración, código fuente, documentación y archivos de construcción.

### .gradle/

Contiene archivos generados automáticamente por Gradle para la gestión de dependencias y compilación del proyecto.

### .idea/

Incluye archivos de configuración utilizados por IntelliJ IDEA.

### .vscode/

Contiene configuraciones específicas utilizadas por Visual Studio Code.

### src/

Directorio principal donde se encuentra el código fuente y documentación desarrollada por el equipo.

#### Main.java

Punto de entrada del simulador. Coordina la ejecución general del sistema.

#### OrekitConfig.java

Configura e inicializa la biblioteca Orekit para permitir la ejecución de simulaciones orbitales.

#### LEOSimulation.java

Implementa la lógica de propagación orbital y simulación de órbitas terrestres bajas (LEO).

#### Docs/

Contiene la documentación técnica generada durante el desarrollo del proyecto.

### target/

Directorio generado automáticamente durante la compilación. Contiene los archivos ejecutables (.class) producidos por Java.

### pom.xml

Archivo de configuración de Maven utilizado para administrar dependencias y automatizar la construcción del proyecto.

### build.gradle

Archivo de configuración de Gradle utilizado para tareas de compilación y gestión de dependencias.

### README.md

Documento principal del repositorio que describe el proyecto, objetivos y estructura general.

### .gitignore

Especifica los archivos y directorios que Git debe ignorar durante el control de versiones.

### RaizappJava.iml

Archivo de configuración generado por IntelliJ IDEA para el proyecto.


