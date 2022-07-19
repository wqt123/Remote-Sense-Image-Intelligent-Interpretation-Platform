import request from "@/utils/request"

/**
 * 变化检测
 * @param {*} params 表单
 * @returns 后端结果，图片数据流
 */
export function changeDetectionAPI(params) {
    return request({
        url: '/api/changeDetection/work',
        method: 'post',
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 变换检测批处理
 * @param {*} his_id 记录ID
 * @returns 
 */
export function changeDetectionBatchAPI(his_id) {
    return request({
        url: "/api/changeDetection/batch_work",
        method: "get",
        params: { his_id }
    })
}

/**
 * 目标检测
 * @param {*} params 表单
 * @returns 后端结果，图片数据流
 */
export function targetDetectionAPI(params) {
    return request({
        url: '/api/targetDetection/work',
        method: 'post',
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 目标检测批处理
 * @param {*} his_id 记录ID
 * @returns 
 */
export function targetDetectionBatchAPI(his_id) {
    return request({
        url: "/api/targetDetection/batch_work",
        method: "get",
        params: { his_id }
    })
}

/**
 * 目标提取
 * @param {*} params 表单
 * @returns 后端结果，图片数据流
 */
export function targetExtractionAPI(params) {
    return request({
        url: '/api/targetExtraction/work',
        method: 'post',
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 目标提取批处理
 * @param {*} his_id 记录ID
 * @returns 
 */
export function targetExtractionBatchAPI(his_id) {
    return request({
        url: "/api/targetExtraction/batch_work",
        method: "get",
        params: { his_id }
    })
}

/**
 * 地物分类
 * @param {*} params 表单
 * @returns 后端结果，图片数据流
 */
export function terrianClassficationAPI(params) {
    return request({
        url: '/api/terrianClassfication/work',
        method: 'post',
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 地物分类批处理
 * @param {*} his_id 记录ID
 * @returns 
 */
export function terrianClassficationBatchAPI(his_id) {
    return request({
        url: "/api/terrianClassfication/batch_work",
        method: "get",
        params: { his_id }
    })
}

/**
 * 登录接口
 * @param {*} params 表单
 */
export function login(userName_or_email, password) {
    return request({
        url: '/api/userService/user/login',
        method: 'post',
        data: {
            userName_or_email,
            password
        }
    })
}

/**
 * 注册接口
 * @param {*} params 表单
 */
export function register(params) {
    return request({
        url: '/api/userService/user/register',
        method: 'post',
        data: params,
    })
}

/**
 * 发送验证码
 * @returns 
 */
export function getCode(email) {
    return request({
        url: '/api/userService/user/getCode',
        method: 'get',
        params: { email }
    })
}

/**
 * 创建历史记录
 * @param {*} params 表单数据
 * @returns 
 */
export function create(params) {
    return request({
        url: '/api/userService/history/create',
        method: 'post',
        data: params
    })
}

/**
 * 根据用户ID获取用户所有历史记录
 * @param {*} userID 用户ID
 * @returns 历史记录
 */
export function getHistoryList(user_id) {
    return request({
        url: '/api/userService/history/getAllByUserId',
        method: 'get',
        params: { user_id }
    })
}

/**
 * 获取单条历史记录
 * @param {*} his_id 历史记录ID
 * @returns 单条历史记录
 */
export function getOneHistory(his_id) {
    return request({
        url: '/api/userService/history/getSingle',
        method: 'get',
        params: { his_id }
    })
}

/**
 * 移除单条历史记录
 * @param {*} his_id 历史记录ID
 * @returns 
 */
export function removeOneHistory(his_id) {
    return request({
        url: '/api/userService/history/remove',
        method: 'get',
        params: { his_id }
    })
}