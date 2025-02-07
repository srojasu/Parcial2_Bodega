package bodegatienda.vistas;

import bodegatienda.FirebaseDatabaseController;
import bodegatienda.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaProductos extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private FirebaseDatabaseController dbController;
    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JComboBox<String> comboOrdenacion;

    public VentanaProductos() {
        setTitle("Gestión de Productos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        dbController = new FirebaseDatabaseController();

        //crear tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Cantidad", "Precio"}, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        //crear el panel de formulario para agregar productos
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        txtNombre = new JTextField();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");

        //crear el JComboBox para seleccionar el modo de ordenación
        comboOrdenacion = new JComboBox<>(new String[] {
            "Ordenar por Nombre", 
            "Ordenar por Precio", 
            "Ordenar por Cantidad"
        });
        comboOrdenacion.setSelectedIndex(0); //valor por defecto ordenar por nombre
        comboOrdenacion.addActionListener(e -> cargarProductosAsync()); //cargar productos con nuevo modo de ordenación

        //crear un panel para el JComboBox
        JPanel panelOrdenacion = new JPanel();
        panelOrdenacion.add(new JLabel("Ordenar por:"));
        panelOrdenacion.add(comboOrdenacion);

        //crear un panel para los botones de agregar y editar
        JPanel panelBotones = new JPanel();
        JButton btnEditar = new JButton("Editar Producto");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        //crear un panel para los componentes en el norte
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BorderLayout());
        panelNorte.add(panelOrdenacion, BorderLayout.NORTH); // Panel con el combo para ordenar
        panelNorte.add(panelBotones, BorderLayout.SOUTH); // Panel con los botones (agregar y editar) al sur

        //agregar componentes al panelFormulario
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);
        panelFormulario.add(btnAgregar);

        //organizar los componentes en un diseño BorderLayout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH); // Panel norte con el combo box y los botones
        add(panelFormulario, BorderLayout.SOUTH); // Formulario en la parte inferior

        //acción de agregar producto
        btnAgregar.addActionListener(e -> agregarProducto());

        //acción de editar producto
        btnEditar.addActionListener(e -> editarProducto());
        
        //acción de eliminar producto
        btnEliminar.addActionListener(e -> eliminarProducto());

        //cargar productos al iniciar
        cargarProductosAsync();
    }

    private void agregarProducto() {
        String nombre = txtNombre.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            Producto nuevoProducto = new Producto(null, nombre, cantidad, precio);
            dbController.guardarProducto(nuevoProducto);       //llama al metodo guardarProducto() de la clase FirebaseDatabaseController

            JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            txtNombre.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");

            cargarProductosAsync();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarProducto() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) modeloTabla.getValueAt(selectedRow, 0);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                dbController.eliminarProducto(id);
                cargarProductosAsync();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla.");
        }
    }

    private void editarProducto() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) modeloTabla.getValueAt(selectedRow, 0);
            Producto productoSeleccionado = dbController.obtenerProductoPorId(id);  //llama al metodo obtenerProductoPorId(id) de FirebaseDatabaseController
            if (productoSeleccionado != null) {
                new VentanaEditarProducto(productoSeleccionado).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla.");
        }
    }

    private void cargarProductosAsync() {
        SwingWorker<java.util.List<Producto>, Void> worker = new SwingWorker<>() {
            @Override
            protected java.util.List<Producto> doInBackground() {
                //obtener los productos y ordenar según la selección del JComboBox
                java.util.List<Producto> productos = dbController.obtenerProductos();
                String criterio = (String) comboOrdenacion.getSelectedItem();
                switch (criterio) {
                    case "Ordenar por Nombre":
                        productos.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));
                        break;
                    case "Ordenar por Precio":
                        productos.sort((p1, p2) -> Double.compare(p1.getPrecio(), p2.getPrecio()));
                        break;
                    case "Ordenar por Cantidad":
                        productos.sort((p1, p2) -> Integer.compare(p1.getCantidad(), p2.getCantidad()));
                        break;
                    default:
                        break;
                }
                return productos;
            }

            @Override
            protected void done() {
                try {
                    java.util.List<Producto> productos = get();

                    //limpiar la tabla antes de cargar los nuevos datos
                    modeloTabla.setRowCount(0);

                    //llenar la tabla con los productos
                    for (Producto p : productos) {
                        modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getCantidad(), p.getPrecio()});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
