package com.antzuhl.kibana.utils;

import com.antzuhl.kibana.common.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author AntzUhl
 * @Date 11:03
 */
public class RequestUtil {

    public static String buildRequestUrl(String baseUrl, String indexName) {
        return baseUrl + indexName + "/_search";
    }

}
