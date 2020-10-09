<template>
  <div class="createPost-container">
    <el-form ref="postForm" :model="postForm" :rules="rules" class="form-container">

      <sticky :z-index="10" :class-name="'sub-navbar '+postForm.status">
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">
          提交
        </el-button>
        <el-button v-loading="loading" type="warning" @click="draftForm">
          取消
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-row>
          <el-col :span="24">
            <el-form-item label="索引名称">
              <el-autocomplete
                popper-class="my-autocomplete"
                v-model="postForm.indexName"
                :fetch-suggestions="querySearch"
                placeholder="请输入要查询的索引信息"
                @select="handleSelect">
                <i
                  class="el-icon-edit el-input__icon"
                  slot="suffix"
                  @click="handleIconClick">
                </i>
                <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
                  <span class="addr">{{ item.address }}</span>
                </template>
              </el-autocomplete>
            </el-form-item>
            <el-form-item label="应用名称">
              <el-autocomplete
                popper-class="my-autocomplete"
                v-model="postForm.application"
                :fetch-suggestions="querySearchApp"
                placeholder="请输入要查询的应用"
                @select="handleSelect">
                <i
                  class="el-icon-edit el-input__icon"
                  slot="suffix"
                  @click="handleIconClick">
                </i>
                <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
                </template>
              </el-autocomplete>
            </el-form-item>

            <div class="postInfo-container">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="查询类型">
                    <el-select v-model="postForm.region" placeholder="选择查询类型" @change="handleRegin($event, 1)">
                      <el-option label="SQL执行耗时" value="sql" />
                      <el-option label="RPC调用失败统计" value="rpc" />
                      <el-option label="HTTP请求失败率" value="http_status" />
                      <el-option label="WEBAPP请求耗时" value="webapp_time" />
                      <el-option label="PYTHON脚本" value="python" />
                    </el-select>
                  </el-form-item>
                  <!--                  <el-form-item label-width="60px" label="Author:" class="postInfo-container-item">-->
                  <!--                    <el-select v-model="postForm.author" :remote-method="getRemoteUserList" filterable default-first-option remote placeholder="Search user">-->
                  <!--                      <el-option v-for="(item,index) in userListOptions" :key="item+index" :label="item" :value="item" />-->
                  <!--                    </el-select>-->
                  <!--                  </el-form-item>-->
                </el-col>

                <el-col :span="10">
                  <el-form-item label-width="120px" label="执行时间:" class="postInfo-container-item">
                    <el-date-picker v-model="displayTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="Select date and time" />
                  </el-form-item>
                </el-col>

                <el-col :span="5">
                  <el-form-item label="是否每日重复执行">
                    <el-switch v-model="postForm.cycle" />
                  </el-form-item>
                </el-col>

              </el-row>

              <el-col :span="15">
                <el-form-item label="通知方式">
                  <el-checkbox-group v-model="postForm.notType">
                    <el-checkbox label="邮箱" name="mail" />
                    <el-checkbox label="钉钉" name="ding" />
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item label="收件人">
                  <el-input v-model="postForm.sendTo" placeholder="收件人邮箱" />
                </el-form-item>
                <el-form-item label="抄送">
                  <el-input v-model="postForm.sendCc" placeholder="抄送人邮箱" />
                </el-form-item>
              </el-col>
            </div>
          </el-col>
        </el-row>

        <el-form-item style="margin-bottom: 20px;" label-width="70px" label="描述:">
          <el-input v-model="postForm.contentShort" :rows="1" type="textarea" class="article-textarea" autosize placeholder="此处填写关于该Job的描述信息" />
          <span v-show="contentShortLength" class="word-counter">{{ contentShortLength }}words</span>
        </el-form-item>

        <el-form-item label="使用富文本编辑器">
          <el-switch v-model="queryShow" />
        </el-form-item>
        <el-row>
          <el-form-item v-show="editorShow && queryShow" prop="content" style="margin-bottom: 30px;">
            <Tinymce ref="editor" v-model="postForm.content"  />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item v-show="editorShow && !queryShow" label="Query">
            <el-input class="text-input" v-model="postForm.content" type="textarea" height="500px" style="min-height: 500px"/>
            <vue-json-editor
              v-model="jsonInfoObject"
              :show-btns="true"
              :mode="'code'"
              lang="zh"
              @json-change="onJsonChange"
              @json-save="onJsonSave"
              @has-error="onError"
            />
          </el-form-item>
        </el-row>

        <el-form-item v-show="!editorShow" prop="image_uri" style="margin-bottom: 30px;">
          <Upload v-model="postForm.image_uri" />
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import Upload from '@/components/Upload/SingleImage3'
import Sticky from '@/components/Sticky' // 粘性header组件
import { validURL } from '@/utils/validate'
import { fetchArticle, createArticle, queryTemplate } from '@/api/article'
import vueJsonEditor from 'vue-json-editor'

const defaultForm = {
  id: -1,
  status: 'draft',
  indexName: '',
  sendTo: '',
  sendCc: '',
  application: '', // 应用名称
  content: '', // 文章内容
  contentShort: '', // 文章摘要
  notType: [], // 通知方式
  cycle: true,
  region: '',
  executeTime: undefined // 前台展示时间
}

