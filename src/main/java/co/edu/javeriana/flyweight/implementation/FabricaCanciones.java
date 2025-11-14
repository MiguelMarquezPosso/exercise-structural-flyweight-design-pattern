package co.edu.javeriana.flyweight.implementation;

import co.edu.javeriana.flyweight.model.Cancion;
import co.edu.javeriana.flyweight.service.GestorListasService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FabricaCanciones {
    public static boolean HabilitarFlyweight = true;
    private static final Map<String, Cancion> PLAY_CANCION = new HashMap<>();
    private static Long Secuencia = 0L;

    private final GestorListasService gestorListasService;

    public FabricaCanciones(GestorListasService gestorListasService) {
        this.gestorListasService = gestorListasService;
    }

    public Cancion crearItem(String nombreTema) {
        if (HabilitarFlyweight && PLAY_CANCION.containsKey(nombreTema)) {
            return PLAY_CANCION.get(nombreTema);
        } else {
            // Usar el servicio que gestiona cache y BD
            Cancion cancion = gestorListasService.obtenerOCrearCancion(nombreTema);
            PLAY_CANCION.put(nombreTema, cancion);
            return cancion;
        }
    }
}