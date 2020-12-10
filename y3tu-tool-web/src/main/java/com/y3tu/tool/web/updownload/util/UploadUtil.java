package com.y3tu.tool.web.updownload.util;

import cn.hutool.http.HttpRequest;
import com.y3tu.tool.core.http.HttpUtil;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.util.IdUtil;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.exception.UpDownLoadException;
import com.y3tu.tool.web.updownload.dto.PictureDto;
import com.y3tu.tool.web.util.TranslatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传工具类
 *
 * @author y3tu
 */
@Slf4j
public class UploadUtil {

    /**
     * 免费图床 外网才能使用，可能会有连接不稳定现象 慎用！
     */
    public static final String SM_MS_URL = "https://sm.ms/api";

    /**
     * MultipartFile转File
     *
     * @param multipartFile 浏览器上传文件
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String suffix = "." + FileUtil.getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), suffix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

    /**
     * 将上传文件存入运行主机指定路径
     *
     * @param file     浏览器上传文件
     * @param filePath 上传路径
     */
    public static boolean upload(MultipartFile file, String filePath) {
        try {
            // getCanonicalFile 可解析正确各种路径
            File dest = new File(filePath).getCanonicalFile();
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    log.error("创建文件夹失败！路径:" + filePath);
                }
            }
            // 文件写入
            file.transferTo(dest);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 上传图片到图传网站
     *
     * @param multipartFile 上传的图片
     * @param token         特定用户图床网站token信息 可为空
     * @return 上传后的图片url地址，可以直接访问
     */
    public static String uploadPictureToSmMs(MultipartFile multipartFile, String token) {
        File file = toFile(multipartFile);
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("smfile", file);
        // 上传文件
        String result = "";

        if (StrUtil.isEmpty(token)) {
            result = HttpRequest.post(SM_MS_URL + "/v2/upload")
                    .form(paramMap)
                    .timeout(20000)
                    .execute().body();
        } else {
            result = HttpRequest.post(SM_MS_URL + "/v2/upload")
                    .header("Authorization", token)
                    .form(paramMap)
                    .timeout(20000)
                    .execute().body();
        }

        Map resultMap = JsonUtil.parseObject(result, Map.class);
        if (!resultMap.get("code").toString().equals("success")) {
            throw new UpDownLoadException(TranslatorUtil.translate(resultMap.get("message").toString()));
        }
        PictureDto pictureDto = JsonUtil.parseObject(resultMap.get("data").toString(), PictureDto.class);

        //删除临时文件
        FileUtil.del(file);
        return pictureDto.getUrl();
    }

    /**
     * 删除图床上的指定图片
     *
     * @param pictureDto
     */
    public static void deletePictureFromSmMs(PictureDto pictureDto) {
        //直接访问图片删除url就可删除图片
        HttpUtil.get(pictureDto.getDelete());
    }

    /**
     * 获取指定用户的所有上传图片信息
     *
     * @param token token信息
     * @return
     */
    public static List<PictureDto> getAllPictureFromSmMs(String token) {
        String result = HttpRequest.get(SM_MS_URL + "/v2/upload_history")
                //头信息，多个头信息多次调用此方法即可
                .header("Authorization", token)
                .timeout(20000)
                .execute().body();

        Map resultMap = JsonUtil.parseObject(result, Map.class);
        List<PictureDto> pictureDtoList = JsonUtil.parseArray(resultMap.get("data").toString(), PictureDto.class);
        return pictureDtoList;
    }


}
