package cn.spidertest;

import cn.spidertest.pageprocessor.MyPageProcessor;
import org.junit.Test;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 万洪基 on 2017/9/4.
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> numberList = new ArrayList<Integer>();
        numberList.add(0);
        for (int i = 1 ; i < 6500; i++) {
            double number = 0.001 * Math.pow(i,2);
            int in  = 0;
            if (number < 1) {
                in = numberList.get(numberList.size()-1) + 1;
            } else {
                in = numberList.get(numberList.size()-1) + (int)number;
            }
            if (in > 6797) {
                int last = numberList.get(numberList.size() - 1);
                if (last != 6797) {
                    numberList.add(6797);
                }
                break;
            }
            numberList.add(in);
        }
        for (int i = 0 ; i < numberList.size()-1; i++) {
            int start = numberList.get(i);
            int end = numberList.get(i+1);
            MyPageProcessor processor = new MyPageProcessor(start,end);
            Spider spider = new Spider(processor);
        }
    }

}
