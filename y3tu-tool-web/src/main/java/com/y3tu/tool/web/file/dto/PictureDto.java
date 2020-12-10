package com.y3tu.tool.web.file.dto;

import lombok.Data;

/**
 * Sm.Ms图床 api返回对象
 *
 * @author y3tu
 */
@Data
public class PictureDto {
    String filename;
    String storename;
    String size;
    String path;
    String url;
    String delete;
    String width;
    String height;
    String hash;
}
