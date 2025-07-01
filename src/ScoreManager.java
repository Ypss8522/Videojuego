/**
 * Esta clase es la que gestiona el puntaje de la aplicación utilizando el patrón Singleton.
 * La clase `ScoreManager` asegura que solo exista una instancia de la gestión de puntajes en toda la aplicación. 
 * Proporciona métodos para obtener el puntaje actual, aumentar el puntaje y restablecerlo a cero.
 */
public class ScoreManager {
    private static ScoreManager instance; // Instancia única de la clase
    private int score; // Puntaje actual

    /**
     * Constructor privado para evitar la creación directa de instancias.
     * Inicializa el puntaje a cero.
     */
    private ScoreManager() {
        score = 0;
    }

    /**
     * Obtiene la instancia única de la clase `ScoreManager`.
     * Si la instancia aún no ha sido creada, se crea una nueva instancia. Este método es sincronizado para asegurar que solo
     * se cree una instancia en un entorno multihilo.
     */
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    /**
     * Obtiene el puntaje actual.
     * Proporciona acceso al puntaje actual gestionado por esta instancia de `ScoreManager`.
     */
    public int getScore() {
        return score;
    }

    /**
     * Aumenta el puntaje actual en la cantidad especificada.
     * Suma el número de puntos proporcionado al puntaje actual.
     */
    public void increaseScore(int points) {
        score += points;
    }

    /**
     * Restablece el puntaje a cero.
     * Esta acción pone el puntaje actual en cero, reseteando el estado del puntaje.
     */
    public void resetScore() {
        score = 0;
    }
}