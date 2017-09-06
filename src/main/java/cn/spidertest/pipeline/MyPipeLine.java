package cn.spidertest.pipeline;

import cn.spidertest.po.TempInfo;
import cn.spidertest.util.PageUtil;
import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 万洪基 on 2017/9/4.
 */
public class MyPipeLine implements Pipeline {

    List<TempInfo> tempInfoList = new ArrayList<TempInfo>();

    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("info") != null) {
            TempInfo info = resultItems.get("info");
            if (tempInfoList.size() >= 2000) {
                try {
                    PageUtil.toFile_append(
                            JSON.toJSONString(tempInfoList) + "~!@#$%^&*()_+",
                            "F:\\tempInfo_singer.json"
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tempInfoList.clear();
            }
            tempInfoList.add(info);
        }
    }
}
