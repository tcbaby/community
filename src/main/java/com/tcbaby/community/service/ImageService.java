package com.tcbaby.community.service;

import com.tcbaby.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tcbaby
 * @date 20/05/07 10:49
 */
@Slf4j
@Service
public class ImageService {

    private final List<String> ALLOW_FILE_TYPE = Arrays.asList("image/png", "image/jpeg");

    @Value("${community.domain}")
    private String domain;
    @Value("${community.upload.path}")
    private String upload_path;

    public Map<String, String> saveImage(MultipartFile file) {
        HashMap<String, String> map = new HashMap<>();
        // 校验文件类型
        if (!ALLOW_FILE_TYPE.contains(file.getContentType())) {
           map.put("fileMsg", "不被允许的文件类型！");
            return map;
        }
        try {
            // 校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                map.put("fileMsg", "文件内容不能为空！");
                return map;
            }
            // 保存文件
            String filename = CommunityUtil.generateUUID() + "-" + file.getOriginalFilename();
            file.transferTo(new File(upload_path + filename));
            map.put("imageUrl", domain + "/user/headImage/" + filename);
            return map;
        } catch (IOException e) {
            log.error("图片上传失败：{}", e.getMessage());
            map.put("fileMsg", "上传失败，请稍后重试！");
            return map;
        }
    }

    public void getImage(String filename, OutputStream out) {
        try {
            FileInputStream in = new FileInputStream(upload_path + filename);
            byte[] buf = new byte[4*1024];
            int len = 0;
            while ((len = in.read(buf)) >= 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            log.error("文件读取失败：{}", e.getMessage());
        }
    }
}
