//米澤
package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import dao.CustomerAccessor;
import dao.ReserveAccessor;
import beans.Address;
import beans.Customer;
import beans.Reserve;
import exceptions.*;

public class A_reservation {
    private Scanner in;
    private String next;
    private String message;
    private Reserve reserve = new Reserve();
    private Customer customer = new Customer();
    private ReserveAccessor ra = new ReserveAccessor();
    private CustomerAccessor ca = new CustomerAccessor();
    private StringCheck sc = new StringCheck();
    private ArrayList<Customer> guestList = new ArrayList<>();
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    //---------------------------------------------------------------------
    //コンストラクタ
    public A_reservation(){
        in = new Scanner(System.in);
    }
    //---------------------------------------------------------------------
    //フィールドnextの値により、各画面処理メソッドを呼び出す
    public void FlowA(){
        next = "a01";
        while(!next.equals("0")){
            if(next.equals("a01")) a01();
            if(next.equals("a02")) reserve = a02();
            if(next.equals("a03")) reserve = a03(reserve);
            if(next.equals("a04")) reserve = a04(reserve);
            if(next.equals("a05")) a05(reserve);
            if(next.equals("a06")) a06();
            if(next.equals("a07")) customer = a07(guestList);
            if(next.equals("a08")) a08(customer);
        }
    }
    //---------------------------------------------------------------------
    //a01予約登録 トップ画面
    private void a01(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("希望する機能を選択してください\n");
        System.out.print("(空席検索:/a,  予約情報本登録:/b,  トップメニューに戻る:/c)\n=> ");
        String inLine = in.nextLine();
        if(inLine.equals("/a")) next = "a02";
        else if(inLine.equals("/b")) next = "a06";
        else if(inLine.equals("/c")) next = "0";
        else message = "[!] 正しいコマンドが入力されていません。\n\n";
    }
    //---------------------------------------------------------------------
    //a02予約登録 空席検索画面
    private Reserve a02(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 空席検索」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[残席数検索] 検索する日付と便名を入力してください");
        System.out.println("(途中で 0 を入力すると予約登録トップ画面へ戻ります)\n");
        String date = "";    //日付
        String flight = "";  //便名
        do{
            System.out.print("● 日付(yyyymmdd) => ");
            date = in.nextLine();
            if(date.equals("0")){
                next = "a01";
                return null;
            }
            if(date.equals("")) System.out.println("\n[!] 日付は必須です\n");
        }while(date.equals("") || !sc.num8check(date));
        do{
            System.out.print("● 便名 => ");
            flight = in.nextLine();
            if(flight.equals("0")){
                next = "a01";
                return null;
            }
            if(flight.equals("")) System.out.println("\n[!] 便名は必須です\n");
        }while(flight.equals("") || !sc.idcheck(flight));
        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(inLine.equals("0")) {
                next = "a01";
                return null;
            }
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません");
        }while(!inLine.equals("") && !inLine.equals("0"));

        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        calendar.set(year,month - 1,day);
        Reserve reserve = new Reserve();
        reserve.setFlightdate(calendar);        //運航日のセット
        reserve.setFlightID(flight);            //便名のセット
        ArrayList<Reserve> reserveList = new ArrayList<>();
        try{
            reserveList = ra.select1(reserve);  //空席数検索メソッドの呼び出し
            reserve = reserveList.get(0);
            next = "a03";
            return reserve;
        }catch(CannotSearchException e){
            message = "[!] 正常に検索できませんでした。";
            return null;
        }catch(NoResultException e){
            message = "[!] 検索結果がありません。";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //a03予約登録 空席検索結果画面
    private Reserve a03(Reserve reserve){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 空席検索結果」===\n\n");
        int seats = reserve.getRemainseats();
        System.out.printf("[検索結果] 残席数：%2d 席です\n",seats);
        String inLine = "";
        do{
            System.out.print("\n(予約に進む:/a,  再検索:/b,  予約登録トップに戻る:/c)\n=> ");
            inLine = in.nextLine();
            if(inLine.equals("/a")) next = "a04";
            else if(inLine.equals("/b")) next = "a02";
            else if(inLine.equals("/c")) next = "a01";
            if(!inLine.equals("/a") && !inLine.equals("/b") && !inLine.equals("/c")){
                System.out.println("\n[!] 正しいコマンドが入力されていません。");    
            }
        }while(!inLine.equals("/a") && !inLine.equals("/b") && !inLine.equals("/c"));
        return reserve;
    }
    //---------------------------------------------------------------------
    //a04予約登録 仮登録画面
    private Reserve a04(Reserve reserve){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 搭乗者情報入力」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("搭乗者名を入力してください\n(途中で 0 を入力すると予約登録トップ画面へ戻ります)\n");
        String inSei = "";  //姓(ふりがな)
        String inMei = "";  //名(ふりがな)
        do{
            System.out.print("● 姓(ふりがな) => ");
            inSei = in.nextLine();
            if(inSei.equals("0")){
                next = "a01";
                return null;
            }
            if(inSei.equals("")) System.out.println("\n[!] 姓(ふりがな)は必須です\n");
        }while(inSei.equals("") || !sc.kanacheck(inSei));
        do{
            System.out.print("● 名(ふりがな) => ");
            inMei = in.nextLine();
            if(inMei.equals("0")){
                next = "a01";
                return null;
            }
            if(inMei.equals("")) System.out.println("\n[!] 名(ふりがな)は必須です\n");
        }while(inMei.equals("") || !sc.kanacheck(inMei));

        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル)\n=> ");
            inLine = in.nextLine();
            if(inLine.equals("0")) {
                next = "a01";
                return null;
            }
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません");
        }while(!inLine.equals("") && !inLine.equals("0"));

