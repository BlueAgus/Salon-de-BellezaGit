package excepciones;

public class FacturaYaExistenteException extends Exception {
    public FacturaYaExistenteException(String message) {
        super(message);
    }
}
