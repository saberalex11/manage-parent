package com.lee.manageplatform.common.upload.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Description:文件上传接口
 *
 * @author: HANP
 * @date: 2017-10-17
 */
public interface IUploadService {

    /**
     * 文件上传的业务处理方法
     *
     * @author hanpeng
     * @date 2017-10-17 21:06
     * @throws Exception
     */
    String upload(String type, String fileName, String realPath, MultipartFile file) throws Exception;

    /**
     * 文件上传的业务处理方法
     *
     * @author hanpeng
     * @date 2017-10-17 22:14
     * @throws Exception
     */
    String getMovie(String searchkey, String videotype) throws Exception;

    /**
     * 图片在线管理的处理地址
     *
     * @author hanpeng
     * @date 2017-10-18 10:14
     * @throws Exception
     */
    String imageManager(Integer picNum, String path) throws Exception;

    /**
     * 图片在线管理的处理地址
     *
     * @author hanpeng
     * @date 2017-10-18 10:14
     * @throws Exception
     */
    Map<String,Object> getRemoteImage(String upfileuri, String realPath) throws Exception;
}
