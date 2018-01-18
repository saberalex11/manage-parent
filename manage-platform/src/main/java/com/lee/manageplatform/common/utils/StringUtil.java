package com.lee.manageplatform.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符工具类
 * @author hanp
 * 2016年4月13日
 */
public class StringUtil {
	
	public static String[] chars = new String[] {
		"0", "1", "2", "3", "4", "5", "7", "8", "9",
		"a", "b", "c", "d", "e", "f",  
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
        "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
        "W", "X", "Y", "Z" };  

	public static String createRandomKey(int num) {
		// 取随机产生的认证码(6位数字)
		String sRand = "";
		for (int i = num; i >0 ; i--) {
			Double rand =Math.random()*10;
			sRand += rand.intValue()+"";
		}
		return sRand;
	}
	
	public static String createRandomChart(int num) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = num; i >0 ; i--) {
			sb.append(chars[r.nextInt(56)]);
		}
		return sb.toString();
	}
	
    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转成字符串
     * @param in
     * @return
     * @throws Exception
     * hanp
     * 2016年4月13日
     */
    public static String convertInputStreamToString(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();

    }

    /**
     * 判断字符串为空
     * @param str
     * @return
     * @date 2016-11-13
     */
    public static boolean isBlank(String str) {
        if(str != null && !"".equals(str) && !"".equals(str.trim())){
            return false;
        }
        return true;
    }

    /**
     * 判断字符串不为空
     * @param str
     * @return
     * @date 2016-11-13
     */
    public static boolean isNotBlank(String str) {
        if(str != null && !"".equals(str) && !"".equals(str.trim())
                && !str.equals("null")){
            return true;
        }
        return false;
    }

    /**
     * 判断字符串数组为空
     * @param strings
     * @return
     * @date 2016-11-13
     */
    public static boolean isNotBlank(String... strings) {
        if(strings == null) {
            return false;
        } else {
            String[] arr$ = strings;
            int len$ = strings.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String str = arr$[i$];
                if(str == null || "".equals(str.trim())) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 判断对象数组不为空
     * @param paras
     * @return
     * @date 2016-11-13
     */
    public static boolean isNotEmpty(Object... paras) {
        if(paras == null) {
            return false;
        } else {
            Object[] arr$ = paras;
            int len$ = paras.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Object obj = arr$[i$];
                if(obj == null) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * 判断对象不为空
     * @param obj
     * @return
     * @date 2016-11-13
     */
    public static boolean isNotEmpty(Object obj) {
        if(obj != null && !"".equals(obj.toString().trim())
                && !"null".equals(obj.toString().trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象为空
     * @param obj
     * @return
     * @date 2016-11-13
     */
    public static boolean isEmpty(Object obj) {
        if(obj == null || "".equals(obj.toString().trim())) {
            return true;
        }
        return false;
    }

    /**
     * 首字母转换成小写
     * @param str
     * @return
     * @date 2016-11-13
     */
    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if(firstChar >= 65 && firstChar <= 90) {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] + 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    /**
     * 首字母转换成大写
     * @param str
     * @return
     * @date 2016-11-13
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if(firstChar >= 97 && firstChar <= 122) {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] - 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    /**
     *  转换成驼峰规则
     * @return
     * @date 2016-11-13
     */
    public static String toCamelCase(String stringWithUnderline) {
        if(stringWithUnderline.indexOf(95) == -1) {
            return stringWithUnderline;
        } else {
            stringWithUnderline = stringWithUnderline.toLowerCase();
            char[] fromArray = stringWithUnderline.toCharArray();
            char[] toArray = new char[fromArray.length];
            int j = 0;

            for(int i = 0; i < fromArray.length; ++i) {
                if(fromArray[i] == 95) {
                    ++i;
                    if(i < fromArray.length) {
                        toArray[j++] = Character.toUpperCase(fromArray[i]);
                    }
                } else {
                    toArray[j++] = fromArray[i];
                }
            }

            return new String(toArray, 0, j);
        }
    }

    /**
     * 拼接字符串
     * @param stringArray
     * @return
     * @date 2016-11-13
     */
    public static String join(String[] stringArray) {
        StringBuilder sb = new StringBuilder();
        String[] arr$ = stringArray;
        int len$ = stringArray.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            sb.append(s);
        }

        return sb.toString();
    }

    public static String join(String[] stringArray, String separator) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < stringArray.length; ++i) {
            if(i > 0) {
                sb.append(separator);
            }

            sb.append(stringArray[i]);
        }

        return sb.toString();
    }

    
    /**
    * @Description: 截取字符串
    * @param @param string
    * @param @param begin
    * @param @param end
    * @author hanp 
    * @date 2016年3月14日 下午3:20:28
     */
    public static String stringCut(String string,int begin,int end){   	
    	if(string != null && !"".equals(string.trim())){
			if(string.length()>end){
		      return string.substring(begin,end);   	
			}	
    	}
		return string;
    }
    
    
    /** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][1,3,4,5,7,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
    
    public static String format(Integer second){
		String  time="00:00";
		if(second !=null){
	         if(second!=null){
	             Double s=Double.parseDouble(String.valueOf(second));
	             String format;
	             Object[] array;
	             Integer hours =(int) (s/(60*60));
	             Integer minutes = (int) (s/60-hours*60);
	             Integer seconds = (int) (s-minutes*60-hours*60*60);
	             if(hours>0){
	                 format="%1$s:%2$s:%3$s";
	                 array=new Object[]{hours,formatNum(minutes),formatNum(seconds)};
	             }else{
	            	 format="%1$s:%2$s";
	                 array=new Object[]{formatNum(minutes),formatNum(seconds)};
	             }
	             time= String.format(format, array);
	         }
		}
		return time;
	}
    
    /**
     * 拼接头像
     * @param headImg
     * @param domain
     * @return
     * wangsj
     * 2016年7月4日
     */
    public static String mosaicHeadImg(String headImg, String domain){
    	if(StringUtil.isNotBlank(headImg)){
    		if(headImg.startsWith("http://") || 
    					headImg.startsWith("https://")){
    			return headImg;
    		}else{
    			return domain+headImg;
    		}
    	}
    	return null;
    }
    
    /**
     * 获得文件的后缀名
     * @param fileName
     * @return
     * 2016年7月26日
     */
    public static String getSuffixName(String fileName){
    	if(fileName.indexOf(".") <= 0 ||
    			fileName.lastIndexOf(".") >= fileName.length()-1){
    		return null;
    	}
    	return fileName.substring(fileName.lastIndexOf("."),fileName.length() );
    }
    
	private static String formatNum(Integer n){
		if(n<10){
			return String.valueOf("0"+n);
		}else{
			return String.valueOf(n);
		}
	}
    
	/** 
	* @ClassName: TestString 
	* @Description: 字符在字符串中出现的次数
	* @author hanp 
	* @date 2015年12月22日 下午1:32:56 
	*  
	*/
	public static int occurTimes(String string, String a) {
	    int pos = -2;
	    int n = 0;
	    if(string != null && a != null){
	    	 while (pos != -1) {
	 	        if (pos == -2) {
	 	            pos = -1;
	 	        }
	 	        pos = string.indexOf(a, pos + 1);
	 	        if (pos != -1) {
	 	            n++;
	 	        }
	 	    }
	    }
	    return n;
	}
	
	/**
	 * 构建方法名字
	 * @param fieldName
	 * @return
	 * 2016年8月25日
	 */
	public static String getGetMethodName(String fieldName){
		if(isNotBlank(fieldName)){
			if(fieldName.length() > 1){
				return "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length());
			}else{
				return "get"+fieldName.toUpperCase();
			}
		}
		return null;
	}
	
	/**
	 * 构建方法名字
	 * @param fieldName
	 * @return
	 * 2016年8月25日
	 */
	public static String getSetMethodName(String fieldName){
		if(isNotBlank(fieldName)){
			if(fieldName.length() > 1){
				return "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length());
			}else{
				return "set"+fieldName.toUpperCase();
			}
		}
		return null;
	}

    /**
     * 首字母转换成大写
     * @author hanp
     * @date 2017-02-20
     */
    public static String convertCharset(String origin,String originCharSet, String destCharSet){
        String newStr= null ;
        try {
            byte[] temp = origin.getBytes(originCharSet);//这里写原编码方式
            newStr=new String(temp,destCharSet);//这里写转换后的编码方式
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newStr;
    }

}
