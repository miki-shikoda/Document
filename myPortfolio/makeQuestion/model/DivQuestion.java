package makeQuestion.model;    //¬àV

import java.util.Random;


public class DivQuestion extends Question{
	private int num1;
	private int num2;
    private Random r = new Random();
	
	@Override//Ÿ‚Ì–â‘è
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
	@Override//³Œë‚Ì”»’è
	public boolean judge(int ans){
		//return (ans == num1 / num2);
		if(ans != num1 / num2){
			return false;
		}
		return true;
	}
	@Override//Œ»İ‚Ì–â‘è•¶
	public String getCurrent(){
		return num1 + " / "+  num2;
	}
}


//uq“cver--------------------------------------
/*
public class DivQuestion extends Question{
	private int num1;
	private int num2;
    private Random r = new Random();

//Ÿ‚Ì–â‘è-----------------------------	
	@Override
	public String next(){
        int ans = r.nextInt(10);
        num2 = r.nextInt(9)+1;
        num1 = num2 * ans ;
        
        return num1 + " / " + num2;
	}
//³Œë‚Ì”»’è---------------------------
	@Override
	public boolean judge(int ans){
		return (ans == num1 / num2);
	}
//Œ»İ‚Ì–â‘è•¶-------------------------
	@Override
	public String getCurrent(){
		return num1 + " / "+  num2;
	}
}
*/