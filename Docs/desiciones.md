Vamos a actualizar `decisiones.md` con las decisiones reales tomadas durante el desarrollo del **Spike TLI**, los ajustes técnicos, el uso de Maven y los resultados obtenidos.

# Decisiones del Proyecto

## Simulador de Misión Lunar Artemis II

**Proyecto:** Simulador de Misión Lunar Artemis II
**Repositorio:** Programadores-Lunares
**Estado actual:** Desarrollo del prototipo TLI
**Última actualización:** Julio de 2026

---

# 1. Historial de Decisiones

## DT-001 - Lenguaje de programación

Se seleccionó **Java 17** como lenguaje principal para el desarrollo del simulador debido a su compatibilidad con Orekit, su enfoque orientado a objetos y su estabilidad para el desarrollo de aplicaciones técnicas.

---

## DT-002 - Biblioteca de simulación orbital

Se decidió utilizar **Orekit** como biblioteca principal para realizar cálculos relacionados con mecánica orbital y propagación de trayectorias.

Orekit proporciona clases especializadas para trabajar con:

* Órbitas.
* Fechas y escalas temporales.
* Marcos de referencia.
* Estados de vehículos espaciales.
* Propagadores orbitales.

---

## DT-003 - Simulación inicial LEO

Como primera validación técnica se implementó una simulación de una órbita terrestre baja, conocida como **LEO (Low Earth Orbit)**.

Esta simulación permitió comprobar:

* La instalación de Java.
* La integración de Orekit.
* La gestión de dependencias.
* La propagación de una órbita.
* La presentación de posiciones en consola.

---

## DT-004 - Arquitectura modular

Se adoptó una arquitectura modular para separar las diferentes responsabilidades del simulador.

La implementación actual está compuesta principalmente por:

* **`Main.java`:** punto de entrada del sistema.
* **`OrekitConfig.java`:** configuración e inicialización de Orekit.
* **`LEOSimulation.java`:** simulación orbital LEO.
* **`TLIPrototype.java`:** prototipo de trayectoria TLI y sobrevuelo lunar.

Esta separación facilita el mantenimiento del código y permite ampliar el sistema sin modificar completamente los módulos anteriores.

---

## DT-005 - Modelo de arquitectura

Se adoptó el **Modelo Arquitectónico 4+1** para documentar el sistema desde diferentes perspectivas:

* Vista lógica.
* Vista de procesos.
* Vista de desarrollo.
* Vista física.
* Escenarios o casos de uso.

Este modelo permite representar la estructura, comportamiento, implementación y despliegue del simulador.

---

## DT-006 - Gestión del proyecto

Para el desarrollo, organización y seguimiento del proyecto se utilizan las siguientes herramientas:

* **Git:** control de versiones local.
* **GitHub:** almacenamiento remoto y colaboración.
* **Apache Maven:** gestión de dependencias, compilación y ejecución.
* **Trello:** planificación y seguimiento de tareas.
* **Visual Studio Code:** edición del código y documentación.
* **PowerShell:** ejecución de comandos en Windows 11.

---

## DT-007 - Selección de Maven como herramienta principal

Se decidió utilizar **Apache Maven** como herramienta principal para la gestión y construcción del proyecto.

Maven se utiliza para:

* Descargar Orekit y sus dependencias.
* Compilar las clases Java.
* Generar los archivos `.class`.
* Ejecutar la clase principal.
* Mantener una estructura estándar del proyecto.

Aunque existía un archivo `build.gradle`, se decidió trabajar principalmente con `pom.xml` para evitar utilizar dos sistemas de construcción al mismo tiempo.

---

## DT-008 - Desarrollo de un Spike TLI

Se decidió desarrollar un **Spike de trayectoria TLI** como prueba de concepto antes de implementar una simulación translunar de mayor fidelidad.

El Spike permite comprobar que el sistema puede representar una trayectoria que:

1. Parte desde una región cercana a la Tierra.
2. Se aleja hacia una distancia similar a la región lunar.
3. Se aproxima a una representación simplificada de la Luna.
4. Presenta posteriormente una tendencia de regreso hacia la Tierra.

El Spike no representa una trayectoria oficial ni una simulación operacional.

---

## DT-009 - Separación entre la simulación LEO y el prototipo TLI

Se creó la clase **`TLIPrototype.java`** como módulo independiente de `LEOSimulation.java`.

Esta decisión permite:

* Mantener disponible la simulación LEO anterior.
* Evitar mezclar dos tipos de simulación en una sola clase.
* Facilitar pruebas independientes.
* Permitir futuras mejoras del módulo TLI.
* Reducir el riesgo de afectar funcionalidades ya desarrolladas.

