package bookstore.beans;
import java.util.Calendar;
/*
-----チーム名：bookstore     作成者：志子田-----
コンパイル用 javac bookstore\beans\Order.java
実行用 java bookstore.beans.Order
*/
public class Order{
	private int no;					//注文番号。仕様は「4.2.データ形式詳細」を参照。
	private Calendar datetime; 		//注文日時。仕様は「4.2.データ形式詳細」を参照。
	private Item item; 				//注文する商品の Item オブジェクト。
	private int quantity; 			//数量。仕様は「4.2.データ形式詳細」を参照。 
	private String sei; 			//購入者姓。仕様は「4.2.データ形式詳細」を参照。 
	private String mei; 			//購入者名。仕様は「4.2.データ形式詳細」を参照。 
	private String pref; 			//購入者住所都道府県。「仕様は 4.2.データ形式詳細」を参照。 
	private String add; 			//購入者住所都道府県以降。仕様は「4.2.データ形式詳細」を参照。 
	private String tel; 			//購入者電話番号。仕様は「4.2.データ形式詳細」を参照。 
	private String mail; 			//購入者メールアドレス。「仕様は 4.2.データ形式詳細」を参照。

	public Order() {
	}

	public void setNo(int no) {
	/*注文番号に値をセットする [引数]セットする注文番号*/
		this.no = no;
	}
	
	public void setDatetime(Calendar datetime) {
	/*注文日時に値をセットする [引数]セットする注文日時*/
		this.datetime=datetime;
	}
	
	public void setItem(Item item) {
	/*注文する商品の Item インスタンスをセットする [引数]セットする Item インスタンス*/
		this.item = item;   //？？
	}
	
	public void setQuantity(int quantity) {
	/*注文数量の値をセットする [引数]セットする数量*/
		this.quantity=quantity;
	}

	public void setSei(String sei) {
	/*購入者姓の値をセットする [引数]セットする購入者姓*/
		this.sei=sei;
	}

	public void setMei(String mei) {
	/*購入者名の値をセットする[引数]セットする購入者名*/
		this.mei=mei;
	}

	public void setPref(String pref) {
	/*購入者住所都道府県の値をセットする [引数]セットする購入者住所都道府県*/
		this.pref=pref;
	}
	
	public void setAdd(String add) {
	/*購入者住所都道府県以降の値をセットする
	[引数]セットする購入者住所都道府県以降*/
		this.add=add;
	}
	
	public void setTel(String tel) {
	/*購入者電話番号の値をセットする
	[引数]セットする購入者電話番号*/
		this.tel=tel;
	}
	
	public void setMail(String mail) {
	/*購入者メールアドレスの値をセットする
	[引数]セットする購入者メールアドレス*/	
		this.mail=mail;
	}
	
	public int getNo() {
	/*注文番号の値を得る [戻値]注文番号の値*/	
		return no;
	}
	
	public Calendar getDatetime() {
	/*注文日時の値を得る [戻値]注文日時の値*/
		return datetime;
	}
	
	public Item getItem() {
	/*注文商品の Item インスタンスを得る
	[戻値]注文商品の Item インスタンス*/
		return item;
	}
	
	public int getQuantity() {
	/*注文数量の値を得る [戻値]注文数量の値*/
		return quantity;
	}
	
	public String getSei() {
	/*購入者姓の値を得る [戻値]購入者姓の値*/
		return sei;
	}
	
	public String getMei() {
	/*購入者名の値を得る [戻値]購入者名の値*/
		return mei;
	}
	
	public String getPref() {
	/*購入者住所都道府県を得る [戻値]購入者住所都道府県の値*/
		return pref;
	}
	
	public String getAdd() {
	/*	購入者住所都道府県以降を得る 
		[戻値]購入者住所都道府県以降*/
		return add;
	}
	
	public String getTel() {
	/*購入者電話番号を得る [戻値]購入者電話番号*/
		return tel;
	}
	
	public String getMail() {
	/*購入者メールアドレス得る [戻値]購入者メールアドレス*/
		return mail;
	
	}
}