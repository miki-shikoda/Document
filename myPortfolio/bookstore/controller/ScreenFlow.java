package bookstore.controller;
//javac bookstore\controller\ScreenFlow.java
//java bookstore.controller.ScreenFlow

import java.util.Scanner;
import java.util.ArrayList;
import bookstore.beans.Item;
import bookstore.beans.Order;
import bookstore.model.ItemsManager;
import bookstore.model.OrdersManager;
import bookstore.beans.OrderCondition;
import java.util.Calendar;
import bookstore.exceptions.*;

public class ScreenFlow{

    //クラスフィールド
    private Scanner in ;
    private String mess = null;
    private int next = 1;
    private static ScreenFlow sf;
    private OrdersManager om = new OrdersManager();


    //コンストラクタ（入力待ちをセット）
    public ScreenFlow(){
        in = new Scanner(System.in);
    }
    
    
    //メインメソット
    public static void main(String[] args){
        sf = new ScreenFlow();
        sf.mainFlow();
    
    }

    //nextの値から次のメニューを判断
    private void mainFlow(){
        ArrayList<Item> itemList = null;
        Item item = null;
        Order order = null;
        while(sf.next != 0){
            ArrayList<Order> orderList = null;
            if(sf.next == 1){
                itemList = sf.s01();
            }
            if(sf.next == 2){
                item = sf.s02(itemList);
            }
            if(sf.next == 3){
                order = sf.s03(item);
            }
            if(sf.next == 4){
                sf.s04(order);
            }
            if(sf.next == 11){
                orderList = sf.s11();
            }
            if(sf.next == 12){
                sf.s12(orderList);
            }
            
        }
    }
    
