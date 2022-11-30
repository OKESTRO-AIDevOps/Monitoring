/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.model;



import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TADP_USER", catalog = "ECP_ADMIN")
public class AdminEntity {

        public AdminEntity() {}
        public AdminEntity(String userId,
                           String pw,
                           String bfPw,
                           String salt,
                           String bfSalt,
                           String userSeCd,
                           String gpkiDn,
                           char selfauthAt,
                           String name,
                           String brthdy,
                           String email,
                           String orgCd,
                           String orgNm,
                           String orgList,
                           String deptNm,
                           String telnoSeCd,
                           String telno,
                           String entrpsNm,
                           char hmpgTosAgreAt,
                           char prscolctUseAgreAt,
                           String joinApproStt,
                           long creatDt,
                           long crtrNo,
                           long updDt,
                           long updmanNo,
                           long roleNo) {
            this.userId = userId;
            this.pw = pw;
            this.bfPw = bfPw;
            this.salt = salt;
            this.bfSalt = bfSalt;
            this.userSeCd = userSeCd;
            this.gpkiDn = gpkiDn;
            this.selfauthAt = selfauthAt;
            this.name = name;
            this.brthdy = brthdy;
            this.email = email;
            this.orgCd = orgCd;
            this.orgNm = orgNm;
            this.orgList = orgList;
            this.deptNm = deptNm;
            this.telnoSeCd = telnoSeCd;
            this.telno = telno;
            this.entrpsNm = entrpsNm;
            this.hmpgTosAgreAt = hmpgTosAgreAt;
            this.prscolctUseAgreAt = prscolctUseAgreAt;
            this.joinApproStt = joinApproStt;
            this.creatDt = creatDt;
            this.crtrNo = crtrNo;
            this.updDt = updDt;
            this.updmanNo = updmanNo;
            this.roleNo = roleNo;
        }

        @Id
        @Column(name="USER_NO")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long userNo;

        @Column
        private String userId;

        @Column
        private String pw;

        @Column
        private String bfPw;

        @Column
        private String salt;

        @Column
        private String bfSalt;

        @Column
        private String userSeCd;

        @Column
        private String gpkiDn;

        @Column
        private char selfauthAt;

        @Column
        private String name;

        @Column
        private String brthdy;

        @Column
        private String email;

        @Column
        private String orgCd;

        @Column
        private String orgNm;

        @Column
        private String deptNm;

        @Column
        private String telnoSeCd;

        @Column
        private String telno;

        @Column
        private String entrpsNm;

        @Column
        private char hmpgTosAgreAt;

        @Column
        private char prscolctUseAgreAt;

        @Column
        private String joinApproStt;

        @Column
        private long creatDt;

        @Column
        private long crtrNo;

        @Column
        private long updDt;

        @Column
        private long updmanNo;

        @Column
        private char pwInitAt;

        @Transient
        protected long roleNo;

        @Column
        private String ssoKey;

        @Column
        private String orgList;
    }
