#include "mcisid.h"
#include "jni.h"

JNIEXPORT jbyte JNICALL Java_org_morkato_mcisid_McisidNative_getIdentifier
  (JNIEnv* env, jclass  clazz, jbyte idx) {
  return (jbyte)mcisidGetIdentifier((uint8_t)idx);
}

JNIEXPORT jbyte JNICALL Java_org_morkato_mcisid_McisidNative_getVersionStrategy
  (JNIEnv* env, jclass clazz, jstring input) {
  const char* text = (*env)->GetStringUTFChars(env, input, NULL);
  if (text == NULL)
    return;
  const uint8_t strategy = (const uint8_t)mcisidGetVersionStrategy(text);
  (*env)->ReleaseStringUTFChars(env, input, text);
  return (jbyte)strategy;
}

JNIEXPORT jbyte JNICALL Java_org_morkato_mcisid_McisidNative_getLookup
  (JNIEnv* env, jclass clazz, jchar utf16char) {
  const char character = (const char)(utf16char & 0xFF);
  return (jbyte)mcisidGetLookup(character);
}