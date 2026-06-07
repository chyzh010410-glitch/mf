<template>
  <div class="page-container" style="max-width:800px">
    <h2 class="section-title">帮助中心</h2>
    <el-collapse v-if="faqs.length" v-loading="loading">
      <el-collapse-item v-for="f in faqs" :key="f.id" :title="f.question">
        <p style="color:#444;line-height:1.8;white-space:pre-wrap">{{ f.answer }}</p>
      </el-collapse-item>
    </el-collapse>
    <div v-if="faqs.length===0&&!loading" style="padding:80px;text-align:center;color:#999">暂无常见问题</div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getFaqList } from '@/api/faq'
const faqs=ref([]); const loading=ref(false)
onMounted(async()=>{ loading.value=true
  try{ const r=await getFaqList(); if(r.code===200) faqs.value=r.data||[] }catch{} finally{loading.value=false} })
</script>
