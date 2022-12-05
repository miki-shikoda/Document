package bookstore.model;

import java.util.ArrayList;
import java.util.Calendar;
import bookstore.beans.Item;
import bookstore.beans.Order;
import bookstore.beans.OrderCondition;
import bookstore.dao.OrderDataAccessor;
import bookstore.model.ItemsManager;
import bookstore.exceptions.CannotAddOrderException;
import bookstore.exceptions.CannotSearchOrdersException;
import bookstore.exceptions.CannotSearchItemsException;
import bookstore.exceptions.EmptyNameException;
import bookstore.exceptions.EmptyAddException;
import bookstore.exceptions.EmptyOrdersConditionException;
import bookstore.exceptions.EmptyItemsConditionException;
import bookstore.exceptions.IllegalItemNoException;
import bookstore.exceptions.IllegalQuantityException;
import bookstore.exceptions.NoOrderException;
import bookstore.exceptions.NoItemsException;


public class OrdersManager{  //注文情報を扱うクラス
	//フィールドなし
	
    OrderDataAccessor oda = new OrderDataAccessor();
	//コンストラクタ : デフォルトコンストラクタ
    public OrdersManager(){
    }
	//メソッド
    public int add(Order newOrder) 
    throws IllegalItemNoException, IllegalQuantityException, EmptyNameException, 
    EmptyAddException, CannotAddOrderException,EmptyItemsConditionException{


    	try{ 
            ItemsManager im = new ItemsManager();
            
            ArrayList list = im.search(newOrder.getItem());
            if(list.get(0) == null){
                throw new IllegalItemNoException("商品番号が存在しません");
            }
            int num = newOrder.getQuantity();
            if(num <= 0){
                throw new IllegalQuantityException("数量が不正です");
            }
            if(newOrder.getSei() == null || newOrder.getMei() == null || newOrder.getMei() == "" || newOrder.getSei() == ""){
                throw new EmptyNameException("購入者氏名(姓または名)が空の場合");
            }
            if(newOrder.getPref() == null || newOrder.getAdd() == null || newOrder.getAdd() == "" || newOrder.getPref() == ""){
                throw new EmptyAddException("購入者住所(都道府県またはそれ以降)が空の場合");
            }
            newOrder.setDatetime(Calendar.getInstance());
            
            int order = 0;
            try{
                
                order = oda.insert(newOrder);    //何らかの理由で注文登録できなかった場合
            }catch(CannotAddOrderException e){
                 throw e;
            }
            newOrder.setNo(order);
            return order;
            
              
        }catch(IllegalItemNoException e){  //システムに存在しない商品番号
         throw e;
        
        }catch(IllegalQuantityException e){  //残量が不正な場合
         throw e; 
        
        }catch(EmptyNameException e){  //購入者氏名が空
         throw e;
        
        }catch(EmptyAddException e){  //住所が空の場合
         throw e;
         
        }catch(EmptyItemsConditionException | NoItemsException | CannotSearchItemsException e){
         throw new IllegalItemNoException("商品コード不正");
        }
         
    }
    public ArrayList<Order> search(OrderCondition key) throws EmptyOrdersConditionException,
    NoOrderException,CannotSearchOrdersException{
        ArrayList<Order> list = new ArrayList<>();

        try{  
            if(key.getNo() == 0 && key.getDatetime() == null && key.getItem() == null 
            && key.getSei() == null && key.getMei() == null && key.getTel() == null){  //検索条件がひとつも入力されていない場合
            throw new EmptyOrdersConditionException("注文検索条件が空");
            }
                
            OrderDataAccessor da = new OrderDataAccessor();
            try{
                list = da.select(key);
                if(list.size() == 0){ //検索した結果、1 件もヒットしなかった場合
                 throw new  NoOrderException("商品検索結果なし");
                }
               
            }catch(NoOrderException e){
             throw e;
       
            }catch(CannotSearchOrdersException e){
             throw e;
            }
            return list;
        }catch(EmptyOrdersConditionException e){ //何らかの理由で商品検索できなかった場合
        	throw e;
		}
	}
}
