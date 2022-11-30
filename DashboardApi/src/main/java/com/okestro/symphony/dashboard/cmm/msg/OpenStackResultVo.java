/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.msg;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class OpenStackResultVo<T> {
    private boolean result;
    private String resultCode;
    private String resultMessage;
    private T viewList;
}
