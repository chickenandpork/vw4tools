depjardir = @datadir@/@PACKAGE@/jars
dist_depjar_DATA = $(JARS)

JARS := jsonschema2pojo.jar

if DO_JAVA_JSONSCHEMA2POJO

JSONSCHEMA_JARS = jsonschema2pojo-cli-0.4.2.jar jsonschema2pojo-core-0.4.2.jar jackson-databind-2.2.0.jar jackson-annotations-2.2.0.jar jackson-core-2.2.0.jar codemodel-2.6.jar gson-2.2.4.jar validation-api-1.0.0.GA.jar joda-time-2.2.jar jackson-mapper-asl-1.9.11.jar jackson-core-asl-1.9.11.jar commons-lang3-3.2.1.jar jcommander-1.30.jar commons-lang-2.6.jar annotations-1.3.9.jar


jsonschema2pojo-cli-0.4.2.jar: jsonschema2pojo-0.4.2.tar.gz
	tar xOf $< '*/$@' > $@

jcommander-1.30.jar jackson-annotations-2.2.0.jar commons-lang3-3.2.1.jar codemodel-2.6.jar jackson-core-2.2.0.jar jackson-mapper-asl-1.9.11.jar jackson-core-asl-1.9.11.jar jsonschema2pojo-core-0.4.2.jar validation-api-1.0.0.GA.jar commons-lang-2.6.jar joda-time-2.2.jar annotations-1.3.9.jar jackson-databind-2.2.0.jar gson-2.2.4.jar : jsonschema2pojo-0.4.2.tar.gz
	tar xOf $< '*/lib/$@' > $@


jsonschema2pojo-0.4.2.tar.gz:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	tar tzf $@ || rm -f $@ $${TMPDIR-/tmp}/$(@F)
	test -f $@ || wget -O $@ https://github.com/joelittlejohn/jsonschema2pojo/releases/download/jsonschema2pojo-0.4.2/jsonschema2pojo-0.4.2.tar.gz || curl -Lo $@ https://github.com/joelittlejohn/jsonschema2pojo/releases/download/jsonschema2pojo-0.4.2/jsonschema2pojo-0.4.2.tar.gz
	tar tzf $@ || rm -f $@
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@
endif


EXTRA_DIST = $(dist_depjar_DATA)
CONFIG_CLEAN_FILES = $(JARS)
BUILT_SOURCES := $(JARS)

check-local: $(JARS)
	for i in $^; do jar tf $$i >/dev/null || exit 1; done
	-@JAVA@ -jar jsonschema2pojo.jar --help

jsonschema2pojo.jar: $(JSONSCHEMA_JARS)
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	@echo "Main-Class: org.jsonschema2pojo.cli.Jsonschema2Pojo" >> $(@D)/.workdir/MANIFEST.MF
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
	find $(@D)/.workdir '(' -name \*.java -o -name COPYING.LIB -o -name \*.html -o -name README -o -name Makefile -o -name ChangeLog -o -name GetoptDemo.class -o -name LANGUAGES -o -name \*.xml ')' -exec rm {} \;
	(cd $(@D)/.workdir && jar cm MANIFEST.MF com edu javax net org ) > $@
	echo rm -fr $(@D)/.workdir
	touch $@


