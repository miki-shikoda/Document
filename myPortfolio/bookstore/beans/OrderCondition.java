package bookstore.beans;
import java.util.Calendar;
/*����������\���N���X�B   �쐬�ҁF�u�q�c
�E�� �������� : Order#datetime �ɃZ�b�g����
�E�� �������� : OrderCondition#enddate �ɃZ�b�g����
��L�ȊO�̒��������� Order ����p���������ڂ����̂܂܎g�p����B*/

public class OrderCondition extends Order{
	private Calendar enddate;   //���������̎����������B
	
	public OrderCondition(){
	}
	public void setEnddate(Calendar enddate){
	/*�����������ɒl���Z�b�g���� [����]�Z�b�g���鎊��������*/
		this.enddate = enddate;
	}
	
	public Calendar getEnddate(){
	/*�����������̒l�𓾂� [�ߒl]�����������̒l */
		return enddate;
	}
}
