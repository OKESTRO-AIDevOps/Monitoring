package Vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectMetricDetailVo {
    private String id;
    private String name;
    @JsonProperty("description")
    private String desc;
    private boolean enabled;
    @JsonProperty("parent_id")
    private String parentId;
    @JsonProperty("is_domain")
    private boolean domainId;
    private String[] tags;
//    private Options options;
    private Links links;


    @Data
    public class Links {
        @JsonProperty("self")
        private String self;

        @JsonProperty("enabled")
        private String enabled;

    }

//    @Data
//    public class Options {
//
//    }
}