export default {
  name: 'ArticleDetail',
  components: { vueJsonEditor, Tinymce, Upload, Sticky },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateRequire = (rule, value, callback) => {
      if (value === '') {
        this.$message({
          message: rule.field + '为必传项',
          type: 'error'
        })
        callback(new Error(rule.field + '为必传项'))
      } else {
        callback()
      }
    }
    const validateSourceUri = (rule, value, callback) => {
      if (value) {
        if (validURL(value)) {
          callback()
        } else {
          this.$message({
            message: '外链url填写不正确',
            type: 'error'
          })
          callback(new Error('外链url填写不正确'))
        }
      } else {
        callback()
      }
    }
    return {
      postForm: Object.assign({}, defaultForm),
      loading: false,
      editorShow: true,
      queryShow: true,
      jsonInfo: {},
      userListOptions: [],
      rules: {
        application: [{ validator: validateRequire }],
        content: [{ validator: validateRequire }],
        source_uri: [{ validator: validateSourceUri, trigger: 'blur' }]
      },
      tempRoute: {},
      thisPageId: null,
      indexList: [],
      appList: [],
      state: ''
    }
  },
  computed: {
    contentShortLength() {
      return this.postForm.contentShort.length
    },
    jsonInfoObject() {
      var key = this.postForm.content
      if (key == null || key.length === 0) {
        return {}
      }
      try {
        JSON.parse(key)
      } catch (e) {
        return {}
      }
      // key = key.replace(/<\/?.+?>/g, '').toString()
      return JSON.parse(key)
    },
    displayTime: {
      // set and get is useful when the data
      // returned by the back end api is different from the front end
      // back end return => "2013-06-25 06:59:25"
      // front end need timestamp => 1372114765000
      get() {
        return (+new Date(this.postForm.executeTime))
      },
      set(val) {
        this.postForm.executeTime = new Date(val)
      }
    }
  },
  mounted() {
    this.indexList = this.loadAllIndex()
    this.appList = this.loadAllApp()
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.thisPageId = parseInt(id)
      this.fetchData(id)
    }

    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
    onJsonChange(value) {
      console.log('value:', value)
    },
    onJsonSave(value) {
      console.log('value:', value)
    },
    onError(value) {
      console.log('value:', value)
    },
    fetchData(id) {
      fetchArticle(id).then(response => {
        this.postForm = response.data
        var query = this.postForm.content
        this.postForm.content = query.replace(/<[^>].*?>/g, '\r\n')
        // just for test
        this.setTagsViewTitle()
        // set page title
        this.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = 'Edit JobInfo'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.postForm.id}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = 'Edit JobInfo'
      document.title = `${title} - ${this.postForm.id}`
    },
    handleRegin(val, type) {
      if (val === 'python') {
        this.editorShow = false
        return
      }
      this.editorShow = true
      console.log('handleRegin' + val)
      queryTemplate(val).then(response => {
        console.log(response)
        if (response.data != null) {
          this.$refs.editor.setContent(response.data.query)
          var query = this.postForm.content
          this.postForm.content = query.replace(/<[^>].*?>/g, '\r\n')
        }
      })
    },
    submitForm() {
      console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.postForm.id = this.thisPageId
          this.postForm.content = this.postForm.content.replace(/<[^>].*?>/g, '\r\n')
          createArticle(this.postForm).then(() => {
            this.$notify({
              title: '成功',
              message: '任务添加成功',
              type: 'success',
              duration: 2000
            })
          })
          this.postForm.status = 'published'
          this.loading = false
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    draftForm() {
      this.postForm.status = 'draft'
      this.postForm.indexName = ''
      this.postForm.application = '' // 应用名称
      this.$refs.editor.setContent('')
      this.postForm.contentShort = '' // 文章摘要
      this.postForm.source_uri = '' // 文章外链
      this.postForm.image_uri = '' // 文章图片
      this.postForm.type = []
      this.postForm.cycle = true
      this.postForm.region = ''
      this.postForm.executeTime = undefined // 前台展示时间
      this.postForm.id = undefined
      this.$message({
        message: 'cancel!',
        type: 'warning'
      })
    },
    querySearch(queryString, cb) {
      var restaurants = this.indexList
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    querySearchApp(queryString, cb) {
      var restaurants = this.appList
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    loadAllIndex() {
      return [ // 索引
        { 'value': 'app-mobi_*' },
        { 'value': 'rpc_request_timeout_*' },
        { 'value': 'sql*' }
      ]
    },
    loadAllApp() {
      return [
        //  app
        { 'value': 'webapp-item' },
        { 'value': 'worker-message' }
      ]
    },
    handleSelect(item) {
      console.log(item)
    },
    handleIconClick(ev) {
      console.log(ev)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";

.createPost-container {
  position: relative;

  .createPost-main-container {
    padding: 40px 45px 20px 50px;

    .postInfo-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;

      .postInfo-container-item {
        float: left;
      }
    }
  }

  .word-counter {
    width: 40px;
    position: absolute;
    right: 10px;
    top: 0px;
  }
}

.text-input {
  background: #ffffff;
  border: none;
  outline: none;
  border-radius: 4px;
  padding: 12px 15px 7px;
  overflow: hidden;
  resize: none;
  color:#333;
  line-height: 1.2;
  word-break: break-all;
  box-sizing: border-box;
}

.article-textarea ::v-deep {
  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }
}
.my-autocomplete {
  li {
    line-height: normal;
    padding: 7px;

    .name {
      text-overflow: ellipsis;
      overflow: hidden;
    }
    .addr {
      font-size: 12px;
      color: #b4b4b4;
    }

    .highlighted .addr {
      color: #ddd;
    }
  }
}
</style>
