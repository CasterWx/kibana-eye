<template>
  <div class="errPage-container">
    <div v-html="htmlInfo"></div>
  </div>
</template>

<script>
import { generReport } from "@/api/article";

export default {
  name: 'ErrorLog',
  data() {
    return {
      id: -1,
      htmlInfo: null
    }
  },
  methods: {
    fetchData(params) {
      generReport(params).then(response => {
        console.log(response.data)
        this.htmlInfo = response.data
        // this.list = response.data.items
      })
    }
  },
  created() {
    var qid = this.$route.query.id
    if (qid !== null) {
      this.id = qid
    }
    this.fetchData({ id: this.id })
  }
}
</script>

<style scoped>
  .errPage-container {
    padding: 30px;
  }
</style>
