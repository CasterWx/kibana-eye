<template>
  <div class="block">
    <el-timeline>
      <el-timeline-item v-for="(item,index) of timeline" :key="index" :timestamp="item.timestamp" placement="top">
        <el-card>
          <h4>{{ item.title }}</h4>
          <p>{{ item.content }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script>
export default {
  data() {
    return {
      timeline: [
        {
          timestamp: '2020/9/11',
          title: 'Init Project',
          content: 'Search script convert to Java project committed 2020/9/1 13:46'
        },
        {
          timestamp: '2020/9/12',
          title: 'Development delayed message',
          content: 'Time wheel committed 2020/9/12 15:46'
        },
        {
          timestamp: '2020/9/12',
          title: 'Complete the front-end interface',
          content: 'Development frontend project committed 2020/9/12 23:41'
        },
        {
          timestamp: '2019/9/13',
          title: 'Initially completed! Kibana eye V_1.0',
          content: 'Committed Kibana eye V_1.0 committed 2019/9/13 21:22'
        }
      ]
    }
  }
}
</script>
