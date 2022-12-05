package makeQuestion.model;

import makeQuestion.bean.Record;

import java.util.ArrayList;

/*
  時間記録のランキング
*/
public class Ranking{
    private final int RANKNUM = 10;                              //ランキング記録数
    private ArrayList<Record> records = new ArrayList<Record>(); //ランキング記録用配列
    
    //--------------------------------------------------------------------------
    // ランキングリスト取得
    // 引数 : なし
    // 戻値 : ランキングリスト
    //--------------------------------------------------------------------------
    public ArrayList<Record> getRecords(){
        return records;
    }
    
    //--------------------------------------------------------------------------
    // ランク判定
    // 引数 : 回答時間(s) 誤答の場合は負
    // 戻値 : 順位 ランク外の場合は0
    //--------------------------------------------------------------------------
    public int judge(double time){
        if(time < 0.0){
            return 0;
        }
        
        int rank = 0;
        while(rank < records.size() && records.get(rank).getTime() < time){
            rank++;
        }
        if(rank < RANKNUM){
            return (rank + 1);
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------
    // ランキング登録
    // 引数 : 登録するRecord
    // 戻値 : 順位 ランク外の場合は0
    //--------------------------------------------------------------------------
    public int insert(Record newRecord){
        double newTime = newRecord.getTime();
        int newRank = judge(newTime);
        
        if(newRank < 1){
            return 0;
        }
        
        records.add(newRank - 1, newRecord);
        while(records.size() > RANKNUM){
            records.remove(RANKNUM);
        }
        return newRank;
    }
}
