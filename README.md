## Ejercicio

### Especificaciones
En `src/main/resources` se encuentra un archivo llamado `Especificicacion.pdf` con información de arquitectura utilizada.

## Setup

### Instrucciones
Para compilar y ejecutar proyecto es necesario contar con la version 1.8 de la JDK y Maven para la gestion de las dependencias.

Para la BDD basta con tener docker y ejecutar: ```docker-compose -f "src/main/docker/docker-compose.yml" up```

Se puede correr el proyecto con el comando de maven ```spring-boot:run``` o simplemente con el plugin de springboot de los IDE.

Una vez levantada la aplicacion se puede realizar invocaciones a la API.

El puerto por defecto de la API es 8080.

### API (Nivel 2 y 3)

La URL para invocar la api es:

(POST) https://mercadolibre.ndamelio.com/mutant
```
  {"dna":["AAAAGA", "CAGGGC", "TTATGT", "AGAAGG", "GGGGTA", "TCACTG"]}
```

(GET) https://mercadolibre.ndamelio.com/stats

En el application-prod estan las creds para acceder a la BDD cloud.