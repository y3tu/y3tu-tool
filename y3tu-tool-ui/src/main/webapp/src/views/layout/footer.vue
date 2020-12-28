<template>
    <div class="wrapper-footer">
        <div class="footer">
            <div class="address flex">
                <div>
                    <div class="copyright">
                        <span>
                          Copyright ©2020 - 2021 Y3tu Tool
                        </span>
                    </div>
                </div>
                <div class="logo">
                    <img :src="logo"/>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

    import logo from '../../assets/logo.png'

    export default {
        name: 'Footer',
        data() {
            return {
                logo
            }
        }
    }
</script>

<style lang="scss">
    .wrapper-footer {
        clear: both;
        background-color: #232f3d;
        position: relative;
        z-index: 100;
        color: rgba(255, 255, 255, 0.8);

        .footer {
            padding: 24px 0;
            font-size: 14px;

            .copyright {
                font-size: 0.75rem;
                color: rgba(255, 255, 255, 0.65);

                a {
                    color: rgba(255, 255, 255, 0.65);
                    text-decoration: none;
                    margin-left: 25px;
                }
            }


            .address {
                justify-content: center;
                align-items: center;
                text-align: center;

                ul {
                    overflow: hidden;
                    margin-top: 0;
                    margin-bottom: 5px;

                    li {
                        list-style: none;
                        float: left;

                        & + li {
                            margin-left: 40px;
                        }
                    }
                }
            }

            .logo {
                margin-left: 30px;
                width: 70px;
                height: 60px;

                img {
                    max-width: 100%;
                }
            }
        }

    }
</style>
