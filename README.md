# Sistema de Listas de Reproducción con Patrón Flyweight

## 📋 Descripción
Implementación de un sistema de gestión de listas de reproducción musical que utiliza el patrón Flyweight para optimizar el uso de memoria, integrado con Supabase PostgreSQL para persistencia de datos.

## 🎯 Objetivo
Demostrar la reutilización eficiente de objetos pesados (canciones de 1MB) across múltiples listas de reproducción, manteniendo en memoria solo las listas más utilizadas y almacenando el resto en base de datos.

## 🏗️ Patrones Implementados

### 🔄 Patrón Flyweight
**Fábrica**: FabricaCanciones (gestión de cache de canciones)

**Objetos Flyweight**: Cancion (objetos pesados reutilizables)

**Clientes**: ListaReproduccion (contexto que usa los flyweights)

```
flyweight/
├── src/main/java/co/edu/javeriana/flyweight/
│ ├── model/
│ │ ├── Cancion.java                           # Entidad Flyweight
│ │ └── ListaReproduccion.java                 # Entidad lista reproducción
│ ├── repository/
│ │ ├── CancionRepository.java                 # Repository Spring Data
│ │ └── ListaReproduccionRepository.java       # Repository Spring Data
│ ├── service/
│ │ └── GestorListasService.java               # Gestión memoria/BD
│ ├── implementation/
│ │ └── FabricaCanciones.java                  # Fábrica Flyweight
│ └── FlyweightApplication.java                # Clase principal Spring Boot
├── src/main/resources/
│ └── application.properties                   # Configuración Spring
├── pom.xml                                    # Dependencias Maven
├── dockerfile                                 # Contenedor Docker
└── .env                                       # Variables entorno Supabase
```

## ⚙️ Configuración

### 🔧 Variables de Entorno (.env)
En el archivo .env reemplazar las credenciales por las generadas al crear una base de datos PostgreSQL de Supabase.

## 🗄️ Configuración Base de Datos
Ejecutar en SQL Editor de Supabase:
```
-- Limpiar tablas existentes si las hay
DROP TABLE IF EXISTS lista_canciones CASCADE;
DROP TABLE IF EXISTS listas_reproduccion CASCADE;
DROP TABLE IF EXISTS canciones CASCADE;

-- Tabla de canciones
CREATE TABLE canciones (
    id BIGSERIAL PRIMARY KEY,
    nombre_cancion VARCHAR(255) UNIQUE NOT NULL,
    cancion_data BYTEA
);

-- Tabla de listas de reproducción
CREATE TABLE listas_reproduccion (
    id BIGSERIAL PRIMARY KEY,
    nombre_lista VARCHAR(255) NOT NULL,
    contador_acceso INTEGER DEFAULT 0
);

-- Tabla de relación muchos-a-muchos
CREATE TABLE lista_canciones (
    lista_id BIGINT REFERENCES listas_reproduccion(id),
    cancion_id BIGINT REFERENCES canciones(id),
    PRIMARY KEY (lista_id, cancion_id)
);
```

## 🐳 Ejecución con Docker

```
docker build -t flyweight .
docker run --env-file .env -p 8080:8080 flyweight
```

## 🎮 Uso

**Crear Nueva Lista de Reproducción**:
```
// Obtener gestor del contexto Spring
GestorListasService gestor = context.getBean(GestorListasService.class);

// Crear lista con canciones específicas
List<String> canciones = Arrays.asList("Song 1", "Song 5", "Song 3");
ListaReproduccion lista = gestor.obtenerOActualizarLista("Mi Playlist", canciones);
```

**Consultar Listas Existentes**:
```
// Lista en memoria (rápido)
ListaReproduccion listaMemoria = gestor.obtenerOActualizarLista("PlayList 1", canciones);

// Lista desde BD (si no está en memoria)
ListaReproduccion listaBD = gestor.obtenerOActualizarLista("PlayList 50", canciones);
```

## 🔐 Flujo de Ejecución
1. Cliente solicita agregar canción a lista

2. FabricaCanciones verifica si canción existe en cache

3. SI existe: ♻️ Reutiliza objeto existente

4. NO existe: 🆕 Crea nuevo objeto y almacena en cache

5. Lista almacena referencia a canción compartida
