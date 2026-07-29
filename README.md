<div align="center">

# 🚀 PROGRAMADORES LUNARES 🌖

<img width="1920" height="600" alt="SIMULADOR DE PROGRAMADORES LUNARES" src="https://github.com/user-attachments/assets/504c98ec-29a6-4ec1-bd84-ed2be2370764" />

## Artemis II Mission Simulator

<img src="https://readme-typing-svg.herokuapp.com?font=Orbitron&size=30&duration=3000&pause=1000&color=00D9FF&center=true&vCenter=true&width=900&lines=NASA+Mission+Simulation;Orekit+Orbital+Propagation;JavaFX+Mission+Visualization;JUnit+5+Testing;Flight+Dynamics+Software" />

---

<img src="https://img.shields.io/badge/STATUS-IN%20DEVELOPMENT-00D9FF?style=for-the-badge&logo=github&logoColor=white"/>
<img src="https://img.shields.io/badge/JAVA-17-orange?style=for-the-badge&logo=openjdk"/>
<img src="https://img.shields.io/badge/JAVAFX-17-blue?style=for-the-badge"/>
<img src="https://img.shields.io/badge/OREKIT-SPACE%20DYNAMICS-blue?style=for-the-badge"/>
<img src="https://img.shields.io/badge/MISSION-ARTEMIS%20II-black?style=for-the-badge"/>

</div>

---

# 🌌 Descripción del proyecto

El **Simulador de Misión Lunar Artemis II** es un proyecto académico desarrollado para la asignatura **Ingeniería de Software I (INF-272)**.

El objetivo consiste en implementar un simulador de misión lunar utilizando **Java**, **Orekit**, **JavaFX** y **Apache Maven**, siguiendo una arquitectura modular basada en los documentos **SRS** y **SDD**.

Durante el desarrollo del proyecto se implementaron progresivamente:

- Simulación de una órbita terrestre baja (LEO).
- Maniobra **Trans-Lunar Injection (TLI)**.
- Trayectoria hacia la Luna.
- Retorno libre.
- Interfaz gráfica desarrollada con JavaFX.
- Panel de telemetría.
- Parámetros configurables.
- Pruebas automatizadas con JUnit 5.
- Cobertura mediante JaCoCo.

El proyecto mantiene una arquitectura modular que separa la lógica de simulación de la interfaz gráfica.

---

# 👨‍🚀 Tripulación y Roles

| Integrante | Rol |
|---|---|
| Enmanuel Suriel | CDR |
| Jesús Polanco | FDO |
| Franklin Isaac Serrano | ARCH |
| John Mario Ventura Contreras | CAPCOM |
| Osvaldo Rafael Díaz Castro | REQ |

---

# 🎯 Objetivos del Proyecto

## Entregable #5

- Desarrollar pruebas unitarias con JUnit 5.
- Implementar pruebas utilizando Mockito.
- Generar reportes Surefire.
- Generar cobertura JaCoCo.
- Documentar la estrategia de pruebas.
- Mantener la trazabilidad entre requisitos y pruebas.

---

# 💻 Tecnologías utilizadas

