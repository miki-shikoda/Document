package makeQuestion.view;

import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;

import java.util.Scanner;
import java.util.ArrayList;

/*
  ���o��̃��[�U�C���^�[�t�F�[�X
*/
public class AddUI{
    private Scanner in = new Scanner(System.in);
    
    //--------------------------------------------------------------------------
    // �R�}���h����
    // ���� : �Ȃ�
    // �ߒl : ���͂��ꂽ�R�}���h(Enter, r, e)
    //--------------------------------------------------------------------------
    public String inputCmd(){
        System.out.print("Enter���͂ŊJ�n(r:�����L���O�\��, e:�I��) => ");
        return in.nextLine();
    }
    
    //--------------------------------------------------------------------------
    // �����L���O�\��
    // ���� : ranking �����L���O�L�^
    // �ߒl : �Ȃ�
    //--------------------------------------------------------------------------
    public void showRanking(Ranking ranking){
        ArrayList<Record> records = ranking.getRecords();
        
        System.out.println("�`�`�����L���O�`�`");
        int rank = 1;
        for(Record curr : records){
            System.out.printf("%2d�� %.3f�b : %s\n", rank, curr.getTime(), curr.getName());
            rank++;
        }
        System.out.println();
    }
    
    //--------------------------------------------------------------------------
    // ���̖��\��
    // ���� : question ��蕶����
    // �ߒl : �Ȃ�
    //--------------------------------------------------------------------------
    public void showQuestion(String question){
        System.out.print(question + " => ");
    }
    
    //--------------------------------------------------------------------------
    // �񓚓���
    // ���� : �Ȃ�
    // �ߒl : ���͂��ꂽ��
    //--------------------------------------------------------------------------
    public int inputAns(){
        int ans = in.nextInt();
        in.nextLine();
        
        return ans;
    }
    
    //--------------------------------------------------------------------------
    // ���남��я��ʂ̕\��
    // ���� : result ����, rank ����(�����N�O��0), time �񓚎���
    // �ߒl : �Ȃ�
    //--------------------------------------------------------------------------
    public void showResult(boolean result, int rank, double time){
        if(result){
            System.out.print("������(" + time + "�b)");
            if(rank != 0){
                System.out.println("���߂łƂ��I" + rank + "�ʂł��I");
            }
        }else{
            System.out.println("�͂���");
            System.out.println();
        }
    }
    
    //--------------------------------------------------------------------------
    // ���O����
    // ���� : �Ȃ�
    // �ߒl : ���͂��ꂽ���O
    //--------------------------------------------------------------------------
    public String inputName(){
        System.out.print("���O => ");
        String name = in.nextLine();
        System.out.println();
        
        return name;
    }
}
