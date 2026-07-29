# Justificación del diseño de pruebas — FDO

**Proyecto:** Simulador de Misión Lunar Artemis II  
**Rol responsable:** FDO — Flight Dynamics Officer  
**Alcance:** Mecánica orbital, propagación numérica, maniobra TLI, trayectoria y eventos físicos.

## 1. Estrategia de pruebas

La estrategia separa las pruebas unitarias de las pruebas de integración. Las pruebas unitarias verifican clases concretas y reglas físicas aisladas, mientras que la prueba de integración ejecuta la canalización principal del motor orbital y valida el contrato del resultado.

Se automatizaron las verificaciones que pueden expresarse mediante resultados numéricos, tipos de modelos instalados, cantidades de puntos, continuidad temporal y activación de detectores. El renderizado JavaFX no forma parte del alcance del FDO.

## 2. Matriz de trazabilidad

| Requisito | Clase de prueba | Evidencia verificada |
|---|---|---|
| OAM-1 | `OrekitConfigTest` | Proveedor registrado y datos cargados sin excepciones. |
| OAM-2 | `ParkingOrbitFactoryTest` | Órbita circular, altitud aproximada de 185 km e inclinación configurada. |
| OAM-3 | `ForceModelFactoryTest` | Gravedad terrestre 8×8 y atracciones de la Luna y el Sol. |
| OAM-4 | `TLIManeuverFactoryTest`, `TLIParametersTest` | Magnitud, dirección, normalización y fecha de encendido TLI. |
| OAM-5 | `TrajectoryCollectorTest`, `ArtemisMissionSimulationIntegrationTest` | Mínimo 500 puntos y marcas temporales crecientes. |
| OAM-6 | `MissionEventsTest` | Registro de un acercamiento lunar posterior a la TLI y cálculo de su altitud. |
| OAM-7 | `MissionEventsTest` | Cruce descendente de la interfaz de reentrada a aproximadamente 120 km. |

## 3. Manejo de la naturaleza numérica

No se utiliza igualdad exacta para magnitudes orbitales calculadas. Los resultados dependen de integradores adaptativos, marcos de referencia, modelos geopotenciales y transformaciones geodésicas. Por esa razón se usan tolerancias explícitas.

Ejemplos:

- Altitud de estacionamiento: tolerancia de 1 metro.
- Excentricidad circular: tolerancia de `1.0e-12`.
- Interfaz de reentrada: tolerancia de 2 kilómetros en el escenario controlado.
- Magnitud del delta-v almacenado: tolerancia numérica pequeña porque es una conversión directa.

También se aplican comprobaciones de cordura: valores finitos, distancias positivas, tiempo estrictamente creciente y alcance superior a 300 000 km en la simulación translunar.

## 4. Qué no se prueba y por qué

No se prueban los algoritmos internos de Orekit, Hipparchus ni JavaFX. Se consideran bibliotecas externas de confianza. Las pruebas verifican la configuración y el uso que hace el proyecto de esas bibliotecas.

Tampoco se verifica mediante JUnit la posición visual de la nave, el rastro, los colores o las animaciones. Esos aspectos pertenecen a las pruebas de aceptación de usuario.

## 5. Datos y accesorios de prueba

- Fecha base: 1 de enero de 2026, 00:00 UTC.
- Altitud de estacionamiento: 185 km.
- Inclinación: 28.5 grados.
- Delta-v TLI de referencia: 3.15 km/s.
- Impulso específico: 450 s.
- Paso de muestreo: 300 s.
- Datos externos: carpeta `orekit-data` incluida en el proyecto.
- Mockito: utilizado para simular estados físicos en la prueba aislada del periapsis lunar y para verificar el contrato de `MissionSimulationResult`.

## 6. Cobertura

JaCoCo se configura para medir el núcleo FDO. Se excluyen la vista JavaFX, clases de demostración y utilidades exploratorias que no forman parte del motor físico entregado por el FDO.

El porcentaje final debe copiarse desde:

`target/site/jacoco/index.html`

**Cobertura obtenida:** pendiente de completar después de ejecutar `mvn clean test`.

## 7. Observación pendiente de aceptación física

Las pruebas unitarias OAM-6 y OAM-7 usan escenarios controlados para verificar los detectores. La aceptación final de la misión requiere confirmar que la trayectoria nominal integrada produzca tanto un sobrevuelo lunar válido como una reentrada real. Esa comprobación debe realizarse después de ejecutar la suite con los parámetros nominales definitivos del equipo.
