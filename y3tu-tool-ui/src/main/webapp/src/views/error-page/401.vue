<template>
    <div class="errPage-container">
        <el-row>
            <el-col :span="12">
                <el-button icon="el-icon-arrow-left" class="pan-back-btn" @click="back">
                    返回
                </el-button>
                <h2>Sorry! 你没有权限访问该页面</h2>
            </el-col>
            <el-col :span="12">
                <img :src="errGif" width="313" height="428" alt="Girl has dropped her ice cream.">
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import errGif from '../../assets/401_images/401.gif'

    export default {
        name: 'Page401',
        data() {
            return {
                errGif: errGif + '?' + +new Date(),
                ewizardClap: 'https://wpimg.wallstcn.com/007ef517-bafd-4066-aae4-6883632d9646',
            }
        },
        methods: {
            back() {
                if (this.$route.query.noGoBack) {
                    this.$router.push({path: '/#'})
                } else {
                    this.$router.go(-1)
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .errPage-container {
        width: 800px;
        max-width: 100%;
        margin: 100px auto;

        .pan-back-btn {
            background: #008489;
            color: #fff;
            border: none !important;
        }

        .pan-gif {
            margin: 0 auto;
            display: block;
        }

        .pan-img {
            display: block;
            margin: 0 auto;
            width: 100%;
        }

        .text-jumbo {
            font-size: 60px;
            font-weight: 700;
            color: #484848;
        }

        .list-unstyled {
            font-size: 14px;

            li {
                padding-bottom: 5px;
            }

            a {
                color: #008489;
                text-decoration: none;

                &:hover {
                    text-decoration: underline;
                }
            }
        }
    }
</style>
