package makeQuestion.bean;

/*
  時間記録
*/
public class Record{
    private double time;  //記録時間
    private String name;  //名前
    
    //コンストラクタ
    public Record(double time, String name){
        setTime(time);
        setName(name);
    }
    
    //セッター&ゲッター
    public void setTime(double time){
        this.time = time;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public double getTime(){
        return time;
    }
    
    public String getName(){
        return name;
    }
}
