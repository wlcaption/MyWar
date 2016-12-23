/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\work\\workspace\\MyWarVIVO\\src\\com\\vivo\\plugin\\aidl\\IPayAndRechargeCallBack.aidl
 */
package com.vivo.plugin.aidl;
public interface IPayAndRechargeCallBack extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.vivo.plugin.aidl.IPayAndRechargeCallBack
{
private static final java.lang.String DESCRIPTOR = "com.vivo.plugin.aidl.IPayAndRechargeCallBack";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.vivo.plugin.aidl.IPayAndRechargeCallBack interface,
 * generating a proxy if needed.
 */
public static com.vivo.plugin.aidl.IPayAndRechargeCallBack asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.vivo.plugin.aidl.IPayAndRechargeCallBack))) {
return ((com.vivo.plugin.aidl.IPayAndRechargeCallBack)iin);
}
return new com.vivo.plugin.aidl.IPayAndRechargeCallBack.Stub.Proxy(obj);
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
case TRANSACTION_payResult:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
this.payResult(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_rechargeResult:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
this.rechargeResult(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.vivo.plugin.aidl.IPayAndRechargeCallBack
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
@Override public void payResult(java.lang.String transNo, boolean pay_result, java.lang.String result_code, java.lang.String pay_msg, java.lang.String gamePackageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(transNo);
_data.writeInt(((pay_result)?(1):(0)));
_data.writeString(result_code);
_data.writeString(pay_msg);
_data.writeString(gamePackageName);
mRemote.transact(Stub.TRANSACTION_payResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rechargeResult(java.lang.String uid, boolean pay_result, java.lang.String result_code, java.lang.String pay_msg, java.lang.String gamePackageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(uid);
_data.writeInt(((pay_result)?(1):(0)));
_data.writeString(result_code);
_data.writeString(pay_msg);
_data.writeString(gamePackageName);
mRemote.transact(Stub.TRANSACTION_rechargeResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_payResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_rechargeResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
	*callback of AIDLClient
	*handle by server
	*/
public void payResult(java.lang.String transNo, boolean pay_result, java.lang.String result_code, java.lang.String pay_msg, java.lang.String gamePackageName) throws android.os.RemoteException;
public void rechargeResult(java.lang.String uid, boolean pay_result, java.lang.String result_code, java.lang.String pay_msg, java.lang.String gamePackageName) throws android.os.RemoteException;
}
