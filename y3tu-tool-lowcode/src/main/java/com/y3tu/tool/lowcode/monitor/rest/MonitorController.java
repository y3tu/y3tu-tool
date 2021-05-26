package com.y3tu.tool.lowcode.monitor.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.SystemUtil;
import com.y3tu.tool.lowcode.monitor.entity.ClassLoadDto;
import com.y3tu.tool.lowcode.monitor.entity.GcDto;
import com.y3tu.tool.lowcode.monitor.entity.JstatDto;
import com.y3tu.tool.lowcode.monitor.jvm.Jstack;
import com.y3tu.tool.lowcode.monitor.jvm.Jstat;
import com.y3tu.tool.lowcode.monitor.jvm.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 监控Api
 *
 * @author y3tu
 */
@RestController
@RequestMapping("y3tu-tool-lowcode/monitor")
@Slf4j
public class MonitorController {

    @Value("${spring.application.name:''}")
    private String name;

    /**
     * 线程信息
     */
    @RequestMapping("/thread")
    public R thread() {
        return R.success(Jstack.jstack());
    }

    /**
     * 类加载信息
     *
     * @return
     */
    @RequestMapping("/classLoad")
    public R classLoad() {
        List<JstatDto> jstatDtoList = Jstat.jstatClass();
        ClassLoadDto dto = new ClassLoadDto();
        dto.setLoaded(jstatDtoList.get(0).getValue());
        dto.setBytes1(jstatDtoList.get(1).getValue());
        dto.setUnloaded(jstatDtoList.get(2).getValue());
        dto.setBytes2(jstatDtoList.get(3).getValue());
        dto.setTime1(jstatDtoList.get(4).getValue());
        dto.setCompiled(jstatDtoList.get(5).getValue());
        dto.setFailed(jstatDtoList.get(6).getValue());
        dto.setInvalid(jstatDtoList.get(7).getValue());
        dto.setTime2(jstatDtoList.get(8).getValue());
        return R.success(dto);
    }

    /**
     * gc信息  堆内存信息
     */
    @RequestMapping("/gc")
    public R gc() {
        List<JstatDto> jstatDtoList = Jstat.jstatGc();
        GcDto dto = new GcDto();
        dto.setS0c(jstatDtoList.get(0).getValue());
        dto.setS1c(jstatDtoList.get(1).getValue());
        dto.setS0u(jstatDtoList.get(2).getValue());
        dto.setS1u(jstatDtoList.get(3).getValue());
        dto.setEc(jstatDtoList.get(4).getValue());
        dto.setEu(jstatDtoList.get(5).getValue());
        dto.setOc(jstatDtoList.get(6).getValue());
        dto.setOu(jstatDtoList.get(7).getValue());
        dto.setMc(jstatDtoList.get(8).getValue());
        dto.setMu(jstatDtoList.get(9).getValue());
        dto.setCcsc(jstatDtoList.get(10).getValue());
        dto.setCcsu(jstatDtoList.get(11).getValue());
        dto.setYgc(jstatDtoList.get(12).getValue());
        dto.setYgct(jstatDtoList.get(13).getValue());
        dto.setFgc(jstatDtoList.get(14).getValue());
        dto.setFgct(jstatDtoList.get(15).getValue());
        dto.setGct(jstatDtoList.get(16).getValue());
        return R.success(dto);
    }

    /**
     * 执行jvm linux命令
     */
    @RequestMapping("/runExecute")
    public R runExecute(@RequestParam Map<String, String> params) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : params.keySet()) {
            list.add(params.get(s));
        }
        String[] array = (String[]) list.toArray(new String[list.size()]);
        log.debug("run exec:{}", Arrays.toString(array));
        String result = SystemUtil.executeCmd(array);
        return R.success(result);
    }

    /**
     * 系统物理内存
     */
    @RequestMapping("/systemInfo")
    public R systemInfo() {
        Server server = new Server();
        server.copyTo();
        return R.success(server);
    }
}
