<template>
  <div class="main">
    <el-card>
      <div class="uploadButton">
        <el-input
          placeholder="title"
          v-model="title"
          style="margin-bottom: 10px"
        ></el-input>
        <el-button size="medium" type="primary" @click="uploadImageList"
          >点击上传</el-button
        >
        <el-button size="medium" type="success" @click="changeBatch"
          >单图片处理</el-button
        >
      </div>
      <el-upload
        accept=".zip"
        drag
        multiple
        ref="upload"
        action="/api/changeDetection/work"
        :auto-upload="false"
        :file-list="filelist"
        :limit="1"
        :on-exceed="handleExceed"
        :on-remove="handleRemove"
        :on-change="handleChange"
        :http-request="httpRequest"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">
          点击上传文件进行变化检测<br />需要上传1个压缩包
        </div>
      </el-upload>
      <el-divider></el-divider>
      <div v-show="isUploading">
        <Spinner size="huge" message="Loading..."></Spinner>
      </div>
    </el-card>
  </div>
</template>

<script>
import Spinner from "vue-simple-spinner"; //加载动画
import { targetDetectionBatchAPI, create } from "@/api/api";
import Client from "@/api/OSS";
import Base64 from "@/utils/encryption/Base64";

export default {
  name: "targetDetection",
  data() {
    return {
      filelist: [],
      isUploading: false,
      formdata: new FormData(),
      title: "",
    };
  },
  components: {
    Spinner,
  },
  methods: {
    beforeUpload(file) {
      // console.log(this.formdata.getAll("picture").length);
      if (this.formdata.getAll("picture").length < 1) {
        this.$message({
          type: "error",
          duration: 2 * 1000,
          message: "需要上传1张图片",
        });
        return false;
      } else {
        this.isUploading = true;
      }
    },
    handleExceed(files, fileList) {
      this.$message({
        type: "error",
        duration: 2 * 1000,
        message: "最多上传1张图片",
      });
      this.$refs.upload.clearFiles();
    },
    handleSuccess(response, file, fileList) {
      this.$message({
        type: "success",
        duration: 2 * 1000,
        message: "上传成功",
      });
      // this.url = "data:image/png;base64," + this.imgBase;
    },
    handleChange(file, filelist) {
      this.filelist = filelist;
      // console.log(filelist);
    },
    handleRemove(file, filelist) {
      this.filelist = filelist;
    },
    uploadImageList() {
      // console.log(this.filelist.length);
      if (this.filelist.length < 1) {
        this.isUploading = false;
        this.$message({
          type: "error",
          duration: 2 * 1000,
          message: "需要上传1张图片",
        });
      } else {
        this.$refs.upload.submit();
      }
    },
    async httpRequest(file) {
      if (this.title == "" || this.title == null) {
        this.$message({
          message: "请输入标题",
          type: "error",
        });
        return;
      }
      let client = Client();
      let fname = `${new Date().getTime()}.zip`; //自定义文件名
      // let fname = file.file.name;
      let options = {
        // 获取分片上传进度、断点和返回值。
        progress: (p, cpt, res) => {
          console.log(p);
        },
        // 设置并发上传的分片数量。
        parallel: Math.ceil(
          Math.floor((file.file.size / 1024 / 1024) * 100) / 100
        ),
        // 设置分片大小。默认值为1 MB，最小值为100 KB。
        partSize: 1024 * 1024,
        // headers,
      };
      try {
        // object-name可以自定义为文件名（例如file.txt）或目录（例如abc/test/file.txt）的形式，
        // 实现将文件上传至当前Bucket或Bucket下的指定目录。
        const result = await client.multipartUpload(fname, file.file, {
          ...options,
          // progress,
        });
        let data = {
          originName1: fname,
          originName2: "",
          size: Math.floor((file.file.size / 1024 / 1024) * 100) / 100 + "M",
          title: this.title,
          type: "目标检测",
          userId: Base64.decode(sessionStorage.getItem("userID")),
        };
        create(data)
          .then((response) => {
            console.log(response);
            targetDetectionBatchAPI(response.data)
              .then((res) => {
                console.log(res);
                this.filelist = [];
                this.$message({
                  type: "success",
                  message: res.message,
                });
              })
              .catch((err) => {
                console.log(err);
              });
          })
          .catch((error) => {
            console.log(error);
            this.$message({
              type: "error",
              message: "上传失败",
            });
          });
        // console.log(data);
      } catch (e) {
        this.$message({
          type: "error",
          message: "上传失败",
        });
        console.log(e);
      }
    },
    changeBatch() {
      this.$router.push("targetDetection");
    },
  },
};
</script>

<style scoped>
.main {
  padding: 2%;
  width: 95%;
}
.uploadButton {
  float: right;
  margin: 10% 10% 0 20px;
}
.images {
  width: 45%;
  text-align: center;
  display: inline-block;
  margin-left: 10px;
  margin-right: 10px;
}
</style>