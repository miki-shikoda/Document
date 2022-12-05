package makeQuestion.controller;

import makeQuestion.model.Question;
import makeQuestion.model.Ranking;
import makeQuestion.view.AddUI;
import makeQuestion.bean.Record;

/*
  出題プログラムのコントローラ
*/
public class AddController{
    private Question question = new Question();
    private Ranking ranking = new Ranking();
    private AddUI ui = new AddUI();
    
    //--------------------------------------------------------------------------
    // 出題プログラム全体の動作
    // 引数 : なし
    // 戻値 : なし
    //--------------------------------------------------------------------------
    public void exec(){
        String cmd = ui.inputCmd(); //コマンド入力
        
        while(!cmd.equals("e")){
            if(cmd.equals("r")){
                //ランキング表示
                ui.showRanking(ranking);
            }else{
                //問題表示と回答入力
                String ques = question.next();
                ui.showQuestion(ques);
                long st = System.currentTimeMillis();
                int ans = ui.inputAns();
                long et = System.currentTimeMillis();
                
                //正誤判定
                double time = (et - st) / 1000.0;
                boolean result = question.judge(ans);
                if(!result){
                    time = -1.0;
                }
                
                //ランクイン判定
                int rank = ranking.judge(time);
                
                //結果表示
                ui.showResult(result, rank, time);
                
                //ランクイン処理
                if(rank > 0){
                    String name = ui.inputName();
                    Record rec = new Record(time, name);
                    ranking.insert(rec);
                }
            }
            
            cmd = ui.inputCmd(); //次のコマンド入力
        }
    }
}
