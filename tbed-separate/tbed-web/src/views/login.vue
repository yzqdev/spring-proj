<template>
  <el-card
    class="animate__animated animate__fadeIn"
    style="width: 390px; margin: 80px auto; min-width: 40vh"
  >
    <div style="text-align: center">
      <el-form ref="formInline" :model="formInline" :rules="ruleInline" inline>
        <Layout style="background: #fff">
          <Header>
            <Icon type="md-finger-print" style="font-size: xx-large" />
            <span style="font-size: large; font-weight: bold">Login</span>
          </Header>
          <Content>
            <el-form
              ref="formInline"
              :model="formInline"
              :rules="ruleInline"
              inline
            >
              <el-form-item prop="email" style="display: inline-block; width: 80%">
                <Input
                  prefix="md-mail"
                  :maxlength="100"
                  size="large"
                  v-model="formInline.email"
                  placeholder="User Email"
                  style="width: 100%; height: 40px"
                />
              </el-form-item>
              <el-form-item
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
              </el-form-item>
              <el-form-item style="display: inline-block; width: 80%">
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
              </el-form-item>
              <el-form-item>
                <ButtonGroup shape="circle">
                  <el-button type="primary" @click="registerClick">
                    <Icon type="ios-arrow-back"></Icon>
                    ??????
                  </el-button>
                  <Button
                    type="primary"
                    @click="handleSubmit('formInline')"
                    @keyup.enter="handleSubmit('formInline')"
                  >
                    ??????
                    <Icon type="ios-arrow-forward"></Icon>
                  </Button>
                </ButtonGroup>
              </el-form-item>
            </el-form>
          </Content>
          <Footer class="layout-footer-center" style="border-radius: 5px">
            <el-button
              type="text"
              ghost
              style="color: #598abb"
              @click="
                () => {
                  IsRetrieveMSG = true;
                  reloadCode(3);
                }
              "
              >????????????</el-button
            >
          </Footer>
        </Layout>
      </el-form>
    </div>

    <el-dialog v-model="IsRetrieveMSG" :footer-hide="true">
      <br />
      <el-card :dis-hover="true" :bordered="false" :shadow="false">
        <div style="text-align: center">
          <p style="color: #656565; font-size: 24px; font-weight: bold">
            ????????????
          </p>
          <br />
          <br />
          <el-form ref="formInline" inline @submit.native.prevent>
            <el-form-item prop="email" style="display: inline-block; width: 80%">
              <Input
                prefix="md-mail"
                :maxlength="100"
                size="large"
                v-model="retrieveData.email"
                placeholder="User Email"
                style="width: 100%; height: 40px"
              />
            </el-form-item>
            <el-form-item style="display: inline-block; width: 80%">
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
            </el-form-item>
            <el-form-item style="display: inline-block; width: 80%">
              <Row>
                <Button
                  type="primary"
                  shape="circle"
                  style="width: 100%"
                  :loading="reloadLoading"
                  @click.native="sendRetrievePass"
                  >????????????</Button
                >
              </Row>
            </el-form-item>
            <!-- ?????? -->
            <!--  /retrievePass   -->
          </el-form>
          <br />
          <p style="color: #656565">
            {{ $store.state.metaInfo.webname }} &copy; All Rights Reserved
          </p>
          <!--            <p>HELLOHAO 2021 &copy; BetaForCore</p>-->
        </div>
      </el-card>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { request } from "../network/request";
import {useGlobalStore} from "../store/g";
import {useRoute,useRouter} from "vue-router";
let gStore=useGlobalStore()
import {ElMessage} from 'element-plus'
let router=useRouter()
let route=useRoute()
// metaInfo() {
  //   return {
  //     title:
  //       "??????|" +
  //       $store.state.metaInfo.webname +
  //       $store.state.metaInfo.splitline +
  //       $store.state.metaInfo.websubtitle, // set a title
  //     meta: [
  //       // set meta
  //       {
  //         name: "keyWords",
  //         content: $store.state.metaInfo.keywords,
  //       },
  //       {
  //         name: "description",
  //         content: $store.state.metaInfo.description,
  //       },
  //     ],
  //   };
  // },

 let  reloadLoading=$ref(false)
 let  IsRetrieveMSG=$ref(false)
 let  verifyCodeURL=$ref()
