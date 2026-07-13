# Decisiones del Proyecto

## Simulador de Misión Lunar Artemis II

**Proyecto:** Simulador de Misión Lunar Artemis II  
**Repositorio:** Programadores-Lunares  
**Estado actual:** Desarrollo y validación del Spike TLI  
**Última actualización:** Julio de 2026  

---

# 1. Historial de decisiones técnicas

## DT-001 – Selección de Java 17

**Estado:** Aceptada

Se seleccionó **Java 17** como lenguaje principal para desarrollar el Simulador de Misión Lunar Artemis II.

### Justificación

Java permite trabajar con programación orientada a objetos y es compatible con las bibliotecas y herramientas establecidas para el proyecto, especialmente Orekit, Maven y JavaFX.

### Consecuencias

- Permite integrar la biblioteca Orekit.
- Facilita la organización del sistema mediante clases y paquetes.
- Mantiene compatibilidad con las tecnologías definidas para el proyecto.
- Requiere que los integrantes tengan instalado Java 17 o una versión superior.

---

## DT-002 – Uso de Orekit para la simulación orbital

**Estado:** Aceptada

Se decidió utilizar **Orekit** como biblioteca principal para realizar los cálculos relacionados con mecánica orbital y propagación de trayectorias.

Orekit proporciona clases especializadas para trabajar con:

- Órbitas.
- Fechas y escalas temporales.
- Marcos de referencia.
- Estados de vehículos espaciales.
- Propagadores orbitales.
- Posiciones y velocidades.
- Maniobras y trayectorias espaciales.

### Justificación

Orekit es una biblioteca especializada en dinámica espacial y permite desarrollar simulaciones orbitales utilizando componentes ya preparados para este tipo de cálculos.

### Consecuencias

- El proyecto utiliza una herramienta especializada en astrodinámica.
- Los integrantes deben comprender la configuración básica de Orekit.
- Una configuración incorrecta de los datos puede impedir la ejecución.
- Las dependencias de Orekit deben administrarse mediante Maven.

---

## DT-003 – Desarrollo de una simulación LEO inicial

**Estado:** Aceptada

Como primera validación técnica se implementó una simulación de una órbita terrestre baja, conocida como **LEO — Low Earth Orbit**.

Esta simulación permitió comprobar:

- La instalación de Java.
- La integración inicial de Orekit.
- La gestión de dependencias.
- La propagación de una órbita.
- La presentación de posiciones mediante consola.
- El funcionamiento general de la cadena de herramientas.

### Consecuencia

Se decidió conservar `LEOSimulation.java` como evidencia de la etapa inicial y como módulo independiente del Spike TLI.

---

## DT-004 – Adopción de una arquitectura modular

**Estado:** Aceptada

Se adoptó una arquitectura modular para separar las principales responsabilidades del simulador.

La implementación se encuentra organizada principalmente mediante las siguientes clases:

- **`Main.java`:** punto de entrada del programa.
- **`OrekitConfig.java`:** configuración e inicialización de Orekit.
- **`LEOSimulation.java`:** simulación inicial de órbita terrestre baja.
- **`TLIPrototype.java`:** prototipo de trayectoria de inyección translunar.

### Justificación

Esta separación permite:

- Evitar que toda la lógica se encuentre en una sola clase.
- Facilitar el mantenimiento.
- Ejecutar pruebas independientes.
- Reducir el riesgo de afectar módulos anteriores.
- Incorporar nuevas funcionalidades en futuras etapas.
- Mantener responsabilidades específicas para cada componente.

---

## DT-005 – Uso del Modelo Arquitectónico 4+1

**Estado:** Aceptada

Se decidió utilizar el **Modelo Arquitectónico 4+1** para documentar la arquitectura del simulador desde distintas perspectivas.

Las vistas utilizadas son:

- Vista lógica.
- Vista de procesos.
- Vista de desarrollo.
- Vista física.
- Escenarios o casos de uso.

### Justificación

El Modelo 4+1 permite describir:

- La estructura interna del sistema.
- El comportamiento de sus procesos.
- La organización del código.
- El entorno de ejecución.
- Los escenarios que validan la arquitectura.

### Consecuencia

Las modificaciones realizadas en la implementación deben revisarse para determinar si requieren actualizaciones en las vistas arquitectónicas.

---

## DT-006 – Uso de Git, GitHub y Trello

**Estado:** Aceptada

