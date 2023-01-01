<template>
  <el-popover
    v-if="visible"
    placement="bottom-end"
    width="300px"
    trigger="click"
    popper-class="header-notice-wrapper"
    show-arrow
  >
    <div v-loading="loading">
      <el-tabs stretch v-model="activeTab">
        <el-tab-pane label="通知" name="1">
          <section>
            <ElList title="你收到了 14 份新周报" description="一年前"
              ><template #avatar
                ><el-avatar
                  style="background-color: white"
                  src="https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png" /></template
            ></ElList>
            <el-divider />
            <ElList title="你推荐的 曲妮妮 已通过第三轮面试" description="一年前">
              <template #avatar>
                <el-avatar
                  style="background-color: white"
                  slot="avatar"
                  src="https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png"
              /></template>
            </ElList>
            <el-divider />
            <ElList title="这种模板可以区分多种通知类型" description="一年前">
              <template #avatar>
                <el-avatar
                  style="background-color: white"
                  slot="avatar"
                  src="https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png"
                />
              </template>
            </ElList>
          </section>
        </el-tab-pane>
        <el-tab-pane label="消息" name="2"> 123 </el-tab-pane>
        <el-tab-pane label="待办" name="3"> 123 </el-tab-pane>
      </el-tabs>
    </div>

    <template #reference
      ><el-badge :value="12" class="notice-badge">
        <BellOutlined style="font-size: 16px; padding: 4px" />
      </el-badge>
      <span @click="fetchNotice" class="header-notice" ref="noticeRef" style="padding: 0 18px; cursor: pointer"> </span>
    </template>
  </el-popover>
</template>

<script>
import { BellOutlined } from '@ant-design/icons-vue'
import ElList from '@/components/ElList'
export default {
  name: 'HeaderNotice',
  components: {
    ElList,
    BellOutlined,
  },
  data() {
    return { activeTab: '1', loading: false, visible: false }
  },
  methods: {
    fetchNotice() {
      if (!this.visible) {
        this.loading = true
        setTimeout(() => {
          this.loading = false
        }, 2000)
      } else {
        this.loading = false
      }
      this.visible = !this.visible
    },
  },
}
</script>

<style lang="less">
.header-notice-wrapper {
   ::v-deep( .el-divider--horizontal){
    margin: 10px 0;
  }
}
.notice-badge {
  margin-top: 10px;
  margin-right: 40px;
}
.header-notice {
  display: inline-block;
  transition: all 0.3s;

  span {
    vertical-align: initial;
  }
}
</style>