let  retrieveCodeURL=$ref()
let   userToken=$ref()
 let  formInline=$ref({
       email: "admin",
       password: "admin",
       verifyCode: "",
     })
 let  retrieveData=$ref({
  email: null,
  retrieveCode: null,
})
let isModule=$ref()
let  ruleInline=$ref({
  email: [{ required: true, message: " ", trigger: "blur" }],
  password: [
    { required: true, message: " ", trigger: "blur" },
    {
      type: "string",
      min: 5,
      message: "??????????????????????????????5???",
      trigger: "blur",
    },
  ],
  verifyCode: [{ required: true, message: " ", trigger: "blur" }],
})


 onMounted(() => {
   verifyCodeURL = $http.defaults.baseURL + "/verifyCode";
    retrieveCodeURL =
       $http.defaults.baseURL + "/verifyCodeForRetrieve";
   // console.log("??????????????????"+$serverHost)
   $emit("getRouterInfo", "ahahha");
 })


  function  registerClick() {
     router.replace("/register");
      isModule = "register";
    }
   function handleSubmit(name) {
      $refs[name].validate((valid) => {
        if (valid) {
          if (formInline.email == "" || formInline.email == null) {
ElMessage({
  type:'success',
  message:"???????????????"
})
            return false;
          }
          if (
            formInline.password == "" ||
            formInline.password == null
          ) {
            $Message.info("???????????????");
            return false;
          }
          if (
            formInline.verifyCode == "" ||
            formInline.verifyCode == null
          ) {
            $Message.info("??????????????????");
            return false;
          }

          // var params = {
          //   data: formInline
          // };
          $Spin.show();
          request("/user/login", formInline)
            .then((res) => {
              $Spin.hide();
              if (res.status == 200) {
                var json = res.data;
                if (json.code == "200") {
                  userToken = json.data.token;
                  var RoleLevel = json.data.RoleLevel;
                  // ?????????token?????????vuex???
                  $store.state.loginStatus = true;
                  $store.state.Authorization = userToken; //user.Authorization;
                  $store.state.RoleLevel = RoleLevel;
                  store.commit("setUserName", json.data.userName);
                  localStorage.setItem("userName", json.data.userName);
                  localStorage.setItem("Authorization", userToken);
                  localStorage.setItem("RoleLevel", RoleLevel);
                  // console.log('vuex-token===='+$store.state.Authorization);
                  $Message.success(json.info);
                   router.replace("/"); //index
                } else {
                  reloadCode(1);
                  formInline.verifyCode = "";
                  $Message.error(json.info);
                }
              } else {
                $Message.error("?????????????????????");
              }
            })
            .catch((err) => {
              $Spin.hide();
              console.log(err);
              reloadCode(1);
              $Message.error("?????????????????????");
            });
        } else {
          $Message.error("??????????????????!");
        }
      });
    }
 function   login() {
      $Spin.show();
      // $Spin.hide();
    }
  function  reloadCode(v) {
      var getTimestamp = new Date().getTime();
      if (v == 1) {
        if (verifyCodeURL.indexOf("?") > -1) {
          verifyCodeURL =
            verifyCodeURL + "&timestamp=" + getTimestamp;
        } else {
          verifyCodeURL =
            verifyCodeURL + "?timestamp=" + getTimestamp;
        }
      } else {
        if (retrieveCodeURL.indexOf("?") > -1) {
          retrieveCodeURL =
            retrieveCodeURL + "&timestamp=" + getTimestamp;
        } else {
          retrieveCodeURL =
            retrieveCodeURL + "?timestamp=" + getTimestamp;
        }
      }
    }
   function sendRetrievePass() {
      var that = this;
      that.reloadLoading = true;
      if (
        that.retrieveData.email == null ||
        that.retrieveData.email.replace(/\s*/g, "") == "" ||
        that.retrieveData.retrieveCode == null ||
        that.retrieveData.retrieveCode.replace(/\s*/g, "") == ""
      ) {
        that.$Message.warning("??????????????????????????????");
        that.reloadCode(3);
        that.reloadLoading = false;
        return false;
      }
      var verify = /^\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
      if (!verify.test(that.retrieveData.email)) {
        that.$Message.warning("??????????????????, ???????????????");
        that.reloadCode(3);
        that.reloadLoading = false;
        return false;
      }
      that.$Modal.confirm({
        title: "????????????",
        content:
          "<p>????????????????????????????????????????????????????????????????????????????????????????????????????????????</p>",
        onOk: () => {
          request("/user/retrievePass", that.retrieveData)
            .then((res) => {
              that.reloadCode(3);
              that.reloadLoading = false; //????????????
              if (res.status == 200) {
                var json = res.data;
                if (json.code == "200") {
                  that.IsRetrieveMSG = false; //????????????
                  that.$Message.success(json.info);
                } else {
                  that.$Message.warning(json.info);
                }
              } else {
                that.$Message.error("?????????????????????");
              }
            })
            .catch((err) => {
              console.log(err);
              that.reloadLoading = true; //????????????
              that.$Message.error("?????????????????????");
            });
        },
        onCancel: () => {
          //$Message.info('Clicked cancel');
        },
      });
    }
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
