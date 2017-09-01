#include "Monero.h"


using namespace Monero;
using namespace epee;
using namespace cryptonote;


bool AndroidWallet::init(string DaemonAddress, string Password, string WalletName, bool testnet, int loglevel){

    if(loglevel == 0 || loglevel < 5) {
        remove("/sdcard/monerolog");
        mlog_configure("/sdcard/monerolog", false);
        mlog_set_log_level(loglevel);
    }

    // public testnet 159.203.250.205:38081
   // daemon.append(DaemonAddress);
    if (wallet2 == nullptr)
    {
        COFFEE_TRY(){
                wallet2 = new tools::wallet2(testnet);
                wallet2->load( WalletName, Password);
            }COFFEE_CATCH(){
               string test;
                coffeecatch_cancel_pending_alarm();
               test += coffeecatch_get_message();
                return false;
            }COFFEE_END();

    }

    bool init = wallet2->init(std::move(DaemonAddress));

    uint32_t version =0 ;
    bool connect = wallet2->check_connection(&version);

    if (connect) {
        return true;
    } else{
        return false;
    }

}

    bool AndroidWallet::GenerateWallet(string path,string Password) {

        boost::filesystem::path dir(path+"/monero");
        boost::filesystem::create_directories(dir);

//        Monero::WalletManagerFactory *test = new Monero::WalletManagerFactory;
//        Monero::WalletManager *walletManager = test->getWalletManager();
//
//        Monero::Wallet *wallet = walletManager->createWallet(path+"/monero/"+Name, Password, "English", true);
//        delete  wallet;


        remove("/sdcard/monerolog");
        mlog_configure("/sdcard/monerolog", false);
        mlog_set_log_level(4);

        bool keys_file;
        bool wallet_file;
        tools::wallet2::wallet_exists(path,keys_file,wallet_file);

        wallet2 = new tools::wallet2(false, false);
        if(wallet_file || keys_file)
        {
            return false;
        }
        crypto::secret_key secretKey, recover_key ;
        try {
         recover_key  =    wallet2->generate(path, Password, secretKey, false, false);
        }catch (const std::exception &e){
            LOG_ERROR("wallet Error creating:" << e.what());
            return false;
        }

        return true;

    }


bool AndroidWallet::GenerateWallet(string path, string Password, string walletname ) {

    boost::filesystem::path dir(path);
    boost::filesystem::create_directories(dir);

//        Monero::WalletManagerFactory *test = new Monero::WalletManagerFactory;
//        Monero::WalletManager *walletManager = test->getWalletManager();
//
//        Monero::Wallet *wallet = walletManager->createWallet(path+"/monero/"+Name, Password, "English", true);
//        delete  wallet;

string temp = dir.string();
    remove("/sdcard/monerolog");
    mlog_configure("/sdcard/monerolog", false);
    mlog_set_log_level(4);

    bool keys_file;
    bool wallet_file;
    tools::wallet2::wallet_exists("/sdcard/monero/"+walletname,keys_file,wallet_file);

    wallet2 = new tools::wallet2(false, false);
    if(wallet_file || keys_file)
    {
        return false;
    }
    crypto::secret_key secretKey, recover_key ;
    COFFEE_TRY() {
        recover_key  =    wallet2->generate("/sdcard//monero/"+walletname, Password, secretKey, false, false);
    }COFFEE_CATCH (){
        coffeecatch_cancel_pending_alarm();
        coffeecatch_cleanup();
        LOG_ERROR(coffeecatch_get_message());
        return false;
    }

    return true;

}
    bool AndroidWallet::Check_Connection(){

        bool connection = wallet2->check_connection();


        if(!connection)
        {
            return false;
        }

        return true;
    }

    static std::string get_human_readable_date(uint64_t ts)
    {
    char buffer[64];

    if (ts < 1234567890)
        return "<unknown>";
    time_t tt = ts;
    struct tm tm;

    gmtime_r(&tt, &tm);

        strftime(buffer, sizeof(buffer), "%Y-%m-%d", &tm);
    return std::string(buffer);
}
static std::string get_human_readable_time(uint64_t ts)
{
    char buffer[64];

    if (ts < 1234567890)
        return "<unknown>";
    time_t tt = ts;
    struct tm tm;

    gmtime_r(&tt, &tm);

    strftime(buffer, sizeof(buffer), "%I:%M:%S %p", &tm);

    return std::string(buffer);
}

