<template>
  <div class="app-shell">
    <header class="app-header">
      <div class="header-inner">
        <router-link to="/home" class="logo">🌱 苗丰施肥</router-link>
        <div class="header-search">
          <el-input v-model="searchKeyword" placeholder="搜索商品、肥料、树木百科..." size="large" clearable @keyup.enter="doSearch">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
        </div>
        <nav class="header-nav">
          <router-link to="/home">首页</router-link>
          <router-link to="/products">商品商城</router-link>
          <router-link to="/encyclopedia">树木百科</router-link>
          <router-link to="/articles">科普文章</router-link>
          <router-link to="/activities">优惠活动</router-link>
          <router-link to="/ai" style="color:#e6a23c">AI 客服</router-link>
        </nav>
        <div class="header-actions">
          <el-badge v-if="unreadCount" :value="unreadCount" :max="99">
            <el-button circle :icon="Bell" @click="$router.push('/messages')" />
          </el-badge>
          <router-link to="/cart" class="cart-link">
            <el-button circle :icon="ShoppingCart" />
            <span v-if="cartCount" class="cart-badge">{{ cartCount }}</span>
          </router-link>
          <template v-if="authStore.token">
            <el-dropdown>
              <span class="user-name">{{ authStore.userInfo?.nickname || '用户' }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/user/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/orders')">我的订单</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/favorites')">我的收藏</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/messages')">消息中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <el-button v-else type="primary" @click="$router.push('/login')">登录/注册</el-button>
        </div>
      </div>
    </header>
    <main class="app-main"><router-view /></main>
    <footer class="app-footer">
      <div class="footer-inner">
        <div class="footer-links">
          <router-link to="/faq">帮助中心</router-link>
          <router-link to="/feedback">意见反馈</router-link>
          <router-link to="/articles">施肥科普</router-link>
        </div>
        <p>© 2026 苗丰施肥管控平台 版权所有</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { Search, Bell, ShoppingCart } from '@element-plus/icons-vue'
import { getUnreadCount } from '@/api/message'
// 第一步：先从 vue 导入 provide（必须写！）
import { provide } from 'vue'

const router = useRouter()
const authStore = useAuthStore()
const searchKeyword = ref('')
const cartCount = ref(0)
const unreadCount = ref(0)

const fetchUnreadCount = async () => {
  try {
    const res = await getUnreadCount()
    unreadCount.value = res.data || 0
  } catch {}
}
provide('fetchUnreadCount', fetchUnreadCount)

const doSearch = () => {
  if (searchKeyword.value) router.push({ path: '/products', query: { keyword: searchKeyword.value } })
}

const handleLogout = () => { authStore.logout(); router.push('/login') }

onMounted(() => { fetchUnreadCount() })
</script>

<style scoped>
.app-shell { display: flex; flex-direction: column; min-height: 100vh; }
.app-header { background: var(--color-white); box-shadow: 0 2px 8px rgba(0,0,0,0.06); position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: var(--max-width); margin: 0 auto; display: flex; align-items: center; height: var(--header-height); padding: 0 20px; gap: 24px; }
.logo { font-size: 20px; font-weight: 700; color: var(--color-primary); white-space: nowrap; }
.header-search { flex: 1; max-width: 400px; }
.header-nav { display: flex; gap: 20px; font-size: 14px; white-space: nowrap; }
.header-nav a { color: var(--color-text-secondary); transition: color 0.2s; }
.header-nav a:hover, .header-nav a.router-link-exact-active { color: var(--color-primary); }
.header-actions { display: flex; align-items: center; gap: 12px; }
.cart-link { position: relative; }
.cart-badge { position: absolute; top: -6px; right: -6px; background: var(--color-danger); color: #fff; font-size: 10px; min-width: 16px; height: 16px; border-radius: 8px; display: flex; align-items: center; justify-content: center; }
.user-name { cursor: pointer; font-size: 14px; color: var(--color-primary); }
.app-main { flex: 1; }
.app-footer { background: var(--color-primary-dark); color: rgba(255,255,255,0.7); padding: 30px 0; margin-top: 40px; }
.footer-inner { max-width: var(--max-width); margin: 0 auto; text-align: center; }
.footer-links { display: flex; justify-content: center; gap: 30px; margin-bottom: 12px; }
.footer-links a { color: rgba(255,255,255,0.7); }
.footer-links a:hover { color: #fff; }
</style>
