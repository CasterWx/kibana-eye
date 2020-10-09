<template>
  <div class="app-container">
    <el-input v-model="filename" placeholder="Please enter the file name (default excel-list)" style="width:350px;" prefix-icon="el-icon-document" />
    <el-button :loading="downloadLoading" style="margin-bottom:20px" type="primary" icon="el-icon-document" @click="handleDownload">
      导出选中的记录
    </el-button>
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      element-loading-text="拼命加载中"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" />
      <el-table-column align="center" label="Id" width="195">
        <template slot-scope="scope">
          {{ scope.row.traceId }}
        </template>
      </el-table-column>
      <el-table-column width="150" label="手机号">
        <template slot-scope="scope">
          {{ scope.row.mobile }}
        </template>
      </el-table-column>
      <el-table-column label="客户端" width="170" align="center">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.clientType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="失败次数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.errorNum }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="PDate" width="220">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.logTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="IP" width="130" align="center">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="环境" width="130" align="center">
        <template slot-scope="scope">
          {{ scope.row.env }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getExcelAll } from '@/api/article'

export default {
  name: 'SelectExcel',
  data() {
    return {
      id: -1,
      list: null,
      listLoading: false,
      multipleSelection: [],
      downloadLoading: false,
      filename: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getExcelAll().then(response => {
        console.log(response.data)
        this.list = response.data
        this.listLoading = false
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDownload() {
      if (this.multipleSelection.length) {
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['Id', '手机号', '客户端', '失败次数', '时间', 'ip', '环境']
          const filterVal = ['traceId', 'mobile', 'clientType', 'errorNum', 'logTime', 'ip', 'env']
          const list = this.multipleSelection
          const data = this.formatJson(filterVal, list)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: this.filename
          })
          // this.$refs.multipleTable.clearSelection()
          this.downloadLoading = false
        })
      } else {
        this.$message({
          message: 'Please select at least one item',
          type: 'warning'
        })
      }
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]))
    }
  }
}
</script>
