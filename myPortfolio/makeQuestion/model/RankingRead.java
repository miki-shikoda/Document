package makeQuestion.model;

import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.lang.ArrayIndexOutOfBoundsException;

public class RankingRead extends Ranking{
	private String[] ques = {"足し算","引き算","掛け算","割り算"};


//    CSV読み込みのためのメソッド------------
	public void Read(int num){
		//ArrayList<Record> ReadRecords = new ArrayList<Record>();
		ArrayList<Record> ReadRecords = super.getRecords();
		
		try( 
			FileReader fr = new FileReader("makeQuestion\\"+ques[num - 1]+"ランキング.csv");
			BufferedReader br = new BufferedReader(fr);
		){
		
//	  CSV読み込み&AlaayListへ
		String line = "";
		int i =0;
		while( (line = br.readLine()) != null ){
			if( i > 0 ){
				String[] dat = line.split(",");
				
				if(dat.length>3){
					String name = dat[3];
					double time = Double.parseDouble(dat[2]);
					//System.out.println(dat[2]+":"+dat[3]); //確認用
					Record rc = new Record(time, name);
					ReadRecords.add(rc);
				}
			}
		i++;
		}
		
//-------------------------------
		}catch(IOException e){
			System.out.println("ファイル読み込みエラーです。");
			
		}catch(ArrayIndexOutOfBoundsException e){
		
		}
	}
}
