<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-sidebar">
      <div class="sidebar-header">
        <span v-if="!isCollapse" class="sidebar-logo-text">🌱 苗丰</span>
        <span v-else class="sidebar-logo-text">🌱</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#1b3a2a"
        text-color="#c0d8c8"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/fertilizer"><el-icon><Shop /></el-icon><span>肥料管理</span></el-menu-item>
        <el-menu-item index="/tree"><el-icon><Grid /></el-icon><span>树木管理</span></el-menu-item>
        <el-menu-item index="/record"><el-icon><Document /></el-icon><span>施肥记录</span></el-menu-item>
        <el-menu-item index="/rule"><el-icon><Setting /></el-icon><span>施肥规则</span></el-menu-item>
        <el-sub-menu index="mall-group">
          <template #title><el-icon><Goods /></el-icon><span>商城管理</span></template>
          <el-menu-item index="/admin/products"><span>商品列表</span></el-menu-item>
          <el-menu-item index="/admin/categories"><span>商品分类</span></el-menu-item>
          <el-menu-item index="/admin/uploads"><span>用户上传审核</span></el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="content-group">
          <template #title><el-icon><Collection /></el-icon><span>内容管理</span></template>
          <el-menu-item index="/admin/encyclopedia"><span>百科管理</span></el-menu-item>
          <el-menu-item index="/admin/articles"><span>文章管理</span></el-menu-item>
          <el-menu-item index="/admin/comments"><span>评论管理</span></el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="order-group">
          <template #title><el-icon><Tickets /></el-icon><span>订单管理</span></template>
          <el-menu-item index="/admin/orders"><span>订单列表</span></el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="user-group">
          <template #title><el-icon><UserFilled /></el-icon><span>用户管理</span></template>
          <el-menu-item index="/admin/users"><span>用户列表</span></el-menu-item>
          <el-menu-item index="/admin/feedbacks"><span>反馈处理</span></el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="system-group">
          <template #title><el-icon><Tools /></el-icon><span>系统管理</span></template>
          <el-menu-item index="/admin/admins"><span>管理员管理</span></el-menu-item>
          <el-menu-item index="/admin/config"><span>平台设置</span></el-menu-item>
          <el-menu-item index="/admin/faqs"><span>FAQ管理</span></el-menu-item>
          <el-menu-item index="/admin/activities"><span>活动管理</span></el-menu-item>
          <el-menu-item index="/admin/messages"><span>消息推送</span></el-menu-item>
          <el-menu-item index="/admin/logs"><span>系统日志</span></el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 右侧主体 -->
    <div class="layout-main">
      <!-- 顶部栏 -->
      <header class="layout-header">
        <div class="header-left">
          <el-icon
            class="collapse-icon"
            @click="isCollapse = !isCollapse"
          >
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="header-breadcrumb">{{ pageTitle }}</span>
        </div>
        <div class="header-right">
          <span class="header-user">{{ authStore.userInfo?.realName || authStore.userInfo?.username || '管理员' }}</span>
          <el-button type="danger" text size="small" @click="handleLogout">
            退出登录
          </el-button>
        </div>
      </header>

      <!-- 内容区 -->
      <main class="layout-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { logout } from '@/api/auth'
import { Shop, Grid, Document, Setting, Goods, Collection, Tickets, UserFilled, Tools } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const isCollapse = ref(false)

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta?.title || '')

const handleLogout = async () => {
  try { await logout() } catch { /* 忽略 */ }
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.layout-sidebar {
  background-color: #1b3a2a;
  overflow-y: auto;
  transition: width 0.3s;
  flex-shrink: 0;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: center;
  height: var(--header-height);
  color: #ffffff;
}

.sidebar-logo-text {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 2px;
  white-space: nowrap;
  overflow: hidden;
}

/* Element Plus menu overrides */
.layout-sidebar .el-menu {
  border-right: none;
}

.layout-sidebar .el-menu-item {
  font-size: 14px;
}

.layout-sidebar .el-menu-item.is-active {
  background-color: var(--color-primary) !important;
}

.layout-sidebar .el-menu-item:hover {
  background-color: rgba(255,255,255,0.08) !important;
}

.layout-sidebar .el-menu-item.is-active:hover {
  background-color: var(--color-primary-light) !important;
}

.layout-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--header-height);
  padding: 0 20px;
  background: var(--color-white);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
  color: var(--color-text-secondary);
}

.collapse-icon:hover {
  color: var(--color-primary);
}

.header-breadcrumb {
  font-size: 15px;
  font-weight: 500;
  color: var(--color-text);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-user {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.layout-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: var(--color-bg);
}
</style>
