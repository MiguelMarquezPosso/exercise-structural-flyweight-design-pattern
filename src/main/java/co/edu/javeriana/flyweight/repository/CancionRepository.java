package co.edu.javeriana.flyweight.repository;

import co.edu.javeriana.flyweight.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
    Optional<Cancion> findByNombreCancion(String nombreCancion);

    @Query("SELECT COUNT(c) FROM Cancion c")
    Long countTotalCanciones();
}
