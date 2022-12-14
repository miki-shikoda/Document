package makeQuestion.bean;
/*
【作成者：チームIQ102】 
CSVに書き出す足し算クラス（のちにsuperになる）  */

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;
import java.util.ArrayList;
import makeQuestion.view.ErrorUI;   //★追加↓


public class QuesWriter{
    private double time;  //記録時間
    private String name;  //名前
	
//    CSV保存のためのメソッド------------
	public void Write(Ranking ranking){
		try(
			FileWriter fw = new FileWriter("makeQuestion\\足し算ランキング.csv",false); //true=追加/false=上書
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
		){
	        ArrayList<Record> records = ranking.getRecords();
	        int rank = 1;
	        String line = "種類"+","+"順位"+","+"タイム"+","+"名前"+",";
	        pw.println(line);
	        
	        for(Record curr : records){
	        	line = "足し算"+","+rank+","+curr.getTime()+","+curr.getName()+",";
				pw.println(line);
				pw.flush();  //一時的にメモリへ保存(バッファ) しているデータを出力先に書き出す
	            rank++;
	        }
        
		}catch(IOException e){
			ErrorUI err = new ErrorUI();  //★追加
			err.IOException(); //★追加
		}
	}
	
}
