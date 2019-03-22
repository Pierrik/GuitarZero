# GuitarZero

Instructions:


Server:
Put Server.java, Handler.java and InvalidSongException.java in one directory.

cd to this directory and enter the following commands -
  javac Server.java
  java Server



Store Manager Mode:
Put StoreManagerMain.java, StoreManagerController.java, StoreManagerModel.java, StoreManagerView.java, StoreManagerButton.java

cd to this directory and enter the following commands -
  javac StoreManagerMain.java
  java StoreManagerMain



GuitarZero:
Have ALL source files in one directory. Ensure the local_store directory is set up properly if intend on using Select mode, and ensure the
server is running if you intend on buying songs in Store mode. local_store/bundle_files/ is where bundles are stored, and 
local_store/preview_files is where the store mode caching is stored.

cd to this directory and enter the following commands -

(Windows)
  set CLASSPATH=jinput-2.0.9.jar;.
  javac *.java
  java Run
  
(Linux)
  $ CLASSPATH=jinput-2.0.9.jar:.
  $ export CLASSPATH
  $ javac *.java
  $ java Run
