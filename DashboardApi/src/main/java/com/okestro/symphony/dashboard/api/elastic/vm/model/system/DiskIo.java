package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class DiskIo {
    private Io io;
    @Field(name = "iostat")
    private Iostat iostat;
    private String name;
    private DIskIoInfo read;
    private DIskIoInfo write;

    @Getter
    public class Io {
        private long time;
    }

    @Getter
    public class Iostat {
        private long await;
        private long busy;
        private Queue queue;
        private Read read;
        private IostatReqeust request;
        @Field(name = "service_time")
        private long serviceTime;
        private Write write;

        @Getter
        public class Queue {
            @Field(name = "avg_size")
            private long avgSize;
        }

        @Getter
        public class Read {
            private long await;
            @Field(name = "per_sec")
            private PerSec perSec;
            private Request request;
        }

        @Getter
        public class IostatReqeust {
            @Field(name = "avg_size")
            private long avgSize;
        }

        @Getter
        public class Write {
            private long await;
            @Field(name = "per_sec")
            private PerSec perSec;
            private Request request;
        }
    }

    @Getter
    public class DIskIoInfo {
        private long bytes;
        private long count;
        private long time;
    }

    @Getter
    public class PerSec {
        private long bytes;
    }

    @Getter
    public class Request {
        @Field(name = "merges_per_sec")
        private long mergesPerSec;

        @Field(name = "per_sec")
        private long perSec;
    }

}
