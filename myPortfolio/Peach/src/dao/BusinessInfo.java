//米澤
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import exceptions.CannotSearchException;
import exceptions.NoResultException;
import beans.ReserveSale;

//営業情報検索クラス
public class BusinessInfo {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    //-------------------------------------------------------------------------------------
    //データベースへの接続
    private boolean connect() throws CannotSearchException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://192.168.67.230:3306/db_peach?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "root";
            String pass = "pass";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            throw new CannotSearchException("正常に検索できませんでした");
        } catch (SQLException e) {
            throw new CannotSearchException("正常に検索できませんでした");
        }
        return true;
    }
    //-------------------------------------------------------------------------------------
    //データベース切断
    private boolean close() throws CannotSearchException{
        boolean result = true;
        if(rs != null ){
            try{
                rs.close();
            }catch(SQLException e){
                throw new CannotSearchException("正常に検索できませんでした");
            }
        }
        if(ps != null){
            try{
                ps.close();
            }catch(SQLException e){
                throw new CannotSearchException("正常に検索できませんでした");
            }
        }
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                throw new CannotSearchException("正常に検索できませんでした");
            }
        }
        return result;
    }
    //-------------------------------------------------------------------------------------
    //<<日別便別>> 売上集計
    public ArrayList<ReserveSale> dailyFlightSale(ReserveSale key)
     throws CannotSearchException, NoResultException{
        //DB接続
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }
        //SQL文の準備
        ArrayList<ReserveSale> list = new ArrayList<>();
        try{
            int prm = 0;  //parameterIndex用変数
            int setdateIndex = 0, enddateIndex = 0;
            String setDate = "", endDate = "";
            String sql = "SELECT flightdate, reserve.flightID, base1.name as frombase, base2.name as tobase,";
            sql += " SUM(price) as sale FROM reserve";
            sql += " INNER JOIN flight ON reserve.flightID = flight.flightID";
            sql += " INNER JOIN route ON flight.route = route.routeID";
            sql += " INNER JOIN base as base1 ON frombase = base1.baseID";
            sql += " INNER JOIN base as base2 ON tobase = base2.baseID WHERE";
            if(key.getFlightdate() != null){     //集計期間 自 のString変換
                Calendar cals = key.getFlightdate();
                Date d = cals.getTime();
                setDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                sql += " (flightdate >= ?)";
                setdateIndex = ++prm;
            }
            if(key.getFlightEnddate() != null){  //集計期間 至 のString変換
                Calendar cals = key.getFlightEnddate();
                Date d = cals.getTime();
                endDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                if(prm > 0) sql += " AND";
                sql += " (flightdate <= ?)";
                enddateIndex = ++prm;
            }
            sql += " GROUP BY reserve.flightID, flightdate ORDER BY flightdate, reserve.flightID ASC";
            ps = conn.prepareStatement(sql);
            if(!setDate.equals("")) ps.setString(setdateIndex,setDate); //集計期間自セット
            if(!endDate.equals("")) ps.setString(enddateIndex,endDate); //集計期間至セット
            //検索実行
            list = new ArrayList<>();
            rs = ps.executeQuery();
            while(rs.next()){
                ReserveSale reserve = new ReserveSale();
                Date d = (rs.getDate("flightdate"));
                Calendar cals = Calendar.getInstance();
                cals.setTime(d);
                reserve.setFlightdate(cals);
                reserve.setFlightID(rs.getString("flightID"));
                reserve.setFrombase(rs.getString("frombase"));
                reserve.setTobase(rs.getString("tobase"));
                reserve.setSale(rs.getInt("sale"));
                list.add(reserve);
            }
            //検索してヒットしなかった場合
            if(list.size() == 0) throw new NoResultException("検索結果なし");
        }catch(SQLException e){
            throw new CannotSearchException("正常に検索できませんでした");
        }finally{
            close();
        }
        return list;
    }
    //-------------------------------------------------------------------------------------
    //<<年月別>> 売上集計
    public ArrayList<ReserveSale> monthSale(ReserveSale key)
     throws CannotSearchException, NoResultException{
        //DB接続
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }        
        //SQL文の準備
        ArrayList<ReserveSale> list = new ArrayList<>();
        try{
            int prm = 0;  //parameterIndex用変数
            int setdateIndex = 0, enddateIndex = 0;
            String setDate = "", endDate = "";
            String sql = "SELECT DATE_FORMAT(flightdate,'%Y%m') as 'year-month', SUM(price) as sale FROM reserve";
            sql += " INNER JOIN flight ON reserve.flightID = flight.flightID";
            sql += " INNER JOIN route ON flight.route = route.routeID WHERE";
            if(key.getFlightdate() != null){     //集計期間 自 のString変換
                Calendar cals = key.getFlightdate();
                Date d = cals.getTime();
                setDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                sql += " (flightdate >= ?)";
                setdateIndex = ++prm;
            }
            if(key.getFlightEnddate() != null){  //集計期間 至 のString変換
                Calendar cals = key.getFlightEnddate();
                Date d = cals.getTime();
                endDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                if(prm > 0) sql += " AND";
                sql += " (flightdate < ?)";
                enddateIndex = ++prm;
            }
            sql += " GROUP BY DATE_FORMAT(flightdate,'%Y%m')";
            sql += " ORDER BY DATE_FORMAT(flightdate,'%Y%m')";
            ps = conn.prepareStatement(sql);
            if(!setDate.equals("")) ps.setString(setdateIndex,setDate); //集計期間自セット
            if(!endDate.equals("")) ps.setString(enddateIndex,endDate); //集計期間至セット
            //検索実行
            list = new ArrayList<>();
            rs = ps.executeQuery();
            while(rs.next()){
                ReserveSale reserve = new ReserveSale();
                String date = (rs.getString("year-month"));
                int year = Integer.parseInt(date.substring(0,4));
                int month = Integer.parseInt(date.substring(4,6));
                Calendar cals = Calendar.getInstance();
                cals.set(year,month - 1,1);
                reserve.setFlightdate(cals);
                reserve.setSale(rs.getInt("sale"));
                list.add(reserve);
            }
            //検索してヒットしなかった場合
            if(list.size() == 0) throw new NoResultException("検索結果なし");
        }catch(SQLException e){
            throw new CannotSearchException("正常に検索できませんでした");
        }finally{
            close();
        }
        return list;
    }
    //-------------------------------------------------------------------------------------
    //<<年月別便別>> 売上集計
    public ArrayList<ReserveSale> monthFlightSale(ReserveSale key)
     throws CannotSearchException, NoResultException{
        //DB接続
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }        
        //SQL文の準備
        ArrayList<ReserveSale> list = new ArrayList<>();
        try{
            int prm = 0;  //parameterIndex用変数
            int setdateIndex = 0, enddateIndex = 0;
            String setDate = "", endDate = "";
            String sql = "SELECT DATE_FORMAT(flightdate,'%Y%m') as 'year-month', reserve.flightID,";
            sql += " base1.name as frombase, base2.name as tobase, SUM(price) as sale FROM reserve";
            sql += " INNER JOIN flight ON reserve.flightID = flight.flightID";
            sql += " INNER JOIN route ON flight.route = route.routeID";
            sql += " INNER JOIN base as base1 ON frombase = base1.baseID";
            sql += " INNER JOIN base as base2 ON tobase = base2.baseID WHERE";
            if(key.getFlightdate() != null){     //集計期間 自 のString変換
                Calendar cals = key.getFlightdate();
                Date d = cals.getTime();
                setDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                sql += " (flightdate >= ?)";
                setdateIndex = ++prm;
            }
            if(key.getFlightEnddate() != null){  //集計期間 至 のString変換
                Calendar cals = key.getFlightEnddate();
                Date d = cals.getTime();
                endDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                if(prm > 0) sql += " AND";
                sql += " (flightdate < ?)";
                enddateIndex = ++prm;
            }
            sql += " GROUP BY DATE_FORMAT(flightdate,'%Y%m'), reserve.flightID";
            sql += " ORDER BY DATE_FORMAT(flightdate,'%Y%m'), reserve.flightID";
            ps = conn.prepareStatement(sql);
            if(!setDate.equals("")) ps.setString(setdateIndex,setDate); //集計期間自セット
            if(!endDate.equals("")) ps.setString(enddateIndex,endDate); //集計期間至セット
            //検索実行
            list = new ArrayList<>();
            rs = ps.executeQuery();
            while(rs.next()){
                ReserveSale reserve = new ReserveSale();
                String date = (rs.getString("year-month"));
                int year = Integer.parseInt(date.substring(0,4));
                int month = Integer.parseInt(date.substring(4,6));
                Calendar cals = Calendar.getInstance();
                cals.set(year,month - 1,1);
                reserve.setFlightdate(cals);
                reserve.setFlightID(rs.getString("flightID"));
                reserve.setFrombase(rs.getString("frombase"));
                reserve.setTobase(rs.getString("tobase"));
                reserve.setSale(rs.getInt("sale"));
                list.add(reserve);
            }
            //検索してヒットしなかった場合
            if(list.size() == 0) throw new NoResultException("検索結果なし");
        }catch(SQLException e){
            throw new CannotSearchException("正常に検索できませんでした");
        }finally{
            close();
        }
        return list;
    }
    //-------------------------------------------------------------------------------------
    //<<便別>> 売上集計
    public ArrayList<ReserveSale> flightSale() throws CannotSearchException{
        //DB接続
        if(!connect()){
            throw new CannotSearchException("検索できませんでした。やり直してください。");
        }        
        //SQL文の準備
        ArrayList<ReserveSale> list = new ArrayList<>();
        try{
            String sql = "SELECT reserve.flightID, SUM(price) as sale,";
            sql += " base1.name as frombase, base2.name as tobase FROM reserve";
            sql += " INNER JOIN flight ON reserve.flightID = flight.flightID";
            sql += " INNER JOIN route ON flight.route = route.routeID";
            sql += " INNER JOIN base as base1 ON frombase = base1.baseID";
            sql += " INNER JOIN base as base2 ON tobase = base2.baseID";
            sql += " GROUP BY reserve.flightID ORDER BY reserve.flightID;";
            ps = conn.prepareStatement(sql);
            //検索実行
            list = new ArrayList<>();
            rs = ps.executeQuery();
            while(rs.next()){
                ReserveSale reserve = new ReserveSale();
                reserve.setFlightID(rs.getString("flightID"));
                reserve.setFrombase(rs.getString("frombase"));
                reserve.setTobase(rs.getString("tobase"));
                reserve.setSale(rs.getInt("sale"));
                list.add(reserve);
            }
        }catch(SQLException e){
            throw new CannotSearchException("正常に検索できませんでした");
        }finally{
            close();
        }
        return list;
    }
}
