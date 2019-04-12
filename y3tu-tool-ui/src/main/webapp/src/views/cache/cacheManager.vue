<template>
    <div>
        <el-container>

            <el-header>
                <el-card>
                    <el-row>
                        <el-col :span="6">
                            <el-autocomplete
                                    v-model="cacheName"
                                    placeholder="请输入缓存名称"
                                    :fetch-suggestions="getCacheName"
                                    @select="handleSelect"
                                    style="width: 300px"
                                    clearable>
                            </el-autocomplete>
                        </el-col>
                        <el-col :span="4">
                            <el-button type="primary" icon="el-icon-search" @click="queryCacheInfo">搜索</el-button>
                        </el-col>

                        <el-col :span="4">
                            <el-button type="primary" icon="el-icon-search" @click="test">测试</el-button>
                        </el-col>
                    </el-row>
                </el-card>
            </el-header>

            <el-main>

                <el-card class="box-card">
                    <el-table :data="tables.slice((page.pageCurrent-1)*page.pageSize,page.pageCurrent*page.pageSize)"
                              size="mini"
                              stripe
                              style="width: 100%;"
                              @selection-change="handleSelectionChange">
                        <el-table-column
                                label="名称"
                                prop="cacheName">
                        </el-table-column>

                        <el-table-column
                                label="内部缓存名称"
                                prop="internalKey">
                        </el-table-column>

                        <el-table-column
                                label="描述"
                                prop="depict">
                        </el-table-column>

                        <el-table-column
                                label="总请求数"
                                prop="requestCount">
                        </el-table-column>

                        <el-table-column
                                label="未命中总数"
                                prop="missCount">
                        </el-table-column>

                        <el-table-column
                                label="命中率"
                                prop="hitRate">
                        </el-table-column>

                        <el-table-column
                                label="一级缓存命中率"
                                prop="cacheName">
                        </el-table-column>

                        <el-table-column
                                label="二级缓存命中率"
                                prop="cacheName">
                        </el-table-column>

                        <el-table-column
                                label="加载缓存平均耗时"
                                prop="cacheName">
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-main>

            <el-footer>
                <el-card>
                    <page-footer
                            :current="page.pageCurrent"
                            :size="page.pageSize"
                            :total="page.pageTotal"
                            @change="handlePaginationChange">
                    </page-footer>
                </el-card>
            </el-footer>

        </el-container>
    </div>
</template>

<script>

    import {getCacheList, getCacheName, testPut} from "@/api/cache";

    export default {
        name: "cacheManager",
        data() {
            return {
                multipleSelection: [],
                cacheName: "",
                cacheNameArr: [],
                tableData: [],
                page: {
                    pageCurrent: 1,
                    pageSize: 10,
                    pageTotal: 0
                }
            }
        },
        components: {
            'PageFooter': () => import('@/components/PageFooter')
        },
        mounted() {
        },
        computed: {
            tables() {
                return this.tableData;
            }
        },
        methods: {
            getCacheName: function (queryString, cb) {
                getCacheName().then(res => {
                    if (res.status === 'SUCCESS') {
                        let cbs = [];
                        for (let i in res.data) {
                            let cacheName = {
                                'value': res.data[i],
                                'cacheName': res.data[i]
                            };
                            cbs.push(cacheName)
                        }
                        cb(cbs);
                    } else {
                        this.$message.error('查询缓存名称错误:' + res.message);
                    }
                });
            },
            handleSelect(item) {
                console.log(item);
            },
            queryCacheInfo: function () {
                getCacheList(this.cacheName).then(res => {
                    if (res.status === 'SUCCESS') {
                        if(res.data==null||res.data.length<1){
                            this.$message.warning("没有查询到数据!");
                        }else {
                            this.tableData = res.data;
                            this.page.pageTotal = this.tableData.length;
                        }
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handlePaginationChange(val) {
                this.page.pageCurrent = val.current;
                this.page.pageSize = val.size;
            },
            test(){
                testPut();
            }
        }
    };
</script>

<style scoped>
</style>
