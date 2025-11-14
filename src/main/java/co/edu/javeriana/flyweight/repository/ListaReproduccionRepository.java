package co.edu.javeriana.flyweight.repository;

import co.edu.javeriana.flyweight.model.ListaReproduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaReproduccionRepository extends JpaRepository<ListaReproduccion, Long> {
    Optional<ListaReproduccion> findByNombreLista(String nombreLista);

    @Query("SELECT l FROM ListaReproduccion l ORDER BY l.contadorAcceso DESC")
    List<ListaReproduccion> findTopUsed();

    @Query("SELECT COUNT(l) FROM ListaReproduccion l")
    Long countTotalListas();
}
