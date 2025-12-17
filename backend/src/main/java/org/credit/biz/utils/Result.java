package org.credit.biz.utils;

import lombok.Data;

@Data
public class Result {
    private String msg;     /* 返回信息 */
    private Integer code;   /* HTTP 状态码 */
    private Object data;    /* 返回数据, json格式 */
}
