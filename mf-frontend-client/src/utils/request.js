import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({ baseURL: 'http://localhost:8080', timeout: 15000 })

service.interceptors.request.use((config) => {
  const token = localStorage.getItem('clientToken')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
}, (error) => Promise.reject(error))

service.interceptors.response.use((response) => {
  const res = response.data
  if (res.code && res.code !== 200) {
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg))
  }
  return res
}, (error) => {
  if (error.response?.status === 401) {
    localStorage.removeItem('clientToken')
    ElMessage.error('登录已过期，请重新登录')
    window.location.href = '/login'
  } else {
    ElMessage.error(error.response?.data?.msg || '网络异常')
  }
  return Promise.reject(error)
})

export default service
