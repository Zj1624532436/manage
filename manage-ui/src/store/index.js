import { createStore } from 'vuex'
import router from "@/router";

export default createStore({
  state: {
    hasRoutes:false,
    editableTabsValue:'/index',
    editableTabs:[
      {title:'首页',name:'/index'}
    ],
    token: "",
    userInfo: {}
  },
  getters: {
    GET_TOKEN: state => {
      return  state.token || sessionStorage.getItem("token")
    },
    GET_MENULIST: state => {
      return JSON.parse(state.menuList||sessionStorage.getItem("menuList"))
    },

    GET_USERINFO: state => {
      return JSON.parse(sessionStorage.getItem("userInfo"))
    }
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
      sessionStorage.setItem("token", token);
    },
    SET_MENULIST: (state, menuList) => {
      state.menuList = JSON.stringify(menuList);
      sessionStorage.setItem("menuList",JSON.stringify(menuList));
    },
    SET_USERINFO: (state, userInfo) => {
      const user = JSON.stringify(userInfo);
      sessionStorage.setItem("userInfo", user);
    },
    SET_ROUTES_STATE(state,hasRoutes){
      state.hasRoutes = hasRoutes
    },
    ADD_TABS(state,tab){
      if (state.editableTabs.findIndex(e=>e.name===tab.path)===-1){//如果没找到tab页
        state.editableTabs.push({
          title: tab.name,
          name: tab.path
        })
      }
      state.editableTabsValue=tab.path
    },
    RESET_TABS(state){
      state.editableTabsValue='/index';
      state.editableTabs=[
        {title:'首页',name:'/index'}
      ]
    }
  },
  actions: {
    logout(){
      window.sessionStorage.clear();
      router.replace('/login')
    }
  },
  modules: {
  }
})
