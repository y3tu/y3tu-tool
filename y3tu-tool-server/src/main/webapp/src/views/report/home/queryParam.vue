<template>
    <el-button class="form-item" size="mini" type="primary" icon="el-icon-plus" circle @click="add"/>
    <el-form v-for="(param,index) in paramArr"
             :key="param.id"
             :inline="true"
             size="mini">
        <el-form-item label="名称">
            <el-input v-model="param.name" size="mini" placeholder="请输入参数名称"></el-input>
        </el-form-item>
        <el-form-item label="字段名">
            <el-input v-model="param.field" size="mini" placeholder="请输入字段名"></el-input>
        </el-form-item>
        <el-form-item label="类型">
            <el-select v-model="param.type" size="mini" placeholder="参数取值">
                <el-option label="输入取值" :value="1"></el-option>
                <el-option label="字典下拉" :value="2"></el-option>
                <el-option label="字典下拉多选" :value="3"></el-option>
                <el-option label="月份" :value="4"></el-option>
                <el-option label="日期" :value="5"></el-option>
                <el-option label="时间" :value="6"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item v-show="param.type===2||param.type===3" label="字典编码">
            <el-select size="mini" v-model="param.dictCode"
                       filterable
                       placeholder="请输入关键词">
                <el-option v-for="item in dictList"
                           :key="item.id"
                           :label="item.name"
                           :value="item.code">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button class="form-item" size="mini" type="danger" icon="el-icon-delete" circle @click="del(index)"/>
            <el-button v-if="index !==0" class="form-item" size="mini" type="primary" icon="el-icon-arrow-up" circle
                       @click="up(index)"/>
            <el-button v-if="index !==paramArr.length-1" class="form-item" size="mini" type="primary"
                       icon="el-icon-arrow-down" circle @click="down(index)"/>
        </el-form-item>
    </el-form>
</template>

<script>
    import {swap} from '@/utils'
    import {getAllDict} from "./api";

    export default {
        props: {
            params: {
                type: Array,
                required: true
            }
        },
        data() {
            return {
                selectLoading: false,
                dictList: []
            }
        },
        created() {
            this.$nextTick(() => {
                getAllDict().then(res => {
                    if (res.data && res.data.length > 0) {
                        this.dictList = res.data;
                    } else {
                        this.dictList = [];
                    }
                })
            })
        },
        computed: {
            paramArr() {
                return this.params;
            }
        },
        methods: {
            add() {
                this.paramArr.push({
                    id: '',
                    name: '',
                    field: '',
                    type: '',
                    dictCode: '',
                })
            },
            del(index) {
                this.paramArr.splice(index, 1);
            },
            up(index) {
                swap(this.paramArr, index, index - 1)
            },
            down(index) {
                swap(this.paramArr, index, index + 1)
            },
            getParams() {
                return this.paramArr;
            }
        }
    }
</script>

<style scoped>
    .el-form-item--mini {
        line-height: 0px;
    }
</style>