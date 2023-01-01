<template>
  <Card
    class="animate__animated animate__fadeIn"
    style="width: 390px; margin: 80px auto; min-width: 40vh"
  >
    <div style="text-align: center">
      <Form ref="formInline" :model="formInline" :rules="ruleInline" inline>
        <Layout style="background: #fff">
          <Header>
            <Icon type="md-finger-print" style="font-size: xx-large" />
            <span style="font-size: large; font-weight: bold">Login</span>
          </Header>
          <Content>
            <Form
              ref="formInline"
              :model="formInline"
              :rules="ruleInline"
              inline
            >
              <FormItem prop="email" style="display: inline-block; width: 80%">
                <Input
                  prefix="md-mail"
                  :maxlength="100"
                  size="large"
                  v-model="formInline.email"
                  placeholder="User Email"
                  style="width: 100%; height: 40px"
                />
              </FormItem>
              <FormItem
                prop="password"
                style="display: inline-block; width: 80%"
              >
                <Input
                  prefix="md-lock"
                  :maxlength="200"
                  @keyup.enter="handleSubmit('formInline')"
                  size="large"
                  type="password"
                  password
                  v-model="formInline.password"
                  placeholder="User Password"
                  style="width: 100%; height: 40px"
                />
              </FormItem>
              <FormItem style="display: inline-block; width: 80%">
                <Row>
                  <Col span="16">
                    <Input
                      prefix="md-barcode"
                      :maxlength="10"
                      :rules="ruleInline"
                      @keyup.enter="handleSubmit('formInline')"
                      v-model="formInline.verifyCode"
                      size="large"
                      style="width: 100%; height: 40px"
                    />
                  </Col>
                  <Col span="4" offset="1">
                    <img @click="reloadCode(1)" :src="verifyCodeURL" />
                  </Col>
                </Row>
              </FormItem>
              <FormItem>
                <ButtonGroup shape="circle">
                  <Button type="primary" @click="registerClick">
                    <Icon type="ios-arrow-back"></Icon>
                    注册
                  </Button>
                  <Button
                    type="primary"
                    @click="handleSubmit('formInline')"
                    @keyup.enter="handleSubmit('formInline')"
                  >
                    登录
                    <Icon type="ios-arrow-forward"></Icon>
                  </Button>
                </ButtonGroup>
              </FormItem>
            </Form>
          </Content>
          <Footer class="layout-footer-center" style="border-radius: 5px">
            <Button
              type="text"
              ghost
              style="color: #598abb"
              @click="
                () => {
                  IsRetrieveMSG = true;
                  reloadCode(3);
                }
              "
              >忘记密码</Button
            >
          </Footer>
        </Layout>
      </Form>
    </div>

    <Modal v-model="IsRetrieveMSG" :footer-hide="true">
      <br />
      <Card :dis-hover="true" :bordered="false" :shadow="false">
        <div style="text-align: center">
          <p style="color: #656565; font-size: 24px; font-weight: bold">
            密码重置
          </p>
          <br />
          <br />
          <Form ref="formInline" inline @submit.native.prevent>
            <FormItem prop="email" style="display: inline-block; width: 80%">
              <Input
                prefix="md-mail"
                :maxlength="100"
                size="large"
                v-model="retrieveData.email"
                placeholder="User Email"
                style="width: 100%; height: 40px"
              />
            </FormItem>
            <FormItem style="display: inline-block; width: 80%">
              <Row>
                <Col span="16">
                  <Input
                    prefix="md-barcode"
                    :maxlength="10"
                    v-model="retrieveData.retrieveCode"
                    size="large"
                    style="width: 100%; height: 40px"
                  />
                </Col>
                <Col span="4" offset="1">
                  <img @click="reloadCode(3)" :src="retrieveCodeURL" />
                </Col>
              </Row>
            </FormItem>
            <FormItem style="display: inline-block; width: 80%">
              <Row>
                <Button
                  type="primary"
                  shape="circle"
                  style="width: 100%"
                  :loading="reloadLoading"
                  @click.native="sendRetrievePass"
                  >找回密码</Button
                >
              </Row>
            </FormItem>
            <!-- 接口 -->
            <!--  /retrievePass   -->
          </Form>
          <br />
          <p style="color: #656565">
            {{ this.$store.state.metaInfo.webname }} &copy; All Rights Reserved
          </p>
          <!--            <p>HELLOHAO 2021 &copy; BetaForCore</p>-->
        </div>
      </Card>
    </Modal>
  </Card>
