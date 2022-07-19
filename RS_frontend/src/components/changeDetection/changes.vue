<template>
  <div>
    <div v-if="hasImage" class="Rate"><b>变化率:&emsp;</b>{{ Rate }}%</div>
    <div v-if="hasImage" class="initial">
      <h1>源图片</h1>
      <el-image style="width: 100%; height: 100%" :src="initialURL[0]">
      </el-image>
      <el-image style="width: 100%; height: 100%" :src="initialURL[1]">
      </el-image>
    </div>
    <div v-if="hasImage" class="result">
      <h1>变化检测结果</h1>
      <el-image style="width: 100%; height: 100%" :src="url"> </el-image>
    </div>
  </div>
</template>

<script>
export default {
  name: "Changes",
  data() {
    return {
      url: "",
      initialURL: [],
      hasImage: false,
      Rate: 0.0,
    };
  },
  props: {
    image: {
      type: String,
      default: "",
    },
    initialImages: { default: [] },
    rate: { default: 0.0 },
  },
  mounted() {
    // console.log(this.image);
    this.url = this.image;
    this.initialURL = this.initialImages;
    this.Rate = this.rate*100;
  },
  watch: {
    image(val) {
      // console.log(this.image);
      this.url = this.image;
      if (this.url != "" && this.url != null) this.hasImage = true;
      else this.hasImage = false;
    },
    initialImages(val) {
      console.log(this.initialImages);
      this.initialURL = this.initialImages;
      // console.log(this.initialUrl);
    },
    rate(val) {
      this.Rate = this.rate*100;
    },
  },
};
</script>

<style>
.initial {
  text-align: center;
  display: inline-block;
  width: 25%;
  margin-left: 50px;
}
.middle {
  display: inline-block;
  margin-left: 20px;
}
.result {
  text-align: center;
  margin-left: 20px;
  width: 50%;
  float: right;
  margin-right: 50px;
}
.Rate {
  text-align: center;
  font-size: 30px;
}
</style>