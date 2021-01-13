<template>
    <span>
        <el-select
                v-if="mode==='multiple'"
                :style="style"
                class="form-item"
                multiple
                collapse-tags
                v-model="value"
                filterable
                placeholder="请输入关键词">
            <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value">
            </el-option>
        </el-select>
        <el-select
                v-if="mode==='single'"
                :style="style"
                class="form-item"
                v-model="value"
                filterable
                remote
                placeholder="请输入关键词">
            <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value">
            </el-option>
        </el-select>
    </span>
</template>

<script>

    import {getDictDataByCode} from './api'

    export default {
        name: 'dictSelect',
        props: {
            dictCode: {
                type: String
            },
            style:{
                type: String
            },
            mode: {
                type: String,
                default: "single"
            }
        },
        data() {
            return {
                options: [],
                value:[],
            }
        },
        created() {
            this.$nextTick(() => {
                getDictDataByCode(this.dictCode).then((res) => {
                    if (res.data) {
                        this.options = res.data;
                    }
                })
            })
        },
        mounted() {

        },
        methods: {}
    }
</script>

<style>
    .form-item {
        display: inline-block;
        vertical-align: middle;
        margin-bottom: 10px;
        margin-left: 10px;
    }
</style>