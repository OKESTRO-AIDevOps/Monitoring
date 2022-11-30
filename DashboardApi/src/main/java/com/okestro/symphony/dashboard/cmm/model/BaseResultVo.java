/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResultVo {
    private String resultCode;
    private String resultMessage;
    private String idToken;
    private String targetName;
    private String targetType;
    private String extraTxt;
}
