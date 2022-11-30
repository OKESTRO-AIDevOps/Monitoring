package com.okestro.symphony.dashboard.api.elastic.vm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class ElasticIndex {

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private static final DateTimeFormatter systemFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");


        public String getIndexDate() {
                return formatter.format(LocalDate.now());
        }

        public String getSystemIndexDate() {
                return systemFormatter.format(LocalDate.now());
        }

        @Getter
        @Setter
        @Value("${config.default.index}") // 초기값 설정
        private String index;

}
