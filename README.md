
Build in IntelliJ:
------------------

* Had to move the manifest:
  Make sure your MANIFEST.MF is in:
      src/main/resources/META_INF/
  NOT
      src/main/java/META_INF/

* Define the build of a JAR (only the first time):
  File > Project Structure > Artifacts > JAR > From modules etc. > 
  Select main class from dropdown + extract to target JAR > OK.

* Build the JAR:
  Build > Build artifacts > Build

* The executable is created in 
    out > artifacts > internist_jar > internist.jar

* Run it: 

    ```bash
    java -jar internist.jar
    ```




