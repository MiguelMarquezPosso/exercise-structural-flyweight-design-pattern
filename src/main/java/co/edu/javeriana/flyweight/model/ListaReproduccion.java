package co.edu.javeriana.flyweight.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "listas_reproduccion")
public class ListaReproduccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_lista")
    private String nombreLista;

    @Column(name = "contador_acceso")
    private Integer contadorAcceso = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lista_canciones",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "cancion_id")
    )
    private List<Cancion> canciones = new ArrayList<>();

    public ListaReproduccion() {}

    public ListaReproduccion(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreLista() { return nombreLista; }
    public void setNombreLista(String nombreLista) { this.nombreLista = nombreLista; }

    public Integer getContadorAcceso() { return contadorAcceso; }
    public void setContadorAcceso(Integer contadorAcceso) { this.contadorAcceso = contadorAcceso; }

    public List<Cancion> getCanciones() { return canciones; }
    public void setCanciones(List<Cancion> canciones) { this.canciones = canciones; }

    public void incrementarAcceso() {
        this.contadorAcceso++;
    }
}
