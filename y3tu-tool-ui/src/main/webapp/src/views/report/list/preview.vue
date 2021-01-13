<template>
    <div class="main-container">
        <div class="head-container">
            <el-row>
                <el-col :span="8" v-for="param in report.params" :key="param.id">
                    <el-tooltip class="item" effect="dark" :content="param.name" placement="top-start">
                        <label style="width:30%" class="form-item-label">{{param.name}}</label>
                    </el-tooltip>
                    <label>
                        <el-input v-if="param.type===1" clearable
                                  style="width:53%"
                                  v-model="param.value"
                                  class="form-item"
                                  @keyup.enter="query"/>
                        <dict-select v-if="param.type===2||param.type===3"
                                     :mode="param.type===3?'single':'multiple'"
                                     :style="'width:53%'"
                                     :dict-code="param.dictCode"
                                     v-model="param.value"/>
                        <el-date-picker
                                v-if="param.type===4"
                                v-model="param.value"
                                type="month"
                                class="form-item"
                                placeholder="选择月">
                        </el-date-picker>
                        <el-date-picker
                                v-if="param.type===5"
                                v-model="param.value"
                                type="date"
                                class="form-item"
                                placeholder="选择日期">
                        </el-date-picker>
                        <el-date-picker
                                v-if="param.type===6"
                                v-model="param.value"
                                type="datetime"
                                class="form-item"
                                placeholder="选择日期时间">
                        </el-date-picker>
                    </label>
                </el-col>
            </el-row>

            <el-row type="flex" justify="end">
                <el-col :span="4">
                    <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain @click="query">
                        查询
                    </el-button>
                    <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain
                               @click="reset">
                        重置
                    </el-button>
                </el-col>
            </el-row>

        </div>

        <el-divider/>

        <el-table ref="table" size="small" style="width: 100%;">

        </el-table>


    </div>
</template>

<script>

    import dictSelect from "../dict/dictSelect";

    export default {
        name: "preview",
        components: {dictSelect},
        props: {
            value: {
                type: Object
            },
        },
        data() {
            return {}
        },
        computed: {
            report() {
                return this.value;
            }
        },
        methods: {
            query() {

            },
            reset() {

            }
        }
    }
</script>

<style>

</style>