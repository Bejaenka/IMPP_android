#!/bin/sh
cd IMPP_APP
rm -rf rust
git clone https://github.com/mad-de/lib_impp_android rust
cd rust
cargo build --target aarch64-linux-android --release
cargo build --target armv7-linux-androideabi --release
cargo build --target i686-linux-android --release
cargo build --target x86_64-linux-android --release
JNI_LIBS=../android/app/src/main/jniLibs
rm -rf $JNI_LIBS
mkdir $JNI_LIBS
mkdir $JNI_LIBS/arm64-v8a
mkdir $JNI_LIBS/armeabi-v7a
mkdir $JNI_LIBS/x86
mkdir $JNI_LIBS/x86_64
cp target/aarch64-linux-android/release/librust.so $JNI_LIBS/arm64-v8a/librust.so
cp target/armv7-linux-androideabi/release/librust.so $JNI_LIBS/armeabi-v7a/librust.so
cp target/i686-linux-android/release/librust.so $JNI_LIBS/x86/librust.so
cp target/x86_64-linux-android/release/librust.so $JNI_LIBS/x86_64/librust.so
