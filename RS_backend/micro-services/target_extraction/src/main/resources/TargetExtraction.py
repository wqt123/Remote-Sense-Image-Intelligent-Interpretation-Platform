import sys
# sys.path.append("./../../../../../PaddleRS")

import paddlers
from paddlers.deploy import Predictor
from paddlers import transforms as T
from paddlers.tasks.utils.visualize import visualize_detection

#import paddle
from paddle import no_grad
from paddle import to_tensor
from paddle import argmax

from subprocess import run
#import cv2
from cv2 import imread
from cv2 import imwrite
import numpy as np

import os


def read_rgb(path):
    im = imread(path)
    im = im[..., ::-1]
    return im

def TargetExtraction(a,r,dir):
    # 输入影像尺寸
    INPUT_SIZE = 1488

    # 获取当前文件路径
    current_path = os.path.abspath(__file__)
    # 获取当前文件的父目录
    absolute = os.path.abspath(os.path.dirname(current_path) + os.path.sep + ".")
    A_PATH = absolute + '/input/'+a

    # 读取输入影像
    im = imread(A_PATH)

    # 定义训练和验证时使用的数据变换（数据增强、预处理等）
    eval_transforms = T.Compose([
        T.Resize(target_size=1488),
        # 验证阶段与训练阶段的数据归一化方式必须相同
        T.Normalize(
            mean=[0.5, 0.5, 0.5], std=[0.5, 0.5, 0.5]),
    ])

    # 第一次运行需去掉注释运行，用于生成模型
    # run(
    #     f"python ./../../../../../../PaddleRS/deploy/export/export_model.py \
    #         --model_dir=./dynamic_model \
    #         --save_dir=./static_models/{INPUT_SIZE}x{INPUT_SIZE} \
    #         --fixed_input_shape=[{INPUT_SIZE},{INPUT_SIZE}]",
    #     shell=True,
    #     check=True
    # )

    modelsPath = absolute + '/static_models'
    predictor = Predictor(f"{modelsPath}/{INPUT_SIZE}x{INPUT_SIZE}", use_gpu=True)

    # 绘制目标框
    with no_grad():
        input = predictor.predict(im)['score_map']
        input = to_tensor(input)
        output = argmax(input, 2)
        res = output.numpy().astype(np.uint8)

    # 存储推理结果
    imwrite(dir + '/'+r, res * 255)
