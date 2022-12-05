//担当：服部
package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import exceptions.CannotSearchException;
import exceptions.NoResultException;
import beans.Reserve;
import beans.Flight;
import dao.ReserveAccessor;

public class C_reservecheck {
    //クラスフィールド
    private Scanner in;
    private String next;
    private String message;
    private Calendar calendar = Calendar.getInstance();
    private Reserve reserve = new Reserve();
    private ReserveAccessor ra = new ReserveAccessor();
    private ArrayList<Flight> flightList = new ArrayList<>();
    private ArrayList<Reserve> resList = new ArrayList<>();
    private StringCheck sc = new StringCheck();
    //---------------------------------------------------------------------
    //コンストラクタ
    public C_reservecheck(){
        in = new Scanner(System.in);
    }
    //---------------------------------------------------------------------
    //フィールドnextの値により、各画面処理メソッドを呼び出す
    public void FlowC(){
        next = "c01";
        while(!next.equals("0")){
            if(next.equals("c01")) c01();
            if(next.equals("c02")) resList = c02();
            if(next.equals("c03")) c03(resList);
            if(next.equals("c04")) flightList = c04();
            if(next.equals("c05")) c05(flightList);
        }
    }
    //---------------------------------------------------------------------
    //c01予約状況検索　トップ画面
    private void c01(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約状況検索」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("希望する機能を選択してください\n");
        System.out.print("(空席検索:/a,  出発便一覧検索:/b,  トップに戻る:/c)\n=> ");
        String inLine = in.nextLine();
        if(inLine.equals("/a")) next = "c02";
        else if(inLine.equals("/b")) next = "c04";
        else if(inLine.equals("/c")) next = "0";
        else message = "[!] 正しいコマンドが入力されていません\n\n";
    }
    //---------------------------------------------------------------------
    //c02予約状況検索　空席検索画面
    private ArrayList<Reserve> c02(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約状況検索」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[空席検索]検索する情報を入力してください\n\n(途中で 0 を入力すると最初の画面へ戻ります)\n");
        String flight = "";   //便名
        do{
            do{
                System.out.print("● 便名 => ");
                flight = in.nextLine();
                if(flight.equals("0")) {    //途中で0が入力されたらトップ画面に戻る
                    next = "c01";
                    return null;
                }
            }while(!flight.equals("") && !sc.idcheck(flight));//便名が空欄だったり誤って入力されていないか判定
            reserve.setFlightID(flight);
            if(flight.equals(""))
             System.out.println("\n[!] 便名は必須です\n");
        }while(flight.equals(""));

        //運航日：必須入力項目が正しく入力されていない場合、エラーメッセージを表示する
        String setDate = "", endDate = "";   //日付(自),日付(至)
        do{
            do{
                System.out.print("● 運航日 自(yyyymmdd) => ");
                setDate = in.nextLine();
                if(setDate.equals("0")) {   //途中で0が入力されたらトップ画面に戻る
                    next = "c01";
                    return null;
                }
            }while(!setDate.equals("") && !sc.num8check(setDate));//運航日が空欄だったり誤って入力されていないか判定
            if(setDate.equals(""))
            System.out.println("\n[!] 運航日は必須です\n");
        }while(setDate.equals(""));

        do{
            do{
                System.out.print("● 運航日 至(yyyymmdd) => ");
                endDate = in.nextLine();
                if(endDate.equals("0")) {
                    next = "c01";
                    return null;
                }               
            }while(!endDate.equals("") && !sc.num8check(endDate));//運航日が空欄だったり誤って入力されていないか判定
            if(endDate.equals(""))
             System.out.println("\n[!] 運航日は必須です\n");
        }while(endDate.equals(""));

        //便名、運航日が正しく入力されていたら表示する
        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(!inLine.equals("") && !inLine.equals("0"))
            System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));

        if(inLine.equals("0")) {    //0が入力されたら最初の画面に戻る
            next = "c01";
            return null;
        }
        Calendar setcal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();
        int year = 0, month = 0, day = 0;

        if(!setDate.equals("")){
            year = Integer.parseInt(setDate.substring(0, 4));
            month = Integer.parseInt(setDate.substring(4, 6));
            day = Integer.parseInt(setDate.substring(6, 8));
            setcal.set(year,month - 1,day);
            reserve.setFlightdate(setcal);     //運航日 自のセット
        }
        //setcalの時刻部分をクリア
        setcal.set(Calendar.HOUR_OF_DAY, setcal.getActualMinimum(Calendar.HOUR_OF_DAY));
        setcal.set(Calendar.MINUTE, setcal.getActualMinimum(Calendar.MINUTE));
        setcal.set(Calendar.SECOND, setcal.getActualMinimum(Calendar.SECOND));
        setcal.set(Calendar.MILLISECOND, setcal.getActualMinimum(Calendar.MILLISECOND));

        if(!endDate.equals("")){
            year = Integer.parseInt(endDate.substring(0, 4));
            month = Integer.parseInt(endDate.substring(4, 6));
            day = Integer.parseInt(endDate.substring(6, 8));
            endcal.set(year,month - 1,day);
            reserve.setFlightEnddate(endcal);  //運航日 至のセット
        }
        //endcalの時刻部分をクリア
        endcal.set(Calendar.HOUR_OF_DAY, endcal.getActualMinimum(Calendar.HOUR_OF_DAY));
        endcal.set(Calendar.MINUTE, endcal.getActualMinimum(Calendar.MINUTE));
        endcal.set(Calendar.SECOND, endcal.getActualMinimum(Calendar.SECOND));
        endcal.set(Calendar.MILLISECOND, endcal.getActualMinimum(Calendar.MILLISECOND));

        try{
            resList = ra.select2(reserve);  //予約情報を扱うメソッドの呼出し
            next = "c03";
            return resList;
        }catch(CannotSearchException e){
            message = "[!] 正常に検索できませんでした\n";
            return null;
        }catch(NoResultException e){
            message = "[!] 検索結果がありません\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //c03予約状況検索　空席検索結果画面
    private void c03(ArrayList<Reserve> resList){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約状況検索」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        Calendar setdate = reserve.getFlightdate();    //検索期間 自
        Calendar enddate = reserve.getFlightEnddate(); //検索期間 至
        Date day1 = setdate.getTime(), day2 = enddate.getTime(); 
        String date_1 = fmt.format(day1), date_2 = fmt.format(day2);
        System.out.println("[検索結果] 検索された空席は以下の通りです\n");
        System.out.printf("便名:%s\n", reserve.getFlightID());
        System.out.printf("運航日:%s ~ %s\n\n", date_1, date_2);
        System.out.println("----------------------");
        System.out.println("   運航日   | 残席数 ");
        System.out.println("----------------------");

        int year = 0, month = 0, day = 0, seat = 0;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.getActualMinimum(Calendar.YEAR));
        for(int i = 0; setdate.compareTo(enddate) <= 0;){
            if(resList.size() > 0) calendar = resList.get(i).getFlightdate();
            //検索したい日とリストに入っている運航日が一致するか判定
            if(setdate.compareTo(calendar) != 0){
                year = setdate.get(Calendar.YEAR);
                month = setdate.get(Calendar.MONTH);
                day = setdate.get(Calendar.DATE);
                seat = 19;  //残席数19として設定
            }else{
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DATE);
                seat = resList.get(i).getRemainseats(); //実際の残席数を設定
                if(seat < 0) seat = 0;
                if(i < resList.size() - 1) i++;
            }
            System.out.printf(" %4d/%2d/%2d ", year, month + 1, day);
            System.out.printf("| %2d 席\n", seat);
            setdate.add(Calendar.DATE, 1);
        }
        System.out.println("----------------------");
        String inLine = "";
        do{
            System.out.print("\n(再検索:/a,  最初に戻る:/b)\n=> ");
            inLine = in.nextLine();
            if(!inLine.equals("/a") && !inLine.equals("/b"))
                System.out.println("\n[!] 正しいコマンドが入力されていません\n");
            else if(inLine.equals("/a")) this.next = "c02";
            else if(inLine.equals("/b")) this.next = "c01";
        }while(!inLine.equals("/a") && !inLine.equals("/b"));
    }
    //---------------------------------------------------------------------
    //c04予約状況検索　出発便一覧検索画面
    private ArrayList<Flight> c04(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約状況検索」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[出発便一覧検索] 空港名を入力してください(部分検索可)\n(途中で 0 を入力すると最初の画面へ戻ります)\n");

        //必須入力項目が空だった場合、エラーメッセージを表示する　ここから
        String airport = "";  //空港名
        do{
            System.out.print("● 空港名(愛称も可) => ");
            airport = in.nextLine();
            if(airport.equals("0")){
                next = "c01";  //0が入力されたら最初の画面に戻る
                return null;
            }
            if(airport.equals("")) System.out.println("\n[!] 空港名は必須です\n");
        }while(airport.equals(""));
        //ここまで
        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(!inLine.equals("") && !inLine.equals("0"))
            System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));

        if(inLine.equals("0")) {
            next = "c01";  //0が入力されたら最初の画面に戻る
            return null;
        }
        
        Flight flight = new Flight();
        flight.setFlightname(airport); //空港名をセット
        try{
            flightList = ra.select3(flight);  //予約情報を扱うメソッドの呼出し
            next = "c05";
            return flightList;
        }catch(CannotSearchException e){
            //e.printStackTrace();
            message = "[!] 正常に検索できませんでした\n";
            return null;
        }catch(NoResultException e){
            //e.printStackTrace();
            message = "[!] 検索結果がありません\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //c05予約状況検索　出発便一覧検索結果
    private void c05(ArrayList<Flight> flightList){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「予約状況検索」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("[検索結果]検索された出発便一覧は以下の通りです\n");
        System.out.println("-------------------------------------------------------------");
        System.out.println("  便名  |  出発空港名  | 出発時刻 |  到着空港名  | 到着時刻 ");
        System.out.println("-------------------------------------------------------------");
        for(Flight fList : flightList){
            //便名
            String flightID = fList.getFlightID();
            flightID = sc.Bytecheck(flightID, 6);
            System.out.printf(" %s ", flightID);
            
            //出発空港名
            String departure = fList.getDeparture();
            departure = sc.Bytecheck(departure, 12);
            System.out.printf("| %s ", departure);

            //出発時刻
            String openTime = fList.getOpenTime();
            openTime = sc.Bytecheck(openTime, 7);
            System.out.printf("|  %s ", openTime);

            //到着空港名
            String destination = fList.getDestination();
            destination = sc.Bytecheck(destination, 12);
            System.out.printf("| %s ", destination);

            //到着時刻
            String closeTime = fList.getCloseTime();
            closeTime = sc.Bytecheck(closeTime, 7);
            System.out.printf("|  %s \n", closeTime);
        }
        System.out.println("-------------------------------------------------------------");
        String inLine = "";
        do{
            System.out.print("\n(再検索:/a,  最初に戻る:/b)\n=> ");
            inLine = in.nextLine();
            if(!inLine.equals("/a") && !inLine.equals("/b"))
                System.out.println("\n[!] 正しいコマンドが入力されていません\n");
            else if(inLine.equals("/a")) this.next = "c04";
            else if(inLine.equals("/b")) this.next = "c01";
        }while(!inLine.equals("/a") && !inLine.equals("/b"));
    }
}
