<template>
  <Layout style="margin-top: 50px">
    <Drawer
      title="图像类别"
      :closable="false"
      v-model="treePopup"
      :width="screenWidth <= 368 ? screenWidth : 368"
    >
      <Form @submit.native.prevent style="margin-top: 30px">
        <FormItem label="存储源">
          <Select
            v-model="searchbucket"
            filterable
            clearable
            placeholder="存储源(默认全部)"
          >
            <Option
              v-for="item in bucketlist"
              :value="item.storageType"
              :key="item.id"
              >{{ item.keyName }}</Option
            >
          </Select>
        </FormItem>
        <FormItem label="起始日期">
          <DatePicker
            @on-change="startDateChange"
            format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            split-panels
            placeholder="起始日期段(默认不限)"
          ></DatePicker>
        </FormItem>
        <FormItem label="结束日期">
          <DatePicker
            @on-change="stopDateChange"
            format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            split-panels
            placeholder="结束日期段(默认当前日期)"
          ></DatePicker>
        </FormItem>
        <FormItem label="用户名">
          <Input v-model="searchtext" placeholder="填写用户名">
            <Select v-model="searchType" slot="prepend" style="width: 100px">
              <Option value="1">包含</Option>
              <Option value="0">排除</Option>
            </Select>
          </Input>
        </FormItem>
        <FormItem label="图像类型">
          <CheckboxGroup>
            <Checkbox label="违规图片" v-model="violation" border></Checkbox>
          </CheckboxGroup>
        </FormItem>
      </Form>

      <div
        style="
          width: 100%;
          height: 55px;
          position: absolute;
          bottom: 0;
          left: 0;
          text-align: right;
          padding: 10px;
        "
      >
        <div style="width: 75px; display: inline-block">
          <Button shape="circle" @click="treePopup = false">取消</Button>
        </div>
        <div style="width: 75px; display: inline-block">
          <Button type="primary" shape="circle" @click="tosearch">检索</Button>
        </div>
      </div>
    </Drawer>

    <!--        minHeight: '500px'-->
    <Content :style="{ margin: '15px 5px 0' }">
      <viewer :images="imglist">
        <p style="position: fixed; right: 30px; z-index: 1; bottom: 68px">
          <!--              @click="searchimg" -->
          <Button
            type="primary"
            shape="circle"
            icon="ios-search"
            style="
              z-index: 1;
              margin-right: 8px;
              box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 6px 0px;
            "
            @click.native="treePopup = true"
            >筛选
          </Button>
          <Dropdown
            trigger="click"
            style="
              z-index: 1;
              box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 6px 0px;
              border-radius: 50%;
            "
          >
            <Button type="primary" shape="circle">
              操作
              <Icon type="ios-arrow-down"></Icon>
            </Button>
            <DropdownMenu slot="list">
              <DropdownItem @click.native="addToAlbum">
                <Icon type="md-checkmark-circle" size="16" />
                添加到相册
              </DropdownItem>
              <DropdownItem @click.native="selectAll">
                <Icon type="md-checkmark-circle" size="16" />
                全部选中
              </DropdownItem>
              <DropdownItem @click.native="noselectAll">
                <Icon type="md-checkmark-circle-outline" size="16" />
                取消选中
              </DropdownItem>
              <DropdownItem @click.native="delSelectImg">
                <Icon type="md-trash" size="16" />
                删除选中
              </DropdownItem>
              <DropdownItem divided @click.native="showViewType">
                <Icon type="md-eye" size="16" />
                {{ viewType == 1 ? "小图模式" : "大图模式" }}
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </p>

        <Row class="animate__animated animate__fadeIn animate__delay-1.5s">
          <Col flex="1" v-for="(item, index) in imglist" :key="index">
            <div
              class="imgdivstyle"
              :class="[viewType == 1 ? 'divimgstyle-max' : 'divimgstyle-min']"
            >
              <span class="formatTag">{{
                item.imgUrl.substr(item.imgUrl.lastIndexOf("\.") + 1)
              }}</span>
              <img
                :class="[viewType == 1 ? 'imgstyle-max' : 'imgstyle-min']"
                class="imgstyle"
                style="cursor: pointer"
                :src="item.imgUrl + ''"
              />
              <!--                <img :class="[viewType==1?'imgstyle-max':'imgstyle-min']"  class="imgstyle" style="cursor:pointer;" :src="'666'"  onerror="this.src='http://tc.hellohao.cn/img/img404.jpg'" >-->
              <div
                class="img-tool-cover"
                :style="{ bottom: toolBottom + 'px' }"
              >
                <Icon
                  style="cursor: pointer"
                  @click.native="selectImgs(item)"
                  :type="
                    selectIndex.indexOf(item.id) > -1
                      ? 'ios-checkmark-circle'
                      : 'ios-checkmark-circle-outline'
                  "
                  :class="{ icostylecolor: selectIndex.indexOf(item.id) > -1 }"
                  class="icostyle"
                  title="选择"
                ></Icon>
                <Icon
                  style="cursor: pointer"
                  type="md-link icostyle cobyOrderSn"
                  title="链接"
                  data-clipboard-action="copy"
                  :data-clipboard-text="item.imgUrl"
                  @click.native="copy"
                />
                <Icon
                  style="cursor: pointer"
                  type="md-trash icostyle"
                  @click.native="delImg(item.id, index)"
                  title="删除"
                ></Icon>
                <Icon
                  style="cursor: pointer"
                  :color="item.violation == null ? '' : 'rgb(228 102 70)'"
                  type="md-information-circle icostyle"
                  @click.native="imgInfo(item)"
                  title="信息"
                ></Icon>
              </div>
            </div>
          </Col>
        </Row>

        <div
          style="width: 100%; text-align: center; color: #797b7f"
          v-show="noImgMsg"
        >
          <Icon type="ios-filing" size="56" />
          <p>当前未找到任何图像</p>
        </div>
      </viewer>
      <div class="example-code-more">
        <Button
          type="dashed"
          :loading="nextButloading"
          @click="selectPhoto"
          :disabled="btntext == '所有数据加载完毕'"
          long
        >
          {{ btntext }}
        </Button>
      </div>
    </Content>
    <!--筛选窗-->
    <Modal v-model="issearchimg" :footer-hide="true">
      <br />
      <Form @submit.native.prevent>
        <FormItem>
          <Row>
            <Col span="11">
              <Select
                v-model="searchbucket"
                filterable
                clearable
                placeholder="存储源(默认全部)"
              >
                <Option
                  v-for="item in bucketlist"
                  :value="item.storageType"
                  :key="item.id"
                  >{{ item.keyname }}</Option
                >
              </Select>
            </Col>
            <Col span="2" style="text-align: center"></Col>
            <Col span="11">
              <!--                  <DatePicker type="daterange" @on-change="setsearchdate" split-panels placeholder="日期段(默认不限)"  separator="~"></DatePicker>-->
            </Col>
          </Row>
        </FormItem>
        <FormItem>
          <Input v-model="searchtext" placeholder="填写用户名">
            <Select v-model="searchType" slot="prepend" style="width: 100px">
              <Option value="1">包含</Option>
              <Option value="0">排除</Option>
            </Select>
          </Input>
        </FormItem>
        <FormItem>
          <Button
            type="primary"
            icon="ios-search"
            long
            shape="circle"
            @click="tosearch"
            >SEARCH</Button
          >
        </FormItem>
      </Form>
    </Modal>

    <!--详细信息-->
    <Modal v-model="isimginfo" :footer-hide="true">
      <List :split="false">
        <ListItem
          ><span
            style="
              text-overflow: ellipsis;
              white-space: nowrap;
              overflow: hidden;
            "
            ><Icon
              style="font-size: 32px"
              type="md-image"
            />&nbsp;&nbsp;&nbsp;<span style="font-size: 18px">{{
              images == null ? "暂缺数据" : images.imgname
            }}</span></span
          ></ListItem
        >
      </List>

      <Tabs>
        <TabPane label="图像信息" icon="ios-images">
          <div style="line-height: 32px; margin-bottom: 40px">
            <p>
              <span class="infotitle"> 文件大小：</span
              ><span style="font-size: 14px">{{
                images == null
                  ? "暂缺数据"
                  : images.sizes > 0
                  ? this.formatBytes(images.sizes, 2)
                  : ""
              }}</span>
            </p>
            <p>
              <span class="infotitle"> 文件类型：</span
              ><span style="font-size: 14px">{{
                images == null ? "未知" : images.format
              }}</span>
            </p>
            <p>
              <span class="infotitle"> 来源IP：</span
              ><span style="font-size: 14px">{{
                images == null ? "暂缺数据" : images.abnormal
              }}</span>
            </p>
            <p>
              <span class="infotitle"> 上传日期：</span
              ><span style="font-size: 14px">{{
                images == null
                  ? "暂缺数据"
                  : images.updateTime
                  ? images.updateTime
                  : ""
              }}</span>
            </p>
            <p>
              <span class="infotitle"> 上传者：</span
              ><span style="font-size: 14px">{{ upName }}</span>
            </p>
            <p>
              <span class="infotitle"> 所属存储源：</span
              ><span style="font-size: 14px">{{ bucketName }}</span>
            </p>
            <p>
              <span class="infotitle|"> 存储性质：</span
              ><span style="font-size: 14px">{{
                images == null
                  ? "暂缺数据"
                  : images.imgType == 0
                  ? "持久"
                  : "暂存"
              }}</span>
            </p>
          </div>
          <p
            style="
              color: rgb(228 102 70);
              font-size: 12px;
              font-weight: 200;
              position: absolute;
              bottom: 10px;
              display: block;
              left: 0;
              width: 100%;
              z-index: 1;
            "
            v-show="isViolation.isControl"
          >
            {{ isViolation.info }}
          </p>
          <div class="QRCodestyle">
            <vue-qr
              :text="
                images
                  ? images.imgUrl
                    ? images.imgUrl
                    : '未获取到文件信息'
                  : '未获取到文件信息'
              "
              :size="160"
            ></vue-qr>
          </div>
        </TabPane>
      </Tabs>
    </Modal>
    <AddAlbum
      :visible="visible"
      :album-list="albumList"
      @close="closeAddAlbum"
    ></AddAlbum>

    <Footer class="layout-footer-center"
      >{{ this.$store.state.metaInfo.webname }} &copy; Control Panel</Footer
    >
  </Layout>
