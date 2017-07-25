
#ifndef MONEROTEST_MONERO_H

#define MONEROTEST_MONERO_H

#include <jni.h>
#include <string>
#include <stdlib.h>
#include <exception>
#include <stdexcept>


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

#include "net/net_helper.h"
#include "wallet/api/wallet_manager.h"
#include "wallet/api/wallet.h"
#include "mnemonics/electrum-words.h"
#include "boost/optional/optional.hpp"
#include "reg_exp_definer.h"
#include "boost/format.hpp"


namespace Monero{
class AndroidWallet {
public:
    enum TransferType {
        TransferOriginal,
        TransferNew,
        TransferLocked,
    };
    string pending_tx = "";
    AndroidWallet();

    ~AndroidWallet();

    bool Check_Connection();

    void transfer(string address, uint64_t ammount, string paymentId, uint32_t priority);

    bool init(string DaemonAddress, string Password, string WalletName, bool testnet, int loglevel);

    bool GenerateWallet(string path, string Password);

    std::multimap<uint64_t, std::pair<bool,std::string>> get_transfers();

    double Balance();

    double UnlockedBalance();

    uint64_t WalletLocalHeight();

    uint64_t DaemonHeight();

    void refresh();

    string address(){ return wallet2->get_account().get_public_address_str(wallet2->testnet()); }

    string get_payment_id();

    bool isnull();

    bool check_address(string address);

    bool check_payment_id(string payment_id_str);

    bool deinit();
    bool reinit(string address);

    string getDaemonaddress();

    string getIntegratedAddress(string _paymentID);

    bool Generatefromseed(string path, string seed,string WalletName,string password,bool testnet);

    bool GeneratefromMnemonic(string path ,string mnemonic,string language,string WalletName,string password,bool testnet);

private:
    uint64_t local_height = 0;
    uint64_t bc_height = 0 ;
    tools::wallet2 *wallet2 = nullptr;

};
};


#endif //MONEROTEST_MONERO_H
