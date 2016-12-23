/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\work\\workspace\\MyWarVIVO\\src\\com\\vivo\\plugin\\aidl\\IAccountCallBack.aidl
 */
package com.vivo.plugin.aidl;
public interface IAccountCallBack extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.vivo.plugin.aidl.IAccountCallBack
{
private static final java.lang.String DESCRIPTOR = "com.vivo.plugin.aidl.IAccountCallBack";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.vivo.plugin.aidl.IAccountCallBack interface,
 * generating a proxy if needed.
 */
public static com.vivo.plugin.aidl.IAccountCallBack asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.vivo.plugin.aidl.IAccountCallBack))) {
return ((com.vivo.plugin.aidl.IAccountCallBack)iin);
}
return new com.vivo.plugin.aidl.IAccountCallBack.Stub.Proxy(obj);
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
case TRANSACTION_loginResult:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.loginResult(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_loginCancel:
{
data.enforceInterface(DESCRIPTOR);
this.loginCancel();
reply.writeNoException();
return true;
}
case TRANSACTION_updateCancel:
{
data.enforceInterface(DESCRIPTOR);
this.updateCancel();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.vivo.plugin.aidl.IAccountCallBack
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
/**
	*callback of AIDLClient
	*handle by server
	*/
@Override public void loginResult(java.lang.String name, java.lang.String openId, java.lang.String token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeString(openId);
_data.writeString(token);
mRemote.transact(Stub.TRANSACTION_loginResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void loginCancel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_loginCancel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void updateCancel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateCancel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_loginResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_loginCancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updateCancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
/**
	*callback of AIDLClient
	*handle by server
	*/
public void loginResult(java.lang.String name, java.lang.String openId, java.lang.String token) throws android.os.RemoteException;
public void loginCancel() throws android.os.RemoteException;
public void updateCancel() throws android.os.RemoteException;
}
