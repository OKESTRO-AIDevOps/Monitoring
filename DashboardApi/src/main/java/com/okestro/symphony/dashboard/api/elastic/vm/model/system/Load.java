package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class Load {
    @Field(name = "1m")
    private long one;
    @Field(name = "5m")
    private long five;
    @Field(name = "15m")
    private long fifteen;
    private long cores;
    private Norm norm;

    @Getter
    public class Norm {
        @Field(name = "1m")
        private long one;
        @Field(name = "5m")
        private long five;
        @Field(name = "15m")
        private long fifteen;
    }
}
