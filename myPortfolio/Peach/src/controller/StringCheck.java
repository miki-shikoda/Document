//米澤
package controller;

//文字列の内容チェックに使用
public class StringCheck {
    public boolean numcheck(String str){ //数字を判定
        boolean result = str.matches("^[1-9]?[0-9]+$");
        if(!result) System.out.println("\n[!] 整数で入力してください\n");
        return result;
    }

    public boolean num8check(String str){ //8ケタの数字(日付用)を判定
        boolean result = str.matches("^[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$");
        if(!result) System.out.println("\n[!] 日付(yyyymmdd)が正しくありません\n");
        return result;
    }

    public boolean num6check(String str){ //6ケタの数字(年月用)を判定
        boolean result = str.matches("^[0-9]{6}$");
        if(!result) System.out.println("\n[!] 年･月を6ケタの整数で入力してください\n");
        return result;
    }

    public boolean kanacheck(String str){ //全角ひらがなを判定
        boolean result = str.matches("^[ぁ-んー]*$");
        if(!result) System.out.println("\n[!] 全角ひらがなで入力してください\n");
        return result;
    }

    public boolean idcheck(String str){  //便名「EJ〇〇」を判定
        boolean result = str.matches("^EJ[0-9]{4}$");
        if(!result) System.out.println("\n[!] 便名は「EJ」から始まる英数字6ケタです\n");
        return result;
    }
    
    public boolean zipcheck(String str){  //郵便番号を判定
        boolean result = str.matches("^[0-9]{3}-[0-9]{4}$");
        if(!result) System.out.println("\n[!] 郵便番号は7ケタの数字(ハイフンあり)で入力してください\n");
        return result;
    }

    //出力文字列の桁数揃え用メソッド  str:文字列、whidth:揃えるバイト数(全角=>2、半角=>1)
    public String Bytecheck(String str, int whidth){
        int strsize = str.getBytes().length;
        while(strsize < whidth){
            str += " ";
            strsize = str.getBytes().length;
        }
        return str;
    }
}
