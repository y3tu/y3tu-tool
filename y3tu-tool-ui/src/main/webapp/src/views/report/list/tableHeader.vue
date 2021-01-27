<template>
  <div :key="editorKey">
    <code-editor :value="value"
                 mode="application/json"
                 height="300px"
                 @change="valueChange"/>
  </div>

  <el-tree
      v-show="!jsonError"
      :data="jsonData"
      node-key="field"
      default-expand-all
      @node-drag-end="handleDragEnd"
      draggable>
  </el-tree>

  <el-alert
      v-show="jsonError"
      title="表头Json数据错误,请检查!"
      type="error">
  </el-alert>

</template>

<script>
/* eslint-disable */
import CodeEditor from '@/components/CodeEditor'

export default {
  name: 'tableHeader',
  components: {CodeEditor},
  props: {
    value: {
      type: String,
      default: ""
    }
  },
  emits: ["change"],
  computed: {
    jsonData() {
      let result = {};
      try {
        result = JSON.parse(this.value);
      } catch (e) {
        this.jsonError = true
        return
      }
      this.jsonError = false
      return result
    }
  },
  data() {
    return {
      editorKey: 0,
      jsonError: false
    };
  },
  methods: {
    handleDragEnd() {
      let value = JSON.stringify(this.jsonData, null, 2);
      this.$emit('change', value)
      this.editorKey = this.editorKey + 1;
    },
    valueChange(value) {
      this.$emit('change', value)
    }
  }
};
</script>