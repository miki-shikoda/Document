//�������q
package makeQuestion.view;

//����Ȃ��jimport makeQuestion.bean.Record;
//����Ȃ��jimport makeQuestion.model.Ranking;
//����Ȃ��jimport java.util.ArrayList;

import java.util.Scanner;

/*
�o��v���O�����̃��[�U�[�C���^�[�t�F�[�X��񋟂���N���X�B
*/

public class QuesUI extends AddUI{ 
    private Scanner in = new Scanner(System.in);
    
    public int selectQuestion(){
        System.out.print("1:�����Z�@2:�����Z�@3:�|���Z�@���̑�:����Z  => ");
        return in.nextInt();
    }
    
} 
        