</template>
<script>
import { request } from "../network/request";
import store from "@/store";
export default {
  metaInfo() {
    return {
      title:
        "登录|" +
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
    };
  },

  data() {
    return {
      reloadLoading: false,
      IsRetrieveMSG: false,
      verifyCodeURL: null,
      retrieveCodeURL: null,
      userToken: null,
      formInline: {
        email: "1@qq.com",
        password: "123456",
        verifyCode: "",
      },
      retrieveData: {
        email: null,
        retrieveCode: null,
      },
      ruleInline: {
        email: [{ required: true, message: " ", trigger: "blur" }],
        password: [
          { required: true, message: " ", trigger: "blur" },
          {
            type: "string",
            min: 5,
            message: "密码长度最短不得小于5位",
            trigger: "blur",
          },
        ],
        verifyCode: [{ required: true, message: " ", trigger: "blur" }],
      },
    };
  },
  mounted() {
    this.verifyCodeURL = this.$http.defaults.baseURL + "/verifyCode";
    this.retrieveCodeURL =
      this.$http.defaults.baseURL + "/verifyCodeForRetrieve";
    // console.log("组件传送开始"+this.$serverHost)
    this.$emit("getRouterInfo", "ahahha");
  },
  methods: {
    registerClick() {
      this.$router.replace("/register");
      this.isModule = "register";
    },
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.formInline.email == "" || this.formInline.email == null) {
            this.$Message.info("请输入邮箱");
            return false;
          }
          if (
            this.formInline.password == "" ||
            this.formInline.password == null
          ) {
            this.$Message.info("请输入密码");
            return false;
          }
          if (
            this.formInline.verifyCode == "" ||
            this.formInline.verifyCode == null
          ) {
            this.$Message.info("请输入验证码");
            return false;
          }

          console.log(this.formInline);
          // var params = {
          //   data: this.formInline
          // };
          this.$Spin.show();
          request("/user/login", this.formInline)
            .then((res) => {
              this.$Spin.hide();
              if (res.status == 200) {
                var json = res.data;
                if (json.code == "200") {
                  this.userToken = json.data.token;
                  var RoleLevel = json.data.RoleLevel;
                  // 将用户token保存到vuex中
                  this.$store.state.loginStatus = true;
                  this.$store.state.Authorization = this.userToken; //user.Authorization;
                  this.$store.state.RoleLevel = RoleLevel;
                  store.commit("setUserName", json.data.userName);
                  localStorage.setItem("userName", json.data.userName);
                  localStorage.setItem("Authorization", this.userToken);
                  localStorage.setItem("RoleLevel", RoleLevel);
                  // console.log('vuex-token===='+this.$store.state.Authorization);
                  this.$Message.success(json.info);
                  this.$router.replace("/"); //index
                } else {
                  this.reloadCode(1);
                  this.formInline.verifyCode = "";
                  this.$Message.error(json.info);
                }
              } else {
                this.$Message.error("请求时出现错误");
              }
            })
            .catch((err) => {
              this.$Spin.hide();
              console.log(err);
              this.reloadCode(1);
              this.$Message.error("服务器请求错误");
            });
        } else {
          this.$Message.error("别尼玛瞎输入!");
        }
      });
    },
    login() {
      this.$Spin.show();
      // this.$Spin.hide();
    },
    reloadCode(v) {
      var getTimestamp = new Date().getTime();
      if (v == 1) {
        if (this.verifyCodeURL.indexOf("?") > -1) {
          this.verifyCodeURL =
            this.verifyCodeURL + "&timestamp=" + getTimestamp;
        } else {
          this.verifyCodeURL =
            this.verifyCodeURL + "?timestamp=" + getTimestamp;
        }
      } else {
        if (this.retrieveCodeURL.indexOf("?") > -1) {
          this.retrieveCodeURL =
            this.retrieveCodeURL + "&timestamp=" + getTimestamp;
        } else {
          this.retrieveCodeURL =
            this.retrieveCodeURL + "?timestamp=" + getTimestamp;
        }
      }
    },
    sendRetrievePass() {
      var that = this;
      that.reloadLoading = true;
      if (
        that.retrieveData.email == null ||
        that.retrieveData.email.replace(/\s*/g, "") == "" ||
        that.retrieveData.retrieveCode == null ||
        that.retrieveData.retrieveCode.replace(/\s*/g, "") == ""
      ) {
        that.$Message.warning("邮箱和验证码不能为空");
        that.reloadCode(3);
        that.reloadLoading = false;
        return false;
      }
      var verify = /^\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
      if (!verify.test(that.retrieveData.email)) {
        that.$Message.warning("邮箱格式错误, 请重新输入");
        that.reloadCode(3);
        that.reloadLoading = false;
        return false;
      }
      that.$Modal.confirm({
        title: "重置密码",
        content:
          "<p>重置链接将发入您的邮箱，邮箱验证完成后密码将重置为，重置后尽快修改新密码</p>",
        onOk: () => {
          request("/user/retrievePass", that.retrieveData)
            .then((res) => {
              that.reloadCode(3);
              that.reloadLoading = false; //释放按钮
              if (res.status == 200) {
                var json = res.data;
                if (json.code == "200") {
                  that.IsRetrieveMSG = false; //关闭弹窗
                  that.$Message.success(json.info);
                } else {
                  that.$Message.warning(json.info);
                }
              } else {
                that.$Message.error("请求时出现错误");
              }
            })
            .catch((err) => {
              console.log(err);
              that.reloadLoading = true; //释放按钮
              that.$Message.error("服务器请求错误");
            });
        },
        onCancel: () => {
          //this.$Message.info('Clicked cancel');
        },
      });
    },
  },
};
</script>

<style scoped>
.ivu-form-item-error .ivu-input {
  border: 2px solid #e86868;
}
.ivu-form-item-error .ivu-input-group-prepend {
  background-color: #fff;
  border: 2px solid #e86868;
}
.ivu-layout-header {
  background: #fff;
}

.ivu-card-bordered {
  border: 0;
  border-color: #e8eaec;
  box-shadow: 0 2px 5px 1px rgba(64, 60, 67, 0.16);
  /*box-shadow: 0 0 10px #ebebec;*/
}
</style>
