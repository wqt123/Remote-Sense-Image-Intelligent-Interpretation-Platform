import OSS from "ali-oss"

export default function Client(data = 'custom-data') {
    // console.log('bucket',data)
    return new OSS({
        // yourRegion填写Bucket所在地域。以华东1（杭州）为例，Region填写为oss-cn-hangzhou。
        region: "oss-cn-hangzhou",
        // secure: true,
        // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
        accessKeyId: "LTAI5tFayF7LGkUjbqhPNDu8",
        accessKeySecret: "rAHGulFmT45UXtiWFJ3QP1xiA33i4m",
        // 从STS服务获取的安全令牌（SecurityToken）。
        // stsToken: "yourSecurityToken",
        // 填写Bucket名称，例如examplebucket。
        bucket: "remote-s",
    })
}
