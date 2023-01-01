<template>
  <div>
    <a-button @click='active'>激活</a-button>
  </div>
</template>

<script>
import { activeUser } from '@/api/modular/system/loginManage'

export default {
  name: 'ActiveUser',
  methods: {
    active() {
      activeUser('1280709549107552257').then(({ data }) => {
        console.log(data)
      })


    }
  }
}
</script>

<style scoped>

</style>