std::multimap<uint64_t, std::pair<bool,std::string>> AndroidWallet::get_transfers() {

    std::multimap<uint64_t, std::pair<bool,std::string>> output;

    bool in = true;
    bool out = true;
    uint64_t  min_height =0;
string err;


    uint64_t  max_height =(uint64_t)-1;
    if (in) {
        std::list<std::pair<crypto::hash, tools::wallet2::payment_details>> payments;
        wallet2->get_payments(payments,min_height,max_height);
        for (std::list<std::pair<crypto::hash, tools::wallet2::payment_details>>::const_iterator i = payments.begin(); i != payments.end(); ++i) {
            const tools::wallet2::payment_details &pd = i->second;
            std::string payment_id = string_tools::pod_to_hex(i->first);
            if (payment_id.substr(16).find_first_not_of('0') == std::string::npos)
                payment_id = payment_id.substr(0,16);
            std::string note = wallet2->get_tx_note(pd.m_tx_hash);
            string temp = "" ;
            temp = "{\"type\":\"in\" ,\"amount\": " +print_money(pd.m_amount)+","+
                    "\"blockheight\": "+ std::to_string(pd.m_block_height)+" ,"+
                    "\"TX\": \""+ string_tools::pod_to_hex(pd.m_tx_hash)+"\" ,"+
                    "\"Payment_id\": \""+ payment_id+"\" ,"+
                    "\"fee\":"+ "0.0 ,"+
                    "\"time\": \""+ get_human_readable_time(pd.m_timestamp)+"\","+
                    "\"date\": \""+get_human_readable_date(pd.m_timestamp)+"\"}";


            output.insert(std::make_pair(pd.m_block_height, std::make_pair(true,temp )));
        }
    }

    if (out) {
        std::list<std::pair<crypto::hash, tools::wallet2::confirmed_transfer_details>> payments;
        wallet2->get_payments_out(payments, min_height, max_height);
        for (std::list<std::pair<crypto::hash, tools::wallet2::confirmed_transfer_details>>::const_iterator i = payments.begin(); i != payments.end(); ++i) {
            const tools::wallet2::confirmed_transfer_details &pd = i->second;
            uint64_t change = pd.m_change == (uint64_t)-1 ? 0 : pd.m_change; // change may not be known
            uint64_t fee = pd.m_amount_in - pd.m_amount_out;
            std::string dests;
            for (const auto &d: pd.m_dests) {
                if (!dests.empty())
                    dests += ", ";
                string address = get_account_address_as_str(wallet2->testnet(), d.addr);
                address = address.substr(0,7)+ "...."+address.substr(address.length()-8,address.length()) ;

                dests +=  address + ": " + print_money(d.amount);
            }
            std::string payment_id = string_tools::pod_to_hex(i->second.m_payment_id);
            if (payment_id.substr(16).find_first_not_of('0') == std::string::npos)
                payment_id = payment_id.substr(0,16);
            std::string note = wallet2->get_tx_note(i->first);
            string temp  ="" ;
            temp = "{\"type\":\"out\" ,\"amount\": " +print_money(pd.m_amount_in - change - fee)+","+
                    "\"blockheight\": "+ std::to_string(pd.m_block_height)+" ,"+
                   "\"TX\": \""+ string_tools::pod_to_hex(i->first)+"\" ,"+
                    "\"Payment_id\": \""+ payment_id+"\" ,"+
                    "\"fee\":"+ print_money(fee)+","+
                    "\"time\": \""+ get_human_readable_time(pd.m_timestamp)+"\","+
                   "\"date\": \""+get_human_readable_date(pd.m_timestamp)+"\"}";

            output.insert(std::make_pair(pd.m_block_height, std::make_pair(false, temp)));
        }
    }


return output;


}

