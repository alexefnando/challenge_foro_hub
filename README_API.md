# API ForoHub - Registro de Tópicos

## Descripción

API REST para el registro de tópicos en un foro educativo, desarrollada con Spring Boot.

## Características Implementadas

- ✅ Endpoint POST `/topicos` para registro de nuevos tópicos
- ✅ Endpoint GET `/topicos` para listado de tópicos con paginación
- ✅ Endpoint GET `/topicos/{id}` para obtener detalle de un tópico específico
- ✅ Endpoint PUT `/topicos/{id}` para actualizar un tópico existente
- ✅ Endpoint DELETE `/topicos/{id}` para eliminar un tópico específico
- ✅ Endpoint GET `/topicos/primeros10` para primeros 10 tópicos ordenados por fecha ASC
- ✅ Búsqueda por nombre de curso y año específico
- ✅ Paginación usando `@PageableDefault`
- ✅ Validación de todos los campos obligatorios usando `@Valid`
- ✅ Prevención de tópicos duplicados (mismo título y mensaje)
- ✅ Persistencia en base de datos MySQL usando JPA
- ✅ Manejo global de excepciones y errores

## Estructura del Proyecto

```
src/main/java/com/example/demo/
├── controller/
│   ├── TopicoController.java    # Endpoint principal para tópicos
│   └── TestController.java      # Endpoints para testing
├── dto/
│   ├── TopicoRequestDTO.java    # DTO para peticiones
│   └── TopicoResponseDTO.java   # DTO para respuestas
├── exception/
│   └── GlobalExceptionHandler.java  # Manejo global de excepciones
├── model/
│   ├── Topico.java             # Entidad principal
│   ├── Usuario.java            # Entidad de usuario
│   ├── Curso.java              # Entidad de curso
│   ├── Perfil.java             # Entidad de perfil
│   ├── Respuesta.java          # Entidad de respuesta
│   └── StatusTopico.java       # Enum para estados
├── repository/
│   ├── TopicoRepository.java   # Repositorio de tópicos
│   ├── UsuarioRepository.java  # Repositorio de usuarios
│   └── CursoRepository.java    # Repositorio de cursos
└── service/
    └── TopicoService.java      # Lógica de negocio
```

## Endpoints

### POST /topicos

Crea un nuevo tópico en el foro.

**Request Body:**

```json
{
  "titulo": "¿Cómo configurar Spring Security?",
  "mensaje": "Necesito ayuda para configurar la autenticación en mi proyecto Spring Boot",
  "autorId": 1,
  "cursoId": 1
}
```

**Response (201 Created):**

```json
{
  "id": 1,
  "titulo": "¿Cómo configurar Spring Security?",
  "mensaje": "Necesito ayuda para configurar la autenticación en mi proyecto Spring Boot",
  "fechaCreacion": "2025-08-19T10:30:00",
  "status": "ABIERTO",
  "autorNombre": "Juan Pérez",
  "cursoNombre": "Spring Boot Avanzado"
}
```

**Validaciones:**

- `titulo`: Campo obligatorio
- `mensaje`: Campo obligatorio
- `autorId`: Campo obligatorio, debe existir en la base de datos
- `cursoId`: Campo obligatorio, debe existir en la base de datos
- No se permiten tópicos duplicados (mismo título y mensaje)

**Códigos de Error:**

- `400 Bad Request`: Errores de validación o tópico duplicado
- `404 Not Found`: Usuario o curso no encontrado
- `500 Internal Server Error`: Error interno del servidor

### GET /topicos

Lista todos los tópicos con paginación y filtros opcionales.

**Parámetros de consulta:**

- `page`: Número de página (por defecto: 0)
- `size`: Tamaño de página (por defecto: 10)
- `sort`: Campo para ordenar (por defecto: fechaCreacion)
- `curso`: Filtrar por nombre de curso (opcional)
- `anio`: Filtrar por año específico (opcional)

**Ejemplos de uso:**

```bash
# Listar todos los tópicos (primera página, 10 elementos)
GET /topicos

# Listar con paginación personalizada
GET /topicos?page=0&size=5&sort=titulo

# Filtrar por curso
GET /topicos?curso=Spring Boot

# Filtrar por año
GET /topicos?anio=2025

# Filtrar por curso y año
GET /topicos?curso=Spring Boot&anio=2025
```

**Response (200 OK):**

```json
{
  "content": [
    {
      "id": 1,
      "titulo": "¿Cómo configurar Spring Security?",
      "mensaje": "Necesito ayuda para configurar la autenticación",
      "fechaCreacion": "2025-08-19T10:30:00",
      "status": "ABIERTO",
      "autorNombre": "Juan Pérez",
      "cursoNombre": "Spring Boot Avanzado"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "numberOfElements": 1
}
```

### GET /topicos/primeros10

