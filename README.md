# Monero Android Wallet (NDK & SDK java)

This App is using the NDK (native development kit) to port the native Monero C++/C 
code into it. The NDK is using a interface called JNI. This interface will sit
between the Monero native code and the Java application. This project is public and
available for anyone who wants to collaborate.

### NDK native functions

To create a native function you will need to use the keyword native like this:
`public native int DaemonHeight()`

The native function will also need to be linked in the JNI file.
The JNI file is called native-lib.cpp located in the app/src/main/cpp
There, you have to specify where the native function will be used. For example: 

`Java_com_example_root_monerotest_Services_SyncWalletService_InitWallet` 

 (       Project package        )(Package)  (   FileName   ) (Method)


You can find native methods all around the proyect. Just make sure that you are
loading the lib in the file that will be using it. For example:

`    static {
        System.loadLibrary("native-lib");
    }`


### App's UI

At this moment, all the navigation is contain in a drawerLayout that holds a 
`NavigationView` (The menu). Every item of the menu is attached to its corresponding
fragment. Only one (1) activity is being used. The toolbar changes custom views
depending  on the fragment that the user is currently located.

### How to Contribute
A good way to help is to test, and report bugs. ( Please wait until the application is stable enough for testing first)

Patches are preferably to be sent via a GitHub pull request. If that can't be done, contact me either on IRC, reddit or directly on GitHub and something can be figured out

Patches should be self contained. A good rule of thumb is to have one patch per separate issue, feature, or logical change. Also, no other changes, such as random whitespace changes or reindentation. Following the code style of the particular chunk of code you're modifying is encourgaged. Proper squashing should be done (eg, if you're making a buggy patch, then a later patch to fix the bug, both patches should be merged). 

### Currently completed but might be missing more testing

- [x] Binaries for Armv7 (32bit), armV8 (64bit), and x86.
- [x] Generate a new wallet.
- [x] Load wallet from external storage.
- [x] Sync wallet to remote node.
- [x] Send transactions (little buggy, needs more testing).
- [x] Display all transactions from wallet into a `ListView`.
- [x] Ability to generate a QR image using the integrated address.
- [x] Ability to read a QR image and validate data. (Send Fragment)
### What is currently missing?

- [ ] \(Optional) Splash screen to give a better experience to user while loading libs.
- [ ] Initial activity to either generate a new wallet or load one from file storage.
		**note:** *For loading wallet from location I'd like to using some sort of file 
		explorer to allow the user to chose the location. However, i'm up for ideas.*
- [ ] Functionality to select a remote node in the settings fragment.
- [ ] Wallet will automatically update itself whenever the phone is plugged and connect to WIFI.

##### if you have more ideas or features, please PM me.



