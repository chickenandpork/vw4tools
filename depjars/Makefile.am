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
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/annotations/jackson-annotations/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/annotations/jackson-annotations/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

jackson-core-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

jackson-databind-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/databind/jackson-databind/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/databind/jackson-databind/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

jackson-module-jsonSchema-2.3.1.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/module/jackson-module-jsonSchema/2.3.1/$(@F) || curl -Lo $@ http://repo1.maven.org/maven2/com/fasterxml/jackson/module/jackson-module-jsonSchema/2.3.1/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
endif


EXTRA_DIST = $(nodist_pkgdata_JAVA)
CONFIG_CLEAN_FILES = $(JARS)
BUILT_SOURCES := $(JARS)

java-getopt-1.0.13.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://download.java.net/maven/2/gnu/getopt/java-getopt/1.0.13/$(@F) || curl -Lo $@ http://download.java.net/maven/2/gnu/getopt/java-getopt/1.0.13/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)

# catch that case of downloading content, the website claims it's sending content, but it's really sending a "hi, the content is over here, behind this ad..." webpage

check-local: $(JARS)
	for i in $^; do jar tf $$i >/dev/null || exit 1; done

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

