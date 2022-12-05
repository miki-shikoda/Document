package bookstore.model;

import java.util.ArrayList;
import java.util.Calendar;
import bookstore.beans.Item;
import bookstore.beans.Order;
import bookstore.beans.OrderCondition;
import bookstore.dao.OrderDataAccessor;
import bookstore.model.ItemsManager;
import bookstore.exceptions.CannotAddOrderException;
import bookstore.exceptions.CannotSearchOrdersException;
import bookstore.exceptions.CannotSearchItemsException;
import bookstore.exceptions.EmptyNameException;
import bookstore.exceptions.EmptyAddException;
import bookstore.exceptions.EmptyOrdersConditionException;
import bookstore.exceptions.EmptyItemsConditionException;
import bookstore.exceptions.IllegalItemNoException;
import bookstore.exceptions.IllegalQuantityException;
import bookstore.exceptions.NoOrderException;
import bookstore.exceptions.NoItemsException;


public class OrdersManager{  //�������������N���X
	//�t�B�[���h�Ȃ�
	
    OrderDataAccessor oda = new OrderDataAccessor();
	//�R���X�g���N�^ : �f�t�H���g�R���X�g���N�^
    public OrdersManager(){
    }
	//���\�b�h
    public int add(Order newOrder) 
    throws IllegalItemNoException, IllegalQuantityException, EmptyNameException, 
    EmptyAddException, CannotAddOrderException,EmptyItemsConditionException{


    	try{ 
            ItemsManager im = new ItemsManager();
            
            ArrayList list = im.search(newOrder.getItem());
            if(list.get(0) == null){
                throw new IllegalItemNoException("���i�ԍ������݂��܂���");
            }
            int num = newOrder.getQuantity();
            if(num <= 0){
                throw new IllegalQuantityException("���ʂ��s���ł�");
            }
            if(newOrder.getSei() == null || newOrder.getMei() == null || newOrder.getMei() == "" || newOrder.getSei() == ""){
                throw new EmptyNameException("�w���Ҏ���(���܂��͖�)����̏ꍇ");
            }
            if(newOrder.getPref() == null || newOrder.getAdd() == null || newOrder.getAdd() == "" || newOrder.getPref() == ""){
                throw new EmptyAddException("�w���ҏZ��(�s���{���܂��͂���ȍ~)����̏ꍇ");
            }
            newOrder.setDatetime(Calendar.getInstance());
            
            int order = 0;
            try{
                
                order = oda.insert(newOrder);    //���炩�̗��R�Œ����o�^�ł��Ȃ������ꍇ
            }catch(CannotAddOrderException e){
                 throw e;
            }
            newOrder.setNo(order);
            return order;
            
              
        }catch(IllegalItemNoException e){  //�V�X�e���ɑ��݂��Ȃ����i�ԍ�
         throw e;
        
        }catch(IllegalQuantityException e){  //�c�ʂ��s���ȏꍇ
         throw e; 
        
        }catch(EmptyNameException e){  //�w���Ҏ�������
         throw e;
        
        }catch(EmptyAddException e){  //�Z������̏ꍇ
         throw e;
         
        }catch(EmptyItemsConditionException | NoItemsException | CannotSearchItemsException e){
         throw new IllegalItemNoException("���i�R�[�h�s��");
        }
         
    }
    public ArrayList<Order> search(OrderCondition key) throws EmptyOrdersConditionException,
    NoOrderException,CannotSearchOrdersException{
        ArrayList<Order> list = new ArrayList<>();

        try{  
            if(key.getNo() == 0 && key.getDatetime() == null && key.getItem() == null 
            && key.getSei() == null && key.getMei() == null && key.getTel() == null){  //�����������ЂƂ����͂���Ă��Ȃ��ꍇ
            throw new EmptyOrdersConditionException("����������������");
            }
                
            OrderDataAccessor da = new OrderDataAccessor();
            try{
                list = da.select(key);
                if(list.size() == 0){ //�����������ʁA1 �����q�b�g���Ȃ������ꍇ
                 throw new  NoOrderException("���i�������ʂȂ�");
                }
               
            }catch(NoOrderException e){
             throw e;
       
            }catch(CannotSearchOrdersException e){
             throw e;
            }
            return list;
        }catch(EmptyOrdersConditionException e){ //���炩�̗��R�ŏ��i�����ł��Ȃ������ꍇ
        	throw e;
		}
	}
}
