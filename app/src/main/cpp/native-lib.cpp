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

std::vector<uint8_t > HexToBytes(const std::string& hex) ;

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
    char resultS[131];

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

    }

    strcat(&resultS[0],"12");
    strcat(&resultS[0], public_spend_string);
    strcat(&resultS[0], public_view_string);

    std::string hello = "\n           Private Spend Key \n\n";


    string temp = private_spend_string;
    hello += temp;
    hello += "\n\n           Public Spend Key \n\n";
    temp = public_spend_string;
    hello += temp;
    hello +="\n\n           Private view key\n\n";
    hello += private_view_string;
    hello += "\n\n           Public view key\n\n";
    hello += public_view_string;

    uint8_t *md = new uint8_t(32);


    uint8_t * addres_pull = new uint8_t[69];
    uint8_t *final = new uint8_t(65);



    // step 1 of wallet generation




//
//    final[0] = 18;
//    for(int i = 0; i < 32; i++) {
//
//        final[i+1] = Public_Spend_key.data[i];
//        final[i+32] = public_viewkey.data[i];
//
//    }



     string hex  = resultS;
    vector<uint8_t> out = HexToBytes( hex);


    for (int i = 0; i < out.size(); ++i) {
        final[i] = out[i];
        addres_pull[i] = out[i];
    }


        char nb[2];
    char keyAckeck[65];
    char keyBcheck[65];
    sprintf(&nb[0 * 2], "%02x", (unsigned int) final[0]);

    for(int i = 1; i < 33; i++) {
        sprintf(&keyAckeck[(i-1) * 2], "%02x", (unsigned int) final[i]);
        sprintf(&keyBcheck[(i-1) * 2], "%02x", (unsigned int) final[i+31]);

    }


    // step 2 keccak-256 keys

    keccak(final,65,md,32);


    addres_pull[65] =md[0];
    addres_pull[66] =md[1];
    addres_pull[67] =md[2];
    addres_pull[68] =md[3];\


    char tempMd[8];
    for(int i = 0; i < 4; i++) {

        sprintf(&tempMd[i * 2], "%02x", (unsigned int) md[i]);
    }

    //strcat(hex,tempMd);
    hex += tempMd;

    string address = tools::base58::encode_addr(95,hex);

    return env->NewStringUTF(hello.c_str());

}



std::vector<uint8_t > HexToBytes(const std::string& hex) {
    std::vector<uint8_t > bytes;

    for (unsigned int i = 0; i < hex.length(); i += 2) {
        std::string byteString = hex.substr(i, 2);
        char byte = (char) strtol(byteString.c_str(), NULL, 16);
        bytes.push_back(byte);
    }

    return bytes;
}