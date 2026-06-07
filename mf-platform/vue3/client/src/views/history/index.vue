<template>
  <div class="page-container">
    <h2 class="section-title">浏览历史</h2>
    <div v-if="items.length===0&&!loading" style="padding:80px;text-align:center;color:#999">暂无浏览记录</div>
    <div v-for="h in items" :key="h.id" class="history-item" @click="goTarget(h)">
      <el-tag size="small" :type="h.targetType==='product'?'warning':h.targetType==='encyclopedia'?'success':''">
        {{ h.targetType==='product'?'商品':h.targetType==='encyclopedia'?'百科':'文章' }}
      </el-tag>
      <span class="name">{{ h.targetName }}</span>
      <span class="time">{{ h.createTime?.substring(0,16) }}</span>
    </div>
    <el-button v-if="items.length" type="danger" plain style="margin-top:16px" @click="handleClear">清空历史</el-button>
    <el-pagination v-if="total>size" v-model:current-page="page" :page-size="size" :total="total"
      layout="prev,pager,next" background @current-change="fetchData" style="justify-content:center;margin-top:24px" />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'; import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getHistory, clearHistory } from '@/api/history'
const router=useRouter(); const items=ref([]); const page=ref(1); const size=ref(20); const total=ref(0); const loading=ref(false)
const fetchData=async()=>{ loading.value=true
  try{ const r=await getHistory({page:page.value,size:size.value}); if(r.code===200&&r.data){ items.value=r.data.records||[]; total.value=r.data.total||0 } }catch{} finally{loading.value=false} }
const goTarget=(h)=>{ const t=h.targetType; const id=h.targetId
  if(t==='product') router.push('/product/'+id)
  else if(t==='encyclopedia') router.push('/encyclopedia/'+id)
  else router.push('/article/'+id) }
const handleClear=()=>{ ElMessageBox.confirm('确认清空所有浏览记录？','提示',{type:'warning'}).then(async()=>{ await clearHistory(); items.value=[]; ElMessage.success('已清空') }).catch(()=>{}) }
onMounted(fetchData)
</script>
<style scoped>
.history-item{ display:flex;align-items:center;gap:12px;background:#fff;border-radius:8px;padding:14px 20px;margin-bottom:8px;border:1px solid #ebeef5;cursor:pointer }
.name{ flex:1;font-size:14px;color:#333 } .time{ font-size:12px;color:#bbb }
</style>
