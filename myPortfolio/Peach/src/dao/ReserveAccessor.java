package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.text.ParseException;//後で削除
import java.text.SimpleDateFormat;
//import beans.Address;//使うときになったらコメントアウト外す
//import beans.Customer; //テスト用のメインメソッドで使う
import beans.Reserve;
import beans.Flight;
import exceptions.*;
/**************   Reserve(予約情報)を管理するクラス   *******************/
    public class ReserveAccessor {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    final int ridelimit = 19;          //飛行機に搭乗可能な上限人数
    private ArrayList<Reserve> list = new ArrayList<Reserve>();

/*******************    データベースへの接続       **********************/
    private boolean connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//192.168.67.230:3306
            String url = "jdbc:mysql:///db_peach?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "root";
            String pass = "pass";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            //System.out.println("connectエラー1 =>");
            //e.printStackTrace();
            return false;
        } catch (SQLException e) {
            //System.out.println("connectエラー2 =>");
            //e.printStackTrace();
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
                //System.out.println("closeエラー1 =>");
                //e.printStackTrace();
            }
        }
        if(ps != null){
            try{
                ps.close();
            }catch(SQLException e){
                //System.out.println("closeエラー2 =>");
                result = false;
            }
        }
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                //System.out.println("closeエラー3 =>");
                result = false;
            }
        }
        return result;
    }
/***********   搭乗日で検索するメソッド   ***********/
    public ArrayList<Reserve> select1(Reserve key) throws CannotSearchException,NoResultException{ //
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }
        //検索するFlightIDが存在するIDか確認する
        String flightID=null;
        try{
            String sql = "SELECT flightID FROM flight WHERE flightID=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,key.getFlightID());
            rs = ps.executeQuery();
            while(rs.next()){
                flightID = rs.getString("flightID");
            }
            if(flightID==null){throw new NoResultException("指定の便名は存在しません");}
        }catch(SQLException e){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }
        Calendar cals = key.getFlightdate();
        Date d = new Date();
        d=cals.getTime();
        String date = "";
        if(cals != null){
            date=new SimpleDateFormat("yyyy-MM-dd").format(d);
        }
        try{
            String sql = "SELECT flightdate,flightID,COUNT(flightID) AS rideNum FROM reserve "; //rideNum:乗車人数
            sql += "WHERE flightID=? AND flightdate=? GROUP BY flightdate ORDER BY flightdate ASC";
            ps = conn.prepareStatement(sql);
            ps.setString(1,key.getFlightID());
            ps.setString(2,date);

            //検索実行
            int remainseats = 0;//残席数
            list = new ArrayList<Reserve>();
            rs = ps.executeQuery();
            while(rs.next()){
                Reserve reserve = new Reserve();
                int test = rs.getInt("rideNum");//
                //System.out.println(test);
                remainseats = ridelimit - (rs.getInt("rideNum"));  //席数上限：ridelimit(19)
                reserve.setRemainseats(remainseats);
            //--日付処理開始
                d = new Date();
                SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    if(test!=0){d=Format.parse(rs.getString("flightdate"));}
                }catch(ParseException e){
                }
                Calendar calSet = Calendar.getInstance();
                calSet.setTime(d); //---処理完了
                reserve.setFlightdate(calSet);
                if(test!=0){reserve.setFlightID(rs.getString("flightID"));}
                //System.out.println(reserve.getFlightID()+":"+reserve.getRemainseats()+":"+date);
                list.add(reserve);
            }
            if(list.size()==0){
                Reserve reserve = new Reserve();
                reserve.setFlightdate(key.getFlightdate());
                reserve.setFlightID(key.getFlightID());
                remainseats = ridelimit;  //席数上限：ridelimit(19)
                reserve.setRemainseats(remainseats);
                list.add(reserve);
            }    
            if(list.size()==0){throw new NoResultException("検索結果がありません");}
        }catch(SQLException e){
            System.out.println("select1-2");
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }finally{
            close();
        }
        return list;
    }
//*********    搭乗期間で検索するメソッド   ***********
    public ArrayList<Reserve> select2(Reserve key)throws CannotSearchException,NoResultException{ //
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }
    //---検索するFlightIDが存在するIDか確認する
        String flightID=null;
        try{
            String sql = "SELECT flightID FROM flight WHERE flightID=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,key.getFlightID());
            rs = ps.executeQuery();
            while(rs.next()){
                flightID = rs.getString("flightID");
            }
            if(flightID==null){throw new NoResultException("指定の便名は存在しません");}
        }catch(SQLException e){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }
    //---期間で空席検索           
        try{
            int prm = 1;  //parameterIndex用変数 ?がある(検索条件 便名1つは必須)→初期値1
            int setdateIndex = 0, enddateIndex = 0;
            String setDate = "", endDate = "";
            String sql = "SELECT flightdate,COUNT(flightID) AS rideNum FROM reserve "; //rideNum:乗車人数
            sql += "WHERE flightID=? AND";        
        //-----検索のための日付処理(Calendar→String)
        if(key.getFlightdate() != null){     //運航日 自 のString変換
            Calendar cals = key.getFlightdate();
            Date d = cals.getTime();
            setDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
            sql += " (flightdate >= ?)";
            setdateIndex = ++prm;
        }
        if(key.getFlightEnddate() != null){  //運航日 至 のString変換
            Calendar cals = key.getFlightEnddate();
            Date d = cals.getTime();
            endDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
            if(prm > 1) sql += " AND";
            sql += " (flightdate <= ?)";
            enddateIndex = ++prm;
        }
        sql += " GROUP BY flightdate ORDER BY flightdate ASC";
        ps = conn.prepareStatement(sql);
        ps.setString(1, key.getFlightID()); //便名 ?の1つ目に必ず入る
        if(!setDate.equals("")) ps.setString(setdateIndex,setDate); //集計期間自セット
        if(!endDate.equals("")) ps.setString(enddateIndex,endDate); //集計期間至セット

        //-----検索実行
            rs = ps.executeQuery();
            list = new ArrayList<>();
            int remainseats = 0;//残席数
            Date date = new Date();//
            while(rs.next()){
                Reserve reserve = new Reserve();
                remainseats = ridelimit - (rs.getInt("rideNum"));  //席数上限：ridelimit(19)
                reserve.setRemainseats(remainseats);
                SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    date=Format.parse(rs.getString("flightdate"));
                }catch(ParseException e){
                }
                Calendar calSet = Calendar.getInstance();
                calSet.setTime(date);
                reserve.setFlightdate(calSet);
                list.add(reserve);//ArrayListへAdd
            }
            //if(list.size()==0){throw new NoResultException("検索結果がありません");}
        }catch(SQLException e){
            System.out.println("select2-2");
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }finally{
            close();
        }
        return list;
    } 
