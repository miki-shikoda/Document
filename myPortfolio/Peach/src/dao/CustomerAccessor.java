package dao; //船木

import java.util.Calendar;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import beans.Reserve;
import beans.Address;
import beans.Customer;
import exceptions.*;

 /*******************    搭乗者情報を担うクラス       **********************/
public class CustomerAccessor{
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

 /*******************    データベースへの接続       **********************/
    private boolean connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://192.168.67.230:3306/db_peach?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "root";
            String pass = "pass";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            System.out.println("connectエラー1 =>接続できませんでした。やり直してください");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.out.println("connectエラー2 =>接続できませんでした。やり直してください");
            e.printStackTrace();
            return false;
        }
        return true;        
    }
/*******************   データベースからの切断   **********************/
    private boolean close(){
        boolean result = true;
        if(rs != null ){
            try{
                rs.close();
            }catch(SQLException e){
                System.out.println("closeエラー1 =>接続切断に失敗しました。");
                e.printStackTrace();
            }
        }
        if(ps != null){
            try{
                ps.close();
            }catch(SQLException e){
                System.out.println("closeエラー2 =>接続切断に失敗しました。");
                result = false;
            }
        }
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                System.out.println("closeエラー3 =>接続切断に失敗しました。");
                result = false;
            }
        }
        return result;
    }   