---

## DT-010 - Modelo orbital del Spike TLI

Para la prueba de concepto se decidió representar la trayectoria TLI mediante una **órbita terrestre altamente elíptica**.

Los parámetros principales utilizados fueron:

* Altitud de perigeo: **300 km**.
* Distancia de apogeo: **405,000 km**.
* Inclinación: **5°**.
* Duración de propagación: **10 días**.
* Intervalo de inspección: **12 horas**.
* Marco de referencia: **EME2000**.
* Propagador: **KeplerianPropagator**.

Esta configuración permite alcanzar una región cercana a la distancia media de la Luna.

---

## DT-011 - Representación simplificada de la Luna

Para esta etapa se decidió representar la Luna mediante un modelo circular simplificado alrededor de la Tierra.

El modelo utiliza:

* Distancia lunar media de **384,400 km**.
* Periodo orbital aproximado de **27.321661 días**.
* Movimiento circular en el plano XY.
* Ajuste de fase para aproximar el encuentro entre la nave y la región lunar.

Esta decisión fue tomada porque el entregable solicita un prototipo sin calidad de producción.

---

## DT-012 - Ejecución sin `orekit-data`

Durante las pruebas se detectó que la carpeta `orekit-data` no estaba disponible en la raíz del proyecto.

Para permitir la ejecución del Spike se decidió:

* Utilizar la escala temporal **TAI**.
* Mantener el marco inercial **EME2000**.
* Utilizar propagación kepleriana básica.
* Permitir que `OrekitConfig.java` continúe en modo básico cuando no encuentre `orekit-data`.

Esta decisión permite ejecutar la prueba de concepto, pero no reemplaza la necesidad futura de integrar correctamente los datos externos de Orekit.

---

## DT-013 - Verificación mediante inspección

La geometría de retorno se verifica mediante inspección de las distancias calculadas durante la propagación.

Se establecieron los siguientes criterios:

* La nave debe superar los **100,000 km** de distancia terrestre.
* La nave debe aproximarse a menos de **50,000 km** de la Luna simplificada.
* Después del máximo alejamiento, la distancia a la Tierra debe comenzar a disminuir.

Cuando estas condiciones se cumplen, el programa informa que la geometría de retorno es visible mediante inspección.

---

## DT-014 - Resultados del Spike TLI

La ejecución produjo los siguientes resultados:

| Resultado                     |         Valor |
| ----------------------------- | ------------: |
| Mayor acercamiento lunar      |  22,445.02 km |
| Tiempo del mayor acercamiento |     132 horas |
| Distancia máxima a la Tierra  | 404,870.37 km |
| Distancia final a la Tierra   | 170,955.32 km |
| Duración total propagada      |     240 horas |

El programa confirmó:

* La salida de la región terrestre.
* La llegada a la región de sobrevuelo lunar.
* La tendencia de regreso hacia la Tierra.
* La finalización correcta con `BUILD SUCCESS`.

---

## DT-015 - Alcance y fidelidad del prototipo

Se decidió declarar claramente que el Spike TLI es una **prueba de concepto académica**.

El prototipo no incluye:

* Gravedad lunar aplicada directamente sobre la nave.
* Gravedad solar.
* Efemérides reales de la Luna.
* Perturbaciones orbitales avanzadas.
* Consumo de combustible.
* Masa variable.
* Correcciones de trayectoria.
* Condiciones reales de reentrada.

Por esta razón, los resultados no deben presentarse como una trayectoria real, certificada o utilizada por NASA.

---

# 2. Uso de Inteligencia Artificial

La Inteligencia Artificial fue utilizada como herramienta de apoyo durante el desarrollo del proyecto.

Su uso estuvo enfocado en:

* Investigación inicial.
* Comprensión de conceptos técnicos.
* Organización de documentos.
* Revisión de código.
* Identificación de errores.
* Propuestas de mejora.
* Apoyo en la documentación del proyecto.

---

## 2.1 Actividades asistidas

La IA fue utilizada para apoyar las siguientes actividades:

* Comprensión de conceptos relacionados con Orekit.
* Investigación sobre órbitas LEO y trayectorias TLI.
* Explicación del concepto de retorno libre.
* Organización de la arquitectura del proyecto.
* Apoyo en la elaboración del SRS.
* Apoyo en el Documento de Arquitectura 4+1.
* Diseño inicial de la estructura de `TLIPrototype.java`.
* Revisión de errores de compilación.
* Configuración inicial de Maven.
* Revisión de las rutas y paquetes Java.
* Elaboración de documentación técnica.
* Organización de tareas en Trello.

---