- Java 17
- JavaFX
- Apache Maven
- Orekit
- Hipparchus
- Git
- GitHub
- IntelliJ IDEA
- Visual Studio Code
- Trello
- JUnit 5
- Mockito
- JaCoCo

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
│   ├── RequisitosTecnicos.md
│   ├── Riesgos.md
│   ├── SpikeTLI.md
│   ├── VistaLogica.md
│   ├── VistaProcesos.md
│   ├── decisiones.md
│   └── ValidacionRequisitos.pdf
│
├── Evidencias/
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
│                       ├── TLIPrototype.java
│                       ├── ui/
│                       │   ├── MissionSimulatorApp.java
│                       │   └── TrajectoryCanvas.java
│                       ├── model/
│                       │   └── TrajectoryPoint.java
│                       └── service/
│                           └── TrajectoryService.java
│
├── pom.xml
└── README.md
```

La carpeta **target/** no forma parte del repositorio debido a que contiene archivos generados automáticamente por Maven y se encuentra excluida mediante **.gitignore**.

# 📚 Componentes principales

## Main.java

Es el punto de entrada del simulador.

Sus responsabilidades son:

- Inicializar la aplicación.
- Configurar los componentes principales.
- Lanzar la interfaz JavaFX.
- Coordinar la ejecución general del sistema.

---

## OrekitConfig.java

Inicializa la biblioteca Orekit y prepara el entorno necesario para ejecutar las simulaciones orbitales.

Entre sus funciones principales se encuentran:

- Configuración del DataContext.
- Carga de datos de Orekit.
- Inicialización de los modelos físicos utilizados por el simulador.

---

## LEOSimulation.java

Implementa la simulación de la órbita terrestre baja (LEO).

Permite:

- Crear la órbita inicial.
- Configurar los parámetros de propagación.
- Validar el funcionamiento básico de Orekit.

---

## TLIPrototype.java

Implementa la maniobra **Trans-Lunar Injection (TLI)**.

Sus funciones principales incluyen:

- Generar la trayectoria translunar.
- Calcular la distancia respecto a la Tierra.
- Calcular la distancia respecto a la Luna.
- Detectar el mayor acercamiento lunar.
- Registrar los eventos importantes de la misión.

---

## MissionSimulatorApp.java

Clase principal de la interfaz gráfica desarrollada con JavaFX.

Permite:

- Crear la ventana principal.
- Inicializar los controles de usuario.
- Mostrar la simulación de la misión.

---

## TrajectoryCanvas.java

Componente encargado del dibujo de la simulación.

Permite representar:

- Tierra.
- Luna.
- Nave espacial.
- Trayectoria orbital.
- Movimiento de la misión.

---

## TrajectoryService.java

Servicio encargado de conectar el motor físico con la interfaz gráfica.

Sus responsabilidades incluyen:

- Obtener la trayectoria calculada.
- Transformar los datos para la interfaz.
- Actualizar la telemetría.

---

## TrajectoryPoint.java

Representa un punto individual de la trayectoria.

Cada punto almacena información como:

- Tiempo.
- Posición.
- Velocidad.
- Distancia a la Tierra.
- Distancia a la Luna.

---

# 🌍 Interfaz gráfica (JavaFX)

A partir del Entregable #4 el simulador incorpora una interfaz gráfica desarrollada utilizando JavaFX.

La aplicación permite visualizar gráficamente:

- 🌍 Tierra
- 🌙 Luna
- 🚀 Nave espacial
- 📈 Trayectoria orbital

Además incorpora un panel de control con información en tiempo real.

---

# 📊 Panel de telemetría

Durante la simulación se muestran continuamente:

- Tiempo de misión.
- Altitud.
- Velocidad.
- Distancia a la Tierra.
- Distancia a la Luna.
- Estado de la misión.

---

# 🎛️ Parámetros configurables

El usuario puede modificar:

- Magnitud del Delta-V.
- Altitud inicial.
- Tiempo del encendido TLI.
- Velocidad de reproducción.

Cada modificación genera una trayectoria diferente durante la simulación.

---

# 🎮 Controles disponibles

La interfaz incluye:

- ▶ Ejecutar
- ⏸ Pausar
- ⏯ Reproducir
- 🔄 Reiniciar

También incorpora un control de velocidad que permite acelerar la simulación desde:

```text
1× hasta 1000×
```

---

# 🌙 Funcionamiento general del simulador

El flujo principal de ejecución es:

```text
Main
      │
      ▼
OrekitConfig
      │
      ▼
LEOSimulation
      │
      ▼
TLIPrototype
      │
      ▼
TrajectoryService
      │
      ▼
MissionSimulatorApp
      │
      ▼