Para la organización, seguimiento y control del proyecto se decidió utilizar:

- **Git:** control de versiones local.
- **GitHub:** almacenamiento remoto y colaboración.
- **Trello:** planificación y seguimiento de tareas.
- **Visual Studio Code:** edición del código y documentación.
- **IntelliJ IDEA:** desarrollo y revisión del proyecto Java.
- **PowerShell:** ejecución de comandos en Windows.

### Consecuencias

- Cada integrante puede registrar sus contribuciones.
- El historial de cambios queda almacenado en GitHub.
- Las tareas pueden organizarse y actualizarse mediante Trello.
- Los cambios deben realizarse mediante ramas y solicitudes de incorporación.

---

## DT-007 – Selección de Maven como herramienta de construcción

**Estado:** Aceptada

Se decidió utilizar **Apache Maven** como herramienta principal para la gestión y construcción del proyecto.

Maven se utiliza para:

- Descargar Orekit y sus dependencias.
- Compilar las clases Java.
- Generar los archivos de compilación.
- Ejecutar la clase principal.
- Mantener una estructura estándar.
- Facilitar la ejecución en diferentes equipos.

Las dependencias se administran mediante el archivo `pom.xml`.

### Justificación

Maven permite mantener una configuración centralizada y evita que los integrantes tengan que descargar manualmente archivos JAR.

Aunque anteriormente existía un archivo `build.gradle`, se decidió utilizar Maven como herramienta principal para evitar trabajar simultáneamente con dos sistemas de construcción.

### Consecuencias

- El proyecto debe compilar correctamente con Maven.
- El archivo `pom.xml` debe mantenerse actualizado.
- Las dependencias deben declararse dentro del archivo `pom.xml`.
- Los integrantes deben tener Maven instalado o utilizar un entorno que lo incluya.

---

## DT-008 – Desarrollo de un Spike TLI

**Estado:** Aceptada

Se decidió desarrollar un **Spike de Trayectoria de Inyección Translunar — TLI** como prueba de concepto antes de implementar una simulación translunar completa.

El Spike permite comprobar que el sistema puede representar una trayectoria que:

1. Parte desde una región cercana a la Tierra.
2. Se aleja hacia una distancia similar a la región lunar.
3. Se aproxima a una representación simplificada de la Luna.
4. Alcanza una distancia máxima respecto a la Tierra.
5. Presenta posteriormente una tendencia de regreso hacia la Tierra.

### Alcance

El Spike corresponde a un prototipo académico sin calidad de producción.

No representa una trayectoria operacional, certificada ni utilizada por la NASA.

---

## DT-009 – Ejecución del Spike sin interfaz gráfica

**Estado:** Aceptada

Para el Hito E4 se decidió desarrollar y ejecutar el Spike TLI mediante consola, sin integrar todavía una interfaz gráfica.

### Justificación

Esta decisión permite concentrar el trabajo en:

- La configuración de Orekit.
- La implementación de la trayectoria translunar.
- Los cálculos de distancia.
- La detección del acercamiento lunar.
- La inspección geométrica del retorno.
- La identificación de errores técnicos.

La interfaz gráfica será incorporada en una etapa posterior del proyecto.

### Consecuencias positivas

- Reduce la complejidad inicial.
- Facilita la revisión de los cálculos.
- Permite validar primero el componente técnico.
- Facilita la demostración de resultados mediante consola.

### Limitaciones

- La trayectoria no cuenta todavía con representación visual.
- Los resultados deben interpretarse mediante datos en consola.
- La interacción del usuario es limitada.

---

## DT-010 – Separación entre la simulación LEO y el prototipo TLI

**Estado:** Aceptada

Se decidió crear `TLIPrototype.java` como un módulo independiente de `LEOSimulation.java`.

### Justificación

Esta decisión permite:

- Mantener disponible la simulación LEO anterior.
- Evitar mezclar dos tipos de simulación en una sola clase.
- Ejecutar pruebas independientes.
- Facilitar futuras modificaciones del módulo TLI.
- Reducir el riesgo de afectar funcionalidades ya desarrolladas.
- Mantener una separación clara de responsabilidades.

---

## DT-011 – Modelo orbital utilizado en el Spike TLI

**Estado:** Aceptada para el prototipo

Para representar la trayectoria TLI se decidió utilizar una órbita terrestre altamente elíptica.

