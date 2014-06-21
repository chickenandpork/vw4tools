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

Overview
========

In general, collection and conversion looks like the following diagram:

@dot
digraph TransformUDC
{
	subgraph cluster_local
	{
		label="local files, or FTP URL, or HTTP URL" ;
		node [ shape=rectangle ]
		file_zoneshow [ label="zoneshow" ]
		file_showzones [ label="showzones" ]
		file_fcalias [ label="fcalias" ]
		file_dad [ label="device-alias database" ]
		file_alishow [ label="alishow" ]
	}
	subgraph cluster_vw4tool
	{
		label="VW4Tool"
		vw4tool [ shape=circle ]
	}
	{ brocade1 brocade2 brocade3 } -> { file_zoneshow file_alishow } -> vw4tool
	{ cisco1 cisco2 cisco3 } -> { file_fcalias file_showzones file_dad} -> vw4tool

	subgraph cluster_scripts
	{
		label="Scripts"
		"csv-to-json.awk" [ shape=rectangle ]
	}

	vw4tool -> "csv-to-json.awk" [ label="OrderedTuples.csv" ]

	"json file" [ shape=rectangle ]
	{ "csv-to-json.awk" vw4tool } -> "json file" -> "VirtualWisdom4 \nEntities Import"
}
@enddot



Import of FTP or HTTP Data
==========================

VW4Tools uses the URLDataSource functionality to open a stream from a HTTP or FTP server; this means that the files it parses can be stored on a local filesystem, a FTP server, and an HTTP server as constant data, or can be the result of a CGI program on an HTTP server.  Less common, a local fil emay also be mounted as a FUSE filesystem, allowing even "local" data to be generated like a CGI program.

In order to draw a text stream from a FTP server or HTTP server, merely use a RFC-1738-compliant URL to indicate these two sources:

    (ftp|http)://{user{:password@@}server{:port}/pathname/to/resource

for example:

(basic user/pass: user = scott, pass = T1ger, host = ftp.example.com, subdir = . , file = nicknames.txt

    java -jar vw4tool.jar --nickname=ftp://scott:T1ger@@ftp.example.com/nicknames.txt

(basic user/pass: user = scott, pass = T1ger, host = ftp.example.com, subdir = /users/local/ScottAdams/ , file = aliases.text

    java -jar vw4tool.jar --nickname=ftp://scott:T1ger@@ftp.example.com/%2fusers/local/ScottAdams/aliases.text

(basic user/pass: user = anonymous, pass = scott@@uberserver.net, host = ftp.example.com, subdir = examples , file = aliases

    java -jar vw4tool.jar -N ftp://anonymous:scott%2Fuberserver.net@@ftp.example.com/examples/aliases

(basic http: host = www.example.com, subdir = . , file = aliases

    java -jar vw4tool.jar --nickname=http://www.example.com/aliases

(basic HTTP GET: host = www.example.com, subdir = . , path=/cgi-bin/fetch.cgi, some parameters

    java -jar vw4tool.jar --nickname=http://www.example.com/cgi-bin/fetch.cgi?now=20140901&realm=WEST&group=production


NOTE: "--nickname=" and "-N" are functionally identical; "--nickname=" may offer a slight beefit in more clearly self-documenting the behavior of the commandline option.



Import of Local Text File
=========================

The most common usage is local text files representing the metadata of the local environment.  This is done by offering a RFC-1738-compliant local file URL:

    file://pathname/to/resource

Note that if the "file://" is not given, and no "protocol" (ie "file://") is given, vw4tool will warn you about this but try the local file:// protocol prefix for you

for example:

(local file in subdir ./files/aliases)

    java -jar vw4tool.jar --nickname=file://files/aliases
    java -jar vw4tool.jar --nickname=files/aliases

(local file in subdir /Full/Path/files/aliases)

    java -jar vw4tool.jar --nickname=file:///Full/Path/files/aliases
    java -jar vw4tool.jar --nickname=/Full/Path/files/aliases



Specifying Formats
==================

The user doesn't need to speficy the format of a file; vw4tool will try the file at the same time across many different parsers and see which one can make sense of it.  Unfortunately, the parsers canot always chew through any user-interface codes (such as "press any key to continue") and preamble (the verbose text trash before the actual zone or aliases or such).  Trimming that to a minimum offers a better chance of parsing the file.

The corollary to this is that if a file doesn't seem to parse, yet it seems like it should, remove anything before or after the actual content, and confirm that it was collected in a non-interactive method.  If the user ever needs to "press any key for more", chances are, the parsed result will be either partial, or none at all.


Multiple Inputs
===============

These input formats can be mixed.  For example

    java -jar vw4tool.jar --nickname=http://www.example.com/cgi-bin/fetch.cgi?now=20140901&realm=WEST&group=production -N local.txt -N file:///Users/allanc/aliases.dad -N morefiles.txt


Outputs
=======

vw4tool will either generate an OrderedTuples.csv file (if that specific filename is given on the "-o" option) or a json file (any other putput filename).  Or both:

    java -jar vw4tool.jar  -N local.txt -oOrderedTuples.csv -oexample.json

NOTE: giving no filename sends the output to stdout:

    java -jar vw4tool.jar  -N local.txt -o

For this reason, the following two commands do different things:

    java -jar vw4tool.jar  -N local.txt -oexample.json
    java -jar vw4tool.jar  -N local.txt -o example.json

Stdout can be represented as "-"; the following two commands do the same thing:

    java -jar vw4tool.jar  -N local.txt -o
    java -jar vw4tool.jar  -N local.txt -o-

This behavior may disappear in future.  Please try to use "-" to print the result to stdout:

    java -jar vw4tool.jar  -N local.txt -N (another) --nickname=(another) -o-



Proprietary Intellectual Property
=================================
With the exception of samples/import01.json, all files and content are based on
the same level of access to the product enjoyed by a customer.  Implicitly, no
private information is shared, all of this content (save the one file) is based
on empirical discovery, and could change overnight.
