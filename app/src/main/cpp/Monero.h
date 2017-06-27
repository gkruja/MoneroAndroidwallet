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
        delete wallet2;
    }

    bool Check_Connection();

    void
    transfer(string address, uint64_t ammount, string paymentId, uint32_t mixin, uint32_t priority);

    bool init(string DaemonAddress, string Password, string WalletName, bool testnet, int loglevel);

    bool GenerateWallet(string path, string Name, string Password);
    std::multimap<uint64_t, std::pair<bool,std::string>> get_transfers();

    double Balance() {
        uint64_t bal =  wallet2->balance();

        double final = (bal / (double) 1000000000000);

        return final;
    }

    double UnlockedBalance() {

        uint64_t ubal =  wallet2->unlocked_balance();

        double final = (ubal / (double) 1000000000000);

        return final;
    }


private:

    tools::wallet2 *wallet2 = nullptr;



};
};


#endif //MONEROTEST_MONERO_H
