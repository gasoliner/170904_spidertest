package cn.spidertest.pageprocessor;

import cn.spidertest.po.TempInfo;
import cn.spidertest.util.PageUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.io.IOException;
import java.util.*;

/**
 * Created by 万洪基 on 2017/8/12.
 */
public class MyPageProcessor implements PageProcessor {

    private ArrayList<String> userAgents;
    private int start;
    private int end;
    private long currentSingerId;
    private String currentInfo;


    public MyPageProcessor(int start , int end) {
        currentSingerId = 0;
        this.start = start;
        this.end = end;
        try {
            userAgents = PageUtil.getUserAgentList("F:\\userAgents.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //     Domain
    private String DOMAIN = "http://www\\.kuwo\\.cn";
    //    歌手列表
    private String SINGER_LIST_VIEW = DOMAIN + "/artist/indexAjax.*";
    //    歌曲列表
    private String SONG_LIST_VIEW = DOMAIN + "/artist/contentMusicsAjax\\?artistId=\\d+&pn=\\d*&rn=\\d*";
    //    歌曲详情
    private String SONG_VIEW = DOMAIN + "/yinyue/\\d+";
    //    歌手首页
    private String SINGER_INDEX_VIEW = DOMAIN + "/artist/content\\?name=.+";


    private Site site =
            Site.me().addHeader("Referer",DOMAIN)
                    .addHeader("Cookie",
                            ""
                    )
                    .setDomain(DOMAIN)
                    .setRetryTimes(1)
                    .setSleepTime(1)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");


    private boolean flag1 = true;

    public void process(Page page) {

        /***
         * 如果是歌手列表页面
         * http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0
         * info：
         * SINGER_INDEX_VIEW（歌手首页）
         */
        if (page.getUrl().regex(SINGER_LIST_VIEW).match()) {
//            System.out.println("如果是歌手列表页面");
            page.addTargetRequests(page.getHtml().links().regex(SINGER_INDEX_VIEW).all());
            if (flag1){
                List<String> urlList = new LinkedList<String>();
                for (int i = start; i < end; i++) {
                    urlList.add("http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn="+i);
                }
                page.addTargetRequests(urlList);
                flag1 = false;
            }
            currentInfo = "kwSpider/歌手列表页面/";

        }
        /***
         * 如果是歌手首页
         * http://www.kuwo.cn/artist/content?name=%E8%B5%B5%E9%9B%B7
         * info：
         * <div class="page" data-page="12"></div>
         * 歌曲列表总数
         * 歌手artistId
         */
        if (page.getUrl().regex(SINGER_INDEX_VIEW).match()) {
//            System.out.println("如果是歌手首页");
            String listCount = page.getHtml().xpath("//div[@class='page']/outerHtml()").get();
            listCount = PageUtil.getValueByKeyInHtml(listCount,"data-page");
            currentSingerId = Long.parseLong(PageUtil.getValueByKeyInHtml(
                    page.getHtml().xpath("//div[@class='artistTop']/outerHtml()").get(),
                    "data-artistid"
            ));
            String currentSinger = page.getHtml().xpath("").get();
            currentInfo = "kwSpider/歌手首页页面/" + currentSinger + "/" + currentSingerId + "/" + listCount;
            TempInfo info = new TempInfo();
            info.setCurrentSinger(currentSinger);
            info.setCurrentSingerId(currentSingerId);
            info.setListCount(listCount);
            page.putField("info",info);
        }

//        必要的时候打开这句话
//        PageUtil.stopRandomTime(1,1500);
        site.setUserAgent(randomUserAgent());
        currentInfo = PageUtil.dateToString(new Date(System.currentTimeMillis())) + ":\t" + currentInfo;
        System.out.println("currentInfo:\t" + currentInfo);

    }

    public Site getSite() {
        return site;
    }

    public String randomUserAgent(){
        Random random = new Random();
        return userAgents.get(random.nextInt(userAgents.size()-1));
    }

    public String getCurrentInfo() {
        return currentInfo;
    }
}

