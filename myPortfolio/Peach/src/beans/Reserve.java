//船木

package beans;
import java.util.Calendar;
public class Reserve{
    private int reserveID = 0;
    private Calendar appdate = null;
    private Customer guestID = new Customer();     //オブジェクトをセットに変わるかも
    private Calendar flightdate = null;
    private String flightID = null;
    private int remainseats = 0;//検証用で追加
    private Calendar flightEnddate = null;//検証用で追加
    //flightEnddate 運航日（至)がここにくるかも

//-----------------------------------------
//セットメソッド
//-----------------------------------------
    public void setReserveID(int reserveID){
        this.reserveID = reserveID;
    }

    public void setAppdate(Calendar appdate){
        this.appdate = appdate;
    }
    
    public void setGuestID(Customer guestID){
        this.guestID = guestID;
    }
    
    public void setFlightdate(Calendar flightdate){
        this.flightdate = flightdate;
    }
    
    public void setFlightID(String flightID){
        this.flightID = flightID;
    }
    
    //検証用の追加
    public void setRemainseats(int remainseats){
        this.remainseats = remainseats;
    }
    public void setFlightEnddate(Calendar flightEnddate){
        this.flightEnddate = flightEnddate;
    }
//-----------------------------------------------
//ゲットメソッド
//-----------------------------------------------
    
    public int getReserveID(){
        return reserveID;
    }
    
    public Calendar getAppdate(){
        return appdate;
    }
    
    public Customer getGuestID(){
        return guestID;
    }
    
    public Calendar getFlightdate(){
        return flightdate;
    }

    public String getFlightID(){
        return flightID;
    }
    //検証用の追加
    public int getRemainseats(){
        return remainseats;
    }
    public Calendar getFlightEnddate(){
        return flightEnddate;
    }


}

