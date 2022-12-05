package bookstore.exceptions;

public class EmptyItemsConditionException extends Exception{
  public EmptyItemsConditionException(String message){
    super(message);
  }
}
