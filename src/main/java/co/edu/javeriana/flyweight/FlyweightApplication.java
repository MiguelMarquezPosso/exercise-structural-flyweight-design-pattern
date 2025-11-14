package co.edu.javeriana.flyweight;

import co.edu.javeriana.flyweight.model.ListaReproduccion;
import co.edu.javeriana.flyweight.service.GestorListasService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class FlyweightApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyweightApplication.class, args);
	}

    @Bean
    public CommandLineRunner demo(GestorListasService gestorListasService) {
        return args -> {
            System.out.println("""
                Proceso de creación de listas de reproducción iniciado,
                este proceso puede ser muy retrasado debido a la gran cantidad de objetos
                que se creará, por favor espere un momento hasta que 
                sea notificado de que el proceso ha terminado..""");

            Runtime runtime = Runtime.getRuntime();
            System.out.println("MaxMemory > " + (runtime.maxMemory() / 1000000));

            // Configuración reducida para pruebas
            final String[] NombreCanciones = new String[10];
            final String[] NombresListas = new String[100];

            // Inicializar arreglos
            for (int c = 0; c < NombreCanciones.length; c++) {
                NombreCanciones[c] = "Song " + (c + 1);
            }
            for (int c = 0; c < NombresListas.length; c++) {
                NombresListas[c] = "PlayList " + (c + 1);
            }

            // Crear listas dinámicamente
            Random random = new Random();
            List<ListaReproduccion> listas = new ArrayList<>();
            int p = 0;

            for (int c = 0; c < NombresListas.length; c++) {
                List<String> canciones = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    int song = random.nextInt(NombreCanciones.length);
                    canciones.add(NombreCanciones[song]);
                }

                ListaReproduccion playList = gestorListasService
                        .obtenerOActualizarLista(NombresListas[c], canciones);
                listas.add(playList);

                if (c != 0 && (c + 1) % (NombresListas.length / 10) == 0) {
                    p += 10;
                    System.out.println("Finalizado " + p + "%");
                    System.out.println("Listas Procesadas " + listas.size());

                    // Mostrar estadísticas periódicamente
                    var stats = gestorListasService.getEstadisticas();
                    System.out.println("Estadísticas: " + stats);
                }
            }

            System.out.println("Total Listas > " + listas.size());

            long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
            System.out.println("Memoria Usada => " + (memoryUsed / 1000000));

            // Estadísticas finales
            var statsFinales = gestorListasService.getEstadisticas();
            System.out.println("Estadísticas Finales: " + statsFinales);
        };
    }
}
