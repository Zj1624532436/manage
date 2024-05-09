import router from "@/router/index"
import store from "@/store";

router.beforeEach((to,from,next)=>{
    const whiteList = ['/login']
    const token = store.getters.GET_TOKEN
    const hasRoutes = store.state.hasRoutes
    const menus = store.getters.GET_MENULIST

    if(token){
        if(!hasRoutes){
             bindRoute(menus)
            store.commit("SET_ROUTES_STATE",true)
        }
        if (to.matched.length ===0) next({path: to.path})
        else next()
    }else{
        if(whiteList.includes(to.path)){
            next();
        }else{
            next("/login")
        }
    }
})

// 动态绑定路由
const bindRoute = (menus)=>{
    const newRoutes = router.options.routes;
    menus.forEach(menu=>{
        if (menu.children) {
            menu.children.forEach(m=>{
                const route = menuToRoute(m,menu.name)
                if(route){
                    newRoutes[0].children.push(route)
                }
            })
        }
    })
    // 重新添加路由
    newRoutes.forEach(route=>{
        router.addRoute(route)
    })
}

// 菜单对象转成路由对象
const menuToRoute = (menu,parentName)=>{
    if(!menu.component){
        return null
    }else{
        const route = {
            name : menu.name,
            path: menu.path,
            meta: {
                parentName: parentName
            }
        }
        /* @vite-ignore */
        route.component=()=>import('../views/'+ menu.component +'.vue')
        /* @vite-ignore */
        return route;
    }
}