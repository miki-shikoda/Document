package makeQuestion.model;
//�����Z����\���N���X						�쐬�ҁF�u�q�c
import java.util.Random;
public class SubQuestion extends Question{
	private Random r = new Random();
	private int num1;
	private int num2;
	
/**----------------------------------
* ���̖����쐬����B�������&�L�q�����犮����2�߂̐�������ѓ����� 0�`9 �̐����ɂ���B������
* [�ߒl]�쐬�������̕���
----------------------------------*/
	@Override
	public String next(){     
		//super.next();
        //num1 = r.nextInt(10);
        num2 = r.nextInt(10);
        int ans = r.nextInt(10);
        num1 = num2 + ans;
        /*while( 0 > num1-num2 ){
	        num1 = r.nextInt(10);
	        num2 = r.nextInt(10);
        }*/
        return num1 + " - " + num2;
	}
/*----------------------------------
�񓚂̐��딻�������
[����]int ans�F���肷���
[�ߒl]���茋�� true�F����, false�F�듚
----------------------------------*/
	@Override
	public boolean judge(int ans){
		return (ans == num1 - num2);
	}

/*----------------------------------
���݂̖��̕�����𓾂�
[�ߒl]���݂̖��̕���
----------------------------------*/
	@Override
	public String getCurrent(){
		return num1 + " - " + num2;
	}
}