Lista los primeros 10 tópicos ordenados por fecha de creación en orden ascendente.

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "titulo": "¿Cómo configurar Spring Security?",
    "mensaje": "Necesito ayuda para configurar la autenticación",
    "fechaCreacion": "2025-08-19T10:30:00",
    "status": "ABIERTO",
    "autorNombre": "Juan Pérez",
    "cursoNombre": "Spring Boot Avanzado"
  }
]
```

### GET /topicos/{id}

Obtiene el detalle de un tópico específico por su ID.

**Parámetros de ruta:**

- `id`: ID del tópico (obligatorio)

**Ejemplo de uso:**

```bash
GET /topicos/1
```

**Response (200 OK):**

```json
{
  "id": 1,
  "titulo": "¿Cómo configurar Spring Security?",
  "mensaje": "Necesito ayuda para configurar la autenticación en mi proyecto Spring Boot",
  "fechaCreacion": "2025-08-19T10:30:00",
  "status": "ABIERTO",
  "autorNombre": "Juan Pérez",
  "cursoNombre": "Spring Boot Avanzado"
}
```

**Códigos de Error:**

- `404 Not Found`: Tópico no encontrado con el ID especificado
- `400 Bad Request`: ID inválido (no numérico)

### PUT /topicos/{id}

Actualiza un tópico existente por su ID.

**Parámetros de ruta:**

- `id`: ID del tópico a actualizar (obligatorio)

**Request Body:**

```json
{
  "titulo": "¿Cómo configurar Spring Security? - Actualizado",
  "mensaje": "Necesito ayuda para configurar la autenticación en mi proyecto Spring Boot. He agregado más detalles.",
  "autorId": 1,
  "cursoId": 1
}
```

**Validaciones:**

- `titulo`: Campo obligatorio
- `mensaje`: Campo obligatorio
- `autorId`: Campo obligatorio, debe existir en la base de datos
- `cursoId`: Campo obligatorio, debe existir en la base de datos
- No se permiten tópicos duplicados (mismo título y mensaje)
- El tópico a actualizar debe existir

**Response (200 OK):**

```json
{
  "id": 1,
  "titulo": "¿Cómo configurar Spring Security? - Actualizado",
  "mensaje": "Necesito ayuda para configurar la autenticación en mi proyecto Spring Boot. He agregado más detalles.",
  "fechaCreacion": "2025-08-19T10:30:00",
  "status": "ABIERTO",
  "autorNombre": "Juan Pérez",
  "cursoNombre": "Spring Boot Avanzado"
}
```

**Códigos de Error:**

- `404 Not Found`: Tópico no encontrado con el ID especificado
- `400 Bad Request`: Errores de validación o tópico duplicado
- `404 Not Found`: Usuario o curso no encontrado

### DELETE /topicos/{id}

Elimina un tópico específico por su ID.

**Parámetros de ruta:**

- `id`: ID del tópico a eliminar (obligatorio)

**Ejemplo de uso:**

```bash
DELETE /topicos/1
```

**Response (204 No Content):**
No se devuelve contenido en el cuerpo de la respuesta. El código de estado 204 indica que la eliminación fue exitosa.

**Códigos de Error:**

- `404 Not Found`: Tópico no encontrado con el ID especificado
- `400 Bad Request`: ID inválido (no numérico)

### Endpoints de Testing (Temporales)

#### POST /test/usuario

Crea un usuario para testing.

**Request Body:**

```json
{
  "nombre": "Juan Pérez",
  "correoElectronico": "juan.perez@email.com",
  "contrasena": "password123"
}
```

#### POST /test/curso

Crea un curso para testing.

**Request Body:**

```json
{
  "nombre": "Spring Boot Avanzado",
  "categoria": "Programación"
}
```

#### GET /test/usuarios

Lista todos los usuarios.

#### GET /test/cursos

Lista todos los cursos.

## Configuración de Base de Datos

La aplicación está configurada para conectarse a una base de datos MySQL llamada `ForoHub`.

**Configuración en application.properties:**

```properties
spring.datasource.url=jdbc:mysql://localhost/ForoHub
spring.datasource.username=root
spring.datasource.password=root
```

## Ejemplo de Uso

1. **Crear un usuario de prueba:**

```bash
curl -X POST http://localhost:8080/test/usuario \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "correoElectronico": "juan.perez@email.com",
    "contrasena": "password123"
  }'
```

2. **Crear un curso de prueba:**

```bash
curl -X POST http://localhost:8080/test/curso \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Spring Boot Avanzado",
    "categoria": "Programación"
  }'
```

3. **Crear un tópico:**

```bash
curl -X POST http://localhost:8080/topicos \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "¿Cómo configurar Spring Security?",
    "mensaje": "Necesito ayuda para configurar la autenticación",
    "autorId": 1,
    "cursoId": 1
  }'
```

4. **Listar todos los tópicos:**

```bash
curl -X GET http://localhost:8080/topicos
```

5. **Listar tópicos con filtros:**

```bash
# Filtrar por curso
curl -X GET "http://localhost:8080/topicos?curso=Spring%20Boot"

# Filtrar por año
curl -X GET "http://localhost:8080/topicos?anio=2025"

# Paginación personalizada
curl -X GET "http://localhost:8080/topicos?page=0&size=5"
```

6. **Obtener los primeros 10 tópicos:**

```bash
curl -X GET http://localhost:8080/topicos/primeros10
```

7. **Obtener detalle de un tópico específico:**

```bash
curl -X GET http://localhost:8080/topicos/1
```

8. **Actualizar un tópico existente:**

```bash
curl -X PUT http://localhost:8080/topicos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "¿Cómo configurar Spring Security? - Actualizado",
    "mensaje": "Necesito ayuda para configurar la autenticación en mi proyecto Spring Boot. He agregado más detalles.",
    "autorId": 1,
    "cursoId": 1
  }'
```

9. **Eliminar un tópico específico:**

```bash
curl -X DELETE http://localhost:8080/topicos/1
```

## Tecnologías Utilizadas

- Spring Boot 3.5.4
- Spring Data JPA
- Spring Validation
- MySQL
- Lombok
- Maven
