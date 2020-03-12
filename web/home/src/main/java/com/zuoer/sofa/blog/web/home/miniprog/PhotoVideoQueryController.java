package com.zuoer.sofa.blog.web.home.miniprog;

import com.alipay.sofa.rpc.common.utils.JSONUtils;
import com.zuoer.sofa.blog.web.home.miniprog.model.Item;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zuoer
 * @version PhotoVideoQuery, v 0.1 2020/3/11 14:28 zuoer Exp $
 */
@Controller
public class PhotoVideoQueryController {
    @ResponseBody
    @RequestMapping("/photoVideoQuery")
    public String hello(){
        List<Item> items=new ArrayList<>();
        items.add(new Item("photo","https://yswy-video-screen-shot.oss-cn-hangzhou.aliyuncs.com/4155603f31fd95dbb2bc38b143d644ea_cover.jpg","1","https://yswy-video-origin.oss-cn-hangzhou.aliyuncs.com/111eb4d79b2ec7ba1ec8ddc0ccad21b8.mp4","一个月"));
        items.add(new Item("video","https://yswy-video-screen-shot.oss-cn-hangzhou.aliyuncs.com/4155603f31fd95dbb2bc38b143d644ea_cover.jpg","2","https://yswy-video-origin.oss-cn-hangzhou.aliyuncs.com/111eb4d79b2ec7ba1ec8ddc0ccad21b8.mp4","一岁"));
        return JSONUtils.toJSONString(items);
    }

}
