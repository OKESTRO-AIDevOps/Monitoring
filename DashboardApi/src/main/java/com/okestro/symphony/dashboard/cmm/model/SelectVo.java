/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectVo<T> {
    private String name;
    private String value;
    private T data;
}
