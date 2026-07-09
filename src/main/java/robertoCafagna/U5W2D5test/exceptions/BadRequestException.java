package robertoCafagna.U5W2D5test.exceptions;

import java.util.List;

public class BadRequestException extends RuntimeException {
    private List<String> errorsList;

    public BadRequestException(List<String> errorsList) {
        super("Richieste Errate ");
        this.errorsList = errorsList;
    }

    public BadRequestException(String message) {
        super(message);
    }
}
