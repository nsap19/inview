<template>
  <el-input
    v-model="textarea"
    type="textarea"
    placeholder="작성하신 메모는 면접 종료 후 다운로드 받을 수 있습니다."
    resize="none"
    class="textarea p-2"
  />
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'

export default defineComponent({
  name: "Memo",
  props: {
    modelValue: String,
  },
  setup(props, { emit }) {
    const textarea = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });
    return { textarea }
  }
})
</script>

<style scoped>
.textarea {
  height: 100%;
  width: 100%;
  -webkit-box-sizing: border-box; /* Safari/Chrome, other WebKit */
  -moz-box-sizing: border-box;    /* Firefox, other Gecko */
  box-sizing: border-box;         /* Opera/IE 8+ */
}
</style>