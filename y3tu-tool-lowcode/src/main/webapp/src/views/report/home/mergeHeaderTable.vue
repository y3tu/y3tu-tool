<script type="text/jsx">
    /* eslint-disable */
    export default {
        name: 'mergeHeaderTable',
        props: {
            header: {
                type: [Array, Object],
                default: () => {
                    return []
                },
            },
            tableData: {
                type: [Array, Object],
                default: () => {
                    return []
                },
            },
            loading: {
                type: Boolean,
                default: () => {
                    return false
                }
            }
        },
        data() {
            return {}
        },
        methods: {
            // 递归函数
            recursion(arrs) {
                if (arrs != undefined) {
                    return arrs.map((item) => {
                        if (!!item.children&&item.children.length>0) {
                            return (
                                <el-table-column label={item.label}>
                                    {this.recursion(item.children)}
                                </el-table-column>
                            )
                        } else {
                            return this.createElTableColumn(item.field, item.label, item.width)
                        }
                    })
                }
            },

            createElTableColumn(prop, label, width) {
                return <el-table-column prop={prop} label={label} width={width}></el-table-column>
            },
        },
        render() {
            return (
                <el-table
                    border
                    v-loading={this.loading}
                    data={this.tableData}
                    style="width: 100%"
                    style="width: 100%;margin: 20px 0;"
                    class="merge-header-table"
                    header-cell-style={() => {
                        return {background: '#f9f9f9'}
                    }}>
                    {this.recursion(this.header)}
                </el-table>
            )
        },
    }
</script>
<style lang="scss">
    .merge-header-table {
        th,
        td {
            color: #767676;
            padding: 5px 0 !important;
        }
    }
</style>