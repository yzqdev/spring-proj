<template>
  <div><a-tag v-for='item in tagList'>{{item.name}}</a-tag> </div>
</template>
<script>
export default {
  name: 'TagCloud',
  props: {
    tagList: { type: Array },
  },
}
</script>
