package dao;

import java.util.Date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//https://www.sejuku.net/blog/20714
import beans.Reserve;

public class test {
    public static void main(String[]args){
        
        Date startdate = new Date();
        Date finishdate = new Date();
        String start = "2022-10-15" ;
        String finish = "2022-10-20" ;
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            startdate = sdFormat.parse(start);
            finishdate = sdFormat.parse(finish);
        }catch (ParseException e) {
        }   
        Calendar calstart = Calendar.getInstance();
        Calendar calfinish = Calendar.getInstance();
        calstart.setTime(startdate);
        calfinish.setTime(finishdate);
        Reserve rs = new Reserve();
        rs.setFlightdate(calstart);
        rs.setFlightEnddate(calfinish);
        // calstart.compareTo(calfinish) <= 0 解説↓↓
        //・比較メソッドの呼び出し元の値が、引数と等しい場合は0
        //・メソッドの呼び出し元の値が、引数より前の場合は-1
        //・メソッドの呼び出し元の値が、引数より後の場合は1
        calstart = rs.getFlightdate();//自
        calfinish = rs.getFlightEnddate();//至
        Date day1 = calstart.getTime(), day2 = calfinish.getTime(); 
        String date_1 = sdFormat.format(day1), date_2 = sdFormat.format(day2);
        System.out.println("[検索結果] 検索された空席は以下の通りです\n");
        System.out.printf("便名:%s\n", rs.getFlightID());
        System.out.printf("運航日:%s ~ %s\n\n", date_1, date_2);
        System.out.println("----------------------");
        System.out.println("   運航日   | 残席数 ");
        System.out.println("----------------------");

        ArrayList<Reserve> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        while(calstart.compareTo(calfinish) <= 0){
            calstart.add(Calendar.DATE,1);
            Date day = calstart.getTime();
            String trans = sdFormat.format(day);
            System.out.println("変換前："+trans);

            day=new Date();
            try{day = sdFormat.parse(trans);}catch (ParseException e) {}   
            Calendar caltrans = Calendar.getInstance();
            caltrans.setTime(day);
            rs = new Reserve();
            rs.setFlightdate(caltrans);
            list.add(rs);
        }
        int i =0;
        while(i<list.size()){
            if(list.size() > 0) calendar = list.get(i).getFlightdate();
            //検索したい日とリストに入っている運航日が一致するか判定
            if(calstart.compareTo(calendar) != 0){

            }else{
                Date dt2 = new Date();
                Calendar cl = list.get(i).getFlightdate();
                dt2=cl.getTime();
                String trans = sdFormat.format(dt2);
                System.out.println(trans);i++;
            }
            calstart.add(Calendar.DATE,1);
        }    
    }
}

//　*****************　　テスト用(削除予定)　　*****************
//検索操作の検証　------
/*
public static void main(String[] args) {
    Reserve res = new Reserve();
    Date date = null;
    String strDate = "2022-10-01" ;
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
    try{
        date = sdFormat.parse(strDate);
    }catch (ParseException e) {
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    res.setFlightdate(cal);
     
    res.setFlightID("EJ0201");
    ReserveAccessor ra = new ReserveAccessor();
    ArrayList<Reserve> list =null;
    try {
        list = ra.select1(res);
    } catch (CannotSearchException e) {
        e.printStackTrace();
    } catch (NoResultException e) {
        e.printStackTrace();
        //
    }
    //表示
    System.out.println(res.getFlightID() + "の残席数参照");    
    for(int i = 0; i < list.size(); i++){
        cal = list.get(i).getFlightdate();
        Date d = new Date();
        d=cal.getTime();
        strDate = "";
        if(cal != null){
            strDate=new SimpleDateFormat("yyyy-MM-dd").format(d);
            System.out.println(strDate+":"+list.get(i).getFlightID());
        }
        System.out.println( strDate + " : " + list.get(i).getRemainseats()+ " 席 ");
    }
}   
//期間検索用
Reserve res = new Reserve();
Date date = null;
Date date2 = null;
String strDate = "2022-09-01" ;
String strDateE = "2022-09-30" ;
SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
try{
    date = sdFormat.parse(strDate);
    date2 = sdFormat.parse(strDateE);
}catch (ParseException e) {
}       
Calendar cal = Calendar.getInstance();
Calendar calE = Calendar.getInstance();
cal.setTime(date);
calE.setTime(date2);

res.setFlightdate(cal);
res.setFlightEnddate(calE);
*/

