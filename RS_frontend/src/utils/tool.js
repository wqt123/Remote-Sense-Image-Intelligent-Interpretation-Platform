export function setTime(params) {
    var time = new Date(params)
    return time.getFullYear() + '-' + (time.getMonth() + 1)
        + '-' + time.getDate() + ' ' + time.getHours()
        + ':' + time.getMinutes() + ':' + time.getSeconds()
}