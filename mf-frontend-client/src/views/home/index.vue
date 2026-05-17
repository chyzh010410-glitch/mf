<template>
  <div class="home-page">
    <!-- Banner轮播 -->
    <section class="banner-section">
      <el-carousel height="420px" indicator-position="outside">
        <el-carousel-item v-for="b in banners" :key="b.targetId">
          <div class="banner-slide" :style="{ backgroundColor: b.bgColor || '#e8f5e9' }">
            <div class="banner-text">
              <h2>{{ b.title }}</h2>
              <el-button type="primary" size="large" @click="$router.push('/activities/' + b.targetId)">查看详情</el-button>
            </div>
            <div class="banner-icon">🌿</div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <!-- 分类导航 -->
    <section class="category-nav">
      <div class="page-container">
        <div class="cat-grid">
          <div class="cat-item" v-for="c in categories" :key="c.name" @click="$router.push('/products?productType=' + c.type)">
            <span class="cat-icon">{{ c.icon }}</span>
            <span>{{ c.name }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐商品 -->
    <section class="product-section" v-if="recommendProducts.length">
      <div class="page-container">
        <h3 class="section-title">🌟 推荐商品</h3>
        <div class="product-grid">
          <div class="product-card" v-for="p in recommendProducts" :key="p.id" @click="$router.push('/product/' + p.id)">
            <div class="product-img" :style="{ background: '#e8f5e9' }">
              <span style="font-size:48px">{{ p.productType === 'fertilizer' ? '🧪' : '🌳' }}</span>
            </div>
            <div class="product-info">
              <h4>{{ p.name }}</h4>
              <div class="product-price"><span class="price">¥{{ p.price }}</span><span class="sales">已售{{ p.salesCount }}</span></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐文章 -->
    <section class="article-section" v-if="recommendArticles.length">
      <div class="page-container">
        <h3 class="section-title">📖 科普推荐</h3>
        <div class="article-grid">
          <div class="article-card" v-for="a in recommendArticles" :key="a.id" @click="$router.push('/article/' + a.id)">
            <div class="article-cover" :style="{ background: '#f0f9f4' }"><span style="font-size:36px">📄</span></div>
            <div class="article-info"><h4>{{ a.title }}</h4><p>{{ a.summary }}</p></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const banners = ref([{ targetId: 1, title: '新品树苗限时特惠', bgColor: '#e8f5e9' }, { targetId: 2, title: '有机肥买三送一', bgColor: '#f1f8e9' }])
const categories = ref([
  { name: '树苗', icon: '🌳', type: 'tree' }, { name: '化肥', icon: '🧪', type: 'fertilizer' },
  { name: '百科', icon: '📚', type: 'encyclopedia' }, { name: '文章', icon: '📰', type: 'articles' },
  { name: '活动', icon: '🎉', type: 'activities' }, { name: '帮助', icon: '❓', type: 'faq' }
])
const recommendProducts = ref([])
const recommendArticles = ref([])

onMounted(async () => {
  try {
    const { default: request } = await import('@/utils/request')
    const res = await request({ url: '/client/home', method: 'get' })
    if (res.code === 200 && res.data) {
      recommendProducts.value = res.data.recommendedProducts || []
      recommendArticles.value = res.data.recommendedArticles || []
    }
  } catch {}
})
</script>

<style scoped>
.banner-slide { display: flex; align-items: center; justify-content: space-between; padding: 0 120px; height: 100%; }
.banner-text h2 { font-size: 32px; color: var(--color-primary-dark); margin-bottom: 20px; }
.banner-icon { font-size: 120px; }
.page-container { max-width: var(--max-width); margin: 0 auto; padding: 40px 20px; }
.cat-grid { display: grid; grid-template-columns: repeat(6, 1fr); gap: 16px; }
.cat-item { display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 20px; background: var(--color-white); border-radius: 8px; cursor: pointer; transition: all 0.2s; font-size: 14px; }
.cat-item:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); color: var(--color-primary); }
.cat-icon { font-size: 32px; }
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.product-card { background: var(--color-white); border-radius: 8px; overflow: hidden; cursor: pointer; transition: box-shadow 0.2s; }
.product-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.product-img { height: 180px; display: flex; align-items: center; justify-content: center; }
.product-info { padding: 12px 16px; }
.product-info h4 { font-size: 15px; margin-bottom: 8px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-price { display: flex; justify-content: space-between; align-items: center; }
.price { font-size: 18px; font-weight: 600; color: var(--color-danger); }
.sales { font-size: 12px; color: var(--color-text-secondary); }
.article-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.article-card { display: flex; background: var(--color-white); border-radius: 8px; overflow: hidden; cursor: pointer; }
.article-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
.article-cover { width: 160px; height: 120px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.article-info { padding: 16px; }
.article-info h4 { font-size: 16px; margin-bottom: 8px; }
.article-info p { font-size: 13px; color: var(--color-text-secondary); display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
</style>
