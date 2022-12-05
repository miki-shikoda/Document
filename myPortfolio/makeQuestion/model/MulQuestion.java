//伊藤
package makeQuestion.model;
import java.util.Random;

//掛け算問題を表すクラス
public class MulQuestion extends Question{
    private static Random r = new Random();
    private int num1;
    private int num2;
    
    @Override
    public String next(){
        //ランダムに出題(0～9)
        num1 = r.nextInt(10);
        num2 = r.nextInt(10);
        //イント型をString型に変換
        String a = String.valueOf(num1);
        String b = String.valueOf(num2);
        String c = a + "*" + b;
        //戻値、作成した問題の文字列
        return c;
    }
    //回答の正誤判定をする
    @Override
    public boolean judge(int ans){
        if((num1 * num2) == ans){  //引数
            return true;     //戻値
        }else{
            return false;
        }
    }
    
    @Override
    public String getCurrent(){
        //現在の問題の文字列を得る
        String a = String.valueOf(num1);
        String b = String.valueOf(num2); 
        String c = a + "*" + b;
        //戻値、現在の問題の文字列
        return c;
    } 
    
}


//志子田ver------------------
//掛け算問題を表すクラス
/*
public class MulQuestion extends Question{
    private static Random r = new Random();
    private int num1;
    private int num2;

//ランダムに出題(0～9)--------------
    @Override
    public String next(){
        num1 = r.nextInt(10);
        num2 = r.nextInt(10);
        return num1 + "*" + num2 ;
    }
    
//回答の正誤判定--------------------
    @Override
    public boolean judge(int ans){
    	return (ans == num1 * num2 );
    }

//戻値、現在の問題の文字列----------
    @Override
    public String getCurrent(){
        return num1 + "*" + num2 ;
    } 
}
*/