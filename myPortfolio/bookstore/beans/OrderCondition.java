package bookstore.beans;
import java.util.Calendar;
/*注文条件を表すクラス。   作成者：志子田
・自 注文日時 : Order#datetime にセットする
・至 注文日時 : OrderCondition#enddate にセットする
上記以外の注文条件は Order から継承した項目をそのまま使用する。*/

public class OrderCondition extends Order{
	private Calendar enddate;   //注文条件の至注文日時。
	
	public OrderCondition(){
	}
	public void setEnddate(Calendar enddate){
	/*至注文日時に値をセットする [引数]セットする至注文日時*/
		this.enddate = enddate;
	}
	
	public Calendar getEnddate(){
	/*至注文日時の値を得る [戻値]至注文日時の値 */
		return enddate;
	}
}
