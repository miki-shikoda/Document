package bookstore.beans;

public class Item{
  private String code;  //���i�R�[�h(VS)
  private String name;  //���i��
  private int price;    //�P��
  
  public Item(String code, String name, int price){
    setCode(code);
    setName(name);
    setPrice(price);
  }
    
  public void setCode(String code){
    this.code = code;
  }
  
  public void setName(String name){
    this.name = name;
  }
  
  public void setPrice(int price){
    this.price = price;
  }
  
  public String getCode(){
    return code;
  }
  
  public String getName(){
    return name;
  }
  
  public int getPrice(){
    return price;
  }
}
