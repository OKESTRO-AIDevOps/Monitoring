package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class Uptime {
    private Duration duration;

    @Getter
    public class Duration {
        private long ms;
    }
}
