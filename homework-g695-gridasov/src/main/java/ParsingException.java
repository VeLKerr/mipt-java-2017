/**
 * Created by ilya on 02.10.17.
 */
public class ParsingException extends RuntimeException {
    private String message;
    ParsingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
