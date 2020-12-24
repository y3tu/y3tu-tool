<template>
    <div id="xss-demo"></div>
</template>

<script>
    import Spreadsheet from 'x-data-spreadsheet';
    /* eslint-disable */
    import zhCN from 'x-data-spreadsheet/dist/locale/zh-cn';

    Spreadsheet.locale('zh-cn');

    export default {
        name: 'design',
        created() {

        },
        mounted() {
            new Spreadsheet("#xss-demo").onSave()
        }
    }
</script>

<style>

</style>