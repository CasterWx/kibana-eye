package com.antzuhl.kibana.common;

/**
 * @author AntzUhl
 * Date 2020/8/25 15:39
 */
public class Constants {

    /** Request Header Token */
    public static final String AUTH_TOKEN = "token";

    public static final String defaultTRequest = "{\n" +
            "  \"size\": 100,\n" +
            "  \"query\": \n" +
            "  {\n" +
            "    \"bool\":  \n" +
            "    {\n" +
            "      \"should\": [\n" +
            "                  { \"match\": { \"content\": \"user_mobile_verify_error_five\" }},\n" +
            "                  { \"match\": { \"content\": \"user_mobile_verify_error_three\"   }}\n" +
            "       ],\n" +
            "      \"filter\": {\n" +
            "        \"bool\": {\n" +
            "          \"must\": [\n" +
            "            {\n" +
            "              \"range\": {\n" +
            "                \"@timestamp\": {\n" +
            "                  \"gte\": \"${gte}\",\n" +
            "                  \"lte\": \"${lte}\",\n" +
            "                  \"format\": \"epoch_millis\"\n" +
            "                }\n" +
            "              }\n" +
            "            }\n" +
            "          ],\n" +
            "          \"must_not\": [\n" +
            "\n" +
            "          ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "    \n" +
            "  }\n" +
            "}\n";


    public static final String BASE_URL = "http://{你的kibana服务地址}/api/console/proxy?";
}
