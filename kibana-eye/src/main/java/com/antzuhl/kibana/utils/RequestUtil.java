package com.antzuhl.kibana.utils;

import com.antzuhl.kibana.common.Constants;

/**
 * @author AntzUhl
 * @Date 11:03
 */
public class RequestUtil {

    public static String buildRequestUrl(String indexName) {
        return Constants.ES_QUERY_URL + indexName + "/_search";
    }

}
