#include <jni.h>
#include <string>
#include "Monero.h"



Monero::AndroidWallet  wallet2;


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_root_monerotest_MainActivity_InitWallet(
        JNIEnv *env,
        jobject /* this */)
{
    bool init = wallet2.init( "http://192.168.1.141:28081", "password", "example", true, 4);

    return init;
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
JNIEXPORT jobjectArray JNICALL
Java_com_example_root_monerotest_DashboardFragment_Transfers(
        JNIEnv *env,
        jobject /* this */)
{
    std::multimap<uint64_t, std::pair<bool,std::string>> transfer  = wallet2.get_transfers();



    int count =0 ;
    string temp;
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

        if(in_out) {
            temp ="<html><font color=\'green\'>IN</font></html> "  + address;
        } else{

            temp = "<html><font color=\'red\'>OUT</font></html> "    +address;
        }
        str = env->NewStringUTF(temp.c_str());

        env->SetObjectArrayElement(payment,count,str);
        count++;



    }


    return payment;
}
extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_root_monerotest_DashboardFragment_Balance(
        JNIEnv *env,
        jobject /* this */)
{
    double balance = wallet2.Balance();

    return balance;
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_root_monerotest_DashboardFragment_UnlockedBalance(
        JNIEnv *env,
        jobject /* this */)
{
    double unlockedbalance = wallet2.UnlockedBalance();
    return unlockedbalance;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MenuFragments_SendFragment_SendTransfer(
        JNIEnv *env,
        jobject /* this */,jstring Address,jdouble Amount,jint Mixin)
{


    string address = env->GetStringUTFChars(Address,0);


    wallet2.transfer(address,Amount *1000000000000,"",4,0);



    return env->NewStringUTF(wallet2.pending_tx.c_str());
}

