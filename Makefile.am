DIST_SUBDIRS =	scripts depjars java convjars tests-autotest
#jsonschema2pojo json-java
SUBDIRS =	scripts depjars
if DO_JAVA
if DO_JAVA_JSONSCHEMA2POJO
SUBDIRS +=	jsonschema2pojo
endif
if DO_JSON_JAVA
SUBDIRS +=	json-java
endif
SUBDIRS +=	java convjars
endif
SUBDIRS +=	scripts
SUBDIRS +=	tests-autotest

ACLOCAL_AMFLAGS = -I m4

# if missing mkinstalldirs install-sh trigger errors, ignore the result code on the "system (cp ...)" command in automake
CONFIG_CLEAN_FILES = aclocal.m4 config.guess config.sub missing install-sh Doxyfile @PACKAGE@.spec
CONFIG_STATUS_DEPENDENCIES = gitdescribe
EXTRA_DIST  = @PACKAGE@.spec gitdescribe README.md
EXTRA_DIST += samples/import01.json samples/server44.json samples/simple.devicealias samples/svc-22-01.json



SOURCES = java/Entity.java java/EntityArray.java java/EntityFA.java java/EntityHBA.java java/EntityHost.java java/VWImport.java java/VirtualWisdom4ClientTool.java.in

if DO_DOXYGEN
if DO_FOP
doc: htdocs/docbook/@PACKAGE@.pdf htdocs/index.html
else
doc: htdocs/index.html
endif DO_FOP_FALSE

htdocs/@PACKAGE@.jar: convjars/@PACKAGE@.jar
	ln -f $< $@

htdocs/index.html htdocs/xml/index.xml htdocs/docbook/index.xml: htdocs/@PACKAGE@.jar htdocs/README.dox Doxyfile all-am $(SOURCES)
	$(DOXYGEN)
	if test \! -d $(<D)/.git; then echo "subdir $(<D) not set as a gh-pages branch; see http://rickfoosusa.blogspot.com/2011/10/howto-use-doxygen-with-github.html"; git branch|grep gh-pages || echo git clone --branch gh-pages $$(git config --get remote.origin.url) $(<D); grep $(<D) .gitignore || echo "echo $(<D) > .gitignore"; echo "then delete everything in $(<D), commit, and push"; exit 1; fi
	@grep $(<D) .gitignore || { echo "echo $(<D) > .gitignore"; exit 1; }
	(cd $(<D) && git add -A)
	if test -n "$(DOXYMESSAGE)" ; then (cd $(<D) && git commit -a -m "$(DOXYMESSAGE)" && git push); fi

if DO_FOP
# fop.sh -xsl /docbook-xsl/fo/docbook.xsl -xml myfile.xml -pdf myfile.pdf
# http://www.sagehill.net/docbookxsl/InstallingAnFO.html
htdocs/docbook/@PACKAGE@.fo: htdocs/docbook/index.xml
	(cd $(@D) && @XSLTPROC@  --xinclude --output $(@F) --stringparam fop1.extensions 1  $(abs_top_srcdir)/depjars/docbook-xsl/fo/docbook.xsl  $(<F) )

htdocs/docbook/@PACKAGE@.pdf: htdocs/docbook/@PACKAGE@.fo
if FETCH_FOP
	@JAVA@ -jar $(abs_builddir)/depjars/fop.jar -fo $< -pdf $@
else
	@FOP@ -fo $< -pdf $@
endif FETCH_FOP_FALSE

endif DO_FOP

htdocs/README.dox: README.md Makefile
	if test \! -d $(@D)/.git; then echo "subdir $(@D) not set as a gh-pages branch; see http://rickfoosusa.blogspot.com/2011/10/howto-use-doxygen-with-github.html"; git branch|grep gh-pages || echo git clone --branch gh-pages $$(git config --get remote.origin.url) $(@D); grep $(@D) .gitignore || echo "echo $(@D) > .gitignore"; echo "then delete everything in $(@D), commit, and push"; exit 1; fi
if DO_DOXYGEN_MD
	awk 'BEGIN { print "@file"; shown=0; } /^@PACKAGE@/ && (0 == shown) { print "@mainpage JavaDoc API Markup for @PACKAGE@"; shown++; } { printf "%s\n",$$0; } END {print "" }' $< > $@
else
	awk 'BEGIN { print "/** @file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } /@PACKAGE@$$/ {next} /^=+$$/ {next} { printf " * %s\n",$$0; } END {print " */" }' $< > $@
endif DO_DOXYGEN_MD_FALSE
endif


if DO_MAKE_JSONSCHEMA2POJO
schema.json: @SCHEMASAMPLE@
	@JAVA@ -jar jsonschema2pojo/jsonschema2pojo.jar  --source @SCHEMASAMPLE@ --package org.smallfoot.vw4.pojo --source-type JSON --target schema
endif

if DO_ASTYLE
formatting: $(SOURCES)

if DO_GITFORMATTING
	@git ls-files --other --exclude-standard --directory || { echo "###################### unignored local files: git add, git commit ?"; }
	@git diff --exit-code || { echo "###################### unstaged changes: git commit -a"; exit 1; }
	@git diff --cached --exit-code || { echo "###################### staged uncommitted changes: git commit"; exit 1; }
endif
	$(MAKE) check
	for i in $^; do $(ASTYLE) -A1 $$i; done
	$(MAKE) check
if DO_GITFORMATTING
	git commit -a -m 'formatting and whitespace'
endif DO_GITFORMATTING

endif DO_ASTYLE

# (done by left for doc) in 1.0 use "tag -a 1.0" and remove the --tags below
if DO_GITFORMATTING
gitdescribe: .git/index
	git describe > $@
endif DO_GITFORMATTING

if HAVE_RPMBUILD
srpm: distcheck
	@RPMBUILD@ -ts $(PACKAGE)-$(VERSION).tar.bz2
if DO_SUDOPOSTMOVE
	@test -n "$(DIRTYOK)" || git diff --exit-code || { echo "###################### unstaged changes: git commit -a"; exit 1; }
	@test -n "$(DIRTYOK)" || git diff --cached --exit-code || { echo "###################### staged uncommitted changes: git commit"; exit 1; }
	mv $$(rpm --eval '%{_srcrpmdir}')/@PACKAGE@-$$(rpm --qf '%{VERSION}\n' -q --specfile @PACKAGE@.spec |head -1)-$$(rpm --qf '%{RELEASE}\n' -q --specfile @PACKAGE@.spec |head -1).src.rpm @SUDOPOSTMOVE@
endif   DO_SUDOPOSTMOVE

rpm: dist-bzip2
	@RPMBUILD@ --define 'skipcheck 1' -ta $(PACKAGE)-$(VERSION).tar.bz2
endif HAVE_RPMBUILD

buildcheck: dist-bzip2
	rm -fr tests-autotest/testsuite.dir
	make check || cat tests-autotest/testsuite.dir/*/testsuite.log



