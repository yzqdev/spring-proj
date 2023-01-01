<template>
  <div>
    <div class="mycard animate__delay-0.8s">
      <Row
        class="animate__animated animate__delay-0.8s animate__fadeInDown"
        style="overflow: hidden"
      >
        <Col flex="1" style="padding: 8px">
          <Card
            class="cardShadow"
            :class="{ Hollowed: true }"
            @mouseover="mouseOver"
            @mouseleave="mouseLeave"
            style="
              margin-top: 10px;
              padding-left: 8px;
              padding-right: 8px;
              cursor: pointer;
            "
            @click.native="showUrlUploadMsg"
          >
            <div class="myupdiv">
              <img
                :src="linkImg"
                referrerpolicy="no-referrer"
                style="width: 45px"
                class="toolImg"
              />
              <p
                style="
                  font-size: 18px;
                  margin-top: 5px;
                  line-height: 32px;
                  font-size: 1vh;
                  color: #808794;
                  text-align: right;
                "
              >
                URL上传
              </p>
            </div>
          </Card>
        </Col>
        <Col flex="1" style="padding: 8px">
          <Card
            class="cardShadow"
            :class="{ Hollowed: false }"
            @mouseover="mouseOver"
            @mouseleave="mouseLeave"
            style="
              margin-top: 10px;
              padding-left: 8px;
              padding-right: 8px;
              cursor: pointer;
            "
            @click.native="showtermMsg"
          >
            <div class="myupdiv">
              <img :src="timeImg" class="toolImg" />
              <p
                style="
                  font-size: 18px;
                  margin-top: 5px;
                  line-height: 32px;
                  text-align: right;

                  color: #808794;
                "
              >
                <span v-if="data.day == 0">永久</span>
                <span v-else>{{ data.day }}天失效</span>
              </p>
            </div>
          </Card>
        </Col>
      </Row>
    </div>

    <Card
      id="box"
      class="
        mycard
        img_list_box
        animate__animated animate__delay-0.8s animate__fadeInUp
      "
      :style="{
        background:
          'linear-gradient(rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.1)), url(' +
          uploadImg +
          ')',
        minHeight: '260px',
        backgroundSize: '262px',
        backgroundRepeat: 'no-repeat',
      }"
      :class="{ Hollowed: false, testback: true }"
    >
      <div class="img_list_arr" style="text-align: center">
        <div
          class="demo-upload-list"
          v-for="(item, index) in uploadList"
          v-bind:key="item.uid"
        >
          <template v-if="item.status === 'finished'">
            <img
              style="cursor: pointer"
              preview="2"
              :data-pswp-uid="index"
              :preview-text="item.name"
              :src="item.response.data.url"
            />
            <div class="demo-upload-list-cover">
              <Icon
                type="md-checkmark-circle"
                style="margin: 0 5px"
                :style="{
                  color:
                    selectIndexUid.indexOf(item.uid) >= 0
                      ? '#43b984'
                      : '#c7c7c7',
                }"
                @click.native="clickImg(item.response.data.imgUid, item.uid)"
                title="选择"
              ></Icon>
              <Icon
                type="md-trash"
                style="margin: 0 5px"
                @click.native="handleRemove(item)"
                title="删除图像"
              ></Icon>
              <Icon
                type="md-git-branch"
                style="margin: 0 5px"
                @click.native="getImgLink(item)"
                title="分享链接"
              ></Icon>
            </div>
          </template>
          <template v-else>
            <Progress
              v-if="item.showProgress"
              :percent="item.percentage"
              hide-info
            ></Progress>
          </template>
        </div>
        <Upload
          ref="upload"
          :paste="true"
          :show-upload-list="false"
          :default-file-list="defaultList"
          :format="uploadInfo.suffix"
          :max-size="uploadInfo.filesize"
          :on-success="handleSuccess"
          :on-format-error="handleFormatError"
          :on-exceeded-size="handleMaxSize"
          :before-upload="handleBeforeUpload"
          :on-error="uploadError"
          :multiple="true"
          :data="data"
          :headers="headers"
          :action="this.$http.defaults.baseURL + '/upload'"
          type="drag"
          style="display: contents; width: 120px"
        >
          <div style="width: 120px; height: 118px; /*line-height: 118px;*/">
            <div
              style="
                width: 98px;
                height: 64px;
                margin: 0px auto;
                text-align: center;
                position: absolute;
                left: 0;
                right: 0;
                top: 30px;
              "
            >
              <Icon type="ios-camera" size="36"></Icon>
              <p
                style="text-align: center; color: #515a6e; font-size: xx-small"
              >
                拖拽图像到此处
              </p>
            </div>
          </div>
        </Upload>
        <AddAlbum :visible.sync="visible" :album-list="albumlist"></AddAlbum>

        <Modal v-model="IsImgLink" :footer-hide="true">
          <br />
          <List :split="false">
            <ListItem>
              <Input
                v-model="imgLinkForUrl"
                class="cobyOrderSn"
                data-clipboard-action="copy"
                :data-clipboard-text="imgLinkForUrl"
                @click.native="copy"
                ><span slot="prepend">U R L</span></Input
              >
            </ListItem>
            <ListItem>
              <Input
                v-model="imgLinkForHtml"
                class="cobyOrderSn"
                data-clipboard-action="copy"
                :data-clipboard-text="imgLinkForHtml"
                @click.native="copy"
                ><span slot="prepend">HTML</span></Input
              >
            </ListItem>
            <ListItem>
              <Input
                v-model="imgLinkForMD"
                class="cobyOrderSn"
                data-clipboard-action="copy"
                :data-clipboard-text="imgLinkForMD"
                @click.native="copy"
                ><span slot="prepend">Markdown</span></Input
              >
            </ListItem>
          </List>
        </Modal>

        <!--URL上传窗-->
        <Modal v-model="urlUploadMsg" :footer-hide="true">
          <br />
          <Form @submit.native.prevent>
            <FormItem>
              <Input
                v-model="imgUrl"
                prefix="ios-contact"
                placeholder="填写图片URL"
                size="large"
                style="width: 100%"
              />
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                :loading="loading"
                icon="md-cloud-upload"
                long
                shape="circle"
                @click="uploadForUrl"
                >UPLOAD</Button
              >
            </FormItem>
          </Form>
        </Modal>

        <!--上传期限-->
        <Modal v-model="termMsg" :footer-hide="true">
          <br />
          <Form @submit.native.prevent>
            <FormItem label="文件有效期">
              <Select
                v-model="data.day"
                style="width: 100px"
                @on-change="selectTerm"
              >
                <Option
                  v-for="item in dayList"
                  :value="item.value"
                  :key="item.value"
                  >{{ item.label }}</Option
                >
              </Select>
            </FormItem>
          </Form>
        </Modal>
      </div>
    </Card>
    <AddAlbum
        :visible="visible"
        :album-list="albumlist"
        @close="closeAddAlbum"
    ></AddAlbum>
  </div>
