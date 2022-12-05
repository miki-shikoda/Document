//米澤
package beans;

//営業情報集計で使用するクラス
public class ReserveSale extends Reserve{
    private int sale;  //売上情報
    private String frombase;  //出発空港情報
    private String tobase;    //到着空港情報

    public void setSale(int sale){
        this.sale = sale;
    }

    public void setFrombase(String frombase){
        this.frombase = frombase;
    }

    public void setTobase(String tobase){
        this.tobase = tobase;
    }
    public int getSale(){
        return sale;
    }

    public String getFrombase(){
        return frombase;
    }

    public String getTobase(){
        return tobase;
    }
}
