<template>
  <div class="page-container">
    <div class="search-bar">
      <el-form :model="q" inline>
        <el-form-item label="关键词"><el-input v-model="q.keyword" placeholder="搜索配置键" clearable @keyup.enter="doSearch" style="width:180px" /></el-form-item>
        <el-form-item label="分组"><el-select v-model="q.configGroup" placeholder="全部分组" clearable style="width:130px">
          <el-option label="通用" value="general" /><el-option label="支付" value="payment" /><el-option label="积分" value="points" /><el-option label="活动" value="activity" />
        </el-select></el-form-item>
        <el-form-item><el-button type="primary" :icon="Search" @click="doSearch">搜索</el-button><el-button @click="doReset">重置</el-button></el-form-item>
      </el-form>
    </div>
    <div class="toolbar"><span class="toolbar-title">平台设置</span><el-button type="primary" :icon="Plus" @click="handleAdd">新增配置</el-button></div>
    <el-table v-loading="loading" :data="table" border stripe>
      <el-table-column prop="id" label="ID" width="65" align="center" />
      <el-table-column prop="configKey" label="配置键" width="160" show-overflow-tooltip />
      <el-table-column prop="configValue" label="配置值" min-width="180" show-overflow-tooltip />
      <el-table-column prop="configGroup" label="分组" width="90" align="center" />
      <el-table-column prop="description" label="说明" width="140" show-overflow-tooltip />
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template #default="{row}">
          <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="q.page" v-model:page-size="q.size" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next,jumper" background @current-change="fetch" @size-change="fetch" />
    <el-dialog v-model="vis" :title="edit?'编辑配置':'新增配置'" width="480px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="fr" :model="f" :rules="rls" label-width="80px">
        <el-form-item label="配置键" prop="configKey"><el-input v-model="f.configKey" placeholder="如: site_name" /></el-form-item>
        <el-form-item label="配置值" prop="configValue"><el-input v-model="f.configValue" type="textarea" :rows="3" placeholder="配置值" /></el-form-item>
        <el-form-item label="分组"><el-select v-model="f.configGroup" placeholder="请选择" style="width:100%">
          <el-option label="通用" value="general" /><el-option label="支付" value="payment" /><el-option label="积分" value="points" /><el-option label="活动" value="activity" />
        </el-select></el-form-item>
        <el-form-item label="说明"><el-input v-model="f.description" placeholder="配置说明" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="vis=false">取消</el-button><el-button type="primary" :loading="saving" @click="handleSave">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'; import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getConfigPage, getConfigDetail, saveConfig, updateConfig, deleteConfig } from '@/api/admin'
const q=reactive({ keyword:'', configGroup:'', page:1, size:10 }); const loading=ref(false); const table=ref([]); const total=ref(0)
const vis=ref(false); const edit=ref(false); const saving=ref(false); const fr=ref(null); const eid=ref(null)
const df=()=>({ configKey:'', configValue:'', configGroup:'', description:'' }); const f=reactive(df())
const rls={ configKey:[{required:true,message:'请输入配置键'}], configValue:[{required:true,message:'请输入配置值'}] }
const fetch=async()=>{ loading.value=true; try{ const p={...q}; if(!p.keyword) delete p.keyword; if(!p.configGroup) delete p.configGroup
  const r=await getConfigPage(p); if(r.code===200&&r.data){ table.value=r.data.records||[]; total.value=r.data.total||0 } } catch{} finally{loading.value=false} }
const doSearch=()=>{ q.page=1; fetch() }; const doReset=()=>{ q.keyword=''; q.configGroup=''; q.page=1; fetch() }
const handleAdd=()=>{ edit.value=false; eid.value=null; Object.assign(f,df()); vis.value=true }
const handleEdit=async(r)=>{ edit.value=true; eid.value=r.id
  try{ const x=await getConfigDetail(r.id); if(x.code===200&&x.data) Object.assign(f,{ configKey:x.data.configKey||'', configValue:x.data.configValue||'', configGroup:x.data.configGroup||'', description:x.data.description||'' }) } catch{}
  vis.value=true }
const handleSave=async()=>{ const v=await fr.value.validate().catch(()=>false); if(!v) return; saving.value=true
  try{ edit.value?await updateConfig(eid.value,f):await saveConfig(f); ElMessage.success(edit.value?'更新成功':'新增成功'); vis.value=false; fetch() } catch{} finally{saving.value=false} }
const handleDelete=(r)=>{ ElMessageBox.confirm(`确认删除「${r.configKey}」？`,'删除确认',{type:'warning'}).then(async()=>{ await deleteConfig(r.id); ElMessage.success('已删除'); fetch() }).catch(()=>{}) }
onMounted(fetch)
</script>
