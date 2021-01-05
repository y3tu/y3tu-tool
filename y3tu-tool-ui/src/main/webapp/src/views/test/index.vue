<template>
    <code-editor :value="querySql" height="250px" code-type="text/x-sql" @change="querySqlChange"/>

</template>

<script>
    import CodeEditor from '@/components/CodeEditor'

    export default {
        components: {CodeEditor},
        data() {
            return {
                querySql:'select * from dual',
                report: {
                    querySql: 'select * from dual;'
                },
            }
        },
        methods: {
            querySqlChange(val) {
                this.querySql = val
            }
        }
    }
</script>