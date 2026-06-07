<template>
  <div class="page-container">
    <h2 class="section-title">优惠活动</h2>
    <div v-if="activities.length===0&&!loading" style="padding:80px;text-align:center;color:#999">暂无活动</div>
    <div v-for="a in activities" :key="a.id" class="act-card">
      <div class="act-cover" :style="{background:a.coverImage?'url('+a.coverImage+') center/cover':'#f0f9f4'}">
        <span v-if="!a.coverImage" style="font-size:48px">🎉</span>
      </div>
      <div class="act-body">
        <h3>{{ a.title }}</h3>
        <p>{{ a.description }}</p>
        <div class="act-meta">
          <el-tag size="small" type="warning">{{ a.status==='active'?'进行中':'已结束' }}</el-tag>
          <span>{{ a.startTime?.substring(0,10) }} ~ {{ a.endTime?.substring(0,10) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getActivities } from '@/api/activity'
const activities=ref([]); const loading=ref(false)
onMounted(async()=>{ loading.value=true
  try{ const r=await getActivities(); if(r.code===200) activities.value=r.data||[] }catch{} finally{loading.value=false} })
</script>
<style scoped>
.act-card{ background:#fff;border-radius:12px;overflow:hidden;margin-bottom:16px;display:flex;border:1px solid #ebeef5 }
.act-cover{ width:200px;height:140px;display:flex;align-items:center;justify-content:center;flex-shrink:0 }
.act-body{ padding:16px 20px;flex:1;display:flex;flex-direction:column;justify-content:center }
.act-body h3{ font-size:18px;margin:0 0 8px } .act-body p{ font-size:13px;color:#666;margin:0 0 10px;flex:1 }
.act-meta{ display:flex;align-items:center;gap:12px;font-size:12px;color:#999 }
</style>
