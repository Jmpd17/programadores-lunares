# Arquitectura del Módulo de Simulación Orbital

## Proyecto Simulador de Misión Lunar Artemis II

**Responsable:** Jesús Polanco  
**Rol:** FDO - Flight Dynamics Officer  

---

## 1. Introducción

Este documento describe la arquitectura del módulo de simulación orbital del proyecto Simulador de Misión Lunar Artemis II.

El objetivo de este módulo es ejecutar una simulación básica de una órbita terrestre baja, conocida como LEO, utilizando la biblioteca Orekit. Esta simulación permite validar que el sistema puede calcular posiciones orbitales y mostrar resultados en consola.

---

## 2. Objetivo del Módulo

El módulo de simulación orbital tiene como objetivo principal calcular el movimiento de una nave o satélite alrededor de la Tierra durante un tiempo determinado.

En esta etapa del proyecto, la simulación se enfoca en una órbita LEO simple. Esto sirve como base para futuras simulaciones más avanzadas relacionadas con trayectorias lunares.

---

## 3. Componentes Principales

El módulo de simulación está formado por tres archivos principales:

### Main.java

Es el punto de entrada del programa.

Sus funciones principales son:

- Iniciar la aplicación.
- Mostrar mensajes iniciales en consola.
- Llamar la configuración de Orekit.
- Ejecutar la simulación orbital.

---

### OrekitConfig.java

Es el componente encargado de preparar Orekit antes de ejecutar la simulación.

Sus funciones principales son:

- Cargar la carpeta `orekit-data`.
- Configurar los datos necesarios para Orekit.
- Validar que el entorno esté listo para simular.

---

### LEOSimulation.java

Es el componente principal del módulo de simulación.

Sus funciones principales son:

- Definir los parámetros de la órbita LEO.
- Crear la órbita inicial.
- Ejecutar la propagación orbital.
- Calcular posiciones en diferentes tiempos.
- Mostrar resultados en consola.

---

## 4. Flujo de Ejecución

El funcionamiento del módulo sigue este flujo:

```text
Usuario
   ↓
Main.java
   ↓
OrekitConfig.java
   ↓
LEOSimulation.java
   ↓
Resultados en consola