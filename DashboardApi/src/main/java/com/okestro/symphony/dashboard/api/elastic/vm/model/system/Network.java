package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class Network {
    private NetworkInfo in;
    private String name;
    private NetworkInfo out;

    @Getter
    public class NetworkInfo {
        private long bytes;
        private long dropped;
        private long errors;
        private long packets;
    }
}
