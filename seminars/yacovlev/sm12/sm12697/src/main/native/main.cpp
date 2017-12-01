#include <cstdint>
#include <iostream>

#include <jni.h>

namespace {

    bool isPrime(int64_t value) {
        for (int64_t test=2; test<value; test++) {
            if (value % test==0) return false;
        }
        return true;
    }

}

extern "C" {

// JNIEXPORT : __attribute__((visibility("default"))) [Linux]
//             declspec(dllexport)
JNIEXPORT
jboolean
JNICALL  // Windows: __stdcall
Java_ru_mipt_java2017_sm12_Generator_isPrimeNative(
        JNIEnv * env,
        jobject object,
        jlong   value
        )
{
    return isPrime(value);
}

}