void AndroidWallet::transfer(string address, uint64_t ammount, string paymentId, uint32_t priority)
{

    int transfer_type = TransferNew;

    size_t fake_outs_count = 4;

    if (fake_outs_count == 0) {
        fake_outs_count = 4;
    }

    std::vector<uint8_t> extra;
    bool payment_id_seen = false;
    bool expect_even = (transfer_type == TransferLocked);

    std::string payment_id_str = paymentId;

    crypto::hash payment_id;
    bool r = tools::wallet2::parse_long_payment_id(payment_id_str, payment_id);
    if (r) {
        std::string extra_nonce;

        cryptonote::set_payment_id_to_tx_extra_nonce(extra_nonce, payment_id);

        r = cryptonote::add_extra_nonce_to_tx_extra(extra, extra_nonce);
    } else {
        crypto::hash8 payment_id8;
        r = tools::wallet2::parse_short_payment_id(payment_id_str, payment_id8);
        if (r) {
            std::string extra_nonce;
            cryptonote::set_encrypted_payment_id_to_tx_extra_nonce(extra_nonce, payment_id8);
            r = cryptonote::add_extra_nonce_to_tx_extra(extra, extra_nonce);
        }
    }

    if (!r) {
        //   fail_msg_writer() << tr("payment id has invalid format, expected 16 or 64 character hex string: ") << payment_id_str;
        //return true;
        return; "payment id has invalid format, expected 16 or 64 character hex string: ";
    }
    payment_id_seen = true;

    uint64_t locked_blocks = 0;
    if (transfer_type == TransferLocked) {
        try {
            //  locked_blocks = boost::lexical_cast<uint64_t>(local_args.back());
        }
        catch (const std::exception &e) {
            //  fail_msg_writer() << tr("bad locked_blocks parameter:") << " " << local_args.back();
            //    return true;
        }
        if (locked_blocks > 1000000) {
            //   fail_msg_writer() << tr("Locked blocks too high, max 1000000 (˜4 yrs)");
            // return true;
        }
        //  local_args.pop_back();
    }

    std::vector<cryptonote::tx_destination_entry> dsts;
    cryptonote::tx_destination_entry de;
    bool has_payment_id;
    crypto::hash8 new_payment_id;
    if (!cryptonote::get_account_address_from_str_or_url(de.addr, has_payment_id,
                                                         new_payment_id, wallet2->testnet(),
                                                         address)) {
        //  fail_msg_writer() << tr("failed to parse address");
        //  return true;
    }
    if (has_payment_id) {
        if (payment_id_seen) {
            //  fail_msg_writer() << tr("a single transaction cannot use more than one payment id: ") << local_args[i];
            //  return true;
        }
        std::string extra_nonce;
        cryptonote::set_encrypted_payment_id_to_tx_extra_nonce(extra_nonce, new_payment_id);
        bool r = cryptonote::add_extra_nonce_to_tx_extra(extra, extra_nonce);
        if (!r) {
            //    fail_msg_writer() << tr("failed to set up payment id, though it was decoded correctly");
            //    return true;
        }
        payment_id_seen = true;
    }

    de.amount = ammount;
    // bool ok = cryptonote::parse_amount(de.amount, to_string(ammount));
    //   if(!ok || 0 == de.amount)
    //    {
    //   fail_msg_writer() << tr("amount is wrong: ") << local_args[i] << ' ' << local_args[i + 1] <<
    //                     ", " << tr("expected number from 0 to ") << print_money(std::numeric_limits<uint64_t>::max());
    //    return true;
    //   }

    dsts.push_back(de);

    // prompt is there is no payment id and confirmation is required

    //try {
    COFFEE_TRY(){
        // figure out what tx will be necessary
        std::vector<tools::wallet2::pending_tx> ptx_vector;
        uint64_t bc_height, unlock_block = 0;
        std::string err;
        switch (transfer_type) {
            case TransferLocked:
                bc_height = wallet2->get_daemon_blockchain_height(err);
                if (!err.empty()) {

                }
                unlock_block = bc_height + locked_blocks;
                ptx_vector = wallet2->create_transactions_2(dsts, fake_outs_count,
                                                            unlock_block /* unlock_time */,
                                                            priority, extra, true);
                break;
            case TransferNew:
                ptx_vector = wallet2->create_transactions_2(dsts, fake_outs_count,
                                                            0 /* unlock_time */, priority,
                                                            extra, true);
                break;
            default:
                LOG_ERROR("Unknown transfer method, using original");
            case TransferOriginal:
                ptx_vector = wallet2->create_transactions(dsts, fake_outs_count,
                                                          0 /* unlock_time */, priority, extra,
                                                          true);

                break;
        }

        if (ptx_vector.empty()) {
            //  fail_msg_writer() << tr("No outputs found, or daemon is not ready");
            // return true;
        }

        // if more than one tx necessary, prompt user to confirm
        if (wallet2->always_confirm_transfers() || ptx_vector.size() > 1) {
            uint64_t total_sent = 0;
            uint64_t total_fee = 0;
            uint64_t dust_not_in_fee = 0;
            uint64_t dust_in_fee = 0;
            for (size_t n = 0; n < ptx_vector.size(); ++n) {
                total_fee += ptx_vector[n].fee;
                for (auto i: ptx_vector[n].selected_transfers)
                    total_sent += wallet2->get_transfer_details(i).amount();
                total_sent -= ptx_vector[n].change_dts.amount + ptx_vector[n].fee;

                if (ptx_vector[n].dust_added_to_fee)
                    dust_in_fee += ptx_vector[n].dust;
                else
                    dust_not_in_fee += ptx_vector[n].dust;
            }


//            std::stringstream prompt;
//            prompt << boost::format(tr("Sending %s.  ")) % print_money(total_sent);
//            if (ptx_vector.size() > 1)
//            {
//                prompt << boost::format(tr("Your transaction needs to be split into %llu transactions.  "
//                                                   "This will result in a transaction fee being applied to each transaction, for a total fee of %s")) %
//                          ((unsigned long long)ptx_vector.size()) % print_money(total_fee);
//            }
//            else
//            {
//                prompt << boost::format(tr("The transaction fee is %s")) %
//                          print_money(total_fee);
//            }
//            if (dust_in_fee != 0) prompt << boost::format(tr(", of which %s is dust from change")) % print_money(dust_in_fee);
//            if (dust_not_in_fee != 0)  prompt << tr(".") << ENDL << boost::format(tr("A total of %s from dust change will be sent to dust address"))
//                                                                    % print_money(dust_not_in_fee);
//            if (transfer_type == TransferLocked)
//            {
//                float days = locked_blocks / 720.0f;
//               // prompt << boost::format(tr(".\nThis transaction will unlock on block %llu, in approximately %s days (assuming 2 minutes per block)")) % ((unsigned long long)unlock_block) % days;
//            }
//            if (wallet2->print_ring_members())
//            {
//
//            }
            //  prompt << ENDL << tr("Is this okay?  (Y/Yes/N/No): ");
        }

        // actually commit the transactions
        if (wallet2->watch_only()) {
            bool r = wallet2->save_tx(ptx_vector, "unsigned_monero_tx");
            if (!r) {
                // fail_msg_writer() << tr("Failed to write transaction(s) to file");
            } else {
                // success_msg_writer(true) << tr("Unsigned transaction(s) successfully written to file: ") << "unsigned_monero_tx";
            }
        } else
            while (!ptx_vector.empty()) {
                auto &ptx = ptx_vector.back();
                wallet2->commit_tx(ptx);
                //    success_msg_writer(true) << tr("Money successfully sent, transaction ") << get_transaction_hash(ptx.tx);

                // if no exception, remove element from vector
                ptx_vector.pop_back();
                pending_tx = epee::string_tools::pod_to_hex(cryptonote::get_transaction_hash(ptx.tx));


                wallet2->store_tx_info();

            }
    }COFFEE_CATCH(){
        coffeecatch_cancel_pending_alarm();
        coffeecatch_cleanup();
       pending_tx =  "An Error Occured when creating the Transaction";
        return;
    }COFFEE_END();
//    catch (const tools::error::daemon_busy &) {
//        //    fail_msg_writer() << tr("daemon is busy. Please try again later.");
//        string ret = "daemon is busy. Please try again later.";
//    }
//    catch (const tools::error::no_connection_to_daemon &) {
//        //   fail_msg_writer() << tr("no connection to daemon. Please make sure daemon is running.");
//        string ret = "unknown error";
//
//    }
//    catch (const tools::error::wallet_rpc_error &e) {
//        //LOG_ERROR("RPC error: " << e.to_string());
//        //      fail_msg_writer() << tr("RPC error: ") << e.what();
//        string ret = "unknown error";
//    }
//    catch (const tools::error::get_random_outs_error &e) {
//        //  fail_msg_writer() << tr("failed to get random outputs to mix: ") << e.what();
//        string ret = "unknown error";
//    }
//    catch (const tools::error::not_enough_money &e) {
//        LOG_PRINT_L0(boost::format("not enough money to transfer, available only %s, sent amount %s") %
//                    print_money(e.available()) %
//                     print_money(e.tx_amount()));
//        fail_msg_writer() << tr("Not enough money in unlocked balance");
//        string ret = "unknown error";
//    }
//    catch (const tools::error::tx_not_possible &e) {
//
//        LOG_PRINT_L0(boost::format("not enough money to transfer, available only %s, transaction amount %s = %s + %s (fee)") %
//                     print_money(e.available()) %
//                     print_money(e.tx_amount() + e.fee())  %
//                     print_money(e.tx_amount()) %
//                     print_money(e.fee()));
//        fail_msg_writer() << tr("Failed to find a way to create transactions. This is usually due to dust which is so small it cannot pay for itself in fees, or trying to send more money than the unlocked balance, or not leaving enough for fees");
//        string ret = "unknown error";
//    }
//    catch (const tools::error::not_enough_outs_to_mix &e) {
//        auto writer = fail_msg_writer();
//        writer << tr("not enough outputs for specified mixin_count") << " = " << e.mixin_count() << ":";
//        for (std::pair<uint64_t, uint64_t> outs_for_amount : e.scanty_outs())
//        {
//            writer << "\n" << tr("output amount") << " = " << print_money(outs_for_amount.first) << ", " << tr("found outputs to mix") << " = " << outs_for_amount.second;
//        }
//        string ret = "unknown error";
//    }
//    catch (const tools::error::tx_not_constructed &) {
//        //   fail_msg_writer() << tr("transaction was not constructed");
//        string ret = "unknown error";
//
//    }
//    catch (const tools::error::tx_rejected &e) {
//        //    fail_msg_writer() << (boost::format(tr("transaction %s was rejected by daemon with status: ")) % get_transaction_hash(e.tx())) << e.status();
//        std::string reason = e.reason();
//        // if (!reason.empty())
//        //    fail_msg_writer() << tr("Reason: ") << reason;
//        string ret = "unknown error";
//    }
//    catch (const tools::error::tx_sum_overflow &e) {
//        //  fail_msg_writer() << e.what();
//        string ret = "unknown error";
//    }
//    catch (const tools::error::zero_destination &) {
//        //  fail_msg_writer() << tr("one of destinations is zero");
//        string ret = "unknown error";
//    }
//    catch (const tools::error::tx_too_big &e) {
//        //  fail_msg_writer() << tr("failed to find a suitable way to split transactions");
//        string ret = "unknown error";
//    }
//    catch (const tools::error::transfer_error &e) {
//       // LOG_ERROR("unknown transfer error: " << e.to_string());
//        //   fail_msg_writer() << tr("unknown transfer error: ") << e.what();
//        string ret = "unknown error";
//    }
//    catch (const tools::error::wallet_internal_error &e) {
//      //  LOG_ERROR("internal error: " << e.to_string());
//        //  fail_msg_writer() << tr("internal error: ") << e.what();
//        string ret = "unknown error";
//    }
//    catch (const std::exception &e) {
//       // LOG_ERROR("unexpected error: " << e.what());
//        //  fail_msg_writer() << tr("unexpected error: ") << e.what();
//        string ret = "unknown error";
//    }
//    catch (...) {
//      //  LOG_ERROR("unknown error");
//        //fail_msg_writer() << tr("unknown error");
//        string ret = "unknown error";
//
//    }

}


    AndroidWallet::AndroidWallet() {
    delete wallet2;
    wallet2 = nullptr;
}

