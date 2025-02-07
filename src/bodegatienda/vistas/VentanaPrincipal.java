package bodegatienda.vistas;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Simulador de Bodega");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //botón que abre la ventana de administración de productos (VentanaProductos)
        JButton btnProductos = new JButton("Administrar Productos De La Bodega");
        btnProductos.addActionListener(e -> new VentanaProductos().setVisible(true));  

        add(btnProductos);
    }
}
