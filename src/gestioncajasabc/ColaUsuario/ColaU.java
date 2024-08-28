package gestioncajasabc.ColaUsuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ColaU {

    private NodoU inicio, ultimo;
    private int cantU = 0;

    public ColaU() {
        this.inicio = this.ultimo = null;
    }

    public boolean esVacia() {
        return inicio == null;
    }

    // Método encola nodos
    public void encola(Usuario user) {
        NodoU nuevo = new NodoU(user);
        if (esVacia()) {
            inicio = ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        cantU ++;
    }

    // Método para iniciar sesión
    public boolean login(String user, String key) {
        if (this.esVacia()) {
            return false;
        }
        NodoU aux = inicio;
        while (aux != null) {
            if (aux.getDato().getUsuario().equals(user)) {
                return aux.getDato().getContrasena().equals(key);
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    // Metodo que carga los usuarios de la base de datos al programa
    public boolean cargarUsuarios(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    encola(new Usuario(datos[0], datos[1]));
                }
            }
            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error leyendo el archivo: " + e);
            return false;
        }
    }
    
    // Método toString
    @Override
    public String toString() {
        String mensaje = "";
        NodoU aux = inicio;
        while (aux != null) {
            mensaje += aux.toString();
            aux = aux.getSiguiente();
        }
        return mensaje;
    }

    public int getCantU() {
        return cantU;
    }

    public void setCantU(int cantU) {
        this.cantU = cantU;
    }
}
