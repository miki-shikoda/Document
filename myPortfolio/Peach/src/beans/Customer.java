
package beans;

public class Customer{
    private int guestID = 0;
    private String sei;
    private String mei;
    private String seikana;
    private String meikana;
    private Address addID;
    private String tel;
    private String mobile;

//------各セッター
    public void setGuestID(int guestID){
        this.guestID = guestID;
    }
    
    public void setSei(String sei){
        this.sei = sei;
    }
    
    public void setMei(String mei){
        this.mei = mei;
    }

    public void setSeikana(String seikana){
        this.seikana = seikana;
    }

    public void setMeikana(String meikana){
        this.meikana = meikana;
    }

    public void setAddID(Address addID){
        this.addID = addID;
    }

    public void setTel(String tel){
        this.tel = tel;
    }

    public void setMobile(String mobile){
        this.mobile = mobile;
    }
//------各ゲッター
    
    public int getGuestID(){
        return guestID;
    }
    
    public String getSei(){
        return sei;
    }
    
    public String getMei(){
        return mei;
    }
    public String getSeikana(){
        return seikana;
    }
    public String getMeikana(){
        return meikana;
    }
    public Address getAddID(){
        return addID;
    }
    public String getTel(){
        return tel;
    }
    public String getMobile(){
        return mobile;
    }
}
