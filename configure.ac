dnl ***********************************************************************
dnl Process this file with autoconf to produce a configure script.
dnl ***********************************************************************
#AC_INIT(install-sh)
AC_INIT(vw4tools, 1.0)
AC_CONFIG_MACRO_DIR([m4])

AC_CANONICAL_HOST

AM_INIT_AUTOMAKE([foreign dist-bzip2])
AC_CONFIG_TESTDIR([tests-autotest])

dnl ***********************************************************************
dnl Checks for programs used during the build
dnl ***********************************************************************
dnl AC_PROG_CC
dnl AC_PROG_CXX
AC_PROG_INSTALL
AC_PROG_MAKE_SET

AC_SUBST(AUTOM4TE, autom4te)

SVN_CHECK_JDK([1.6],[1.6])
AM_CONDITIONAL(DO_JAVA, test "$JAVAC" != none)
AC_CHECK_PROGS(JARSIGNER, [jarsigner], [missing])

AC_PATH_PROGS(BYACC, [yacc.macosx byaccj], [missing byacc], $PATH:$HOME/bin)
if test "x$BYACC" = "xmissing byacc"; then echo "BYACC available at http://byaccj.sourceforge.net/#download"; fi
AM_CONDITIONAL(DO_BYACC, true)
AM_CONDITIONAL(DO_CHECKLOCAL_PARSER, false)
dnl AM_CONDITIONAL(DO_BYACC, test "$BYACC" != "missing byacc")

dnl ***********************************************************************
dnl Checks for programs used during test, markup, diagrams. etc.  I'm a huge fan
dnl of automation, and if I can type a single command to make, check, package,
dnl and push a new release, HECK YEAH!
dnl 
dnl This was based on my work for the fctools project (nee vitools) so some may
dnl prune out in the near future
dnl ***********************************************************************
AC_PATH_PROGS(PATHAWK, [awk gawk nawk], [/bin/awk])
AC_PATH_PROGS(XMLLINT, [xmllint], [missing xmllint])
AC_ARG_WITH(xmllintcommentindent,
[AS_HELP_STRING([--with-xmllintcommentindent],[Set XML-comment indentation fudge such as xmllint-2.6.x/2.7.x differences; default=detect])],
[
    case "$withval" in
	"yes") WITH_XMLLINTINDENT="  " ;;
	"no")  WITH_XMLLINTINDENT=""  ;;
	*) case $(xmllint --version 2>&1 | awk '/version/ {print $NF}') in
	    206*) WITH_XMLLINTINDENT="" ;;
	    *) WITH_XMLLINTINDENT="  "  ;;
	    esac ;;
    esac
],
[
    case $(xmllint --version 2>&1 | awk '/version/ {print $NF}') in
	206*) WITH_XMLLINTINDENT="" ;;
	*) WITH_XMLLINTINDENT="  "  ;;
    esac
])
AC_SUBST([XMLLINTINDENT], ${WITH_XMLLINTINDENT})

AC_PATH_PROGS(XSLTPROC, [xsltproc], [missing xsltproc])
AC_PATH_PROGS(TIDY, [tidy], [missing tidy])
AC_PATH_PROGS(BZCAT, [bzcat], [missing bzcat])
AC_PATH_PROGS(SEVENZIP, [7za 7z keka7z], [missing 7za], $PATH:/Applications/Keka.app/Contents/Resources)
AC_PATH_PROGS(FILE, [file], [missing file])

JSON_JAVA=no ;
JSON_JACKSON=yes ;
AM_CONDITIONAL(DO_JSON_JACKSON, test "x$JSON_JACKSON" = "xyes")
AM_CONDITIONAL(DO_JSON_JAVA, test "x$JSON_JAVA" = "xyes")

