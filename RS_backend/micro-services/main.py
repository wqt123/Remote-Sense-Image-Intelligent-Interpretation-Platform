from flask import Flask, jsonify, request
from flask_httpauth import HTTPBasicAuth
from flask_cors import cross_origin
from change_detection.src.main.resources.ChangeDetector import ChangeDetector
from target_detection.src.main.resources.TargetDetector import TargetDetector
from target_extraction.src.main.resources.TargetExtraction import TargetExtraction
from terrian_classification.src.main.resources.TerrianClassification import TerrianClassification

app = Flask(__name__)
auth = HTTPBasicAuth()

@app.route('/ChangeDetector', methods=['GET'])
@cross_origin(supports_credentials=True)
def change_detector():
    args = request.args  # 获取get参数
    a = args.get("a")
    b = args.get("b")
    r = args.get("r")
    dir = args.get("dir")
    rate=ChangeDetector(a,b,r,dir)
    res={"res":rate}
    return jsonify(res)

@app.route('/TargetDetector', methods=['GET'])
@cross_origin(supports_credentials=True)
def target_detector():
    args = request.args  # 获取get参数
    a = args.get("a")
    r = args.get("r")
    dir = args.get("dir")
    TargetDetector(a,r,dir)
    res={"res":"success"}
    return jsonify(res)

@app.route('/TargetExtraction', methods=['GET'])
@cross_origin(supports_credentials=True)
def target_extraction():
    args = request.args  # 获取get参数
    a = args.get("a")
    r = args.get("r")
    dir = args.get("dir")
    TargetExtraction(a,r,dir)
    res={"res":"success"}
    return jsonify(res)

@app.route('/TerrianClassification', methods=['GET'])
@cross_origin(supports_credentials=True)
def terrian_classification():
    args = request.args  # 获取get参数
    a = args.get("a")
    r = args.get("r")
    dir = args.get("dir")
    rate=TerrianClassification(a,r,dir)
    res={"res":rate}
    return jsonify(res)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0',port=8300)