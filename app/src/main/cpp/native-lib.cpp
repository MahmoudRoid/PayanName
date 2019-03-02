#include <jni.h>
#include <string>
#include <pthread.h>


static bool isOk = false;
pthread_t threads[20];
static double resultArray[20];
static int threadCounts ;

/////////////////////////////////

void *count_all(void *id){

    int tid = (int)id;
    resultArray[tid] = 0;

    while(true){
        if(isOk){
            resultArray[tid]++;
        } else break;
    }
    return &resultArray;
}

extern "C" JNIEXPORT jstring JNICALL
Java_ir_mahmoud_payanname_Classes_Counter_intFromJNI(
        JNIEnv* env,
        jobject /* this */) {

    std::string result;
    for (int I = 0; I < threadCounts ; I++) {
        result += std::to_string(resultArray[I]) + "\n";
    }

    return env->NewStringUTF(result.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_ir_mahmoud_payanname_Classes_Counter_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string result = std::to_string(resultArray[0]) ;
    return env->NewStringUTF(result.c_str());
}




extern "C" JNIEXPORT void JNICALL
Java_ir_mahmoud_payanname_Classes_Counter_createThread(
        JNIEnv* env,
        jobject /* this */ , jint tedade_threadha) {
    // define threads Count
    threadCounts = tedade_threadha;
    // set zero all of elements
    for (int I = 0; I < threadCounts; I++) {
        resultArray[I] = I;
    }
    // set while true
    isOk = true;
    // call threads
    for (int I = 0; I < threadCounts; I++) {
        pthread_create(&threads[I], NULL, count_all, (void *)I);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_ir_mahmoud_payanname_Classes_Counter_stopThread(
        JNIEnv* env,
        jobject /* this */) {

    isOk = false;
}
