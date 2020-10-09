package com.antzuhl.kibana.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author AntzUhl
 * @Date 18:54
 */
public class RPCQueryParser {

    public static String css_highlight(int count, String color) {
        if (count > 10)
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

    public static String parser(String applicationName, String result) {
        result = result.replaceAll("&gt;", ">");
        result = result.replaceAll("&lt;", "<");
        String timeHtmlBegin = "<!DOCTYPE html>\n" +
                "    <html>\n" +
                "      <head>\n" +
                "      <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/>\n" +
                "      <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" + html_style() + "</head>\n" +
                "      <body>\n" +
                "        <table>\n" +
                "<tr><td colspan=\"7\" style=\"background:#92D14F\">&nbsp;应用名称: "+ applicationName +" </td></tr>" +
                "          <tr>\n" +
                "            <th style=\"width: 20%\">类名(次数)</th>\n" +
                "            <th style=\"width: 10%\">方法</th>\n" +
                "            <th style=\"width: 10%\">次数</th>\n" +
                "          </tr>";
        String time_html_end = "<tr><td colspan=\"7\" style=\"background:#92D14F\">&nbsp;超时次数>10飘红   总次数: ${sum_count} </td></tr></table></body></html>";
        try {
            JSONObject json = JSON.parseObject(result);
            String trBody = "";
            JSONArray array = json.getJSONObject("aggregations").getJSONObject("2").getJSONArray("buckets");
            int length = array.size();
            for (int i=0; i<length; i++) {
                JSONObject item = array.getJSONObject(i);
                Integer total = item.getInteger("doc_count");
                time_html_end = time_html_end.replace("${sum_count}", " "+total);
                String appName = item.getString("key");
                trBody += "<tr><td colspan=\"7\" style=\"background:#F1F8FF\">"+ appName +"</td></tr>";
                JSONArray className = item.getJSONObject("3").getJSONArray("buckets");
                int buck_length = className.size();
                for (int j = 0; j < buck_length; j++) {
                    JSONObject classItem = className.getJSONObject(j);
                    // 处理每一个类中
                    Integer count = classItem.getInteger("doc_count");
                    String class_name = classItem.getString("key");
                    JSONArray func = classItem.getJSONObject("4").getJSONArray("buckets");
                    int func_count = func.size();
                    for (int k = 0; k < func_count; k++) {
                        JSONObject func_item = func.getJSONObject(k);
                        String func_name = func_item.getString("key");
                        int func_rpc_count = func_item.getInteger("doc_count");
                        trBody += "<tr><td>" + class_name + "  ("+ count +")"  + "</td>  <td>"+ func_name +"</td> <td" + css_highlight(func_rpc_count, "orangered") + ">"+ func_rpc_count +"</td> </tr>";
                    }
                }
            }
            return timeHtmlBegin + trBody + time_html_end;
        } catch (Exception e) {
            return timeHtmlBegin + "<tr><td>查询异常</td><tr>" + time_html_end;
        }
    }


}
