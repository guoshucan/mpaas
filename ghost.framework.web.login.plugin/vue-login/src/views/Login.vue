<template>
  <div class="login-container">
    <el-form
      :model="ruleForm2"
      :rules="rules2"
      status-icon
      ref="ruleForm2"
      label-position="left"
      label-width="0px"
      class="demo-ruleForm login-page"
    >
      <div class="select">
        <el-select v-model="svalue" :placeholder="text" filterable style="width: auto;">
          <el-option
            v-for="item in options"
            :key="item.name"
            :label="item.title"
            :value="item.name"
          ></el-option>
        </el-select>
      </div>
      <h3 class="title">{{$t('system.login')}}</h3>
      <el-form-item prop="username">
        <el-input
          type="text"
          v-model="ruleForm2.username"
          auto-complete="off"
          :placeholder="$t('username')"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          v-model="ruleForm2.password"
          auto-complete="off"
          :placeholder="$t('password')"
        ></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="text" v-model="ruleForm2.code" placeholder="- - - -">
          <template slot="prepend">{{$t('code.title')}}</template>
          <template slot="append">
            <div class="login-code" @click="refreshCode(true)">
              <!-- <Identify :identifyCode="identifyCode"></Identify> -->
              <img
                id="imgIdentifyingCode"
                style="height:28px; width: 100px; cursor: pointer;"
                :alt="$t('code.alt')"
                :title="$t('code.alt')"
              />
            </div>
          </template>
        </el-input>
      </el-form-item>
      <el-checkbox v-model="checked" class="rememberme">{{$t('record.password')}}</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          type="primary"
          style="width:100%;"
          @click="handleSubmit"
          :loading="logining"
        >{{$t('button.login')}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
//vant 弹框包
import { Toast } from "vant";
//QS 序列化包
import QS from "qs";
import axios from "axios";
import * as api from "../common/AxiosApi";
export default {
  data() {
    return {
      logining: false,
      ruleForm2: {
        username: "admin",
        password: "123456",
        code: ""
      },
      rules2: {
        username: [
          {
            required: true,
            message: this.$t("message.username"),
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: this.$t("message.password"),
            trigger: "blur"
          }
        ]
      },
      checked: false,
      options: [],
      svalue: (localStorage.getItem("Accept-Language") == null ? "zh-CN" : localStorage.getItem("Accept-Language").split(',')[0]),
      text: "简体中文",
      //语言地图
      languageMap: []
    };
  },
  created: function() {},
  mounted: function() {
    //页面初始化完成刷新验证码
    this.refreshCode(true);
    //获取语言地图
    api.getLanguageMap().then(data => {
      console.log(QS.stringify(data));
      if (data.code == 0) {
        this.options = data.data;
        //遍历选择默认语言
        this.options.forEach(item => {
          if (localStorage.getItem("Accept-Language") == null) {
            //未初始化客户端本地缓存语言
            if (item.current) {
              this.svalue = item.name;
              localStorage.setItem("Accept-Language", this.svalue);
              console.log(
                "初始化默认语言:" + localStorage.getItem("Accept-Language")
              );
              return;
            }
          } else {
            //已经初始化过客户端本地语言缓存
            this.svalue = localStorage.getItem("Accept-Language");
            console.log(
              "初始化默认语言:" + localStorage.getItem("Accept-Language")
            );
            return;
          }
        });
      } else {
        Toast({
          message: data.message,
          duration: 1000,
          forbidClick: true
        });
      }
    });
  },
  methods: {
    //登录
    handleSubmit(event) {
      this.$refs.ruleForm2.validate(valid => {
        if (valid) {
          this.logining = true;
          if (
            this.ruleForm2.username === "admin" &&
            this.ruleForm2.password === "123456"
          ) {
            this.logining = false;
            sessionStorage.setItem("user", this.ruleForm2.username);
            this.$router.push({ path: "/" });
          } else {
            this.logining = false;
            this.$alert("username or password wrong!", "info", {
              confirmButtonText: "ok"
            });
          }
        } else {
          console.log("error submit!");
          return false;
        }
      });
    },
    /**
     * 设置语音
     */
    setLocaleMessage(locale) {
      if (localStorage.getItem("Accept-Language") == this.svalue) {
        return;
      }
      localStorage.setItem("Accept-Language", this.svalue);
      let this_ = this;
      api.getLanguageMessage({ language: locale }).then(r => {
        let data = JSON.parse(r.data);
        console.log(QS.stringify(data));
        this_.$i18n.setLocaleMessage(locale, data);
        //动态更改语言，页面不刷新
        this_.$i18n.locale = this_.svalue;
      });
    },
    /**
     * 刷新验证码
     */
    refreshCode(refresh) {
      let identifyCodeSrc =
        axios.defaults.baseURL +
        "/api/code/6c9028a3-2ad0-47ea-8fc2-9e3f905a141f";
      if (refresh) {
        identifyCodeSrc =
          axios.defaults.baseURL +
          "/api/code/6c9028a3-2ad0-47ea-8fc2-9e3f905a141f?" +
          Math.random();
      }
      let objs = document.getElementById("imgIdentifyingCode");
      objs.src = identifyCodeSrc;
      console.log(objs.src);
    }
  },
  watch: {
    //判断下拉框的值是否有改变
    svalue(val, oldVal) {
      if (val != oldVal) {
        this.$emit("input", this.svalue);
        this.setLocaleMessage(this.svalue);
        console.log(this.svalue);
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100%;
}
.login-page {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
label.el-checkbox.rememberme {
  margin: 0px 0px 15px;
  text-align: left;
}
.select {
  text-align: end;
}
</style>