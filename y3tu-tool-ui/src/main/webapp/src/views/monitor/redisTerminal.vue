<template>
    <el-card>
        <div class="container">
            <div class="window">
                <div class="handle">
                    <div class="buttons">
                        <button class="closes"></button>
                        <button class="minimize"></button>
                        <button class="maximize"></button>
                    </div>
                    <span class="title">{{title}}</span>
                </div>
                <div class="terminal" type='text' v-text="terminal"></div>
            </div>
        </div>
    </el-card>
</template>

<script>
    import {validateNull} from '@/utils/validate'

    export default {
        name: "redisTerminal",
        data() {
            return {
                commands: [{
                    name: "clear",
                    function: this.clear
                }, {
                    name: "help",
                    function: this.help
                }, {
                    name: "echo",
                    function: this.echo
                }, {
                    name: "keys",
                    function: keys
                }, {
                    name: "get",
                    function: get
                }, {
                    name: "exists",
                    function: exists
                }, {
                    name: "pttl",
                    function: pttl
                }, {
                    name: "pexpire",
                    function: pexpire
                }, {
                    name: "set",
                    function: set
                }, {
                    name: "del",
                    function: del
                }],
                osName: 'Linux',
                prompt: "➜",
                path: "~",
                commandHistory: [],
                historyIndex: 0,
                command: '',
                terminal: ''
            }
        },
        mounted() {
            this.initUI();
        },
        methods: {
            initUI() {

            },
            //清空控制台
            clear() {
                this.terminal = '';
            },
            help(arg) {
                arg = arg.join(" ");
                if (validateNull(arg)) {
                    switch (arg) {
                        case "keys": {
                            this.printf(" KEYS pattern\n");
                            this.printf(" <span class='kw_a'>summary</span>: Find all keys matching the given pattern.\n");
                            this.printf(" <span class='kw_a'>since</span>: 1.0.0\n");
                            this.printf(" <span class='kw_a'>group</span>: generic\n");
                            break;
                        }

                        case "get": {
                            this.printf(" GET key\n");
                            this.printf(" <span class='kw_a'>summary</span>: Get the value of a key\n");
                            this.printf(" <span class='kw_a'>since</span>: 1.0.0\n");
                            this.printf(" <span class='kw_a'>group</span>: string\n");
                            break;
                        }
                        case "set": {
                            this.printf(" DEL key [key ...]\n");
                            this.printf(" <span class='kw_a'>summary</span>:  Delete a key\n");
                            this.printf(" <span class='kw_a'>since</span>: 1.0.0\n");
                            this.printf(" <span class='kw_a'>group</span>: generic\n");
                            break;
                        }
                        case "del": {
                            this.printf(" SET key value\n");
                            this.printf(" <span class='kw_a'>summary</span>: Set the string value of a key\n");
                            this.printf(" <span class='kw_a'>since</span>: 1.0.0\n");
                            this.printf(" <span class='kw_a'>group</span>: string\n");
                            break;
                        }
                        case "exists": {
                            this.printf(" EXISTS key [key ...]\n");
                            this.printf(" <span class='kw_a'>summary</span>: Determine if a key exists\n");
                            this.printf(" <span class='kw_a'>since</span>: 1.0.0\n");
                            this.printf(" <span class='kw_a'>group</span>: generic\n");
                            break;
                        }
                        case "pttl": {
                            this.printf(" PTTL key\n");
                            this.printf(" <span class='kw_a'>summary</span>: Get the time to live for a key in milliseconds\n");
                            this.printf(" <span class='kw_a'>since</span>: 2.6.0\n");
                            this.printf(" <span class='kw_a'>group</span>: generic\n");
                            break;
                        }
                        case "pexpire": {
                            this.printf(" PEXPIRE key milliseconds\n");
                            this.printf(" <span class='kw_a'>summary</span>: Set a key's time to live in milliseconds\n");
                            this.printf(" <span class='kw_a'>since</span>: 2.6.0\n");
                            this.printf(" <span class='kw_a'>group</span>: generic\n");
                            break;
                        }
                        default: {
                            this.printf(" no more messages about command `" + arg + "`\n");
                        }
                    }
                }else {
                    this.printf(" Redis bash with SpringBoot by <a href='https://mrbird.cc' class='kw_b'>MrBird</a>, version v0.0.1 - Snapshot \n");
                    this.printf(" These shell commands are defined internally.  Type `help` to see this list.\n");
                    this.printf(" Type `help name` to find out more about the command `name`.\n");
                    this.printf(" -------------------------------- \n");
                    this.printf(" |       system command         | \n");
                    this.printf(" -------------------------------- \n");
                    this.printf(" clear    help    echo\n");
                    this.printf(" -------------------------------- \n");
                    this.printf(" |       redis  command         | \n");
                    this.printf(" -------------------------------- \n");
                    this.printf(" keys         get         set \n");
                    this.printf(" del          exists        pttl\n");
                    this.printf(" pexpire\n");
                }
                this.printc();
            },
            echo(args){
                let str = args.join("");
                this.printf(str + "\n");
                this.printc();
            },
            printf(msg) {
                this.terminal += msg;
            },
            printc() {

            },
            scroll(o) {
                o = o[0];
                o.scrollTop = o.scrollHeight;
            },
            displayPrompt() {
                this.printf('<span class="prompt">' + this.prompt + "</span> ");
                this.printf('<span class="path">' + this.path + "</span> ");
            }

        },
        computed: {
            title() {
                return "@" + this.osName + ": ~ (redis-cli)";
            }
        },
        components: {}
    };
