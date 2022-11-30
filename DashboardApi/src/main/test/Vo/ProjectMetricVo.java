package Vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProjectMetricVo {
    private List<ProjectMetricDetailVo> projects;
    private Links links;

    @Data
    public class Links {
        private String next;
        @JsonProperty("self")
        private String self;
        private String previous;
    }
}