AC_ARG_WITH(doxygen,
[AS_HELP_STRING([--with-doxygen],[Indicate Doxygen allowing a without-; (default=search)])],
[
    case "$withval" in
	""|"yes") 
		AC_PATH_PROGS(DOXYGEN, [doxygen], missing, $PATH:/Applications/Doxygen.app/Contents/Resources)
		AC_PATH_PROGS(FOP, [xmlgraphics-fop fop], missing)
		;;
	"no")  DOXYGEN=missing ;;
	*) DOXYGEN=$withval ;;
    esac

    case "$DOXYGEN" in
	"missing") HAVE_DOXYGEN_MARKDOWN=no ;;
	*)
	    case `$DOXYGEN --version` in
		1.[0-7]* ) HAVE_DOXYGEN_MARKDOWN=no;;
		*) HAVE_DOXYGEN_MARKDOWN=yes;;
	    esac ;;
    esac
],
[
	DOXYGEN=missing 
	HAVE_DOXYGEN_MARKDOWN=no
])
AC_SUBST(HAVE_DOXYGEN_MARKDOWN)
AM_CONDITIONAL(DO_DOXYGEN_MD, test "x$HAVE_DOXYGEN_MARKDOWN" = "xyes")

AC_ARG_WITH(docbook,
[AS_HELP_STRING([--with-docbook],[Indicate where Docbook is or should be downloaded; (default=depjars/docbook-xsl)])],
[
    case "$withval" in
	""|"yes") 
		DOCBOOKDIR=`pwd`/depjars/docbook-xsl ;;
	"no")  DOCBOOKDIR=no ;;
	*) DOCBOOKDIR=$withval ;;
    esac
],
[
	DOCBOOKDIR=`pwd`/depjars/docbook-xsl
])
AC_SUBST(DOCBOOKDIR)
AM_CONDITIONAL(FETCH_DOCBOOK, test "x$DOCBOOKDIR" != "xno" -a ! -f ${DOCBOOKDIR}/fo/docbook.xsl)

AC_PATH_PROGS(DOT, [dot], missing, $PATH:/usr/local/bin:/usr/local/graph*/bin)
AC_SUBST(PATHDOT, [`dirname "${DOT}"`])
AM_CONDITIONAL(DO_DOT, test "x$DOT" != "xmissing")
AM_CONDITIONAL(DO_DOXYGEN, test "x$DOXYGEN" != "xmissing" -a "x$DOT" != "xmissing")
if test "x$DOT" != "xmissing"; then HAVE_DOT=yes; else HAVE_DOT=no; fi
AC_SUBST(HAVE_DOT)
AM_CONDITIONAL(DO_FOP, test "x$DOXYGEN" != "xmissing" -a "x$DOT" != "xmissing" -a "x$XSLTPROC" != "xmissing" )
AM_CONDITIONAL(FETCH_FOP, test "x$DOXYGEN" != "xmissing" -a "x$DOT" != "xmissing" -a "x$XSLTPROC" != "xmissing" -a "x$FOP" = "xmissing" )
AC_PATH_PROGS(ASTYLE, [astyle], missing, $PATH:$HOME/bin)
AM_CONDITIONAL(DO_ASTYLE, test "x$ASTYLE" != "xmissing")
AC_PATH_PROGS(RPMBUILD, [rpmbuild], [missing rpmbuild])
AM_CONDITIONAL(HAVE_RPMBUILD, [test "x$RPMBUILD" != "xmissing rpmbuild"])


AC_PATH_PROGS(GIT, [git], [missing])
AC_MSG_CHECKING([version agreement with git tag])
BUILDID=$( (git describe ; cat gitdescribe) 2>/dev/null |head -1| cut -d "-" -f 2)
CHKVER=$( (git describe ; cat gitdescribe) 2>/dev/null |head -1| cut -d "-" -f 1)
if test "${CHKVER}" = "${VERSION}"
then 
  AC_MSG_RESULT([ok])
else
  AC_MSG_ERROR([version mismatch: configure.ac needs version ${CHKVER} not ${VERSION}])
fi
AC_SUBST(BUILDID)


