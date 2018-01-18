package com.lee.manageplatform.common.upload.service;

import com.lee.manageplatform.common.upload.api.IUploadService;
import com.lee.manageplatform.common.upload.utils.UploadHelper;
import com.lee.manageplatform.common.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:文件上传接口实现
 *
 * @author: HANP
 * @date: 2017-10-17
 */
@Service("uploadService")
public class UploadServiceImpl implements IUploadService {

    private final static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    /**
     * 文件上传的业务处理方法
     *
     * @param type
     * @param fileName
     * @param realPath
     * @param file
     * @throws Exception
     * @author hanpeng
     * @date 2017-10-17 21:06
     */
    @Override
    public String upload(String type, String fileName, String realPath,
                         MultipartFile file) throws Exception {

        logger.info("文件上传类型type是："+type);
        if(StringUtil.isEmpty(type)) {
            type = UploadHelper.IMAGE;
        }
        //保存文件
        //得到后缀
        String extension = FilenameUtils.getExtension(fileName);
        String fileUri = UploadHelper.getUploadPath(type,extension).replaceFirst("/","");
        logger.info("上传的文件路径是："+fileUri);

        //上传附件目录创建
        String dir = fileUri.substring(0,fileUri.lastIndexOf("/")) ;
        Path path = Paths.get(dir);
        logger.info("文件目录是："+dir);
        if(!Files.isDirectory(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("上传图片创建上传文件夹错误： {}", e);
            }
            logger.info("文件目录:"+dir+"不存在创建文件目录成功");
        }

        if(StringUtils.equals("relateDownload", type)){
            logger.info("relateDownload判断成功");
            String tmpStr = fileUri.substring(fileUri.lastIndexOf("/") + 1);
            tmpStr = tmpStr.substring(tmpStr.indexOf("_") + 1);
            fileUri = fileUri.replace(tmpStr, fileName);
        }
        OutputStream out = null;
        InputStream in = null;
        try {
            out = new FileOutputStream(new File(fileUri));
            in = file.getInputStream();
            int b = 0;
            while((b=in.read())!=-1){ //读取文件
                out.write(b);
            }
            out.flush();

            return fileUri;
        }finally {
            in.close();
            out.close();
        }
    }

    /**
     * 文件上传的业务处理方法
     *
     * @throws Exception
     * @author hanpeng
     * @date 2017-10-17 22:14
     */
    @Override
    public String getMovie(String searchkey,String videotype) throws Exception {
        StringBuffer readOneLineBuff = new StringBuffer();
        String content = "";

        searchkey = URLEncoder.encode(searchkey, "utf-8");
        URL url = new URL("http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw="
                + searchkey + "&pageNo=1&pageSize=20&channelId="
                + videotype + "&inDays=7&media=v&sort=s");
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "utf-8"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            readOneLineBuff.append(line);
        }
        content = readOneLineBuff.toString();
        reader.close();
        return content;
    }

    /**
     * 图片在线管理的处理地址
     *
     * @param picNum
     * @param path
     * @throws Exception
     * @author hanpeng
     * @date 2017-10-18 10:14
     */
    @Override
    public String imageManager(Integer picNum, String path) throws Exception {
        //文件存储路径
        String realpath = path + UploadHelper.getReleasePathname();
        String imgStr ="";
        StringBuffer imgStrBuff=new StringBuffer();
        List<File> files = getFiles(realpath,new ArrayList<File>());
        if(picNum==null){
            picNum=10;
        }
        if(picNum>files.size()){
            picNum=files.size();
        }
        for(int i=0;i<picNum;i++){
            File file=files.get(i);
            imgStrBuff.append(file.getPath().replace(path,UploadHelper.getContentPath())+ UploadHelper.UE_SEPARATE_UE);
        }
        imgStr=imgStrBuff.toString();
        if(StringUtils.isNotBlank(imgStr)){
            imgStr = imgStr.substring(0,imgStr.lastIndexOf(UploadHelper.UE_SEPARATE_UE)).replace(File.separator, "/").trim();
        }
        return imgStr;
    }

    /**
     * 图片在线管理的处理地址
     *
     * @param upfileuri
     * @throws Exception
     * @author hanpeng
     * @date 2017-10-18 10:14
     */
    @Override
    public Map<String, Object> getRemoteImage(String upfileuri,String realPath) throws Exception {

        Map<String,Object> map = new HashMap<>();

        String urlPrefix = UploadHelper.getContentPath();

        String state = "";
        String[] arr = upfileuri.split(UploadHelper.UE_SEPARATE_UE);
        List<String> urls = new ArrayList<String>();
        List<String> srcs = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            String extension = FilenameUtils.getExtension(arr[i]);
            // 文件格式验证
            if (!UploadHelper.isValidImageExt(extension)) {
                state = "请上传jpg、png格式的图片";
                continue;
            }
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection conn = (HttpURLConnection) new URL(arr[i])
                    .openConnection();
            if (conn.getContentType().indexOf("image") == -1) {
                state = "ContentType Invalid";
                continue;
            }
            if (conn.getResponseCode() != 200) {
                state = "Request Error";
                continue;
            }
            //保存文件
            String pathname = UploadHelper.getUploadPath(UploadHelper.IMAGE, extension);
            File dest = new File(realPath+pathname);
            FileUtils.copyInputStreamToFile(conn.getInputStream(), dest);

            urls.add(urlPrefix + pathname);
            srcs.add(arr[i]);
        }
        StringBuilder outstr = new StringBuilder();
        for (String url : urls) {
            outstr.append(url).append(UploadHelper.UE_SEPARATE_UE);
        }
        StringBuilder srcUrl = new StringBuilder();
        for (String src : srcs) {
            srcUrl.append(src).append(UploadHelper.UE_SEPARATE_UE);
        }
        int sepLength = UploadHelper.UE_SEPARATE_UE.length();
        if (outstr.length() > sepLength) {
            outstr.setLength(outstr.length() - sepLength);
        }
        if (srcUrl.length() > sepLength) {
            srcUrl.setLength(srcUrl.length() - sepLength);
        }
        map.put("URL",outstr);
        map.put("TIP",state);
        map.put("SRC_URL",srcUrl);
        return map;
    }

    /**
     * 获取路径下的所有文件，添加到list中
     *
     * @throws Exception
     * @author hanpeng
     * @date 2017-10-18 10:14
     */
    private List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for(File file :subfiles ){
                if(file.isDirectory()){
                    getFiles(file.getAbsolutePath(),files);
                }else{
                    if(UploadHelper.isValidImageExt(FilenameUtils.getExtension(file.getName()))){
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

}
