import request from '@/utils/request'

// 查询执行全部记录
export function fetchList(query) {
  return request({
    url: '/querylog/list',
    method: 'get',
    params: query
  })
}

// 生成报表
export function getExcelAll() {
  return request({
    url: '/excel/all',
    method: 'post'
  })
}
// 生成报表
export function generReport(query) {
  return request({
    url: '/querylog/report',
    method: 'get',
    params: query
  })
}

// 根据类型查询记录
export function queryLogListByType(query) {
  return request({
    url: '/querylog/query',
    method: 'get',
    params: query
  })
}

// 查询job任务
export function queryJobList() {
  return request({
    url: '/job/all',
    method: 'post'
  })
}

// 手动执行job任务
export function runJob(query) {
  return request({
    url: '/job/run',
    method: 'get',
    params: query
  })
}

export function fetchArticle(id) {
  return request({
    url: '/job/get',
    method: 'get',
    params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: '/article/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: '/job/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/article/update',
    method: 'post',
    data
  })
}

export function queryTemplate(type) {
  return request({
    url: '/template/type',
    method: 'get',
    params: { type }
  })
}
