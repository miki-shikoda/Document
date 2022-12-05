//佐藤聖子

package makeQuestion.controller;

import makeQuestion.model.Question; 
import makeQuestion.model.SubQuestion; 
import makeQuestion.model.MulQuestion; 
import makeQuestion.model.DivQuestion; 

import makeQuestion.model.Ranking;
import makeQuestion.view.QuesUI;
import makeQuestion.bean.Record;

//出題プログラムの全体の流れを制御するクラス
//extendsでaddControllerを継承
   
public class QuesController extends AddController{ 

    private Question question = new Question();  //問題オブジェクトへの参照
    private Ranking ranking = new Ranking();     //ランキングオブジェクトへの参照
    private QuesUI ui = new QuesUI();                //ユーザ I/F オブジェクトへの参照
    
    
    //出題プログラム全体の流れ
    @Override
    public void exec(){ 
        int num = ui.selectQuestion(); 
        
        if(num == 1){      
            question = new Question(); 
        }else if(num == 2){ 
            question = new SubQuestion();
            //String question = minus.next();
        }else if(num == 3){
            question = new MulQuestion(); 
            //String question = mul.next(); 
        }else{
            question = new DivQuestion(); 
            //String question = div.next();
        }

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
    
