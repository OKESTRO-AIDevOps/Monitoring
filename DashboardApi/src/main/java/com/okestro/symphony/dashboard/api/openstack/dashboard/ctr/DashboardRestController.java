//package com.okestro.symphony.dashboard.api.openstack.dashboard.ctr;
//
//import com.okestro.symphony.dashboard.api.openstack.dashboard.model.*;
//import com.okestro.symphony.dashboard.api.openstack.dashboard.svc.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin("*")
////@Api(value = "dashboardSummery", description = "[TEST]Dashboard 화면 호출 관련 데이터 리스트")
//@RequestMapping("/dashboard")
//@RestController
//public class DashboardRestController {
//	@Autowired
//	DashboardService dashboardService;
//
//
//	/**
//	 * openstack API 통해서 Summary 데이터 호
//	 * @return SummaryVo
//	 */
////	@GetMapping("/info")
//	public SummaryVo retrieveSummary() {
//		return dashboardService.retrieveSummary();
//	}
//
//
//	/**
//	 * Symphony MariaDB에서 projects 조회
//	 * @return VProjectLastedVo
//	 */
////	@GetMapping("/projects")
//	public List<VProjectLastedVo> retrieveLastedProjects() {
//		return dashboardService.retrieveLastedProject();
//	}
//
//	/**
//	 * Symphony MariaDB에서 projectQuotas 조회
//	 * @return VProjectQuotaLastedVo
//	 */
////	@GetMapping("/projectQuotas")
//	public List<VProjectQuotaLastedVo> retrieveLastedProjectQuota() {
//		return dashboardService.retrieveLastedProjectQuota();
//	}
//
//	/**
//	 * Symphony MariaDB에서 hosts 조회
//	 * @return VHostLastedVo
//	 */
////	@GetMapping("/hosts")
//	public List<VHostLastedVo> retrieveLastedHosts() {
//		return dashboardService.retrieveLastedHosts();
//	}
//
//	/**
//	 * Symphony MariaDB에서 hostAggregates 조회
//	 * @return VHostAggregateLastedVo
//	 */
////	@GetMapping("/hostAggregates")
//	public List<VHostAggregateLastedVo> retrieveLastedHostAggregates() {
//		return dashboardService.retrieveLastedHostsAggregates();
//	}
//
//	/**
//	 * Symphony MariaDB에서 hostAggregateHost 조회
//	 * @return VHostAggregateHostLastedVo
//	 */
////	@GetMapping("/hostAggregateHost")
//	public List<VHostAggregateHostLastedVo> retrieveLastedHostAggregateHosts() {
//		return dashboardService.retrieveLastedHostsAggregatesHosts();
//	}
//
//	/**
//	 * Symphony MariaDB에서 Vms 조회
//	 * @return VVmLastedVo
//	 */
////	@GetMapping("/vms")
//	public List<VVmLastedVo> retrieveLastedVms() {
//		return dashboardService.retrieveLastedVms();
//	}
//
//
//
//}