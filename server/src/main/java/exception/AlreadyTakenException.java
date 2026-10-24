package exception;

public class AlreadyTakenException extends RuntimeException{

    //constructor
    public AlreadyTakenException(String message){
        super(message);
    }
}
