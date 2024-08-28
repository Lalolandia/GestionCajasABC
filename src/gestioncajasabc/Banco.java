package gestioncajasabc;

import gestioncajasabc.ColaTiquete.ColaT;
import gestioncajasabc.ColaTiquete.Tiquete;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class Banco {

    private String nombre;
    private int cantCajas;
    private Map<String, ColaT> colasCajas;

    // Constructor sin inicialización de colas
    public Banco() {
        this.colasCajas = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(int cantCajas) {
        this.cantCajas = cantCajas;
    }

    public boolean cargarBanco(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = br.readLine();
            if (linea != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    this.nombre = datos[0];
                    this.cantCajas = Integer.parseInt(datos[1]);
                    inicializarColas(); // Inicializar las colas después de cargar cantCajas

                    // Cargar los tiquetes en las colas
                    while ((linea = br.readLine()) != null) {
                        String[] partes = linea.split(":");
                        String nombreCaja = partes[0].trim();
                        ColaT cola = colasCajas.get(nombreCaja);

                        if (cola != null && partes.length > 1 && !partes[1].trim().isEmpty()) {
                            // Dividir la parte que contiene los tiquetes
                            String[] tiquetes = partes[1].split(";");
                            for (String t : tiquetes) {
                                String[] tiqueteDatos = t.split(",");

                                if (tiqueteDatos.length == 10) {
                                    try {
                                        int numTiquete = Integer.parseInt(tiqueteDatos[0].trim());
                                        String id = tiqueteDatos[1].trim();
                                        String nombre = tiqueteDatos[2].trim();
                                        int edad = Integer.parseInt(tiqueteDatos[3].trim());
                                        String tramite = tiqueteDatos[4].trim();
                                        String horaCreacion = tiqueteDatos[5].trim();
                                        String horaAtencion = tiqueteDatos[6].trim();
                                        String tipo = tiqueteDatos[7].trim();
                                        String estado = tiqueteDatos[8].trim();
                                        String colaAsignada = tiqueteDatos[9].trim();

                                        // Crear el tiquete
                                        Tiquete tiquete = new Tiquete(numTiquete, id, nombre, edad, tramite, tipo, estado, colaAsignada);
                                        tiquete.setHoraCreacion(horaCreacion);
                                        tiquete.setHoraAtencion(horaAtencion);

                                        // Insertar en la cola
                                        cola.instera(tiquete);

                                    } catch (Exception e) {
                                        // Manejo de excepciones al procesar el tiquete
                                        JOptionPane.showMessageDialog(null, "Error al procesar el tiquete: " + t + "\n" + e.getMessage());
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Formato de tiquete incorrecto: " + t);
                                }
                            }
                        }
                    }
                    return true;
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del banco: " + e.getMessage());
        }
        return false;
    }

    // Método que solicita al usuario los datos del banco y los guarda en el archivo prod.txt
    public boolean actualizarBanco(String rutaArchivo) {
        this.nombre = JOptionPane.showInputDialog("Ingrese el nombre del banco:");
        int cantCajas = 1;
        do {
            try {
                cantCajas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de cajas, la cantidad mínima es de tres.\n"
                        + "La ingresada cantidad será administrada de la siguiente manera: \n"
                        + "-> Una única caja preferencial.\n"
                        + "-> Una única para trámites rápidos (un solo trámite).\n"
                        + "-> Las demás son para dos o más trámites y no preferenciales."));
            } catch (HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error dato no válido: " + e.getMessage());
            }
            if (cantCajas < 3) {
                JOptionPane.showMessageDialog(null, "La cantidad ingresada es menor a la cantidad mínima.");
            }
        } while (cantCajas < 3);
        this.cantCajas = cantCajas;

        // Guardar los datos en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            bw.write(this.nombre + "," + this.cantCajas);
            JOptionPane.showMessageDialog(null, "Datos guardados exitosamente.");
            inicializarColas();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando el archivo: " + e.getMessage());
            return false;
        }
    }

    // Método que inicializa las colas una vez que cantCajas ha sido cargado
    public void inicializarColas() {
        if (cantCajas < 3) {
            throw new IllegalArgumentException("La cantidad de cajas debe ser al menos 3.");
        }
        for (int i = 1; i <= cantCajas; i++) {
            String nombreCaja = "C" + i;
            colasCajas.put(nombreCaja, new ColaT());
        }
    }

    // Método para obtener la cola según el tipo de tiquete
    public String obtenerNombreColaPorTipo(String tipo) {
        String nombreCaja = null;
        switch (tipo) {
            case "P. Preferencial":
                nombreCaja = "C1"; // Primera caja preferencial
                break;
            case "A. Un solo tramite":
                nombreCaja = "C2"; // Segunda caja para trámites rápidos
                break;
            case "B. Dos o mas tramites":
                // Encuentra la caja de tipo B con menos personas en fila
                int menorTamano = Integer.MAX_VALUE;
                for (int i = 3; i <= cantCajas; i++) {
                    String caja = "C" + i;
                    ColaT cola = colasCajas.get(caja);
                    if (cola != null && cola.getActivos() < menorTamano) {
                        menorTamano = cola.getActivos();
                        nombreCaja = caja;
                    }
                }
                break;
            default:
                return null;
        }
        return nombreCaja;
    }

    public Map<String, ColaT> getColasCajas() {
        return colasCajas;
    }

    public int sumarTamanoColas() {
        int sumaTotal = 0;
        for (ColaT cola : colasCajas.values()) {
            sumaTotal += cola.getTamano();
        }
        return sumaTotal;
    }

    public Tiquete buscarTiquete(int numeroTiquete) {
        for (Map.Entry<String, ColaT> entry : colasCajas.entrySet()) {
            ColaT cola = entry.getValue();
            Tiquete tiquete =cola.buscarTiquete(numeroTiquete);
            if (tiquete != null) {
                return tiquete; // Devuelve el tiquete si lo encuentra
            }
        }
        return null; // Devuelve null si no se encuentra el tiquete
    }

    public void guardarEstado(String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            bw.write(this.nombre + "," + this.cantCajas);
            bw.newLine();

            for (Map.Entry<String, ColaT> entry : colasCajas.entrySet()) {
                String nombreCaja = entry.getKey();
                ColaT cola = entry.getValue();

                bw.write(nombreCaja + ":");
                bw.write(cola.toString());
                bw.newLine();
            }

            bw.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error guardando el archivo: " + e.getMessage());
        }
    }

    public ColaT obtenerColaTiquetes(String numeroCaja) {
        return colasCajas.get(numeroCaja);
    }

    public String cajaConMasClientesAtendidos() {
        int maxResueltos = 0;
        List<String> cajasEmpatadas = new ArrayList<>();

        for (Map.Entry<String, ColaT> entry : colasCajas.entrySet()) {
            ColaT cola = entry.getValue();
            int resueltos = cola.contarTiquetesEstado("Resuelto");

            if (resueltos > maxResueltos) {
                maxResueltos = resueltos;
                cajasEmpatadas.clear();
                cajasEmpatadas.add(entry.getKey());
            } else if (resueltos == maxResueltos) {
                cajasEmpatadas.add(entry.getKey());
            }
        }

        if (cajasEmpatadas.size() == 1) {
            return "Caja con mayor número de clientes atendidos: " + cajasEmpatadas.get(0) + " con " + maxResueltos + " clientes atendidos.";
        } else if (cajasEmpatadas.size() > 1) {
            return "Hay un empate entre las siguientes cajas con " + maxResueltos + " clientes atendidos: " + String.join(", ", cajasEmpatadas);
        } else {
            return "No se encontraron tiquetes resueltos en ninguna caja.";
        }
    }

    public int totalClientesAtendidos() {
        int total = 0;

        for (ColaT cola : colasCajas.values()) {
            total += cola.contarTiquetesEstado("Resuelto");
        }
        return total;
    }

    public String cajaConMejorTiempoPromedio() {
        String mejorCaja = null;
        double mejorPromedio = Double.MAX_VALUE;

        for (Map.Entry<String, ColaT> entry : colasCajas.entrySet()) {
            ColaT cola = entry.getValue();
            double promedio = cola.calcularTiempoPromedioAtencion();

            if (promedio < mejorPromedio) {
                mejorPromedio = promedio;
                mejorCaja = entry.getKey();
            }
        }
        return mejorCaja + " con un promedio de " + mejorPromedio + " minutos.";
    }
    
    public double promedioGeneralTiempoAtencion() {
        int totalTiquetes = 0;
        double sumaTotalTiempo = 0;

        for (ColaT cola : colasCajas.values()) {
            totalTiquetes += cola.contarTiquetesEstado("Resuelto");
            sumaTotalTiempo += cola.calcularSumaTotalTiempoAtencion();
        }
        return totalTiquetes > 0 ? (sumaTotalTiempo / totalTiquetes) : 0;
    }
}
