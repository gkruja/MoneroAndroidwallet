#include <jni.h>
#include <string>

#include "cryptonote_basic/cryptonote_basic.h"

#include "cryptonote_core/blockchain.h"
#include "cryptonote_core/tx_pool.h"
#include "blockchain_db/lmdb/db_lmdb.h"

#include "common/base58.h"

#include "wallet/wallet2.h"

#include "crypto/crypto.h"
#include "crypto/hash.h"
#include "crypto/random.h"
#include "mnemonics/english.h"
extern "C"
{

#include "crypto/crypto-ops.h"
#include "crypto/keccak.h"

}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_root_monerotest_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {


    size_t size =32;
    uint8_t *bytes = new uint8_t(size);
      crypto::rand(size,bytes);

    char testString[65];

    secret_key rk;

    for(int i = 0; i < 32; i++)
    {
        rk.data[i] = bytes[i];
        sprintf(&testString[i*2], "%02x", (unsigned int)bytes[i]);
    }





    secret_key Private_Spend_key;
    public_key Public_Spend_key;

    secret_key private_viewkey;
    public_key public_viewkey;

    char private_spend_string[65];
    char public_spend_string[65];
    char private_view_string[65];
    char public_view_string[65];
    char resultS[65];

    secret_key test = generate_keys(Public_Spend_key,Private_Spend_key,rk);
    crypto::rand(size,bytes);

//    for(int i = 0; i < 32; i++) {
//        public_viewkey.data[i] = bytes[i];
//    }

     test = generate_keys(public_viewkey,private_viewkey,rk);



    for(int i = 0; i < 32; i++)
    {

        sprintf(&private_spend_string[i*2], "%02x", (unsigned int)Private_Spend_key.data[i]);
        sprintf(&public_spend_string[i*2], "%02x", (unsigned int)Public_Spend_key.data[i]);
        sprintf(&private_view_string[i*2], "%02x", (unsigned int)private_viewkey.data[i]);
        sprintf(&public_view_string[i*2], "%02x", (unsigned int)public_viewkey.data[i]);
        sprintf(&resultS[i*2], "%02x", (unsigned int)test.data[i]);

    }



    std::string hello = "\tPrivate Spend Key \n";


    string temp = private_spend_string;
    hello += temp;
    hello += "\n\tPublic Spend Key \n";
    temp = public_spend_string;
    hello += temp;
    hello +="\n \tPrivate view key\n";
    hello += private_view_string;
    hello += "\n \tPublic view key\n";
    hello += public_view_string;

    uint8_t *md = new uint8_t(size);
    keccak(bytes,size,md,size);



    char kek[65];

    for(int i = 0; i < 32; i++) {
        sprintf(&kek[i * 2], "%02x", (unsigned int) md[i]);
    }

    return env->NewStringUTF(hello.c_str());



}
