# Monero Android Wallet (NDK & SDK java)

This App is using the NDK (native development kit) to port the native Monero C++/C 
code into it. The NDK is using a interface called JNI. This interface will sit
between the Monero native code and the Java application. This proyect is public and
available for anyone who wants to colaborate. 

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
navationView (The menu). Every item of the menu is attach to its corresponding
fragment. Only one (1) activity is being used. The toolbar changes custom views
depending  on the fragment that the user is currently located.



### Currently completed but might be missing more testing

- [x] Binaries for Armv7 (32bit), armV8 (64bit), and x86.
- [x] Generate a new wallet.
- [x] Load wallet from external storage.
- [x] Sync wallet to remote node.
- [x] Send transactions (little buggy, needs more testing).
- [x] Display all transactions from wallet into a Listview. 

## What is currently missing?

- [] \(Optional) Splash screen to give a better experience to user while loading libs.
- [] Initial activity to either generate a new wallet or load one from file storage.
		**note:** *For loading wallet from location I'd like to using some sort of file 
		explorer to allow the user to chose the location. However, i'm up for ideas.*

- [] Functionality to select a remote node in the settings fragment.
- [] Ability to generate a QR image using the integrated address. (Receive Fragment)
- [] Ability to read a QR image and validate data. (Send Fragment)
- [] if you have more ideas or features, please PM me.



