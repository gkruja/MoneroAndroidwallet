#include <jni.h>
#include <string>
#include "Monero.h"

#include <boost/regex.hpp>

const static epee::global_regexp_critical_section gregexplock;


Monero::AndroidWallet  wallet2;

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_InitActivity_RestoreWallet_RestoreWalletFragment_GeneratefromMnemonic(
        JNIEnv *env, jobject instance, jstring path_, jstring mnemonic_, jstring language_,
        jstring walletname_, jstring password_, jboolean testnet) {
    const char *path = env->GetStringUTFChars(path_, 0);
    const char *mnemonic = env->GetStringUTFChars(mnemonic_, 0);
    const char *language = env->GetStringUTFChars(language_, 0);
    const char *walletname = env->GetStringUTFChars(walletname_, 0);
    const char *password = env->GetStringUTFChars(password_, 0);

    // TODO

    bool ret = wallet2.GeneratefromMnemonic(path,mnemonic,language,walletname,password,testnet);

    env->ReleaseStringUTFChars(path_, path);
    env->ReleaseStringUTFChars(mnemonic_, mnemonic);
    env->ReleaseStringUTFChars(language_, language);
    env->ReleaseStringUTFChars(walletname_, walletname);
    env->ReleaseStringUTFChars(password_, password);

    return ret;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_InitActivity_GenerateWallet_GenerateWalletFragment_GenerateWallet(
        JNIEnv *env, jobject instance, jstring Path_, jstring WalletName_, jstring Password_) {
    string Path = env->GetStringUTFChars(Path_, 0);
    string WalletName = env->GetStringUTFChars(WalletName_, 0);
    string Password = env->GetStringUTFChars(Password_, 0);


    if (std::ifstream(Path+"/"+WalletName)) {

        return false;
    } else
    {

        return   wallet2.GenerateWallet(Path,WalletName,Password);

    }

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MainActivity_ParseQR(JNIEnv *env, jobject instance,
                                                                   jstring Qrresult_) {
    string Qrresult = env->GetStringUTFChars(Qrresult_, 0);

    boost::regex expr  {"monero:(\\w{95})(\\?tx_amount=(\\d+))?((\\?|&)tx_payment_id=(\\w*))?"};

    boost::smatch result;
    string zero;
    string one;
    string two;
    string three;
    string four;
    string five;
    string six;
   bool test =  boost::regex_search(Qrresult,result,expr);

         one = result[1];
         three = result[3];
         six = result[6];


    return env->NewStringUTF(one.c_str());

}



extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_Services_SyncWalletService_InitWallet(
        JNIEnv *env,
        jobject /* this */,jstring Path,jstring DaemonAddress,jstring Password)
{
    bool init =false ;
    string path = env->GetStringUTFChars(Path,0);
    string daemon = env->GetStringUTFChars(DaemonAddress,0);
    string password = env->GetStringUTFChars(Password,0);

   if (std::ifstream(path+"/monero/example.keys")) {

       //159.203.250.205:38081
       //192.168.1.141:28081
        init = wallet2.init(daemon, password, path, true, 4);
   } else
   {
       wallet2.GenerateWallet(path,password);

      init =   wallet2.init(daemon, password, path, true, 4);
   }

    return init;
}

// TODO: check if still needed now or in the future

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_SettingActivity_ReInitWallet(
        JNIEnv *env,
        jobject /* this */,jstring Path,jstring DaemonAddress,jstring Password)
{
    bool init =false ;
    string path = env->GetStringUTFChars(Path,0);
    string daemon = env->GetStringUTFChars(DaemonAddress,0);
    string password = env->GetStringUTFChars(Password,0);
    if (std::ifstream(path)) {

        //159.203.250.205:38081
        //192.168.1.141:28081
        init = wallet2.reinit(daemon);
    }

    return init;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_MainActivity_ReInitWallet(
        JNIEnv *env,
        jobject /* this */,jstring DaemonAddress)
{
    bool init =false ;

    string daemon = env->GetStringUTFChars(DaemonAddress,0);

        //159.203.250.205:38081
        //192.168.1.141:28081
        init = wallet2.reinit(daemon);


    return init;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_MainActivity_InitWallet(
        JNIEnv *env,
        jobject /* this */,jstring Path,jstring Password)
{
    bool init =false;



    string path = env->GetStringUTFChars(Path,0);
    string password = env->GetStringUTFChars(Password,0);

    if (std::ifstream(path)) {
        //159.203.250.205:38081
        //192.168.1.141:28081
        init = wallet2.init("159.203.250.205:38081", password, path, true, 4);
    } else
    {
        wallet2.GenerateWallet(path,"password");

        init = wallet2.init("159.203.250.205:38081", password, path, true, 4);
    }

    return init;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MainActivity_GetDaemonAddress(
        JNIEnv *env,
        jobject /* this */)
{
    return env->NewStringUTF(wallet2.getDaemonaddress().c_str());
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_root_monerotest_Services_SyncWalletService_WalletHeight(
        JNIEnv *env,
        jobject /* this */) {


    return wallet2.WalletLocalHeight();

}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_root_monerotest_Services_SyncWalletService_DaemonHeight(
        JNIEnv *env,
        jobject /* this */) {

    return wallet2.DaemonHeight();

}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_root_monerotest_MainActivity_WalletHeight(
        JNIEnv *env,
        jobject /* this */) {

    return wallet2.WalletLocalHeight();

}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_root_monerotest_MainActivity_DaemonHeight(
        JNIEnv *env,
        jobject /* this */) {

    return wallet2.DaemonHeight();

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_root_monerotest_Services_SyncWalletService_WalletRefresh(
        JNIEnv *env,
        jobject /* this */) {

    wallet2.refresh();


}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_DashboardFragment_CheckConnection(
        JNIEnv *env,
        jobject /* this */)
{

    return wallet2.Check_Connection();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_DashboardFragment_WalletAvailable(
        JNIEnv *env,
        jobject /* this */)
{
    return !wallet2.isnull();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_DashboardFragment_Transfers(
        JNIEnv *env,
        jobject /* this */)
{
    if(wallet2.isnull())
    {
        string ret ="[]";
        return env->NewStringUTF(ret.c_str());
    }

    std::multimap<uint64_t, std::pair<bool,std::string>> transfer  = wallet2.get_transfers();

    int count =0 ;
    string temp = "[";
    jstring str;
    jobjectArray payment;
    jsize len = transfer.size();

    payment = (*env).NewObjectArray(len,env->FindClass("java/lang/String"),0);
    // print in and out sorted by height
    for (std::map<uint64_t, std::pair<bool, std::string>>::const_iterator i = transfer.begin(); i != transfer.end(); ++i) {
        //   message_writer(i->second.first ? console_color_green : console_color_magenta, false) <<
        //                                                                                      boost::format("%8.8llu %6.6s %s") %
        //                                                                                      ((unsigned long long)i->first) % (i->second.first ? tr("in") : tr("out")) % i->second.second;

        bool in_out = i->second.first;
        string address = i->second.second;
        uint64_t height =  i->first;

        temp += address +",";

        str = env->NewStringUTF(temp.c_str());

        env->SetObjectArrayElement(payment,count,str);
        count++;
    }

    int i = temp.find_last_of(",");
    temp = temp.substr(0,i)+"]";
    return env->NewStringUTF(temp.c_str());
}
extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_root_monerotest_DashboardFragment_Balance(
        JNIEnv *env,
        jobject /* this */)
{
    return  wallet2.Balance();
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_root_monerotest_DashboardFragment_UnlockedBalance(
        JNIEnv *env,
        jobject /* this */)
{
    return wallet2.UnlockedBalance();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MenuFragments_SendFragment_SendTransfer(
        JNIEnv *env, jobject /* this */,jstring Address,jdouble Amount) {

    string address = env->GetStringUTFChars(Address,0);

    wallet2.transfer(address,Amount *1000000000000,"",0);

    return env->NewStringUTF(wallet2.pending_tx.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_MenuFragments_SendFragment_CheckPaymentID(
        JNIEnv *env, jobject /* this */,jstring PaymentID) {

    string paymentid = env->GetStringUTFChars(PaymentID,0);


    return wallet2.check_payment_id(paymentid);

}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_MenuFragments_SendFragment_CheckAddress(
        JNIEnv *env, jobject /* this */,jstring Address) {

    string address = env->GetStringUTFChars(Address,0);

    return wallet2.check_address(address);

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MainActivity_SendTransfer(
        JNIEnv *env, jobject /* this */,jstring Address,jdouble Amount) {

    string address = env->GetStringUTFChars(Address,0);

    wallet2.transfer(address,Amount *1000000000000,"0000000000000000",1);

    return env->NewStringUTF(wallet2.pending_tx.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_QRGenerator_QRGeneratorFragment_GeneratePaymentId(
        JNIEnv *env, jobject /* this */) {

    return env->NewStringUTF(wallet2.get_payment_id().c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_QRGenerator_QRGeneratorFragment_GetAddress(
        JNIEnv *env, jobject /* this */) {

    return env->NewStringUTF(wallet2.address().c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MenuFragments_ReceiveFragment_GetAddress(
        JNIEnv *env, jobject /* this */) {

    return env->NewStringUTF(wallet2.address().c_str());

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MenuFragments_ReceiveFragment_GeneratePaymentId(
        JNIEnv *env, jobject /* this */) {

    return env->NewStringUTF(wallet2.get_payment_id().c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MenuFragments_SettingsFragment_GetMnemonicseed(JNIEnv *env,
                                                                                jobject instance) {

    return env->NewStringUTF(wallet2.GetMnmonicseed().c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MenuFragments_ReceiveFragment_GetIntegratedAddress(
        JNIEnv *env, jobject /* this */, jstring _paymentid) {

    string paymentid = env->GetStringUTFChars(_paymentid,0);

    return env->NewStringUTF(wallet2.getIntegratedAddress(paymentid).c_str());

}

