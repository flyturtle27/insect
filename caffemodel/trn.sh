#!/usr/bin/env sh
GLOG_logtostderr=0 GLOG_log_dir=/home/asus/study/insect/log/ \
/home/asus/caffe/build/tools/caffe train \
    --solver=/home/asus/study/insect/make_model/solver.prototxt
