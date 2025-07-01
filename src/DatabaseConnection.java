import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta es la clase que gestiona la conexión a la base de datos utilizando el patrón Singleton.
 * La clase `DatabaseConnection` asegura que solo exista una instancia de la conexión a la base de datos en toda la aplicación.
 * Proporciona un método para obtener la instancia única de la clase y un método para obtener la conexión a la base de datos.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance; // Instancia única de la clase

    // La conexión a la base de datos
    private Connection connection;
    private String url = "jdbc:mysql://bopntoh687tet4w3pzrq-mysql.services.clever-cloud.com:3306/bopntoh687tet4w3pzrq"; // URL de la base de datos
    private String user = "ua7t7fruxl6yoqso"; // Usuario para la conexión
    private String password = "nZdF9JBWd6rT68kmiNZU"; // Contraseña para la conexión

    /**
     * Constructor privado para evitar la creación directa de instancias.
     * Carga el driver JDBC para MySQL y establece la conexión a la base de datos utilizando la URL, usuario y contraseña especificados.
     */
    private DatabaseConnection() {
        try {
            // Cargar el driver de JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión a la base de datos
            this.connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Manejo de excepciones: imprimir el traza del error
        }
    }

    /**
     * Obtiene la instancia única de la clase `DatabaseConnection`.
     * Si la instancia aún no ha sido creada, se crea una nueva instancia. Este método es sincronizado para asegurar que solo
     * se cree una instancia en un entorno multihilo.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtiene la conexión a la base de datos.
     * Proporciona acceso a la conexión establecida en la base de datos para ser utilizada en las operaciones de acceso a datos.
     */
    public Connection getConnection() {
        return this.connection;
    }
}