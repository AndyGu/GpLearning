cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(can_use_assembler TRUE)
enable_language(ASM)

#头文件配置
include_directories(include)

#编译头文件 指定的路径
file(GLOB my_source_path ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c ${CMAKE_SOURCE_DIR}/bzip/*.c ${CMAKE_SOURCE_DIR}/gif/*.c ${CMAKE_SOURCE_DIR}/giflib/*.c)
if((${ANDROID_ABI} STREQUAL "armeabi") OR (${ANDROID_ABI} STREQUAL "armeabi-v7a"))
    list(APPEND my_source_path	gif/memset.arm.S)
endif((${ANDROID_ABI} STREQUAL "armeabi") OR (${ANDROID_ABI} STREQUAL "armeabi-v7a"))
add_library(native-lib SHARED ${my_source_path})

#添加动态库
add_library(lib_opencv SHARED IMPORTED)

#设置lib_opencv动态库的真实地址
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libopencv_java4.so)

find_library(
        log-lib
        log
)

find_library(
        jnigraphics-lib
        jnigraphics
)

find_library(
        android-lib
        android
)

find_library(
        GLESv2-lib
        GLESv2
)

target_link_libraries(
        native-lib
        ${android-lib}
        ${GLESv2-lib}
        ${log-lib}
        ${jnigraphics-lib}
        lib_opencv
)



#file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)
#
##设置本地动态库，编译生成动态库
#add_library(
#        native-lib #模块名
#        SHARED
#
#        bspatch.c
#        ${bzip_source}
#        native-lib.cpp) #源文件
#
##查找系统库 log日志
#find_library(
#        log-lib
#        log)
#
#
##需要链接或者编译的库
#target_link_libraries(
#        native-lib
#        ${log-lib})