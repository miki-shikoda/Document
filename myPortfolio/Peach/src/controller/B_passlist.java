//担当：服部
package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import exceptions.CannotSearchException;
import exceptions.NoResultException;
import beans.Reserve;
import dao.CustomerAccessor;
import beans.Customer;

public class B_passlist {
    //クラスフィールド
    private Scanner in;
    private String next;
    private String message;
    private Calendar calendar = Calendar.getInstance();
    private Reserve reserve = new Reserve();
    private StringCheck sc = new StringCheck();
    private CustomerAccessor ca = new CustomerAccessor();
    private ArrayList<Customer> customerList = new ArrayList<>();

    //コンストラクタ
    public B_passlist(){
        in = new Scanner(System.in);
    }
    //---------------------------------------------------------------------
    //フィールドnextの値により、各画面処理メソッドを呼び出す
    public void FlowB(){
        next = "b01";
        while(!next.equals("0")){
            if(next.equals("b01")) customerList = b01();    //戻り値
            if(next.equals("b02")) b02(customerList);       //引数
        }
    }
    //---------------------------------------------------------------------
    //b01搭乗者名簿出力　検索画面
    private ArrayList<Customer> b01(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「搭乗者名簿出力」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[搭乗者名簿出力] 日付と便名を入力してください\n(途中で 0 を入力するとトップ画面へ戻ります)\n");

        //必須入力項目が空だった場合、エラーメッセージを表示する
        String date = "";     //日付
        do{
            do{
                System.out.print("● 日付(yyyymmdd) => ");
                date = in.nextLine();
                if(date.equals("0")){   //途中で0が入力されたらトップ画面に戻る
                    next = "0";
                    return null;
                }
            }while(!date.equals("") && !sc.num8check(date));    //num8check:整数8桁かどうか判定
            if(date.equals("")) System.out.println("\n[!] 日付は必須です\n");
        }while(date.equals(""));

        String flight = "";   //便名
        do{
            do{
                System.out.print("● 便名 => ");//正しい便名を入れなかった場合、Exceptionを投げる
                flight = in.nextLine();
                if(flight.equals("0")){ //途中で0が入力されたらトップ画面に戻る
                    next = "0";
                    return null;
                }
            }while(!flight.equals("") && !sc.idcheck(flight));//便名が正しく入力されているか判定
            reserve.setFlightID(flight);
            if(flight.equals("")) System.out.println("\n[!] 便名は必須です\n");
        }while(flight.equals(""));

        //日付、便名が正しく入力されていたら表示する
        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));

        if(inLine.equals("0")) {  //0が入力されたら戻る
            next = "0";
            return null;
        }

        Calendar cal = Calendar.getInstance();
        int year = 0, month = 0, day = 0;

        if(!date.equals("")){
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(4, 6));
            day = Integer.parseInt(date.substring(6, 8));
            cal.set(year,month - 1,day);
            reserve.setFlightdate(cal);     //日付のセット
        }
        
        try{
            customerList = ca.select(reserve);  //搭乗名簿を扱うメソッドの呼び出し
            //Reserve resData = reserveList.get(0);
            next = "b02";
            return customerList;
        }catch(CannotSearchException e){
            message = "[!] 正常に検索できませんでした\n";
            return null;
        }catch(NoResultException e){
            message = "[!] 検索結果がありません\n";
            return null;
        }

    }
    //---------------------------------------------------------------------
    //b02搭乗者名簿出力　検索結果画面
    private void b02(ArrayList<Customer> customerList){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「搭乗者名簿出力」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[検索結果] 検索された便名の名簿は以下の通りです\n");
        System.out.printf("便名:%s\n", reserve.getFlightID());
        calendar = reserve.getFlightdate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(year + "年" + month + "月" + day + "日\n");

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("  No. |       氏名        |        氏名(かな)         | 郵便番号 |                 住所                 |   電話番号    |  携帯電話番号  ");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        for(Customer cusList : customerList){   //Customerクラスからgetする
            //No.
            System.out.printf("%5d ", cusList.getGuestID());

            //氏名
            String sei = cusList.getSei();
            String mei = cusList.getMei();
            sei = sc.Bytecheck(sei, 8);
            mei = sc.Bytecheck(mei, 8);
            System.out.printf("| %s %s ", sei, mei);

            //氏名(かな)
            String seikana = cusList.getSeikana();
            String meikana = cusList.getMeikana();
            seikana = sc.Bytecheck(seikana, 12);
            meikana = sc.Bytecheck(meikana, 12);
            System.out.printf("| %s %s ", seikana, meikana);

            //郵便番号
            String zipcode = cusList.getAddID().getZipcode();
            zipcode = sc.Bytecheck(zipcode, 8);
            System.out.printf("| %s ", zipcode);

            //住所
            String address = cusList.getAddID().getAddress();
            address = sc.Bytecheck(address, 36);
            System.out.printf("| %s ", address);

            //電話番号
            String tel = cusList.getTel();
            tel = sc.Bytecheck(tel, 13);
            System.out.printf("| %s ", tel);

            //携帯電話番号
            String mobile= cusList.getMobile();
            mobile = sc.Bytecheck(mobile, 13);
            System.out.printf("| %s \n", mobile);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        String inLine = "";
        do{
            System.out.print("(最初に戻る:/a)\n=> ");
            inLine = in.nextLine();
            if(!inLine.equals("/a"))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
            else this.next = "b01";
        }while(!inLine.equals("/a"));
    }
}
