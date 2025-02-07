package bodegatienda;

import bodegatienda.vistas.VentanaLogin;

public class Main {
    public static void main(String[] args) {
        // Inicializar Firebase antes de abrir la interfaz gráfica
        FirebaseConfig.initFirebase();
        
        // Mostrar la ventana de login
        java.awt.EventQueue.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}


