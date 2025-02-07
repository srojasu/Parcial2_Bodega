package bodegatienda;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//Configura la conexión con Firebase usando las credenciales de un archivo JSON
public class FirebaseConfig {
    private static FirebaseDatabase firebaseDatabase;

    public static void initFirebase() {
        if (firebaseDatabase == null) {
            try {
                FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                        .setDatabaseUrl("https://bodega-ab712-default-rtdb.firebaseio.com/")
                        .setServiceAccount(new FileInputStream(new File("C:\\Users\\admin\\Downloads\\bodega-ab712-firebase-adminsdk-fbsvc-039bbee5b9.json")))
                        .build();

                FirebaseApp.initializeApp(firebaseOptions);
                firebaseDatabase = FirebaseDatabase.getInstance();
                System.out.println("Conexión exitosa con Firebase...");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static FirebaseDatabase getDatabase() {
        return firebaseDatabase;
    }
}
