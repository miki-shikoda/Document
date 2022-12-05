//�y�쐬�ҁF�`�[��IQ102�z �u�q�c

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

//�o��v���O�����̑S�̗̂���𐧌䂷��N���X
//extends��addController���p��
   
public class WriterQuesController extends QuesController{ 

    private Question question = new Question();  //���I�u�W�F�N�g�ւ̎Q��
    //private Ranking ranking = new Ranking();     //�����L���O�I�u�W�F�N�g�ւ̎Q��
    private QuesUI ui = new QuesUI();                //���[�U I/F �I�u�W�F�N�g�ւ̎Q��
    private QuesWriter qw = new QuesWriter();
    private RankingRead ranking = new RankingRead();
    private ErrorUI errui = new ErrorUI();                //���[�U I/F �I�u�W�F�N�g�ւ̎Q��
    
    //�o��v���O�����S�̗̂���
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

        String cmd = ui.inputCmd(); //�R�}���h����
        while(!cmd.equals("e")){
            if(cmd.equals("r")){
                //�����L���O�\��
                ui.showRanking(ranking);
            	
            }else{
                //���\���Ɖ񓚓���
                String ques = question.next();
                ui.showQuestion(ques);
                long st = System.currentTimeMillis();
                int ans = ui.inputAns();
                long et = System.currentTimeMillis();
                
                //���딻��
                double time = (et - st) / 1000.0;
                boolean result = question.judge(ans);
                if(!result){
                    time = -1.0;
                }
                
                //�����N�C������
                int rank = ranking.judge(time);
                
                //���ʕ\��
                ui.showResult(result, rank, time);
                
                //�����N�C������
                if(rank > 0){
                    String name = errui.inputName();
                    Record rec = new Record(time, name);
                    ranking.insert(rec);

//-------CSV����
                    if(num == 1){           //�����Z
                    	qw.Write(ranking);
                    	
			        }else if(num == 2){     //�����Zver
			            qw = new SubWriter();
                    	qw.Write(ranking);

			        }else if(num == 3){     //�����Zver
                    	qw = new MulWriter();
                    	qw.Write(ranking);
			        }else{                   //���Zver
                    	qw = new DivWriter();
                    	qw.Write(ranking);
                    }
                }
            }
            cmd = ui.inputCmd(); //���̃R�}���h����
        }

        }
    }
    
