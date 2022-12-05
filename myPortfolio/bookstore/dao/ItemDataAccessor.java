package bookstore.dao;
//コンパイル用 javac bookstore\dao\ItemDataAccessor.java
//実行用 java bookstore.dao.ItemDataAccessor

import bookstore.beans.Item;
import bookstore.exceptions.*;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDataAccessor{
	public ItemDataAccessor(){
	//何もしない
	}

/* ---引数 key に設定された条件に従って商品情報を検索し、条件を満たした商品の一覧を戻す。---
[引数]商品の検索条件。当該フィールドに条件をセットしておく
条件を指定しないフィールドは null または空文字列または 0。

[戻値]条件を満たした商品の Item インスタンスのリスト。
・商品コードおよび商品名による検索を行う。
・検索は、どの項目も部分一致で抽出する。
・複数の項目に検索条件が入っているときは、AND 検索(全ての条件を満たす商品だけを抽出)する。
*/
	public ArrayList<Item> select(Item key) throws CannotSearchItemsException{
		ArrayList<Item> item = new ArrayList<Item>();
		
	//ドライバロード
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}catch (ClassNotFoundException e){
			System.out.println("JDBCドライバが見つかりません。");
		}
			
	//データベースへの接続
		Connection conn = null;
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass);
		}catch (SQLException e){
			throw new CannotSearchItemsException("");
		}

		//SQLを準備
		PreparedStatement ps = null;
		try{
			String sql = "SELECT code, name, price FROM items";
			sql += " WHERE name LIKE ?";    //" WHERE name LIKE ?"
			ps = conn.prepareStatement(sql);
			ps.setString( 1,"%" + key.getName() + "%");
			
		}catch (SQLException e){
			try{
				conn.close();   //データベース切断
			}catch (SQLException e2){
				throw new CannotSearchItemsException("");
			}
			throw new CannotSearchItemsException("");
		}
		
		//SQL発行
		ResultSet rs = null;
		try{
		    conn.setAutoCommit(false);  //トランザクション開始(BEGINと同じ)
            rs = ps.executeQuery();
            conn.commit();              //コミット(COMMITと同じ)
		}catch (SQLException e){
			try{
				conn.close();
			}catch (SQLException e2){
				throw new CannotSearchItemsException("");
			}
			try{
                conn.close();
            }catch(SQLException e3){
                throw new CannotSearchItemsException("");
            }
            throw new CannotSearchItemsException("");
        }
		
		//結果の取得
		try{
			while(rs.next()){
				if(rs.getString("code") != null){
					String code = rs.getString("code");
					String name = rs.getString("name");
					int price = rs.getInt("price");
					Item search = new Item(code,name,price);
					item.add(search); //ArrayListへadd
				}
			}
			return item;
		}catch (SQLException e){
			throw new CannotSearchItemsException("");
		}finally{
			try{
            	rs.close();
        	}catch(SQLException e){
            	throw new CannotSearchItemsException("");
       		}
        	try{
            	ps.close();
        	}catch(SQLException e){
            	throw new CannotSearchItemsException("");
        	}
        	try{
            	conn.close();
        	}catch(SQLException e){
            	throw new CannotSearchItemsException("");
            }
        }   
	}
	
/*----テスト用-----
	public static void main(String[] args)throws CannotSearchItemsException{
		String code = null;  //商品コード
	  	String name;  //商品名
	  	int price = 0;    //単価

		name = "Java";
		
		ItemDataAccessor id = new ItemDataAccessor();
		Item item = new Item(code,name,price);
		id.select(item);
	}*/
}