TrajectoryCanvas
```

Durante la ejecución:

1. Se inicializa Orekit.
2. Se genera la órbita de estacionamiento.
3. Se ejecuta la maniobra TLI.
4. Se propaga la trayectoria.
5. Se actualizan los datos de telemetría.
6. La interfaz JavaFX representa la simulación en tiempo real.
   # ▶️ Compilación y ejecución

## Requisitos previos

Antes de ejecutar el proyecto es necesario contar con:

- Java JDK 17 o superior.
- Apache Maven 3.9 o superior.
- JavaFX SDK.
- Git.
- IntelliJ IDEA o Visual Studio Code.

---

## Clonar el repositorio

```bash
git clone https://github.com/Jmpd17/programadores-lunares.git
cd programadores-lunares
```

---

## Compilar el proyecto

Desde la carpeta principal ejecutar:

```bash
mvn clean package
```

Si la compilación finaliza correctamente deberá mostrarse:

```text
BUILD SUCCESS
```

---

## Ejecutar la aplicación

Desde Maven:

```bash
mvn javafx:run
```

También puede ejecutarse desde el IDE iniciando la clase principal del proyecto.

---

# 🚀 Funcionamiento de la simulación

Una vez iniciada la aplicación el usuario podrá:

1. Modificar los parámetros de entrada.
2. Ejecutar la simulación.
3. Observar la trayectoria de la nave.
4. Consultar la telemetría.
5. Cambiar la velocidad de reproducción.
6. Reiniciar la simulación.

Durante la ejecución se visualizarán:

- Tierra.
- Luna.
- Nave espacial.
- Trayectoria.
- Panel de telemetría.

---

# 🧪 Pruebas automatizadas

El proyecto incorpora pruebas automatizadas para verificar el correcto funcionamiento del simulador.

Las pruebas fueron desarrolladas utilizando:

- JUnit 5.
- Mockito.

Se implementaron:

- Pruebas unitarias.
- Pruebas de integración.

---

## Ejecutar las pruebas

```bash
mvn clean test
```

Este comando ejecuta automáticamente todas las pruebas definidas en el proyecto.

---

# 📑 Reporte Surefire

Después de ejecutar las pruebas puede generarse el reporte HTML mediante:

```bash
mvn surefire-report:report
```

El reporte estará disponible en:

```text
target/site/surefire-report.html
```

---

# 📈 Cobertura JaCoCo

La cobertura del código se obtiene utilizando JaCoCo.

Generar el reporte mediante:

```bash
mvn jacoco:report
```

El informe HTML queda disponible en:

```text
target/site/jacoco/index.html
```

El objetivo del proyecto es mantener una cobertura significativa sobre la lógica de simulación y los modelos de la interfaz de usuario.

---

# 📊 Resultados esperados

Durante la ejecución del simulador se espera observar:

- Generación de la órbita inicial.
- Ejecución de la maniobra TLI.
- Aproximación a la Luna.
- Trayectoria de retorno.
- Actualización continua de la telemetría.
- Interfaz JavaFX funcionando correctamente.
- Finalización sin errores.

---

# ✅ Validación de requisitos

Los requisitos fueron revisados por el responsable del área REQ.

Durante la validación se comprobó:

- Funcionamiento de la simulación.
- Correcta integración con Orekit.
- Separación entre interfaz y motor físico.
- Funcionamiento de JavaFX.
- Telemetría.
- Parámetros configurables.
- Controles de reproducción.
- Velocidad configurable.
- Compatibilidad con Maven.
- Cumplimiento de los requisitos funcionales definidos para el proyecto.

Estado de validación:

**Aprobado para el Entregable #5.**
# ⚠️ Observaciones y limitaciones

El proyecto corresponde a un simulador académico desarrollado como parte de la asignatura **Ingeniería de Software I (INF-272)**.

Actualmente presenta las siguientes limitaciones:

- La simulación representa un modelo simplificado de la misión Artemis II.
- No utiliza todos los modelos físicos disponibles en Orekit.
- No contempla consumo de combustible.
- No implementa masa variable de la nave.
- No incorpora perturbaciones atmosféricas completas.
- No representa condiciones reales de reentrada.
- La interfaz JavaFX corresponde a una versión inicial para demostración académica.
- Las pruebas automatizadas se centran en la lógica del proyecto y no en el renderizado gráfico.

Los resultados obtenidos no deben interpretarse como una simulación certificada por la NASA.

---

# 📸 Evidencias del proyecto

Las evidencias organizadas para los Entregables #4 y #5 incluyen:

## Código fuente

- Main.java
- OrekitConfig.java
- LEOSimulation.java
- TLIPrototype.java
- MissionSimulatorApp.java
- TrajectoryCanvas.java
- TrajectoryService.java
- TrajectoryPoint.java

---

## Evidencias de ejecución

- Compilación mediante Maven.
- BUILD SUCCESS.
- Ejecución del simulador.
- Interfaz JavaFX.
- Trayectoria generada.
- Panel de telemetría.
- Parámetros configurables.
- Controles de simulación.

---

## Evidencias de pruebas

- Pruebas unitarias con JUnit 5.
- Pruebas utilizando Mockito.
- Reporte HTML de Surefire.
- Reporte HTML de JaCoCo.
- Documento de justificación del diseño de pruebas.
- Matriz de trazabilidad prueba–requisito.

---

## Documentación

- Arquitectura del sistema.
- Arquitectura del módulo TLI.
- Documento de decisiones.
- Documento de validación de requisitos.
- SpikeTLI.md.
- README actualizado.

---

## Gestión del proyecto

- Evidencias del tablero Trello.
- Historial Git.
- Pull Requests.
- Commits del equipo.

---

# 🧑‍🏫 Demostración

Durante la presentación del proyecto el equipo realizará el siguiente recorrido:

1. Presentación del objetivo general del simulador.
2. Explicación de la arquitectura del proyecto.
3. Compilación mediante Maven.
4. Ejecución de la interfaz JavaFX.
5. Presentación de la simulación.
6. Explicación del panel de telemetría.
7. Modificación de parámetros.
8. Ejecución de una nueva simulación.
9. Presentación de las pruebas automatizadas.
10. Visualización de los reportes Surefire y JaCoCo.
11. Conclusiones finales.

---

# 🚀 Estado del proyecto

El proyecto incorpora:

- Simulación orbital utilizando Orekit.
- Maniobra Trans-Lunar Injection (TLI).
- Interfaz gráfica desarrollada con JavaFX.
- Visualización de Tierra, Luna, nave y trayectoria.
- Panel de telemetría.
- Parámetros configurables.
- Controles de simulación.
- Arquitectura modular.
- Pruebas unitarias con JUnit 5.
- Uso de Mockito para pruebas de modelos.
- Reportes Surefire.
- Cobertura mediante JaCoCo.
- Documentación técnica.
- Evidencias de validación.
- Gestión del proyecto mediante GitHub y Trello.

El simulador constituye una base sólida para futuras mejoras relacionadas con dinámica orbital, visualización avanzada y simulaciones espaciales de mayor complejidad.

---

# 📜 Licencia

Proyecto desarrollado exclusivamente con fines académicos para la asignatura **Ingeniería de Software I (INF-272)**.

---

<div align="center">

## 🚀 Programadores Lunares

**"Simulando hoy las misiones del mañana."**

</div>
