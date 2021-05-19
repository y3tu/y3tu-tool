<template>
    <textarea ref="textarea" v-model="content"/>
</template>

<script>
    import CodeMirror from 'codemirror'
    import 'codemirror/lib/codemirror.css'
    // 替换主题这里需修改名称
    import 'codemirror/theme/idea.css'
    import 'codemirror/theme/rubyblue.css'
    import 'codemirror/mode/sql/sql.js'

    //代码补全提示
    import 'codemirror/addon/hint/show-hint.css';
    import 'codemirror/addon/hint/show-hint.js';
    import 'codemirror/addon/hint/sql-hint.js';
    import 'codemirror/addon/lint/json-lint.js'


    export default {
        props: {
            value: {
                type: String,
                required: true
            },
            height: {
                type: String
            },
            mode: {
                type: String,
                required: true
            },
            theme: {
                type: String,
                default: 'rubyblue'
            },
            readOnly: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                editor: null,
                codeTypes: ['text/x-java', 'text/x-mysql']
            }
        },
        computed: {
            content() {
                return this.value;
            }
        },
        created() {
            this.$nextTick(() => {
                    this.editor = CodeMirror.fromTextArea(this.$refs.textarea, {
                        mode: this.mode,
                        lineNumbers: true,
                        matchBrackets: true,
                        styleActiveLine: true,
                        lint: true,
                        lineWrapping: false,
                        tabSize: 1,
                        cursorHeight: 0.9,
                        // 替换主题这里需修改名称
                        theme: this.theme,
                        readOnly: this.readOnly
                    });


                    this.editor.setSize('auto', this.height);

                    this.editor.on('change', cm => {
                        this.$emit('change', cm.getValue())
                        this.$emit('input', cm.getValue())
                    })
                }
            )
        },
        methods: {
            forceUpdate() {
                this.$forceUpdate();
            }
        }
    }
</script>

<style lang="scss">
    .CodeMirror-sizer {
        line-height: 20px;
    }
</style>
