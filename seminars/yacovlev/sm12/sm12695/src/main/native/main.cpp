#include <cstdint>

#include <jni.h>

namespace {

    bool isPrime(int64_t test) {
        for (int64_t i=2; i<test; ++i) {
            if (test % i == 0) return false;
        }
        return true;
    }

}

extern "C" {

void func(int x) {}

JNIEXPORT  // export from lib (declspec(dllexport) - for Windows)
jboolean
JNICALL  // for Windows - stdcall, cdecl, fastcall etc.
Java_ru_mipt_java2017_sm12_Generator_isPrimeNative
        (JNIEnv * env,
         jobject object,
         jlong  test
        )
{
    return isPrime(test);
}

}
