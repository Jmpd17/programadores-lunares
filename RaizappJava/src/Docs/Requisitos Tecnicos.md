# Technical Requirements Specification

## Flight Dynamics Module

### Responsable

Jesús Polanco — Flight Dynamics Officer (FDO)

---

# Requisitos Funcionales

### FDO-SIM-001

El sistema deberá permitir la creación de una órbita terrestre baja utilizando parámetros orbitales configurables.

### FDO-SIM-002

El sistema deberá propagar la órbita utilizando la librería Orekit.

### FDO-SIM-003

El sistema deberá calcular posiciones orbitales en función del tiempo.

### FDO-SIM-004

El sistema deberá mostrar información de posición en los ejes X, Y y Z.

### FDO-SIM-005

El sistema deberá permitir futuras extensiones para simulaciones lunares.

---

# Requisitos No Funcionales

### FDO-NFR-001

El sistema deberá desarrollarse utilizando Java.

### FDO-NFR-002

El sistema deberá utilizar Maven para la gestión de dependencias.

### FDO-NFR-003

La arquitectura deberá permitir escalabilidad para futuras misiones.

### FDO-NFR-004

La simulación deberá ejecutarse sin errores críticos.

### FDO-NFR-005

El código deberá mantenerse documentado y modular.

---

# Restricciones Técnicas

* Uso obligatorio de Orekit.
* Compatibilidad con Java JDK 17.
* Control de versiones mediante GitHub.
* Referencia técnica basada en Artemis II.
