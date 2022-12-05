package bookstore.dao;
//作成者：志子田
//コンパイル用 javac bookstore\dao\OrderDataAccessor.java
//実行用 java bookstore.dao.OrderDataAccessor
import bookstore.beans.Item;
import bookstore.beans.Order;
import bookstore.beans.OrderCondition;
import bookstore.exceptions.*;
//登録で必要
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
//検索で必要
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class OrderDataAccessor{
/*	---注文情報追加登録---
登録時に当該注文の注文番号を発行(※既存の注文番号の最大値に+1 した番号)
[引数]追加登録する注文の情報[戻値]当該注文で発行した注文番号。
[例外]CannotAddOrderException : 何らかの理由で注文登録できなかった場合。
・発行した注文番号は、戻値で返すとともに、引数 newOrder のインスタンスにもセットする
------------------------------------------------------------*/	
	public int insert(Order newOrder) throws CannotAddOrderException{
		//注文番号をnewOrderの注文番号へセットする
		int num = num();
		//追加登録対象のデータをゲットする（Calendar→Date→String）
		Calendar orderdate = newOrder.getDatetime(); 	//カレンダー型へ引数の注文日時ゲットする。orderdate
		Date d = new Date();
		d = orderdate.getTime();					//ゲットした注文日時を日付型へ変更
    	String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
		
		Item item = newOrder.getItem(); 				//一旦、newOrderのItemインスタンスを取得
		String code = item.getCode();					//それから、itemCodeを取得
		int quantity = newOrder.getQuantity(); 			//数量。quantity 
		String sei = newOrder.getSei(); 				//購入者姓。sei
		String mei = newOrder.getMei(); 				//購入者名。mei
		String pref = newOrder.getPref(); 				//購入者住所都道府県 pref
		String add = newOrder.getAdd(); 				//購入者住所都道府県以降 address 
		String tel = newOrder.getTel(); 				//購入者電話番号 tel 
		String mail = newOrder.getMail(); 				//購入者メールアドレス mail
		
		//JDBCドライバをロードする
		Connection conn = null;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			try{
				conn.close();
            }catch(SQLException e2){
                throw new CannotAddOrderException("");
            }
            throw new CannotAddOrderException("");
		}
		//データベースへの接続
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass); 
		}catch (SQLException e){
			try{
				conn.close();
            }catch(SQLException e2){
                throw new CannotAddOrderException("");
            }
			throw new CannotAddOrderException("");
		}
		
		//SQLを準備
		PreparedStatement ps = null;
		ps = null;//INSERT INTO orders(no,orderdate,itemCode,quantity,sei,mei,pref,address,tel,mail) VALUES(1,'2022-5-18 13:35:22','P001',1,'鈴木','宏','宮城県','仙台市青葉区','022-222-2222',null);
		try{
			String sql = "INSERT INTO orders(no,orderdate,itemCode,quantity,sei,mei,pref,address,tel,mail) VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1,++num);
			ps.setString(2,str);
			ps.setString(3,code);
			ps.setInt(4,quantity);
			ps.setString(5,sei);
			ps.setString(6,mei);
			ps.setString(7,pref);
			ps.setString(8,add);
			ps.setString(9,tel);
			ps.setString(10,mail);
			
		}catch (SQLException e){
			throw new CannotAddOrderException("");
		}
		
		//SQL結果
		try{
			ps.executeUpdate();
		}catch (SQLException e){
			throw new CannotAddOrderException("");
		}finally{
			try{
				conn.close();
			}catch (SQLException e2){
				throw new CannotAddOrderException("");
			}
			newOrder.setNo(num);			
			return num;
		}
	}
	