    //商品検索するメソッド
    private ArrayList<Item> s01(){
        System.out.println("=== 書籍販売システム「商品検索」 ===");
        System.out.println();
        System.out.println();
        
        //エラーメッセージの表示
        if(mess != null){
            System.out.println(mess);
        }
        mess = null;
        
        System.out.println();
        System.out.println();
        System.out.println("検索する書籍名を入力してください。書籍名の一部分でも検索できます");
        System.out.println("(店員メニュー：/t, 終了：/q)");
        System.out.print("⇒");
        String inLine = in.nextLine();
        ItemsManager im = new ItemsManager();
        ArrayList<Item> itemsList;
       
        if(inLine.equals("/q")){        ///qは終了
            this.next = 0;
            return null;
        }else if(inLine.equals("/t")){    ///tは店員メニュー
            this.next = 11;
            return null;
        }else{
            Item item = new Item(null,inLine,0);
//            item.setName(inLine);
            try{
                itemsList = im.search(item);
            }catch(EmptyItemsConditionException e){
                mess = "検索語が入力されていません";
                this.next = 1;
                return null;
            }catch(NoItemsException e2){
                mess = "検索結果はありません";
                this.next = 1;
                return null;
            }catch(CannotSearchItemsException e3){
                mess = "検索できませんでした。やり直してください。";
                this.next = 1;
                return null;
            }
        }    
        this.next = 2;
        return itemsList;
        
        
    }
    
    
    //注文する商品を選ぶメソッド
    private Item s02(ArrayList<Item> itemlist){
        System.out.println("=== 書籍販売システム「商品一覧」 ===");
        System.out.println();
        System.out.println();
        //エラーメッセージの表示
        if(mess != null){
            System.out.println(mess);
        }
        mess = null;
        
        System.out.println();
        System.out.println();
        
        //商品一覧表示
        for(int i = 0; i < itemlist.size(); i++){
            System.out.println(i + 1 + ". " + itemlist.get(i).getName() + " [" + itemlist.get(i).getPrice() + " 円] ");
        }
        
        System.out.println();
        System.out.println();
        
        System.out.println("注文する商品の番号を入力してください。(0：最初へ戻る)");
        System.out.print("⇒");
        String iInt = in.nextLine();
        //整数判断
        int Int;
        try{
            Int = Integer.parseInt(iInt);
        }catch(NumberFormatException e){
            this.next = 2;
            mess = "商品の番号が違います";
            return null;
        }
        
        if(Int == 0){
            this.next = 1;          //ユーザーが0を入力
            return null;
        }else if( itemlist.size() < Int){
            this.next = 2;                           //表示商品一覧の数字よりも大きい値をしてしまった
            mess = "商品の番号が違います";
            return null;
        }
        Item it = itemlist.get(Int-1);
        this.next = 3;
        return it;
    }
    
    
    //注文情報入力メソッド
    private Order s03(Item item){
        System.out.println("=== 書籍販売システム「商品一覧」 ===");
        System.out.println();
        System.out.println();
        
        //エラーメッセージの表示
        if(mess != null){
            System.out.println(mess);
        }
        mess = null;

        System.out.println();
        System.out.println();
        System.out.println("ご注文情報をご入力ください。途中で 0 を入力すると最初の画面へ戻ります。（※は必須項目です）");
        System.out.println();
        System.out.println();
        
        
        //情報入力してもらう(0の時終了)
        System.out.println("商品名 ：" + item.getName() + " [" + item.getPrice() + " 円]"); 
        Order order = new Order();
        order.setItem(item);
        System.out.print("※ご注文個数 => ");
        String quantity2 = in.nextLine();
        //整数判断
        int quantity3 = 0;
        try{
            quantity3 = Integer.parseInt(quantity2);
        }catch(NumberFormatException e){
            this.next = 3;
            mess = "ご注文個数は 1 以上の整数です";
        }
        order.setQuantity(quantity3);
        
        System.out.println();
        
        System.out.print("※お名前(姓) => ");
        String sei = in.nextLine();
        if(sei.equals("0")){
            next = 1;
            return null;
        }else{
            order.setSei(sei);
        }
        System.out.println();
        
        System.out.print("※お名前(名) => ");
        String mei = in.nextLine();
        if(mei.equals("0")){
            next = 1;
            return null;
        }else{
            order.setMei(mei);
        }
        System.out.println();
        
        System.out.print("※ご住所(都道府県) => ");
        String pref = in.nextLine();
        if(pref.equals("0")){
            this.next = 1;
            return null;
        }else{
            order.setPref(pref);
        }
        System.out.println();
        
        System.out.print("※ご住所(都道府県以降) => ");
        String add = in.nextLine();
        if(add.equals("0")){
            this.next = 1;
            return null;
        }else{
            order.setAdd(add);
        }
        System.out.println();
        
        System.out.print("お電話番号 => ");
        String tel = in.nextLine();
        if(tel.equals("0")){
            this.next = 1;
            return null;
        }else{
            order.setTel(tel);
        }
        System.out.println();
        
        System.out.print("メールアドレス => ");
        String mail = in.nextLine();
        if(mail.equals("0")){
            this.next = 1;
            return null;
        }else{
            order.setMail(mail);
        }
        System.out.println();
        
        System.out.print("よろしいですか？(Enter で確定、0 でキャンセル) => ");
        String okng = in.nextLine();
        if(okng.equals("0")){
            this.next = 1;
            return null;
        }
        try{
            quantity3 = Integer.parseInt(quantity2);
        }catch(NumberFormatException e){
            this.next = 3;
            mess = "ご注文個数は 1 以上の整数です";
            return null;
        }

        //注文を登録(order追加)
        try{
//            OrdersManager om = new OrdersManager();
            om.add(order);
            quantity3 = Integer.parseInt(quantity2);
        }catch(IllegalItemNoException e){
            mess ="注文できませんでした。やり直してください。"/*"商品番号が違います。"*/;
            this.next = 2;
            return null;
        }catch(IllegalQuantityException e2){
            mess = "ご注文個数は 1 以上の整数です";
            this.next = 3;
            return null;
        }catch(EmptyNameException e3){
            mess = "お名前を入力してください";
            this.next = 3;
            return null;
        }catch(EmptyAddException e4){
            mess = "ご住所を入力してください";
            this.next = 3;
            return null;
        }catch(CannotAddOrderException e5){
            mess = "注文登録できませんでした。やり直してください。"/*"商品番号が違います。"*/;
            this.next = 3;
            return null;
        }catch(EmptyItemsConditionException e6){
            mess = "検索語が入力されていません";
            this.next = 1;
            return null;
        }catch(NumberFormatException e7){
            this.next = 3;
            mess = "ご注文個数は 1 以上の整数です";
            return null;
        }
        this.next = 4;
        return order;
        
    } 
    
    //注文の確認するメソッド(注文商品の表示)
    private void s04(Order order){
    
        System.out.println("=== 書籍販売システム「ご注文の完了」 ===");
        System.out.println();
        System.out.println();
        System.out.println("ご注文ありがとうございます。");
        System.out.println("以下の通り承りました。");
        System.out.println();
        System.out.println();
        System.out.print("ご注文番号：");
        System.out.println(order.getNo());
        System.out.println();
        System.out.println();           
        System.out.print("商品名：");
        Item it = order.getItem();
        System.out.println(it.getName() + " [" + it.getPrice() + " 円]");
        System.out.print("ご注文個数：");
        System.out.println(order.getQuantity());
        System.out.print("お名前：");
        System.out.println(order.getSei() + " " + order.getMei());
        System.out.print("ご住所：");
        System.out.println(order.getPref() + " " + order.getAdd());
        System.out.print("お電話番号：");
        System.out.println(order.getTel());
        System.out.print("メールアドレス：");
        System.out.println(order.getMail());
        System.out.println();
        System.out.println();           
        System.out.println("ご注文番号は、画面保存やメモを取るなどして、お手元に残しておいてください。");
        System.out.println("Enter キーで最初に戻ります => ");
        in.nextLine();
        
        this.next = 1 ;
    
    }
    
    
    
