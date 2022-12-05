package makeQuestion.view;

import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;

import java.util.Scanner;
import java.util.ArrayList;

/*
  問題出題のユーザインターフェース
*/
public class AddUI{
    private Scanner in = new Scanner(System.in);
    
    //--------------------------------------------------------------------------
    // コマンド入力
    // 引数 : なし
    // 戻値 : 入力されたコマンド(Enter, r, e)
    //--------------------------------------------------------------------------
    public String inputCmd(){
        System.out.print("Enter入力で開始(r:ランキング表示, e:終了) => ");
        return in.nextLine();
    }
    
    //--------------------------------------------------------------------------
    // ランキング表示
    // 引数 : ranking ランキング記録
    // 戻値 : なし
    //--------------------------------------------------------------------------
    public void showRanking(Ranking ranking){
        ArrayList<Record> records = ranking.getRecords();
        
        System.out.println("～～ランキング～～");
        int rank = 1;
        for(Record curr : records){
            System.out.printf("%2d位 %.3f秒 : %s\n", rank, curr.getTime(), curr.getName());
            rank++;
        }
        System.out.println();
    }
    
    //--------------------------------------------------------------------------
    // 次の問題表示
    // 引数 : question 問題文字列
    // 戻値 : なし
    //--------------------------------------------------------------------------
    public void showQuestion(String question){
        System.out.print(question + " => ");
    }
    
    //--------------------------------------------------------------------------
    // 回答入力
    // 引数 : なし
    // 戻値 : 入力された回答
    //--------------------------------------------------------------------------
    public int inputAns(){
        int ans = in.nextInt();
        in.nextLine();
        
        return ans;
    }
    
    //--------------------------------------------------------------------------
    // 正誤および順位の表示
    // 引数 : result 正誤, rank 順位(ランク外は0), time 回答時間
    // 戻値 : なし
    //--------------------------------------------------------------------------
    public void showResult(boolean result, int rank, double time){
        if(result){
            System.out.print("あたり(" + time + "秒)");
            if(rank != 0){
                System.out.println("おめでとう！" + rank + "位です！");
            }
        }else{
            System.out.println("はずれ");
            System.out.println();
        }
    }
    
    //--------------------------------------------------------------------------
    // 名前入力
    // 引数 : なし
    // 戻値 : 入力された名前
    //--------------------------------------------------------------------------
    public String inputName(){
        System.out.print("名前 => ");
        String name = in.nextLine();
        System.out.println();
        
        return name;
    }
}
