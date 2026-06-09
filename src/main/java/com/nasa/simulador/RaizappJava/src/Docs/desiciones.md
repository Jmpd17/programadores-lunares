# Decisiones Técnicas — Flight Dynamics

## Selección de Orekit

Se decidió utilizar Orekit como biblioteca principal para cálculos orbitales debido a su amplio uso en aplicaciones aeroespaciales y su capacidad para modelar dinámicas orbitales complejas.

---

## Simulación Inicial LEO
c
Como primera validación técnica se seleccionó una órbita terrestre baja (LEO), ya que permite comprobar el funcionamiento de la cadena de herramientas antes de avanzar hacia simulaciones más complejas relacionadas con Artemis II....

---

## Arquitectura Modular

La implementación se dividió en módulos independientes:

* Main.java
* OrekitConfig.java
* LEOSimulation.java

Esta separación facilita el mantenimiento, las pruebas y futuras ampliaciones.

---

## Uso de Inteligencia Artificial

La IA fue utilizada como herramienta de apoyo para:

* Investigación preliminar.
* Comprensión de Orekit.
* Organización de documentación.
* Refinamiento de requisitos técnicos.

Todo el contenido generado fue revisado, validado y adaptado manualmente antes de incorporarse al proyecto.

---

## Riesgos Técnicos Identificados

* Problemas de compatibilidad entre versiones de Java y Orekit.
* Configuración incorrecta de orekit-data.
* Errores de propagación orbital.
* Dependencia de documentación especializada.

Se establecieron pruebas progresivas para reducir el impacto de estos riesgos.
