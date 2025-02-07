# Parcial2_Bodega
Para este ejercicio se solicito crear un simulador de las operaciones relacionadas a una bodega (CRUD), que cuente con interfaz grafica y que la informacion persista en el tiempo, en este caso use Firebase.(El proyecto se encuentra en la branch master).

Para el proyecto cree 8 clases diferentes con diferentes funciones:
1. Main:<br/>
   Donde se inicializa Firebase y se abre la ventana de inicio de sesión (VentanaLogin).<br/>
   Utiliza FirebaseConfig.initFirebase() para establecer la conexión con Firebase antes de mostrar la interfaz gráfica.
2. FirebaseConfig:<br/>
   Esta clase configura la conexión con Firebase usando las credenciales de un archivo JSON.
   El método initFirebase() inicializa Firebase.
3. Producto:<br/>
   Representa los productos en la bodega.<br/>
   Tiene atributos como id, nombre, cantidad y precio, junto con sus respectivos métodos getter y setter.
4. FirebaseDatabaseController:<br/>
  Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los productos en Firebase.<br/>
  guardarProducto(): Añade un producto a la base de datos.<br/>
  actualizarProducto(): Actualiza un producto en la base de datos usando su id.<br/>
  eliminarProducto(): Elimina un producto de la base de datos.<br/>
  obtenerProductos(): Recupera todos los productos de Firebase y los devuelve ordenados por nombre.<br/>
  obtenerProductoPorId(): Recupera un producto específico utilizando su id.
5. VentanaLogin:<br/>
  Permite al usuario iniciar sesión utilizando Firebase Authentication.<br/>
  Realiza una autenticación de usuario con correo y contraseña a través de la API REST de Firebase.<br/>
  Si el login es exitoso, se muestra la ventana principal.
6. VentanaPrincipal:<br/>
  Muestra un botón que abre la ventana de administración de productos (VentanaProductos).
7. VentanaProductos:<br/>
  Permite gestionar los productos: agregar, editar, eliminar y ordenar.<br/>
  Los productos se muestran en una tabla (JTable), con botones para agregar, editar y eliminar productos.<br/>
  La lista de productos se puede ordenar por nombre, precio o cantidad usando un JComboBox.<br/>
  Al agregar un producto, se valida que los campos estén completos y que los valores numéricos sean correctos.
8. VentanaEditarProducto:<br/>
  Muestra los detalles de un producto seleccionado para permitir su edición.<br/>
  Los campos se rellenan con los datos del producto seleccionado.<br/>
  Permite actualizar el producto en Firebase.

El programa se ve de la siguiente manera:<br/>
Primero tenemos la ventana para logearnos, en este caso solo añadi un usuario en firebase, seria el unico trabajador de la bodega osea yo.<br/>
![image](https://github.com/user-attachments/assets/42c29a29-9cec-4f79-8506-4a950c80265b)<br/>
Luego de logearse exitosamente iremos a la ventana principal, en esta tenemos la opcion de ir a administrar la bodega.<br/>
![image](https://github.com/user-attachments/assets/802ae546-dce3-4c41-9667-7c976ee1fb35)<br/>
Al darle a administrar iremos al gestor de productos, en este podremos agregar un producto en la parte de abajo, eliminar uno o editarlo, y tambien la lista de productos los podemos ver organizados por su nombre, por su precio, o por su cantidad.<br/>
![image](https://github.com/user-attachments/assets/66516814-ffa7-456f-b173-15f028aa3935)<br/>
En este caso si arriba elejimos que se organicen por su cantidad la lista se actualizara.<br/>
![image](https://github.com/user-attachments/assets/f40cf60c-ecb1-46fa-85d1-691908515af3)<br/>
Ahora si elejmos un producto y selecionamos editar producto iremos a otra ventana donde podemos editar cualquier parametro del producto, en este caso le vamos a subir el precio a la cerveza.<br/>
![image](https://github.com/user-attachments/assets/2bd3bc59-c8af-4892-94dd-06aea44404fb)<br/>
Ya en la lista de productos vamos a ver el precio actualizado.<br/>
![image](https://github.com/user-attachments/assets/cf302cf9-66a3-4f3a-b0d2-094dbdb7ee15)<br/>
Y por ultimo si se quiere eliminar un producto se selecciona y se le da al boton de eliminar, en este caso eliminaremos la cerveza porque estaba muy cara.<br/>
![image](https://github.com/user-attachments/assets/ffb2f7e3-ffe8-46ff-b0af-d850c6f946aa)<br/>
Todos estos cambios se veran reflejados en Firebase.<br/>
![image](https://github.com/user-attachments/assets/6ff17157-4cdd-4d05-ac36-0676c9b8b964)







