<template>
  <el-card  :shadow='false' style="display: flex;justify-content:center;height: 100%" >

    <div style="margin:100px auto;">
      <img src="~@/assets/welcome.png" class="logo" alt="logo">
    </div>
  </el-card>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name:'Welcome',data(){
    return{
      radio:''
    }
  }
})
</script>

<style lang="less" scoped>

</style>
