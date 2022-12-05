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
	private String[] ques = {"�����Z","�����Z","�|���Z","����Z"};


//    CSV�ǂݍ��݂̂��߂̃��\�b�h------------
	public void Read(int num){
		//ArrayList<Record> ReadRecords = new ArrayList<Record>();
		ArrayList<Record> ReadRecords = super.getRecords();
		
		try( 
			FileReader fr = new FileReader("makeQuestion\\"+ques[num - 1]+"�����L���O.csv");
			BufferedReader br = new BufferedReader(fr);
		){
		
//	  CSV�ǂݍ���&AlaayList��
		String line = "";
		int i =0;
		while( (line = br.readLine()) != null ){
			if( i > 0 ){
				String[] dat = line.split(",");
				
				if(dat.length>3){
					String name = dat[3];
					double time = Double.parseDouble(dat[2]);
					//System.out.println(dat[2]+":"+dat[3]); //�m�F�p
					Record rc = new Record(time, name);
					ReadRecords.add(rc);
				}
			}
		i++;
		}
		
//-------------------------------
		}catch(IOException e){
			System.out.println("�t�@�C���ǂݍ��݃G���[�ł��B");
			
		}catch(ArrayIndexOutOfBoundsException e){
		
		}
	}
}
