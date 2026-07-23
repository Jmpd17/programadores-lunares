# Diseño de Pruebas — Entregable 5

## 1. Información general

**Proyecto:** Simulador de Misión Lunar Artemis II  
**Módulo:** Motor de dinámica orbital  
**Responsable:** FDO  
**Tecnologías:** Java 17, Maven, JUnit 5, Mockito, Orekit, JaCoCo y Maven Surefire.

## 2. Objetivo

Verificar mediante pruebas unitarias y de integración que el motor orbital inicialice Orekit, construya la órbita de estacionamiento, configure los modelos de fuerza, aplique la maniobra TLI, genere una trayectoria válida, detecte el periapsis lunar y registre la reentrada terrestre a 120 km.

## 3. Trazabilidad de requisitos

| Requisito | Validación | Prueba |
|---|---|---|
| OAM-1 | Inicialización y carga de datos de Orekit | `OrekitConfigTest` |
| OAM-2 | Órbita de estacionamiento a 185 km | `ParkingOrbitFactoryTest` |
| OAM-3 | Gravedad terrestre, lunar y solar | `ForceModelFactoryTest` |
| OAM-4 | Magnitud, dirección y fecha de la TLI | `TLIParametersTest`, `TLIManeuverFactoryTest` |
| OAM-5 | Mínimo 500 puntos y fechas crecientes | `TrajectoryCollectorTest`, `TrajectoryPointTest`, prueba de integración |
| OAM-6 | Periapsis lunar válido | `MissionEventsTest`, prueba de integración |
| OAM-7 | Reentrada descendente a 120 km | `MissionEventsTest`, prueba de integración |

## 4. Inventario de pruebas

1. `OrekitConfigTest`
2. `ParkingOrbitFactoryTest`
3. `ForceModelFactoryTest`
4. `TLIParametersTest`
5. `TLIManeuverFactoryTest`
6. `TrajectoryPointTest`
7. `TrajectoryCollectorTest`
8. `MissionSimulationResultTest`
9. `MissionEventsTest`
10. `ArtemisMissionSimulationIntegrationTest`

## 5. Prueba de integración

La prueba de integración ejecuta la canalización completa:

`Órbita inicial → Propagador numérico → Modelos gravitacionales → Maniobra TLI → Trayectoria translunar → Periapsis lunar → Regreso terrestre → Reentrada`

Parámetros validados:

| Parámetro | Valor |
|---|---:|
| Altitud inicial | 185 km |
| Delta-v TLI | 3.175 km/s |
| Momento de encendido | 1.224883 horas |
| Dirección VNC | `(1.0, 0.0, 0.0)` |
| Impulso específico | 450 s |
| Intervalo de muestreo | 300 s |
| Duración máxima | 16 días |
| Interfaz de reentrada | 120 km |

La ejecución produjo 4,377 puntos de trayectoria, registró un periapsis lunar válido y detectó la reentrada terrestre a 120 km.

## 6. Resultados

```text
Tests run: 27
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

JaCoCo analizó 15 clases y confirmó que se cumplieron todas las reglas de cobertura configuradas.

## 7. Comandos

En este equipo Maven se ejecuta con la ruta completa:

```powershell
& "C:\Users\jesus\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" clean test
& "C:\Users\jesus\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" surefire-report:report-only
```

## 8. Evidencias

- `target/surefire-reports/`
- `target/reports/surefire.html`
- `target/site/jacoco/index.html`
- `target/jacoco.exec`

## 9. Criterios de aceptación

El módulo se considera aprobado porque compila correctamente, las 27 pruebas terminan sin fallos, la integración genera más de 500 puntos, detecta el periapsis lunar, registra la reentrada a 120 km y cumple la cobertura mínima de JaCoCo.

## 10. Conclusión

Las pruebas demuestran que el módulo FDO funciona de forma correcta, repetible y verificable. La simulación completa abandona la órbita terrestre, realiza un sobrevuelo lunar válido y regresa hasta cruzar la interfaz de reentrada.
