package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class FileSystem {
    private long available;
    @Field(name = "device_name")
    private String deviceName;
    private long files;
    private long free;
    @Field(name = "free_files")
    private long freeFiles;
    @Field(name = "mount_point")
    private String mountPoint;
    private long total;
    private String type;
    private Used used;

    @Getter
    public class Used {
        private long bytes;
        private long pct;
    }
}
