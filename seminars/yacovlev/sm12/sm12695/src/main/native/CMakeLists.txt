cmake_minimum_required(VERSION 2.8)
project(native)

find_package(JNI) # Java Native Interface
set(CMAKE_CXX_FLAGS "-std=c++11 -O3 -march=native -mtune=native")

include_directories(${JNI_INCLUDE_DIRS})

# add_executable - create executable from sources list
# add_library - create executable from sources list
# SHARED | STATIC:
#  - SHARED - .so/.dll
#  - STATIC - .a/.lib
add_library(MyGreatLibrary SHARED "main.cpp")
