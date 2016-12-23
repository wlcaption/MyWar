/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\work\\workspace\\MyWarVIVO\\src\\com\\vivo\\plugin\\aidl\\ExecuteServiceAIDL.aidl
 */
package com.vivo.plugin.aidl;
public interface ExecuteServiceAIDL extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.vivo.plugin.aidl.ExecuteServiceAIDL
{
private static final java.lang.String DESCRIPTOR = "com.vivo.plugin.aidl.ExecuteServiceAIDL";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.vivo.plugin.aidl.ExecuteServiceAIDL interface,
 * generating a proxy if needed.
 */
public static com.vivo.plugin.aidl.ExecuteServiceAIDL asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.vivo.plugin.aidl.ExecuteServiceAIDL))) {
return ((com.vivo.plugin.aidl.ExecuteServiceAIDL)iin);
}
return new com.vivo.plugin.aidl.ExecuteServiceAIDL.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerCallBack:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.vivo.plugin.aidl.IAccountCallBack _arg1;
_arg1 = com.vivo.plugin.aidl.IAccountCallBack.Stub.asInterface(data.readStrongBinder());
com.vivo.plugin.aidl.IPayAndRechargeCallBack _arg2;
_arg2 = com.vivo.plugin.aidl.IPayAndRechargeCallBack.Stub.asInterface(data.readStrongBinder());
int _arg3;
_arg3 = data.readInt();
com.vivo.plugin.aidl.ISinglePayCallBack _arg4;
_arg4 = com.vivo.plugin.aidl.ISinglePayCallBack.Stub.asInterface(data.readStrongBinder());
this.registerCallBack(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_stopAssistService:
{
data.enforceInterface(DESCRIPTOR);
this.stopAssistService();
reply.writeNoException();
return true;
}
case TRANSACTION_startAssistService:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.startAssistService(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_paymentActionInit:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.paymentActionInit(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_paymentActionInitial:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.paymentActionInitial(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_vivoAccountreportRoleInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
this.vivoAccountreportRoleInfo(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.vivo.plugin.aidl.ExecuteServiceAIDL
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void registerCallBack(java.lang.String gamePackageName, com.vivo.plugin.aidl.IAccountCallBack icallback, com.vivo.plugin.aidl.IPayAndRechargeCallBack iPayCallback, int oritation, com.vivo.plugin.aidl.ISinglePayCallBack iSinglePayCallback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(gamePackageName);
_data.writeStrongBinder((((icallback!=null))?(icallback.asBinder()):(null)));
_data.writeStrongBinder((((iPayCallback!=null))?(iPayCallback.asBinder()):(null)));
_data.writeInt(oritation);
_data.writeStrongBinder((((iSinglePayCallback!=null))?(iSinglePayCallback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopAssistService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopAssistService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startAssistService(java.lang.String gamePkgName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(gamePkgName);
mRemote.transact(Stub.TRANSACTION_startAssistService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void paymentActionInit(java.lang.String appId, java.lang.String openid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(appId);
_data.writeString(openid);
mRemote.transact(Stub.TRANSACTION_paymentActionInit, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void paymentActionInitial(java.lang.String appId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(appId);
mRemote.transact(Stub.TRANSACTION_paymentActionInitial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void vivoAccountreportRoleInfo(java.lang.String roleid, java.lang.String rolelevel, java.lang.String serviceArea, java.lang.String roleName, java.lang.String serviceAreaName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(roleid);
_data.writeString(rolelevel);
_data.writeString(serviceArea);
_data.writeString(roleName);
_data.writeString(serviceAreaName);
mRemote.transact(Stub.TRANSACTION_vivoAccountreportRoleInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopAssistService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startAssistService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_paymentActionInit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_paymentActionInitial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_vivoAccountreportRoleInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void registerCallBack(java.lang.String gamePackageName, com.vivo.plugin.aidl.IAccountCallBack icallback, com.vivo.plugin.aidl.IPayAndRechargeCallBack iPayCallback, int oritation, com.vivo.plugin.aidl.ISinglePayCallBack iSinglePayCallback) throws android.os.RemoteException;
public void stopAssistService() throws android.os.RemoteException;
public void startAssistService(java.lang.String gamePkgName) throws android.os.RemoteException;
public void paymentActionInit(java.lang.String appId, java.lang.String openid) throws android.os.RemoteException;
public void paymentActionInitial(java.lang.String appId) throws android.os.RemoteException;
public void vivoAccountreportRoleInfo(java.lang.String roleid, java.lang.String rolelevel, java.lang.String serviceArea, java.lang.String roleName, java.lang.String serviceAreaName) throws android.os.RemoteException;
}
