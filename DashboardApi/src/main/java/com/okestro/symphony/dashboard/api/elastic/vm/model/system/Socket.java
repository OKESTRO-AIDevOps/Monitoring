package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;

public class Socket {
    private SockInfo local;
    private Process process;
    private SockInfo remote;
    private Summary summary;

    @Getter
    public class SockInfo {
        private String ip;
        private long port;
    }

    @Getter
    public class Process {
        private String cmdline;
    }

    @Getter
    public class Summary {
        private SummaryAll all;
        private Tcp tcp;
        private Udp udp;

        @Getter
        public class SummaryAll {
            private long count;
            private long listening;
        }

        @Getter
        public class Tcp {
            private TcpAll all;
            private long memory;

            @Getter
            public class TcpAll {
                @Field(name = "close_wait")
                private long closeWait;
                private long count;
                private long established;
                private long listening;
                private long orphan;
                @Field(name = "time_wait")
                private long timeWait;
            }
        }

        @Getter
        public class Udp {
            private UdpAll all;
            private long memory;

            @Getter
            public class UdpAll {
                private long count;
            }

        }
    }

}
