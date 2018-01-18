package com.lee.manageplatform.common.upload.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件上传工具类
 */
public class UploadHelper {
	
	/*
	 * session中资源路径
	 */
	public static final String RESOURCE_NAME = "ResourceFile";
	
	/**
	 * 图片
	 */
	public static final String IMAGE = "image";

	/**
	 * 视频
	 */
	public static final String VIDEO = "video";
	
	/**
	 * 白皮書
	 */
	public static final String WHITERPAPER = "whiterpaper";
	
	/**
	 * 合作伙伴资质证书目录
	 */
	public static final String PARNTER_CERTIFICATEN = "parnterCertificaten";
	
	
	/**
	 * 快速上传文件夹
	 */
	public static final String UPLOAD_PATH = "/home/geecuser/web/apache-tomcat-8.5.23/temp";
	
	/**
	 * 视频后缀
	 */
	public static final String[] VIDEO_EXT = new String[] { "swf","f4v","mp4","rmvb","avi" };
	
	/**
	 * 图片的后缀
	 */
	public static final String[] IMAGE_EXT = new String[] { "jpg", "jpeg",
			"gif", "png", "bmp" };
	
	/**
	 * 白皮書的后缀
	 */
	public static final String[] WHITERPAPER_EXT = new String[] { "doc", "docx", "txt","PDF"};

    /**
     * 在线图片管理图片分隔符
     */
    public static final String UE_SEPARATE_UE="ue_separate_ue";

	/**
	 * 
	* 获取上传静态资源的路径
	* 
	* @return
	*<p>
	* 修改记录:sunjian6 created
	*</p>
	 */
	public static String getUploadPath(String type, String extension) {
		StringBuilder name = new StringBuilder();
		name.append('/').append(UPLOAD_PATH);
		name.append('/').append(type);
		name.append(UploadHelper.randomPathname(extension));
		return name.toString();
	}
	
	/**
	 * 
	* 获取项目路径：用于前台拼接url
	* 
	* @return
	*<p>
	* 修改记录:sunjian6 created
	*</p>
	 */
	public static String getContentPath() {
		
		return "/";
	}

    /**
     *
     * 获取项目的物理路径：用于保存文件
     *
     * @date 2017-10-18
     * @author hanpeng
     */
    public static String getRealPath(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath("");
        //转换目录分割符
        return realPath.replaceAll("\\\\", "/");
    }

	public static String getReleasePathname() {
		
		return UPLOAD_PATH;
	}
	
	private static String randomPathname(String pattern, String extension) {
		StringBuilder filename = new StringBuilder();
		DateFormat df = new SimpleDateFormat(pattern);
		filename.append(df.format(new Date()));
		filename.append(RandomStringUtils.random(10, '0', 'Z', true, true)
				.toLowerCase());
		if (StringUtils.isNotBlank(extension)) {
			filename.append(".").append(extension.toLowerCase());
		}
		return filename.toString();
	}

	public static String randomPathname(String extension) {
		return randomPathname("/yyyyMMdd/yyyyMMddHHmmss_", extension);
	}
	
	public static boolean isValidVideoExt(String ext) {
		
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : VIDEO_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 是否是图片
	 * 
	 * @param ext
	 * @return "jpg", "jpeg", "gif", "png", "bmp" 为文件后缀名者为图片
	 */
	public static boolean isValidImageExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : IMAGE_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getRandomKey() {
		
		StringBuilder randomKey = new StringBuilder();
		randomKey.append("ResourceFile");
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		randomKey.append(df.format(new Date()));
		randomKey.append(RandomStringUtils.random(4, '0', '9', true, true).toLowerCase());
		return randomKey.toString();
	}
	
}
