vw4tools
========

Tools and utilities for VirtualWisdom4

At current, VW4Tools is a script or two, plus one big jar file.  vw4tool.jar
allows a user to import a text file in one of several formats and output an
Entity import JSON for VirtualWisdom4.

VW4Tools is intentionally 100% opensource to both show the code a user may be running on a trusted server, and to emphasize that Support is also of the OpenSource variety (ie not by VI Support).  In turn, VW4Tools uses other projects to achieve its goals:

@dot
// If this doesn't show up well, see it at http://chickenandpork.github.io/vw4tools/index.html
digraph Dependencies
{
"vw4tools" [ label="VW4Tools\n(AL-2.0)", URL="https://www.openhub.net/p/\N" ];				// AL-2.0

"jackson" [ label="fasterxml::jackson", URL="http://wiki.fasterxml.com/JacksonHome" ];				// LGPL-2.1, AL-2.0

"fibrechannel-parsers" [ label="\N", URL="http://chickenandpork.github.io/fibrechannel-parsers/index.html" ];	// AL-2.0

"wwndesc" [ label="\N", URL="http://chickenandpork.github.io/wwndesc/index.html" ];				// AL-2.0
{ "jackson" "fibrechannel-parsers" "wwndesc" } -> "vw4tools" [ label="AL-2.0" ];

"java-getopt" [ label="USL/GNU SUN/java-getopt", URL="http://www.gnu.org/software/java-getopt/" ];		// LGPL-2
"java-getopt" -> "vw4tools" [ label="LGPL-2" ];
}
@enddot

If we support the notion of "GNU/Linux" rather than "Linux", then by the same logic, we should refer to java-getopt as "USL/GNU SUN/java-getopt"

Key Resources
=============

1. @ref md_htdocs_OVERVIEW "Usage Overview" can describe general usage
2. @ref md_htdocs_RUNNING "Running Java" can help users who are new to Java understand how to run Java (note: Here There Be Command Terminals)
3. @ref md_htdocs_BUILDING "Building" can elaborate on how the project is built from this sourcecode


Proprietary Intellectual Property
=================================
With the exception of samples/import01.json, all files and content are based on
the same level of access to the product enjoyed by a customer.  Implicitly, no
private information is shared, all of this content (save the one file) is based
on empirical discovery, and could change overnight.
