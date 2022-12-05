//�������q

package makeQuestion.controller;

import makeQuestion.model.Question; 
import makeQuestion.model.SubQuestion; 
import makeQuestion.model.MulQuestion; 
import makeQuestion.model.DivQuestion; 

import makeQuestion.model.Ranking;
import makeQuestion.view.QuesUI;
import makeQuestion.bean.Record;

//�o��v���O�����̑S�̗̂���𐧌䂷��N���X
//extends��addController���p��
   
public class QuesController extends AddController{ 

    private Question question = new Question();  //���I�u�W�F�N�g�ւ̎Q��
    private Ranking ranking = new Ranking();     //�����L���O�I�u�W�F�N�g�ւ̎Q��
    private QuesUI ui = new QuesUI();                //���[�U I/F �I�u�W�F�N�g�ւ̎Q��
    
    
    //�o��v���O�����S�̗̂���
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
                    String name = ui.inputName();
                    Record rec = new Record(time, name);
                    ranking.insert(rec);
                }
            }
            
            cmd = ui.inputCmd(); //���̃R�}���h����

        }

        }
    }
    
