package bodegatienda.vistas;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMensaje;

    //constante de la API de Firebase
    private static final String FIREBASE_API_KEY = "AIzaSyAnruGKm3el_2BVy6huz4W2WncrVyNdJtI";
    private static final String FIREBASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;

    public VentanaLogin() {
        setTitle("Login Administrativo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //crear componentes
        JLabel lblEmail = new JLabel("Correo electrónico:");
        txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(20);

        btnLogin = new JButton("Iniciar sesión");
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(Color.RED);

        //configurar el diseño del formulario
        setLayout(new GridLayout(4, 2));
        add(lblEmail);
        add(txtEmail);
        add(lblPassword);
        add(txtPassword);
        add(btnLogin);
        add(lblMensaje);

        //acción de iniciar sesión
        btnLogin.addActionListener(e -> autenticarUsuario());
    }

    private void autenticarUsuario() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor ingrese el correo y la contraseña.");
            return;
        }

        //llamar al método para autenticar con la REST API de Firebase
        obtenerTokenFirebase(email, password);
    }

    private void obtenerTokenFirebase(String email, String password) {
        try {
            //construir el cuerpo de la solicitud con las credenciales del usuario
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("email", email);
            jsonInput.put("password", password);
            jsonInput.put("returnSecureToken", true);

            //crear la URL del endpoint de Firebase Authentication
            URL url = new URL(FIREBASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            //escribir los datos en el cuerpo de la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            //leer la respuesta de Firebase
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                //parsear la respuesta JSON
                JSONObject responseJson = new JSONObject(response.toString());
                String idToken = responseJson.getString("idToken");

                //verificar el token y proceder con el login
                if (idToken != null) {
                    lblMensaje.setText("Login exitoso");
                    new VentanaPrincipal().setVisible(true);
                    dispose();
                }
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al autenticar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
