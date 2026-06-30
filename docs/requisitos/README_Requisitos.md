# Ingeniería de Requisitos

**Responsable:** Osvaldo Rafael Díaz Castro

## Objetivo

Definir los requisitos funcionales y no funcionales del simulador lunar, así como los actores y casos de uso principales.

## Actores

- Operador del simulador
- Investigador
- Administrador
- Orekit (sistema externo)

## Requisitos Funcionales

- RF-01: Inicializar la configuración de Orekit.
- RF-02: Ejecutar simulaciones orbitales.
- RF-03: Calcular propagación orbital.
- RF-04: Mostrar resultados de simulación.
- RF-05: Validar parámetros antes de ejecutar.
- RF-06: Registrar errores y eventos.

## Requisitos No Funcionales

- RNF-01: Ejecutarse sobre Java 17.
- RNF-02: Utilizar Orekit para cálculos orbitales.
- RNF-03: Mantener código documentado.
- RNF-04: Ser modular y extensible.
- RNF-05: Generar resultados reproducibles.

## Historias de Usuario

### HU-01
Como operador del simulador, quiero iniciar una simulación orbital para analizar el comportamiento de una órbita LEO.

### HU-02
Como investigador, quiero consultar los resultados generados para evaluar la trayectoria calculada.

### HU-03
Como usuario del sistema, quiero que Orekit se configure automáticamente para evitar errores de inicialización.

## Casos de Uso

- Iniciar simulación
- Configurar parámetros orbitales
- Inicializar Orekit
- Ejecutar propagación orbital
- Consultar resultados
- Revisar registros

## Matriz de Trazabilidad

| Requisito | Caso de Uso |
|------------|------------|
| RF-01 | Inicializar Orekit |
| RF-02 | Iniciar simulación |
| RF-03 | Ejecutar propagación orbital |
| RF-04 | Consultar resultados |
| RF-05 | Configurar parámetros |