string AndroidWallet::getIntegratedAddress(string _paymentID)
{

    hash8 paymentid;
    if(tools::wallet2::parse_short_payment_id(_paymentID,paymentid)){

        return wallet2->get_account().get_public_integrated_address_str(paymentid, wallet2->testnet());

    } else{ return "";}

}

string AndroidWallet::getDaemonaddress() {
    if(wallet2 == nullptr)
        return "";
    return wallet2->get_daemon_address();
}

bool AndroidWallet::reinit(string address) {

    if(wallet2 == nullptr)
        return false;

    bool deinit = wallet2->deinit();

    if(deinit)
    {
        return wallet2->init(address);
    }
}

bool AndroidWallet::check_payment_id(string payment_id_str) {
    crypto::hash payment_id;

    bool r = tools::wallet2::parse_long_payment_id(payment_id_str, payment_id);
    if (r) {
        return true;

    } else {
        crypto::hash8 payment_id8;
        r = tools::wallet2::parse_short_payment_id(payment_id_str, payment_id8);
        if (r) {
            return true;
        }
    }
    if (!r) {

        return false;
    }
}

bool AndroidWallet::check_address(string address) {
    cryptonote::account_public_address account_public_address;

    bool check = cryptonote::get_account_address_from_str(account_public_address,wallet2->testnet(),address);

    return check;
}

