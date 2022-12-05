//�ɓ�
package makeQuestion.model;
import java.util.Random;

//�|���Z����\���N���X
public class MulQuestion extends Question{
    private static Random r = new Random();
    private int num1;
    private int num2;
    
    @Override
    public String next(){
        //�����_���ɏo��(0�`9)
        num1 = r.nextInt(10);
        num2 = r.nextInt(10);
        //�C���g�^��String�^�ɕϊ�
        String a = String.valueOf(num1);
        String b = String.valueOf(num2);
        String c = a + "*" + b;
        //�ߒl�A�쐬�������̕�����
        return c;
    }
    //�񓚂̐��딻�������
    @Override
    public boolean judge(int ans){
        if((num1 * num2) == ans){  //����
            return true;     //�ߒl
        }else{
            return false;
        }
    }
    
    @Override
    public String getCurrent(){
        //���݂̖��̕�����𓾂�
        String a = String.valueOf(num1);
        String b = String.valueOf(num2); 
        String c = a + "*" + b;
        //�ߒl�A���݂̖��̕�����
        return c;
    } 
    
}


//�u�q�cver------------------
//�|���Z����\���N���X
/*
public class MulQuestion extends Question{
    private static Random r = new Random();
    private int num1;
    private int num2;

//�����_���ɏo��(0�`9)--------------
    @Override
    public String next(){
        num1 = r.nextInt(10);
        num2 = r.nextInt(10);
        return num1 + "*" + num2 ;
    }
    
//�񓚂̐��딻��--------------------
    @Override
    public boolean judge(int ans){
    	return (ans == num1 * num2 );
    }

//�ߒl�A���݂̖��̕�����----------
    @Override
    public String getCurrent(){
        return num1 + "*" + num2 ;
    } 
}
*/