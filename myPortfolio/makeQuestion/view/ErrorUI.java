package makeQuestion.view;


/*
�o��v���O�����̃��[�U�[�C���^�[�t�F�[�X��񋟂���N���X��
�p�����AError��\�����邽�߂̃N���X�B
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
		System.out.println("�t�@�C�����J���܂���ł����B");
    }
    
    @Override
    public int inputAns(){
    	int ans;
    	try{
		    ans = in.nextInt();
		    in.nextLine();
    	
		}catch(InputMismatchException e){
			System.out.println("�����ȊO�̕��������͂���܂����B");
			System.out.println("������x���͂��Ă�������");
			ans = in.nextInt();
    	}
		return ans;
    }

    @Override
    public String inputName(){
        System.out.print("���O => ");
        String name = in.nextLine();
        
        if( name == "" ){
        	name ="����";
        }
        
        return name;
    }
} 