Los parámetros registrados para la prueba de concepto son:

- Altitud aproximada de perigeo: **300 km**.
- Distancia aproximada de apogeo: **405,000 km**.
- Inclinación: **5°**.
- Duración de propagación: **10 días**.
- Intervalo de inspección: **12 horas**.
- Marco de referencia: **EME2000**.
- Propagador utilizado: **KeplerianPropagator**.

### Justificación

Esta configuración permite alcanzar una región cercana a la distancia media entre la Tierra y la Luna.

### Limitación

La órbita inicial de aproximadamente 300 km no coincide con la referencia cercana a 185 km establecida para una versión posterior del proyecto.

Este parámetro deberá ajustarse durante las siguientes etapas.

---

## DT-012 – Representación simplificada de la Luna

**Estado:** Aceptada temporalmente

Para el Spike TLI se decidió representar la posición de la Luna mediante un modelo circular simplificado alrededor de la Tierra.

El modelo utiliza:

- Distancia lunar media aproximada de **384,400 km**.
- Periodo orbital aproximado de **27.321661 días**.
- Movimiento circular en el plano XY.
- Ajuste de fase para aproximar el encuentro entre la nave y la región lunar.

### Justificación

El entregable requiere una prueba de concepto sin calidad de producción.

El modelo simplificado permite:

- Representar conceptualmente la región lunar.
- Calcular la distancia aproximada entre la nave y la Luna.
- Identificar el momento de mayor acercamiento lunar.
- Comprobar el flujo general de la simulación.

### Limitaciones

- No utiliza efemérides lunares reales.
- No representa completamente la interacción gravitacional Tierra-Luna.
- No ofrece la precisión requerida para una validación final.
- Deberá sustituirse o mejorarse en futuras versiones.

---

## DT-013 – Ejecución sin integración completa de `orekit-data`

**Estado:** Aceptada temporalmente

Durante las pruebas se identificó que los archivos de `orekit-data` no estaban completamente disponibles o configurados en el entorno de ejecución.

Para permitir la ejecución del Spike se decidió:

- Utilizar la escala temporal TAI.
- Mantener el marco inercial EME2000.
- Utilizar propagación kepleriana básica.
- Permitir que `OrekitConfig.java` continúe en un modo básico cuando no encuentre los datos externos.

### Consecuencias

- La prueba de concepto puede ejecutarse.
- La solución no sustituye la integración completa de los datos de Orekit.
- Los datos externos deberán incorporarse en futuras etapas.
- La precisión del modelo actual es limitada.

---

## DT-014 – Verificación geométrica del retorno libre

**Estado:** Aceptada para el Spike

Se decidió verificar la geometría del retorno mediante la inspección de las distancias calculadas durante la propagación.

Se establecieron los siguientes criterios:

- La nave debe superar los **100,000 km** de distancia respecto a la Tierra.
- La nave debe aproximarse a menos de **50,000 km** de la Luna simplificada.
- Después del máximo alejamiento, la distancia respecto a la Tierra debe comenzar a disminuir.

Cuando estas condiciones se cumplen, el programa indica que existe una geometría de retorno visible mediante inspección.

### Justificación

La inspección geométrica permite verificar conceptualmente el comportamiento general de la trayectoria dentro del alcance académico del Spike.

### Limitación

Esta comprobación no constituye una simulación gravitacional completa de una trayectoria de retorno libre.

---

## DT-015 – Resultados registrados del Spike TLI

**Estado:** Verificados mediante ejecución

La ejecución documentada del prototipo produjo los siguientes resultados:

| Resultado | Valor |
|---|---:|
| Mayor acercamiento lunar | 22,445.02 km |
| Tiempo del mayor acercamiento | 132 horas |
| Distancia máxima a la Tierra | 404,870.37 km |
| Distancia final a la Tierra | 170,955.32 km |
| Duración total propagada | 240 horas |

El programa permitió observar:

- La salida de la región terrestre.
- La aproximación a la región lunar.
- El mayor acercamiento lunar.
- La máxima distancia alcanzada.
- La tendencia de regreso hacia la Tierra.
- La finalización correcta de la ejecución.
- El resultado `BUILD SUCCESS`.

Estos valores deberán actualizarse si la ejecución final del repositorio produce resultados diferentes.

---

## DT-016 – Alcance y fidelidad del prototipo

**Estado:** Aceptada

