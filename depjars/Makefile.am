depjardir = @datadir@/@PACKAGE@/jars
dist_depjar_DATA = $(JARS)

JARS := java-getopt-1.0.13.jar json.jar

if DO_JSON_JACKSON
dnl http://repo1.maven.org/maven2/com/fasterxml/jackson/
JSON_JARS = jackson-core-2.3.1.jar jackson-databind-2.3.1.jar jackson-annotations-2.3.1.jar jackson-module-jsonSchema-2.3.1.jar
dnl http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.3.1/jackson-core-2.3.1.jar
dnl http://repo1.maven.org/maven2/com/fasterxml/jackson/module/jackson-module-jsonSchema/2.3.1/jackson-module-jsonSchema-2.3.1.jar

jackson-annotations-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

jackson-core-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

jackson-databind-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

jackson-module-jsonSchema-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/module/jackson-module-jsonSchema/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/module/jackson-module-jsonSchema/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
endif

if FETCH_FOP
FOPJARS = avalon-framework-4.2.0.jar batik-all-1.7.jar commons-io-1.3.1.jar commons-logging-1.0.4.jar serializer-2.7.0.jar 
FOPJARS += xalan-2.7.0.jar xercesImpl-2.7.1.jar xml-apis-1.3.04.jar xml-apis-ext-1.3.04.jar xmlgraphics-commons-1.5.jar

$(FOPJARS): fop-1.1-bin.tar.gz
	tar xOf $< '*/$@' > $@
	if jar tf $@ >/dev/null ; then true; else rm $@; false; fi

fop-1.1.jar: fop-1.1-bin.tar.gz
	tar xOf $< '*/build/fop.jar' > $@
	if jar tf $@ >/dev/null ; then true; else rm $@; false; fi


#http://apache.mirrors.tds.net/xmlgraphics/fop/binaries/fop-1.1-bin.tar.gz
fop-1.1-bin.tar.gz:
	test \! -f $${TMPDIR-/tmp}/$(@F) || tar tzf $${TMPDIR-/tmp}/$(@F) >/dev/null || rm $${TMPDIR-/tmp}/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://apache.mirrors.tds.net/xmlgraphics/fop/binaries/$(@F)
	test \! -f $@ || tar tzf $@ >/dev/null || rm $@
	test -f $@ || curl -Lo $@ http://apache.mirrors.tds.net/xmlgraphics/fop/binaries/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@

fop.jar: $(FOPJARS) fop-1.1.jar
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
	cp $(@D)/.workdir/META-INF/MANIFEST.MF $(@D)/.workdir/MANIFEST.MF
	(cd $(@D)/.workdir && jar cm MANIFEST.MF META-INF org javax java_cup) > $@
	rm -fr $(@D)/.workdir
	touch $@
endif FETCH_FOP

if FETCH_DOCBOOK

# http://hivelocity.dl.sourceforge.net/project/docbook/docbook-xsl/1.78.1/docbook-xsl-1.78.1.tar.bz2
docbook-xsl-1.78.1.tar.bz2:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	tar tjf $@ >/dev/null 2>&1 || rm -f $${TMPDIR-/tmp}/$(@F) $@
	test -f $@ || wget -O $@ http://hivelocity.dl.sourceforge.net/project/docbook/docbook-xsl/1.78.1/$(@F) || curl -Lo $@ http://hivelocity.dl.sourceforge.net/project/docbook/docbook-xsl/1.78.1/$(@F)
	tar tjf $@ >/dev/null 2>&1 || rm -f $@
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@

$(DOCBOOKDIR)/fo/docbook.xsl: docbook-xsl-1.78.1.tar.bz2
	-@rmdir $(srcdir)/.workdir
	mkdir -p $(srcdir)/.workdir
	tar -C $(srcdir)/.workdir -xjf $<
	-rmdir $$(dirname $(@D))
	BUGS=$$(find $(srcdir)/.workdir -name BUGS); case "x$${BUGS}" in "x") ;; *) mv $$(dirname $$(dirname "$${BUGS}"))/* $$(dirname $(@D));; esac
	touch $@
	
	
endif FETCH_DOCBOOK


EXTRA_DIST = $(nodist_pkgdata_JAVA)
CONFIG_CLEAN_FILES = $(JARS)
BUILT_SOURCES := $(JARS)
if FETCH_FOP
BUILT_SOURCES += $(DOCBOOKDIR)/fo/docbook.xsl fop.jar
endif FETCH_FOP

if FETCH_GETOPTJAR
JARS += getopt.jar

# getopt.jar includes both org.smallfoot.getopt and gnu.getopt

getopt.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://chickenandpork.github.io/wwndesc/$(@F) || curl -Lo $@ http://chickenandpork.github.io/wwndesc/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
endif

if FETCH_WWNDESC
JARS += wwndesc.jar

# getopt.jar includes both org.smallfoot.getopt and gnu.getopt

wwndesc.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://chickenandpork.github.io/wwndesc/$(@F) || curl -Lo $@ http://chickenandpork.github.io/wwndesc/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
endif

if FETCH_FCPARSERS
JARS += fcparsers.jar

# getopt.jar includes both org.smallfoot.getopt and gnu.getopt

fcparsers.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://chickenandpork.github.io/fibrechannel-parsers/$(@F) || curl -Lo $@ http://chickenandpork.github.io/fibrechannel-parsers/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
endif


# catch that case of downloading content, the website claims it's sending content, but it's really sending a "hi, the content is over here, behind this ad..." webpage

check-local: $(JARS)
	for i in $^; do jar tf $$i >/dev/null || { echo "fail: $$i"; exit 1; }; done

clean-local:
	rm -f $(JSON_JARS)

json.jar: $(JSON_JARS)
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
	find $(@D)/.workdir '(' -name \*.java -o -name COPYING.LIB -o -name \*.html -o -name README -o -name Makefile -o -name ChangeLog -o -name GetoptDemo.class -o -name LANGUAGES -o -name \*.xml ')' -exec rm {} \;
	(cd $(@D)/.workdir && jar cm MANIFEST.MF com ) > $@
	rm -fr $(@D)/.workdir
	touch $@

