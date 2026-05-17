import request from '@/utils/request'
export function getUnreadCount() { return request({ url: '/client/messages/unread-count', method: 'get' }) }
