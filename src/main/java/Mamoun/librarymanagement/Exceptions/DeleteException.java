package Mamoun.librarymanagement.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)

public class DeleteException extends RuntimeException {
    public DeleteException(String Message){
        super(Message);
    }

}
