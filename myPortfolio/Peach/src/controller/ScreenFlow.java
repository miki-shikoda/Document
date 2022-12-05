//米澤
package controller;

import java.util.Scanner;

public class ScreenFlow {
    private Scanner in;
    private String message;
    private String next = "t01";
    private A_reservation a = new A_reservation();
    private B_passlist b = new B_passlist();
    private C_reservecheck c = new C_reservecheck();
    private D_sales d = new D_sales();

    //コンストラクタ
    public ScreenFlow(){
        in = new Scanner(System.in);
    }
    //メインメソッド
    public static void main(String[] args){
        ScreenFlow sf = new ScreenFlow();
        sf.mainFlow();
    }
    //フィールドnextの値により、各画面処理メソッドを呼び出す
    private void mainFlow(){
        while(!next.equals("0")){
            if(next.equals("t01")) t01();  //トップメニューへ
            else if(next.equals("A")) {
                a.FlowA();  //予約登録へ
                next = "t01";
            }
            else if(next.equals("B")) {
                b.FlowB();  //搭乗名簿へ
                next = "t01";
            }
            else if(next.equals("C")) {
                c.FlowC();  //予約状況検索へ
                next = "t01";
            }
            else if(next.equals("D")) {
                d.FlowD();  //営業情報集計へ
                next = "t01";
            }
        }
    }
    //t01システムトップ画面
    private void t01(){
        System.out.println("\n\n\n\n=== 搭乗予約台帳管理システム 「トップメニュー」===\n\n");
        if(message != null) {
            System.out.println(message); //エラーメッセージ表示
            message = null;
        }
        System.out.println("希望するメニューを選択してください\n");
        System.out.println("(予約登録:/a,  搭乗名簿:/b,  予約状況検索:/c,  営業情報集計:/d,  終了:/e)");
        System.out.print("=> ");
        String inLine = in.nextLine();
        if(inLine.equals("/a")) next = "A";
        else if(inLine.equals("/b")) next = "B";
        else if(inLine.equals("/c")) next = "C";
        else if(inLine.equals("/d")) next = "D";
        else if(inLine.equals("/e")) next = "0";
        else message = "[!] 正しいコマンドが入力されていません。\n\n";
    }
}
