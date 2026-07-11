# Informe Técnico del Spike TLI

## Proyecto Simulador de Misión Lunar Artemis II

**Documento:** Informe técnico del prototipo de trayectoria TLI  
**Responsable:** Jesús Polanco  
**Rol:** FDO — Flight Dynamics Officer  
**Versión:** 1.0  
**Estado:** Prototipo funcional  
**Tipo de entrega:** Prueba de concepto académica sin interfaz gráfica  

---

## 1. Propósito del documento

Este documento describe el diseño, funcionamiento, ejecución y resultados del prototipo de trayectoria **TLI (Trans-Lunar Injection)** desarrollado en Java mediante la biblioteca Orekit.

El objetivo principal del Spike es demostrar que el sistema puede:

- Crear una trayectoria terrestre altamente elíptica con alcance aproximado a la región lunar.
- Propagar el estado de una nave durante varios días.
- Calcular la distancia de la nave respecto a la Tierra y a una representación simplificada de la Luna.
- Identificar un punto aproximado de sobrevuelo lunar.
- Inspeccionar una tendencia de retorno hacia la Tierra.
- Ejecutarse completamente desde consola, sin interfaz gráfica.

Este prototipo no representa una solución de calidad de producción ni una reproducción exacta de una trayectoria oficial de Artemis II. Su propósito es validar la viabilidad técnica inicial del módulo de dinámica de vuelo.

---

## 2. Alcance

El Spike cubre las siguientes capacidades:

1. Inicialización del entorno Orekit.
2. Creación de una órbita terrestre altamente elíptica.
3. Propagación kepleriana durante diez días.
4. Representación simplificada del movimiento lunar.
5. Cálculo de distancias Tierra–nave y Luna–nave.
6. Detección del mayor acercamiento lunar.
7. Inspección de una posible geometría de retorno.
8. Presentación de resultados en consola.

Quedan fuera del alcance actual:

- Interfaz gráfica.
- Integración de modelos de fuerza de alta fidelidad.
- Gravedad lunar aplicada directamente sobre la nave.
- Perturbaciones solares.
- Correcciones de trayectoria.
- Optimización real de la maniobra TLI.
- Validación contra efemérides oficiales.
- Certificación de trayectoria de misión.

---

## 3. Conceptos principales

### 3.1 TLI

La **Trans-Lunar Injection** es la maniobra que permite abandonar una órbita cercana a la Tierra e iniciar una trayectoria dirigida hacia la región lunar.

En este Spike, la TLI no se modela como un encendido de motor con duración, empuje y consumo de combustible. En su lugar, se representa mediante una órbita altamente elíptica cuyo perigeo se encuentra cerca de la Tierra y cuyo apogeo alcanza una distancia aproximada a la región lunar.

### 3.2 Sobrevuelo lunar

El sobrevuelo lunar ocurre cuando la nave pasa cerca de la Luna sin entrar necesariamente en órbita lunar ni aterrizar.

El prototipo detecta este evento calculando la distancia entre la posición de la nave y una posición lunar aproximada. El menor valor registrado durante la propagación se considera el punto de mayor acercamiento lunar del Spike.

### 3.3 Retorno libre

Una trayectoria de retorno libre es aquella cuya geometría permite que la nave regrese hacia la Tierra después de pasar por la región lunar, sin depender de una maniobra principal adicional.

En este prototipo, el retorno libre se verifica únicamente mediante inspección de tendencias:

- La nave se aleja de la Tierra.
- La nave se aproxima a la región lunar.
- Después del máximo alejamiento, la distancia a la Tierra comienza a disminuir.

Esta verificación es geométrica y académica; no constituye una validación dinámica completa.

---

## 4. Tecnologías utilizadas

| Tecnología | Uso dentro del Spike |
|---|---|
| Java 17 | Lenguaje principal del prototipo |
| Orekit | Creación y propagación de la órbita |
| Hipparchus | Operaciones matemáticas y vectores tridimensionales |
| Apache Maven | Gestión de dependencias, compilación y ejecución |
| Git | Control de versiones local |
| GitHub | Repositorio remoto del proyecto |
| Visual Studio Code | Edición y revisión del código |
| PowerShell | Ejecución de comandos Maven en Windows 11 |

---

## 5. Estructura técnica

```text
programadores-lunares/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── nasa/
│                   └── simulador/
│                       ├── Main.java
│                       ├── OrekitConfig.java
│                       ├── LEOSimulation.java
│                       └── TLIPrototype.java
│
├── Docs/
│   └── SpikeTLI.md
│
├── pom.xml
└── README.md
```

### 5.1 `Main.java`

Es el punto de entrada del proyecto.

Responsabilidades:

- Mostrar el encabezado general del simulador.
- Inicializar Orekit.
- Ejecutar el Spike TLI.
- Capturar errores generales de ejecución.

### 5.2 `OrekitConfig.java`