AC_CHECK_PROG(SVN, [svn], [svn],[missing svn])
AM_CONDITIONAL(DO_SVNLOG, test "$SVN" != "missing svn" -a "$XSLTPROC" != "missing xsltproc" -a -f .svn/entries -a -f /usr/share/cnp/svnlog2atom.xsl)
AM_CONDITIONAL(DO_GITFORMATTING, test "$GIT" != "missing git" -a -f .git/config)
AC_PATH_PROGS(ECHO, [echo], [missing])
AC_PATH_PROGS(NC, [nc], [missing])
AC_PATH_PROGS(SED, [sed], [missing])
AC_PATH_PROGS(BASE64, [base64], [missing])
AM_CONDITIONAL(HAVE_BASE64, [test "x$BASE64" != "xmissing"])
AC_PATH_PROGS(N2A, [native2ascii])

AC_SUBST(yumrepos, [$sysconfdir/yum.repos.d])
AC_SUBST(emaildest, [ftpupdates])

AC_ARG_WITH(distname,
[AS_HELP_STRING([--with-distname],[Define a repository name; default=guess])],
[
    DISTNAME=$withval
],
[
    DISTNAME=$(cat /etc/redhat-release /dev/null 2>/dev/null | awk 'BEGIN {REL=""} /^CentOS release/ { REL=$1$3} END { if ("" == REL) print "unknown"; else print REL} ')
])

AC_SUBST(localreposversion, ${DISTNAME})

AC_SUBST(httpconfd, [/etc/httpd/conf.d])
AC_SUBST(localreposroot, [/var/ftp])

AC_SUBST(defaultcompattarget, [4.0.2])

AC_ARG_ENABLE(autotest,
[AS_HELP_STRING([--enable-autotest],[Enable autotest testcases; breaks: make distcheck; default=disabled])],
[
    case "$enableval" in
	"yes") ENAB=yes ;;
	"no")  ENAB=no  ;;
	*) ENAB=no ;;
    esac
],
[
    ENAB=no
])
AM_CONDITIONAL(DO_AUTOTEST, test "$ENAB" = yes)


SCHEMASAMPLE=
AC_ARG_ENABLE(jsonschema2pojo,
[AS_HELP_STRING([--enable-jsonschema2pojo],[Enable jsonschema2pojo run to create POJO classes; default=disabled])],
[
    case "$enableval" in
	"yes") ENABJS2P=yes ;;
	"no")  ENABJS2P=no  ;;
	*) ENABJS2P=yes ; SCHEMASAMPLE="$enableval";;
    esac
],
[
    ENABJS2P=no
])
AM_CONDITIONAL(DO_JAVA_JSONSCHEMA2POJO, test "$ENABJS2P" = yes)
AM_CONDITIONAL(DO_MAKE_JSONSCHEMA2POJO, test "x$SCHEMASAMPLE" != x)
AC_SUBST([SCHEMASAMPLE])


AC_ARG_WITH(passwordfile,
[AS_HELP_STRING([--with-passwordfile],[external-to-codebase password file avoiding checking in passwords; default=disabled])],
[
    case "$withval" in
	"no")
		PW_VISVC="tiger";
		URL_VIRTUOSO4="http://fcfae.com/";
		;;
	*)
		PW_VISVC=`awk -F, '($1 == "visvc") {print $2}' ${withval}`
		URL_VIRTUOSO4=`awk -F, '($1 == "urlVirtuoso4") {print $2}' ${withval}`
		;;
    esac
],
[
	PW_VISVC="tiger";
	URL_VIRTUOSO4="http://fcfae.com/";
])
AC_SUBST([URL_VIRTUOSO4],${URL_VIRTUOSO4})
AC_SUBST([PW_VISVC],${PW_VISVC})


AC_ARG_WITH(java-keystore,
[AS_HELP_STRING([--with-java-keystore],[Enable signing of jars (jarsigner -keystore [[keystore]] [[jar]] vitools), by giving a java keystore; (default=disabled, yes=use ~/.keystore)])],
[
    case "$withval" in
        "yes") WITHJAVAKEYSTORE=${HOME}/.keystore ;;
        "no")  WITHJAVAKEYSTORE=no ;;
        *) WITHJAVAKEYSTORE=$withval ;;
    esac
],
[
    WITHJAVAKEYSTORE=no
])
AM_CONDITIONAL(DO_JARSIGNER, test "$WITHJAVAKEYSTORE" != no -a "x$JARSIGNER" != xmissing)
AC_SUBST([WITHJAVAKEYSTORE], ${WITHJAVAKEYSTORE})


