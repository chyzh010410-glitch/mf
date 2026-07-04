<template>
  <div class="layout">
    <el-aside width="220px" class="layout-sidebar">
      <div class="sidebar-header">苗丰商家端</div>
      <el-menu router :default-active="activeMenu" background-color="#1b3a2a" text-color="#c0d8c8" active-text-color="#fff">
        <el-menu-item index="/dashboard"><el-icon><DataBoard /></el-icon><span>首页概览</span></el-menu-item>
        <el-menu-item index="/products"><el-icon><Goods /></el-icon><span>商品管理</span></el-menu-item>
        <el-menu-item index="/orders"><el-icon><Tickets /></el-icon><span>订单管理</span></el-menu-item>
        <el-menu-item index="/profile"><el-icon><Shop /></el-icon><span>店铺资料</span></el-menu-item>
      </el-menu>
    </el-aside>
    <div class="layout-main">
      <header class="layout-header">
        <span class="header-title">{{ pageTitle }}</span>
        <div class="header-right">
          <span>{{ merchantInfo?.shopName || merchantInfo?.username || '商家' }}</span>
          <el-button type="danger" text size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <main class="layout-content"><router-view /></main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { DataBoard, Goods, Tickets, Shop } from '@element-plus/icons-vue'
import { logoutMerchant } from '@/api/merchant'

const route = useRoute()
const router = useRouter()
const merchantInfo = computed(() => JSON.parse(localStorage.getItem('merchantInfo') || 'null'))
const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta?.title || '')

const handleLogout = async () => {
  try {
    await logoutMerchant()
  } catch {
    // Keep local logout reliable even if the token is already expired.
  }
  localStorage.removeItem('merchantToken')
  localStorage.removeItem('merchantInfo')
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.layout-sidebar {
  background: #1b3a2a;
}

.sidebar-header {
  height: var(--header-height);
  line-height: var(--header-height);
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  text-align: center;
}

.layout-sidebar .el-menu {
  border-right: none;
}

.layout-sidebar .el-menu-item.is-active {
  background: var(--color-primary) !important;
}

.layout-main {
  display: flex;
  flex: 1;
  flex-direction: column;
  min-width: 0;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--header-height);
  padding: 0 20px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.header-title {
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 14px;
  color: var(--color-text-secondary);
}

.layout-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
