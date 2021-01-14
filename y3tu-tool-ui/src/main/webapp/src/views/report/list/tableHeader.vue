<template>
    <div :key="editorKey">
        <code-editor :value="value"
                     mode="application/json"
                     height="300px"
                     @change="valueChange"/>
    </div>

    <el-tree
            :data="jsonData"
            node-key="field"
            default-expand-all
            @node-drag-end="handleDragEnd"
            draggable>
    </el-tree>

</template>

<script>
    import CodeEditor from '@/components/CodeEditor'

    export default {
        name: 'tableHeader',
        components: {CodeEditor},
        props: {
            value: {
                type: String,
                default: ""
            }
        },
        emits:["change"],
        computed: {
            jsonData() {
                return JSON.parse(this.value);
            }
        },
        data() {
            return {
                editorKey: 0,
            };
        },
        methods: {
            handleDragEnd() {
                let value = JSON.stringify(this.jsonData, null, 2);
                this.$emit('change', value)
                this.editorKey = this.editorKey + 1;
            },
            valueChange(value) {
                this.$emit('change', value)
            }
        }
    };
</script>