    //注文検索するメソッド(店員メニュー)
    private ArrayList<Order> s11(){
    
        System.out.println("=== 書籍販売システム「注文検索」 ===");
        System.out.println();
        System.out.println();
        
        //エラーメッセージの表示
        if(mess != null){
            System.out.println(mess);
        }
        mess = null;
        
        //
        System.out.println("検索する注文の情報を入力してください。省略する項目は Enter のみを押下します。");
        System.out.println("途中で 0 を入力すると最初の画面へ戻ります。");
        OrderCondition oc = new OrderCondition();
        System.out.print("注文番号 => ");
        String orderNo = in.nextLine();
        
        if(orderNo.equals("0")){
            this.next = 1;
            return null;
        }
        try{
            int on = Integer.parseInt(orderNo);
            oc.setNo(on);
        }catch(NumberFormatException e){
            //なにもしない
            
        }
        
        System.out.println();
        System.out.print("注文日(yyyymmdd) 自 => ");
        String st1 = in.nextLine();
        if(st1.equals("0")){
            this.next = 1;
            return null;
        }
        if(8 == st1.length()){
	        int year1 = Integer.valueOf(st1.substring(0,4));
	        int month1 = Integer.valueOf(st1.substring(4,6));
	        int day1 = Integer.valueOf(st1.substring(6,8));
	        Calendar cl1 = Calendar.getInstance();
	        cl1.set(year1,(month1-1),day1);
	        oc.setDatetime(cl1);
        }
        System.out.println();
        System.out.print("注文日(yyyymmdd) 至 => ");
        String st2 = in.nextLine();
        if(st2.equals("0")){
            this.next = 1;
            return null;
        }
        if(8 == st2.length()){
	        int year2 = Integer.valueOf(st2.substring(0,4));
	        int month2 = Integer.valueOf(st2.substring(4,6));
	        int day2 = Integer.valueOf(st2.substring(6,8));
	        Calendar cl2 = Calendar.getInstance();
	        cl2.set(year2,(month2-1),day2);
	        oc.setEnddate(cl2);
        }
        System.out.println();
        System.out.print("購入者氏名(部分も可) => ");
        String orderName = in.nextLine();
        if(orderName.equals("0")){
            this.next = 1;
            return null;
        }
        if(!orderName.equals("")){
	        oc.setSei(orderName);
	        oc.setMei(orderName);
        }
        System.out.println();
        System.out.print("購入者電話番号 => ");
        String orderTel = in.nextLine();
        if(orderTel.equals("0")){
            this.next = 1;
            return null;
        }
        if(!orderTel.equals("")){
	        oc.setTel(orderTel);
        }
        System.out.println();
        System.out.print("商品名(部分も可) => ");
        String ordergoods = in.nextLine();
        if(ordergoods.equals("0")){
            this.next = 1;
            return null;
        }
        if(!ordergoods.equals("")){
            oc.setItem(new Item("0",ordergoods,0));

//	        Item it2 = oc.getItem();
//	        it2.setName(ordergoods);
        }
        
        ArrayList<Order> ordlist;
        try{
//            OrdersManager om = new OrdersManager();
            ordlist = om.search(oc);
        }catch(EmptyOrdersConditionException e){
            mess = "検索条件がひとつも入力されていません";
            this.next = 11;
            return null;
        }catch(NoOrderException e2){
            mess = "検索結果はありません";
            this.next = 11;
            return null;
        }catch(CannotSearchOrdersException e3){
            mess = "検索できませんでした。やり直してください。";
            this.next = 11;
            return null;
        }
        this.next = 12;
        return ordlist;

    
    
    
    }


    //注文表示するメソッド
    private void s12(ArrayList<Order> orderlist){
        System.out.println("=== 書籍販売システム「注文一覧」 ===");
        System.out.println();
        System.out.println();
        System.out.println("検索された注文は以下の通りです。");
        System.out.println();
        System.out.println();
        
        
        for(int i = 0; i < orderlist.size(); i++){
            System.out.println("No. " + (/*(i + 1)*/orderlist.get(i).getNo()) + ", " + orderlist.get(i).getDatetime().get(Calendar.YEAR) 
            + "/" + (orderlist.get(i).getDatetime().get(Calendar.MONTH)+1) + "/" + orderlist.get(i).getDatetime().get(Calendar.DATE) 
            + " " + orderlist.get(i).getDatetime().get(Calendar.HOUR_OF_DAY) + ":" +  orderlist.get(i).getDatetime().get(Calendar.MINUTE) 
            + ":" +  orderlist.get(i).getDatetime().get(Calendar.SECOND) 
            + ", " + orderlist.get(i).getItem().getName() + ", " + orderlist.get(i).getQuantity() + "個, " 
            + orderlist.get(i).getSei() + orderlist.get(i).getMei() + ", " 
            + orderlist.get(i).getPref() + ", " + orderlist.get(i).getAdd()
            + ", " + orderlist.get(i).getTel() + ", " + orderlist.get(i).getMail());
        }
        System.out.println();
        System.out.println();
        System.out.print("最初に戻る:0, 新たな検索:0 以外 => ");
        int finalDo = in.nextInt();
        in.nextLine();
        if(finalDo == 0){
            this.next = 1;
        }else{
            this.next = 11;
        }
    }
    

}
