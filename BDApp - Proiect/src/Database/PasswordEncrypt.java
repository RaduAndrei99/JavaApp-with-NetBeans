/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author budea
 */
public class PasswordEncrypt {
    private final static int offset = 10;
    static String getEncryptedPassword(String plainText){
        String encrypted_pass = "";
   
        if(plainText != null && plainText.length() >= 6 && plainText.length() <= 20 ){
            for(int i=0;i<plainText.length();++i){   
                int ch = plainText.charAt(i);
                
                ch = ch + offset ;
                
                if(ch > 'z')
                {
                    ch = '!' + (ch - 'z') - 1;
                }
                
                
                String hex = Integer.toHexString(ch);

                encrypted_pass += hex;
            }  
        }
        
        return encrypted_pass;
    }
    
    
    static String getPlainTextPassword(String encryptedPass){
        String plain_text_pass = "";
   
        if(encryptedPass != null && encryptedPass.length() >= 12 && encryptedPass.length() <= 40 ){
            for(int i=0;i<encryptedPass.length();i+=2){   
                String hex = encryptedPass.substring(i, i + 2);
                int ch = Integer.parseInt(hex, 16);
                
                ch = ch - offset ;
                
                if( ch < '!')
                {
                   ch = 'z' - ('!' - ch) + 1; 
                }
                
                plain_text_pass += (char)ch;
            }  
        }
        
        return plain_text_pass;
    }
    
    public static void main(String []argv){
        System.out.println(PasswordEncrypt.getEncryptedPassword("parola_de_nespart"));
        System.out.println(PasswordEncrypt.getPlainTextPassword("7a6b2279766b696b6e777378"));
    }

}