## 2.2 Partes técnicas asistidas

Durante el desarrollo del Spike TLI, la IA brindó apoyo en:

* Separación de responsabilidades entre clases.
* Creación de una órbita altamente elíptica.
* Uso de `KeplerianOrbit`.
* Uso de `KeplerianPropagator`.
* Cálculo de posiciones mediante `Vector3D`.
* Cálculo de distancias Tierra–nave y Luna–nave.
* Definición de criterios de inspección.
* Organización del informe final mostrado en consola.
* Corrección de problemas relacionados con Java, Maven y estructura del proyecto.

---

## 2.3 Refinamientos realizados por el equipo

Todo el contenido asistido por Inteligencia Artificial fue revisado, probado y adaptado antes de ser incorporado al proyecto.

Entre los refinamientos realizados se encuentran:

* Corrección de nombres de clases y métodos.
* Organización de los archivos dentro del paquete `com.nasa.simulador`.
* Separación entre `LEOSimulation.java` y `TLIPrototype.java`.
* Ajuste de parámetros orbitales.
* Modificación de la escala temporal a TAI.
* Adaptación del programa para funcionar sin `orekit-data`.
* Instalación y configuración manual de Maven.
* Corrección de rutas de ejecución en Windows 11.
* Validación de la compilación mediante `BUILD SUCCESS`.
* Verificación manual de los resultados en consola.
* Adaptación de la documentación al contexto académico.

---

## 2.4 Validación humana

La validación final fue realizada por los integrantes del equipo.

Se comprobó manualmente:

* Que las clases compilaran.
* Que Maven reconociera el proyecto.
* Que Orekit se cargara correctamente.
* Que la propagación terminara sin errores.
* Que las distancias cambiaran durante la simulación.
* Que se detectara un acercamiento lunar.
* Que existiera una tendencia de regreso.
* Que el programa finalizara con `BUILD SUCCESS`.

---

## 2.5 Consideraciones sobre el uso de IA

La Inteligencia Artificial no fue utilizada para reemplazar las decisiones del equipo.

Las respuestas y propuestas generadas fueron:

* Revisadas.
* Probadas.
* Corregidas.
* Adaptadas.
* Documentadas.

Las decisiones finales sobre arquitectura, código, parámetros, organización y presentación fueron tomadas por los integrantes del proyecto.

---

# 3. Registro de Actualizaciones

## Entregable 0

* Creación del repositorio en GitHub.
* Diseño del distintivo del equipo.
* Configuración inicial de Orekit.
* Desarrollo de la primera simulación orbital LEO.
* Elaboración del registro inicial de riesgos.

---

## Entregable 1

* Elaboración del Documento de Especificación de Requisitos de Software.
* Definición de requisitos funcionales.
* Definición de requisitos no funcionales.
* Organización inicial de la documentación.
* Actualización de `decisiones.md`.

---

## Entregable 2

* Elaboración del Documento de Arquitectura de Software.
* Aplicación del Modelo Arquitectónico 4+1.
* Documentación de la arquitectura del módulo de simulación.
* Creación de documentos de arquitectura.
* Actualización del registro de decisiones.
* Actualización de la sección sobre uso de IA.

---

## Entregable 3 - Spike TLI

* Instalación y configuración de Apache Maven.
* Actualización del archivo `pom.xml`.
* Integración de Orekit mediante Maven.
* Creación de `TLIPrototype.java`.
* Implementación de la trayectoria altamente elíptica.
* Implementación del modelo lunar simplificado.
* Cálculo de distancias Tierra–nave y Luna–nave.
* Detección del mayor acercamiento lunar.
* Inspección de la tendencia de retorno.
* Corrección de errores de paquetes y rutas.
* Organización de la estructura Maven.
* Ejecución correcta del prototipo.
* Obtención de `BUILD SUCCESS`.
* Elaboración del documento `SpikeTLI.md`.

---

# 4. Próximas decisiones

En futuras etapas deberán registrarse decisiones relacionadas con:

* Incorporación de `orekit-data`.
* Uso de efemérides lunares reales.
* Implementación de propagación numérica.
* Inclusión de gravedad lunar y solar.
* Modelado de maniobras impulsivas.
* Correcciones de trayectoria.
* Visualización gráfica.
* Desarrollo de interfaz de usuario.
* Exportación de datos.
* Validación contra datos de referencia.

---

# 5. Observaciones

Este documento será actualizado continuamente durante el desarrollo del proyecto.

Cada decisión técnica, arquitectónica, organizacional o relacionada con el uso de Inteligencia Artificial deberá registrarse para mantener un historial claro y verificable de la evolución del sistema.

