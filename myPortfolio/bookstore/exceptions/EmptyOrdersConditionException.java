package bookstore.exceptions;

public class EmptyOrdersConditionException extends Exception{
  public EmptyOrdersConditionException(String message){
    super(message);
  }
}
