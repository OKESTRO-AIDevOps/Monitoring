/*
 * Developed by kbcho@okestro.com on 2020-11-30
 * Last modified 2020-11-30 16:46:53
 */

package com.okestro.symphony.dashboard.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
//@Configuration
//@ConfigurationProperties(prefix = "cloudplatform.keycloak")
public class KeycloakProperties {
    private boolean isUseAt;
    private String adminCliId;
    private String adminCliPw;
    private String clientId;
    private String clientSecret;
    private String realm;
    private String logoutUrl;
    private String logoutCallbackUrl;
    private String loginCallbackUrl;
    private String loginRedirectUrl;
    private String tokenCallbackUrl;
    private String url;
    private String oauthAuthorizeUrl;
    private String oauthTokenUrl;
    public void setRealm(String realm){
        this.realm = realm;
        this.oauthAuthorizeUrl =  "/auth/realms/"+realm+"/protocol/openid-connect/auth";
        this.oauthTokenUrl =  "/auth/realms/"+realm+"/protocol/openid-connect/token";
        this.logoutUrl = "/auth/realms/"+realm+"/protocol/openid-connect/logout";

    }
}