</script>

<style>
    textarea, input, button {
        outline: none
    }

    .window-button, .window .buttons .closes, .window .buttons .minimize, .window .buttons .maximize {
        padding: 0;
        margin: 0;
        margin-right: 6px;
        width: 12px;
        height: 12px;
        background-color: gainsboro;
        border: 1px solid rgba(0, 0, 0, 0);
        border-radius: 6px;
        color: rgba(0, 0, 0, 0.5)
    }

    .window {
        width: 100%
    }

    .window .handle {
        height: 38px;
        background: #f9f9f9;
        border: 1px solid #eee;
        border-top-left-radius: 3px;
        border-top-right-radius: 3px;
        color: rgba(0, 0, 0, 0.7);
        font-family: 'consolas';
        font-size: 13px;
        line-height: 38px;
        text-align: center
    }

    .window .buttons {
        position: absolute;
        float: left;
        margin: 0 8px
    }

    .window .buttons .closes {
        background-color: #fc625d
    }

    .window .buttons .minimize {
        background-color: #fdbc40
    }

    .window .buttons .maximize {
        background-color: #35cd4b
    }

    .window .terminal {
        padding: 4px;
        background-color: black;
        opacity: 0.7;
        height: 27rem;
        color: white;
        font-family: 'consolas';
        font-weight: 200;
        font-size: 13px;
        white-space: pre-wrap;
        white-space: -moz-pre-wrap;
        white-space: -o-pre-wrap;
        word-wrap: break-word;
        border-bottom-left-radius: 3px;
        border-bottom-right-radius: 3px;
        border-top-left-radius: 1px;
        border-top-right-radius: 1px;
        overflow-y: scroll
    }

    .window .terminal::after {
        content: "|";
        animation: blink 1.25s steps(1) infinite
    }

    .prompt {
        color: #bde371
    }

    .path {
        color: #5ed7ff
    }

    @keyframes blink {
        50% {
            color: transparent
        }
    }

    @keyframes bounceIn {
        0% {
            transform: translateY(-1000px)
        }
        60% {
            transform: translateY(200px)
        }
        100% {
            transform: translateY(0px)
        }
    }

    .kw_a {
        color: #E6DB74
    }

    .kw_b {
        color: #bde371
    }
</style>