Prepara el entorno de Orekit.

Responsabilidades:

- Buscar la carpeta `orekit-data`.
- Cargarla si está disponible.
- Permitir que el programa continúe en modo básico cuando dicha carpeta no existe.
- Evitar inicializaciones repetidas.

### 5.3 `TLIPrototype.java`

Contiene la lógica principal del Spike.

Responsabilidades:

- Definir los parámetros orbitales iniciales.
- Construir la órbita TLI conceptual.
- Crear el propagador.
- Estimar la posición lunar.
- Propagar la nave durante el periodo establecido.
- Calcular distancias.
- Generar el informe de inspección.

### 5.4 `LEOSimulation.java`

Corresponde a la simulación LEO desarrollada en una etapa anterior.

No es el módulo principal del entregable TLI, pero se conserva como referencia y base histórica del proyecto.

---

## 6. Flujo de ejecución

```text
Inicio
  │
  ▼
Main.java
  │
  ▼
OrekitConfig.init()
  │
  ├── Si existe orekit-data: cargar datos
  │
  └── Si no existe: continuar en modo básico
  │
  ▼
TLIPrototype.run()
  │
  ▼
Crear fecha y marco inercial
  │
  ▼
Construir órbita altamente elíptica
  │
  ▼
Crear propagador kepleriano
  │
  ▼
Propagar durante 10 días
  │
  ▼
Calcular distancias Tierra–nave y Luna–nave
  │
  ▼
Identificar mayor acercamiento lunar
  │
  ▼
Inspeccionar tendencia de retorno
  │
  ▼
Mostrar informe final
```

---

## 7. Configuración orbital del Spike

| Parámetro | Valor |
|---|---:|
| Radio medio terrestre usado | 6,378.137 km |
| Altitud de perigeo | 300 km |
| Distancia de apogeo | 405,000 km |
| Distancia lunar media simplificada | 384,400 km |
| Inclinación | 5° |
| Duración de propagación | 10 días |
| Intervalo de impresión | 12 horas |
| Marco de referencia | EME2000 |
| Escala temporal | TAI |
| Modelo de propagación | Kepleriano |
| Parámetro gravitacional terrestre | EGM96_EARTH_MU |

### 7.1 Cálculo del radio de perigeo

```text
radio de perigeo = radio terrestre + altitud de perigeo
```

### 7.2 Cálculo del semieje mayor

```text
semieje mayor = (radio de perigeo + radio de apogeo) / 2
```

### 7.3 Cálculo de la excentricidad

```text
excentricidad =
(radio de apogeo - radio de perigeo)
/
(radio de apogeo + radio de perigeo)
```

---

## 8. Modelo lunar simplificado

La Luna se representa mediante un movimiento circular uniforme alrededor de la Tierra.

El modelo utiliza:

- Una distancia media constante de 384,400 km.
- Un periodo orbital aproximado de 27.321661 días.
- Movimiento en el plano XY.
- Una fase inicial ajustada para favorecer el encuentro cercano entre la nave y la región lunar.

La posición aproximada se calcula con:

```text
x = R · cos(θ)
y = R · sin(θ)
z = 0
```

Este modelo es suficiente para una prueba de concepto, pero no sustituye efemérides reales ni modelos gravitacionales de alta fidelidad.

---

## 9. Criterios de inspección

### 9.1 Salida de la región terrestre

Se considera que la nave abandona la región terrestre cuando supera una distancia de 100,000 km desde el centro de la Tierra.

### 9.2 Llegada a la región de sobrevuelo lunar

Se considera que la nave alcanza la región de sobrevuelo cuando su distancia aproximada a la Luna es menor de 50,000 km.

### 9.3 Tendencia de retorno

Se considera que existe tendencia de retorno cuando:

1. La nave ya se ha alejado significativamente de la Tierra.
2. La distancia a la Tierra comienza a disminuir respecto al paso anterior.

Si las tres condiciones se cumplen, el programa informa que la geometría de retorno es visible mediante inspección.

---

## 10. Resultados obtenidos

La ejecución del Spike finalizó correctamente con `BUILD SUCCESS`.

| Resultado | Valor |
|---|---:|
| Mayor acercamiento lunar | 22,445.02 km |
| Tiempo del mayor acercamiento | 132 horas |
| Distancia máxima a la Tierra | 404,870.37 km |
| Distancia final a la Tierra | 170,955.32 km |
| Duración total propagada | 240 horas |

### 10.1 Interpretación

Los resultados muestran el siguiente comportamiento:

1. La nave comienza a aproximadamente 6,678 km del centro terrestre.
2. La distancia a la Tierra aumenta progresivamente.
3. La nave alcanza una distancia máxima cercana a 404,870 km.
4. La distancia a la Luna disminuye hasta aproximadamente 22,445 km.
5. Después del punto de máximo alejamiento terrestre, la distancia a la Tierra comienza a reducirse.
6. Al finalizar la propagación, la nave se encuentra a aproximadamente 170,955 km de la Tierra.