/*******************   条件を満たした搭乗者名、便名等の情報を検索   **********************/
    public ArrayList<Customer> select(Reserve key)  throws CannotSearchException,NoResultException {
        ArrayList<Customer> list = new ArrayList<Customer>();
            //データベース接続
        if(!connect()){
            throw new CannotSearchException("搭乗者名簿検索に失敗しました");
        }
        Calendar flightdate = null;
        Date d = new Date();
        flightdate = key.getFlightdate();
        d = flightdate.getTime();
        String date = "";
        if(flightdate != null){
            date =new SimpleDateFormat("yyyy-MM-dd").format(d);
        }

        String sei = null; 
        String mei = null;         
        String seikana = null; 
        String meikana = null;         
        String tel = null;          
        String mobile = null;          
        int guestID = 0;
        String zipcode = null;
        String address = null;
        int addID = 0;

        //検索キーワード(=データベースの運航日と便名が一致するもの)
        //SQL(SELECT文)を準備
        try{//guestID, concat(sei, mei)as seimei, concat(seikana, meikana)as seimeikana, tel ,mobile
            String sql = "SELECT *";
            sql += " FROM customer INNER JOIN reserve ON reserve.guestID = customer.guestID";
            sql += " INNER JOIN address ON address.addID = customer.addID WHERE flightdate = ? AND flightID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setString(2, key.getFlightID());
        }catch (SQLException e){
            throw new CannotSearchException("搭乗者名簿検索に失敗しました");
        }                             
        //SQLの発行(答えがrsに入る)
        try{
            try{
                conn.setAutoCommit(false);
                rs = ps.executeQuery();
                conn.commit();
            }catch(SQLException e){
            throw new CannotSearchException("搭乗者名簿検索に失敗しました");
            }
            
            //答えを一行ずつArrayListにいれる
            while(rs.next()){
                sei = rs.getString("sei");                //名前 (姓)
                mei = rs.getString("mei");                //名前 (名)
                seikana = rs.getString("seikana");        // ふりがな(姓)
                meikana = rs.getString("meikana");        // ふりがな（名）
                tel = rs.getString("tel");                // 電話番号
                mobile = rs.getString("mobile");          // 携帯電話番号
                guestID = rs.getInt("guestID");           //顧客ID
                address = rs.getString("address");
                zipcode = rs.getString("zipcode");
                addID = rs.getInt("addID");
                
                //ArrayList生成    
                Customer addCustomer = new Customer();
                Address add= new Address();
                addCustomer.setSei(sei);
                addCustomer.setMei(mei);
                addCustomer.setSeikana(seikana);
                addCustomer.setMeikana(meikana);
                addCustomer.setTel(tel);
                addCustomer.setMobile(mobile);
                addCustomer.setGuestID(guestID);
                add.setAddress(address);
                add.setZipcode(zipcode);
                add.setAddID(addID);
                addCustomer.setAddID(add);         

                list.add(addCustomer); //ArrayListへadd
            }
            if(list.size() == 0){
                throw new NoResultException("");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   //cvs名を設定
            String cs = sdf.format(flightdate.getTime());
            String csv = cs  + key.getFlightID();

        if(list.size() != 0){        
            try(    
                FileWriter fileWriter = new FileWriter(csv + ".csv");  //後で
                BufferedWriter bw = new BufferedWriter(fileWriter);
                PrintWriter pw = new PrintWriter(bw);
            ){    
                try{
                    //リストの内容を順にCSVへ入れる
                    for (Customer p : list) {
                        pw.println(p.getGuestID() + "," + p.getSei() + "," + p.getMei() +  "," + p.getSeikana()
                         + "," + p.getMeikana() + "," + p.getTel() + "," + p.getMobile() + "," + p.getAddID().getAddID()
                         + "," + p.getAddID().getZipcode() + "," + p.getAddID().getAddress());
                    }    
                }catch(NoSuchElementException e){
                    //入力終了。特に何もしない
                }catch(IndexOutOfBoundsException e){
                }
                pw.flush();
            }catch(IOException e){
                System.out.println("CSVファイルを開けませんでした。");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }

        }catch(SQLException e2){
            throw new CannotSearchException("搭乗者名簿検索に失敗しました");
        }finally{
            close(); 
        }
        //搭乗者の情報のArrayListをリターンする 
        //搭乗者名、ふりがな、電話番号、携帯電話番号、顧客IDの情報の一覧(ArrayList)  
        return list;
    }
/*******************   フリガナ顧客検索だけするメソッド   **********************/
    public ArrayList<Customer> select2(Customer key) throws CannotSearchException ,NoResultException{
        if(!connect()){
            throw new CannotSearchException("登録失敗しました。やり直してください。");
        }
        ArrayList<Customer> list = new ArrayList<Customer>();
        try{
            String sql = "SELECT guestID,sei,mei,seikana,meikana,tel,mobile,addID FROM customer"; 
            sql += " WHERE seikana=? AND meikana=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,key.getSeikana());
            ps.setString(2,key.getMeikana());

            //検索実行
            rs = ps.executeQuery();
            while(rs.next()){
                Customer cs = new Customer();
                Address add= new Address();
                cs.setGuestID(rs.getInt("guestID"));
                cs.setSei(rs.getString("sei"));
                cs.setMei(rs.getString("mei"));
                cs.setSeikana(rs.getString("seikana"));
                cs.setMeikana(rs.getString("meikana"));
                add.setAddID(rs.getInt("addID"));
                cs.setAddID(add);
                cs.setTel(rs.getString("tel"));
                cs.setMobile(rs.getString("mobile"));
                list.add(cs);
            }
            if(list.size() == 0){throw new NoResultException("検索結果がありません");}
        }catch(SQLException e){
            System.out.println("select2");
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }finally{
            close();
        }
        return list;
    }
/*******************   顧客情報を登録するメソッド   **********************/
    public Customer insert(Customer newCustomer) throws CannotRegistrationException{
        //※ここだけ志子田作成
        //----データベース接続
        if(!connect()){
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }
        //----住所録検索・登録
        int addid = 0;
        String address = newCustomer.getAddID().getZipcode();
        address += newCustomer.getAddID().getAddress();
        try{
            String sql = "SELECT addID, CONCAT(zipcode, address) AS address FROM address";
            sql += " WHERE CONCAT(zipcode, address) = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, address);
            rs = ps.executeQuery();
            while(rs.next()){
                addid = rs.getInt("addID");
            }
            //住所が住所録に無い場合に新規登録する
            if(addid == 0){
                sql = "SELECT MAX(addID) AS max_no FROM address";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    addid = (rs.getInt("max_no")) + 1;
                }
                sql = "INSERT INTO address(addID, zipcode, address)";
                sql += " VALUES(?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, addid);
                ps.setString(2,newCustomer.getAddID().getZipcode());
                ps.setString(3,newCustomer.getAddID().getAddress());
                int num = ps.executeUpdate();//SQL実行
                if(num == 0) throw new CannotRegistrationException("住所録の更新失敗");            
            }
        }catch(SQLException e){
            close();
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }
        /*//----住所録最大値確認
        int address_Max=0;
        try{
            String sql = "SELECT MAX(addID) AS max_no FROM address";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                address_Max = rs.getInt("max_no");
            }
        }catch(SQLException e){
            close();
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }
        //----住所登録
        try{
            String sql = "INSERT INTO address(addID,zipcode,address)";
            sql += " VALUES(?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, addid);
            ps.setString(2,newCustomer.getAddID().getZipcode());
            ps.setString(3,newCustomer.getAddID().getAddress());
            int num = ps.executeUpdate();//SQL実行
            if(num==0){throw new CannotRegistrationException("更新失敗しました。やり直してください。");}            
        }catch(SQLException e){
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }*/
        //顧客情報update
        try{
            String sql = "UPDATE customer SET sei=?, mei=?, addID=?, tel=?, mobile=? ";
            sql += "WHERE guestID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,newCustomer.getSei());
            ps.setString(2,newCustomer.getMei());
            ps.setInt(3,addid);
            ps.setString(4,newCustomer.getTel());
            ps.setString(5,newCustomer.getMobile());
            ps.setInt(6,newCustomer.getGuestID());
            newCustomer.getAddID().setAddID(addid);
            int num =ps.executeUpdate(); //PreparedStatementオブジェクトのSQL文を実行
            if(num==0){throw new CannotRegistrationException("登録情報変更失敗しました。やり直してください。");}
        }catch(SQLException e){
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }finally{
            close();//データベース切断
        }
        return newCustomer;
    }
}