package co.edu.javeriana.flyweight.model;

import jakarta.persistence.*;

@Entity
@Table(name = "canciones")
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_cancion", unique = true)
    private String nombreCancion;

    /*@Lob
    @Column(name = "cancion_data")
    private byte[] cancionData;*/

    public Cancion() {}

    public Cancion(String nombreCancion) {
        this.nombreCancion = nombreCancion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCancion() { return nombreCancion; }
    public void setNombreCancion(String nombreCancion) { this.nombreCancion = nombreCancion; }

    /*public byte[] getCancionData() { return cancionData; }
    public void setCancionData(byte[] cancionData) { this.cancionData = cancionData; }*/

    @Override
    public String toString() {
        return "Canción{id=" + id + ", Tema=" + nombreCancion + '}';
    }
}