</template>

<script>
import { request } from "@/network/request";
import vueQr from "vue-qr";
import AddAlbum from "@/views/admin/AddAlbum";

export default {
  name: "photo",
  data() {
    return {
      albumData: {},
      isAlbum: false,
      screenWidth: document.body.clientWidth,
      httpText: window.location.protocol,
      hostText: window.location.host,
      viewType: 1, //1-大图模式 2-小图
      upName: null,
      images: null,
      isViolation: {
        isControl: false,
        info: "此图像可能存在违规内容 建议删除",
      },
      issearchimg: false,
      isimginfo: false,
      imglist: [],
      pageNum: 1,
      pageSize: 20,
      selectType: 2,
      type: "picture",
      selectIndex: [],
      selectImgUrl: [],
      bucketName: null,
      bucketlist: [],
      searchType: "1", //查询的文本类型
      searchtext: null, //查询的文本
      searchbucket: "",
      searchStartDate: null,
      searchStopDate: null,
      submitData: [],
      toolBottom: 5,
      nextButloading: false,
      btntext: "加载更多",
      treePopup: false,
      violation: false,
      moveImgLoading: false,
      noImgMsg: false, //没有图像的时候提示
      visible: false,
      albumList: [],
    };
  },
  methods: {
    closeAddAlbum(visible) {
      this.visible = visible;
    },
    selectPhoto() {
      this.issearchimg = false;
      // this.$Spin.show();
      this.nextButloading = true;
      var paramJson = {};
      paramJson.pageNum = this.pageNum;
      paramJson.pageSize = this.pageSize;
      paramJson.selectType = this.searchType;
      paramJson.username = this.searchtext;
      paramJson.source = this.searchbucket;
      paramJson.startTime =
        this.searchStartDate == "" ? null : this.searchStartDate;
      paramJson.stopTime =
        this.searchStopDate == "" ? null : this.searchStopDate;
      paramJson.violation = this.violation;

      request("/admin/selectPhoto", paramJson)
        .then((res) => {
          if (res.status == 200) {
            if (res.data.code == "200") {
              var arr = res.data.data.rows;
              this.nextButloading = false;
              this.treePopup = false;
              if (arr.length > 0) {
                this.imglist = this.imglist.concat(arr);
                this.pageNum++;
                if (this.imglist.length < this.pageSize) {
                  this.btntext = "所有数据加载完毕";
                } else {
                  this.btntext = "加载更多";
                }
              } else {
                this.btntext = "所有数据加载完毕";
              }
              if (this.imglist.length == 0) {
                this.noImgMsg = true;
              }
            } else {
              this.$Message.warning(res.data.info);
            }
          } else {
            this.$Message.error("请求时出现错误");
          }
        })
        .catch((err) => {
          console.log(err);
          this.$Message.error("服务器请求错误");
        });
    },
    selectImgs(item) {
      if (this.selectIndex.indexOf(item.id) == -1) {
        this.selectIndex.push(item.id);
      } else {
        this.selectIndex.splice(this.selectIndex.indexOf(item.id), 1);
      }

      if (this.selectImgUrl.indexOf(item.imgUrl) == -1) {
        this.selectImgUrl.push(item.imgUrl);
      } else {
        this.selectImgUrl.splice(this.selectImgUrl.indexOf(item.imgUrl), 1);
      }
    },
    addToAlbum() {
      let param = this.selectIndex;

      request("/getAlbumImgList", param)
        .then((res) => {
          if (res.status == 200) {
            var json = res.data.data;
            console.log(
              `%c这是addto album`,
              `color:red;font-size:16px;background:transparent`
            );

            this.albumList = json;
            console.log(json);
            this.visible = true;
          } else {
            this.$Message.error("请求时出现错误");
          }
        })
        .catch((err) => {
          console.log(err);
          this.$Message.error("服务器请求错误");
        });
    },
    selectAll() {
      this.selectIndex = [];
      this.selectImgUrl = [];
      for (let i = 0; i < this.imglist.length; i++) {
        this.selectIndex.push(this.imglist[i].id);
        this.selectImgUrl.push(this.imglist[i].imgUrl);
      }
      this.$Message.success("已选中" + this.selectIndex.length + "张图像");
    },
    noselectAll() {
      if (this.selectIndex.length > 0) {
        this.selectIndex = [];
        this.$Message.success("已清空所选图像");
      } else {
        this.$Message.warning("你还没有选择图像哦");
      }
    },
    searchimg() {
      this.issearchimg = true;
    },
    startDateChange(e) {
      console.log(e);
      this.searchStartDate = e;
    },
    stopDateChange(e) {
      console.log(e);
      this.searchStopDate = e;
    },
    //检测两个日期大小
    compareDate(checkStartDate, checkEndDate) {
      var arys1 = new Array();
      var arys2 = new Array();
      if (checkStartDate != null && checkEndDate != null) {
        arys1 = checkStartDate.split("-");
        var sdate = new Date(arys1[0], parseInt(arys1[1] - 1), arys1[2]);
        arys2 = checkEndDate.split("-");
        var edate = new Date(arys2[0], parseInt(arys2[1] - 1), arys2[2]);
        if (sdate > edate) {
          alert("日期开始时间大于结束时间");
          return false;
        } else {
          alert("通过");
          return true;
        }
      }
    },

    //搜索触发
    tosearch() {
      this.pageNum = 1;
      this.imglist = [];
      this.selectIndex = [];
      this.selectImgUrl = [];
      this.selectPhoto();
    },
    imgInfo(e) {
      this.isimginfo = true;
      this.getbucketName(e.source);
      if (e.username != null) {
        this.upName = e.username;
      } else {
        this.upName = "游客";
      }
      if (e.violation != null && e.violation != "") {
        this.isViolation.isControl = true;
      } else {
        this.isViolation.isControl = false;
      }
      this.images = e;
    },

    delSelectImg() {
      if (this.selectIndex.length == 0) {
        this.$Message.warning("请先选择要操作的数据");
        return false;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "<p>确认删除所选图像吗</p>",
        onOk: () => {
          this.deleteImages(null, null);
        },
        onCancel: () => {},
      });
    },
    delImg(id, index) {
      this.$Modal.confirm({
        title: "确认删除",
        content: "<p>确认删除所选图像吗</p>",
        onOk: () => {
          this.deleteImages(id, index);
        },
        onCancel: () => {},
      });
    },
    deleteImages(id, index) {
      this.$Spin.show();

      if (id == null) {
        if (this.selectIndex.length == 0) {
          this.$Message.warning("所选数据丢失，不可操作");
          return false;
        }
      } else {
        this.selectIndex = [];
        this.selectIndex.push(id);
      }
      let paramJson = this.selectIndex;
      request("/admin/deleImages", paramJson)
        .then((res) => {
          if (res.status == 200) {
            if (index != null) {
              this.imglist.splice(index, 1);
            } else {
              for (let i = 0; i < this.imglist.length; i++) {
                for (let j = 0; j < this.selectIndex.length; j++) {
                  if (this.imglist[i].id == this.selectIndex[j]) {
                    this.imglist.splice(i, 1);
                  }
                }
              }
            }
            this.selectIndex = [];
            this.selectImgUrl = [];
            this.$Spin.hide();
            if (res.data.code == "200") {
              this.$Message.success(res.data.info);
            } else {
              this.$Message.warning(res.data.info);
            }
            this.selectIndex = [];
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

    getbucketName(id) {
      this.bucketName = "";
      for (let i = 0; i < this.bucketlist.length; i++) {
        if (id == this.bucketlist[i].id) {
          this.bucketName = this.bucketlist[i].keyname;
        }
      }
    },
    copy() {
      var clipboard = new this.clipboard(".cobyOrderSn");
      clipboard.on("success", (e) => {
        this.$Message.success("复制成功");
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$Message.error("该浏览器不支持自动复制");
        clipboard.destroy();
      });
    },
    getStorage() {
      request("/admin/getStorageName", {})
        .then((res) => {
          if (res.status == 200) {
            this.$Spin.hide();
            this.bucketlist = res.data.data;
            this.$Spin.hide();
          }
        })
        .catch((err) => {
          this.$Spin.hide();
          console.log(err);
          this.$Message.error("服务器请求错误");
        });
    },
    formatBytes(a, b) {
      if (0 == a) return "0 Bytes";
      var c = 1024,
        d = b || 2,
        e = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"],
        f = Math.floor(Math.log(a) / Math.log(c));
      return parseFloat((a / Math.pow(c, f)).toFixed(d)) + " " + e[f];
    },
    showViewType() {
      this.$Spin.show();
      setTimeout(() => {
        if (this.viewType == 1) {
          this.toolBottom = 15;
          this.viewType = 2;
          this.selectPhoto();
        } else {
          this.toolBottom = 5;
          this.viewType = 1;
          this.selectPhoto();
        }
        this.$Spin.hide();
      }, 1000);
    },
  },
  mounted() {
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth;
        this.screenWidth = window.screenWidth;
      })();
    };
    this.$Spin.show();
    this.getStorage();
    this.selectPhoto();
  },
  components: {
    AddAlbum,
    Treeselect,
    vueQr,
  },
};
</script>

