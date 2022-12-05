package makeQuestion.view;


/*
出題プログラムのユーザーインターフェースを提供するクラスを
継承し、Errorを表示するためのクラス。
*/
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ErrorUI extends AddUI{ 
	private Scanner in = new Scanner(System.in);

    public void IOException(){
		System.out.println("ファイルを開けませんでした。");
    }
    
    @Override
    public int inputAns(){
    	int ans;
    	try{
		    ans = in.nextInt();
		    in.nextLine();
    	
		}catch(InputMismatchException e){
			System.out.println("整数以外の文字が入力されました。");
			System.out.println("もう一度入力してください");
			ans = in.nextInt();
    	}
		return ans;
    }

    @Override
    public String inputName(){
        System.out.print("名前 => ");
        String name = in.nextLine();
        
        if( name == "" ){
        	name ="無名";
        }
        
        return name;
    }
} 
