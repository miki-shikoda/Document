package makeQuestion.model;

import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.lang.ArrayIndexOutOfBoundsException;

public class RankingReadTest extends Ranking{
	private String[] ques = {"�����Z","�����Z","�|���Z","����Z"};


//    CSV�ǂݍ��݂̂��߂̃��\�b�h------------
	public void Read(int num){
		//ArrayList<Record> ReadRecords = new ArrayList<Record>();
		ArrayList<Record> ReadRecords = super.getRecords();
		
		try(
			/*BufferedReader br = new BufferedReader(new InputStreamReader(
			new FileInputStream("makeQuestion\\"+ques[num - 1]+"�����L���O.csv"),Charset.forName("Shift-JIS")))){*/
			FileReader fr = new FileReader("makeQuestion\\"+ques[num - 1]+"�����L���O.csv");
			BufferedReader br = new BufferedReader(fr);
			){
//	  CSV�ǂݍ���&AlaayList��
		String line = "";
		int i =0;
		
/*�u�q�cver*/
		while( (line = br.readLine()) != null ){
			if( i > 0 ){
				String[] dat = line.split(",");
				
				//if(dat.length>3){
					String name = dat[3];
					double time = Double.parseDouble(dat[2]);
					System.out.println(dat[2]+":"+dat[3]); //�m�F�p
					Record rc = new Record(time, name);
					ReadRecords.add(rc);
				//}
			}
		i++;
		}
		
/*rankUPVER

         while((line = br.readLine()) != null){
         	if(i >= 1){
            	String[] dat = line.split(",");
                double time = Double.parseDouble(dat[1]);
                String name = dat[2];
				System.out.println(dat[2]+":"+dat[3]); //�m�F�p
                Record rec = new Record(time, name);
                ReadRecords.add(rec);
            }
         i++;
         }*/
		
/*���P*/

         	

//-------------------------------
		/*}catch(FileNotFoundException e){
			System.out.println("�w�肳�ꂽ�t�@�C��������܂���B");*/
		}catch(IOException e){
			System.out.println("�t�@�C���ǂݍ��݃G���[�ł��B");
			
		/*}catch(ArrayIndexOutOfBoundsException e){*/
		}
	}
	
	public static void main(String[] args){
		RankingReadTest test = new RankingReadTest();
		int num = 1;
		test.Read(num);
	}
}
