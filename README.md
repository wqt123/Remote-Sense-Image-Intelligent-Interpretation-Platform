# 基于百度飞桨的遥感图像智能解译平台

------

## 介绍

本项目为软件杯A4遥感赛道：基于百度飞桨的遥感图像智能解译平台的参赛项目。小组在分析赛题需求的基础上，结合当下分布式云服务的广泛发展前景，提出了：Vue+ Spring Cloud + Flask+PaddlePaddle的智能化遥感解译平台解决方案。

本项目共有：目标提取服务、变化检测服务、目标检测服务、地物分类服务、用户管理服务，五个微服务。对于赛题要求的思想基本功能，系统都支持单图片（对于变化检测是两张图片）推理，以及多图片批量式推理。对于批量式推理，系统会将结果存储在数据库中，用户可根据需求进行下载。在推理的基础上，本系统还支持对部分服务进行辅助数据分析，比如：对变化检测系统可以自动计算两张图片前后变化比率等。

------

## 如何运行

### 1. 安装PaddleRS

```
git clone https://github.com/PaddleCV-SIG/PaddleRS
pip install -r PaddleRS/requirements.txt
pip install -e PaddleRS/
```

**paddleRS文件夹应与RS_backend在同一目录下。**

### 2. 前端

下载依赖：

```
npm install
```

运行项目

```
npm run dev
```

### 3. 后端

#### 数据库配置

数据库包括：

- redis数据库用于gateway 网关
- 阿里云OSS用于批量处理时文件的上传下载
- MySQL用于存储用户和历史记录数据

mysql数据库配置位于MySQL文件夹下。

#### 静态模型下载

由于静态模型过大，目前放在百度网盘：

链接：https://pan.baidu.com/s/16IITwiVZtDX7qeYbwRIRtA 
提取码：gh51

下载后将对应微服务的静态模型static_models 放在对应微服务项目中的resources文件夹下即可。

#### 运行后端代码

后端通过maven进行项目管理，在下载好依赖后，将下图所示的5个微服务以及main.py
flask接口文件全部运行起来即可。

