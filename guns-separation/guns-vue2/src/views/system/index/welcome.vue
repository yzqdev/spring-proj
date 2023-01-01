<template>
  <a-card :bordered="false" style="display: flex;justify-content:center;height: 100%" >
    <div style="margin:100px auto;">
      <img src="~@/assets/welcome.png" class="logo" alt="logo">
    </div>
  </a-card>
</template>

<script>
export default {
  created() {
    console.log(this.$router.getRoutes())
  }
}
</script>

<style lang="less" scoped>

</style>
