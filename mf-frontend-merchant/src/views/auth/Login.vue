<template>
  <div class="auth-page">
    <div class="auth-panel">
      <h1>苗丰商家端</h1>
      <el-form :model="form" size="large" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-button type="primary" :loading="loading" class="submit-btn" @click="handleLogin">登录</el-button>
      </el-form>
      <div class="auth-link">还没有账号？<router-link to="/register">提交入驻申请</router-link></div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginMerchant } from '@/api/merchant'

const router = useRouter()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await loginMerchant(form)
    localStorage.setItem('merchantToken', res.data.token)
    localStorage.setItem('merchantInfo', JSON.stringify(res.data))
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #eef8f2, #dfeee5);
}

.auth-panel {
  width: 380px;
  padding: 32px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 12px 32px rgba(31, 45, 36, 0.12);
}

h1 {
  margin-bottom: 24px;
  font-size: 24px;
  text-align: center;
}

.submit-btn {
  width: 100%;
}

.auth-link {
  margin-top: 18px;
  text-align: center;
  color: var(--color-text-secondary);
}

.auth-link a {
  color: var(--color-primary);
}
</style>
