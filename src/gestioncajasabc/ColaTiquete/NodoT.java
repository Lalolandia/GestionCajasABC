package gestioncajasabc.ColaTiquete;

public class NodoT {

    private Tiquete tiquete; 
    private NodoT siguiente;

    public NodoT() {
        this.tiquete = null;
        siguiente = null;
    }

    public NodoT(Tiquete tiquete) {
        this.tiquete = tiquete;
        siguiente = null;
    }

    public Tiquete getDato() {
        return tiquete;
    }

    public void setDato(Tiquete tiquete) {
        this.tiquete = tiquete;
    }

    public NodoT getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoT siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return "" + tiquete ;
    }
}
