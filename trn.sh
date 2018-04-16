#!/usr/bin/env sh
GLOG_logtostderr=0 GLOG_log_dir=/home/asus/study/rjgc/log/ \
/home/asus/caffe/build/tools/caffe train \
    --solver=/home/asus/study/rjgc/make_model/solver.prototxt
