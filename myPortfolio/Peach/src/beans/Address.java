//船木

package beans;


public class Address{
    private int addID = 0;
    private String zipcode = null;
    private String address = null;


//----------------------------------------
//デフォルトコンストラクタ
//----------------------------------------



//-----------------------------------------
//セットメソッド
//-----------------------------------------

    public void setAddID(int addID){
        this.addID = addID;
    }
    
    
    public void setZipcode(String zipcode){
        this.zipcode = zipcode;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
//-----------------------------------------------
//ゲットメソッド
//-----------------------------------------------
    
    public int getAddID(){
        return addID;
    }
    
    public String getZipcode(){
        return zipcode;
    }
    
    public String getAddress(){
        return address;
    }

}
