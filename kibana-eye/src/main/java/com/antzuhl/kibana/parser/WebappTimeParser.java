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
public class WebappTimeParser {

    public static String parser(String applicationName, String result) {
        result = result.replaceAll("&gt;", ">");
        result = result.replaceAll("&lt;", "<");
        String timeHtmlBegin = "<!DOCTYPE html><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" + html_style() + "</head><body><table>" +
                "<tr><th style=\"width: 20%\">" + applicationName + "</th>" +
                "<th style=\"width: 10%\">总量</th> " +
                "<th style=\"width: 10%\">0~200ms</th>" +
                "<th style=\"width: 10%\">201~500ms</th>" +
                "<th style=\"width: 10%\">501ms~1s</th" +
                "><th style=\"width: 10%\">>1s</th></tr>";
        String time_html_end = "<tr><td colspan=\"7\" style=\"background:#92D14F\">&nbsp;NG耗时统计：0~200ms小于95%时整行加粗，200ms以上大于1%时单元格红字显示 </td></tr></table></body></html>";
        try {
            JSONObject json = JSON.parseObject(result);
            String trBody = "";
            JSONArray array = json.getJSONObject("aggregations").getJSONObject("2").getJSONArray("buckets");
            int length = array.size();
            for (int i=0; i<length; i++) {
                JSONObject item = array.getJSONObject(i);
                System.out.println(item.toJSONString());
                String db_table = item.getString("key");
                Integer total = item.getInteger("doc_count");

                JSONObject d_c_b = item.getJSONObject("3").getJSONObject("buckets");

                Integer status_lt_0_200 = d_c_b.getJSONObject("0~200ms").getInteger("doc_count");
                Integer status_lt_200_500 = d_c_b.getJSONObject("201~500ms").getInteger("doc_count");
                Integer status_lt_500_1000 = d_c_b.getJSONObject("501~1000s").getInteger("doc_count");
                Integer status_tt_1s = d_c_b.getJSONObject(">1s").getInteger("doc_count");

                float val_0_200 = (float)status_lt_0_200 / (float)(total);
                float val_200_500 = ((float)status_lt_200_500 / (float)(total));
                float val_500_1000 = ((float)status_lt_500_1000 / (float)(total));
                float val_1s = ((float)status_tt_1s / (float)(total)*100);
                String tr_body_item = String.format("<tr>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td%s>%s (%s)</td>" +
                                "<td%s>%s (%s)</td>" +
                                "<td%s>%s (%s)</td>" +
                                "<td%s>%s (%s)</td>" +
                                "</tr>",
                        db_table, total,
                        css_highlight(val_0_200, "red"), status_lt_0_200, val_0_200,
                        css_200_highlight(val_200_500, "red"), status_lt_200_500, val_200_500,
                        css_200_highlight(val_500_1000, "red"), status_lt_500_1000, val_500_1000,
                        css_200_highlight(val_1s, "red"), status_tt_1s, val_1s);
                trBody += tr_body_item;
            }
            return timeHtmlBegin + trBody + time_html_end;
        } catch (Exception e) {
            log.error("WebappTime Parser error: {}", e.getMessage());
            return timeHtmlBegin + "<tr><td>查询异常</td><tr>" + time_html_end;
        }
    }


    public static String css_highlight(float count, String color) {
        if (count < 0.95)
            return " style=\"background: " + color + "\"";
        else
            return "";
    }

    public static String css_200_highlight(float count, String color) {
        if (count >= 0.01)
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