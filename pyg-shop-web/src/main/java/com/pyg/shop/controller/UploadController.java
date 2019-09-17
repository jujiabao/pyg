package com.pyg.shop.controller;

import com.pyg.utils.FastDFSClient;
import com.pyg.utils.PygResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Title UploadController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-09-11 20:40
 */
@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    @RequestMapping("/upload")
    public PygResult upload(MultipartFile file) {
        PygResult result = null;
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String path = client.uploadFile(file.getBytes(), extName);
            return new PygResult(true, fileServerUrl + path);
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "上传失败");
        }
    }
}
