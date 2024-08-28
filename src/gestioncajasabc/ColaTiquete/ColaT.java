package gestioncajasabc.ColaTiquete;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ColaT {

    private NodoT inicio, ultimo;
    private int tamano, activos;

    public ColaT() {
        this.inicio = this.ultimo = null;
        this.tamano = activos = 0;
    }

    public boolean esVacia() {
        return inicio == null;
    }

    // Método encola nodos
    public void instera(Tiquete pTiquete) {
        tamano++;

        NodoT nuevo = new NodoT(pTiquete);
        if (esVacia()) {
            inicio = ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }

        if (!pTiquete.getEstado().equals("Resuelto")) {
            activos++;
        }
    }

    // Método atiende nodos
    public NodoT buscarPrimerTiqueteNoResuelto() {
        NodoT actual = inicio; // Empezamos desde el primer nodo en la cola
        while (actual != null) {
            // Verificamos si el estado del tiquete no es "Resuelto"
            if (!actual.getDato().getEstado().equals("Resuelto")) {
                return actual; // Retorna el nodo si su tiquete no está resuelto
            }
            actual = actual.getSiguiente(); // Avanza al siguiente nodo
        }
        return null; // Si no hay más tiquetes no resueltos, retorna null
    }

    // Buscar valor en la cola
    public Tiquete buscarTiquete(int x) {
        if (this.esVacia()) {
            return null;
        }
        NodoT aux = inicio;
        while (aux != null) {
            if (aux.getDato().getNumTiquete() == x) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public int contarTiquetesEstado(String estado) {
        int contador = 0;
        NodoT actual = inicio;

        while (actual != null) {
            Tiquete tiquete = actual.getDato();
            if (tiquete.getEstado().equals(estado)) {
                contador++;
            }
            actual = actual.getSiguiente();
        }

        return contador;
    }

    public double calcularTiempoPromedioAtencion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm");
        int contador = 0;
        double sumaDuracionMinutos = 0;

        NodoT actual = inicio;

        while (actual != null) {
            Tiquete tiquete = actual.getDato();

            // Verificamos si el tiquete está en estado "Resuelto"
            if (tiquete.getEstado().equals("Resuelto") && !tiquete.getHoraAtencion().equals("-1")) {
                LocalDateTime horaCreacion = LocalDateTime.parse(tiquete.getHoraCreacion(), formatter);
                LocalDateTime horaAtencion = LocalDateTime.parse(tiquete.getHoraAtencion(), formatter);

                // Calcular la duración entre horaCreacion y horaAtencion
                Duration duracion = Duration.between(horaCreacion, horaAtencion);
                sumaDuracionMinutos += duracion.toMinutes();
                contador++;
            }

            actual = actual.getSiguiente();
        }

        // Calcular el promedio
        return (contador > 0) ? (sumaDuracionMinutos / contador) : 0.0;
    }

    public double calcularSumaTotalTiempoAtencion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm");
        double sumaDuracionMinutos = 0;

        NodoT actual = inicio;

        while (actual != null) {
            Tiquete tiquete = actual.getDato();

            // Verificamos si el tiquete está en estado "Resuelto"
            if (tiquete.getEstado().equals("Resuelto") && !tiquete.getHoraAtencion().equals("-1")) {
                LocalDateTime horaCreacion = LocalDateTime.parse(tiquete.getHoraCreacion(), formatter);
                LocalDateTime horaAtencion = LocalDateTime.parse(tiquete.getHoraAtencion(), formatter);

                // Calcular la duración entre horaCreacion y horaAtencion
                Duration duracion = Duration.between(horaCreacion, horaAtencion);
                sumaDuracionMinutos += duracion.toMinutes();
            }

            actual = actual.getSiguiente();
        }

        return sumaDuracionMinutos;
    }

    // Método toString
    @Override
    public String toString() {
        String mensaje = "";
        NodoT aux = inicio;
        while (aux != null) {
            mensaje += aux.toString();
            aux = aux.getSiguiente();
        }
        return mensaje;
    }

    public NodoT getInicio() {
        return inicio;
    }

    public void setInicio(NodoT inicio) {
        this.inicio = inicio;
    }

    public NodoT getUltimo() {
        return ultimo;
    }

    public void setUltimo(NodoT ultimo) {
        this.ultimo = ultimo;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public int getActivos() {
        return activos;
    }

    public void setActivos(int activos) {
        this.activos = activos;
    }

}
