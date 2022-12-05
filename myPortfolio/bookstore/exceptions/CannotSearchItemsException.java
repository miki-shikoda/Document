package bookstore.exceptions;

public class CannotSearchItemsException extends Exception{
  public CannotSearchItemsException(String message){
    super(message);
  }
}
