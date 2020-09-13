package com.antzuhl.kibana.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AntzUhl
 * @Date 13:20
 */
@Slf4j
public class SQLQueryParser {

    public static String parser(String applicationName, String result) {
        result = result.replaceAll("&gt;", ">");
        result = result.replaceAll("&lt;", "<");
        String timeHtmlBegin = "<!DOCTYPE html><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" + html_style() + "</head><body><table><tr><th style=\"width: 20%\">" + applicationName + "</th><th style=\"width: 10%\">总量</th><th style=\"width: 10%\">0~100ms</th><th style=\"width: 10%\">101~200ms</th><th style=\"width: 10%\">201~500ms</th><th style=\"width: 10%\">501ms~1s</th><th style=\"width: 10%\">>1s</th></tr>";
        String time_html_end = "<tr><td colspan=\"7\" style=\"background:#92D14F\">&nbsp;SQL耗时统计：重点关注200ms以上的 </td></tr></table></body></html>";
        try {
            JSONObject json = JSON.parseObject(result);
            String trBody = "";
            JSONArray array = json.getJSONObject("aggregations").getJSONObject("2").getJSONArray("buckets");
            int length = array.size();
            for (int i=0; i<length; i++) {
                JSONObject item = array.getJSONObject(i);
                String db_table = item.getString("key");
                Integer total = item.getInteger("doc_count");

                JSONObject d_c_b = item.getJSONObject("3").getJSONObject("buckets");

                Integer status_lt_100 = d_c_b.getJSONObject("100ms").getInteger("doc_count");
                Integer status_lt_200 = d_c_b.getJSONObject("100-200ms").getInteger("doc_count");
                Integer status_200_between_500 = d_c_b.getJSONObject("200-500ms").getInteger("doc_count");
                Integer status_500_between_1000 = d_c_b.getJSONObject("500-1000ms").getInteger("doc_count");
                Integer status_tt_1s = d_c_b.getJSONObject(">1s").getInteger("doc_count");

                System.out.printf("\t%s,\t%s,\t%s,\t%s,\t%s,\t%s,\t%s" ,db_table, total, status_lt_100,
                        status_lt_200, status_200_between_500, status_500_between_1000, status_tt_1s);

                float val_100 = Math.round((float)status_lt_100 / (float)(total)*100);
                float val_200 = Math.round((float)status_lt_200 / (float)(total)*100);
                float val_200_500 = Math.round((float)status_200_between_500 / (float)(total)*100);
                float val_500_1000 = Math.round((float)status_500_between_1000 / (float)(total)*100);
                float val_1s = Math.round((float)status_tt_1s / (float)(total)*100);
                String tr_body_item = String.format("<tr><td>%s</td><td>%s</td><td>%s(%s)</td><td>%s(%s)</td><td%s>%s(%s)</td><td%s>%s(%s)</td><td%s>%s(%s)</td></tr>",
                        db_table, total, status_lt_100, val_100,
                        status_lt_200, val_200, css_highlight(status_200_between_500, "moccasin"),
                        status_200_between_500, val_200_500,
                        css_highlight(status_500_between_1000, "sandybrown"), status_500_between_1000,
                        val_500_1000, css_highlight(status_tt_1s, "orangered"), status_tt_1s,
                        val_1s);
                trBody += tr_body_item;
            }
            return timeHtmlBegin + trBody + time_html_end;
        } catch (Exception e) {
            return timeHtmlBegin + "查询异常" + time_html_end;
        }
    }


    public static String css_highlight(int count, String color) {
        if (count > 0)
            return " style=\"background: " + color + "\"";
        else
            return "";
    }

    public static String html_style() {
        return "<style type=\"text/css\">\n" +
                "        body {\n" +
                "          padding: 0 2% 0 2%;\n" +
                "        }\n" +
                "        table {\n" +
                "          width: 100%;\n" +
                "          font: normal 14px/22px \"microsoft yahei\";\n" +
                "          border-collapse: 5px;\n" +
                "          border-spacing: 1px;\n" +
                "          background: #999;\n" +
                "          margin-top: 20px;\n" +
                "        }\n" +
                "        table th{\n" +
                "          font-weight: bold;\n" +
                "          background: #92D14F;\n" +
                "        }\n" +
                "        table td{\n" +
                "          font-weight: nomal;\n" +
                "          background: #ffffff;\n" +
                "        }\n" +
                "        \n" +
                "        table tr.highlight td{\n" +
                "          font-weight: bold;\n" +
                "        }\n" +
                "        \n" +
                "        table tr td.highlight{\n" +
                "          color: white;\n" +
                "          background: red;\n" +
                "        }\n" +
                "    </style>";
    }
}