</template>

<script>
import { request } from "@/network/request";
import vueQr from "vue-qr";
import md5 from "js-md5";
import axios from "axios";
import AddAlbum from "@/views/admin/AddAlbum";
export default {
  metaInfo() {
    return {
      title:
        this.$store.state.metaInfo.webname +
        this.$store.state.metaInfo.splitline +
        this.$store.state.metaInfo.websubtitle, // set a title
      meta: [
        // set meta
        {
          name: "keyWords",
          content: this.$store.state.metaInfo.keywords,
        },
        {
          name: "description",
          content: this.$store.state.metaInfo.description,
        },
      ],
      link: [
        {
          rel: "asstes",
          href: "http://www.hellohao.cn/",
        },
      ],
    };
  },

  name: "upload",
  props: {
    msg: String,
  },
  data() {
    return {
      uploadImg: require("@/assets/img/uploadImg.png"),
      linkImg: require("@/assets/img/link.png"),
      timeImg: require("@/assets/img/time.png"),
      isToolImgStyle: "width: 50px;",
      loading: false,
      urlUploadMsg: false,
      termMsg: false, //上传期限的弹窗
      imgUrl: null,
      uploadInfo: {
        suffix: null,
        filesize: null,
        imgcount: null,
      },
      isAlbum: false,
      ViewURL: null,
      albumData: {},
      headers: {
        usersOrigin: md5(
          window.location.protocol + "//" + window.location.host
        ),
        Authorization: localStorage.getItem("Authorization"),
      },
      data: {
        day: 0,
        classifications: null,
      },
      imgListShow: "show",
      selectIndex: [],
      selectIndexUid: [], //这个uid是系统自己生成的一个数字串，仅用于勾选上传图片使用，防止重复图片选一个 都是同一个imguid的问题
      selectImg: [],
      defaultList: [],
      imgName: "",
      visible: false,
      IsImgLink: false,
      uploadList: [],
      // imgLinkForShortLink:null,
      imgLinkForUrl: null,
      imgLinkForMD: null,
      imgLinkForHtml: null,
      images: [],
      imgIndex: null,
      albumlist: [],
      dayList: [
        { label: "永久", value: 0 },
        { label: "1天", value: 1 },
        { label: "3天", value: 3 },
        { label: "7天", value: 7 },
        { label: "30天", value: 30 },
      ],
      IsShowtagMsg: false,
      tagInfo: null,
      tempNum: 0, //验证是否提示过单次上传个数弹窗
    };
  },
  methods: {
    created() {
      this.imgListShow = "show";
    },
    closeAddAlbum(visible) {
      this.visible = visible;
    },
    handleRemove(file) {
      this.$Modal.confirm({
        title: "确认删除",
        content: "<p>您确认删除该图像吗</p>",
        onOk: () => {
          this.$Spin.show();

          request("/deleImagesByUid/" + file.response.data.imguid)
            .then((res) => {
              this.$Spin.hide();
              if (res.status == 200) {
                if (res.data.code == "200") {
                  this.$Message.success(res.data.info);
                  const fileList = this.$refs.upload.fileList;
                  this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
                } else {
                  this.$Message.error(res.data.info);
                }
              } else {
                this.$Message.error("请求时出现错误");
              }
            })
            .catch((err) => {
              this.$Spin.hide();
              console.log(err);
              this.$Message.error("服务器请求错误");
            });
        },
        onCancel: () => {
          // this.$Message.info('已取消删除');
        },
      });
    },
    handleSuccess(res, file) {
      this.tempNum = 0;
      if (res.code != "200") {
        const fileList = this.$refs.upload.fileList;
        this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
        this.$Message.error(res.info);
        return false;
      }
      if (res.length > 0) {
        this.images.push(res[0]);
      }
      let urlText = this.$store.state.copyAllUrl + res.data.url + "\n";
      this.$store.commit("setCopyAllUrl", urlText);
      this.$emit("showBtn", this.uploadList);
      const box = document.getElementById("box");
      box.scrollTop = box.scrollHeight;
    },
    handleFormatError(file) {
      this.$Notice.warning({
        title: "文件格式不正确",
        desc: file.name + " 的文件格式不受支持.",
      });
    },
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "超过图像大小限制",
        desc:
          "文件: " +
          file.name +
          " 太大, 不能超过 " +
          this.uploadInfo.filesize +
          " K",
      });
    },
    handleBeforeUpload() {
      const check = this.uploadList.length < this.uploadInfo.imgcount;
      if (!check) {
        if (this.tempNum == 0) {
          this.$Notice.warning({
            name: "imgcount",
            duration: 0,
            onClose: this.setImgCountMSG,
            title: "页面上传图像过多",
            desc:
              "系统限制了页面的上传图像数量，上传图像不得超过" +
              this.uploadInfo.imgcount +
              "个图像",
          });
          this.tempNum = 1;
        }
      }
      return check;
    },
    setImgCountMSG() {
      this.tempNum = 0;
    },
    uploadError() {
      console.log("图片上传出错");
    },
    allSett() {
      if (this.selectIndex.length > 0) {
        this.getAlbumImgList();
        this.visible = true;
      } else {
        this.$Message.info("请选择要操作的图片");
      }
    },
    clickImg(key, uid) {
      if (!this.selectIndexUid.includes(uid)  ) {
        this.selectIndex.push(key);
        this.selectIndexUid.push(uid);
      } else {
        this.selectIndex.splice(this.selectIndex.indexOf(key), 1);
        this.selectIndexUid.splice(this.selectIndexUid.indexOf(uid), 1);
      }
    },
    uploadForUrl() {
      if (this.imgUrl != null && this.imgUrl != "") {
        if (
          this.imgUrl.indexOf("http") != -1 &&
          this.imgUrl.indexOf("://") != -1 &&
          this.imgUrl.indexOf(".") != -1
        ) {
          this.loading = true;
          this.sendUploadForUrl();
        } else {
          this.$Message.warning("请输入正确格式的链接地址");
        }
      } else {
        this.$Message.warning("请先输入图像链接");
      }
    },
    sendUploadForUrl() {
      var param = {
        day: 0,
        imgUrl: this.imgUrl,
      };
      request("/uploadForUrl", param)
        .then((res) => {
          this.loading = false;
          this.urlUploadMsg = false;
          if (res.status == 200) {
            if (res.data.code == "200") {
              var json = {
                status: "finished",
                name: "link.png",
                size: 0,
                percentage: 100,
                uid: this.getUuid(14, 14),
                showProgress: false,
                response: res.data,
                exceptions: null,
              };
              this.$refs.upload.fileList.push(json);
              let urlText = this.$store.state.copyAllUrl + res.data.url + "\n";
              this.$store.commit("setCopyAllUrl", urlText);
              this.$emit("showBtn", this.uploadList);
              this.$Message.success("上传成功");
            } else {
              this.$Message.warning(res.data.info);
            }
          } else {
            this.$Message.error("请求时出现错误");
          }
        })
        .catch((err) => {
          this.$Spin.hide();
          this.loading = false;
          this.urlUploadMsg = false;
          console.log(err);
          this.$Message.error("服务器请求错误");
        });
    },
    //获取选中的画廊图片信息
    getAlbumImgList() {
      let param = this.selectIndex;

      request("/getAlbumImgList", param)
        .then((res) => {
          this.$Spin.hide();
          if (res.status == 200) {
            var json = res.data.data;
            this.albumlist = json;
          } else {
            this.$Message.error("请求时出现错误");
          }
        })
        .catch((err) => {
          this.$Spin.hide();
          console.log(err);
          this.$Message.error("服务器请求错误");
        });
    },
    getUploadInfo() {
      axios
        .get("/getUploadInfo")
        .then((res) => {
          this.$Spin.hide();
          if (res.status == 200) {
            var json = res.data.data;
            if (json) {
              this.uploadInfo = json;
            } else {
              this.$Message.error("获取当前上传信息时出现错误");
            }
          } else {
            this.$Message.error("获取当前上传信息时出现错误");
          }
        })
        .catch((err) => {
          this.$Spin.hide();
          console.log(err);
          this.$Message.error("服务器请求错误");
        });
    },
    getImgLink(img) {
      var host = window.location.host;
      this.imgLinkForUrl = img.response.data.url;
      this.imgLinkForHtml =
        '<img src="' + img.response.data.url + '" alt="' + img.name + '" />';
      this.imgLinkForMD = "![" + img.name + "](" + img.response.data.url + ")";
      this.IsImgLink = true;
    },
    copyBtn() {
      var e = document.createEvent("MouseEvents");
      e.initEvent("click", true, true);
      document.getElementById("idCopyAll").dispatchEvent(e);
    },
    copy() {
      var clipboard = new this.clipboard(".cobyOrderSn");
      clipboard.on("success", (e) => {
        this.$Message.success("复制成功");
        // 释放内存
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        // 不支持复制
        this.$Message.error("该浏览器不支持自动复制");
        // 释放内存
        clipboard.destroy();
      });
    },
    returnData(data) {
      this.visible = false;
      this.albumData = data;
      if (this.albumData.password == null || this.albumData == "") {
        this.albumData.password = "无";
      }
      this.albumData.url =
        window.location.protocol +
        "//" +
        window.location.host +
        "/h/" +
        data.url;
      this.isAlbum = true;
    },
    showUrlUploadMsg() {
      this.urlUploadMsg = true;
    },
    showtermMsg() {
      this.termMsg = true;
    },

    selectTerm(value) {
      this.termMsg = false;
      if (value == 0) {
        this.$Notice.info({
          title: "您之后上传的数据将会永久保存",
        });
      } else {
        this.$Notice.info({
          title: "您之后上传的数据将会在" + value + "天后过期",
        });
      }
    },
    // 移入
    mouseOver() {
      this.isToolImgStyle =
        "width: 50px;\n" +
        "  -webkit-transition:all 0.5s ease 0s;\n" +
        "  -moz-transition:all 0.5s ease 0s;\n" +
        "  transition:all 0.5s ease 0s;";
    },
    // 移出
    mouseLeave() {
      this.isToolImgStyle = "width: 50px;\n";
    },
    getUuid(len, radix) {
      var chars =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split(
          ""
        );
      var uuid = [],
        i;
      radix = radix || chars.length;

      if (len) {
        for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random() * radix)];
      } else {
        var r;
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
        uuid[14] = "4";

        for (i = 0; i < 36; i++) {
          if (!uuid[i]) {
            r = 0 | (Math.random() * 16);
            uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
          }
        }
      }
      return uuid.join("");
    },
  },
  mounted() {
    this.$Spin.show();
    this.getUploadInfo();
    this.uploadList = this.$refs.upload.fileList;
  },
  components: {
    AddAlbum,
    vueQr,
    Treeselect,
  },
  computed: {},
};
</script>

