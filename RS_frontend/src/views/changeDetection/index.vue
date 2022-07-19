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
        :limit="2"
        :on-exceed="handleExceed"
        :on-remove="handleRemove"
        :on-change="handleChange"
        :http-request="httpRequest"
        ><i class="el-icon-plus"></i>
        <div slot="tip" class="el-upload__tip">
          点击上传图片进行变化检测<br />需要上传2张图片
        </div>
      </el-upload>
      <el-divider></el-divider>
      <div v-show="isUploading">
        <Spinner size="huge" message="Loading..."></Spinner>
      </div>
      <Changes :rate="rate" :initialImages="initialURL" :image="url"></Changes>
    </el-card>
  </div>
</template>

<script>
import Changes from "@/components/changeDetection/changes";
import Spinner from "vue-simple-spinner"; //加载动画
import { changeDetectionAPI } from "@/api/api";
import Client from "@/api/OSS";
import Base64 from "@/utils/encryption/Base64";

export default {
  name: "changeDetection",
  data() {
    return {
      filelist: [],
      url: "",
      initialURL: [], //原先图片地址
      isUploading: false,
      formdata: new FormData(),
      rate: 0.0,
    };
  },
  components: {
    Changes,
    Spinner,
  },
  mounted() {},
  methods: {
    beforeUpload(file) {
      // console.log(this.formdata.getAll("pictures").length);
      if (this.formdata.getAll("pictures").length < 2) {
        this.$message({
          type: "error",
          duration: 2 * 1000,
          message: "需要上传2张图片",
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
        message: "最多上传2张图片",
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
      this.initialURL = [];
      this.url = "";
      // console.log(this.filelist.length);
      if (this.filelist.length < 2) {
        this.isUploading = false;
        this.$message({
          type: "error",
          duration: 2 * 1000,
          message: "需要上传2张图片",
        });
      } else {
        this.$refs.upload.submit();
      }
    },
    httpRequest(file) {
      this.formdata.append("pictures", file.file);
      // console.log(this.filelist);
      if (this.formdata.getAll("pictures").length == 2) {
        this.isUploading = true;

        changeDetectionAPI(this.formdata)
          .then((response) => {
            this.$message({
              type: "success",
              duration: 2 * 1000,
              message: "上传成功",
            });
            this.initialURL = [];
            // console.log(response);
            this.$refs.upload.clearFiles();
            this.isUploading = false;
            this.formdata = new FormData();
            this.filelist.forEach((item) => {
              // console.log(item);
              this.initialURL.push(item.url);
            });
            this.url = "data:image/jpg;base64," + response.data.map;
            this.rate = response.data.rate;
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    changeBatch() {
      this.$router.push("changeDetectionbatch");
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
</style>