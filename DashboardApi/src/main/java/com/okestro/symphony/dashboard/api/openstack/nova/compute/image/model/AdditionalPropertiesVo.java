/*
 * Developed by bhhan@okestro.com on 2020-07-10
 * Last modified 2020-07-10 17:08:54
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.image.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalPropertiesVo {
	// 기본 속성
	private String osHidden;
	private String osHashAlgo;
	private String osHashValue;

	// 스냅샷일 경우 추가 속성
	private String ownerUserName;
	private String baseImageRef;
	private String bootRoles;
	private String userId;
	private String ownerId;
	private String imageLocation;
	private String imageState;
	private String ownerProjectName;
	private String imageType;

	// 설명 추가시 생성되는 속성
	private String description;
}
