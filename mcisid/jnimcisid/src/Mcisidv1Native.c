#include "jni.h"
#include "mcisidv1.h"

JNIEXPORT jint JNICALL Java_org_morkato_mcisid_Mcisidv1Native_nextValue
  (JNIEnv* env, jclass sclass, jclass jcls, jobject jobj, jlong jinstant) {
  mcisidv1seq sequence;
  jfieldID currentFieldId = (*env)->GetFieldID(env, jcls, "current", "I");
  jfieldID lastimeFieldId = (*env)->GetFieldID(env, jcls, "lastime", "J");
  if (currentFieldId == NULL || lastimeFieldId == NULL)
    return (jint)0;
  sequence.current = (int32_t)((*env)->GetIntField(env, jobj, currentFieldId));
  sequence.lastime = (time_t)((*env)->GetLongField(env, jobj, lastimeFieldId));
  mcisidstate result = mcisidv1NextValue(&sequence, (time_t)jinstant);
  (*env)->SetIntField(env, jobj, currentFieldId, (jint)sequence.current);
  (*env)->SetLongField(env, jobj, lastimeFieldId, (jlong)sequence.lastime);
  return (jint)result;
}

JNIEXPORT void JNICALL Java_org_morkato_mcisid_Mcisidv1Native_resetSequence
  (JNIEnv* env, jclass sclass, jclass jcls, jobject jobj) {
  mcisidv1seq sequence;
  jfieldID currentFieldId = (*env)->GetFieldID(env, jcls, "current", "I");
  jfieldID lastimeFieldId = (*env)->GetFieldID(env, jcls, "lastime", "J");
  if (currentFieldId == NULL || lastimeFieldId == NULL)
    return;
  sequence.current = (int32_t)(*env)->GetIntField(env, jobj, currentFieldId);
  sequence.lastime = (time_t)(*env)->GetLongField(env, jobj, lastimeFieldId);
  mcisidv1ResetSequence(&sequence);
  (*env)->SetIntField(env, jobj, currentFieldId, (jint)sequence.current);
  (*env)->SetLongField(env, jobj, lastimeFieldId, (jlong)sequence.lastime);
}

JNIEXPORT jboolean JNICALL Java_org_morkato_mcisid_Mcisidv1Native_isValidId
  (JNIEnv* env, jclass sclass, jbyteArray jarray) {
  jbyte* buffer = (*env)->GetByteArrayElements(env, jarray, NULL);
  jsize length = (*env)->GetArrayLength(env, jarray);
  if (length != MCISIDV1_IOSIZE
      || mcisidGetLookup(buffer[0]) != 0
      || mcisidv1IsInvalid((const mcisidv1*)buffer)) {
    (*env)->ReleaseByteArrayElements(env, jarray, buffer, JNI_ABORT);
    return (jboolean)0;
  }
  for (uint8_t i = 0; i < MCISIDV1_IOSIZE; ++i) {
    if (mcisidGetLookup((const char)buffer[i]) == MCISID_INVALID_CHARACTER) {
      (*env)->ReleaseByteArrayElements(env, jarray, buffer, JNI_ABORT);
      return (jboolean)0;
    }
  }
  (*env)->ReleaseByteArrayElements(env, jarray, buffer, JNI_ABORT);
  return (jboolean)1;
}

JNIEXPORT jbyte JNICALL Java_org_morkato_mcisid_Mcisidv1Native_getModelType
  (JNIEnv* env, jclass sckass, jbyteArray jarray) {
  jbyte* buffer = (*env)->GetByteArrayElements(env, jarray, NULL);
  uint8_t result = mcisidv1GetOriginModel((const mcisidv1*)buffer);
  (*env)->ReleaseByteArrayElements(env, jarray, buffer, JNI_ABORT);
  return (jbyte)result;
}

JNIEXPORT jint JNICALL Java_org_morkato_mcisid_Mcisidv1Native_getSequenceValue
  (JNIEnv* env, jclass sclass, jbyteArray jarray) {
  jbyte* buffer = (*env)->GetByteArrayElements(env, jarray, NULL);
  int32_t result = mcisidv1GetSequence((const mcisidv1*)buffer);
  (*env)->ReleaseByteArrayElements(env, jarray, buffer, JNI_ABORT);
  return (jint)result;
}

JNIEXPORT jlong JNICALL Java_org_morkato_mcisid_Mcisidv1Native_getInstantCreated
  (JNIEnv* env, jclass sclass, jbyteArray jarray) {
  jbyte* buffer = (*env)->GetByteArrayElements(env, jarray, NULL);
  time_t result = mcisidv1GetTimeInstantMilis((const mcisidv1*)buffer);
  (*env)->ReleaseByteArrayElements(env, jarray, buffer, JNI_ABORT);
  return (jlong)result;
}