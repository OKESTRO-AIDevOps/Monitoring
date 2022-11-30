package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class Core {
    private long id;
    private CoreInfo1 idle;
    private CoreInfo1 iowait;
    private CoreInfo2 irq;
    private CoreInfo2 nice;
    private CoreInfo1 softirq;
    private CoreInfo2 steal;
    private CoreInfo1 system;
    private CoreInfo1 user;

    @Getter
    public class CoreInfo1 {
        private float pct;
        private long ticks;
    }

    @Getter
    public class CoreInfo2 {
        private long pct;
        private long ticks;
    }
}
