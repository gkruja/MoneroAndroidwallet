#include <jni.h>
#include <string>

#include "cryptonote_basic/cryptonote_basic.h"

#include "cryptonote_core/blockchain.h"
#include "cryptonote_core/tx_pool.h"
#include "blockchain_db/lmdb/db_lmdb.h"

#include "common/base58.h"
#include "serialization/binary_utils.h"
#include "string_coding.h"

#include "rapidjson/document.h"
#include "rapidjson/stringbuffer.h"
#include "rapidjson/writer.h"
#include "wallet/wallet2.h"

#include "wallet/wallet2.h"



#include "mnemonics/electrum-words.h"

std::vector<uint8_t > HexToBytes(const std::string& hex) ;

secret_key * get_key_from_hash(crypto::hash& in_hash);

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

    string language {"English"};

    std::string hello = "\n";

    size_t size =32;
    uint8_t *bytes = new uint8_t(size);
      crypto::rand(size,bytes);

    char testString[65];

    secret_key hexseed;

    for(int i = 0; i < 32; i++)
    {
        hexseed.data[i] = bytes[i];
        sprintf(&testString[i*2], "%02x", (unsigned int)bytes[i]);
    }


    crypto::public_key public_spend_key;
    crypto::secret_key private_spend_key;

    char PubSK[65];
    char PrivSK[65];

    crypto::generate_keys(public_spend_key, private_spend_key,hexseed, true);

    for(int i = 0; i < 32; i++)
    {
        sprintf(&PubSK[i*2], "%02x", (unsigned int)public_spend_key.data[i]);
        sprintf(&PrivSK[i*2], "%02x", (unsigned int)private_spend_key.data[i]);

    }


    crypto::secret_key hash_of_hash;

    cn_fast_hash(private_spend_key.data, sizeof(private_spend_key.data), hash_of_hash.data);

    crypto::public_key public_view_key;
    crypto::secret_key private_view_key;

    char PubVK[65];
    char PrivVK[65];

    crypto::generate_keys(public_view_key, private_view_key,hash_of_hash, true);

    for(int i = 0; i < 32; i++)
    {
        sprintf(&PubVK[i*2], "%02x", (unsigned int)public_view_key.data[i]);
        sprintf(&PrivVK[i*2], "%02x", (unsigned int)private_view_key.data[i]);

    }


    // having all keys, we can get the corresponding monero address
    cryptonote::account_public_address address {public_spend_key, public_view_key};

    string addr = cryptonote::get_account_address_as_str(true,address);


    string mnemonic_str ;

    bool r =crypto::ElectrumWords::bytes_to_words(private_spend_key,mnemonic_str,language);




    // simplewallet wallet file name, e.g., mmwallet.bin
    // actually we do not directy create this file. we
    // create a file *.keys containing the address and the private keys
    string wallet_file = "Test";

    string password= "password";
    // name of the keys files
    string keys_file_name = wallet_file + string(".keys");

    cryptonote::account_keys accountKeys = cryptonote::account_keys();

    accountKeys.m_account_address = address;
    accountKeys.m_spend_secret_key = private_spend_key;
    accountKeys.m_view_secret_key = private_view_key;

    std::string account_data;


   bool a = epee::serialization::store_t_to_binary(accountKeys,account_data);


    rapidjson::Document json;
    json.SetObject();
    rapidjson::Value value(rapidjson::kStringType);
    rapidjson::Value keyData(rapidjson::kStringType);

    keyData.SetString("key_data",8);

    value.SetString(account_data.c_str(), account_data.length());






    json.AddMember(keyData,value,json.GetAllocator());


    tools::wallet2::keys_file_data keys_file_data ;
    keys_file_data = boost::value_initialized<tools::wallet2::keys_file_data>();


    // Serialize the JSON object
    rapidjson::StringBuffer buffer;
    rapidjson::Writer<rapidjson::StringBuffer> writer(buffer);
    json.Accept(writer);
    account_data = buffer.GetString();

    crypto::chacha8_key key;
    crypto::generate_chacha8_key(password, key);
    std::string cipher;
    cipher.resize(account_data.size());
    keys_file_data.iv = crypto::rand<crypto::chacha8_iv>();
    crypto::chacha8(account_data.data(), account_data.size(),
                    key, keys_file_data.iv, &cipher[0]);
    keys_file_data.account_data = cipher;

    std::string buf;

    // serialize key file data
    if (!serialization::dump_binary(keys_file_data, buf))
    {
        cerr << "Something went wrong with serializing keys_file_data" << endl;
    }

    // save serialized keys into the wallet file
    if (!epee::file_io_utils::save_string_to_file("/sdcard/monero/"+keys_file_name, buf))
    {
        cerr << "Something went wrong with writing file: " << keys_file_name << endl;
    }


//    FILE* file = fopen("/sdcard/hello.txt","w+");
//
//    if (file != NULL)
//    {
//        fputs("HELLO WORLD! Monero Bitches\n", file);
//        fflush(file);
//        fclose(file);
//    }

    hello+="\n Private Spend Key \n\n";
    hello += PrivSK;

    hello+="\n Public Spend Key\n\n";
    hello += PubSK;
    hello+="\n Private View Key\n\n";
    hello += PrivVK;
    hello+="\n Public view Key\n\n";
    hello += PubVK;

    hello+="\n\n Address\n\n" +addr;

    hello+="\n\n"+mnemonic_str;



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

secret_key* get_key_from_hash(crypto::hash& in_hash)
{
    crypto::secret_key* key;
    key = reinterpret_cast<secret_key*>(&in_hash);
    return key;
}
