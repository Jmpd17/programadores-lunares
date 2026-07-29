# Justificación del Diseño de Pruebas

**Proyecto:** Programadores Lunares – Simulador Mínimo Viable (MVS)

**Asignatura:** Ingeniería de Software I (INF-272)

**Entregable:** #5 – Artefactos de Prueba

**Equipo:** Tripulación 4

**Responsable:** Osvaldo Díaz

---

# 1. Estrategia de Pruebas

El propósito de este documento es justificar el diseño de las pruebas implementadas para verificar el correcto funcionamiento del Simulador Mínimo Viable (MVS).

La estrategia de pruebas adoptada divide la validación del sistema en pruebas unitarias y pruebas de integración.

Las pruebas unitarias verifican de manera aislada el comportamiento de los módulos relacionados con la mecánica orbital, la propagación de la trayectoria y los modelos de la interfaz de usuario. Estas pruebas fueron implementadas utilizando **JUnit 5** y **Mockito**, permitiendo validar la lógica del sistema de forma independiente y reproducible.

Las pruebas de integración verifican el funcionamiento conjunto de todos los módulos del simulador, comprobando que la información producida por el motor físico sea correctamente utilizada por la interfaz y que el flujo completo de la simulación funcione como fue diseñado.

Esta estrategia facilita la detección temprana de errores, mejora la mantenibilidad del proyecto y garantiza que cada requisito funcional pueda ser validado mediante pruebas automatizadas.

---

# 2. Matriz de Trazabilidad Prueba – Requisito

| Prueba | Requisito | Descripción | Tipo |
|--------|-----------|-------------|------|
| testOrekitInitialization() | OAM-1 | Verifica la correcta inicialización del contexto de Orekit y el registro de las fuentes de datos. | Unitaria |
| testParkingOrbit() | OAM-2 | Comprueba que la órbita inicial sea circular y tenga aproximadamente 185 km de altitud. | Unitaria |
| testForceModels() | OAM-3 | Verifica que el propagador contenga los tres modelos de fuerza requeridos. | Unitaria |
| testTLIManeuver() | OAM-4 | Comprueba que la maniobra TLI aplique correctamente el delta-v configurado. | Unitaria |
| testTrajectoryGeneration() | OAM-5 | Verifica que la trayectoria generada contenga al menos 500 puntos. | Unitaria |
| testLunarPeriapsisDetection() | OAM-6 | Comprueba la detección del periapsis durante el sobrevuelo lunar. | Unitaria |
| testReentryDetection() | OAM-7 | Verifica la detección de la interfaz de reentrada alrededor de 120 km. | Unitaria |
| testTelemetryModel() | UI-2 | Comprueba el cálculo correcto de la telemetría de la nave. | Unitaria |
| testParameterValidation() | UI-3 | Verifica la validación de parámetros de entrada y valores límite. | Unitaria |
| testTrajectoryChange() | UI-4 | Comprueba que diferentes parámetros produzcan trayectorias distintas. | Unitaria |
| CompleteMissionIT | Integración | Ejecuta la simulación completa desde la órbita inicial hasta la reentrada. | Integración |

La matriz de trazabilidad permite identificar claramente qué prueba verifica cada requisito funcional definido para el proyecto, garantizando la cobertura de los requisitos establecidos.

---

# 3. Manejo de la Naturaleza Numérica

La simulación orbital utiliza cálculos numéricos de precisión flotante, por lo que pequeñas variaciones entre los resultados esperados y los obtenidos son normales.

Por esta razón, las pruebas utilizan tolerancias numéricas mediante aserciones con margen de error (`assertEquals` con delta), evitando comparaciones de igualdad exacta que podrían generar falsos errores.

Asimismo, se emplean comprobaciones de cordura (*sanity checks*) para verificar que los resultados permanezcan dentro de rangos físicamente aceptables, como la altitud de la órbita, la detección del periapsis lunar y la interfaz de reentrada.

---

# 4. Qué No se Prueba y Por Qué

Las pruebas desarrolladas se enfocan en validar el código implementado por el equipo.

No se prueban los algoritmos internos de Orekit debido a que corresponden a una biblioteca externa ampliamente validada.

Del mismo modo, el renderizado gráfico desarrollado mediante JavaFX no forma parte de las pruebas unitarias automatizadas, ya que su validación corresponde a pruebas de aceptación realizadas por el usuario.

---

# 5. Datos y Accesorios de Prueba

Durante la ejecución de las pruebas se utilizaron los siguientes elementos:

- Contexto de datos de Orekit.
- Parámetros orbitales de la misión.
- Estados iniciales de la nave.
- Valores de delta-v para la maniobra TLI.
- Objetos simulados (Mocks) utilizando Mockito.
- Fechas y épocas empleadas por el propagador orbital.

Estos datos permiten ejecutar las pruebas de forma consistente y reproducible.

---

# 6. Resumen de Cobertura

Las pruebas fueron ejecutadas utilizando Maven junto con JUnit 5, Mockito, Surefire y JaCoCo.

El informe de cobertura generado mediante JaCoCo evidencia el nivel de cobertura alcanzado por las pruebas implementadas. Las áreas con mayor cobertura corresponden a la lógica de mecánica orbital, propagación y validación de parámetros, mientras que los componentes gráficos asociados a JavaFX presentan una cobertura menor debido a que son evaluados mediante pruebas de aceptación.

> **Nota:** Actualizar este apartado con el porcentaje final de cobertura obtenido en JaCoCo antes de la entrega.

---

# 7. Conclusión

La estrategia de pruebas implementada proporciona evidencia de que los requisitos funcionales del Simulador Mínimo Viable han sido verificados mediante pruebas unitarias e integración.

La matriz de trazabilidad garantiza la relación entre los requisitos y las pruebas desarrolladas, mientras que la utilización de tolerancias numéricas permite validar correctamente los resultados de una simulación física sin depender de comparaciones exactas.

Este documento constituye el artefacto de justificación del diseño de pruebas correspondiente al Entregable #5.
