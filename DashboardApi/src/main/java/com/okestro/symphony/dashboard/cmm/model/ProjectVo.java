/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectVo {
    public ProjectVo() {};
    public ProjectVo(String projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    private String project;
    private String projectId;
    private String projectName;
    private String endpoint;
    private String domain;
    private String user;
    private String password;
}
