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
                    </el-row>
                </el-card>
            </el-header>

            <el-main>

                <el-card class="box-card">
                    <el-table :data="currentTableData"
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

    import {cacheList} from "@/api/cache";

    export default {
        name: "cacheManager",
        data() {
            return {
                currentTableData: [],
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
        methods: {
            getCacheName: function (queryString, cb) {
                let arr = [{"value": "Hot honey 首尔炸鸡（仙霞路）", "name": "上海市长宁区淞虹路661号"},
                    {"value": "新旺角茶餐厅", "name": "上海市普陀区真北路988号创邑金沙谷6号楼113"}];
                cb(arr);
            },
            handleSelect(item) {
                console.log(item);
            },
            queryCacheInfo: function () {
                cacheList(this.cacheName);
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handlePaginationChange(val) {
                this.$notify({
                    title: '分页变化',
                    message: `当前第${val.current}页 共${val.total}条 每页${val.size}条`
                })
                this.page = val
                // nextTick 只是为了优化示例中 notify 的显示
                this.$nextTick(() => {

                })
            },
        }
    };
</script>

<style scoped>
</style>
