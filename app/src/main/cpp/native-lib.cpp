#include <jni.h>
#include <string>
#include "Monero.h"
#include "boost/optional/optional.hpp"
#include "reg_exp_definer.h"
#include <boost/regex/v4/regex_search.hpp>

const static epee::global_regexp_critical_section gregexplock;


Monero::AndroidWallet  wallet2;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MainActivity_ParseQR(JNIEnv *env, jobject instance,
                                                                   jstring Qrresult_) {
    string Qrresult = env->GetStringUTFChars(Qrresult_, 0);

    string regex = "monero:(\\w{95})(\\?tx_amount=(\\d+))?((\\?|&)tx_payment_id=(\\w*))?";
    STATIC_REGEXP_EXPR_1(rexp_match_uri,  regex , boost::regex::icase | boost::regex::normal);

    boost::smatch result;

    boost::regex_search(Qrresult, result, rexp_match_uri, boost::match_default) ;


    string zero = result[0];
    string one = result[1];
    string two = result[2];
    string three = result[3];
    string four = result[4];
    string five = result[5];

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
        jobject /* this */,jstring Path)
{
    bool init =false ;
    string path = env->GetStringUTFChars(Path,0);
    if (std::ifstream(path)) {
        //159.203.250.205:38081
        //192.168.1.141:28081
        init = wallet2.init("159.203.250.205:38081", "password", path, true, 4);
    } else
    {
        wallet2.GenerateWallet(path,"password");

        init = wallet2.init("159.203.250.205:38081", "password", path, true, 4);
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

