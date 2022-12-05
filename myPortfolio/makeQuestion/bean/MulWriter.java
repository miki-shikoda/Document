package makeQuestion.bean;
/*   【作成者：チームIQ102】 CSVに書き出すクラス   */
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import makeQuestion.bean.Record;
import makeQuestion.model.Ranking;
import java.util.ArrayList;
import makeQuestion.view.ErrorUI;   //★追加

public class MulWriter extends QuesWriter{
    private double time;  //記録時間
    private String name;  //名前
    
    @Override
	public void Write(Ranking ranking){
		try(
			FileWriter fw = new FileWriter("makeQuestion\\掛け算ランキング.csv",false); //true=追加/false=上書
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
		){
	        ArrayList<Record> records = ranking.getRecords();
	        int rank = 1;
	        String line = "種類"+","+"順位"+","+"タイム"+","+"名前"+",";
	        pw.println(line);
	        
	        for(Record curr : records){
	        	line = "掛け算"+","+rank+"位"+","+curr.getTime()+","+curr.getName()+",";
				pw.println(line);
				pw.flush();  //一時的にメモリへ保存(バッファ) しているデータを出力先に書き出す
	            rank++;
	        }
        
		}catch(IOException e){
			ErrorUI err = new ErrorUI();  //★追加
			//System.out.println("ファイルを開けませんでした。");//★無効化
		}
	}
}
