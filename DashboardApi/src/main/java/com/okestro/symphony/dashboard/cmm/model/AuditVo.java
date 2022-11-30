/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuditVo {
    private String se;
    private String prjctNm;
    private String tenNm;
    private String cn;
    private String userId;
    private long creatDt;

    public AuditVo(String se, String prjctNm, String tenNm, String cn, String userId){
        this.se = se;
        this.prjctNm = prjctNm;
        this.tenNm = tenNm;
        this.cn = cn;
        this.userId = userId;
    }
    public AuditVo(String se, String prjctNm, String tenNm, String cn, String userId, long creatDt){
        this.se = se;
        this.prjctNm = prjctNm;
        this.tenNm = tenNm;
        this.cn = cn;
        this.userId = userId;
        this.creatDt = creatDt;
    }
}
