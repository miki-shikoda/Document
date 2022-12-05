package makeQuestion.bean;
/*   �y�쐬�ҁF�`�[��IQ102�z CSV�ɏ����o���N���X   */
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;
import java.util.ArrayList;
import makeQuestion.view.ErrorUI;   //���ǉ�

public class MulWriter extends QuesWriter{
    private double time;  //�L�^����
    private String name;  //���O
    
    @Override
	public void Write(Ranking ranking){
		try(
			FileWriter fw = new FileWriter("makeQuestion\\�|���Z�����L���O.csv",false); //true=�ǉ�/false=�㏑
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
		){
	        ArrayList<Record> records = ranking.getRecords();
	        int rank = 1;
	        String line = "���"+","+"����"+","+"�^�C��"+","+"���O"+",";
	        pw.println(line);
	        
	        for(Record curr : records){
	        	line = "�|���Z"+","+rank+"��"+","+curr.getTime()+","+curr.getName()+",";
				pw.println(line);
				pw.flush();  //�ꎞ�I�Ƀ������֕ۑ�(�o�b�t�@) ���Ă���f�[�^���o�͐�ɏ����o��
	            rank++;
	        }
        
		}catch(IOException e){
			ErrorUI err = new ErrorUI();  //���ǉ�
			//System.out.println("�t�@�C�����J���܂���ł����B");//��������
		}
	}
}
