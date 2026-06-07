<template>
  <div class="page-container" v-loading="loading">
    <div class="back-bar">
      <el-button text :icon="ArrowLeft" @click="$router.push('/orders')">返回订单列表</el-button>
    </div>
    <div v-if="order">
      <div class="status-header">
        <h2 class="section-title" style="margin-bottom:8px">订单详情</h2>
        <el-tag size="large" :type="statusType[order.status]">{{ statusMap[order.status] || order.status }}</el-tag>
      </div>
      <el-card style="margin-bottom:16px">
        <template #header><span>订单信息</span></template>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="订单编号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ order.createTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="商品总额">¥{{ (order.totalAmount||0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="运费">¥{{ (order.freightAmount||0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="优惠金额">¥{{ (order.discountAmount||0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="实付金额"><span style="font-size:16px;color:#e74c3c;font-weight:600">¥{{ (order.payAmount||0).toFixed(2) }}</span></el-descriptions-item>
          <el-descriptions-item label="支付方式" v-if="order.paymentMethod">{{ order.paymentMethod }}</el-descriptions-item>
          <el-descriptions-item label="支付时间" v-if="order.payTime">{{ order.payTime }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2" v-if="order.addressSnapshot">{{ order.addressSnapshot }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
      <el-card style="margin-bottom:16px">
        <template #header><span>商品明细</span></template>
        <el-table :data="order.items||[]" border stripe size="small">
          <el-table-column label="商品" min-width="160">
            <template #default="{row}">{{ row.productName }}</template>
          </el-table-column>
          <el-table-column prop="price" label="单价" width="100" align="center">
            <template #default="{row}">¥{{ (row.price||0).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" align="center" />
          <el-table-column label="小计" width="100" align="center">
            <template #default="{row}">¥{{ (row.totalPrice||0).toFixed(2) }}</template>
          </el-table-column>
        </el-table>
      </el-card>
      <div style="text-align:right">
        <el-button v-if="order.status==='pending_pay'" type="danger" @click="handleCancel">取消订单</el-button>
        <el-button v-if="order.status==='shipped'" type="primary" @click="handleConfirm">确认收货</el-button>
      </div>
    </div>
    <el-empty v-if="!loading && !order" description="订单不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getOrderDetail, cancelOrder, confirmOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

const statusMap = { pending_pay:'待付款', pending_ship:'待发货', shipped:'已发货', completed:'已完成', cancelled:'已取消' }
const statusType = { pending_pay:'warning', pending_ship:'', shipped:'', completed:'success', cancelled:'info' }

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getOrderDetail(route.params.id)
    if (res.code === 200) order.value = res.data
  } catch {} finally { loading.value = false }
}

const handleCancel = async () => {
  try {
    await ElMessageBox.prompt('请输入取消原因', '取消订单', { confirmButtonText:'确认取消', type:'warning' }).then(async ({value}) => {
      await cancelOrder(order.value.id, { reason: value })
      ElMessage.success('订单已取消')
      fetchDetail()
    }).catch(() => {})
  } catch {}
}

const handleConfirm = async () => {
  try {
    await ElMessageBox.confirm('确认已收到商品？', '确认收货', { type:'success' })
    await confirmOrder(order.value.id)
    ElMessage.success('已确认收货')
    fetchDetail()
  } catch {}
}

onMounted(fetchDetail)
</script>

<style scoped>
.back-bar { margin-bottom: 12px; }
.status-header { display: flex; align-items: center; gap: 12px; }
</style>
