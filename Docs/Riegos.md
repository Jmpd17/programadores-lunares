# Registro de Riesgos
## Proyecto Simulador de Misión Lunar - Artemis II

---

## Información General

- Proyecto: Simulador de misión lunar
- Equipo: Equipo 4
- Misión base: Artemis II
- Entregable: Primera fase

---

## Objetivo

Este documento se realiza para identificar algunos riesgos que podrían afectar el desarrollo del proyecto durante las primeras etapas de implementación y pruebas del simulador orbital.

---

# Riesgos identificados

| ID     | Riesgo | Probabilidad | Impacto | Posible solución |
|--------|---|---|---|---|
| R-01   | Problemas instalando Java o Maven | Alta | Alta | Verificar instalación y versiones antes de iniciar |
| R-02   | Errores configurando Orekit | Alta | Alta | Revisar documentación y ejemplos básicos |
| R-03   | Fallos cargando la carpeta `orekit-data` | Media | Alta | Confirmar que la carpeta esté en la ruta correcta |
| R-04   | Falta de experiencia usando Orekit | Alta | Media | Investigar tutoriales y realizar pruebas simples |
| R-05   | Conflictos al trabajar en GitHub | Media | Media | Mantener organización de ramas y commits |
| R-06   | Errores de compilación en Java | Media | Media | Revisar imports y versiones del JDK |
| R-07   | Retrasos en tareas del equipo | Media | Alta | Dividir responsabilidades correctamente |
| R-08   | Problemas integrando módulos | Baja | Alta | Mantener estructura organizada del proyecto |
| R-09   | Dependencia de internet o documentación externa | Media | Baja | Guardar ejemplos y referencias importantes |
| R-10   | Resultados incorrectos en simulaciones | Media | Alta | Revisar parámetros orbitales y validaciones |

---

# Riesgos técnicos principales

## Configuración de Orekit
Orekit necesita varios archivos y configuraciones para funcionar correctamente. Si algo falta o está mal configurado, la simulación no se ejecutará.

---

## Compatibilidad de Java
Dependiendo de la versión de Java pueden aparecer errores de compilación o incompatibilidad con algunas funciones utilizadas.

---

## Simulación orbital
La propagación orbital requiere parámetros matemáticos específicos. Un error pequeño puede afectar los resultados obtenidos.

---

# Medidas para reducir riesgos

- Realizar pruebas pequeñas antes de implementar cambios grandes.
- Mantener el proyecto organizado.
- Hacer commits frecuentes en GitHub.
- Leer documentación oficial y ejemplos.
- Probar cada módulo individualmente.

---

# Estado actual

| Área | Estado |
|---|---|
| Instalación de Java | En progreso |
| Configuración Orekit | En progreso |
| Simulación LEO | En desarrollo |
| GitHub | Configurado |
| README | Terminado |

---

# Observación

Este documento puede cambiar durante el desarrollo del proyecto dependiendo de los problemas o riesgos nuevos que aparezcan más adelante.