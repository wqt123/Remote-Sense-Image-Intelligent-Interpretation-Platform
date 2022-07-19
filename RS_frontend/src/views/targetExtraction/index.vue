<template>
  <div class="main">
    <el-card>
      <div class="uploadButton">
        <el-button size="medium" type="primary" @click="uploadImageList"
          >点击上传</el-button
        >
        <el-button size="medium" type="success" @click="changeBatch"
          >批处理</el-button
        >
      </div>
      <el-upload
        accept="image/*"
        list-type="picture-card"
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
        ><i class="el-icon-plus"></i>
        <div slot="tip" class="el-upload__tip">点击上传图片进行目标提取</div>
      </el-upload>
      <el-divider></el-divider>
      <div v-show="isUploading">
        <Spinner size="huge" message="Loading..."></Spinner>
      </div>
      <div v-if="success" class="images">
        <h1>提取前</h1>
        <el-image style="width: 100%; height: 100%" :src="input"> </el-image>
      </div>
      <div v-if="success" class="images">
        <h1>提取结果</h1>
        <el-image style="width: 100%; height: 100%" :src="url"> </el-image>
      </div>
    </el-card>
  </div>
</template>

<script>
import Spinner from "vue-simple-spinner"; //加载动画
import { targetExtractionAPI } from "@/api/api";
import Client from "@/api/OSS";
import Base64 from "@/utils/encryption/Base64";

export default {
  name: "targetExtraction",
  data() {
    return {
      filelist: [],
      url: "",
      input: "",
      success: false,
      isUploading: false,
      formdata: new FormData(),
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
      this.url = "";
      this.input = "";
      console.log(this.filelist.length);
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
    httpRequest(file) {
      this.formdata.append("picture", file.file);
      if (this.formdata.getAll("picture").length == 1) {
        this.isUploading = true;

        targetExtractionAPI(this.formdata)
          .then((response) => {
            this.$message({
              type: "success",
              duration: 2 * 1000,
              message: "上传成功",
            });
            // console.log(response);
            this.$refs.upload.clearFiles();
            this.isUploading = false;
            this.formdata = new FormData();
            this.success = true;
            this.input = this.filelist[0].url;
            this.url = "data:image/jpg;base64," + response.data;
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    changeBatch() {
      this.$router.push("targetExtractionbatch");
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