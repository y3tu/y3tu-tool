import request from '@/plugin/axios'

export function policy() {
  return request({
    url:'/aliyun/oss/policy',
    method:'get',
  })
}
