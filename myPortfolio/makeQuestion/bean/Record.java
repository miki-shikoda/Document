package makeQuestion.bean;

/*
  ���ԋL�^
*/
public class Record{
    private double time;  //�L�^����
    private String name;  //���O
    
    //�R���X�g���N�^
    public Record(double time, String name){
        setTime(time);
        setName(name);
    }
    
    //�Z�b�^�[&�Q�b�^�[
    public void setTime(double time){
        this.time = time;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public double getTime(){
        return time;
    }
    
    public String getName(){
        return name;
    }
}
