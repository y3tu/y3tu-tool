//基础路径 注意发布之前要先修改这里
//打包使用
const baseUrl = '';

module.exports = {
    publicPath: baseUrl,
    lintOnSave: true,
    devServer: {
        disableHostCheck: true,
    },
    // 打包时不生成.map文件 避免看到源码
    productionSourceMap: false
}