        reserve.getGuestID().setSeikana(inSei);   //姓(ふりがな)のセット
        reserve.getGuestID().setMeikana(inMei);   //名(ふりがな)のセット
        Calendar appdate = Calendar.getInstance();
        reserve.setAppdate(appdate);              //予約日のセット
        try{
            reserve = ra.insert(reserve);         //仮予約登録メソッド呼び出し
            next ="a05";
            return reserve;
        }catch(CannotRegistrationException e){
            message = "[!] 正常に登録できませんでした。\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //a05予約登録 仮登録完了画面
    private void a05(Reserve reserve){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 予約登録完了」===\n\n");
        System.out.println("予約完了しました\n");
        String appdate = fmt.format(reserve.getAppdate().getTime());       //申込日
        String flightdate = fmt.format(reserve.getFlightdate().getTime()); //運航日
        System.out.printf("予約番号：%d\n",reserve.getReserveID());
        System.out.printf("申込日:%s\n運航日%s:\n",appdate,flightdate);
        System.out.printf("便名:%s\n",reserve.getFlightID());
        System.out.printf("姓(ふりがな):%s\n",reserve.getGuestID().getSeikana());
        System.out.printf("名(ふりがな):%s\n\n",reserve.getGuestID().getMeikana());
        String inLine = "";
        do{
            System.out.print("Enterキーで予約登録トップ画面に戻ります => ");
            inLine = in.nextLine();
            if(!inLine.equals(""))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals(""));
        this.next = "a01";
    }
    //---------------------------------------------------------------------
    //a06予約登録 本登録予約検索画面
    private ArrayList<Customer> a06(){
        //customer の情報
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 顧客情報検索画面」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("情報を追加したい顧客名(ふりがな)を入力してください\n(0 を入力すると予約登録トップ画面へ戻ります)\n");
        String inSei = "", inMei = "", inLine = "";
        do{
            do{
                System.out.print("● 姓(ふりがな) => ");
                inSei = in.nextLine();
                if(inSei.equals("0")){
                    next = "a01";
                    return null;
                }
            }while(!inSei.equals("") && !sc.kanacheck(inSei));
            do{
                System.out.print("● 名(ふりがな) => ");
                inMei = in.nextLine();
                if(inMei.equals("0")){
                    next = "a01";
                    return null;
                }
            }while(!inMei.equals("") && !sc.kanacheck(inMei));
            //追記(どちらか欠けていると「検索結果がありません」とでてくるのでどちらも必須にしました)
            if(inSei.equals("") || inMei.equals(""))
             System.out.println("\n[!] 姓・名はどちらも必須です\n");
        }while(inSei.equals("") || inMei.equals(""));

        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(inLine.equals("0")) {  //0が入力されたら戻る
                next = "a01";
                return null;
            }
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));

