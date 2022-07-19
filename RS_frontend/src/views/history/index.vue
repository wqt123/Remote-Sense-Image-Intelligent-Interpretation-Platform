<template>
  <div class="main">
    <el-card>
      <h1 class="title">用户历史记录</h1>
      <el-divider></el-divider>
      <el-table class="history" :data="history" stripe border>
        <el-table-column
          prop="id"
          label="记录ID"
          sortable
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="title"
          label="标题"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="createTime"
          label="时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="type"
          label="种类"
          sortable
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="status"
          label="运行状态"
          sortable
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="240" align="center"
          ><template slot-scope="scope"
            ><el-button
              v-if="scope.row.status == 'uploadSuccess'"
              type="success"
              @click="run(scope.row)"
              >运行</el-button
            ><el-button
              type="primary"
              @click="download(scope.row)"
              v-if="scope.row.status == 'runEnd'"
              >下载</el-button
            ><el-button
              type="danger"
              v-if="!(scope.row.status == 'isRunning')"
              @click="remove(scope.row)"
              >删除</el-button
            >
            <el-button
              type="warning"
              v-if="scope.row.status == 'isRunning'"
              plain
              disabled
              >运行中</el-button
            >
          </template></el-table-column
        >
      </el-table>
    </el-card>
  </div>
</template>

<script>
import Client from "@/api/OSS";
import Base64 from "@/utils/encryption/Base64";
import {
  getHistoryList,
  removeOneHistory,
  changeDetectionBatchAPI,
  targetDetectionBatchAPI,
  targetExtractionBatchAPI,
  terrianClassficationBatchAPI,
} from "@/api/api";

export default {
  name: "history",
  data() {
    return {
      history: [],
      intervalId: "",
    };
  },
  methods: {
    getHistory() {
      getHistoryList(Base64.decode(sessionStorage.getItem("userID")))
        .then((response) => {
          // console.log(response);
          this.history = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    download(row) {
      console.log(row);
      if (row.status != "runEnd") {
        this.$message({
          type: "error",
          message: "未运行结束",
        });
        return;
      }
      let client = Client();
      let response = {
        "content-disposition": `attachment; filename=${encodeURIComponent(
          row.resultName
        )}`,
      };
      // 填写Object完整路径。Object完整路径中不能包含Bucket名称。
      let url = client.signatureUrl(row.resultName, { response });
      // console.log(url);
      fetch(url)
        .then((res) => res.blob())
        .then((blob) => {
          // 将链接地址字符内容转变成blob地址
          const a = document.createElement("a");
          a.href = URL.createObjectURL(blob);
          //测试链接console.log(a.href)
          a.download = "fileName.xlsx"; // 下载文件的名字
          document.body.appendChild(a);
          a.click();
        });
    },
    remove(row) {
      if (row.status == "isRunning") {
        this.$message({
          type: "error",
          message: "正在运行中，无法删除",
        });
        return;
      }
      // console.log(row);
      removeOneHistory(row.id)
        .then((response) => {
          this.getHistory();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    run(row) {
      // console.log(row);
      switch (row.type) {
        case "变化检测":
          // changeDetectionBatchAPI()
          changeDetectionBatchAPI(row.id)
            .then((res) => {
              console.log(res);
              this.filelist = [];
              this.$message({
                type: "success",
                message: res.message,
              });
              this.getHistory();
            })
            .catch((err) => {
              console.log(err);
            });
          break;
        case "目标检测":
          // targetDetectionBatchAPI()
          targetDetectionBatchAPI(row.id)
            .then((res) => {
              console.log(res);
              this.filelist = [];
              this.$message({
                type: "success",
                message: res.message,
              });
              this.getHistory();
            })
            .catch((err) => {
              console.log(err);
            });
          break;
        case "目标提取":
          // targetExtractionBatchAPI()
          targetExtractionBatchAPI(row.id)
            .then((res) => {
              console.log(res);
              this.filelist = [];
              this.$message({
                type: "success",
                message: res.message,
              });
              this.getHistory();
            })
            .catch((err) => {
              console.log(err);
            });
          break;
        case "地物分类":
          // terrianClassficationBatchAPI()
          terrianClassficationBatchAPI(row.id)
            .then((res) => {
              console.log(res);
              this.filelist = [];
              this.$message({
                type: "success",
                message: res.message,
              });
              this.getHistory();
            })
            .catch((err) => {
              console.log(err);
            });
          break;
        default:
          this.$message({
            type: "error",
            message: "运行失败",
          });
          break;
      }
    },
  },
  mounted() {
    this.getHistory();
    clearInterval(this.intervalId);
    //10s自动更新表格数据
    this.intervalId = setInterval(() => {
      this.getHistory();
    }, 10000);
  },
  destroyed() {
    clearInterval(this.intervalId);
  },
};
</script>

<style scoped>
.main {
  padding: 2%;
  width: 95%;
}
.title {
  text-align: center;
}
.history {
  position: relative;
  margin: 0 auto;
}
</style>