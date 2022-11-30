/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model;

import com.okestro.symphony.dashboard.api.elastic.vm.model.system.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.lang.Process;

// 시스템메트릭 조회 테스트
@Getter
@Document(indexName = "#{@elasticIndex.getIndex()}")
// @Document(indexName = "test_beats_metric_2021_02_02")
public class VmMetric {


    @Id
    private String timestamp;
    private Host host;
    private System system;

    @Builder
    public VmMetric(String timestamp, Host host, System system) {
        this.timestamp = timestamp;
        this.host = host;
        this.system = system;
    }


    @Getter
    public class Host {
        private String id;
        private String name;
        private Os os;
        private String ip;
        private String mac;

        @Getter
        public class Os {
            private String name;
            private String version;
        }
    }

    @Getter
    public class System {
        private Core core;
        private Cpu cpu;
        @Field(name = "diskio")
        private DiskIo diskIo;
        @Field(name = "filesystem")
        private FileSystem fileSystem;
        private Fsstat fsstat;
        private Load load;
        private Memory memory;
        private Network network;
        private Process process;
        private Service service;
        private Socket socket;
        private Uptime uptime;
    }


}
