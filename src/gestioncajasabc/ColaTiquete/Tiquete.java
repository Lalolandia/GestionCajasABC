package gestioncajasabc.ColaTiquete;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Tiquete {

    // Atributos de la clase Tiquete:
    private final String id;
    private final int numTiquete;
    private String nombre, tramite, horaCreacion, horaAtencion, tipo, caja, estado;
    private int edad;

    // Constructores de la clase tiquete    
    public Tiquete(int numTiquete, String id, String nombre, int edad, String tramite,
            String tipo, String estado, String caja) {
        this.id = id;
        this.numTiquete = numTiquete;
        this.nombre = nombre;
        this.edad = edad;
        this.tramite = tramite;
        this.tipo = tipo;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm");
        this.horaCreacion = ZonedDateTime.now().format(formatter);
        this.horaAtencion = "-1";
        this.estado = estado;
        this.caja = caja;
    }

    // Setters de los atributos
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public void setHoraCreacion(String horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public void setHoraAtencion(String horaAtencion) {
        this.horaAtencion = horaAtencion;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
    // Getters de los atributos
    public int getNumTiquete() {
        return numTiquete;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTramite() {
        return tramite;
    }

    public String getHoraCreacion() {
        return horaCreacion;
    }

    public String getHoraAtencion() {
        return horaAtencion;
    }

    public String getTipo() {
        return tipo;
    }

    public int getEdad() {
        return edad;
    }

    public String getCaja() {
        return caja;
    }
    
    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return numTiquete + ","
                + id + ","
                + nombre + ","
                + edad + ","
                + tramite + ","
                + horaCreacion + ","
                + horaAtencion + ","
                + tipo + ","
                + estado + ","
                + caja + ";";
    }

}
