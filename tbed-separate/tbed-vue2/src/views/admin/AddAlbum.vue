<template>
  <div>
    <Modal :value="isAlbum" :footer-hide="true" :title="albumData.title">
      <Form @submit.native.prevent :label-width="70" style="margin-top: 30px">
        <FormItem label="链接">
          <Input v-model="albumData.url" style="width: auto; width: 100%" />
        </FormItem>
        <FormItem label="密码">
          <Input
            v-model="albumData.password"
            style="width: auto;  "
          />
          <Button
            style="position: absolute; right: 30px"
            size="small"
            type="primary"
            shape="circle"
            class="cobyOrderSn"
            data-clipboard-action="copy"
            :data-clipboard-text="
              '画廊链接：' +
              albumData.url +
              '提取码：' +
              albumData.password +
              ' 复制这段内容后用浏览器打开，即可查看画廊哦'
            "
            @click.native="copy"
            >复 制</Button
          >
        </FormItem>
      </Form>
      <div class="QRCodestyle" v-if="albumData.url" style="text-align: center">
        <vue-qr :text="albumData.url" :size="200"></vue-qr>
      </div>
      <div style="text-align: center">
        <p>对方通过扫码即可访问画廊</p>
      </div>
    </Modal>
    <Modal
      :value="visible"
      @on-visible-change="closeModal"
      :footer-hide="true"
      width="620"
    >
      <Tabs>
        <TabPane label="画廊" icon="ios-images">
          <div
            style="
              margin: 0 auto;
              max-height: 60vh;
              overflow-x: hidden;
              overflow-y: auto;
            "
          >
            <Divider>画廊信息</Divider>
            <Row>
              <Col :xs="24" :sm="24" :md="12" :lg="12"
                ><Input
                  v-model="albumTitle"
                  maxlength="30"
                  style="width: 266px; margin: auto"
                  ><span slot="prepend" placeholder="画廊标题"
                    >标题</span
                  ></Input
                >
              </Col>
              <Col :xs="24" :sm="24" :md="12" :lg="12"
                ><Input
                  v-model="password"
                  maxlength="30"
                  style="width: 266px; margin: auto"
                  ><span slot="prepend" placeholder="可留空">密码</span></Input
                >
              </Col>
            </Row>

            <Divider>图片列表</Divider>
            <List
              style="max-height: 60vh; overflow: auto"
              v-for="(album, key) in albumList"
              :key="key"
            >
              <ListItem>
                <ListItemMeta :avatar="album.imgurl" />
                <Input
                  type="textarea"
                  v-model="album.notes"
                  :rows="2"
                  maxlength="100"
                  placeholder="图片说明/描述,可留空"
                />
              </ListItem>
            </List>
          </div>
          <Button
            type="primary"
            shape="circle"
            @click.native="submitAlbum"
            style="float: right; margin-right: 10px"
            >保存
          </Button>
        </TabPane>
        <TabPane label="更多" icon="ios-filing" disabled
          >更多功能待开发</TabPane
        >
      </Tabs>
    </Modal>
  </div>
</template>

<script>
import { request } from "@/network/request";
import VueQr from "vue-qr";
export default {
  name: "AddAlbum",
  components: {
    VueQr,
  },
  data() {
    return {
      albumTitle: null,
      password: null,
      isAlbum: false,
      albumData: {},
    };
  },
  props: {
    visible: { type: Boolean, default: false },
    albumList: { type: Array, default: [] },
  },
  mounted() {
    this.shuoming = this.albumList;
  },
  methods: {
    closeModal(vis) {
      this.$emit("close", vis);
    },
    submitAlbum() {
      if (this.albumTitle == null || this.albumTitle == "") {
        this.$Message.warning("画廊标题不能为空");
        return false;
      }
      if (this.albumList.length == 0) {
        this.$Message.warning("画廊图像不存在");
        return false;
      }
      var param = {
        albumTitle: this.albumTitle,
        password: this.password,
        albumList: this.albumList,
      };
      console.log("=====" + JSON.stringify(param));
      request(
        "/SaveForAlbum",
        param,
        this.$store,
        this.$router,
        this.$serverHost
      )
        .then((res) => {
          this.$Spin.hide();
          if (res.status == 200) {
            var json = res.data.data;
            if (res.data.code == "200") {
              this.albumData = json;
              if (this.albumData.password == null || this.albumData == "") {
                this.albumData.password = "无";
              }
              this.albumData.url =
                window.location.protocol +
                "//" +
                window.location.host +
                "/h/" +
                json.url;
              this.$emit("close", false);
              this.isAlbum = true;
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
  },
};
</script>

<style scoped>
.ivu-avatar {
  width: 42px;
  height: 42px;
  border-radius: 5px;
  box-shadow: 0 0 6px #c1c1c1;
}
</style>
