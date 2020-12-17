import {
    ElButton,
    ElSelect,
    ElTable,
    ElCol,
    ElRow,
    ElDialog,
    ElMessage,
    ElInput,
    ElTableColumn,
    ElPagination,
    ElLoading,
} from 'element-plus';

export default {
    install(app) {
        app.use(ElButton)
        app.use(ElSelect)
        app.use(ElTable)
        app.use(ElCol)
        app.use(ElRow)
        app.use(ElDialog)
        app.use(ElMessage)
        app.use(ElInput)
        app.use(ElTableColumn)
        app.use(ElPagination)
        app.use(ElLoading)
    }
}
