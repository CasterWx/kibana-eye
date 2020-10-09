<template>
  <el-table :data="list" border fit highlight-current-row style="width: 100%">
    <el-table-column
      v-loading="loading"
      align="center"
      label="ID"
      width="65"
      element-loading-text="请给我点时间！"
    >
      <template slot-scope="scope">
        <span>{{ scope.row.id }}</span>
      </template>
    </el-table-column>

    <el-table-column min-width="150px" label="Application">
      <template slot-scope="{row}">
        <span>{{ row.application }}</span>
      </template>
    </el-table-column>
    <el-table-column min-width="100px" label="Index">
      <template slot-scope="{row}">
        <span>{{ row.name }}</span>
      </template>
    </el-table-column>

    <el-table-column min-width="300px" label="Query">
      <template slot-scope="{row}">
        <router-link :to="'/report-log/log/?id='+row.id" class="link-type">
          <span>{{ row.query.substring(0, 140) + '......' }}</span>
        </router-link>
      </template>
    </el-table-column>

    <el-table-column width="180px" align="center" label="Date">
      <template slot-scope="scope">
        <span>{{ scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
      </template>
    </el-table-column>

<!--    <el-table-column min-width="300px" label="Title">-->
<!--      <template slot-scope="{row}">-->
<!--        <span>{{ row.title }}</span>-->
<!--        <el-tag>{{ row.status }}</el-tag>-->
<!--      </template>-->
<!--    </el-table-column>-->

    <el-table-column width="110px" align="center" label="Type">
      <template slot-scope="scope">
        <span>{{ scope.row.type }}</span>
      </template>
    </el-table-column>

<!--    <el-table-column width="120px" label="Importance">-->
<!--      <template slot-scope="scope">-->
<!--        <svg-icon v-for="n in +scope.row.importance" :key="n" icon-class="star" />-->
<!--      </template>-->
<!--    </el-table-column>-->

<!--    <el-table-column align="center" label="Readings" width="95">-->
<!--      <template slot-scope="scope">-->
<!--        <span>{{ scope.row.pageviews }}</span>-->
<!--      </template>-->
<!--    </el-table-column>-->

    <el-table-column class-name="status-col" label="Status" width="110">
      <template slot-scope="{row}">
        <el-tag :type="row.status | statusFilter">
          {{ row.status }}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import { queryLogListByType } from '@/api/article'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        200: 'success',
        500: 'danger',
        400: 'danger'
      }
      if (status !== 200 && status !== 500 && status !== 400)
        return 'danger'
      return statusMap[status]
    }
  },
  props: {
    type: {
      type: String,
      default: 'sql'
    }
  },
  data() {
    return {
      list: null,
      listQuery: {
        page: 1,
        limit: 5,
        type: this.type,
        sort: '+id'
      },
      loading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.$emit('create') // for test
      queryLogListByType(this.listQuery).then(response => {
        if (response.data != null) {
          this.list = response.data.item
          console.log(this.list)
        }
        this.loading = false
      })
    }
  }
}
</script>

