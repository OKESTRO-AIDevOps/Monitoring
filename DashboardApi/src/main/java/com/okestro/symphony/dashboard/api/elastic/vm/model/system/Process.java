package com.okestro.symphony.dashboard.api.elastic.vm.model.system;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;

@Component
public class Process {
    private Cgroup cgroup;
    private String cmdline;
    private ProcessCpu cpu;
    private Fd fd;
    private ProcessMemory memory;
    private String state;
    private Summary summary;

    @Getter
    public class Cgroup {
        private Blkio blkio;
        private CgroupCpu cpu;
        private Cpuacct cpuacct;
        private String id;
        private Memory memory;
        private String path;

        @Getter
        public class Blkio {
            private String id;
            private String path;
            private CgroupTotal total;

            @Getter
            public class CgroupTotal {
                private long bytes;
                private long ios;
            }
        }

        @Getter
        public class CgroupCpu {
            private Cfs cfs;
            private String id;
            private String path;
            private Rt rt;
            private CgroupCpuStats stats;

            @Getter
            public class Cfs {
                private CpuInfo period;
                private CpuInfo quota;
                private long shares;
            }

            @Getter
            public class Rt {
                private CpuInfo period;
                private CpuInfo runtime;
            }

            @Getter
            public class CgroupCpuStats {
                private long periods;
                private Throttled throttled;

                @Getter
                public class Throttled {
                    private long ns;
                    private long periods;
                }
            }

            @Getter
            public class CpuInfo {
                private long us;
            }
        }

        @Getter
        public class Cpuacct {
            private String id;
            private String path;
            private CpuacctPercpu percpu;
            private CpuacctStats stats;
            private CpuacctInfo total;


            @Getter
            public class CpuacctPercpu {

                private long c01;
                private long c02;
            }

            @Getter
            public class CpuacctStats {
                private CpuacctInfo system;
                private CpuacctInfo user;
            }

            @Getter
            public class CpuacctInfo {
                private long ns;
            }
        }

        @Getter
        public class Memory {
            private String id;
            private MemoryInfo kmem;
            @Field(name = "kmem_tcp")
            private MemoryInfo kmemTcp;
            private MemoryInfo mem;
            private MemoryInfo memsw;
            private String path;
            private MemoryStats stats;


            @Getter
            public class MemoryInfo {
                private long failures;
                private Limit limit;
                private Usage usage;

                @Getter
                public class Limit {
                    private long bytes;
                }

                @Getter
                public class Usage {
                    private long bytes;
                    private Max max;

                    @Getter
                    public class Max {
                        private long bytes;
                    }
                }
            }

            @Getter
            public class MemoryStats {
                @Field(name = "active_anon")
                private StatsInfo activeAnon;
                @Field(name = "active_file")
                private StatsInfo activeFile;
                private StatsInfo cache;
                @Field(name = "hierarchical_memory_limit")
                private StatsInfo hierarchicalMemoryLimit;
                @Field(name = "hierarchical_memsw_limit")
                private StatsInfo hierarchicalMemswLimit;
                @Field(name = "inactive_anon")
                private StatsInfo inactiveAnon;
                @Field(name = "inactive_file")
                private StatsInfo inactiveFile;
                @Field(name = "major_page_faults")
                private long majorPageFaults;
                @Field(name = "mapped_file")
                private StatsInfo mappedFile;
                @Field(name = "page_faults")
                private long pageFaults;
                @Field(name = "pages_in")
                private long pagesIn;
                @Field(name = "pages_out")
                private long pagesOut;
                private StatsInfo rss;
                @Field(name = "rss_huge")
                private StatsInfo rssHuge;
                private StatsInfo swap;
                private StatsInfo unevictable;

                @Getter
                public class StatsInfo {
                    private long bytes;
                }
            }

        }
    }

    @Getter
    public class ProcessCpu {
        @Field(name = "start_time")
        private String startTime;
        private Total total;

        @Getter
        public class Total {
            private Norm norm;
            private float pct;
            private float value;

            @Getter
            public class Norm {
                private float pct;
            }
        }
    }

    @Getter
    public class Fd {
        private Limit limit;
        private long open;

        @Getter
        public class Limit {
            private long hard;
            private long soft;
        }
    }

    @Getter
    public class ProcessMemory {
        private Rss rss;
        private long share;
        private long size;

        @Getter
        public class Rss {
            private long bytes;
            private float pct;
        }
    }

    @Getter
    public class Summary {
        private long dead;
        private long idle;
        private long running;
        private long sleeping;
        private long stopped;
        private long total;
        private long unknown;
        private long zombie;
    }
}
