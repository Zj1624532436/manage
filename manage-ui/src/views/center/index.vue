<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6"><div class="grid-content ep-bg-purple" />
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <Avatar  :user="currentUser"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon="user" />&nbsp;&nbsp;用户名称
                <div class="pull-right">{{currentUser.username}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon="phone" />&nbsp;&nbsp;手机号码
                <div class="pull-right">{{currentUser.phonenumber}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon="email" />&nbsp;&nbsp;用户邮箱
                <div class="pull-right">{{currentUser.email}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon="peoples" />&nbsp;&nbsp;所属角色
                <div class="pull-right">{{currentUser.roles}}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon="date" />&nbsp;&nbsp;创建日期
                <div class="pull-right">{{formatDate(currentUser.loginDate)}}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card class="box-card-two">
          <template v-slot:header>
            <div>
              <span>基本资料</span>
            </div>
          </template>
          <UserInfo :user="currentUser" style="margin-top: 20px"/>
          <ResetPwd :user="currentUser"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import store from "@/store";
import {ref} from "vue";
import {formatDate} from '@/utils/formatDate'
import Avatar from "./avatar.vue"
import ResetPwd from "./resetPwd.vue"
import UserInfo from "./userInfo.vue";

const currentUser = ref(store.getters.GET_USERINFO);
</script>

<style scoped lang="scss">

.list-group-item{
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 13px;
}
.pull-right{
  float: right!important;
}
.box-card-two{
  height: 100%;
}
.box-card{
  height: 100%;
}

.el-tabs__item{
  width: 300px;
}

</style>