package excepciones;

public class ServicioNoExistenteException extends RuntimeException {
    public ServicioNoExistenteException(String message) {
        super(message);
    }
}
