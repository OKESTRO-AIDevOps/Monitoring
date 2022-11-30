/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.model;

import lombok.Data;

@Data
// 상속 체계 안에 resultMessage field가 중복되어 json 파싱이 정상적으로 작동하지 않는 문제가 있었음. OctaviaServiceImpl 112 jd.eom 21.02.15
//public class CommonVo extends BaseResultVo {
public class CommonVo  {
    private String projectName;
    private String userId;
}
