/*
 * Developed by bhhan@okestro.com on 2020-08-31
 * Last modified 2020-08-31 18:10:06
 */

package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.HostVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaProjectVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SummaryVo {

	private String providerId;
	private String providerName;

	private String hostId;
	private String hostName;

	private int vcpus;
	private int vcpusUsed;
	private int lswVcpusUsed;			// 지난주 cpu 사용량

	private int instances;
	private int instancesUsed;
	private int lswInstancesUsed;		// 지난주 instance 사용량

	private int ram;
	private int ramUsed;
	private int lswRamUsed;				// 지난주 ram 사용량

	private int ips;
	private int ipsUsed;
	private int volumes;
	private int volumesUsed;
	private int gigabytes;
	private int gigabytesUsed;


	private List<ProjectMainQuota> projectMainQuotaList;

	private List<HostVo> rctHostVoList;
	private List<HostVo> lastHostVoList;

	private List<QuotaProjectVo> rctProjectQuotaList;
	private List<QuotaProjectVo> lastProjectQuotaList;


	@Data
	public static class ProjectMainQuota {
		private String projectId;
		private String projectName;

		private double cpu;
		private double usageCpu;
		private double ram;
		private double usageRam;
		private double disk;
		private double usageDisk;
		private int instance;
		private int instanceUsage;

	}
//	private int instances;
//	private int instancesUsed;
//	private int vcpus;
//	private int vcpusUsed;
//	private int ram;
//	private int ramUsed;
//	private int securityGroups;
//	private int securityGroupsUsed;
//	private int securityGroupRules;
//	private int securityGroupRulesUsed;
//	private int ips;
//	private int ipsUsed;
//	private int volumes;
//	private int volumesUsed;
//	private int gigabytes;
//	private int gigabytesUsed;
}