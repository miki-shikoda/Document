package makeQuestion.controller;

import makeQuestion.model.Question;
import makeQuestion.model.Ranking;
import makeQuestion.view.AddUI;
import makeQuestion.bean.Record;

/*
  �o��v���O�����̃R���g���[��
*/
public class AddController{
    private Question question = new Question();
    private Ranking ranking = new Ranking();
    private AddUI ui = new AddUI();
    
    //--------------------------------------------------------------------------
    // �o��v���O�����S�̂̓���
    // ���� : �Ȃ�
    // �ߒl : �Ȃ�
    //--------------------------------------------------------------------------
    public void exec(){
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
