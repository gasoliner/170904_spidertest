package cn.spidertest.po;

/**
 * Created by 万洪基 on 2017/9/4.
 */
public class TempInfo {

    private Long currentSingerId;

    private String currentSinger;

    private String listCount;

    public Long getCurrentSingerId() {
        return currentSingerId;
    }

    public void setCurrentSingerId(Long currentSingerId) {
        this.currentSingerId = currentSingerId;
    }

    public String getCurrentSinger() {
        return currentSinger;
    }

    public void setCurrentSinger(String currentSinger) {
        this.currentSinger = currentSinger;
    }

    public String getListCount() {
        return listCount;
    }

    public void setListCount(String listCount) {
        this.listCount = listCount;
    }
}
