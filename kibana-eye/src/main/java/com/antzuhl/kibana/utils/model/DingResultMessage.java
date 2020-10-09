package com.antzuhl.kibana.utils.model;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingResultMessage {

    private Integer errCode;

    private String errMessage;

    public Boolean isSuccess() {
        return errCode == 0;
    }

    public DingResultMessage valid() {
        if (errCode == null || errMessage == null || StringUtils.isEmpty(errMessage)) {
            return DingResultMessage.builder()
                    .errCode(CustomError.ERROR_UNKNOWN.getErrCode())
                    .errMessage(CustomError.ERROR_UNKNOWN.getErrMessage())
                    .build();
        }
        return this;
    }

    public static DingResultMessage createEmptyResult() {
        return DingResultMessage.builder()
                .errCode(CustomError.ERROR_CONTENT_EMPTY.getErrCode())
                .errMessage(CustomError.ERROR_CONTENT_EMPTY.getErrMessage())
                .build();
    }

    enum CustomError {
        ERROR_CONTENT_EMPTY(-1, "Content is empty."),
        ERROR_UNKNOWN(-2, "Unknown error.");

        int errCode;
        String errMessage;

        CustomError(int errCode, String errMessage) {
            this.errCode = errCode;
            this.errMessage = errMessage;
        }

        public int getErrCode() {
            return errCode;
        }

        public String getErrMessage() {
            return errMessage;
        }
    }
}
