/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.engine;

import com.okestro.symphony.dashboard.cmm.model.ProjectVo;
import com.okestro.symphony.dashboard.cmm.model.SimpleProjectVo;
//import com.okestro.symphony.dashboard.cmm.repo.AdminRepository;
//import com.okestro.symphony.dashboard.cmm.repo.OrgProjectRepository;
//import com.okestro.symphony.dashboard.cmm.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.vault.core.VaultTemplate;
//import org.springframework.vault.support.VaultResponse;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OpenStackConnectionService {
    private OSClient.OSClientV3 osClient;

//    @Autowired
//    OrgProjectRepository orgProjectRepository;

//    @Autowired
//    ProjectRepository projectRepository;

//    @Autowired
//    UserRepository userRepository;

//    @Autowired
//    AdminRepository adminRepository;

//    @Autowired
//    private VaultTemplate vaultTemplate;

//    @Value("${spring.cloud.vault.kv.application-name}")
    private String appName;


    public List<SimpleProjectVo> getSimpleProjectList(List<ProjectVo> projectList) {
        List<SimpleProjectVo> list = new ArrayList<>();

        if(projectList.size() > 0) {
            for(ProjectVo project : projectList) {
                // appName 추가시 변경 - appName.split(",")[0]
//                VaultResponse vaultResponse = vaultTemplate.read(OpenStackConstant.PREFIX_VAULT + appName.split(",")[0] + File.separator + project.getProjectId());
//
//                if (vaultResponse != null) {
//                    // 상단 메뉴용
//                    SimpleProjectVo item = new SimpleProjectVo();
//                    item.setName(project.getProjectName());
//                    item.setValue(project.getProjectName());
//                    list.add(item);
//                }
            }
        }

        return list;
    }

    /**
     * v3 authentication
     * unscoped authentication
     */
    public OSClient.OSClientV3 connectUnscoped(String endpoint, String domain, String user, String password) throws ConnectException {

        Identifier domainIdentifier = Identifier.byName(domain);

        osClient = OSFactory.builderV3()
//                .withConfig(Config.newConfig().withSSLVerificationDisabled())
                .withConfig(Config.newConfig().withEndpointNATResolution("100.0.0.189"))
                .endpoint(endpoint)
                .credentials(user, password, domainIdentifier)
                .authenticate();


        return osClient;

    }

    /**
     * v3 authentication
     * project scope authentication
     */
    public OSClient.OSClientV3 connect(String endpoint, String domain, String project, String user, String password) throws ConnectException {
        //
        Identifier domainIdentifier = Identifier.byName(domain);
        Identifier projectIdentifier = Identifier.byName(project);

        // check https
        if (endpoint.startsWith("https://")) {
            osClient = OSFactory.builderV3()
//					.withConfig(Config.newConfig().withSSLContext(UntrustedSSL.getSSLContext()))
                    .withConfig(Config.newConfig().withEndpointNATResolution("100.0.0.189"))
                    .withConfig(Config.newConfig().withSSLVerificationDisabled())
                    .endpoint(endpoint)
                    .credentials(user, password, domainIdentifier)
                    .scopeToProject(projectIdentifier, domainIdentifier)
                    .authenticate();
        } else {
            osClient = OSFactory.builderV3()
                    .endpoint(endpoint)
                    .withConfig(Config.newConfig().withEndpointNATResolution("100.0.0.189"))
                    .credentials(user, password, domainIdentifier)
                    .scopeToProject(projectIdentifier, domainIdentifier)
                    .authenticate();
        }


        return osClient;
    }

    /**
     * v3 authentication
     * project scope authentication
     */
    public OSClient.OSClientV3 connect(String projectName, String userId) throws ConnectException {
//        ProjectVo project = getProject(projectName, userId); //2021-03-18 임시조치 getProject는 사용안하기때메.
        ProjectVo project = null;


        Identifier domainIdentifier = Identifier.byName(project.getDomain());
        Identifier projectIdentifier = Identifier.byName(project.getProject());
        // check https
        if (project.getEndpoint().startsWith("https://")) {
            osClient = OSFactory.builderV3()
//					.withConfig(Config.newConfig().withSSLContext(UntrustedSSL.getSSLContext()))
                    .withConfig(Config.newConfig().withSSLVerificationDisabled())
                    .endpoint(project.getEndpoint())
                    .credentials(project.getUser(), project.getPassword(), domainIdentifier)
                    .scopeToProject(projectIdentifier, domainIdentifier)
                    .authenticate();
        } else {
            osClient = OSFactory.builderV3()
                    .endpoint(project.getEndpoint())
                    .credentials(project.getUser(), project.getPassword(), domainIdentifier)
                    .scopeToProject(projectIdentifier, domainIdentifier)
                    .authenticate();
        }

        return osClient;
    }

    /**
     * v3 authentication
     * project scope authentication
     */
    public OSClient.OSClientV3 connect(ProjectVo project) throws ConnectException {


        Identifier domainIdentifier = Identifier.byName((project.getDomain() == null) ? "Default": project.getDomain());
        log.debug("## Y_TEST connect Method 진입. project.getDomain() 값["+project.getDomain()+"]");
        log.debug("## Y_TEST connect Method 진입. Identifier.byName() 값["+((project.getDomain() == null) ? "Default": project.getDomain())+"]");
        log.debug("## Y_TEST connect Method 진입. project.getUser() 값["+project.getUser()+"]");
        log.debug("## Y_TEST connect Method 진입. project.getPassword() 값["+project.getPassword()+"]");



//        Identifier domainIdentifier = Identifier.byName("Default");
        Identifier projectIdentifier = Identifier.byName(project.getProject());
        log.debug("## Y_TEST connect Method 진입. project Name is ["+project.getProject()+"]");


//        Identifier projectIdentifier = Identifier.byName("k8s-test");


        // check https
        if (project.getEndpoint().startsWith("https://")) {
            osClient = OSFactory.builderV3()
//					.withConfig(Config.newConfig().withSSLContext(UntrustedSSL.getSSLContext()))
                    .withConfig(Config.newConfig().withSSLVerificationDisabled())
                    .endpoint(project.getEndpoint())
                    .credentials(project.getUser(), project.getPassword(), domainIdentifier)
                    .scopeToProject(projectIdentifier, domainIdentifier)
                    .authenticate();
        } else {
            osClient = OSFactory.builderV3()
                    .withConfig(Config.newConfig().withSSLVerificationDisabled())
                    .endpoint(project.getEndpoint())
                    .credentials(project.getUser(), project.getPassword(), domainIdentifier)
                    .scopeToProject(projectIdentifier, domainIdentifier)
                    .authenticate();
        }

        return osClient;
    }




    public ProjectVo getProject(String projectName, List<ProjectVo> projectList) {
        ProjectVo project = new ProjectVo();

        for(ProjectVo item : projectList) {
            if(projectName.equals(item.getProjectName())) {
                project = item;

                // appName 추가시 변경 - appName.split(",")[0]
//                VaultResponse vaultResponse = vaultTemplate.read(OpenStackConstant.PREFIX_VAULT + appName.split(",")[0] + File.separator + item.getProjectId());
//
//                if (vaultResponse != null) {
//                    // 프로젝트 접속 정보 저장용
//                    project.setProject((String) ((Map) vaultResponse.getData().get(OpenStackConstant.DATA)).get(OpenStackConstant.PROJECT));
//                    project.setEndpoint((String) ((Map) vaultResponse.getData().get(OpenStackConstant.DATA)).get(OpenStackConstant.ENDPOINT));
//                    project.setDomain((String) ((Map) vaultResponse.getData().get(OpenStackConstant.DATA)).get(OpenStackConstant.DOMAIN));
//                    project.setUser((String) ((Map) vaultResponse.getData().get(OpenStackConstant.DATA)).get(OpenStackConstant.USERNAME));
//                    project.setPassword((String) ((Map) vaultResponse.getData().get(OpenStackConstant.DATA)).get(OpenStackConstant.PASSWORD));
//                }
            }
        }

        return project;
    }

    /**
     * close
     */
    public void close() {
        //@ ignore
    }
}
