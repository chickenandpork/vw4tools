vw4tools		{#mainpage}
========

Tools and utilities for VirtualWisdom4

At current, VW4Tools is a script or two, plus one big jar file.  vw4tool.jar
allows a user to import a text file in one of several formats and output an
Entity import JSON for VirtualWisdom4.

VW4Tools is intentionally 100% opensource to both show the code a user may be running on a trusted server, and to emphasize that Support is also of the OpenSource variety (ie not by VI Support).

@dot
digraph Dependencies
{
"fibrechannel-parsers" [ label="\N", URL="http://chickenandpork.github.io/fibrechannel-parsers/index.html" ];

"fasterxml::jackson" -> "vw4tools"
"fibrechannel-parsers" -> "vw4tools"
"java-getopt" -> "vw4tools"
"wwndesc" -> "vw4tools"
}
@enddot



Proprietary Intellectual Property
=================================
With the exception of samples/import01.json, all files and content are based on
the same level of access to the product enjoyed by a customer.  Implicitly, no
private information is shared, all of this content (save the one file) is based
on empirical discovery, and could change overnight.
