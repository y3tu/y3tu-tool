<template>
    <div class="components-libs-wrapper">
        <p class="">组件库</p>
        <el-scrollbar style="height: 100%;">
            <ul>
                <li v-for="(item, index) in componentsList" :key="index">
                    <div class="components-libs-title">
                        <p>{{item.title}}</p>
                    </div>
                    <div v-if="item.components && item.components.length">
                        <div class="components-lib-item" v-for="(element,i) in item.components" :key="i"
                             @click="handleClick(element)">
                            <div class="lib-item-img"><i :class="[element.icon]"></i></div>
                            <p class="lib-item-title">{{element.title}}</p>
                        </div>
                    </div>
                    <div v-else>
                        <p class="">待完善...</p>
                    </div>
                </li>
            </ul>
        </el-scrollbar>
    </div>
</template>

<script>
    import {camelCase} from 'lodash'
    import config from '../config'
    import register_components_object from '../component-libs'

    export default {
        name: 'component-libs',
        data() {
            return {
                componentsList: config
            }
        },
        methods: {
            /**
             * 点击事件, 向父组件派发add-element事件，参数： 当前组件对象
             * @param item
             */
            handleClick(item) {
                let props = this.getComponentProps(item.elName);
                this.$store.dispatch('addElement', {...item, needProps: props})
            },
            /**
             * 根据elname获取组件默认props数据
             * @param elName
             */
            getComponentProps(elName) {
                let elComponentData
                for (let key in register_components_object) {
                    if (key.toLowerCase() === camelCase(elName).toLowerCase()) {
                        elComponentData = register_components_object[key];
                        break;
                    }
                }
                if (!elComponentData) return {}

                let props = {}
                for (let key in elComponentData.props) {
                    props[key] = [Object, Array].includes(elComponentData.props[key].type) ? elComponentData.props[key].default() : elComponentData.props[key].default
                }
                return props;
            },
        }
    }
</script>

<style lang="scss" scoped>
    .components-libs-wrapper {
        user-select: none;
        height: 100%;
        padding-top: 60px;
        position: relative;

        & ul {
            padding: 10px;
        }
    }

    .components-libs-title {
        margin-bottom: 16px;
    }

    .components-lib-item {
        color: #424242;
        text-align: center;
        background: #f4f4f4;
        width: 80px;
        float: left;
        padding: 6px 0;
        margin: 5px;
        border: 1px solid #dddddd;
        font-size: 12px;
        cursor: pointer;
        transition: All 0.3s ease-in-out;

        &:hover {
            background: #fff;
            border: 1px solid;
        }

        .lib-item-img {

        }

        .lib-item-title {

        }
    }
</style>