<style>
.example-code-more {
  text-align: center;
  cursor: pointer;
  padding: 30px 0;
  font-size: 16px;
  line-height: 30px;
  font-weight: bold;
}

.imgdivstyle {
  /*width:300px;*/
  height: 160px;
  margin-top: 10px;
  /*background: #e86868;*/
  text-align: center;
  margin-right: 2px;
}

.imgstyle-max {
  /*width: 300px;*/
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 5px;
  border: 1px solid #eee;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 2px 10px 0px;
  transform: translateZ(0);
}

.imgstyle-min {
  /*width: 155px;*/
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 5px;
  border: 1px solid #eee;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 2px 10px 0px;
  transform: translateZ(0);
}

.divimgstyle-max {
  min-width: 300px;
  height: 160px;
  margin: 10px;
}

.divimgstyle-min {
  min-width: 155px;
  height: 150px;
  margin: 10px;
}

.img-tool-cover {
  display: block;
  position: absolute;
  /*bottom: 5px;*/
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.87);
  height: 30px;
  width: 124px;
  box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 6px 0px;
  margin: 0 auto;
  border-radius: 5px;
  text-align: center;
}

.icostyle {
  margin: 0 3px 0 3px;
  font-size: 22px;
  line-height: 28px;
  transition: transform 0.2s;
}

.icostyle:hover {
  transform: scale(1.1);
}

.icostylecolor {
  color: #2d8cf0;
}

.img-search {
  position: absolute;
  width: 100%;
  min-width: 512px;
  height: 15px;
  z-index: 1;
}

.img-search-div {
  margin: auto;
  width: 70%;
  height: 70px;
  background: #fff;
  padding: 13px;
  border-radius: 5px;
  box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 6px 0px;
}

.ivu-btn-icon-only {
  width: 55px;
}

.infotitle {
  color: #464c5b;
  font-size: 14px;
  font-weight: 500;
  margin-right: 10px;
}

.QRCodestyle2 {
  height: 200px;
  width: 200px;
  position: absolute;
  right: 0;
  left: 0;
  opacity: 0.7;
  margin: auto;
  bottom: 47px;
}

.formatTag {
  display: block;
  width: 50px;
  height: 20px;
  background: rgba(38, 38, 38, 0.72);
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 1;
  box-shadow: 0 1px 6px rgba(255, 255, 255, 0.2);
  color: #e3e1e1;
  border-radius: 3px;
}
</style>
