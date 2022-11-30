package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class Fsstat {
    private long count;
    @Field(name = "total_files")
    private long totalFiles;
    @Field(name = "total_size")
    private TotalSize totalSize;

    @Getter
    public class TotalSize {
        private long free;
        private long total;
        private long used;
    }


}