Se decidió declarar claramente que el Spike TLI es una **prueba de concepto académica**.

El prototipo no incluye todavía:

- Gravedad lunar aplicada directamente sobre la nave.
- Gravedad solar.
- Efemérides reales de la Luna.
- Modelos de fuerza avanzados.
- Perturbaciones orbitales completas.
- Consumo de combustible.
- Masa variable.
- Correcciones de trayectoria.
- Condiciones reales de reentrada.
- Interfaz gráfica.

### Consecuencia

Los resultados no deben presentarse como una trayectoria real, certificada ni utilizada por la NASA.

---

## DT-017 – Organización de las evidencias

**Estado:** Aceptada

Se decidió conservar y organizar las siguientes evidencias del Entregable 3:

- Capturas del código fuente.
- Capturas de `Main.java`.
- Capturas de `OrekitConfig.java`.
- Capturas de `TLIPrototype.java`.
- Capturas de la ejecución mediante consola.
- Evidencia del mayor acercamiento lunar.
- Evidencia de la tendencia de retorno.
- Documento de validación de requisitos.
- Documento de arquitectura del módulo TLI.
- Evidencia del tablero de Trello actualizado.
- Material preparado para la demostración.

### Justificación

Estas evidencias permiten:

- Comprobar el funcionamiento del prototipo.
- Facilitar la revisión del instructor.
- Mantener trazabilidad del trabajo realizado.
- Preparar la demostración del equipo.
- Respaldar el contenido del repositorio.

---

## DT-018 – Actualización de la documentación

**Estado:** Aceptada

Se decidió actualizar la documentación del repositorio para reflejar la incorporación del Spike TLI.

Los documentos actualizados o revisados incluyen:

- `README.md`.
- `decisiones.md`.
- `SpikeTLI.md`.
- Documento de validación de requisitos.
- Documentación de arquitectura del módulo TLI.
- Evidencias del funcionamiento.
- Tablero de Trello.

### Consecuencia

La documentación debe coincidir con el código real y con la estructura actual del repositorio.

No deben incluirse clases, funciones, resultados o archivos que no existan dentro del proyecto.

---

# 2. Validación del Entregable 3

La implementación fue revisada por el responsable de requisitos del equipo.

Durante la validación se confirmó que:

- El Spike TLI fue desarrollado utilizando Orekit.
- La trayectoria fue implementada en Java.
- La simulación se ejecuta mediante consola.
- El prototipo funciona sin interfaz gráfica.
- El programa demuestra un acercamiento a la región lunar.
- Se calcula la distancia de la nave respecto a la Tierra.
- Se calcula la distancia de la nave respecto a la Luna.
- Se identifica el momento de mayor acercamiento lunar.
- Se observa una tendencia de retorno hacia la Tierra.
- La geometría de retorno puede verificarse mediante inspección.
- Las limitaciones del prototipo fueron documentadas.
- Se conservaron capturas de la ejecución.

**Estado del Entregable 3: Aprobado para entrega dentro del alcance académico del prototipo.**

---

# 3. Uso de inteligencia artificial

## 3.1 Herramienta utilizada

Se utilizó **ChatGPT** como herramienta de apoyo durante diferentes actividades del proyecto.

## 3.2 Propósito

La inteligencia artificial fue utilizada para apoyar:

- La comprensión inicial de conceptos relacionados con Orekit.
- La investigación sobre órbitas LEO y trayectorias TLI.
- La explicación del concepto de retorno libre.
- La revisión de la estructura del proyecto.
- La separación de responsabilidades entre clases.
- La identificación de errores de compilación.
- La organización de la documentación.
- La revisión del archivo `README.md`.
- La estructuración del archivo `decisiones.md`.
- La corrección del formato Markdown.
- La revisión de rutas y paquetes.
- La orientación sobre la configuración de Maven.
- La organización de tareas y evidencias.

## 3.3 Partes técnicas asistidas

La inteligencia artificial proporcionó orientación relacionada con:

- Separación de responsabilidades entre clases.
- Uso de `KeplerianOrbit`.
- Uso de `KeplerianPropagator`.
- Cálculo de posiciones mediante `Vector3D`.
- Cálculo de distancias Tierra–nave y Luna–nave.
- Definición de criterios de inspección geométrica.
- Organización de los resultados mostrados mediante consola.
- Configuración inicial de Maven.
- Organización de los archivos dentro del paquete `com.nasa.simulador`.
- Revisión de errores relacionados con Java y Maven.

