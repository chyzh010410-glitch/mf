<template><div class="page-container" v-if="product"><el-row :gutter="40"><el-col :span="12"><div class="img-main" :style="{background:'#e8f5e9',height:'400px',display:'flex',alignItems:'center',justifyContent:'center'}"><span style="font-size:120px">{{ product.productType==='fertilizer'?'🧪':'🌳' }}</span></div></el-col><el-col :span="12"><h1>{{ product.name }}</h1><p style="color:#999;margin:8px 0">{{ product.brand }}</p><div style="background:#fff8f0;padding:16px;border-radius:8px;margin:16px 0"><span style="font-size:28px;color:#e74c3c;font-weight:700">¥{{ product.price }}</span><span style="color:#999;text-decoration:line-through;margin-left:12px" v-if="product.originalPrice">¥{{ product.originalPrice }}</span></div><p style="margin:8px 0">库存：{{ product.stock }} | 销量：{{ product.salesCount }}</p><div style="margin:16px 0"><template v-if="product.stock > 0"><el-input-number v-model="quantity" :min="1" :max="product.stock" /> <span style="margin-left:8px;color:#999">{{ product.unit }}</span></template><el-tag v-else type="danger" size="large">已售罄</el-tag></div><div style="margin-top:20px;display:flex;gap:12px"><el-button type="primary" size="large" @click="handleBuy" :disabled="product.stock === 0">立即购买</el-button><el-button size="large" @click="handleAddCart" :disabled="product.stock === 0">加入购物车</el-button></div></el-col></el-row><el-descriptions title="商品详情" :column="2" border style="margin-top:30px"><el-descriptions-item label="类型">{{ {tree:'树苗',fertilizer:'化肥'}[product.productType] }}</el-descriptions-item><el-descriptions-item label="品牌">{{ product.brand }}</el-descriptions-item><el-descriptions-item label="单位">{{ product.unit }}</el-descriptions-item><el-descriptions-item label="运费">{{ product.freight ? '¥'+product.freight : '免运费' }}</el-descriptions-item></el-descriptions><div style="margin-top:20px" v-html="product.description"></div></div></template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail } from '@/api/product'
import request from '@/utils/request'
const route = useRoute(); const router = useRouter()
const product = ref(null); const quantity = ref(1)
onMounted(async () => { try { const res = await getProductDetail(route.params.id); if (res.code===200) product.value = res.data } catch {} })
const adding = ref(false)
const handleAddCart = async () => {
  adding.value = true
  try {
    const res = await request({ url:'/client/cart', method:'post', data:{ productId:product.value.id, quantity:quantity.value } })
    if (res.code === 200) ElMessage.success('已加入购物车')
    else ElMessage.error(res.msg || '操作失败')
  } catch {} finally { adding.value = false }
}
const handleBuy = () => {
  const items = [{
    productId: product.value.id,
    productName: product.value.name,
    productImage: product.value.coverImage,
    productType: product.value.productType,
    price: product.value.price,
    stock: product.value.stock,
    unit: product.value.unit,
    quantity: quantity.value
  }]
  router.push({ name: 'Checkout', state: { buyNowItems: items } })
}
</script>
