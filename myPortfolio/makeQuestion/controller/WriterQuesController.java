//【作成者：チームIQ102】 志子田

package makeQuestion.controller;

import makeQuestion.model.Question; 
import makeQuestion.model.SubQuestion; 
import makeQuestion.model.MulQuestion; 
import makeQuestion.model.DivQuestion; 

import makeQuestion.model.Ranking;
import makeQuestion.model.RankingRead;
import makeQuestion.view.QuesUI;
import makeQuestion.view.ErrorUI;
import makeQuestion.bean.Record;
import makeQuestion.bean.QuesWriter;
import makeQuestion.bean.SubWriter;
import makeQuestion.bean.MulWriter;
import makeQuestion.bean.DivWriter;

//出題プログラムの全体の流れを制御するクラス
//extendsでaddControllerを継承
   
public class WriterQuesController extends QuesController{ 

    private Question question = new Question();  //問題オブジェクトへの参照
    //private Ranking ranking = new Ranking();     //ランキングオブジェクトへの参照
    private QuesUI ui = new QuesUI();                //ユーザ I/F オブジェクトへの参照
    private QuesWriter qw = new QuesWriter();
    private RankingRead ranking = new RankingRead();
    private ErrorUI errui = new ErrorUI();                //ユーザ I/F オブジェクトへの参照
    
    //出題プログラム全体の流れ
    @Override
    public void exec(){ 
        int num = ui.selectQuestion(); 
        
        if(num == 1){      
            question = new Question();
            
        }else if(num == 2){ 
            question = new SubQuestion();
        }else if(num == 3){
            question = new MulQuestion(); 
        }else{
            question = new DivQuestion(); 
        }
        
        ranking.Read(num);

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
                    String name = errui.inputName();
                    Record rec = new Record(time, name);
                    ranking.insert(rec);

//-------CSV処理
                    if(num == 1){           //足し算
                    	qw.Write(ranking);
                    	
			        }else if(num == 2){     //引き算ver
			            qw = new SubWriter();
                    	qw.Write(ranking);

			        }else if(num == 3){     //かけ算ver
                    	qw = new MulWriter();
                    	qw.Write(ranking);
			        }else{                   //わり算ver
                    	qw = new DivWriter();
                    	qw.Write(ranking);
                    }
                }
            }
            cmd = ui.inputCmd(); //次のコマンド入力
        }

        }
    }
    
