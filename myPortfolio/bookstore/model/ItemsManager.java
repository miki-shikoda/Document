//�쐬��:�ѕx

package bookstore.model;

import java.util.ArrayList;
import bookstore.beans.Item;
import bookstore.dao.ItemDataAccessor;
import bookstore.exceptions.EmptyItemsConditionException;
import bookstore.exceptions.NoItemsException;
import bookstore.exceptions.CannotSearchItemsException;


public class ItemsManager{  //���i���������N���X
	//�t�B�[���h : �Ȃ�
	//�R���X�g���N�^ : �f�t�H���g�R���X�g���N�^
	public ItemsManager(){
	}
	//���\�b�h
	public ArrayList<Item> search(Item key) throws EmptyItemsConditionException,
	NoItemsException, CannotSearchItemsException{

		try{
			if(key.getCode() == null && key.getName() == null || key.getCode() == null && key.getName() == ""  ){  //���������̏��i�R�[�h�Ə��i���̂������null�܂��͋󕶎��������ꍇ
			
				throw new EmptyItemsConditionException("���i�������ʂ��󔒂ł�");
			}
			
			ItemDataAccessor da  = new ItemDataAccessor();
			ArrayList<Item> list = new ArrayList<>();
			
			try{
				list = da.select(key);
				if(list.size() == 0){  //�����������ʁA1�����q�b�g���Ȃ������ꍇ
					throw new NoItemsException("���i��������܂���ł���");
				}
				
			}catch(NoItemsException e){
			 throw e;
			
			}catch(CannotSearchItemsException e){
			 throw e;
			
		}
		
		return list;
		
		}catch(EmptyItemsConditionException e){ //��������̗��R�ŏ��i�����o���Ȃ������ꍇ
		
		 throw e;
		}
	}
}

