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
                        <el-col :span="2">
                            <el-button type="primary" icon="el-icon-search" @click="queryCacheInfo">搜索</el-button>
                        </el-col>
                        <el-col :span="2">
                            <el-button type="warning" icon="el-icon-delete" @click="handleResetCacheStats">重置统计数据
                            </el-button>
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

                        <el-table-column type="expand">
                            <template slot-scope="props">
                                <el-collapse>
                                    <el-collapse-item title="一级缓存配置项" name="1">
                                        <div><label>缓存初始Size:</label><span>{{props.row.layerCacheSetting.firstCacheSetting.initialCapacity}}</span>
                                        </div>
                                        <div><label>缓存最大Size:</label><span>{{props.row.layerCacheSetting.firstCacheSetting.maximumSize}}</span>
                                        </div>
                                        <div><label>缓存有效时间:</label><span>{{props.row.layerCacheSetting.firstCacheSetting.expireTime}}</span>
                                        </div>
                                        <div><label>缓存时间单位:</label><span>{{props.row.layerCacheSetting.firstCacheSetting.timeUnit}}</span>
                                        </div>
                                        <div><label>缓存失效模式:</label><span>{{props.row.layerCacheSetting.firstCacheSetting.expireMode}}</span>
                                        </div>
                                        <div><label>是否允许存NULL值:</label><span>{{props.row.layerCacheSetting.firstCacheSetting.allowNullValues}}</span>
                                        </div>
                                    </el-collapse-item>
                                    <el-collapse-item title="二级缓存配置" name="2">
                                        <div><label>缓存有效时间:</label><span>{{props.row.layerCacheSetting.secondaryCacheSetting.expiration}}</span>
                                        </div>
                                        <div><label>缓存主动在失效前强制刷新缓存的时间:</label><span>{{props.row.layerCacheSetting.secondaryCacheSetting.preloadTime}}</span>
                                        </div>
                                        <div><label>时间单位:</label><span>{{props.row.layerCacheSetting.secondaryCacheSetting.timeUnit}}</span>
                                        </div>
                                        <div><label>是否强制刷新:</label><span>{{props.row.layerCacheSetting.secondaryCacheSetting.forceRefresh}}</span>
                                        </div>
                                        <div><label>是否使用缓存名称作为redis key前缀:</label><span>{{props.row.layerCacheSetting.secondaryCacheSetting.usePrefix}}</span>
                                        </div>
                                        <div><label>是否允许存NULL值:</label><span>{{props.row.layerCacheSetting.secondaryCacheSetting.allowNullValues}}</span>
                                        </div>
                                    </el-collapse-item>
                                </el-collapse>
                            </template>
                        </el-table-column>

                        <el-table-column
                                label="名称"
                                prop="cacheName">
                        </el-table-column>

                        <el-table-column
                                min-width="110px"
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
                                prop="firstHitRate">
                        </el-table-column>

                        <el-table-column
                                label="二级缓存命中率"
                                prop="secondHitRate">
                        </el-table-column>

                        <el-table-column
                                label="加载缓存平均耗时"
                                prop="averageTotalLoadTime">
                        </el-table-column>

                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                        size="mini"
                                        type="danger"
                                        @click="handleDelete(scope.$index, scope.row)">删除缓存
                                </el-button>
                            </template>
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

    import {getCacheList, getCacheName, deleteCache, resetCacheStats} from "@/api/cache";

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
                        if (res.data == null || res.data.length < 1) {
                            this.$message.warning("没有查询到数据!");
                            this.tableData = [];
                            this.page.pageTotal = 0;
                        } else {
                            this.formatData(res.data);
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
            handleDelete(index, row) {
                deleteCache(row).then(res => {
                    if (res.status === 'SUCCESS') {
                        this.$message.success('删除成功！');
                        this.queryCacheInfo();
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            handleResetCacheStats() {
                resetCacheStats().then(res => {
                    if (res.status === 'SUCCESS') {
                        this.$message.success('重置成功！');
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            formatData(data) {
                data.map(function (cs, key, arr) {
                    cs.hitRate = cs.hitRate.toFixed(2) + "%";
                    if (cs.firstCacheRequestCount > 0) {
                        cs.firstHitRate = ((cs.firstCacheRequestCount - cs.firstCacheMissCount) / cs.firstCacheRequestCount * 100).toFixed(2) + "%";
                    } else {
                        cs.firstHitRate = (0).toFixed(2) + "%";
                    }

                    if (cs.secondCacheRequestCount > 0) {
                        cs.secondHitRate = ((cs.secondCacheRequestCount - cs.secondCacheMissCount) / cs.secondCacheRequestCount * 100).toFixed(2) + "%";
                    } else {
                        cs.secondHitRate = (0).toFixed(2) + "%";
                    }

                    if (cs.missCount > 0) {
                        cs.averageTotalLoadTime = (cs.totalLoadTime / cs.missCount).toFixed(2) + "毫秒";
                    } else {
                        cs.averageTotalLoadTime = (0).toFixed(2) + "毫秒";
                    }
                    return cs;
                })
            },
        }
    };
</script>

<style scoped>
</style>
