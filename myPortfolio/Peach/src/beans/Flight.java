package beans;
//作成者：志子田
public class Flight {
    private String flightname;//検索する空港名
    private String flightID;//便名
    private String departure;//出発地
    private String destination; //到着地
    private String openTime; //出発時間
    private String closeTime; //到着時間

    //各セッター
    public void setFlightname(String flightname){
        this.flightname = flightname;
    }
    public void setFlightID(String flightID){
        this.flightID = flightID;
    }
    public void setDeparture(String departure){
        this.departure = departure;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }
    public void setOpenTime(String openTime){
        this.openTime = openTime;
    }
    public void setCloseTime(String closeTime){
        this.closeTime = closeTime;
    }

    //各ゲッター
    public String getFlightname(){
        return flightname;
    }
    public String getFlightID(){
        return flightID;
    }
    public String getDeparture(){
        return departure;
    }
    public String getDestination(){
        return destination;
    }
    public String getOpenTime(){
        return openTime;
    }
    public String getCloseTime(){
        return closeTime;
    }
}