string AndroidWallet::get_payment_id() {
    return  epee::string_tools::pod_to_hex(crypto::rand<crypto::hash8>());
}

void AndroidWallet::refresh() {
    uint32_t version =0;
    bool connection = wallet2->check_connection(&version);

    if(connection){
        COFFEE_TRY(){
            wallet2->refresh();
            wallet2->store();
        }COFFEE_CATCH(){
            coffeecatch_cleanup();
            coffeecatch_cancel_pending_alarm();
            return;
        }
    }
}

uint64_t AndroidWallet::DaemonHeight() {
    string err;
    bc_height = wallet2->get_daemon_blockchain_height(err);

    uint32_t version =0 ;

    if(! wallet2->check_connection(&version)){
        return 0;
    }

    if(err.empty())
    {
        return bc_height;
    } else{ return 0; }
}

uint64_t AndroidWallet::WalletLocalHeight() {
    local_height = wallet2->get_blockchain_current_height();
    return local_height;
}

double AndroidWallet::UnlockedBalance() {
    return (wallet2->unlocked_balance() / (double) 1000000000000); }

double AndroidWallet::Balance() {
    return (wallet2->balance() / (double) 1000000000000); }

AndroidWallet::~AndroidWallet() {
    if(wallet2!= nullptr)
    {
        wallet2->store();
    }
    wallet2->deinit();
    delete wallet2;
}

