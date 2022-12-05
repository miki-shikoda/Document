package makeQuestion.model;

import makeQuestion.bean.Record;

import java.util.ArrayList;

/*
  ���ԋL�^�̃����L���O
*/
public class Ranking{
    private final int RANKNUM = 10;                              //�����L���O�L�^��
    private ArrayList<Record> records = new ArrayList<Record>(); //�����L���O�L�^�p�z��
    
    //--------------------------------------------------------------------------
    // �����L���O���X�g�擾
    // ���� : �Ȃ�
    // �ߒl : �����L���O���X�g
    //--------------------------------------------------------------------------
    public ArrayList<Record> getRecords(){
        return records;
    }
    
    //--------------------------------------------------------------------------
    // �����N����
    // ���� : �񓚎���(s) �듚�̏ꍇ�͕�
    // �ߒl : ���� �����N�O�̏ꍇ��0
    //--------------------------------------------------------------------------
    public int judge(double time){
        if(time < 0.0){
            return 0;
        }
        
        int rank = 0;
        while(rank < records.size() && records.get(rank).getTime() < time){
            rank++;
        }
        if(rank < RANKNUM){
            return (rank + 1);
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------
    // �����L���O�o�^
    // ���� : �o�^����Record
    // �ߒl : ���� �����N�O�̏ꍇ��0
    //--------------------------------------------------------------------------
    public int insert(Record newRecord){
        double newTime = newRecord.getTime();
        int newRank = judge(newTime);
        
        if(newRank < 1){
            return 0;
        }
        
        records.add(newRank - 1, newRecord);
        while(records.size() > RANKNUM){
            records.remove(RANKNUM);
        }
        return newRank;
    }
}
