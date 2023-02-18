<template>
  <van-row class="headRow">
    <van-col span="2"><van-icon name="scan" size="1.5rem" /></van-col>
    <van-col span="20"></van-col>
    <van-col span="2"
      ><van-icon name="setting-o" size="1.5rem" @click="show = true"
    /></van-col>
  </van-row>

  <van-row class="bodyRow">
    <van-col span="6" class="leftCol">
    <AppUserIcon width="4rem" height="4rem" :src="user.icon"></AppUserIcon>
    </van-col>
    <van-col span="12" class="title"><span>{{user.nick}}</span></van-col>
    <van-col span="6" class="subtitle"
      ><span>个人主页<van-icon name="arrow" /></span
    ></van-col>

    <van-col span="6" class="bodyCol"
      ><div><span>6</span><br />关注</div></van-col
    >
    <van-col span="6" class="bodyCol"
      ><div><span>3</span><br />粉丝</div></van-col
    >
    <van-col span="6" class="bodyCol"
      ><div><span>0</span><br />人气</div></van-col
    >
    <van-col span="6" class="bodyCol"
      ><div><span>3</span><br />钱钱</div></van-col
    >

    <van-col span="24">
      <van-row class="btnRow">
        <van-col span="12">
          <van-button class="btn">
            <van-icon name="bag" size="1.8rem" /> <span>我的购物</span><van-icon
              name="arrow"
              size="1.2rem" /></van-button
        ></van-col>

        <van-col span="12">
          <van-button class="btn"
            ><van-icon name="send-gift" size="1.8rem" /> <span>我的签到</span><van-icon
              name="arrow"
              size="1.2rem" /></van-button
        ></van-col>
      </van-row>
    </van-col>

    <AppGrid class="grid" :data="data1"></AppGrid>
    <AppGrid class="grid" :data="data2"></AppGrid>
    <AppGrid class="grid" :data="data3"></AppGrid>
  </van-row>

  <van-popup
    v-model:show="show"
    position="right"
    :style="{ height: '100%', width: '100%', backgroundColor: '#F8F8F8' }"
  >
    <van-nav-bar
      title="设置"
      left-text="返回"
      left-arrow
      @click-left="show = false"
    />
    <van-cell-group>
      <van-cell title="账户与安全" is-link />
      <van-cell title="黑名单" is-link />
      <van-cell title="推送设置" is-link />
      <van-cell title="隐私管理" is-link />
      <van-cell title="通用设置" is-link />
    </van-cell-group>
    <van-cell-group class="group">
      <van-cell title="家庭入驻" is-link />
      <van-cell title="社区入驻" is-link />
    </van-cell-group>

    <van-cell-group class="group">
      <van-cell title="清理缓存" is-link />
      <van-cell title="检测更新" is-link />
      <van-cell title="关于我们" is-link />
      <van-cell title="给个好评" is-link />
    </van-cell-group>

    <van-button type="danger" @click="outLog">退出登录</van-button>
  </van-popup>
  <!-- <van-popup v-model:show="show" position="right">
     <div class="body-div"> 内容</div>
      </van-popup> -->
</template>
<script setup lang="ts">
import AppGrid from "@/components/AppGrid.vue";
import { AppGridData } from "@/type/class/AppGridData";
import { ref } from "vue";
import { Dialog } from "vant";
import useUserStore from "@/store/modules/user";
import { storeToRefs } from 'pinia';
import   AppUserIcon from "@/components/AppUserIcon.vue";
const show = ref<boolean>(false);
let data1: AppGridData = {
  head: "个人中心",
  body: [
    {
      title: "我的消息",
      icon: "comment-o",
    },
    {
      title: "我的聊天",
      icon: "chat-o",
    },
    {
      title: "我的喜欢",
      icon: "like-o",
    },
    {
      title: "我的关注",
      icon: "user-o",
    },
  ],
};
let data2: AppGridData = {
  head: "功能",
  body: [
    {
      title: "排行榜",
      icon: "medal-o",
    },
    {
      title: "活动报名",
      icon: "balance-list-o",
    },
    {
      title: "钱钱兑换",
      icon: "gem-o",
    },
    {
      title: "全网上新",
      icon: "gift-card-o",
    },
  ],
};
let data3: AppGridData = {
  head: "服务",
  body: [
    {
      title: "客服",
      icon: "service-o",
    },
    {
      title: "小黑屋",
      icon: "wap-home-o",
    },
    {
      title: "邀请好友",
      icon: "friends-o",
    },
  ],
};
const userStore=useUserStore();
const {user}=storeToRefs(useUserStore());
const outLog = () => {
  Dialog.confirm({
    title: "提示",
    message: "确定退出当前用户吗？",
  })
    .then(() => {
      userStore
        .logOut()
        .then((response: any) => {
          location.href = "/";
        });
    })
    .catch(() => {
      // on cancel
    });
};

</script>
<style scoped>
.bodyCol
{
  color: #9B9B9B;
}
.bodyCol span{
  color: black;
  font-size:larger;
  font-weight: 500;
}
.btn .van-icon{
  color: #FF689B;
}
.btn span{
  font-size:medium;
  font-weight:600;
}
.grid {
  width: 100%;
  margin-top: 1rem;
}
.headRow {
  padding: 0.5rem 0.5rem 0.5rem 0.5rem;
}
.bodyRow {
  text-align: left;
  margin-top: 1.5rem;
  padding-left: 1.5rem;
  padding-right: 1.5rem;
  padding-bottom: 4rem;
}

.title {
  font-size: 1.5rem;
  line-height: 4rem;
}
.subtitle {
  line-height: 4rem;
  color: #9B9B9B;
}
.bodyCol {
  text-align: center;
  font-size: 1.2rem;
  margin-top: 1rem;
}
.btn {
  width: 100%;
  margin: 0;
  padding: 0;
  background-color: #ffffff;
  border: none;
  color: black;

}
.btnRow {
  margin-top: 1.5rem;
  box-shadow: 0rem 0rem 0.2rem 0.2rem #f3f3f3;
}
.btnRow .van-button {
  font-size: 1.2rem;
}
.van-icon-send-gift {
  margin-right: 0.5rem;
}
.btnRow .van-icon-arrow {
  margin-left: 0.45rem;
}
.van-cell {
  text-align: left;
}
.group {
  margin-top: 1rem;
}
.van-popup .van-button {
  width: 90%;
  margin-top: 2rem;
}
</style>