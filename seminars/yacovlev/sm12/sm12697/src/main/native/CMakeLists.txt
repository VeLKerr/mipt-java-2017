cmake_minimum_required(VERSION 2.8)
project(native)
find_package(JNI)

set(CMAKE_CXX_FLAGS "-std=c++11 -O3 -march=native -mtune=native")

# -I/usr/java/latest/include etc
include_directories(${JNI_INCLUDE_DIRS})

# add_executable - target for exe file
# add_library    - target for library
#  STATIC | SHARED
add_library(MyGreatLib SHARED "main.cpp")
