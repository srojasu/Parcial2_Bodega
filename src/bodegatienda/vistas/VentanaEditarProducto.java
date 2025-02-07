package bodegatienda.vistas;

import bodegatienda.FirebaseDatabaseController;
import bodegatienda.Producto;

import javax.swing.*;
import java.awt.*;

public class VentanaEditarProducto extends JFrame {

    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JButton btnActualizar;
    private Producto producto;
    private FirebaseDatabaseController dbController;

    public VentanaEditarProducto(Producto producto) {
        this.producto = producto;
        dbController = new FirebaseDatabaseController();
        
        setTitle("Editar Producto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //crear los campos de texto y etiquetas
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(producto.getNombre(), 20);

        JLabel lblCantidad = new JLabel("Cantidad:");
        txtCantidad = new JTextField(String.valueOf(producto.getCantidad()), 20);

        JLabel lblPrecio = new JLabel("Precio:");
        txtPrecio = new JTextField(String.valueOf(producto.getPrecio()), 20);

        btnActualizar = new JButton("Actualizar");
        
        //configurar el diseño del formulario
        setLayout(new GridLayout(5, 2));
        add(lblNombre);
        add(txtNombre);
        add(lblCantidad);
        add(txtCantidad);
        add(lblPrecio);
        add(txtPrecio);
        add(btnActualizar);
        
        //acción de actualizar el producto
        btnActualizar.addActionListener(e -> actualizarProducto());
    }

    private void actualizarProducto() {
        String nombre = txtNombre.getText();
        String cantidadStr = txtCantidad.getText();
        String precioStr = txtPrecio.getText();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            //crear un nuevo objeto Producto con los datos editados
            Producto productoActualizado = new Producto(producto.getId(), nombre, cantidad, precio);
            
            //llamar al método actualizarProducto de la clase FirebaseDatabaseController para actualizar los datos en Firebase
            dbController.actualizarProducto(producto.getId(), productoActualizado);

            JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores válidos para cantidad y precio.");
        }
    }
}
