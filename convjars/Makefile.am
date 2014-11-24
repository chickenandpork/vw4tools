nodist_pkgdata_DATA = @PACKAGE@.jar

CONFIG_CLEAN_FILES = @PACKAGE@.jar convjars.jar

BUILDTMP=$(shell date +%s)

JAVAROOT = bld


$(subdir).jar: ../java/@PACKAGE@.jar $(top_builddir)/depjars/json.jar @FCPARSERSJAR@ @WWNDESCJAR@ @GETOPTJAR@
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.vw4.VirtualWisdom4ClientTool"  >> $(@D)/.workdir/MANIFEST.MF
	for i in $^ ; do ABS=`readlink -f $$i`; (cd $(@D)/.workdir  && jar xf $${ABS}) ; done
	(cd $(@D)/.workdir && find * -name Driver.class -print) | sed -e 's@/@.@g' -e 's/.class$$//g' > $(@D)/.workdir/META-INF/services/java.sql.Driver
	find $(@D)/.workdir '(' -name \*.java -o -name COPYING.LIB -o -name \*.html -o -name README -o -name Makefile -o -name ChangeLog -o -name GetoptDemo.class -o -name LANGUAGES -o -name \*.xml ')' -exec rm {} \;
	(cd $(@D)/.workdir && jar cm MANIFEST.MF META-INF/services/java.sql.Driver com gnu net* org*) > $@
	rm -fr $(@D)/.workdir
	touch $@

if DO_JARSIGNER
@PACKAGE@.jar: $(subdir).jar @WITHJAVAKEYSTORE@
	@echo "jar-signing $@ using vitools key"
	@cat /dev/null | @JARSIGNER@ -keystore @WITHJAVAKEYSTORE@ @JAVAKEYSTOREPASSSTRING@ -signedjar $@ $< vitools
#@PACKAGE@

else
@PACKAGE@.jar: $(subdir).jar
	cp $< $@
endif