AC_ARG_WITH(java-keystorepass,
[AS_HELP_STRING([--with-java-keystorepass],[Automate signing of jars (jarsigner -keystore [[keystore]] [[jar]] vitools), by giving a java keystorepass; (default=disabled, yes= no password)])],
[
    case "$withval" in
        "yes") WITHJAVAKEYSTOREPASS=- ;;
        "no")  WITHJAVAKEYSTOREPASS= ;;
        *) WITHJAVAKEYSTOREPASS=$withval ;;
    esac
],
[
    WITHJAVAKEYSTOREPASS=
])
AC_SUBST([JAVAKEYSTOREPASSSTRING], ["-storepass ${WITHJAVAKEYSTOREPASS}"])


AC_ARG_WITH(getoptplus,
[AS_HELP_STRING([--with-getoptplus],[Enable inclusion of an improved getopt .jar; default=search])],
[
    case "$withval" in
	"yes") WITH_GETOPTJAR=$((locate getopt.jar) | grep -v .workdir | head -1) ;;
	"no")  WITH_GETOPTJAR=  ;;
	*) WITH_GETOPTJAR="$withval" ;;
    esac
],
[
AC_MSG_CHECKING([for getopt.jar])
    WITH_GETOPTJAR=$((locate getopt.jar) | grep -v .workdir | head -1) ;
AC_MSG_RESULT([${WITH_GETOPTJAR}])
])
AM_CONDITIONAL(DO_GETOPTJAR, test "x$WITH_GETOPTJAR" != x)
AC_SUBST([GETOPTJAR], ${WITH_GETOPTJAR})


AC_ARG_WITH(fcparsers,
[AS_HELP_STRING([--with-fcparsers],[Enable autodecompression of a built fcparsers.jar; default=search])],
[
    case "$withval" in
        "yes") WITH_FCPARSERS=$((rpm -ql fcparsers-java 2>/dev/null|grep -v 'is not installed' ; locate fcparsers.jar) | grep -v .workdir| head -1) ;;
        "no")  WITH_FCPARSERS=no  ;;
        *) WITH_FCPARSERS="$withval" ;;
    esac
],
[
AC_MSG_CHECKING([for fcparsers.jar])
    WITH_FCPARSERS=$((rpm -ql fcparsers-java 2>/dev/null|grep -v 'is not installed'; locate fcparsers.jar) | grep -v .workdir| head -1) ;
AC_MSG_RESULT([${WITH_FCPARSERS}])
])
AM_CONDITIONAL(DO_FCPARSERSJAR, test "$WITH_FCPARSERS" != no)
AC_SUBST([FCPARSERSJAR], ${WITH_FCPARSERS})
AC_MSG_CHECKING([fcparsers.jar version])
VER_FCPARSERS=$(java -jar ${FCPARSERSJAR} --version)
AC_MSG_RESULT([${VER_FCPARSERS}])
AC_SUBST([VER_FCPARSERS], ${VER_FCPARSERS})


AC_ARG_WITH(wwndesc,
[AS_HELP_STRING([--with-wwndesc],[Enable autodecompression of a built wwndesc.jar; default=search])],
[
    case "$withval" in
        "yes") WITH_WWNDESC=$((rpm -ql wwndesc-java 2>/dev/null|grep -v 'is not installed' ; locate wwndesc.jar) | grep -v .workdir| head -1) ;;
        "no")  WITH_WWNDESC=no  ;;
        *) WITH_WWNDESC="$withval" ;;
    esac
],
[
AC_MSG_CHECKING([for wwndesc.jar])
    WITH_WWNDESC=$((rpm -ql wwndesc-java 2>/dev/null|grep -v 'is not installed'; locate wwndesc.jar) | grep -v .workdir| head -1) ;
AC_MSG_RESULT([${WITH_WWNDESC}])
])
AM_CONDITIONAL(DO_WWNDESCJAR, test "$WITH_WWNDESC" != no)
AC_SUBST([WWNDESCJAR], ${WITH_WWNDESC})
AC_MSG_CHECKING([wwndesc.jar version])
VER_WWNDESC=$(java -jar ${WWNDESCJAR} --version)
AC_MSG_RESULT([${VER_WWNDESC}])
AC_SUBST([VER_FCPARSERS], ${VER_FCPARSERS})


