<template>
  <transition name="van-slide-right">
    <div v-show="visible">
      <van-sticky>
        <van-row class="head-row">
          <van-col span="3">
            <router-link to="/recommend">
              <van-icon name="arrow-left" size="1.5rem" />
            </router-link>
          </van-col>
          <van-col span="18"><span>发图文</span></van-col>
          <van-col
            span="3"
            @click="send"
            :style="{ color: isSend ? '#FE70A0' : '#979797' }"
            >发布</van-col
          >
        </van-row>
      </van-sticky>

      <van-cell-group>
        <van-field
          rows="5"
          autosize
          type="textarea"
          v-model="content"
          label-width="0"
          :show-word-limit="true"
          maxlength="500"
          placeholder="大于5字，每一天，都是为了下一天"
        />
      </van-cell-group>
      <van-row class="body-row">
        <van-col span="10">
          <van-icon name="share-o" size="1.5rem" /><span>发布到去其他</span>
        </van-col>
        <van-col span="4"></van-col>
        <van-col span="10"
          ><span class="right-span">选择更多人看到</span>
          <van-icon name="arrow" size="1.2rem" />
        </van-col>
      </van-row>

      <van-divider />
      <van-row>
        <van-col class="img-col" span="24">
          <van-uploader
            accept="image/*"
            :after-read="afterRead"
            v-model="fileList"
            multiple
          />
        </van-col>
      </van-row>
    </div>
  </transition>
</template>
<script setup lang="ts">
import { ref, onMounted, reactive, toRefs, watch } from "vue";
import { ArticleEntity } from "@/type/interface/ArticleEntity";
import fileApi from "@/api/fileApi";
import articleApi from "@/api/articleApi";
import { Toast } from "vant";
import { useRouter } from "vue-router";
const router = useRouter();
const form = reactive<any>({
  title: "",
  content: "",
  images: [],
  isDeleted: false,
});

const isSend = ref(false);
const { images, content } = toRefs(form);
const fileList = ref([]);
const visible = ref<boolean>(false);
onMounted(() => {
  visible.value = true;
});
const afterRead = (file: any) => {
  file.status = "uploading";
  file.message = "上传中...";
  var formData = new FormData();
  //一个文件
  if (file.length == undefined) {
    formData.append("file", file.file);
  } else {
    //多个文件
    file.forEach((f: any) => {
      formData.append("file", f.file);
      f.status = "uploading";
      f.message = "上传中...";
    });
    Toast({
      message: "全部文件正在上传",
      position: "bottom",
    });
  }

  fileApi.upload("image", formData).then((response: any) => {
    images.value.push(...response.data);

    if (file.length == undefined) {
      file.status = "done";
      file.message = "成功";
    } else {
      //多个文件
      file.forEach((f: any) => {
        f.status = "done";
        f.message = "成功";
      });
      Toast({
        message: "全部文件上传成功",
        position: "bottom",
      });
    }
  });
};

const send = () => {
  if (form.content.length < 5) {
    Toast({
      message: "请输入至少5个字符",
      position: "bottom",
    });
  } else {
    articleApi.add(form).then((response: any) => {
      router.push({ path: "/recommend" });
    });
  }
};

watch(
  () => form.content,
  (newValue, oldValue) => {
    if (newValue.length < 5) {
      isSend.value = false;
    } else {
      isSend.value = true;
    }
  }
);
</script>
<style scoped>
.head-row {
  background-color: #f8f8f8;

  padding: 1.2rem 1rem 0.8rem 1rem;
}

.head-row span {
  font-size: large;
}

.van-field-5-label {
  display: none;
}

.body-row {
  margin-top: 1rem;
}

.preview-cover {
  position: absolute;
  bottom: 0;
  box-sizing: border-box;
  width: 100%;
  padding: 4px;
  color: #fff;
  font-size: 12px;
  text-align: center;
  background: rgba(0, 0, 0, 0.3);
}

.van-uploader {
  margin: 0 1.2rem 0 1.2rem;
}

.img-col {
  text-align: left;
}
.right-span {
  color: #979797;
}
</style>