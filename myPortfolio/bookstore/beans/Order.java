package bookstore.beans;
import java.util.Calendar;
/*
-----�`�[�����Fbookstore     �쐬�ҁF�u�q�c-----
�R���p�C���p javac bookstore\beans\Order.java
���s�p java bookstore.beans.Order
*/
public class Order{
	private int no;					//�����ԍ��B�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB
	private Calendar datetime; 		//���������B�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB
	private Item item; 				//�������鏤�i�� Item �I�u�W�F�N�g�B
	private int quantity; 			//���ʁB�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB 
	private String sei; 			//�w���Ґ��B�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB 
	private String mei; 			//�w���Җ��B�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB 
	private String pref; 			//�w���ҏZ���s���{���B�u�d�l�� 4.2.�f�[�^�`���ڍׁv���Q�ƁB 
	private String add; 			//�w���ҏZ���s���{���ȍ~�B�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB 
	private String tel; 			//�w���ғd�b�ԍ��B�d�l�́u4.2.�f�[�^�`���ڍׁv���Q�ƁB 
	private String mail; 			//�w���҃��[���A�h���X�B�u�d�l�� 4.2.�f�[�^�`���ڍׁv���Q�ƁB

	public Order() {
	}

	public void setNo(int no) {
	/*�����ԍ��ɒl���Z�b�g���� [����]�Z�b�g���钍���ԍ�*/
		this.no = no;
	}
	
	public void setDatetime(Calendar datetime) {
	/*���������ɒl���Z�b�g���� [����]�Z�b�g���钍������*/
		this.datetime=datetime;
	}
	
	public void setItem(Item item) {
	/*�������鏤�i�� Item �C���X�^���X���Z�b�g���� [����]�Z�b�g���� Item �C���X�^���X*/
		this.item = item;   //�H�H
	}
	
	public void setQuantity(int quantity) {
	/*�������ʂ̒l���Z�b�g���� [����]�Z�b�g���鐔��*/
		this.quantity=quantity;
	}

	public void setSei(String sei) {
	/*�w���Ґ��̒l���Z�b�g���� [����]�Z�b�g����w���Ґ�*/
		this.sei=sei;
	}

	public void setMei(String mei) {
	/*�w���Җ��̒l���Z�b�g����[����]�Z�b�g����w���Җ�*/
		this.mei=mei;
	}

	public void setPref(String pref) {
	/*�w���ҏZ���s���{���̒l���Z�b�g���� [����]�Z�b�g����w���ҏZ���s���{��*/
		this.pref=pref;
	}
	
	public void setAdd(String add) {
	/*�w���ҏZ���s���{���ȍ~�̒l���Z�b�g����
	[����]�Z�b�g����w���ҏZ���s���{���ȍ~*/
		this.add=add;
	}
	
	public void setTel(String tel) {
	/*�w���ғd�b�ԍ��̒l���Z�b�g����
	[����]�Z�b�g����w���ғd�b�ԍ�*/
		this.tel=tel;
	}
	
	public void setMail(String mail) {
	/*�w���҃��[���A�h���X�̒l���Z�b�g����
	[����]�Z�b�g����w���҃��[���A�h���X*/	
		this.mail=mail;
	}
	
	public int getNo() {
	/*�����ԍ��̒l�𓾂� [�ߒl]�����ԍ��̒l*/	
		return no;
	}
	
	public Calendar getDatetime() {
	/*���������̒l�𓾂� [�ߒl]���������̒l*/
		return datetime;
	}
	
	public Item getItem() {
	/*�������i�� Item �C���X�^���X�𓾂�
	[�ߒl]�������i�� Item �C���X�^���X*/
		return item;
	}
	
	public int getQuantity() {
	/*�������ʂ̒l�𓾂� [�ߒl]�������ʂ̒l*/
		return quantity;
	}
	
	public String getSei() {
	/*�w���Ґ��̒l�𓾂� [�ߒl]�w���Ґ��̒l*/
		return sei;
	}
	
	public String getMei() {
	/*�w���Җ��̒l�𓾂� [�ߒl]�w���Җ��̒l*/
		return mei;
	}
	
	public String getPref() {
	/*�w���ҏZ���s���{���𓾂� [�ߒl]�w���ҏZ���s���{���̒l*/
		return pref;
	}
	
	public String getAdd() {
	/*	�w���ҏZ���s���{���ȍ~�𓾂� 
		[�ߒl]�w���ҏZ���s���{���ȍ~*/
		return add;
	}
	
	public String getTel() {
	/*�w���ғd�b�ԍ��𓾂� [�ߒl]�w���ғd�b�ԍ�*/
		return tel;
	}
	
	public String getMail() {
	/*�w���҃��[���A�h���X���� [�ߒl]�w���҃��[���A�h���X*/
		return mail;
	
	}
}