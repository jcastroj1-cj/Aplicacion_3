# Inventario API (arquitectura hexagonal)

API REST en Spring Boot 3 / Java 17 / PostgreSQL para el inventario de computadores.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | /health | Comprobar que el servicio responde |
| GET | /usuarios | Listar usuarios |
| GET | /usuarios/{uid} | Obtener un usuario |
| POST | /usuarios | Crear usuario `{ "id", "nombre", "correo" }` |
| GET | /usuarios/{uid}/computadores | Listar computadores del usuario |
| POST | /usuarios/{uid}/computadores | Crear computador |
| PUT | /usuarios/{uid}/computadores/{id} | Actualizar computador |
| DELETE | /usuarios/{uid}/computadores/{id} | Eliminar computador |

Ejemplo de cuerpo para crear un computador (`codigo` es el campo "Id" de la app):

```json
{
  "codigo": "PC-001",
  "marca": "Lenovo",
  "categoria": "Portatil",
  "marcaCpu": "Intel",
  "velocidadCpu": "2.4 GHz",
  "tecnologiaRam": "DDR4",
  "capacidadRam": "8 GB",
  "capacidadDisco": "512 GB",
  "tecnologiaDisco": "SSD",
  "numPuertosUsb": "3",
  "numPuertosHdmi": "1",
  "marcaMonitor": "LG",
  "pulgadas": "15.6",
  "precio": "2500000",
  "latitud": 9.2415,
  "longitud": -75.3480,
  "luz": 120.5
}
```

Solo `codigo` es obligatorio. `latitud`/`longitud` vienen del GPS y `luz` del sensor de luz del celular.

`fecha` es opcional: si no se envía, el servidor pone la fecha y hora actuales.

## Documentación interactiva (Swagger UI)

Con la aplicación en marcha, la dirección base (`/`) redirige a Swagger UI. También puedes abrir `/swagger-ui/index.html` (por ejemplo `https://aplicacion-u2.onrender.com/swagger-ui.html`).
La especificación OpenAPI en JSON está en `/v3/api-docs`.

## Ejecutar en local

1. Tener PostgreSQL con una base llamada `inventario`. Con Docker:
   ```
   docker run --name pg -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=inventario -p 5432:5432 -d postgres:16
   ```
2. `mvn spring-boot:run`
3. Probar: `GET http://localhost:8080/health`

## Variables de entorno

| Variable | Ejemplo |
|---|---|
| SPRING_DATASOURCE_URL | jdbc:postgresql://HOST:5432/NOMBRE_BD |
| SPRING_DATASOURCE_USERNAME | usuario de la base |
| SPRING_DATASOURCE_PASSWORD | contraseña de la base |
| PORT | lo define Render automáticamente |

La URL debe empezar por `jdbc:postgresql://` (no `postgres://`).

## Desplegar en Render

1. Subir el proyecto a GitHub.
2. En Render: New → Web Service → elegir el repositorio, runtime **Docker**.
3. Añadir las tres variables de entorno de la tabla.
4. Dejar activado el despliegue automático sobre la rama base.

## Estructura

- `domain`: modelos (sin dependencias de frameworks).
- `application`: puertos de entrada (casos de uso), puertos de salida (repositorios) y servicios.
- `infrastructure`: adaptadores. `in/web` (controladores REST) y `out/persistence` (JPA).
- `infrastructure/config/BeanConfig`: conecta los servicios con los adaptadores.
