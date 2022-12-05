//作成者:飯富

package bookstore.model;

import java.util.ArrayList;
import bookstore.beans.Item;
import bookstore.dao.ItemDataAccessor;
import bookstore.exceptions.EmptyItemsConditionException;
import bookstore.exceptions.NoItemsException;
import bookstore.exceptions.CannotSearchItemsException;


public class ItemsManager{  //商品情報を扱うクラス
	//フィールド : なし
	//コンストラクタ : デフォルトコンストラクタ
	public ItemsManager(){
	}
	//メソッド
	public ArrayList<Item> search(Item key) throws EmptyItemsConditionException,
	NoItemsException, CannotSearchItemsException{

		try{
			if(key.getCode() == null && key.getName() == null || key.getCode() == null && key.getName() == ""  ){  //検索条件の商品コードと商品名のいずれもnullまたは空文字だった場合
			
				throw new EmptyItemsConditionException("商品検索結果が空白です");
			}
			
			ItemDataAccessor da  = new ItemDataAccessor();
			ArrayList<Item> list = new ArrayList<>();
			
			try{
				list = da.select(key);
				if(list.size() == 0){  //検索した結果、1件もヒットしなかった場合
					throw new NoItemsException("商品が見つかりませんでした");
				}
				
			}catch(NoItemsException e){
			 throw e;
			
			}catch(CannotSearchItemsException e){
			 throw e;
			
		}
		
		return list;
		
		}catch(EmptyItemsConditionException e){ //何かしらの理由で商品検索出来なかった場合
		
		 throw e;
		}
	}
}

