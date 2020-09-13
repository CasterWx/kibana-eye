<template>
  <div class="app-container">
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column align="center" label="ID" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="Application">
        <template slot-scope="scope">
          <span>{{ scope.row.application }}</span>
        </template>
      </el-table-column>

      <el-table-column width="120px" align="center" label="Index">
        <template slot-scope="scope">
          <span>{{ scope.row.indexName }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="Type">
        <template slot-scope="scope">
          <span>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column width="180px" align="center" label="Date">
        <template slot-scope="scope">
          <span>{{ scope.row.executeTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="收件人" width="110">
        <template slot-scope="{row}">
          <el-tag :type="row.sendTo | statusFilter">
            {{ row.sendTo }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="抄送" width="110">
        <template slot-scope="{row}">
          <el-tag :type="row.sendCc | statusFilter">
            {{ row.sendCc }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column min-width="300px" label="Query">
        <template slot-scope="{row}">
          <router-link :to="'/example/edit/'+row.id" class="link-type">
            <span>{{ row.query.substring(0, 100) + '.......' }}</span>
          </router-link>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions" width="120">
        <template slot-scope="{row}">
            <el-button type="primary" size="small" icon="el-icon-run" @click="runScript(row.id)">
              Run
            </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import { queryJobList, runJob } from '@/api/article'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'ArticleList',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    runScript(id) {
      this.listLoading = true
      runJob({ id: id }).then(response => {
        var that = this
        setTimeout(function() {
          that.$message({
            message: response.message,
            type: 'success'
          })
          that.listLoading = false
        }, 1000)
      })
    },
    getList() {
      this.listLoading = true
      queryJobList(this.listQuery).then(response => {
        console.log(response)
        this.list = response.data.item
        this.total = response.data.total
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
