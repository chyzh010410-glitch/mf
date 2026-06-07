import request from '@/utils/request'
export function createOrder(data) { return request({ url: '/client/orders', method: 'post', data }) }
export function getOrderList(params) { return request({ url: '/client/orders', method: 'get', params }) }
export function getOrderDetail(id) { return request({ url: '/client/orders/'+id, method: 'get' }) }
export function cancelOrder(id, data) { return request({ url: '/client/orders/'+id+'/cancel', method: 'post', data }) }
export function confirmOrder(id) { return request({ url: '/client/orders/'+id+'/confirm', method: 'post' }) }
