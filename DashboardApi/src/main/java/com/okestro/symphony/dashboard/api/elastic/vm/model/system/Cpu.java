package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class Cpu {
    private int cores;
    private CpuInfo3 idle;
    private CpuInfo3 iowait;
    private CpuInfo2 irq;
    private CpuInfo2 nice;
    private CpuInfo3 softirq;
    private CpuInfo2 steal;
    private CpuInfo3 system;
    private CpuInfo1 total;
    private CpuInfo3 user;

    @Getter
    public class CpuInfo1 {
        private NormInfo1 norm;
        private float pct;
    }

    @Getter
    public class CpuInfo2 {
        private NormInfo2 norm;
        private long pct;
        private long ticks;
    }

    @Getter
    public class CpuInfo3 extends CpuInfo1 {
        private long ticks;
    }

    @Getter
    public class NormInfo1 {
        private float pct;
    }

    @Getter
    public class NormInfo2 {
        private long pct;
    }
}
