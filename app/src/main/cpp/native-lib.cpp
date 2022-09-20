#include <jni.h>
#include <string>
#include "utils.h"
#include <opencv2/opencv.hpp>
#include "common.h"

#define LOG_TAG "native-lib"
#define DEFAULT_CARD_WIDTH 640
#define DEFAULT_CARD_HEIGHT 400
#define FIX_IDCARD_SIZE Size(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT)

using namespace std;
using namespace cv;

//extern声明在bspatch.c
extern "C"{
    extern int main(int argc, char * argv[]);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_bard_gplearning_MainActivity_bsPatch(JNIEnv *env, jobject instance, jstring oldApk_,
                                              jstring patch_, jstring output_) {
    //将java的字符串转为C/C++的字符串，即 转换为UTF-8格式的char指针
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    // bspatch oldfile newfile patchfile
    char *argv[] = {const_cast<char *>(""), const_cast<char *>(oldApk), const_cast<char *>(output),
                    const_cast<char *>(patch)};
    main(4, argv);


    //释放char指针（相当于java的gc，recycle）
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_bard_gplearning_ORCActivity_findIdNumber(JNIEnv *env, jobject instance, jobject bitmap,
                                                  jobject config) {

    //1.将bitmap转为Mat
    Mat src_img;
    Mat dst_img;
    bitmap2Mat(env, bitmap, &src_img);

    //2.归一化
    Mat dst;
    resize(src_img, dst, FIX_IDCARD_SIZE);

    //3.灰度化
    cvtColor(src_img, dst, COLOR_RGB2GRAY);

    //4.二值化
    threshold(dst, dst, 100, 255, THRESH_BINARY);

    //5.图像膨胀
    Mat erodeElement = getStructuringElement(MORPH_RECT, Size(50, 10));
    erode(dst, dst, erodeElement);

    //6.轮廓检测
    vector<vector<Point>> contours;
    vector<Rect> rects;
    findContours(dst, contours, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0, 0));

    for(int i=0; i<contours.size(); i++){
        //7.获取矩形区域
        Rect rect = boundingRect(contours.at(i));
        //8.绘制区域
//        rectangle(dst, rect, Scalar(0, 0, 255));
        //9.遍历所有区域 查找符合号码的区域位置
        //身份证号码是有固定宽高比： >1:8 && <1:16
        if(rect.width > rect.height * 8 && rect.width < rect.height * 16){
//            rectangle(dst, rect, Scalar(0, 0, 255));
            rects.push_back(rect);
        }
    }

    //10.查找坐标最低的矩形区域（冒泡算法）
    int lowPoint = 0;
    Rect finalRect;
    for(int i = 0; i < rects.size(); i++){
        Rect rect = rects.at(i);
        Point point = rect.tl();
        if(point.y > lowPoint){
            lowPoint = point.y;
            finalRect = rect;
        }
    }



    //11.图像分割
    dst_img = src_img(finalRect);

    //资源释放



    //2.将Mat转化为bitmap
    return createBitmap(env, dst_img, config);
}