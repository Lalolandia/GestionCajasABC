package gestioncajasabc;

import gestioncajasabc.ColaTiquete.ColaT;
import gestioncajasabc.ColaTiquete.NodoT;
import gestioncajasabc.ColaTiquete.Tiquete;
import gestioncajasabc.ColaUsuario.ColaU;
import java.awt.HeadlessException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class Menu {

    String rutaBanco = "prod.txt";

    //Login del usuario
    public String login() {

        int intentos = 3;
        String ruta = "usuarios.txt";
        ColaU listaU = new ColaU();
        if (listaU.cargarUsuarios(ruta)) {
            if (listaU.getCantU() >= 2) {
                System.out.println();
                do {
                    String user = JOptionPane.showInputDialog("Ingrese el usuario con el que desea iniciar sesion: ");
                    String key = JOptionPane.showInputDialog("Ingrese la contraseña del usuario: ");
                    if (listaU.login(user, key)) {
                        JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                        return user;
                    } else {
                        intentos--;
                        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta. Tiene " + intentos + " intentos.");
                    }
                } while (intentos > 0);
                JOptionPane.showMessageDialog(null, "Ha agotado los intentos. Se procede con el cierre del programa.");
            } else {
                JOptionPane.showMessageDialog(null, "Error: El sistema debe contar con al menos dos usuarios. Contacte a su administrador.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error al cargar base de datos. Contacte a su administrador.");
        }
        return null;
    }

    //Menu Principal
    public void menuPrincipal(String usuario, Banco banco) {

        String opcion;
        do {
            opcion = JOptionPane.showInputDialog("Seleccione un modulo:\n"
                    + "1. Creación de tiquetes\n"
                    + "2. Atender tiquete\n"
                    + "3. Reportes\n"
                    + "4. Buscar tiquete por numero\n"
                    + "5. Salir");
            if (opcion == null) {
                break;
            }
            switch (opcion) {
                case "1":
                    crearTiquete(banco);
                    break;
                case "2":
                    atenderTiquete(banco);
                    break;
                case "3":
                    menuReportes(usuario, banco);
                    break;
                case "4":
                    int numeroT;
                    do {
                        try {
                            numeroT = Integer.parseInt(JOptionPane.showInputDialog("Digite el numero de tiquete a buscar:"));
                        } catch (HeadlessException | NumberFormatException e) {
                            numeroT = 0;
                        }
                    } while (numeroT <= 0);
                    buscarTiquete(banco, numeroT);
                    break;
                case "5":
                    JOptionPane.showMessageDialog(null, "¡Gracias por usar el programa! Esperamos vuelva pronto");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        } while (!opcion.equals(
                "5"));
    }

    //Crear tiquete
    public void crearTiquete(Banco banco) {

        String nombre;
        do {
            nombre = JOptionPane.showInputDialog("Nombre del Cliente:");
            if (nombre == null) {
                nombre = "";
            }
        } while (nombre.isBlank() || nombre.isEmpty());

        int edad;
        do {
            try {
                edad = Integer.parseInt(JOptionPane.showInputDialog("Edad del Cliente:"));
            } catch (HeadlessException | NumberFormatException e) {
                edad = 0;
            }
        } while (edad <= 0);

        String id;
        do {
            id = JOptionPane.showInputDialog("Identificación del Cliente:");
            if (id == null) {
                id = "";
            }
        } while (id.isBlank() || id.isEmpty());

        String[] tramites = {"Depósitos", "Retiros", "Cambio de Divisas"};

        String tramite;
        do {
            tramite = (String) JOptionPane.showInputDialog(null, "Seleccione el trámite:", "Trámite", JOptionPane.QUESTION_MESSAGE, null, tramites, tramites[0]);
        } while (tramite == null);

        String[] tipos = {"P. Preferencial", "A. Un solo tramite", "B. Dos o mas tramites"};

        String tipo;
        do {
            tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo:", "Tipo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        } while (tipo == null);

        // Obtener la cola con menos tiquetes por tipo de cola
        String nombreCola = banco.obtenerNombreColaPorTipo(tipo);

        // Verificar si hay una cola disponible para el tipo de tiquete
        if (nombreCola == null) {
            JOptionPane.showMessageDialog(null, "No hay colas disponibles para este tipo de tiquete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la cola correspondiente
        ColaT colaAsignada = banco.getColasCajas().get(nombreCola);

        String estado = "Pendiente";
        int numTiquete = banco.sumarTamanoColas() + 1;

        // Crear y agregar el nuevo tiquete a la cola
        Tiquete nuevoTiquete = new Tiquete(numTiquete, id, nombre, edad, tramite, tipo, estado, nombreCola);

        colaAsignada.instera(nuevoTiquete);

        if (colaAsignada.getActivos() == 1) {
            estado = "Atendiendo";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm");
            String horaActual = ZonedDateTime.now().format(formatter);
            nuevoTiquete.setHoraAtencion(horaActual);
            nuevoTiquete.setEstado(estado);
        }

        // Mostrar la información del tiquete
        buscarTiquete(banco, numTiquete);
        banco.guardarEstado(rutaBanco);
        int fila = colaAsignada.getActivos() - 1;
        if (fila > 0) {
            JOptionPane.showMessageDialog(null, "Debe esperar a ser atendido. \nNumero de personas antes en la cola: " + fila);
        } else {
            JOptionPane.showMessageDialog(null, "Puede dirigirse a la caja asignada " + nombreCola + " para ser atendido.");
        }
    }

    //Buscar tiquete
    public void buscarTiquete(Banco banco, int numeroTiquete) {
        Tiquete tiquete = banco.buscarTiquete(numeroTiquete);
        if (tiquete != null) {
            String infoTiquete = "Informacion del tiquete:\n"
                    + "\nNumero de tiquete: " + numeroTiquete
                    + "\nNumero de identicacion: " + tiquete.getId()
                    + "\nNombre del cliente: " + tiquete.getNombre()
                    + "\nEdad del cliente: " + tiquete.getEdad()
                    + "\nTipo de tramite seleccionado: " + tiquete.getTramite()
                    + "\nTipo de tiquete: " + tiquete.getTipo()
                    + "\nCaja asignada: " + tiquete.getCaja()
                    + "\nHora de creacion: " + tiquete.getHoraCreacion()
                    + "\nHora de atencion: " + tiquete.getHoraAtencion()
                    + "\nEstado del tiquete: " + tiquete.getEstado();
            JOptionPane.showMessageDialog(null, infoTiquete, "Información del Tiquete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Tiquete no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Menu reportes
    public void menuReportes(String usuario, Banco banco) {

        String opcion;
        do {
            opcion = JOptionPane.showInputDialog("Seleccione un modulo:\n"
                    + "1. Caja con mayor número de clientes atendidos.\n"
                    + "2. Total de clientes atendidos\n"
                    + "3. Caja con el mejor promedio de tiempo de atención de tiquetes.\n"
                    + "4. Promedio de tiempo de atención de tiquetes general.\n"
                    + "5. Regresar al menú principal");
            if (opcion == null) {
                break;
            }
            switch (opcion) {
                case "1":
                    JOptionPane.showMessageDialog(null, banco.cajaConMasClientesAtendidos());
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, "Total de clientes atendidos: " + banco.totalClientesAtendidos());
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, banco.cajaConMejorTiempoPromedio());
                    break;
                case "4":
                    JOptionPane.showMessageDialog(null, "Promedio de tiempo de atencion de tiquetes en minutos: " + banco.promedioGeneralTiempoAtencion());
                    break;
                case "5":
                    JOptionPane.showMessageDialog(null, "Regresando al menú principal...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        } while (!opcion.equals(
                "5"));
    }

    //Atender tiquetes
    public void atenderTiquete(Banco banco) {
        String cajaSeleccionada = JOptionPane.showInputDialog("Ingrese el número de la caja que desea trabajar:");

        ColaT colaTiquetes = banco.obtenerColaTiquetes(cajaSeleccionada);

        if (colaTiquetes != null && !colaTiquetes.esVacia()) {
            NodoT nodoAtendiendo = colaTiquetes.buscarPrimerTiqueteNoResuelto();

            if (nodoAtendiendo != null) {
                Tiquete tiqueteAtendiendo = nodoAtendiendo.getDato();
                tiqueteAtendiendo.setEstado("Atendiendo");
                buscarTiquete(banco, tiqueteAtendiendo.getNumTiquete());
                
                int opcion = JOptionPane.showConfirmDialog(null, infoComplementaria(tiqueteAtendiendo.getTramite()) + 
                "¿Marcar este tiquete como resuelto?", "Atendiendo Tiquete", JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    tiqueteAtendiendo.setEstado("Resuelto");
                    banco.guardarEstado(rutaBanco);

                    NodoT siguienteNodo = colaTiquetes.buscarPrimerTiqueteNoResuelto();
                    if (siguienteNodo != null) {
                        Tiquete siguienteTiquete = siguienteNodo.getDato();
                        siguienteTiquete.setEstado("Atendiendo");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm");
                        String horaActual = ZonedDateTime.now().format(formatter);
                        siguienteTiquete.setHoraAtencion(horaActual);

                        JOptionPane.showMessageDialog(null, "El siguiente tiquete ahora está en estado 'Atendiendo'.\n" + siguienteTiquete);
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay más tiquetes pendientes en esta caja.");
                    }

                    int nuevaOpcion = JOptionPane.showConfirmDialog(null, "¿Desea atender un nuevo caso?", "Atención de Tiquetes", JOptionPane.YES_NO_OPTION);
                    if (nuevaOpcion == JOptionPane.YES_OPTION) {
                        atenderTiquete(banco);
                    } else {
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay tiquetes pendientes o en estado \"Atendiendo\" o \"Pendiente\" en esta caja.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay tiquetes pendientes en esta caja.");
        }
    }

    public String infoComplementaria(String tramite) {
        String info="";
        switch (tramite) {
                case "Depósitos":
                    info="Al realizar un depósito, considere ofrecer al cliente el servicio de seguros. Este producto "
                            + "complementario puede proporcionar al cliente mayor seguridad financiera y protección "
                            + "para sus ahorros.\n\n";
                    break;
                case "Retiros":
                    info="Si el cliente está realizando un retiro, no olvide ofrecer la opción de 'retiro sin tarjeta' "
                            + "disponible en nuestros cajeros automáticos. Esto permite a los clientes acceder a su dinero "
                            + "de manera más flexible y segura.\n\n";
                    break;
                case "Cambio de Divisas":
                    info="Cuando un cliente realice un cambio de divisas, puede ser útil ofrecer información sobre "
                            + "servicios adicionales como la transferencia internacional de fondos. Este servicio podría"
                            + " ser especialmente relevante para aquellos que necesitan enviar dinero al extranjero.\n";
                    info+="\nDolar:\nCompra: 514 colones\t\tVenta: 528 colones\n"
                            + "Euro:\nCompra: 562 colones\t\tVenta: 591 colones\n\n";
                    break;
                default:
                    info = "Error tipo de tramite no encontrado.\n";
            }
        return info;
    }

}
