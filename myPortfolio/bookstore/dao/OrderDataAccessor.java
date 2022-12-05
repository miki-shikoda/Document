package bookstore.dao;
//�쐬�ҁF�u�q�c
//�R���p�C���p javac bookstore\dao\OrderDataAccessor.java
//���s�p java bookstore.dao.OrderDataAccessor
import bookstore.beans.Item;
import bookstore.beans.Order;
import bookstore.beans.OrderCondition;
import bookstore.exceptions.*;
//�o�^�ŕK�v
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
//�����ŕK�v
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class OrderDataAccessor{
/*	---�������ǉ��o�^---
�o�^���ɓ��Y�����̒����ԍ��𔭍s(�������̒����ԍ��̍ő�l��+1 �����ԍ�)
[����]�ǉ��o�^���钍���̏��[�ߒl]���Y�����Ŕ��s���������ԍ��B
[��O]CannotAddOrderException : ���炩�̗��R�Œ����o�^�ł��Ȃ������ꍇ�B
�E���s���������ԍ��́A�ߒl�ŕԂ��ƂƂ��ɁA���� newOrder �̃C���X�^���X�ɂ��Z�b�g����
------------------------------------------------------------*/	
	public int insert(Order newOrder) throws CannotAddOrderException{
		//�����ԍ���newOrder�̒����ԍ��փZ�b�g����
		int num = num();
		//�ǉ��o�^�Ώۂ̃f�[�^���Q�b�g����iCalendar��Date��String�j
		Calendar orderdate = newOrder.getDatetime(); 	//�J�����_�[�^�ֈ����̒��������Q�b�g����Borderdate
		Date d = new Date();
		d = orderdate.getTime();					//�Q�b�g����������������t�^�֕ύX
    	String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
		
		Item item = newOrder.getItem(); 				//��U�AnewOrder��Item�C���X�^���X���擾
		String code = item.getCode();					//���ꂩ��AitemCode���擾
		int quantity = newOrder.getQuantity(); 			//���ʁBquantity 
		String sei = newOrder.getSei(); 				//�w���Ґ��Bsei
		String mei = newOrder.getMei(); 				//�w���Җ��Bmei
		String pref = newOrder.getPref(); 				//�w���ҏZ���s���{�� pref
		String add = newOrder.getAdd(); 				//�w���ҏZ���s���{���ȍ~ address 
		String tel = newOrder.getTel(); 				//�w���ғd�b�ԍ� tel 
		String mail = newOrder.getMail(); 				//�w���҃��[���A�h���X mail
		
		//JDBC�h���C�o�����[�h����
		Connection conn = null;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			try{
				conn.close();
            }catch(SQLException e2){
                throw new CannotAddOrderException("");
            }
            throw new CannotAddOrderException("");
		}
		//�f�[�^�x�[�X�ւ̐ڑ�
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass); 
		}catch (SQLException e){
			try{
				conn.close();
            }catch(SQLException e2){
                throw new CannotAddOrderException("");
            }
			throw new CannotAddOrderException("");
		}
		
		//SQL������
		PreparedStatement ps = null;
		ps = null;//INSERT INTO orders(no,orderdate,itemCode,quantity,sei,mei,pref,address,tel,mail) VALUES(1,'2022-5-18 13:35:22','P001',1,'���','�G','�{�錧','���s�t��','022-222-2222',null);
		try{
			String sql = "INSERT INTO orders(no,orderdate,itemCode,quantity,sei,mei,pref,address,tel,mail) VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1,++num);
			ps.setString(2,str);
			ps.setString(3,code);
			ps.setInt(4,quantity);
			ps.setString(5,sei);
			ps.setString(6,mei);
			ps.setString(7,pref);
			ps.setString(8,add);
			ps.setString(9,tel);
			ps.setString(10,mail);
			
		}catch (SQLException e){
			throw new CannotAddOrderException("");
		}
		
		//SQL����
		try{
			ps.executeUpdate();
		}catch (SQLException e){
			throw new CannotAddOrderException("");
		}finally{
			try{
				conn.close();
			}catch (SQLException e2){
				throw new CannotAddOrderException("");
			}
			newOrder.setNo(num);			
			return num;
		}
	}
	
