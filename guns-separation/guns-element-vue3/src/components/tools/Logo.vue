<template>
  <div class="logo">
    <router-link :to="{name:'Console'}">
       <img  src='@/assets/logo.svg' />
      <h1 v-if="showTitle">{{ this.titles }}</h1>
    </router-link>
  </div>
</template>

<script>

import { mixin, mixinDevice } from '@/utils/mixin'

export default {
  name: 'Logo',

  mixins: [mixin, mixinDevice],
  data () {
    return {
      titles: ''
    }
  },
  props: {
    title: {
      type: String,
      default: '提瓦特乐园',
      required: false
    },
    showTitle: {
      type: Boolean,
      default: true,
      required: false
    }
  },
  created () {
    if (this.layoutMode === 'topmenu') {
      if (this.title.length > 8) {
        this.titles = this.title.substring(0, 7) + '...'
      } else {
        this.titles = this.title
      }
    } else {
      if (this.title.length > 10) {
        this.titles = this.title.substring(0, 9) + '...'
      } else {
        this.titles = this.title
      }
    }
  }
}
</script>
