/*
 * Developed by bhhan@okestro.com on 2020-07-09
 * Last modified 2020-07-09 17:50:34
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.image.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ImageVo extends CommonVo {
	private String id;
	private String name;
	private String projectNm;
	private String status;
	private String containerFormat;
	private String diskFormat;
	private Date createdAt;
	private Date updatedAt;
	private long minDisk;
	private long minRam;
	private boolean isProtected;
	private String checksum;
	private String owner;
	private String ownerName;
	private String visibility;
	private long size;
	private String self;
//	private String file;
	private String schema;
	private String architecture;
	private String instanceUuid;
	private String kernelId;
	private String ramdiskId;
	private com.okestro.symphony.dashboard.api.openstack.nova.compute.image.model.AdditionalPropertiesVo additionalProperties;

	private String attachedFileType;
	private String attachedFileValue;
	private String isProtectedToString;

	public static enum AttachedFileType {
		URL,
		PATH,
		UPLOAD;

		private AttachedFileType() {
		}

		@JsonCreator
		public static AttachedFileType forValue(String value) {
			if (value != null) {
				AttachedFileType[] var1 = values();
				int var2 = var1.length;

				for(int var3 = 0; var3 < var2; ++var3) {
					AttachedFileType s = var1[var3];
					if (s.name().equalsIgnoreCase(value)) {
						return s;
					}
				}
			}

			return null;
		}

		@JsonValue
		public String value() {
			return this.name().toLowerCase();
		}
	}
}