```text
Tierra → salida translunar → sobrevuelo lunar → tendencia de retorno
```

### 10.2 Estado de verificación

```text
[OK] La nave abandona la región terrestre.
[OK] La trayectoria alcanza la región de sobrevuelo lunar.
[OK] La nave presenta una tendencia de regreso a la Tierra.

[RESULTADO] Geometría de retorno visible por inspección.
[SUCCESS] Spike TLI completado.
[INFO] BUILD SUCCESS
```

---

## 11. Procedimiento de compilación y ejecución

### 11.1 Requisitos

- Windows 11
- Java JDK 17
- Apache Maven 3.9.16
- Acceso al repositorio del proyecto

### 11.2 Compilar

```powershell
& "C:\Users\jesus\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" clean compile
```

### 11.3 Ejecutar

```powershell
& "C:\Users\jesus\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" exec:java
```

### 11.4 Nota sobre `orekit-data`

La ejecución actual puede continuar sin la carpeta `orekit-data` porque el Spike utiliza TAI, EME2000 y propagación kepleriana básica.

---

## 12. Verificación técnica

| Prueba | Resultado |
|---|---|
| Maven reconoce el proyecto | Aprobada |
| Compilación con Java 17 | Aprobada |
| Descarga de dependencias Orekit | Aprobada |
| Creación de clases compiladas | Aprobada |
| Ejecución de `Main.java` | Aprobada |
| Inicialización de Orekit | Aprobada en modo básico |
| Propagación TLI | Aprobada |
| Cálculo de distancias | Aprobada |
| Detección de sobrevuelo | Aprobada |
| Inspección de retorno | Aprobada |
| Finalización con `BUILD SUCCESS` | Aprobada |

---

## 13. Limitaciones conocidas

1. La Luna no se obtiene desde efemérides reales.
2. La gravedad lunar no modifica directamente la trayectoria de la nave.
3. No se modela la gravedad solar.
4. No se incluyen perturbaciones terrestres de orden superior.
5. La maniobra TLI se representa mediante condiciones orbitales iniciales.
6. No se calcula consumo de combustible.
7. No se aplican correcciones de trayectoria.
8. No se modela masa variable.
9. La verificación del retorno es visual y numérica.
10. La simulación no debe utilizarse para decisiones operacionales reales.

---

## 14. Riesgos técnicos

| Riesgo | Impacto | Mitigación |
|---|---|---|
| Confundir el Spike con una trayectoria real | Alto | Declarar claramente el carácter conceptual |
| Ausencia de `orekit-data` | Medio | Mantener modo básico con TAI |
| Parámetros orbitales mal ajustados | Alto | Revisar distancias y tendencias |
| Simplificación excesiva de la Luna | Alto | Incorporar efemérides en versiones futuras |
| Dependencia de versiones Java/Orekit | Medio | Mantener Java 17 y Maven configurados |
| Errores de estructura de carpetas | Medio | Usar el diseño estándar de Maven |

---

## 15. Mejoras futuras

- Efemérides reales de la Luna.
- Propagación numérica.
- Atracción gravitacional de la Tierra, Luna y Sol.
- Maniobra TLI basada en incremento de velocidad.
- Masa inicial y consumo de combustible.
- Detección automática de periapsis lunar.
- Cálculo de condiciones de reentrada terrestre.
- Exportación a CSV.
- Gráficos de distancia respecto al tiempo.
- Representación 2D y 3D.
- Interfaz gráfica.
- Pruebas unitarias y de integración.

---

## 16. Conclusión

El Spike TLI fue implementado y ejecutado correctamente como prueba de concepto del módulo de dinámica de vuelo.

La simulación demostró que el sistema puede:

- Propagar una trayectoria altamente elíptica.
- Alcanzar la región lunar aproximada.
- Detectar un sobrevuelo conceptual.
- Identificar una tendencia posterior de regreso hacia la Tierra.
- Presentar resultados claros desde consola.
- Completar la ejecución con `BUILD SUCCESS`.

El mayor acercamiento lunar obtenido fue de **22,445.02 km** a las **132 horas**, mientras que la distancia máxima respecto a la Tierra fue de **404,870.37 km**.

Estos resultados cumplen el objetivo académico del Spike. Debido a las simplificaciones realizadas, la trayectoria debe considerarse una demostración conceptual y no una simulación operacional de misión.

---

## 17. Declaración de fidelidad del modelo

La propagación de la nave se realiza mediante un modelo kepleriano centrado en la Tierra y la Luna se representa mediante una órbita circular simplificada.

La geometría de retorno se verifica por inspección de distancias y tendencias, no mediante una solución completa del problema Tierra–Luna–nave.

Por tanto, el resultado demuestra viabilidad técnica inicial, pero no precisión de navegación espacial real.