## 3.4 Revisión humana aplicada

Todo contenido asistido por inteligencia artificial fue revisado, probado y adaptado por los integrantes del equipo antes de incorporarse al proyecto.

La revisión humana incluyó:

- Corrección de nombres de clases y métodos.
- Verificación de las rutas del proyecto.
- Organización de los paquetes Java.
- Ajuste de parámetros orbitales.
- Revisión de la escala temporal utilizada.
- Instalación y configuración de Maven.
- Verificación de la compilación.
- Comparación de la documentación con el código real.
- Revisión de los resultados mostrados mediante consola.
- Validación del acercamiento lunar.
- Verificación de la tendencia de retorno.
- Adaptación de la documentación al contexto académico.

La inteligencia artificial no sustituyó las decisiones del equipo ni la revisión técnica de los responsables.

## 3.5 Responsabilidad del equipo

Las decisiones finales relacionadas con:

- Arquitectura.
- Código.
- Parámetros.
- Organización.
- Documentación.
- Presentación.
- Validación.

fueron tomadas y aprobadas por los integrantes del proyecto.

Cada integrante es responsable de comprender y explicar el contenido correspondiente a su participación.

---

# 4. Registro de actualizaciones

## Entregable 0 – Inicio y planificación

- Creación del repositorio en GitHub.
- Diseño del distintivo del equipo.
- Configuración inicial de Orekit.
- Desarrollo de la primera simulación orbital LEO.
- Elaboración del registro inicial de riesgos.

## Entregable 1 – Requisitos

- Elaboración del Documento de Especificación de Requisitos de Software.
- Definición de requisitos funcionales.
- Definición de requisitos no funcionales.
- Creación de casos de uso.
- Organización inicial de la documentación.
- Actualización de `decisiones.md`.

## Entregable 2 – Diseño y arquitectura

- Elaboración del Documento de Diseño de Software.
- Aplicación del Modelo Arquitectónico 4+1.
- Documentación de la arquitectura del sistema.
- Creación y revisión de diagramas y vistas arquitectónicas.
- Organización de la estructura del proyecto.
- Actualización del registro de decisiones.
- Actualización de la sección relacionada con el uso de IA.

## Entregable 3 – Spike TLI

- Instalación y configuración de Apache Maven.
- Actualización del archivo `pom.xml`.
- Integración de Orekit mediante Maven.
- Creación de `TLIPrototype.java`.
- Implementación de una trayectoria altamente elíptica.
- Implementación de un modelo lunar simplificado.
- Cálculo de distancias Tierra–nave y Luna–nave.
- Detección del mayor acercamiento lunar.
- Inspección geométrica de la tendencia de retorno.
- Corrección de errores de paquetes y rutas.
- Organización de la estructura Maven.
- Ejecución correcta del prototipo.
- Obtención de `BUILD SUCCESS`.
- Elaboración del documento `SpikeTLI.md`.
- Actualización del archivo `README.md`.
- Actualización de `decisiones.md`.
- Organización de evidencias.
- Validación de requisitos.
- Actualización del tablero de Trello.
- Preparación del material para la demostración.

---

# 5. Próximas decisiones

En etapas posteriores deberán registrarse decisiones relacionadas con:

- Integración completa de `orekit-data`.
- Uso de efemérides lunares reales.
- Implementación de propagación numérica.
- Inclusión de gravedad lunar y solar.
- Uso de `NumericalPropagator`.
- Implementación de una maniobra impulsiva TLI.
- Ajuste de la órbita inicial a aproximadamente 185 km.
- Correcciones de trayectoria.
- Integración con JavaFX.
- Visualización gráfica de la Tierra, la Luna y la nave.
- Panel de telemetría.
- Exportación de datos.
- Pruebas JUnit.
- Validación contra datos de referencia.
- Desarrollo del manual de usuario.
- Preparación del paquete ejecutable.

---

# 6. Observaciones finales

Este documento deberá mantenerse actualizado durante las siguientes etapas del proyecto.

Cada decisión técnica, arquitectónica, organizacional o relacionada con el uso de inteligencia artificial debe registrarse para conservar un historial claro y verificable de la evolución del sistema.

Toda modificación registrada en este documento debe coincidir con la implementación real, la documentación del repositorio y las evidencias presentadas por el equipo.
