package co.edu.javeriana.flyweight.service;

import co.edu.javeriana.flyweight.model.Cancion;
import co.edu.javeriana.flyweight.model.ListaReproduccion;
import co.edu.javeriana.flyweight.repository.CancionRepository;
import co.edu.javeriana.flyweight.repository.ListaReproduccionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GestorListasService {
    private final CancionRepository cancionRepository;
    private final ListaReproduccionRepository listaRepository;

    // Cache en memoria para listas más utilizadas
    private final Map<String, ListaReproduccion> cacheListas = new ConcurrentHashMap<>();
    private final Map<String, Cancion> cacheCanciones = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_SIZE = 1000;

    public GestorListasService(CancionRepository cancionRepository,
                               ListaReproduccionRepository listaRepository) {
        this.cancionRepository = cancionRepository;
        this.listaRepository = listaRepository;
    }

    @Transactional
    public Cancion obtenerOCrearCancion(String nombreCancion) {
        gestionarCacheBasadoEnMemoria();

        // Primero buscar en cache
        if (cacheCanciones.containsKey(nombreCancion)) {
            return cacheCanciones.get(nombreCancion);
        }

        // Buscar en base de datos
        Optional<Cancion> cancionExistente = cancionRepository.findByNombreCancion(nombreCancion);
        if (cancionExistente.isPresent()) {
            Cancion cancion = cancionExistente.get();
            // Agregar a cache si hay espacio
            if (cacheCanciones.size() < MAX_CACHE_SIZE) {
                cacheCanciones.put(nombreCancion, cancion);
            }
            return cancion;
        }

        // Crear nueva canción
        Cancion nuevaCancion = new Cancion(nombreCancion);
        cancionRepository.save(nuevaCancion);

        // Agregar a cache si hay espacio
        if (cacheCanciones.size() < MAX_CACHE_SIZE) {
            cacheCanciones.put(nombreCancion, nuevaCancion);
        }

        return nuevaCancion;
    }

    @Transactional
    public ListaReproduccion obtenerOActualizarLista(String nombreLista, List<String> nombresCanciones) {
        System.out.println("Procesando lista: " + nombreLista); // DEBUG

        // Buscar en cache primero
        if (cacheListas.containsKey(nombreLista)) {
            ListaReproduccion lista = cacheListas.get(nombreLista);
            lista.incrementarAcceso();
            listaRepository.save(lista);
            return lista;
        }

        // Buscar en base de datos
        Optional<ListaReproduccion> listaExistente = listaRepository.findByNombreLista(nombreLista);

        if (listaExistente.isPresent()) {
            // Si ya existe, simplemente devolverla sin modificar
            ListaReproduccion lista = listaExistente.get();
            lista.incrementarAcceso();
            listaRepository.save(lista);
            cacheListas.put(nombreLista, lista);
            return lista;
        } else {
            // Crear NUEVA lista solamente si no existe
            ListaReproduccion lista = new ListaReproduccion(nombreLista);

            // Agregar canciones (usando Flyweight)
            for (String nombreCancion : nombresCanciones) {
                Cancion cancion = obtenerOCrearCancion(nombreCancion);
                lista.getCanciones().add(cancion);
            }

            lista.incrementarAcceso();
            listaRepository.save(lista);

            // Gestionar cache
            if (cacheListas.size() < MAX_CACHE_SIZE) {
                cacheListas.put(nombreLista, lista);
            }

            return lista;
        }
    }

    public void limpiarCache() {
        cacheListas.clear();
        cacheCanciones.clear();
    }

    public Map<String, Object> getEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("listasEnCache", cacheListas.size());
        stats.put("cancionesEnCache", cacheCanciones.size());
        stats.put("totalListasBD", listaRepository.countTotalListas());
        stats.put("totalCancionesBD", cancionRepository.countTotalCanciones());
        return stats;
    }

    private void gestionarCacheBasadoEnMemoria() {
        Runtime runtime = Runtime.getRuntime();
        long memoriaLibre = runtime.freeMemory();
        long memoriaTotal = runtime.totalMemory();

        // Si usa más del 80% de memoria, reducir cache
        double usoMemoria = (double) (memoriaTotal - memoriaLibre) / memoriaTotal;

        if (usoMemoria > 0.8 && cacheListas.size() > 100) {
            // Reducir cache a la mitad
            int nuevoTamano = cacheListas.size() / 2;
            evictarListasMenosUtilizadas(nuevoTamano);
        }
    }

    private void evictarListasMenosUtilizadas(int mantener) {
        List<Map.Entry<String, ListaReproduccion>> menosUtilizadas =
                cacheListas.entrySet().stream()
                        .sorted(Comparator.comparing(e -> e.getValue().getContadorAcceso()))
                        .limit(cacheListas.size() - mantener)
                        .collect(Collectors.toList());

        for (Map.Entry<String, ListaReproduccion> entry : menosUtilizadas) {
            // Guardar en BD antes de remover
            listaRepository.save(entry.getValue());
            cacheListas.remove(entry.getKey());
        }
    }
}