/*	---������񌟍�---
[����]�����̌��������B [�ߒl]�����𖞂����������� Order �C���X�^���X�̃��X�g�B
[��O]CannotSearchOrdersException : ���炩�̗��R�Œ��������ł��Ȃ������ꍇ�B
�E�����ԍ��A�d�b�ԍ��͊��S��v����  /  �E�����A���i���͕�����v����
�E�����̏����́A�����邢�͖��̂ǂ��炩����ɃZ�b�g����΁A�������̐��Ɩ��̗����Ō��������B��������ђ������̂ǂ�������Ɩ����������A���̌�������������Ō���������@���l������B
�E�������ʁA���[���A�h���X�A�s���{������ѓs���{���ȍ~�͌��������Ƃ��Ďg�p���Ȃ��B
�E�����̍��ڂɌ��������������Ă���Ƃ��́AAND ����(�S�Ă̏����𖞂������������𒊏o)����B
�Eorder.csv ����Ǐo�����������ɂ́A���i�R�[�h�͂����Ă����i���͂Ȃ��B
���������āAItemDataAccessor#select()�𗘗p���āA���i�R�[�h����Y������ 
Item �C���X�^���X�𓾂�K�v������
------------------------------------------------------------*/
	
	public ArrayList<Order> select(OrderCondition key)throws CannotSearchOrdersException{
		ArrayList<Order> item = null;
		
		//�����L�[���[�h�����l (=�f�[�^�x�[�Xorders�ɓo�^����Ă��鍀�ڂƈ�v������e)
		int no = key.getNo();					//�����ԍ����S��v no = ?
		
		//��������(��) ���J�����_�[�^����date�^�֕ύX
		Calendar orderdate = key.getDatetime();
		String date1 = null;
		Date orderDate = null;
		if(orderdate != null){
			orderDate = new Date();
			orderDate = orderdate.getTime();
	    	date1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(orderDate);//hh:mm:ss
		}
		
		//���������̎����������B
		Calendar enddate = key.getEnddate();
		String date2 = null;
		Date orderEnd = null;
		if(enddate != null){
			orderEnd = new Date();
			orderEnd = enddate.getTime();
	    	date2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(orderEnd);//hh:mm:ss
	    	
		}else if(orderdate != null && enddate == null){ //orderdate�����t�w�肠��Aenddate��null�̏ꍇ
			orderEnd = new Date();
			orderEnd = orderdate.getTime();
	    	date2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(orderEnd);//hh:mm:ss
	    	//orderdate ��null�ł͂Ȃ��ꍇ�A�����̂Ƃ���"yyyy-MM-dd 23:59:59"�Ō������Ȃ��Ƃ����Ȃ�
		}
		
		String code = null;						// ���i�R�[�h (�����l�Ɏg��Ȃ�)
		int quantity = 0; 					// ����  (�����l�Ɏg��Ȃ�)
		String sei = key.getSei(); 				//�w���Ґ�������v sei LIKE ?
		String mei = key.getMei(); 				//�w���Җ�������v mei LIKE ?
		String pref = null; 					// �w���ҏZ���s���{��   (�����l�Ɏg��Ȃ�)
		String add = null; 						// �w���ҏZ���s���{���ȍ~   (�����l�Ɏg��Ȃ�)
		String tel  = key.getTel(); 			//�w���ғd�b�ԍ����S��v tel = ?
		String mail = null;						// �w���҃A�h���X (�����l�Ɏg��Ȃ�)
		//�����l�����Ɏg��
		Item searchitem = key.getItem();		//���i��������v LIKE ?
		String name = null;
		if(searchitem != null){
			name = searchitem.getName();
		}
		//�Ō��ArrayList��add����Ƃ��Ɏg��
		int price = 0;    //�P��
		
	//�h���C�o���[�h
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			//System.out.println("EE1");
			throw new CannotSearchOrdersException("");
		}
	//�f�[�^�x�[�X�ւ̐ڑ�
		Connection conn = null;
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass);
		}catch (SQLException e){
			//System.out.println("EE2");
			throw new CannotSearchOrdersException("");
		}
		//SQL������
		PreparedStatement ps = null;
		String sql = "";
		try{ 
			//�����ԍ����������͂���Ă���ꍇ
			if( no > 0 && ( sei == null || sei == "") && ( tel == null || tel == "") && ( name == null || name == "") && date1 == null && date2 == null ){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE no = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,no);
				//System.out.println("�����ԍ������Ō������s");
				
			//�������钍�������������͂���Ă���
			}else if( date1 != null && date2 != null && ( no == 0 ) && ( sei == null || sei == "") && ( tel == null || tel == "") && ( name == null || name == "") ){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE (orderdate >= ?) AND (orderdate <= ?)";//BETWEEN ? AND ?
				ps = conn.prepareStatement(sql);
				ps.setString(1,date1);
				ps.setString(2,date2);
				//System.out.println("������(����)�����Ō������s");
				//System.out.println(date1+"|"+date2);
			
			//�����ǂ��炩���L������Ă���
			}else if( sei != null &&  no == 0  && date1 == null && date2 == null && tel == null && name == null ){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE sei LIKE ? OR mei LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,"%" + sei + "%");
				ps.setString(2,"%" + mei + "%");
				//System.out.println("���������Ō������s");
				
			//�d�b�ԍ����������͂���Ă���ꍇ
			}else if( (tel != "" || tel != null ) && ( no == 0 ) && ( sei == null || sei == "" ) && date1 == null && date2 == null && ( name == null || name == "" )){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE tel = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,tel);
				//System.out.println("�d�b�ԍ������Ō������s");
				
			//���i�����������͂���Ă���ꍇ
			}else if( name != null && ( sei == null || sei == "") && ( tel == null || tel == "") && ( no == 0 ) && date1 == null && date2 == null ){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code WHERE name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,"%" + name + "%");
				//System.out.println("���i�������Ō������s");
				
			}else if( date1 == null && date2 != null && ( sei != null || sei != "") && ( tel == null || tel == "") && ( no == 0 ) && ( name != null || name != "" )){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE orderdate < ? AND sei LIKE ? OR mei LIKE ? AND name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,date2);
				ps.setString(2,"%" + sei + "%");
				ps.setString(3,"%" + mei + "%");
				ps.setString(4,"%" + name + "%");
				//System.out.println("������(��)�A�����A���i���Ō������s");
				
			}else if( date1 != null && date2 != null && ( no == 0 ) && ( sei != null || sei != "") && ( tel == null || tel == "") && ( name != null || name != "" )){ 
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE (orderdate >= ?) AND (orderdate <= ?) AND sei LIKE ? OR mei LIKE ? AND name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,date1);
				ps.setString(2,date2);
				ps.setString(3,"%" + sei + "%");
				ps.setString(4,"%" + mei + "%");
				ps.setString(5,"%" + name + "%");
				//System.out.println("�������A�����A���i���Ō������s");
				
			}else if( no == 0 && date1 == null && date2 == null &&  ( tel == null || tel == "") && ( sei != null || sei != "") && ( name != null || name != "")){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE sei LIKE ? OR mei LIKE ? AND name LIKE ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,"%" + sei + "%");
				ps.setString(2,"%" + mei + "%");
				ps.setString(3,"%" + name + "%");
				//System.out.println("�w���҂Ə��i�������Ō������s");
			
			}else if(  date1 != null && date2 != null && ( no != 0 ) &&( sei != null || sei != "") && ( tel != null || tel != "") && name != null ){
				sql = "SELECT * FROM orders INNER JOIN items ON orders.itemCode = items.code";
				sql += " WHERE (orderdate BETWEEN ? AND ?) AND ( sei LIKE ? OR mei LIKE ?) AND name LIKE ? AND tel = ? AND no = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1,date1);
				ps.setString(2,date2);
				ps.setString(3,"%" + sei + "%");
				ps.setString(4,"%" + mei + "%");
				ps.setString(5,"%" + name + "%");
				ps.setString(6,tel);
				ps.setInt(7,no);
				//System.out.println("�S���ڂŌ������s");
				
			}else{
				System.out.println("Error�FOrderDataAccessor");//��O�̏����ł�
			}
		
		}catch (SQLException e){
			try{
				conn.close();   //�f�[�^�x�[�X�ؒf
			}catch (SQLException e2){
				//System.out.println("EE3");
				throw new CannotSearchOrdersException("");
			}
			//System.out.println("EE4");
			throw new CannotSearchOrdersException("");
		}
		//SQL���s
		ResultSet rs = null;
		try{
			conn.setAutoCommit(false); //�g�����U�N�V�����J�n(MySQL��BEGIN�Ɠ���)
			rs = ps.executeQuery();
			conn.commit();             //�R�~�b�g(COMMIT�Ɠ���)
		}catch (SQLException e){
			try{
				ps.close();
			}catch (SQLException e2){
				//System.out.println("EE5");
				throw new CannotSearchOrdersException("");
			}
			try{
				conn.close();
			}catch (SQLException e2){
				//System.out.println("EE6");
				throw new CannotSearchOrdersException("");
			}
			//System.out.println("EE7");
			//throw new NoOrderException("")�͂ǂ��ł��̂��H
			throw new CannotSearchOrdersException("");
		}
		//���ʂ̎擾
		item = new ArrayList<Order>();
		try{
			while(rs.next()){
				//System.out.println("while���s���B");
				no = rs.getInt("no");					//�����ԍ�
				date1 = rs.getString("orderdate"); 		//��������
				code = rs.getString("itemCode");		// ���i�ԍ�
				quantity = rs.getInt("quantity"); 		// ����
				
				sei = rs.getString("sei"); 				//�w���Ґ�
				mei = rs.getString("mei"); 				//�w���Җ�
				pref = rs.getString("pref"); 			// �w���ҏZ���s���{��
				add = rs.getString("address"); 			// �w���ҏZ���s���{���ȍ~
				tel = rs.getString("tel"); 				//�w���ғd�b�ԍ�
				mail = rs.getString("mail");			// �w���҃A�h���X
				name = rs.getString("name");				//�w�����i��
				price = rs.getInt("price");					//���i���z
				
				//String��date�^��Calendar�^�֕ϊ�
    			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	try{
		    		orderDate = sdFormat.parse(date1);
		    	}catch (ParseException e) {
		    	}
				Calendar cal = Calendar.getInstance();
				cal.setTime(orderDate);
				
				Item additem = new Item(code,name,price);
				Order addorder = new Order();
				addorder.setNo(no);
				addorder.setDatetime(cal);
				addorder.setItem(additem);
				addorder.setQuantity(quantity);
				addorder.setSei(sei);
				addorder.setMei(mei);
				addorder.setPref(pref);
				addorder.setAdd(add);
				addorder.setTel(tel);
				addorder.setMail(mail);
				item.add(addorder);  //ArrayList��add
				//System.out.println(no+"|"+date1+"|"+code+"|"+quantity+"|"+sei+"|"+mei+"|"+pref+"|"+add+"|"+tel+"|"+mail+"|"+addorder);
			}
		}catch (SQLException e2){
			//System.out.println("EE8");
			throw new CannotSearchOrdersException("");
		}
		//�N���[�Y ??
		try{
			rs.close();
		}catch (SQLException e2){
			//System.out.println("EE9");
			throw new CannotSearchOrdersException("");
		}
		try{
			ps.close();
		}catch (SQLException e2){
			//System.out.println("EE10");
			throw new CannotSearchOrdersException("");
		}
		try{
			conn.close();
		}catch (SQLException e2){
			//System.out.println("EE11");
			throw new CannotSearchOrdersException("");
		}
		//�ߒl ArrayList<Order>�����^�[������
		return item;
	}
	
	