AC_MSG_CHECKING([for user-defined specific testcase])
AC_ARG_WITH(usertestcase,
[AS_HELP_STRING([--with-usertestcase],[Enable a one-off test using a user-defined testcase; default=ignore])],
[
    USERTESTCASE="$withval"
    AC_MSG_RESULT([${USERTESTCASE}])
],
[
    USERTESTCASE="missing"
    AC_MSG_RESULT([not given])
])
AC_SUBST([USERTESTCASE], ${USERTESTCASE})



AC_ARG_ENABLE(vertimeoutwarning,
[AS_HELP_STRING([--disable-vertimeoutwarning],[Disable DNS Version Timeout warning; default=enabled])],
[
    NOVERTIMEOUTWARNING=yes
    NRPE_SERVER=127.0.0.1
],
[
    NOVERTIMEOUTWARNING=no
    NRPE_SERVER=$(host smallfoot.org|awk 'BEGIN {res=""} /has address/ { res=$NF; } END {print res}')
])
AC_SUBST([NOVERTIMEOUTWARNING], ${NOVERTIMEOUTWARNING})



AC_ARG_WITH(sudopostmove,
[AS_HELP_STRING([--with-sudopostmove],[Enable sudopostmove of a built RPM; default=disabled])],
[
    case "$withval" in
        "no")  WITH_SUDOPOSTMOVE=no  ;;
        *) WITH_SUDOPOSTMOVE="$withval" ;;
    esac
],
[
    WITH_SUDOPOSTMOVE=no
])
AM_CONDITIONAL(DO_SUDOPOSTMOVE, test "$WITH_SUDOPOSTMOVE" != no)
AC_SUBST([SUDOPOSTMOVE], ${WITH_SUDOPOSTMOVE})

AC_ARG_WITH(ftppostmove,
[AS_HELP_STRING([--with-ftppostmove],[Enable ftpbased post-build move of a convenience jarfile; default=disabled])],
[
    case "$withval" in
        "no")  WITH_FTPPOSTMOVE=no  ;;
        *) WITH_FTPPOSTMOVE="$withval";
	    FCTRANSFERJAR=$((rpm -ql fctransfer-java 2>/dev/null|grep -v 'is not installed' ; locate fctransfer.jar) | grep -v .workdir| head -1)
	    VER_FCTRANSFERJAR=$(java -jar ${FCTRANSFERJAR} -V) || VER_FCTRANSFERJAR=""
		;;
    esac
],
[
    WITH_FTPPOSTMOVE=no
])
AM_CONDITIONAL(DO_FTPPOSTMOVE, test "$WITH_FTPPOSTMOVE" != no -a -n "${VER_FCTRANSFERJAR}" )
AC_SUBST([FTPPOSTMOVE], ${WITH_FTPPOSTMOVE})
AC_SUBST([FCTRANSFERJAR], ${FCTRANSFERJAR})



AC_CONFIG_FILES(Makefile
  convjars/Makefile 
  depjars/Makefile 
  java/Makefile java/VirtualWisdom4ClientTool.java
  scripts/Makefile scripts/csv-to-json.awk
  tests-autotest/Makefile tests-autotest/testsuite.at
)

if test "x$RPMBUILD" != "xmissing rpmbuild"
then
  AC_CONFIG_FILES(vw4tools.spec:pkg/rpm/vw4tools.spec.in)
fi

if test "x$DOXYGEN" != "xmissing"
then
  AC_CONFIG_FILES(Doxyfile:pkg/Doxyfile.in)
fi

if test "x$ENABJS2P" = "xyes"
then
  AC_CONFIG_FILES(jsonschema2pojo/Makefile)
fi

AC_OUTPUT
