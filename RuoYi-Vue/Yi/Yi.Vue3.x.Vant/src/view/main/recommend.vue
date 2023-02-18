<template>
  <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
    <van-list
      class="list"
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <van-row v-for="(item, index) in articleList" :key="index" class="row">
        <van-col span="4" class="leftCol">
          <AppUserIcon
            width="3rem"
            height="3rem"
            :src="item.user == null ? null : item.user.icon"
          />
        </van-col>

        <van-col span="14" class="centerTitle">
          <span class="justtitle">{{
            item.user == null ? "-" : item.user.nick ?? item.user.username
          }}</span>
          <br />
          <app-createTime :time="item.createTime" />
        </van-col>

        <van-col span="6" class="down">
          <van-icon name="arrow-down" @click="show = true" />
        </van-col>

        <van-col class="rowBody" span="24">{{ item.content }}</van-col>

        <van-col
          span="8"
          v-for="(image, imageIndex) in item.images"
          :key="imageIndex"
          class="imageCol"
          @click="openImage(item.images, imageIndex)"
        >
          <van-image
            lazy-load
            fit="cover"
            width="100%"
            height="7rem"
            :src="url + image + '/true'"
            radius="5"
          />
          <template v-slot:loading>
            <van-loading type="spinner" size="20" />
          </template>
        </van-col>

        <van-col span="24" class="bottomRow">
          <van-grid direction="horizontal" :column-num="3">
            <van-grid-item icon="share-o" text="分享" />
            <van-grid-item
              icon="comment-o"
              text="评论"
              @click="openComment(item.id)"
            />
            <van-grid-item
              icon="good-job-o"
              :text="`点赞:${item.agreeNum}`"
              @click="aggreeHand(item.id)"
            />
          </van-grid>
        </van-col>
      </van-row>
    </van-list>
  </van-pull-refresh>
  <!-- 功能页面 -->
  <van-action-sheet
    v-model:show="show"
    :actions="actions"
    cancel-text="取消"
    close-on-click-action
  />

  <!-- 图片预览 -->
  <van-image-preview
    v-model:show="imageShow"
    :images="imagesPreview"
    :startPosition="startIndex"
    @change="onChange"
    :closeable="true"
  >
    <template v-slot:index>第{{ index + 1 }}页</template>
  </van-image-preview>

  <!-- 评论面板 -->
  <van-action-sheet v-model:show="commentShow" title="共10条评论">
    <van-row v-for="i in commentList" :key="i" class="commentContent">
      <van-col span="4">头像</van-col>
      <van-col span="16">{{ i.content }}</van-col>
      <van-col span="4">点赞</van-col>
    </van-row>

    <van-cell-group inset>
      <van-field v-model="commentData.content"  placeholder="请输入评论" >
        <template #button>
      <van-button size="small" type="primary" @click="sendComment()">发布</van-button>
    </template>
      </van-field>
    </van-cell-group>
  </van-action-sheet>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, toRefs } from "vue";
import { ImagePreview, Toast } from "vant";
import AppCreateTime from "@/components/AppCreateTime.vue";
import AppUserIcon from "@/components/AppUserIcon.vue";
import articleApi from "@/api/articleApi";
import agreeApi from "@/api/agreeApi";
import commentApi from "@/api/commentApi";
import { ArticleEntity } from "@/type/interface/ArticleEntity";
const VanImagePreview = ImagePreview.Component;
const url = `${import.meta.env.VITE_APP_BASE_API}/file/`;
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    // dictName: undefined,
    // dictType: undefined,
    isDeleted: false,
  },
});

const commentData = reactive({
  content: "",
  articleId:0
});
const sendComment=()=>{
 commentData.articleId=openCommentId.value;
  commentApi.add(commentData).then(()=>{
    getCommentList(openCommentId.value);
    commentData.content="";
  })
}

const { queryParams } = toRefs(data);
const {content}=toRefs(commentData);
const articleList = ref<any[]>([]);
const commentList = ref<any[]>([]);
const totol = ref<Number>(0);
const imageShow = ref(false);
const commentShow = ref<any>(false);
const index = ref(0);
let imagesPreview = ref<string[]>([]);
const openCommentId=ref(0);
const openComment = (id: any) => {
  commentShow.value = true;
  openCommentId.value=id;
  getCommentList(id);
};

const onChange = (newIndex: any) => {
  index.value = newIndex;
};

const list = ref<Number[]>([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const startIndex = ref(0);
const show = ref(false);
const actions = [{ name: "取消关注" }, { name: "将TA拉黑" }, { name: "举报" }];

const onLoad = async () => {
  if (refreshing.value) {
    articleList.value = [];
    refreshing.value = false;
  }
  // 异步更新数据
  // setTimeout 仅做示例，真实场景中一般为 ajax 请求
  articleApi.pageList(queryParams.value).then((response: any) => {
    if (response.data.data.length == 0) {
      console.log("结束");
      finished.value = true;
    } else {
      console.log("执行");
      articleList.value.push(...response.data.data);
      totol.value = response.data.totol;
      queryParams.value.pageNum += 1;
    }

    loading.value = false;

    console.log(loading.value);
  });
};
const onRefresh = () => {
  finished.value = false;

  // 重新加载数据
  // 将 loading 设置为 true，表示处于加载状态
  loading.value = true;
  queryParams.value.pageNum = 1;
  onLoad();
};
const openImage = (imagesUrl: string[], imageIndex: any) => {
  imagesPreview.value = imagesUrl.map((i) => url + i);
  startIndex.value = imageIndex;
  imageShow.value = true;
};
onMounted(() => {
  articleList.value = [];
  // getList();
});

const getCommentList = (id: any) => {
  commentApi.getListByArticleId(id).then((response: any) => {
    commentList.value = response.data;
  });
};

const getList = () => {
  articleApi.pageList(queryParams.value).then((response: any) => {
    articleList.value.push(...response.data.data);
    totol.value = response.data.totol;
  });
};
const aggreeHand = (articleId: any) => {
  agreeApi.operate(articleId).then((response: any) => {
    //更改显示的值
    if (response.status) {
      articleList.value.filter((p) => p.id == articleId)[0].agreeNum += 1;
    } else {
      articleList.value.filter((p) => p.id == articleId)[0].agreeNum -= 1;
    }
    Toast({
      message: response.message,
      position: "bottom",
    });
  });
};
</script>
<style scoped>
.list {
  background-color: #f4f4f4;
}

.row {
  background-color: white;
  padding-top: 1rem;
  padding-left: 1rem;
  padding-right: 1rem;
  margin-bottom: 0.6rem;
}

.rowBody {
  text-align: left;
  background-color: white;

  margin-top: 1rem;
  margin-bottom: 1rem;
}

.title {
  padding-top: 1rem;

  min-height: 3rem;
  text-align: left;
}

.leftCol {
  align-content: left;
  text-align: left;
}

.centerTitle {
  text-align: left;
}

.imageCol {
  padding: 0.1rem 0.1rem 0.1rem 0.1rem;
}

.subtitle {
  color: #cbcbcb;
}

.justtitle {
  font-size: large;
}

.bottomRow {
  color: #999999;
}

.down {
  text-align: right;
  padding-right: 0.5rem;
}

.commentContent {
  margin-bottom: 4rem;
}
</style>