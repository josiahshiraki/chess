package exception;

public class ResponseException extends RuntimeException {
    private final int status;
    public ResponseException(String message, int status) {
        this.status = status;
        super(message);
    }

    public int getStatusCode() {
        return status;
    }
}
