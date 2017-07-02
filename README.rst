
Build in IntelliJ:
------------------


* Project Structure > Artifacts > JAR > From modules etc. > Select main class from dropdown + extract to target JAR > OK.

* Build > Build artifacts > Build

* Move the manifest:

.. note::

    Make sure your MANIFEST.MF is in:
        src/main/resources/META_INF/
    NOT
        src/main/java/META_INF/

* The executable is created in 
    out > artifacts > [project]_jar > [project].jar

* Run it: 
    java -jar [project].jar