<style scoped>
.ivu-upload-drag {
  background: rgba(255, 255, 255, 0);
  border: 1px dashed #dcd9d9;
}
</style>

<style>
/*.ivu-upload-drag{*/
/*  background: rgba(255, 255, 255, 0);*/
/*  border: 1px dashed #dcd9d9;*/
/*}*/

.demo-upload-list {
  line-height: 28px;
  width: 120px;
  height: 120px;
  box-shadow: 0 2px 9px 0 rgba(0, 0, 0, 0.1);
  background-image: linear-gradient(147deg, #fffcf9, #cde8f6);
}
.demo-upload-list-cover {
  display: block;
  background: rgba(0, 0, 0, 0.5);
  height: 25px;
}
.select-img-list {
  position: absolute;
  top: 92px;
  width: 65px;
  height: 30px;
  transform: rotate(-45deg);
  -ms-transform: rotate(-45deg);
  left: 73px;
  background: #57a3f3;
}

.select-img-icon {
  position: absolute;
  top: 4px;
  left: 26px;
  color: #fff;
  transform: rotate(37deg);
  -ms-transform: rotate(37deg);
  font-weight: 600;
  font-size: large;
}
.demo-upload-list-cover i {
  color: rgba(255, 255, 255, 0.6);
  font-size: 18px;
}
.demo-upload-list-cover i:hover {
  color: #fff;
}
.mycard {
  max-height: 600px;
  margin: 25px auto;
  width: 55%;
  min-width: 40vh;
  overflow-y: auto;
}

.ivu-modal-header p,
.ivu-modal-header-inner {
  width: 85%;
  font-size: xx-small;
}

.ivu-avatar {
  width: 42px;
  height: 42px;
  border-radius: 5px;
}

.ivu-card-bordered {
  border: 0;
  border-color: #e8eaec;
  box-shadow: 0 2px 5px 1px rgba(64, 60, 67, 0.16);
}

.demo::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

.demo {
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
  overflow-x: hidden;
  overflow-y: auto;
}
.infotitle {
  font-size: 16px;
}
.testback {
  background: linear-gradient(
      rgba(255, 255, 255, 0.1),
      rgba(255, 255, 255, 0.1)
    ),
    url("http://img.wwery.com/Hellohao/386f20802054547.png");
  /*background-position: 100% 100%;这个是按从左往右，从上往下的百分比位置进行调整*/
  background-size: 300px; /*按比例缩放*/
  /*background-size: 100px 100px;!*这个是按数值缩放*!*/
  background-repeat: no-repeat; /*还有repeat-x,y等*/
}
.myupdiv {
  text-align: center;
  height: 80px;
  /*padding: 10px;*/
  /*font-weight: bold;*/
  min-width: 65px;
  -webkit-transition: all 0.5s ease 0s;
  -moz-transition: all 0.5s ease 0s;
  transition: all 0.5s ease 0s;
}
.toolImg {
  width: 50px;
  -webkit-transition: all 0.5s ease 0s;
  -moz-transition: all 0.5s ease 0s;
  transition: all 0.5s ease 0s;
}
.myupdiv:hover {
  opacity: 0.5;
  /*margin-top: 10px;*/
  -webkit-transition: all 0.5s ease 0s;
  -moz-transition: all 0.5s ease 0s;
  transition: all 0.5s ease 0s;
}
</style>