bool AndroidWallet::deinit() {
    return wallet2->deinit();
}

bool AndroidWallet::isnull() {
    if(wallet2 == nullptr)
        return true;
    else
        return false;

}

bool AndroidWallet::Generatefromseed(string path, string seed, string WalletName, string password,
                                     bool testnet)
{

    boost::filesystem::path dir(path+"/monero");
    boost::filesystem::create_directories(dir);

    wallet2 = new tools::wallet2(false, false);


    crypto::secret_key secretKey, recoverykey ;
    if( epee::string_tools::hex_to_pod(seed,secretKey))
    {
        try {

            recoverykey   =    wallet2->generate(path, password, secretKey,true, false);

        }catch (const std::exception &e){

            LOG_ERROR("wallet Error creating:" << e.what());

            return false;
        }

        return true;

    }else{return false;}

}

bool AndroidWallet::GeneratefromMnemonic(string path, string mnemonic, string language,
                                         string WalletName, string password, bool testnet)
{


    boost::filesystem::path dir(path+"/monero");
    boost::filesystem::create_directories(dir);


    bool keys_file;
    bool wallet_file;
    tools::wallet2::wallet_exists(path+"/"+WalletName,keys_file,wallet_file);

    wallet2 = new tools::wallet2(false, false);
    if(wallet_file || keys_file)
    {
        return false;
    }

    crypto::secret_key secretKey,recoverykey;
    if(crypto::ElectrumWords::words_to_bytes(mnemonic,secretKey,language))
    {
        try {

            recoverykey   =  wallet2->generate(path+"/"+WalletName, password, secretKey,true, false);

        }catch (const std::exception &e){

            LOG_ERROR("wallet Error creating:" << e.what());

            return false;
        }

        return true;
    }else{ return false;}

}



