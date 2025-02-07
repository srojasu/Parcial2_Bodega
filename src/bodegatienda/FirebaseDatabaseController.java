package bodegatienda;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FirebaseDatabaseController {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference productosRef;   //es la referencia al nodo "productos" dentro de la base de datos, donde se almacenan los productos

    public FirebaseDatabaseController() {
        FirebaseConfig.initFirebase();        //para asegurarse de que Firebase está inicializado
        firebaseDatabase = FirebaseConfig.getDatabase();
        productosRef = firebaseDatabase.getReference("productos");  //establece productosRef para apuntar al nodo productos en la base de datos
    }

    public void guardarProducto(Producto producto) {
        String key = productosRef.push().getKey();   //genera un ID único
        producto.setId(key);

        productosRef.child(key).setValue(producto, (databaseError, databaseReference) -> {
            if (databaseError == null) {
                System.out.println("Producto guardado con éxito!");
            } else {
                System.out.println("Error al guardar: " + databaseError.getMessage());
            }
        });
    }

    public void actualizarProducto(String id, Producto productoActualizado) {
        /*el HashMap contiene los nombres de las propiedades del producto (nombre, cantidad, precio)
        como claves, y los nuevos valores que quieres asignar a esas propiedades como valores*/
        productosRef.child(id).updateChildren(new HashMap<String, Object>() {{  //usa updateChildren() para cambiar solo los campos modificados
            put("nombre", productoActualizado.getNombre());
            put("cantidad", productoActualizado.getCantidad());
            put("precio", productoActualizado.getPrecio());
        }}, (error, ref) -> {
            if (error == null) {
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("Error al actualizar: " + error.getMessage());
            }
        });
    }

    public void eliminarProducto(String id) {
        productosRef.child(id).removeValue((error, ref) -> {
            if (error == null) {
                System.out.println("Producto eliminado correctamente.");
            } else {
                System.out.println("Error al eliminar: " + error.getMessage());
            }
        });
    }

    public List<Producto> obtenerProductos() {             //recupera todos los productos de Firebase en una lista
        List<Producto> listaProductos = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Producto producto = child.getValue(Producto.class);
                    producto.setId(child.getKey());
                    listaProductos.add(producto);
                }
                Collections.sort(listaProductos, Comparator.comparing(Producto::getNombre));
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error al obtener productos: " + error.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
        }

        return listaProductos;
    }

    public Producto obtenerProductoPorId(String id) {
        final Producto[] producto = new Producto[1];
        CountDownLatch latch = new CountDownLatch(1);

        productosRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                producto[0] = snapshot.getValue(Producto.class);
                if (producto[0] != null) {
                    producto[0].setId(snapshot.getKey());
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error al obtener producto: " + error.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
        }

        return producto[0];
    }
}