//----仮登録の検証 
 /*  public static void main(String[] args) {
        Reserve res = new Reserve();
        Date appdate = null;
        Date flightdate = null;
        String strApp = "2022-10-01" ;
        String strFlight = "2022-10-2" ;
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            appdate = sdFormat.parse(strApp);
            flightdate = sdFormat.parse(strFlight);
        }catch (ParseException e) {
        }   
        Calendar calApp = Calendar.getInstance();
        Calendar calFlight = Calendar.getInstance();
        calApp.setTime(appdate);
        calFlight.setTime(flightdate);
        Customer cos = new Customer();
        String seikana ="しこだ";
        String meikana ="みき";
        cos.setSeikana(seikana);
        cos.setMeikana(meikana);

        res.setAppdate(calApp);
        res.setFlightdate(calFlight);
        res.setFlightID("EJ0201");
        res.setGuestID(cos);
        ReserveAccessor ra = new ReserveAccessor();
        
        try {
            ra.insert(res);
        } catch (CannotRegistrationException e) {
            System.out.println("予約登録に失敗");
            e.printStackTrace();
        }

    //結果表示
        System.out.println("以下IDにて、登録完了いたしました。");
        System.out.println("予約ID =>" + res.getReserveID());
        System.out.println("顧客ID =>" + res.getGuestID().getGuestID());
    }

    //期間検索の検証(失敗作)
                if(list.size()==0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal =  key.getFlightdate();
                date=cal.getTime();
                //①
                Reserve reserve = new Reserve();
                reserve.setFlightdate(cal);
                reserve.setFlightID(key.getFlightID());
                remainseats = ridelimit;  //席数上限：ridelimit(19)
                reserve.setRemainseats(remainseats);
                list.add(reserve);

                //②
                cal.add(Calendar.DATE,1);             
                sdf.format(cal.getTime());
                reserve = new Reserve();
                reserve.setFlightdate(cal);
                reserve.setFlightID(key.getFlightID());
                remainseats = ridelimit;  //席数上限：ridelimit(19)
                reserve.setRemainseats(remainseats);
                list.add(reserve);
            }

//空港検索用 *****************
/*  public static void main(String[] args) {
        Flight fli = new Flight();
        fli.setFlightname("仙台");
        ArrayList<Flight> fliList = new ArrayList<Flight>();
        ReserveAccessor ra = new ReserveAccessor();
        try {
            fliList = ra.select3(fli);
        }catch(CannotSearchException e){
        }
            
        //結果表示
        System.out.println("  便名"+" | "+"出発拠点"+" | "+"到着拠点"+" | "+"出発"+" | "+"到着");
        System.out.println("---------------------------------------------");
        for(int i = 0;i<fliList.size();i++){
            System.out.println(fliList.get(i).getFlightID()+" | "+fliList.get(i).getDeparture()+" | "+fliList.get(i).getDestination()+" | "+fliList.get(i).getOpenTime()+" | "+fliList.get(i).getCloseTime());
        }
    } 

//----本登録の検証 
/*     
    public static void main(String[] args) {
        Customer newcus= new Customer();
        Address add = new Address();        

        //Customer セット
        newcus.setGuestID(2053);
        newcus.setSei("志子田");
        newcus.setMei("美希");
        newcus.setSeikana("しこだ");
        newcus.setMeikana("みき");
        //Addressセット
        add.setZipcode("000-0000");
        add.setAddress("宮城県仙台市一番町１丁目");
        newcus.setAddID(add);
        CustomerAccessor cs = new CustomerAccessor();
        try {
            cs.insert(newcus);
        } catch (CannotRegistrationException e) {
            System.out.println("");
            e.printStackTrace();
        }
        //Reseveセット準備
        Reserve res = new Reserve();
        Date appdate = null;
        Date flightdate = null;
        String strApp = "2022-10-01" ;
        String strFlight = "2022-10-2" ;
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            appdate = sdFormat.parse(strApp);
            flightdate = sdFormat.parse(strFlight);
        }catch (ParseException e) {
        }   
        Calendar calApp = Calendar.getInstance();
        Calendar calFlight = Calendar.getInstance();
        calApp.setTime(appdate);
        calFlight.setTime(flightdate);
        //Reseveセット
        res.setReserveID(57490);
        res.setAppdate(calApp);
        res.setFlightdate(calFlight);
        res.setFlightID("EJ0201");
        res.setGuestID(newcus);
    } 
*/
