package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openstack4j.openstack.common.Metadata;

import java.util.List;

@Getter
@Setter
@ToString
public class ImageMetricVo {
    private List<Image> images;
    private Image image;

    @Getter
    @Setter
    @ToString
    public static class Image {
        private String id;
        private String name;
        private double minRam;
        private double minDisk;
        private Metadata metadata;              // openstack4j에 정의되어있는 Vo쓴거라서 에러나면 직접만들어서사용.
        private String created;
        private String updated;
        private String status;
        private double progress;
        @JsonProperty("OS-EXT-IMG-SIZE:size")
        private double size;
        private List<Link> links;
    }

        @Getter
        @Setter
        @ToString
        public static class Link {
            private String rel;
            private String type;

            @JsonProperty("href")
            private String href;
        }
}
