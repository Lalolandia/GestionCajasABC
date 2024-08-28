package gestioncajasabc.ColaUsuario;

public class NodoU {
    
    private Usuario tiquete; 
    private NodoU siguiente;

    public NodoU() {
        this.tiquete = null;
        siguiente = null;
    }

    public NodoU(Usuario tiquete) {
        this.tiquete = tiquete;
        siguiente = null;
    }

    public Usuario getDato() {
        return tiquete;
    }

    public void setDato(Usuario tiquete) {
        this.tiquete = tiquete;
    }

    public NodoU getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoU siguiente) {
        this.siguiente = siguiente;
    }
    
}
