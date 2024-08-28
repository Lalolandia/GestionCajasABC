package gestioncajasabc;

public class Main {

    /**
     * @param args the command line arguments
     */
    
    /*
    Grupo 5
    Integrantes:
    CASTILLO VEGA EDWARD ANTONIO 
    DAVILA HERRERA FERNANDO JOSE
    HERNANDEZ ALVAREZ LUIS CARLOS
    HUETE CARBALLO CARLOS ISAAC
     */
    
    public static void main(String[] args) {

        Menu menu = new Menu();
        String usuarioLogueado = menu.login();

        if (usuarioLogueado != null) {
            Banco banco = new Banco();
            String rutaBanco = "prod.txt";
            if (banco.cargarBanco(rutaBanco)) {
                menu.menuPrincipal(usuarioLogueado, banco);
            } else if (banco.actualizarBanco(rutaBanco)) {
                menu.menuPrincipal(usuarioLogueado, banco);
            }
        }

    }

}
