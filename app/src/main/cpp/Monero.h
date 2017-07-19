//
// Created by gkruj on 6/18/2017.
//

#ifndef MONEROTEST_MONERO_H

#define MONEROTEST_MONERO_H
//
// Created by gkruj on 6/18/2017.
//

#include <jni.h>
#include <string>
#include <stdlib.h>
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

    ~AndroidWallet(){
        if(wallet2!= nullptr)
        {
            wallet2->store();
        }
        wallet2->deinit();
        delete wallet2;
    }

    bool Check_Connection();

    void transfer(string address, uint64_t ammount, string paymentId, uint32_t priority);

    bool init(string DaemonAddress, string Password, string WalletName, bool testnet, int loglevel);

    bool GenerateWallet(string path, string Name, string Password);
    std::multimap<uint64_t, std::pair<bool,std::string>> get_transfers();

    double Balance() {
        return (wallet2->balance() / (double) 1000000000000); }

    double UnlockedBalance() {
        return (wallet2->unlocked_balance() / (double) 1000000000000); }

    uint64_t WalletLocalHeight(){
        local_height = wallet2->get_blockchain_current_height();
        return local_height;
    }

    uint64_t DaemonHeight(){
        string err;
         bc_height = wallet2->get_daemon_blockchain_height(err);

        uint32_t version =0 ;

        if(! wallet2->check_connection(&version)){
            return 0;
        }

        if(err.empty())
        {
            return bc_height;
        } else{
            return 0;
        }
    }

    void refresh(){
        uint32_t version =0;
        bool connection = wallet2->check_connection(&version);

        if(connection){
            wallet2->refresh();

            wallet2->store();
        }
    }

    string address(){
        return wallet2->get_account().get_public_address_str(wallet2->testnet());
    }

    string get_payment_id(){
        return  epee::string_tools::pod_to_hex(crypto::rand<crypto::hash8>());
    }
    bool isnull(){
        if(wallet2 == nullptr)
            return true;
        else
            return false;
    }

private:
    uint64_t local_height = 0;
    uint64_t bc_height = 0 ;
    tools::wallet2 *wallet2 = nullptr;

};
};


#endif //MONEROTEST_MONERO_H