//*********    空港名で便名を検索するメソッド    ***************
    public ArrayList<Flight> select3(Flight key)throws CannotSearchException ,NoResultException { //
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }
        ArrayList<Flight> flightlist = new ArrayList<Flight>();
        try{
            String sql = "SELECT flightID,base1.name AS departure,base2.name AS destination,base1.open AS openTime,base2.close AS closeTime FROM flight"; 
            sql += " INNER JOIN route ON flight.route = routeID INNER JOIN base AS base1 ON  base1.baseID =  frombase INNER JOIN base AS base2 ON  base2.baseID =  tobase";
            sql += " WHERE base1.name LIKE ? OR base1.nicknamed LIKE ?";
            ps = conn.prepareStatement(sql);
            ps.setString( 1,"%" + key.getFlightname() + "%");
            ps.setString( 2,"%" + key.getFlightname() + "%");
        //-----検索実行
            rs = ps.executeQuery();
            flightlist = new ArrayList<Flight>();
            while(rs.next()){
                //結果読み込み
                Flight flight = new Flight();
                flight.setFlightID(rs.getString("flightID"));
                flight.setDeparture(rs.getString("departure"));
                flight.setDestination(rs.getString("destination"));
                flight.setOpenTime(rs.getString("openTime"));
                flight.setCloseTime(rs.getString("closeTime"));
                flightlist.add(flight);
                //System.out.println(rs.getString("flightID"));
            }
            if(flightlist.size()==0){throw new NoResultException("検索結果がありません");}
        }catch(SQLException e){
            System.out.println("select2-2");
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }finally{
            close();
        }
        return flightlist;
    }
//*********    予約登録するメソッド    ***************
    public Reserve insert(Reserve newReserve) throws CannotRegistrationException{
        if(!connect()){
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }
    //----予約番号最大値の確認
        int reserve_Max=0;
        try{
            String sql = "SELECT MAX(reserveID) AS max_no FROM reserve";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                reserve_Max = rs.getInt("max_no");
            }
        }catch(SQLException e){
            close();
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }
    //----顧客番号の最大値
        int customer_Max=0;
        try{
            String sql = "SELECT MAX(guestID) AS max_no FROM customer";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                customer_Max = rs.getInt("max_no");
            }           
        }catch(SQLException e){
            close();
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }
    //-----登録のための日付処理(Calendar→String) ※※※配列に入れてます　0→appdate　1→flightdate
        Calendar[] cals = {newReserve.getAppdate(),newReserve.getFlightdate()};
        String[] date = {null,null};
        for(int i=0;i<cals.length;i++){
            Date d = new Date();
            d=cals[i].getTime();
            if(cals[i] != null){
                date[i] =new SimpleDateFormat("yyyy-MM-dd").format(d);
            }
        }
    //----customer仮登録
        try{
            String sql = "INSERT INTO customer(guestID,seikana,meikana)";
            sql += " VALUES(?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,++customer_Max);
            ps.setString(2,newReserve.getGuestID().getSeikana());
            ps.setString(3,newReserve.getGuestID().getMeikana());
            //System.out.println(customer_Max+"|"+newReserve.getGuestID().getSei()+ "|" +newReserve.getGuestID().getSeikana() +"|"+newReserve.getGuestID().getAddID()+"|"+newReserve.getGuestID().getTel());
    //---PreparedStatementオブジェクトのSQL文を実行
            int num = ps.executeUpdate();
            if(num==0){throw new CannotRegistrationException("顧客登録に失敗しました。");}
        }catch(SQLException e){
            e.printStackTrace();
            throw new CannotRegistrationException("顧客仮登録に失敗しました。やり直してください。");
        }
    //-----顧客IDセット
        newReserve.getGuestID().setGuestID(customer_Max);
    //----reserve本登録
        try{
            String sql = "INSERT INTO reserve(reserveID,appdate,guestID,flightdate,flightID)";
            sql += " VALUES(?, ?, ?, ? , ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,++reserve_Max);
            ps.setString(2,date[0]);
            ps.setInt(3,customer_Max);//customer登録してからかも
            ps.setString(4,date[1]);
            ps.setString(5,newReserve.getFlightID()); //System.out.println(reserve_Max+"|"+date[0]+"|"+customer_Max+"|"+date[1]);
    //---PreparedStatementオブジェクトのSQL文を実行
            ps.executeUpdate();
        }catch(SQLException e){
            throw new CannotRegistrationException("登録失敗しました。やり直してください。");
        }finally{
            close();//データベース切断           
        }
    //---予約IDセット
        newReserve.setReserveID(reserve_Max);
        return newReserve;
    }
}
