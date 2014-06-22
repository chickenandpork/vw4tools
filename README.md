vw4tools
========

Tools and utilities for VirtualWisdom4

At current, VW4Tools is a script or two, plus one big jar file.  vw4tool.jar
allows a user to import a text file in one of several formats and output an
Entity import JSON for VirtualWisdom4.  

Running Java
============

In this document, it is assumed "java" runs from the command prompt.  In some cases, this isn't true.  Windows users, for example, may need to change their usage, from:

    java -jar vw4tool.jar ...

to:

    java.exe -jar vw4tool.jar ...


In both cases, if the "java" (or java.exe) executable is not available on the user's "path" or list of locations wherein to find a binary/executable, the user will need to type the full pathname to the java executable.

Macintosh OSX users can simply type "java":

    java -jar vw4tool.jar ...

A UNIX (or unix-like) user may need to type /opt/local/bin/java or similar:

    /opt/local/bin/java -jar vw4tool.jar ...

A Windows user may need to type a full pathname such as:

    "C:\Program Files (x86)\Java\jre7\bin\java.exe" -jar vw4tool.jar ...

In all cases, where I type merely "java", make sure you replace that with however the invocation details are for your platform.

To confirm you've found a working Java Runtime Environment, use the "help" command to as vw4tool.jar what version it is:

    java -jar vw4tool.jar --help
    Usage: vw4tool -V|--version|-H|--help
         : vw4tool --read <filename>|--input <filename> | -r <filename> | -i <filename>
       ie: vw4tool --read import.json
       ie: vw4tool -r import.json

note: many commands have shorter versions.  "--version" (with two "-") is the same as "-V" (with one dash).  Pay special attention to case.  Uppercase "N" is different from lowercase "n".




Proprietary Intellectual Property
=================================
With the exception of samples/import01.json, all files and content are based on
the same level of access to the product enjoyed by a customer.  Implicitly, no
private information is shared, all of this content (save the one file) is based
on empirical discovery, and could change overnight.
