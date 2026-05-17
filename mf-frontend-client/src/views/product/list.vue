<template><div class="page-container"><h2 class="section-title">商品商城</h2><el-tabs v-model="productType" @tab-change="fetchList"><el-tab-pane label="全部" name="" /><el-tab-pane label="树苗" name="tree" /><el-tab-pane label="化肥" name="fertilizer" /></el-tabs><el-row :gutter="16"><el-col :span="6" v-for="p in products" :key="p.id" style="margin-bottom:16px"><div class="product-card" @click="$router.push('/product/'+p.id)"><div class="img-place" :style="{background:'#e8f5e9'}"><span style="font-size:64px">{{ p.productType==='fertilizer'?'🧪':'🌳' }}</span></div><div style="padding:12px"><h4>{{ p.name }}</h4><p style="font-size:12px;color:#999;margin:4px 0">{{ p.brand }}</p><div style="display:flex;justify-content:space-between;align-items:center"><span style="font-size:18px;color:#e74c3c;font-weight:600">¥{{ p.price }}</span><span style="font-size:12px;color:#999">已售{{ p.salesCount }}</span></div></div></div></el-col></el-row><el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" :page-sizes="[12,24]" background layout="prev,pager,next" @current-change="fetchList" @size-change="fetchList" style="justify-content:center;margin-top:24px" /></div></template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductList } from '@/api/product'
const route = useRoute()
const products = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(12)
const productType = ref(route.query.productType || '')
const fetchList = async () => { try { const res = await getProductList({ page: page.value, size: 12, productType: productType.value || undefined, keyword: route.query.keyword }); if (res.code===200&&res.data) { products.value = res.data.records||[]; total.value = res.data.total||0 } } catch {} }
onMounted(() => fetchList())
</script>
<style scoped>.product-card{background:#fff;border-radius:8px;overflow:hidden;cursor:pointer;transition:box-shadow .2s}.product-card:hover{box-shadow:0 4px 16px rgba(0,0,0,.1)}.img-place{height:200px;display:flex;align-items:center;justify-content:center}</style>