        Customer customer = new Customer();
        customer.setSeikana(inSei);            //姓(ふりがな)のセット
        customer.setMeikana(inMei);            //名(ふりがな)のセット
        try{
            guestList = ca.select2(customer);  //顧客情報検索メソッドの呼び出し
            next = "a07";
            return guestList;
        }catch(CannotSearchException e){
            message = "[!] 正常に検索できませんでした。\n";
            return null;
        }catch(NoResultException e){
            message = "[!] 検索結果がありません。\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //a07予約登録 本登録情報入力画面
    private Customer a07(ArrayList<Customer> guestlist){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 本登録情報入力」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[検索結果]\n");
        System.out.print("顧客No.|   姓    |   名    |  姓(かな)   |  名(かな)   ");
        System.out.println("| 住所ID |    固定電話    |    携帯電話    ");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for(Customer x : guestlist){
            System.out.printf("%7d",x.getGuestID());  //顧客番号の表示

            String sei = x.getSei();  //姓の取得・表示
            if(sei == null) sei = "-";
            sei = sc.Bytecheck(sei, 8);
            System.out.printf("| %s",sei);

            String mei = x.getMei();  //名の取得・表示
            if(mei == null) mei = "-";
            mei = sc.Bytecheck(mei, 8);
            System.out.printf("| %s",mei);

            String seikana = x.getSeikana();  //姓(かな)の取得・表示
            seikana = sc.Bytecheck(seikana, 12);
            System.out.printf("| %s",seikana);

            String meikana = x.getMeikana();  //名(かな)の取得・表示
            meikana = sc.Bytecheck(meikana, 12);
            System.out.printf("| %s",meikana);
            
            String zip = Integer.toString(x.getAddID().getAddID());  //住所IDの取得・表示
            if(zip.equals("0")) zip = "-";
            zip = sc.Bytecheck(zip, 7);
            System.out.printf("| %s",zip);

            String tel = x.getTel();  //電話番号の取得・表示
            if(tel == null) tel = "-";
            tel = sc.Bytecheck(tel, 14);
            System.out.printf("| %s ",tel);

            String mob = x.getMobile();  //携帯番号の取得・表示
            if(mob == null) mob = "-";
            mob = sc.Bytecheck(mob, 14);
            System.out.printf("| %s\n",mob);
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("登録する情報を入力してください  ※は入力必須");
        System.out.println("(今回入力した内容で上書きされます)\n(0 を入力すると顧客名検索画面へ戻ります)\n");
        String id = "", sei = "", mei = "", zip = "", add = "", tel = "", mob = "", inLine = "";
        Customer customer = new Customer();
        Address address = new Address();
        boolean boo = false;
        do{
            System.out.print("※ ● 顧客No. => ");
            id = in.nextLine();
            if(id.equals("0")){
                next = "a06";
                return null;
            }
            if(id.equals("")) System.out.println("\n[!] 顧客Noは必須です\n");
            //追記（リストにある顧客IDを入力してもらう、リストにないものの書き換え阻止）
            for(Customer x : guestlist){
                String gusID = String.valueOf(x.getGuestID()); 
            if(gusID.equals(id)){ boo = true;}
            }
            if(!boo){System.out.println("リストにある顧客IDを入力してください。");}
        }while(id.equals("") || !sc.numcheck(id) || !boo );
        customer.setGuestID(Integer.parseInt(id));  //顧客IDのセット
        do{
            System.out.print("※ ● 姓 => ");
            sei = in.nextLine();
            if(sei.equals("0")){
                next = "a06";
                return null;
            }
            if(sei.equals("")) System.out.println("\n[!] 姓は必須です\n");
        }while(sei.equals(""));
        customer.setSei(sei);  //姓のセット
        do{
            System.out.print("※ ● 名 => ");
            mei = in.nextLine();
            if(mei.equals("0")){
                next = "a06";
                return null;
            }
            if(mei.equals("")) System.out.println("\n[!] 名は必須です\n");
        }while(mei.equals(""));
        customer.setMei(mei);  //名のセット
        do{
            System.out.print("※ ● 郵便番号(ハイフンあり) => ");
            zip = in.nextLine();
            if(zip.equals("0")){
                next = "a06";
                return null;
            }
            if(zip.equals("")) System.out.println("\n[!] 郵便番号は必須です\n");
        }while(zip.equals("") || !sc.zipcheck(zip));
        address.setZipcode(zip);  //郵便番号のセット
        do{
            System.out.print("※ ● 住所 => ");
            add = in.nextLine();
            if(add.equals("0")){
                next = "a06";
                return null;
            }
            if(add.equals("")) System.out.println("\n[!] 住所は必須です\n");
        }while(add.equals(""));
        address.setAddress(add);    //住所のセット
        customer.setAddID(address); //Addressインスタンスのセット
        do{
            System.out.print("  ● 固定電話 => ");
            tel = in.nextLine();
            if(tel.equals("0")){
                next = "a06";
                return null;
            }
            System.out.print("  ● 携帯電話 => ");
            mob = in.nextLine();
            if(mob.equals("0")){
                next = "a06";
                return null;
            }
            if(tel.equals("") && mob.equals("")) 
             System.out.println("\n[!] 固定電話または携帯電話のどちらかは必須です\n");
        }while(tel.equals("") && mob.equals(""));
        customer.setTel(tel);     //固定電話のセット
        customer.setMobile(mob);  //携帯電話のセット
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));
        
        if(inLine.equals("0")) {
            next = "a06";
            return null;
        }
        try{
            customer = ca.insert(customer);  //顧客情報登録メソッドの呼び出し
            next = "a08";
            return customer;
        }catch(CannotRegistrationException e){
            message = "\n[!] 正常に登録できませんでした\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //a08予約登録 本登録完了画面
    private void a08(Customer customer){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約登録 情報登録完了」===\n\n");
        System.out.println("登録完了しました\n");
        System.out.println("[登録内容]\n");
        System.out.printf("顧客No:%d\n", customer.getGuestID());
        System.out.printf("氏名:%s %s\n", customer.getSei(),customer.getMei());
        System.out.printf("郵便番号:%s\n", customer.getAddID().getZipcode());
        System.out.printf("住所:%s\n", customer.getAddID().getAddress());
        System.out.printf("固定電話:%s\n", customer.getTel());
        System.out.printf("携帯電話:%s\n", customer.getMobile());
        
        String inLine = "";
        do{
            System.out.print("\n(続けて登録:/a,  予約登録トップ画面に戻る:/b)\n=> ");
            inLine = in.nextLine();
            if(!inLine.equals("/a") && !inLine.equals("/b"))
             System.out.println("[!] 正しいコマンドが入力されていません");
            else if(inLine.equals("/a")) this.next = "a06";
            else if(inLine.equals("/b")) this.next = "a01";            
        }while(!inLine.equals("/a") && !inLine.equals("/b"));
    }
}
