package bookstore.exceptions;

public class CannotAddOrderException extends Exception{
  public CannotAddOrderException(String message){
    super(message);
  }
}
