<template>
  <div class="page-container">
    <h2 class="section-title">意见反馈</h2>
    <el-form ref="formRef" :model="form" :rules="rules" style="max-width:500px" label-width="60px">
      <el-form-item label="类型" prop="type">
        <el-select v-model="form.type" style="width:100%">
          <el-option label="功能建议" value="suggestion" />
          <el-option label="问题反馈" value="bug" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.contact" placeholder="手机号或邮箱，方便我们回复" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请详细描述您的建议或问题" maxlength="500" show-word-limit />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">提交反馈</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue'; import { ElMessage } from 'element-plus'
import { submitFeedback } from '@/api/feedback'
const formRef=ref(null); const submitting=ref(false)
const form=reactive({ type:'', contact:'', content:'' })
const rules={ type:[{required:true,message:'请选择类型'}], content:[{required:true,message:'请输入内容'}] }
const handleSubmit=async()=>{ const v=await formRef.value.validate().catch(()=>false); if(!v) return; submitting.value=true
  try{ const r=await submitFeedback(form); if(r.code===200){ ElMessage.success('感谢反馈！'); form.type=''; form.contact=''; form.content='' } } catch{} finally{submitting.value=false} }
</script>
