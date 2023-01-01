<template>
  <section class="list-item">
    <article class="list-avatar"><slot name="avatar"></slot></article>
    <article class="list-content">
      <h4 class="list-title">{{ title }}</h4>
      <div class="list-description">{{ description }}</div>
    </article>
  </section>

</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'ElList',
  props: {
    title: {
      type: String,
    },
    description: {
      type: String,
    },
  },
})
</script>

<style lang="less" scoped>
.list-item {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;
  -webkit-box-align: start;
  -ms-flex-align: start;
  align-items: flex-start;
  font-size: 0;
  .list-avatar {
    margin-right: 16px;
  }
  .list-content {
    -webkit-box-flex: 1;
    -ms-flex: 1 0;
    flex: 1 0;
    .list-title {
      margin-bottom: 4px;
      color: rgba(0, 0, 0, 0.65);
      font-size: 14px;
      line-height: 22px;
    }
    .list-description {
      color: rgba(0, 0, 0, 0.45);
      font-size: 14px;
      line-height: 22px;
    }
  }
}
</style>
