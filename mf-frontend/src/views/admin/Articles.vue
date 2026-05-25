<template>
  <div class="page-container">
    <div class="search-bar">
      <el-form :model="queryForm" inline>
        <el-form-item label="标题">
          <el-input v-model="queryForm.keyword" placeholder="请输入标题" clearable @keyup.enter="handleSearch" style="width: 200px" />
        </el-form-item>
        <el-form-item label="发布">
          <el-select v-model="queryForm.isPublished" placeholder="全部" clearable style="width: 100px">
            <el-option label="已发布" :value="1" />
            <el-option label="未发布" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐">
          <el-select v-model="queryForm.isRecommend" placeholder="全部" clearable style="width: 100px">
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <span class="toolbar-title">文章管理</span>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增文章</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
      <el-table-column prop="summary" label="摘要" min-width="180" show-overflow-tooltip />
      <el-table-column prop="isPublished" label="发布" width="80" align="center">
        <template #default="{ row }">
          <el-switch
            :model-value="row.isPublished === 1"
            inline-prompt active-text="" inactive-text=""
            @change="() => handleTogglePublish(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="isTop" label="置顶" width="70" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="row.isTop === 1 ? 'warning' : 'info'">
            {{ row.isTop === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isRecommend" label="推荐" width="70" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="row.isRecommend === 1 ? 'danger' : 'info'">
            {{ row.isRecommend === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="70" align="center" />
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button type="warning" link size="small" @click="handleToggleTop(row)">置顶</el-button>
          <el-button type="success" link size="small" @click="handleToggleRecommend(row)">推荐</el-button>
          <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="queryForm.page" v-model:page-size="queryForm.size"
      :total="total" :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper" background
      @current-change="fetchData" @size-change="fetchData"
    />

    <el-dialog
      v-model="dialogVisible" :title="isEdit ? '编辑文章' : '新增文章'"
      width="720px" :close-on-click-modal="false" destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item label="文章标题" prop="title">
              <el-input v-model="formData.title" placeholder="请输入文章标题" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="摘要">
              <el-input v-model="formData.summary" type="textarea" :rows="2" placeholder="文章摘要" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面图">
              <el-input v-model="formData.coverImage" placeholder="图片URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签">
              <el-input v-model="formData.tags" placeholder="逗号分隔" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="文章内容" prop="content">
              <el-input v-model="formData.content" type="textarea" :rows="6" placeholder="文章正文内容" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发布">
              <el-switch v-model="formData.isPublished" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="置顶">
              <el-switch v-model="formData.isTop" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="推荐">
              <el-switch v-model="formData.isRecommend" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import {
  getArticlePage, getArticleDetail,
  saveArticle, updateArticle, deleteArticle,
  toggleArticlePublish, toggleArticleTop, toggleArticleRecommend
} from '@/api/admin'

const queryForm = reactive({ keyword: '', isPublished: null, isRecommend: null, isTop: null, page: 1, size: 10 })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const formRef = ref(null)
const editId = ref(null)

const defaultForm = () => ({
  title: '', summary: '', coverImage: '', tags: '',
  content: '', isPublished: 0, isTop: 0, isRecommend: 0
})

const formData = reactive(defaultForm())
const formRules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (params.isPublished === null || params.isPublished === '') delete params.isPublished
    if (params.isRecommend === null || params.isRecommend === '') delete params.isRecommend
    if (params.isTop === null || params.isTop === '') delete params.isTop
    if (!params.keyword) delete params.keyword
    const res = await getArticlePage(params)
    if (res.code === 200 && res.data) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch { /* ignore */ } finally { loading.value = false }
}

const handleSearch = () => { queryForm.page = 1; fetchData() }
const handleReset = () => {
  queryForm.keyword = ''; queryForm.isPublished = null
  queryForm.isRecommend = null; queryForm.isTop = null
  queryForm.page = 1; fetchData()
}

const handleAdd = () => {
  isEdit.value = false; editId.value = null
  Object.assign(formData, defaultForm())
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true; editId.value = row.id
  try {
    const res = await getArticleDetail(row.id)
    if (res.code === 200 && res.data) {
      Object.assign(formData, {
        title: res.data.title || '', summary: res.data.summary || '',
        coverImage: res.data.coverImage || '', tags: res.data.tags || '',
        content: res.data.content || '',
        isPublished: res.data.isPublished ?? 0,
        isTop: res.data.isTop ?? 0,
        isRecommend: res.data.isRecommend ?? 0
      })
    }
  } catch { /* ignore */ }
  dialogVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    isEdit.value
      ? await updateArticle(editId.value, formData)
      : await saveArticle(formData)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    fetchData()
  } catch { /* ignore */ } finally { saving.value = false }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除文章「${row.title}」？`, '删除确认',
    { confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    await deleteArticle(row.id)
    ElMessage.success('删除成功')
    fetchData()
  }).catch(() => {})
}

const handleTogglePublish = async (row) => {
  try {
    await toggleArticlePublish(row.id)
    row.isPublished = row.isPublished === 1 ? 0 : 1
    ElMessage.success(row.isPublished ? '已发布' : '已下架')
  } catch { /* ignore */ }
}

const handleToggleTop = async (row) => {
  try {
    await toggleArticleTop(row.id)
    row.isTop = row.isTop === 1 ? 0 : 1
    ElMessage.success('切换成功')
  } catch { /* ignore */ }
}

const handleToggleRecommend = async (row) => {
  try {
    await toggleArticleRecommend(row.id)
    row.isRecommend = row.isRecommend === 1 ? 0 : 1
    ElMessage.success('切换成功')
  } catch { /* ignore */ }
}

onMounted(() => { fetchData() })
</script>
