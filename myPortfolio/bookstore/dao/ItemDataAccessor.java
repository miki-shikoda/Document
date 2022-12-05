package bookstore.dao;
//�R���p�C���p javac bookstore\dao\ItemDataAccessor.java
//���s�p java bookstore.dao.ItemDataAccessor

import bookstore.beans.Item;
import bookstore.exceptions.*;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDataAccessor{
	public ItemDataAccessor(){
	//�������Ȃ�
	}

/* ---���� key �ɐݒ肳�ꂽ�����ɏ]���ď��i�����������A�����𖞂��������i�̈ꗗ��߂��B---
[����]���i�̌��������B���Y�t�B�[���h�ɏ������Z�b�g���Ă���
�������w�肵�Ȃ��t�B�[���h�� null �܂��͋󕶎���܂��� 0�B

[�ߒl]�����𖞂��������i�� Item �C���X�^���X�̃��X�g�B
�E���i�R�[�h����я��i���ɂ�錟�����s���B
�E�����́A�ǂ̍��ڂ�������v�Œ��o����B
�E�����̍��ڂɌ��������������Ă���Ƃ��́AAND ����(�S�Ă̏����𖞂������i�����𒊏o)����B
*/
	public ArrayList<Item> select(Item key) throws CannotSearchItemsException{
		ArrayList<Item> item = new ArrayList<Item>();
		
	//�h���C�o���[�h
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}catch (ClassNotFoundException e){
			System.out.println("JDBC�h���C�o��������܂���B");
		}
			
	//�f�[�^�x�[�X�ւ̐ڑ�
		Connection conn = null;
		try{
			String url = "jdbc:mysql:///GoodsOrder?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String pass = "pass";
			conn = DriverManager.getConnection(url,user,pass);
		}catch (SQLException e){
			throw new CannotSearchItemsException("");
		}

		//SQL������
		PreparedStatement ps = null;
		try{
			String sql = "SELECT code, name, price FROM items";
			sql += " WHERE name LIKE ?";    //" WHERE name LIKE ?"
			ps = conn.prepareStatement(sql);
			ps.setString( 1,"%" + key.getName() + "%");
			
		}catch (SQLException e){
			try{
				conn.close();   //�f�[�^�x�[�X�ؒf
			}catch (SQLException e2){
				throw new CannotSearchItemsException("");
			}
			throw new CannotSearchItemsException("");
		}
		
		//SQL���s
		ResultSet rs = null;
		try{
		    conn.setAutoCommit(false);  //�g�����U�N�V�����J�n(BEGIN�Ɠ���)
            rs = ps.executeQuery();
            conn.commit();              //�R�~�b�g(COMMIT�Ɠ���)
		}catch (SQLException e){
			try{
				conn.close();
			}catch (SQLException e2){
				throw new CannotSearchItemsException("");
			}
			try{
                conn.close();
            }catch(SQLException e3){
                throw new CannotSearchItemsException("");
            }
            throw new CannotSearchItemsException("");
        }
		
		//���ʂ̎擾
		try{
			while(rs.next()){
				if(rs.getString("code") != null){
					String code = rs.getString("code");
					String name = rs.getString("name");
					int price = rs.getInt("price");
					Item search = new Item(code,name,price);
					item.add(search); //ArrayList��add
				}
			}
			return item;
		}catch (SQLException e){
			throw new CannotSearchItemsException("");
		}finally{
			try{
            	rs.close();
        	}catch(SQLException e){
            	throw new CannotSearchItemsException("");
       		}
        	try{
            	ps.close();
        	}catch(SQLException e){
            	throw new CannotSearchItemsException("");
        	}
        	try{
            	conn.close();
        	}catch(SQLException e){
            	throw new CannotSearchItemsException("");
            }
        }   
	}
	
/*----�e�X�g�p-----
	public static void main(String[] args)throws CannotSearchItemsException{
		String code = null;  //���i�R�[�h
	  	String name;  //���i��
	  	int price = 0;    //�P��

		name = "Java";
		
		ItemDataAccessor id = new ItemDataAccessor();
		Item item = new Item(code,name,price);
		id.select(item);
	}*/
}
