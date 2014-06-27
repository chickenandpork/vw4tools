nodist_java_JAVA = VirtualWisdom4ClientTool.java VWImport.java Entity.java EntityHost.java EntityHBA.java EntityArray.java EntityFA.java
nodist_java_DATA = $(subdir).jar
javadir = $(pkgdir)
faejavadir = @faehome@/@PACKAGE@
faejava_DATA = $(subdir).jar

EXTRA_DIST = $(nodist_java_JAVA) VirtualWisdom4ClientTool.java.in
CONFIG_CLEAN_FILES = $(subdir).jar

JAVAROOT = bld
BUILT_SOURCES = javaroot.stamp

javaroot.stamp: $(nodist_java_JAVA)
	mkdir -p $(JAVAROOT) && touch $@

AM_JAVACFLAGS= @JAVAC_FLAGS@ -cp $(JAVAROOT):@GETOPTJAR@:@FCPARSERSJAR@:@WWNDESCJAR@:$(top_builddir)/depjars/json.jar

@PACKAGE@.jar: classjava.stamp
	awk -v BUILDTOP="bld" '/^package / {gsub (";$$",""); gsub ("\\.","/",$$2); SUBS[$$2]=$$2 } END { FIRST=0; for (n in SUBS) { if (0 == FIRST++) printf "jar cvf $@"; printf " -C %s %s",BUILDTOP,n }; printf "\n"; }' $(nodist_java_JAVA) | $(SHELL)
	touch $(notdir $(subdir)).class

clean-local:
	CLASSES=`for i in $(nodist_java_JAVA); do awk -v FILE="$$i" '/^package / {gsub (";$$",""); gsub (".java",".class",FILE); gsub ("\\\\.","/",$$2); printf "%s/%s\n",$$2,FILE}' $(srcdir)/$$i; done`; (cd $(top_builddir)/ && rm -f $$CLASSES)
	

$(subdir).jar: @PACKAGE@.jar @GETOPTJAR@
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.vw4.VirtualWisdom4ClientTool"  >> $(@D)/.workdir/MANIFEST.MF
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do (cd $(@D)/.workdir  && jar xf $$i) ; done
if DO_GETOPTJAR
	(cd $(@D)/.workdir && jar cm MANIFEST.MF gnu* org) > $@
else
	(cd $(@D)/.workdir && jar cm MANIFEST.MF org) > $@
endif DO_GETOPTJAR_FALSE
	rm -fr $(@D)/.workdir

