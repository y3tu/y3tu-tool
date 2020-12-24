<template>
    <div class="main-container">
        <div class="head-container">
            <label class="form-item-label">缓存名称</label>
            <el-input clearable v-model="cacheName" placeholder="请输入缓存名" style="width:200px" class="form-item"
                      @keyup.enter="search"/>
            <el-button class="form-item" size="mini" type="success" icon="el-icon-search" @click="search">搜索</el-button>
            <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left"
                       @click="resetAllCacheStat">重置缓存统计数据
            </el-button>
            <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left"
                       @click="clearAllCache">清除缓存
            </el-button>
        </div>

        <el-table
                border
                stripe
                fit
                highlight-current-row
                fixed
                v-loading="pageLoading"
                :data="tableData"
                size="small"
                style="width: 100%;">
            <el-table-column label="序号" width="80" align="center">
                <template #defualt="scope">
                    <div>{{ scope.$index + 1 }}</div>
                </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" align="center" prop="cacheName" label="缓存名"/>
            <el-table-column :show-overflow-tooltip="true" align="center" prop="depict" label="描述"/>
            <el-table-column prop="requestCount" align="center" label="请求总数"/>
            <el-table-column prop="missCount" align="center" label="未命中总数" width="150px"/>
            <el-table-column :show-overflow-tooltip="true" prop="hitRate" align="center" label="命中率"/>
            <el-table-column prop="firstCacheRequestCount" align="center" label="一级缓存命中总数" width="150px"/>
            <el-table-column prop="firstCacheMissCount" align="center" label="一级缓存未命中总数" width="150px"/>
            <el-table-column prop="secondCacheRequestCount" align="center" label="级缓存命中总数" width="150px"/>
            <el-table-column prop="secondCacheMissCount" align="center" label="二级缓存未命中总数" width="150px"/>
            <el-table-column prop="totalLoadTime" align="center" label="总的请求时间" width="150px"/>


            <el-table-column label="操作" align="center" fixed="right" width="150px">
                <template #default="scope">
                    <el-button size="mini" type="danger" icon="el-icon-delete" @click="clearCacheByName(scope.row)">
                        清除缓存
                    </el-button>
                    <el-button size="mini" type="danger" icon="el-icon-delete" @click="resetCacheStatByCacheName(scope.row)">
                        重置统计
                    </el-button>
                </template>
            </el-table-column>

        </el-table>

        <!--分页组件-->
        <el-pagination
                :total="total"
                style="margin-top: 8px;"
                layout="prev, pager, next"
                :currentPage="currentPage"
                :page-size="pageSize"
                @size-change="sizeChange"
                @current-change="pageChange">
        </el-pagination>

    </div>
</template>

<script>
    import {listCacheStats, resetAllCacheStat, resetCacheStatByCacheName, clearAllCache, clearCacheByName} from './api'

    export default {
        name: 'cacheManager',
        data() {
            return {
                cacheName: '',
                tableData: [],
                pageLoading: false,
                currentPage: 1, // 当前页码
                total: 0, // 总条数
                pageSize: 10, // 每页的数据条数
            }

        },
        created() {
            this.$nextTick(() => {
                this.init();
            })
        },
        methods: {
            init() {
            },
            search() {
                this.pageLoading = true;
                listCacheStats().then(res => {
                    this.tableData = res.data;
                    let list = this.tableData.filter((item) =>
                        item.cacheName.includes(this.cacheName)
                    );
                    this.list = list.filter((item, index) =>
                        index < this.currentPage * this.pageSize && index >= this.pageSize * (this.currentPage - 1)
                    );
                    this.total = list.length;
                    this.tableData = this.list;
                    this.pageLoading = false;
                }).catch(() => {
                    this.pageLoading = false;
                })
            },
            clearAllCache() {
                clearAllCache().then(() => {
                    this.$message({
                        message: '清除缓存',
                        type: 'success',
                        duration: 3 * 1000
                    });
                })
            },
            clearCacheByName(row) {
                clearCacheByName(row.cacheName).then(() => {
                    this.$message({
                        message: '清除缓存',
                        type: 'success',
                        duration: 3 * 1000
                    });
                })
            },
            resetCacheStatByCacheName(row) {
                resetCacheStatByCacheName(row.cacheName).then(() => {
                    this.$message({
                        message: '重置缓存统计',
                        type: 'success',
                        duration: 3 * 1000
                    });
                })
            },
            resetAllCacheStat() {
                resetAllCacheStat().then(() => {
                    this.$message({
                        message: '重置缓存统计',
                        type: 'success',
                        duration: 3 * 1000
                    });
                })
            },
            sizeChange(val) {
                this.currentPage = 1;
                this.pageSize = val;
                this.search();
            },
            pageChange(val) {
                this.currentPage = val;
                this.search();
            },
        }

    }
</script>

<style>

</style>