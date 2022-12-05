package bookstore.exceptions;

public class CannotSearchOrdersException extends Exception{
  public CannotSearchOrdersException(String message){
    super(message);
  }
}
