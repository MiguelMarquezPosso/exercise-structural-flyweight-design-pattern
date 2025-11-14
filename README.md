# 🎵 Sistema de Listas de Reproducción con Patrón Flyweight

## 📋 Descripción
Implementación de un sistema de gestión de listas de reproducción musical que utiliza el patrón Flyweight para optimizar el uso de memoria, integrado con Supabase PostgreSQL para persistencia de datos.

## 🎯 Objetivo
Demostrar la reutilización eficiente de objetos pesados (canciones de 1MB) across múltiples listas de reproducción, manteniendo en memoria solo las listas más utilizadas y almacenando el resto en base de datos.

## 🏗️ Patrones Implementados
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

## 🐳 Ejecución con Docker

```
docker build -t flyweight .
docker run --env-file .env -p 8080:8080 flyweight
```

## 🎮 Uso

## 🔐 Flujo de Ejecución
