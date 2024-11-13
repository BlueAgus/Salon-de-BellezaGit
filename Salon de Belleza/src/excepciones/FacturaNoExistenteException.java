package excepciones;

public class FacturaNoExistenteException extends Exception {
    public FacturaNoExistenteException(String message) {
        super(message);
    }
}
