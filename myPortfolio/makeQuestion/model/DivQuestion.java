package makeQuestion.model;    //小澤

import java.util.Random;


public class DivQuestion extends Question{
	private int num1;
	private int num2;
    private Random r = new Random();
	
	@Override//次の問題
	public String next(){
        num1 = r.nextInt(10);
        num2 = r.nextInt(9)+1;
        int ans = num1 % num2;
        while(ans > 0){    //num1 % num2
        	num1 = r.nextInt(10);
        	num2 = r.nextInt(9)+1;
        	ans = num1 % num2;
        }
        return num1 + " / " + num2;
	}
	@Override//正誤の判定
	public boolean judge(int ans){
		//return (ans == num1 / num2);
		if(ans != num1 / num2){
			return false;
		}
		return true;
	}
	@Override//現在の問題文
	public String getCurrent(){
		return num1 + " / "+  num2;
	}
}


//志子田ver--------------------------------------
/*
public class DivQuestion extends Question{
	private int num1;
	private int num2;
    private Random r = new Random();

//次の問題-----------------------------	
	@Override
	public String next(){
        int ans = r.nextInt(10);
        num2 = r.nextInt(9)+1;
        num1 = num2 * ans ;
        
        return num1 + " / " + num2;
	}
//正誤の判定---------------------------
	@Override
	public boolean judge(int ans){
		return (ans == num1 / num2);
	}
//現在の問題文-------------------------
	@Override
	public String getCurrent(){
		return num1 + " / "+  num2;
	}
}
*/