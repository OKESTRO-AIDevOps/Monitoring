package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class Memory {
    private Actual actual;
    private long free;
    @Field(name = "hugepages")
    private HugePages hugePages;
    @Field(name = "page_stats")
    private PageStats pageStats;
    private Swap swap;
    private long total;
    private Used2 used;

    @Getter
    public class Actual {
        private long free;
        private Used2 used;
    }

    @Getter
    public class HugePages {
        @Field(name = "default_size")
        private long defaultSize;
        private long free;
        private long reserved;
        private long surplus;
        private HugePagesSwap swap;
        private long total;
        private Used1 used;

        @Getter
        public class HugePagesSwap {
            private Out out;

            @Getter
            public class Out {
                private long fallback;
                private long pages;
            }
        }

    }

    @Getter
    public class Used1 {
        private long bytes;
        private long pct;
    }

    @Getter
    public class Used2 {
        private long bytes;
        private float pct;
    }

    @Getter
    public class PageStats {
        private PageStatsInfo pgfree;
        @Field(name = "pgscan_direct")
        private PageStatsInfo pgscanDirect;
        @Field(name = "pgscan_kswapd")
        private PageStatsInfo pgscanKswapd;
        @Field(name = "pgsteal_direct")
        private PageStatsInfo pgstealDirect;
        @Field(name = "pgsteal_kswapd")
        private PageStatsInfo pgstealKswapd;

        @Getter
        public class PageStatsInfo {
            private long pages;
        }
    }

    @Getter
    public class Swap {
        private long free;
        private SwapInfo in;
        private SwapInfo out;
        private Readahead readahead;
        private long total;
        private Used1 used;


        @Getter
        public class SwapInfo {
            private long pages;
        }

     /*   @Getter
        public class Used {
            private long bytes;
            private long pct;
        }*/

        @Getter
        public class Readahead extends SwapInfo {
            private long cached;
        }
    }


}
