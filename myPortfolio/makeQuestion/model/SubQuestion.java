package makeQuestion.model;
//引き算問題を表すクラス						作成者：志子田
import java.util.Random;
public class SubQuestion extends Question{
	private Random r = new Random();
	private int num1;
	private int num2;
	
/**----------------------------------
* 次の問題を作成する。※※解読&記述したら完成→2つめの整数および答えは 0～9 の整数にする。←※※
* [戻値]作成した問題の文字
----------------------------------*/
	@Override
	public String next(){     
		//super.next();
        //num1 = r.nextInt(10);
        num2 = r.nextInt(10);
        int ans = r.nextInt(10);
        num1 = num2 + ans;
        /*while( 0 > num1-num2 ){
	        num1 = r.nextInt(10);
	        num2 = r.nextInt(10);
        }*/
        return num1 + " - " + num2;
	}
/*----------------------------------
回答の正誤判定をする
[引数]int ans：判定する回答
[戻値]判定結果 true：正答, false：誤答
----------------------------------*/
	@Override
	public boolean judge(int ans){
		return (ans == num1 - num2);
	}

/*----------------------------------
現在の問題の文字列を得る
[戻値]現在の問題の文字
----------------------------------*/
	@Override
	public String getCurrent(){
		return num1 + " - " + num2;
	}
}