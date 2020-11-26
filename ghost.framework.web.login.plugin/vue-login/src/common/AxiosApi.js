import { get, post } from './AxiosCommon'
/**
 * 判断是否为登录状态
 */
export const isLogin = () => {
    console.log('isLogin');
    return get('api/login/0eda3ff1-6c6c-48a0-adb8-de34a7debf75');
}
/**
 * 退出登录
 */
export const outLogin = () => {
    console.log('outLogin');
    return get('api/login/fca58346-8e17-498d-9902-e90748ca890a');
}
/**
 * 用户登录操作
 * @param {*} data 
 */
export const userLogin = (data) => {
    console.log('userLogin');
    return post('api/login/d73a3f84-048b-49fc-85aa-e2c6c1be4902', data);
}
/**
 * 获取后端语言地图
 * 格式为Map<String, String>
 */
export const getLanguageMap = () => {
    console.log('getLanguageMap');
    return get('api/login/e09b2ff4-9713-450e-a185-1b3f2ec29daa');
}
/**
 * 获取初始化模块地址
 */
export const getInitModuleUrl = () => {
    console.log('getInitModuleUrl');
    return get('api/login/ade5697f-ef65-4981-b310-e305d2bd8ab7');
}
/**
 * 获取json语言包数据
 * @param {*} language 
 */
export const getLanguageMessage = (language) => {
    console.log('getLanguageMessage');
    return get('api/login/5f3586ac-bbd8-4fe7-af1e-33ac37387ed9', language);
}