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
EXTRA_DIST = @PACKAGE@.spec gitdescribe



#dwd.zip: Doxyfile
#	$(DOXYGEN)
#	rm -f $@
#	for i in $$(basename -s .awk $$(grep -l @page scripts/*.awk) ; basename -s .xsl $$(grep -l @page scripts/*.xsl)) ; do ls htdocs/html/$$i.html; done | zip $@ -@
#	unzip -t $@ >/dev/null
#endif

if DO_DOXYGEN
doc: htdocs/README.dox Doxyfile
	$(DOXYGEN)
	if test \! -d $(<D)/.git; then echo "subdir $(<D) not set as a gh-pages branch; see http://rickfoosusa.blogspot.com/2011/10/howto-use-doxygen-with-github.html"; git branch|grep gh-pages || echo git clone --branch gh-pages $$(git config --get remote.origin.url) $(<D); grep $(<D) .gitignore || echo "echo $(<D) > .gitignore"; echo "then delete everything in $(<D), commit, and push"; exit 1; fi
	@grep $(<D) .gitignore || { echo "echo $(<D) > .gitignore"; exit 1; }
	(cd $(<D) && git add -A)
	if test -n "$(DOXYMESSAGE)" ; then (cd $(<D) && git commit -a -m "$(DOXYMESSAGE)" && git push); fi

htdocs/README.dox: README.md Makefile
	if test \! -d $(@D)/.git; then echo "subdir $(@D) not set as a gh-pages branch; see http://rickfoosusa.blogspot.com/2011/10/howto-use-doxygen-with-github.html"; git branch|grep gh-pages || echo git clone --branch gh-pages $$(git config --get remote.origin.url) $(@D); grep $(@D) .gitignore || echo "echo $(@D) > .gitignore"; echo "then delete everything in $(@D), commit, and push"; exit 1; fi
if DO_DOXYGEN_MD
	awk 'BEGIN { print "@file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } { printf "%s\n",$$0; } END {print "" }' $< > $@
else
	awk 'BEGIN { print "/** @file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } /@PACKAGE@$$/ {next} /^=+$$/ {next} { printf " * %s\n",$$0; } END {print " */" }' $< > $@
endif DO_DOXYGEN_MD_FALSE
endif


if DO_MAKE_JSONSCHEMA2POJO
schema.json: @SCHEMASAMPLE@
	@JAVA@ -jar jsonschema2pojo/jsonschema2pojo.jar  --source @SCHEMASAMPLE@ --package org.smallfoot.vw4.pojo --source-type JSON --target schema
endif


if DO_ASTYLE
formatting: java/VirtualWisdom4ClientTool.java.in
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

# in 1.0 use "tag -a 1.0" and remove the --tags below
if DO_GITFORMATTING
gitdescribe: .git/index
	git describe --tags > $@
endif DO_GITFORMATTING

if HAVE_RPMBUILD
rpm: dist-bzip2
	@RPMBUILD@ --define 'skipcheck 1' -ta $(PACKAGE)-$(VERSION).tar.bz2
endif