/*	---注文情報検索---
[引数]注文の検索条件。 [戻値]条件を満たした注文の Order インスタンスのリスト。
[例外]CannotSearchOrdersException : 何らかの理由で注文検索できなかった場合。
・注文番号、電話番号は完全一致検索  /  ・氏名、商品名は部分一致検索
・氏名の条件は、姓あるいは名のどちらか一方にセットすれば、注文情報の姓と名の両方で検索される。条件および注文情報のどちらも姓と名を結合し、その結合した文字列で検索する方法が考えられる。
・注文数量、メールアドレス、都道府県および都道府県以降は検索条件として使用しない。
・複数の項目に検索条件が入っているときは、AND 検索(全ての条件を満たす注文だけを抽出)する。
・order.csv から読出した注文情報には、商品コードはあっても商品名はない。
したがって、ItemDataAccessor#select()を利用して、商品コードから該当する 
Item インスタンスを得る必要がある
------------------------------------------------------------*/
	
	public ArrayList<Order> select(OrderCondition key)throws CannotSearchOrdersException{
		ArrayList<Order> item = null;
		
		//検索キーワード初期値 (=データベースordersに登録されている項目と一致する内容)
		int no = key.getNo();					//注文番号完全一致 no = ?
		
		//注文日時(至) をカレンダー型からdate型へ変更
		Calendar orderdate = key.getDatetime();
		String date1 = null;
		Date orderDate = null;
		if(orderdate != null){
			orderDate = new Date();
			orderDate = orderdate.getTime();
	    	date1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(orderDate);//hh:mm:ss
		}
		
		//注文条件の至注文日時。
		Calendar enddate = key.getEnddate();
		String date2 = null;
		Date orderEnd = null;
		if(enddate != null){
			orderEnd = new Date();
			orderEnd = enddate.getTime();
	    	date2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(orderEnd);//hh:mm:ss
	    	
		}else if(orderdate != null && enddate == null){ //orderdateが日付指定あり、enddateがnullの場合
			orderEnd = new Date();
			orderEnd = orderdate.getTime();
	    	date2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(orderEnd);//hh:mm:ss
	    	//orderdate がnullではない場合、検索のときに"yyyy-MM-dd 23:59:59"で検索しないといけない
		}
		
		String code = null;						// 商品コード (検索値に使わない)
		int quantity = 0; 					// 数量  (検索値に使わない)
		String sei = key.getSei(); 				//購入者姓部分一致 sei LIKE ?
		String mei = key.getMei(); 				//購入者名部分一致 mei LIKE ?
		String pref = null; 					// 購入者住所都道府県   (検索値に使わない)
		String add = null; 						// 購入者住所都道府県以降   (検索値に使わない)
		String tel  = key.getTel(); 			//購入者電話番号完全一致 tel = ?
		String mail = null;						// 購入者アドレス (検索値に使わない)
		//検索値だけに使う
		Item searchitem = key.getItem();		//商品名部分一致 LIKE ?
		String name = null;
		if(searchitem != null){
			name = searchitem.getName();
		}
		//最後にArrayListにaddするときに使う
		int price = 0;    //単価
		
	//ドライバロード
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			//System.out.println("EE1");
			throw new CannotSearchOrdersException("");
		}
	//データベースへの接続
		Connection conn = null;
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass);
		}catch (SQLException e){
			//System.out.println("EE2");
			throw new CannotSearchOrdersException("");
		}
		//SQLを準備
		PreparedStatement ps = null;
		String sql = "";
		try{ 
			//注文番号だけが入力されている場合
			if( no > 0 && ( sei == null || sei == "") && ( tel == null || tel == "") && ( name == null || name == "") && date1 == null && date2 == null ){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE no = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,no);
				//System.out.println("注文番号だけで検索実行");
				
			//検索する注文日だけが入力されている
			}else if( date1 != null && date2 != null && ( no == 0 ) && ( sei == null || sei == "") && ( tel == null || tel == "") && ( name == null || name == "") ){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE (orderdate >= ?) AND (orderdate <= ?)";//BETWEEN ? AND ?
				ps = conn.prepareStatement(sql);
				ps.setString(1,date1);
				ps.setString(2,date2);
				//System.out.println("注文日(自至)だけで検索実行");
				//System.out.println(date1+"|"+date2);
			
			//姓名どちらかが記入されている
			}else if( sei != null &&  no == 0  && date1 == null && date2 == null && tel == null && name == null ){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE sei LIKE ? OR mei LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,"%" + sei + "%");
				ps.setString(2,"%" + mei + "%");
				//System.out.println("姓名だけで検索実行");
				
			//電話番号だけが入力されている場合
			}else if( (tel != "" || tel != null ) && ( no == 0 ) && ( sei == null || sei == "" ) && date1 == null && date2 == null && ( name == null || name == "" )){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE tel = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,tel);
				//System.out.println("電話番号だけで検索実行");
				
			//商品名だけが入力されている場合
			}else if( name != null && ( sei == null || sei == "") && ( tel == null || tel == "") && ( no == 0 ) && date1 == null && date2 == null ){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,"%" + name + "%");
				//System.out.println("商品名だけで検索実行");
				
			}else if( date1 == null && date2 != null && ( sei != null || sei != "") && ( tel == null || tel == "") && ( no == 0 ) && ( name != null || name != "" )){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE orderdate < ? AND sei LIKE ? OR mei LIKE ? AND name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,date2);
				ps.setString(2,"%" + sei + "%");
				ps.setString(3,"%" + mei + "%");
				ps.setString(4,"%" + name + "%");
				//System.out.println("注文日(至)、姓名、商品名で検索実行");
				
			}else if( date1 != null && date2 != null && ( no == 0 ) && ( sei != null || sei != "") && ( tel == null || tel == "") && ( name != null || name != "" )){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE (orderdate >= ?) AND (orderdate <= ?) AND sei LIKE ? OR mei LIKE ? AND name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,date1);
				ps.setString(2,date2);
				ps.setString(3,"%" + sei + "%");
				ps.setString(4,"%" + mei + "%");
				ps.setString(5,"%" + name + "%");
				//System.out.println("注文日、姓名、商品名で検索実行");
				
			}else if( no == 0 && date1 == null && date2 == null &&  ( tel == null || tel == "") && ( sei != null || sei != "") && ( name != null || name != "")){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE sei LIKE ? OR mei LIKE ? AND name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,"%" + sei + "%");
				ps.setString(2,"%" + mei + "%");
				ps.setString(3,"%" + name + "%");
				//System.out.println("購入者と商品名だけで検索実行");
			
			}else if(  date1 != null && date2 != null && ( no != 0 ) &&( sei != null || sei != "") && ( tel != null || tel != "") && name != null ){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE (orderdate BETWEEN ? AND ?) AND ( sei LIKE ? OR mei LIKE ?) AND name LIKE ? AND tel = ? AND no = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,date1);
				ps.setString(2,date2);
				ps.setString(3,"%" + sei + "%");
				ps.setString(4,"%" + mei + "%");
				ps.setString(5,"%" + name + "%");
				ps.setString(6,tel);
				ps.setInt(7,no);
				//System.out.println("全項目で検索実行");
				
			}else{
				System.out.println("Error：OrderDataAccessor");//例外の条件です
			}
		
		}catch (SQLException e){
			try{
				conn.close();   //データベース切断
			}catch (SQLException e2){
				//System.out.println("EE3");
				throw new CannotSearchOrdersException("");
			}
			//System.out.println("EE4");
			throw new CannotSearchOrdersException("");
		}
		//SQL発行
		ResultSet rs = null;
		try{
			conn.setAutoCommit(false); //トランザクション開始(MySQLのBEGINと同じ)
			rs = ps.executeQuery();
			conn.commit();             //コミット(COMMITと同じ)
		}catch (SQLException e){
			try{
				ps.close();
			}catch (SQLException e2){
				//System.out.println("EE5");
				throw new CannotSearchOrdersException("");
			}
			try{
				conn.close();
			}catch (SQLException e2){
				//System.out.println("EE6");
				throw new CannotSearchOrdersException("");
			}
			//System.out.println("EE7");
			//throw new NoOrderException("")はどこでやるのか？
			throw new CannotSearchOrdersException("");
		}
		//結果の取得
		item = new ArrayList<Order>();
		try{
			while(rs.next()){
				//System.out.println("while実行中。");
				no = rs.getInt("no");					//注文番号
				date1 = rs.getString("orderdate"); 		//注文日時
				code = rs.getString("itemCode");		// 商品番号
				quantity = rs.getInt("quantity"); 		// 数量
				
				sei = rs.getString("sei"); 				//購入者姓
				mei = rs.getString("mei"); 				//購入者名
				pref = rs.getString("pref"); 			// 購入者住所都道府県
				add = rs.getString("address"); 			// 購入者住所都道府県以降
				tel = rs.getString("tel"); 				//購入者電話番号
				mail = rs.getString("mail");			// 購入者アドレス
				name = rs.getString("name");				//購入商品名
				price = rs.getInt("price");					//商品金額
				
				//String→date型→Calendar型へ変換
    			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	try{
		    		orderDate = sdFormat.parse(date1);
		    	}catch (ParseException e) {
		    	}
				Calendar cal = Calendar.getInstance();
				cal.setTime(orderDate);
				
				Item additem = new Item(code,name,price);
				Order addorder = new Order();
				addorder.setNo(no);
				addorder.setDatetime(cal);
				addorder.setItem(additem);
				addorder.setQuantity(quantity);
				addorder.setSei(sei);
				addorder.setMei(mei);
				addorder.setPref(pref);
				addorder.setAdd(add);
				addorder.setTel(tel);
				addorder.setMail(mail);
				item.add(addorder);  //ArrayListへadd
				//System.out.println(no+"|"+date1+"|"+code+"|"+quantity+"|"+sei+"|"+mei+"|"+pref+"|"+add+"|"+tel+"|"+mail+"|"+addorder);
			}
		}catch (SQLException e2){
			//System.out.println("EE8");
			throw new CannotSearchOrdersException("");
		}
		//クローズ ??
		try{
			rs.close();
		}catch (SQLException e2){
			//System.out.println("EE9");
			throw new CannotSearchOrdersException("");
		}
		try{
			ps.close();
		}catch (SQLException e2){
			//System.out.println("EE10");
			throw new CannotSearchOrdersException("");
		}
		try{
			conn.close();
		}catch (SQLException e2){
			//System.out.println("EE11");
			throw new CannotSearchOrdersException("");
		}
		//戻値 ArrayList<Order>をリターンする
		return item;
	}
	
	
