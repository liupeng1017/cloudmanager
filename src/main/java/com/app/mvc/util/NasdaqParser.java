package com.app.mvc.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class NasdaqParser {

    public static void main(String[] args) throws Exception {
        String symbol = "PG";
        String url = "http://vip.stock.finance.sina.com.cn/usstock/summary.php?s=PG";
        Document doc = Jsoup.connect(url).get();
        // 找到指定id的table及里面的body
        Element tbody = doc.select("#quotes_content_left_dividendhistoryGrid tbody").first();
        // 遍历tbody里的每个tr
        for(Element tr : tbody.children()) {
            // 获取tr下面td列表
            Elements tdList = tr.children();
            List<String> line = Lists.newArrayList();
            // 遍历每个td
            for (Element td : tdList) {
                // 有的td下面包含span
                Elements span = td.select("span");
                if (span == null || span.size() == 0) {
                    line.add(td.text());
                } else {
                    line.add(span.html());
                }
            }
            // line里的数据一次为 Ex/Eff Date, Type, Cash Amount,  Declaration Date, Record Date, Payment Date
            System.out.println(Joiner.on(",").join(line));
        }
    }
}
