package com.tushar;

import com.tushar.utilities.JWT_Util;

public class Test 
{
    public static void main( String[] args )
    {
    	String id="1102";
    	String subject="TKhadse";
    	String key="Khadse";
    	String token=JWT_Util.getToken(id, subject, key);
        System.out.println("Generated JWT Token: "+token );
        boolean isValidToken = JWT_Util.isvalidToken(key, token);
        System.out.println("isValidToken:"+isValidToken);
        
    }
}
