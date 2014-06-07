package test;  
  
public class Main {  
    public static void main(String[] args) {  
        String pwd1="123";  
        String pwd2="";  
        CipherUtil cipher = new CipherUtil();  
        System.out.println("未加密的密码:"+pwd1);  
        //将123加密  
        pwd2 = cipher.generatePassword(pwd1);  
        System.out.println("加密后的密码:"+pwd2);  
          
        System.out.print("验证密码是否下确:");  
        if(cipher.validatePassword(pwd2, pwd1)) {  
            System.out.println("正确");  
        }  
        else {  
            System.out.println("错误");  
        }  
    }  
}  