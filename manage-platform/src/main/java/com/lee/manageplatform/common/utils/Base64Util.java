package com.lee.manageplatform.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * base64 加密解密工具类
 * @author hanpeng
 * @date 2017-09-26
 */
public class Base64Util {
	//解密
	public static String decodeStr(String s) {
        byte[] b = null;  
        String result = null;  
        if (s != null) {

            try {
                b =  Base64.getDecoder().decode(s);
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }
	
	// 加密  
    public static String encodeStr(String str) {
        byte[] b = null;  
        String s = null; 
        if (str != null) { 
        	try {  
                b = str.getBytes("utf-8");  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }  
            if (b != null) {  
                s = Base64.getEncoder().encodeToString(b);
            } 
        }
         
        return s;  
    }
}
