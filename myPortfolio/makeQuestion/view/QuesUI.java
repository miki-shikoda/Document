//佐藤聖子
package makeQuestion.view;

//いらない）import makeQuestion.bean.Record;
//いらない）import makeQuestion.model.Ranking;
//いらない）import java.util.ArrayList;

import java.util.Scanner;

/*
出題プログラムのユーザーインターフェースを提供するクラス。
*/

public class QuesUI extends AddUI{ 
    private Scanner in = new Scanner(System.in);
    
    public int selectQuestion(){
        System.out.print("1:足し算　2:引き算　3:掛け算　その他:割り算  => ");
        return in.nextInt();
    }
    
} 
        
