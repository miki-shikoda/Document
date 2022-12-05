//米澤
package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import exceptions.CannotSearchException;
import exceptions.NoResultException;
import beans.ReserveSale;
import dao.BusinessInfo;

public class D_sales {
    private Scanner in;
    private String next;
    private String message;
    private BusinessInfo bs = new BusinessInfo();
    private ArrayList<ReserveSale> reserveList = new ArrayList<>();
    private StringCheck sc = new StringCheck();
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
    //---------------------------------------------------------------------
    //コンストラクタ
    public D_sales(){
        in = new Scanner(System.in);
    }
    //---------------------------------------------------------------------
    //フィールドnextの値により、各画面処理メソッドを呼び出す
    public void FlowD(){
        next = "d01";
        while(!next.equals("0")){
            if(next.equals("d01")) d01();
            if(next.equals("d02")) reserveList = d02();
            if(next.equals("d03")) d03(reserveList);
            if(next.equals("b/d04") || next.equals("c/d04")) reserveList = d04();
            if(next.equals("b/d05") || next.equals("c/d05")) d05(reserveList);
            if(next.equals("d06")) d06();
        }
    }
    //---------------------------------------------------------------------
    //d01 営業情報集計 トップ画面
    private void d01(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「営業情報集計」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("集計内容を選択してください\n");
        System.out.print("(日別便別売上:/a,  年月別売上:/b,  年月別便別:/c,  便別売上:/d,  トップメニューに戻る:/e)\n=> ");
        String inLine = in.nextLine();
        if(inLine.equals("/a")) next = "d02";
        else if(inLine.equals("/b")) next = "b/d04";
        else if(inLine.equals("/c")) next = "c/d04";
        else if(inLine.equals("/d")) next = "d06";
        else if(inLine.equals("/e")) next = "0";
        else message = "[!] 正しいコマンドが入力されていません。\n\n";
    }
    //---------------------------------------------------------------------
    //d02 営業情報集計 日別便別集計<<条件入力>>
    private ArrayList<ReserveSale> d02(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「営業情報集計 日別便別集計」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("集計する期間を入力してください");
        System.out.println("(途中で 0 を入力すると営業情報集計トップ画面へ戻ります)\n");
        String setDate = "", endDate = "";
        do{
            do{
                System.out.print("● 集計期間 自(yyyymmdd)=> ");
                setDate = in.nextLine();
                if(setDate.equals("0")) {
                    next = "d01";
                    return null;
                }
            }while(!setDate.equals("") && !sc.num8check(setDate));
            do{
                System.out.print("● 集計期間 至(yyyymmdd)=> ");
                endDate = in.nextLine();
                if(endDate.equals("0")) {
                    next = "d01";
                    return null;
                }
            }while(!endDate.equals("") && !sc.num8check(endDate));
            if(setDate.equals("") && endDate.equals(""))
             System.out.println("\n[!] 集計期間はどちらか必須です\n");
        }while(setDate.equals("") && endDate.equals(""));

        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(inLine.equals("0")) {  //0が入力されたら戻る
                next = "d01";
                return null;
            }
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));

        Calendar setcal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();
        ReserveSale reserve = new ReserveSale();
        int year = 0, month = 0, day = 0;
        if(!setDate.equals("")){
            year = Integer.parseInt(setDate.substring(0, 4));
            month = Integer.parseInt(setDate.substring(4, 6));
            day = Integer.parseInt(setDate.substring(6, 8));
            setcal.set(year,month - 1,day);
            reserve.setFlightdate(setcal);     //集計期間 自のセット
        }
        if(!endDate.equals("")){
            year = Integer.parseInt(endDate.substring(0, 4));
            month = Integer.parseInt(endDate.substring(4, 6));
            day = Integer.parseInt(endDate.substring(6, 8));
            endcal.set(year,month - 1,day);
            reserve.setFlightEnddate(endcal);  //集計期間 至のセット
        }
        try{
            reserveList = bs.dailyFlightSale(reserve); //集計メソッドの呼び出し
            next = "d03";
            return reserveList;
        }catch(CannotSearchException e){
            message = "[!] 正常に検索できませんでした。\n";
            return null;
        }catch(NoResultException e){
            message = "[!] 検索結果がありません。\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //d03 営業情報集計 日別便別集計<<結果>>
    private void d03(ArrayList<ReserveSale> list){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「営業情報集計 日別便別集計結果」===\n\n");
        System.out.println("[集計結果]");
        System.out.println("-------------------------------------------------------------------");
        System.out.println("   日付    |  便名  |      出発      |      到着      |   売上");
        System.out.println("-------------------------------------------------------------------");
        for(ReserveSale x : list){
            String flightdate = fmt.format(x.getFlightdate().getTime());
            System.out.printf("%s ", flightdate);
            System.out.printf("| %s ", x.getFlightID());
            String frombase = x.getFrombase();
            frombase = sc.Bytecheck(frombase, 14);
            System.out.printf("| %s ", frombase);
            String tobase = x.getTobase();
            tobase = sc.Bytecheck(tobase, 14);
            System.out.printf("| %s ", tobase);
            System.out.printf("| %,7d円 \n", x.getSale());
        }
        System.out.println("-------------------------------------------------------------------");
        String inLine = "";
        do{
            System.out.print("\n再検索:/a,  営業情報集計トップ画面に戻る:/b\n=> ");
            inLine = in.nextLine();
            if(inLine.equals("/a")) next = "d02";
            else if(inLine.equals("/b")) next = "d01";
            if(!inLine.equals("/a") && !inLine.equals("/b"))
             System.out.println("\n[!] 正しいコマンドが入力されていません");
        }while(!inLine.equals("/a") && !inLine.equals("/b"));
    }
    //---------------------------------------------------------------------
    //d04 営業情報集計 年月別・年月別便別集計<<条件入力>>
    private ArrayList<ReserveSale> d04(){
        System.out.print("\n\n\n\n=== 搭乗予約台帳管理システム 「営業情報集計 ");
        if(next.equals("b/d04")) System.out.print("年月別");
        else if(next.equals("c/d04")) System.out.print("年月別便別");
        System.out.println("集計」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("集計する期間を入力してください");
        System.out.println("(途中で 0 を入力すると営業情報集計トップ画面へ戻ります)\n");
        String setDate = "", endDate = "";
        do{
            do{
                System.out.print("● 集計期間 自(yyyymm)=> ");
                setDate = in.nextLine();
                if(setDate.equals("0")) {
                    next = "d01";
                    return null;
                }
            }while(!setDate.equals("") && !sc.num6check(setDate));
            do{
                System.out.print("● 集計期間 至(yyyymm)=> ");
                endDate = in.nextLine();
                if(endDate.equals("0")) {
                    next = "d01";
                    return null;
                }
            }while(!endDate.equals("") && !sc.num6check(endDate));
            if(setDate.equals("") && endDate.equals(""))
                System.out.println("\n[!] 集計期間はどちらか必須です\n");
        }while(setDate.equals("") && endDate.equals(""));
        String inLine = "";
        do{
            System.out.print("\nよろしいですか？(Enterで確定、0 でキャンセル) => ");
            inLine = in.nextLine();
            if(inLine.equals("0")) {  //0が入力されたら戻る
                next = "d01";
                return null;
            }
            if(!inLine.equals("") && !inLine.equals("0"))
             System.out.println("\n[!] 正しいコマンドが入力されていません\n");
        }while(!inLine.equals("") && !inLine.equals("0"));

        Calendar setcal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();
        ReserveSale reserve = new ReserveSale();
        int year = 0, month = 0;
        if(!setDate.equals("")){
            year = Integer.parseInt(setDate.substring(0, 4));
            month = Integer.parseInt(setDate.substring(4, 6));
            setcal.set(year,month - 1,1);
            reserve.setFlightdate(setcal);     //集計期間 自のセット
        }
        if(!endDate.equals("")){
            year = Integer.parseInt(endDate.substring(0, 4));
            month = Integer.parseInt(endDate.substring(4, 6));
            endcal.set(year,month,1);
            reserve.setFlightEnddate(endcal);  //集計期間 至のセット
        }
        try{
            //集計メソッドの呼び出し
            if(next.equals("b/d04")) {
                reserveList = bs.monthSale(reserve);//年月別集計メソッドの呼び出し
                next = "b/d05";
            }
            else if(next.equals("c/d04")) {
                reserveList = bs.monthFlightSale(reserve);//年月別便別集計メソッドの呼び出し
                next ="c/d05";
            }
            return reserveList;
        }catch(CannotSearchException e){
            message = "[!] 正常に集計できませんでした。\n";
            return null;
        }catch(NoResultException e){
            message = "[!] 検索結果がありません。\n";
            return null;
        }
    }
    //---------------------------------------------------------------------
    //d05 営業情報集計 年月別・年月別便別集計<<結果>>
    private void d05(ArrayList<ReserveSale> list){
        System.out.print("\n\n\n\n=== 搭乗予約台帳管理システム 「営業情報集計 ");
        if(next.equals("b/d05")) System.out.print("年月別");
        else if(next.equals("c/d05")) System.out.print("年月別便別");
        System.out.println("集計結果」===\n\n");

        System.out.println("[集計結果]");
        if(next.equals("b/d05")){ //年月別集計の結果表示
            System.out.println("-----------------------------");
            System.out.println("    年月   |    売上");
            System.out.println("-----------------------------");
            for(int i = 0; i < list.size(); i++){
                Calendar date = list.get(i).getFlightdate();
                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                System.out.printf("%4d年%2d月 ",year,month + 1);
                System.out.printf("| %,11d円 \n",list.get(i).getSale());
            }
            System.out.println("-----------------------------");
        
        }else if(next.equals("c/d05")){ //年月別便別集計の結果表示
            System.out.println("----------------------------------------------------------------------");
            System.out.println("   年月    |  便名  |      出発      |      到着      |    売上");
            System.out.println("----------------------------------------------------------------------");
            for(ReserveSale x : list){
                Calendar date = x.getFlightdate();
                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                System.out.printf("%4d年%2d月 ",year,month + 1);
                System.out.printf("| %s ", x.getFlightID());
                String frombase = x.getFrombase();
                frombase = sc.Bytecheck(frombase, 14);
                System.out.printf("| %s ", frombase);
                String tobase = x.getTobase();
                tobase = sc.Bytecheck(tobase, 14);
                System.out.printf("| %s ", tobase);
                System.out.printf("| %,10d円 \n", x.getSale());
            }
            System.out.println("----------------------------------------------------------------------");
        }
        String inLine = "";
        do{
            System.out.print("\n再検索:/a,  営業情報集計トップ画面に戻る:/b\n=> ");
            inLine = in.nextLine();
            if(inLine.equals("/a")) {
                if(next.equals("b/d05")) next = "b/d04";      //年月別の再検索
                else if(next.equals("c/d05")) next = "c/d04"; //年月別便別の再検索
            }else if(inLine.equals("/b")) next = "d01";
            if(!inLine.equals("/a") && !inLine.equals("/b"))
             System.out.println("\n[!] 正しいコマンドが入力されていません。");
        }while(!inLine.equals("/a") && !inLine.equals("/b"));
    }
    //---------------------------------------------------------------------
    //d06 営業情報集計 便別集計結果
    private void d06(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「営業情報集計 便別集計」===\n\n");
        try{
            ArrayList<ReserveSale> reserveList = bs.flightSale(); //便別集計メソッドの呼び出し
            System.out.println("[集計結果]");
            System.out.println("---------------------------------------------------------");
            System.out.println("  便名  |      出発     |      到着     |      売上");
            System.out.println("---------------------------------------------------------");
            for(ReserveSale x : reserveList){
                System.out.printf(" %s ", x.getFlightID());
                String frombase = x.getFrombase();
                frombase = sc.Bytecheck(frombase, 14);
                System.out.printf("| %s", frombase);
                String tobase = x.getTobase();
                tobase = sc.Bytecheck(tobase, 14);
                System.out.printf("| %s", tobase);
                System.out.printf("| %,11d円 \n", x.getSale());
            }
            System.out.println("---------------------------------------------------------");
        }catch(CannotSearchException e){
            System.out.println("[!] 正常に検索できませんでした。\n");
        }
        String inLine = "";
        do{
            System.out.print("\nEnterで営業情報集計トップ画面に戻る => ");
            inLine = in.nextLine();
            if(!inLine.equals("")) System.out.println("\n[!] 正しいコマンドが入力されていません。\n");
        }while(!inLine.equals(""));
        next = "d01";
    }
}