//-----*-----
	public int num(){
		//JDBCドライバをロードする
		Connection conn = null;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			try{
				conn.close();
            }catch(SQLException e2){
                System.out.println("E01");
            }
            System.out.println("E02");
		}
		//データベースへの接続
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass); 
		}catch (SQLException e){
			try{
				conn.close();
            }catch(SQLException e2){
                System.out.println("E03");
            }
			System.out.println("E04");
		}
		
		//SQLを準備(注文番号最大値を取りたい)
		PreparedStatement ps = null;
		try{
			String sql = "SELECT MAX(no) AS no FROM orders";
			ps = conn.prepareStatement(sql);
		}catch (SQLException e){
			try{
				conn.close();   //データベース切断
			}catch (SQLException e2){
				System.out.println("E05");
			}
			System.out.println("E06");
		}
		//SQL発行
		ResultSet rs = null;
		try{
			conn.setAutoCommit(false); //トランザクション開始(MySQLのBEGINと同じ)
			rs = ps.executeQuery();
			conn.commit();             //コミット(COMMITと同じ)
		}catch (SQLException e){
			try{
				ps.close();
			}catch (SQLException e2){
				System.out.println("E07");
			}
			try{
				conn.close();
			}catch (SQLException e2){
				System.out.println("E08");
			}
			System.out.println("E09");
		}
		int no = 12;
		//注文番号最大値の結果取得
		try{
			while(rs.next()){
				no = rs.getInt("no");
			}
		}catch (SQLException e){
			System.out.println("E10");
		}
		
		//クローズ
		try{
			rs.close();
		}catch (SQLException e2){
			System.out.println("E11");
		}
		try{
			ps.close();
		}catch (SQLException e2){
			System.out.println("E12");
		}
		try{
			conn.close();
		}catch (SQLException e2){
			System.out.println("E13");
		}
		return no;
	}

//-----	
	
	/*public static void main(String[] args){
	
		//検索キーワード初期値 (=データベースordersに登録されている項目と一致する内容)
		int no = 5;								//注文番号完全一致 no = ?
		String sei = ""; 					//購入者姓部分一致 sei LIKE ?
		String mei = ""; 					//購入者名部分一致 mei LIKE ?
		String tel  = "0120-000-000"; 			//購入者電話番号完全一致 tel = ?
		//検索値だけに使う
		String code = null;  		//商品コード
	  	String name = "基礎";  		//商品名
	  	int price = 0;    			//単価
		Item item = new Item(code,name,price);
		OrderCondition test = new OrderCondition();
		
		test.setNo(no);
		test.setItem(item);
		//test.setSei(sei);
		//test.setMei(mei);
		test.setTel(tel);
		
		System.out.println(test.getNo());
		
		OrderDataAccessor testOda = new OrderDataAccessor();
		
		try{
			testOda.select(test);
		}catch (CannotSearchOrdersException e){
			System.out.println("E20");
			return;
		}
	}*/
	
			/*}catch (CannotAddOrderException e){
			System.out.println("E01");
			return;*/
}
