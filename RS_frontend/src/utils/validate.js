/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}


/* 是否手机号码或者固话*/
export function validatePhoneTwo(rule, value, callback) {
  const reg = /^((0\d{2,3}-\d{7,8})|(1[345789]\d{9}))$/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的电话号码或者固话号码'))
    } else {
      callback()
    }
  }
}
/* 是否固话*/
export function validateTelphone(rule, value, callback) {
  const reg = /0\d{2}-\d{7,8}/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的固话（格式：区号+号码,如010-1234567）'))
    } else {
      callback()
    }
  }
}
/* 是否手机号码*/
export function validatePhone(rule, value, callback) {
  const reg = /^[1][3,4,5,7,8,9][0-9]{9}$/
  if (value === '' || value === undefined || value == null) {
    callback(new Error('电话号码不能为空'))
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的电话号码'))
    } else {
      callback()
    }
  }
}
/* 是否身份证号码*/
export function validateIdNo(rule, value, callback) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的身份证号码'))
    } else {
      callback()
    }
  }
}
/* 是否邮箱*/
export function validateEMail(rule, value, callback) {
  const reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的邮箱地址'))
    } else {
      callback()
    }
  }
}
/* 验证内容是否英文数字以及下划线*/
export function isPassword(rule, value, callback) {
  const reg = /^[_a-zA-Z0-9]{6,16}$/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if (!reg.test(value)) {
      callback(new Error('密码仅由6到16位英文字母，数字以及下划线组成'))
    } else {
      callback()
    }
  }
}
/* 验证是否是账号*/
export function validateNumber(rule, value, callback) {
  const reg = /^U[0-9]+$/
  if (value === '' || value === undefined || value == null) {
    callback()
    return true
  } else {
    if (!reg.test(value)) {
      callback(new Error('用户ID仅由U+数字构成'))
      return false
    } else {
      callback()
      return true
    }
  }
}
/* 是否是固定电话或者手机号码或者是邮箱*/
export function validdateContact(rule, value, callback) {
  const reg = /^((0\d{2,3}-\d{7,8})|(1[345789]\d{9})|(([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+))$/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的联系方式'))
    } else {
      callback()
    }
  }
}
