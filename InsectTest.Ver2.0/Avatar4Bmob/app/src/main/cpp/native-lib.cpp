#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <opencv2/dnn.hpp>
#include <iostream>
using namespace cv;
using namespace cv::dnn;
using namespace std;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ego_avatar4bmob_insect_GoogleNet(JNIEnv *env, jclass obj,
                                                             jintArray buf, jint w, jint h,
                                                             jstring model_bin_file_,
                                                             jstring model_txt_file_,
                                                             jstring labels_txt_file_) {
    const char *model_bin_file = env->GetStringUTFChars(model_bin_file_, 0);
    const char *model_txt_file = env->GetStringUTFChars(model_txt_file_, 0);
    const char *labels_txt_file = env->GetStringUTFChars(labels_txt_file_, 0);
    //读取int数组并转为Mat类型
    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf,JNI_FALSE);
    if (NULL == cbuf)
    {
        return 0;
    }
    Mat imgData(h,w,CV_8UC4,(unsigned char*) cbuf);

    // TODO
    String res;
    Mat src = imgData;
    if (src.empty()) {
        return env->NewStringUTF("could not load image...");
    }
    vector<String> labels;
    ifstream fp(labels_txt_file);
    if (!fp.is_open()) {
        fp.close();
        return env->NewStringUTF("could not open the file");
    }
    string name;
    while (!fp.eof()) {
        getline(fp, name);
        if (name.length()) {
            labels.push_back(name.substr(name.find(' ') + 1));
        }
    }
    fp.close();

    Net net = readNetFromCaffe(model_txt_file, model_bin_file);
    if (net.empty()) {
        return env->NewStringUTF("read caffe model data failure...");
    }
    Mat inputBlob = blobFromImage(src, 1.0, Size(227, 227), Scalar(122.991, 145.282, 146.901), false);
    Mat prob;
    for (int i = 0; i < 10; i++) {
        net.setInput(inputBlob, "data");
        prob = net.forward("prob");
    }
    Mat probMat = prob.reshape(1, 1);
    Point classNumber;
    double classProb;
    minMaxLoc(probMat, NULL, &classProb, NULL, &classNumber);
    int classidx = classNumber.x;
    char r[100];
    sprintf(r,"可能的结果为:%s,可能性:%.2f", labels.at(classidx).c_str(), classProb);
    res = r;

    env->ReleaseIntArrayElements(buf, cbuf, 0);
    env->ReleaseStringUTFChars(model_bin_file_, model_bin_file);
    env->ReleaseStringUTFChars(model_txt_file_, model_txt_file);
    env->ReleaseStringUTFChars(labels_txt_file_, labels_txt_file);

    return env->NewStringUTF(res.c_str());
}
