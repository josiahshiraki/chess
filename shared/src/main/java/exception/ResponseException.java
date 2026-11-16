package exception;

public class ResponseException extends RuntimeException {
    private final int status;
    public ResponseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatusCode() {
        return status;
    }
}
