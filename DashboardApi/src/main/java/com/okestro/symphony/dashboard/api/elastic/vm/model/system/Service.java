package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class Service {
    @Field(name = "exec_code")
    private String execCode;
    @Field(name = "load_state")
    private String loadState;
    private String name;
    private Resources resources;
    private String state;
    @Field(name = "state_since")
    private String stateSince;
    @Field(name = "sub_state")
    private String subState;

    @Getter
    public class Resources {
        private Tasks tasks;

        @Getter
        public class Tasks {
            private long count;
        }
    }
}
