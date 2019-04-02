<template>
    <div>
        <developing></developing>
        <div class="card">
            <div class="card-header">
            </div>
            <div class="card-block">
                <div id="container" style="height:300px"></div>
            </div>
        </div>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title"></h2>
            </div>
            <div class="card-block">
                <div id="keysChart" style="height:300px"></div>
            </div>
        </div>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title" style="padding:1rem 0">Redis 详细信息：</h2>
            </div>
            <div class="card-block">
                <table class="table table-condensed table-hover"
                       style="word-break: break-all; word-wrap: break-word;">
                    <tbody>

                    <tr v-for="info in infoList">
                        <td style="width:30%;font-weight:bold">{{info.key}}</td>
                        <td style="width:40%">{{info.description}}</td>
                        <td style="width:30%">{{info.value}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</template>

<script>
    import developing from '@/views/components/Developing'

    export default {
        name: "redisMonitor",
        data() {
            return {
                infoList: []
            }
        },
        components: {
            developing
        }
    };
</script>

<style>
    tr {
        border-top: 1px solid #eee;
        font-size: 1rem
    }

    tr:last-child {
        border-bottom: 1px solid #eee
    }

    tr:hover {
        background: #fafafa !important
    }
</style>
