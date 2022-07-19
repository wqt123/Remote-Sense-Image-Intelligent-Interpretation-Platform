import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/login/register'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/changeDetection',   //变化检测
    children: [{
      path: 'changeDetection',
      name: 'ChangeDetection',
      component: () => import('@/views/changeDetection/index'),
      meta: { title: '变化检测', icon: 'dashboard' }
    }, {
      path: 'changeDetectionbatch',
      component: () => import('@/views/changeDetection/batch'),
      hidden: true,
      meta: { title: '变化检测批处理', icon: 'form' }
    }]
  },

  {
    path: '/targetDetection',
    component: Layout,
    redirect: '/targetDetection',
    children: [{
      path: '/targetDetection',
      name: 'TargetDetection',    //目标检测
      component: () => import('@/views/targetDetection/index'),
      meta: { title: '目标检测', icon: 'example' }
    }, {
      path: '/targetDetectionbatch',
      component: () => import('@/views/targetDetection/batch'),
      hidden: true,
      meta: { title: '目标检测批处理', icon: 'form' }
    }]
  },
  {
    path: '/targetExtraction',
    component: Layout,
    redirect: '/targetExtraction',
    children: [{
      path: '/targetExtraction',
      name: 'TargetExtraction',    //目标提取
      component: () => import('@/views/targetExtraction/index'),
      meta: { title: '目标提取', icon: 'tree' }
    }, {
      path: '/targetExtractionbatch',
      component: () => import('@/views/targetExtraction/batch'),
      hidden: true,
      meta: { title: '目标提取批处理', icon: 'form' }
    }]
  },
  {
    path: '/terrianClassification',
    component: Layout,
    redirect: '/terrianClassification',
    children: [{
      path: '/terrianClassification',
      name: 'TerrianClassification',    //地物分类
      component: () => import('@/views/terrianClassification/index'),
      meta: { title: '地物分类', icon: 'form' }
    }, {
      path: '/terrianClassificationbatch',
      component: () => import('@/views/terrianClassification/batch'),
      hidden: true,
      meta: { title: '地物分类批处理', icon: 'form' }
    }]
  },
  {
    path: '/history',
    component: Layout,
    redirect: '/historyList',
    children: [{
      path: 'historyList',
      name: 'History',    //历史记录
      component: () => import('@/views/history/index'),
      meta: { title: '历史记录', icon: 'history' }
    }]
  },


  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
