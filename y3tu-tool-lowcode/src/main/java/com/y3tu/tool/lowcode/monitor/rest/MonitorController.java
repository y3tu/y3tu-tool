package com.y3tu.tool.lowcode.monitor.rest;

import com.y3tu.tool.core.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 监控Api
 *
 * @author y3tu
 */
@RestController
@RequestMapping("y3tu-tool-lowcode/monitor")
@Slf4j
public class MonitorController {

    @Value("${spring.application.name}")
    private String name;

    public R thread() {
        String date = time();
        try {

        }catch (Exception e){
            e.printStackTrace();

        }

        return R.success();
    }

    /**
     * 现在时间
     */
    public static String time() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(new Date());
    }

}
