# Flight Dynamics Technical Report

## Artemis II Mission Simulator

### Responsable

Jesús Polanco — Flight Dynamics Officer (FDO)

---

# 1. Objetivo

Este documento describe la implementación técnica de la simulación orbital inicial utilizada para validar la capacidad del sistema de modelar trayectorias espaciales mediante la librería Orekit.

La simulación constituye la primera prueba funcional de dinámica orbital del proyecto y servirá como base para futuras simulaciones relacionadas con la misión Artemis II.

---

# 2. Descripción General

La simulación consiste en la propagación de una órbita terrestre baja (LEO) utilizando un modelo orbital kepleriano.

El sistema calcula la posición del vehículo espacial a intervalos regulares de tiempo y presenta los resultados en coordenadas cartesianas tridimensionales.

---

# 3. Componentes Utilizados

* Java JDK 17
* Apache Maven
* Orekit
* Hipparchus
* Visual Studio Code
* GitHub

---

# 4. Parámetros Orbitales

| Parámetro               | Valor          |
| ----------------------- | -------------- |
| Semi-major Axis         | 7,000,000 m    |
| Eccentricity            | 0.001          |
| Inclination             | 98°            |
| Frame                   | EME2000        |
| Gravitational Parameter | EGM96_EARTH_MU |

---

# 5. Flujo de Simulación

1. Inicialización de Orekit.
2. Definición de fecha inicial.
3. Creación de órbita kepleriana.
4. Inicialización del propagador.
5. Propagación temporal.
6. Obtención de posición orbital.
7. Presentación de resultados.

---

# 6. Resultados Esperados

La simulación debe generar posiciones espaciales para distintos instantes de tiempo permitiendo validar la propagación orbital del sistema.

---

# 7. Próximas Etapas

* Simulación translunar.
* Maniobras orbitales.
* Navegación lunar.
* Visualización gráfica de trayectorias.
