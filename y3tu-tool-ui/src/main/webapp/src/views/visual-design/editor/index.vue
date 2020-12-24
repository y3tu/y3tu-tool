<template>
    <div class="editor-wrapper">
        <!--左侧导航-->
        <div class="editor-side-bar">
            <el-tabs tab-position="left" v-model="activeSideBar" style="height: 100%;">
                <el-tab-pane v-for="(item, index) in sidebarMenus" :key="index" :name="item.value">
                    <template v-slot:label>
                        <el-tooltip effect="dark" :content="item.label" placement="right">
                            <i :class="item.elementUiIcon"></i>
                        </el-tooltip>
                    </template>
                </el-tab-pane>
            </el-tabs>
        </div>
        <!--组件&页面&模板-->
        <div class="editor-component-wrapper">
            <componentPanel v-if="activeSideBar === 'componentPanel'"/>
        </div>

        <!--页面编辑区域-->
        <div class="editor-main">
            <div class="control-bar-wrapper">
                <controlBar
                        v-model="canvasConfig.scale"
                        @import-psd-data="importPsdData"
                        @showPreview="showPreviewFn"
                        @cancel="cancelFn"
                        @publish="publishFn"
                        @save="saveFn"/>
            </div>
            <editorPanel v-model="canvasConfig.scale"/>
        </div>

        <!--属性编辑区域-->
        <div class="editor-attr-wrapper">

        </div>
    </div>


</template>
<script>

    import componentPanel from './component-panel'
    import controlBar from './control-bar'
    import editorPanel from './editor-panel'

    export default {
        components: {
            componentPanel,
            controlBar,
            editorPanel
        },
        data() {
            return {
                activeSideBar: 'componentPanel',
                sidebarMenus: [
                    {
                        label: '组件列表',
                        value: 'componentPanel',
                        elementUiIcon: 'el-icon-s-operation'
                    },
                    {
                        label: '页面管理',
                        value: 'pageManage',
                        elementUiIcon: 'el-icon-document'
                    },
                    {
                        label: '模板库',
                        value: 'templateLibs',
                        elementUiIcon: 'el-icon-files'
                    }
                ],
                canvasConfig: {
                    scale: 1
                }
            };
        }
    };
</script>

<style lang="scss" scoped>
    .editor-wrapper {
        display: flex;
        height: 100%;
        position: relative;

        .editor-side-bar {
            width: 55px;
        }

        .editor-component-wrapper {
            width: 210px;
            padding: 0 1px;
        }

        .editor-main {
            flex: 1;
            background: #f0f2f5;
            position: relative;
        }

        .editor-attr-wrapper {
            width: 380px;
            padding: 0;
        }
    }
</style>