//-----*-----
	public int num(){
		//JDBC�h���C�o�����[�h����
		Connection conn = null;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			try{
				conn.close();
            }catch(SQLException e2){
                System.out.println("E01");
            }
            System.out.println("E02");
		}
		//�f�[�^�x�[�X�ւ̐ڑ�
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass); 
		}catch (SQLException e){
			try{
				conn.close();
            }catch(SQLException e2){
                System.out.println("E03");
            }
			System.out.println("E04");
		}
		
		//SQL������(�����ԍ��ő�l����肽��)
		PreparedStatement ps = null;
		try{
			String sql = "SELECT MAX(no) AS no FROM orders";
			ps = conn.prepareStatement(sql);
		}catch (SQLException e){
			try{
				conn.close();   //�f�[�^�x�[�X�ؒf
			}catch (SQLException e2){
				System.out.println("E05");
			}
			System.out.println("E06");
		}
		//SQL���s
		ResultSet rs = null;
		try{
			conn.setAutoCommit(false); //�g�����U�N�V�����J�n(MySQL��BEGIN�Ɠ���)
			rs = ps.executeQuery();
			conn.commit();             //�R�~�b�g(COMMIT�Ɠ���)
		}catch (SQLException e){
			try{
				ps.close();
			}catch (SQLException e2){
				System.out.println("E07");
			}
			try{
				conn.close();
			}catch (SQLException e2){
				System.out.println("E08");
			}
			System.out.println("E09");
		}
		int no = 12;
		//�����ԍ��ő�l�̌��ʎ擾
		try{
			while(rs.next()){
				no = rs.getInt("no");
			}
		}catch (SQLException e){
			System.out.println("E10");
		}
		
		//�N���[�Y
		try{
			rs.close();
		}catch (SQLException e2){
			System.out.println("E11");
		}
		try{
			ps.close();
		}catch (SQLException e2){
			System.out.println("E12");
		}
		try{
			conn.close();
		}catch (SQLException e2){
			System.out.println("E13");
		}
		return no;
	}

//-----	
	
	/*public static void main(String[] args){
	
		//�����L�[���[�h�����l (=�f�[�^�x�[�Xorders�ɓo�^����Ă��鍀�ڂƈ�v������e)
		int no = 5;								//�����ԍ����S��v no = ?
		String sei = ""; 					//�w���Ґ�������v sei LIKE ?
		String mei = ""; 					//�w���Җ�������v mei LIKE ?
		String tel  = "0120-000-000"; 			//�w���ғd�b�ԍ����S��v tel = ?
		//�����l�����Ɏg��
		String code = null;  		//���i�R�[�h
	  	String name = "��b";  		//���i��
	  	int price = 0;    			//�P��
		Item item = new Item(code,name,price);
		OrderCondition test = new OrderCondition();
		
		test.setNo(no);
		test.setItem(item);
		//test.setSei(sei);
		//test.setMei(mei);
		test.setTel(tel);
		
		System.out.println(test.getNo());
		
		OrderDataAccessor testOda = new OrderDataAccessor();
		
		try{
			testOda.select(test);
		}catch (CannotSearchOrdersException e){
			System.out.println("E20");
			return;
		}
	}*/
	
			/*}catch (CannotAddOrderException e){
			System.out.println("E01");
			return;*/
}
