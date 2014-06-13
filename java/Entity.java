package org.smallfoot.vw4 ;

/** @file */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import org.smallfoot.parser.ParserTee;
import org.smallfoot.parser.zone.AliShowZoneParser;
import org.smallfoot.parser.zone.Alias4Parser;
import org.smallfoot.parser.zone.BNAZoneParser;
import org.smallfoot.parser.zone.DeviceAliasParser;
import org.smallfoot.parser.zone.NicknameParser;
import org.smallfoot.parser.zone.ShowZoneParser;
import org.smallfoot.parser.zone.ZPAliasEntry;
import org.smallfoot.parser.zone.ZoneParser;
import org.smallfoot.vw4.VWImport;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/* The following is conditionally permitted based on the mutually-exclusive choice at ./configure time.  --with-json= chooses exactly one of the following, causing one of the following comment blocks pairs to be butchered */
/* *#/	import org.json.*;				/#* */
/* */	import com.fasterxml.jackson.core.json.*;	/* */
/* */	import com.fasterxml.jackson.core.*;		/* */
/* */	import com.fasterxml.jackson.databind.*;	/* */
/* */	import com.fasterxml.jackson.databind.jsonschema.*;	/* */

/**
 * An Entity is the core mutable object used in the JSON import for VW4.  I'm not so sure how it'll unfold yet, but I intend to treat all hosts and hbas and storagecontrollers and iomodules as similar objects: things that can have children things.  In the short term, be very careful: there is an Entity, and a VWImport::Entity
 */
public abstract class Entity
{
	/** Descendents of Entity should know whether a given entity can be one of their child elements.  This exception is intended as a method of signalling -- and rippling up if necessary -- than an intended seconding as a child element is not accepted by the would-be parent. */
    public class ImproperChildException extends java.lang.Exception
    {
	/** create a new instance with the given message @param s description to encapsulate */
	public ImproperChildException(String s) { super(s); }

	/** create a new instance with a consistent message based on the entities offered @param e child entity @param p parent entity */
	public ImproperChildException(Entity e, Entity p) { super(String.format("Entity %s cannot be a child of %s",e.getClass().getName().replaceAll("^.*\\.",""),p.getClass().getName().replaceAll("^.*\\.",""))); }
    }

    protected Vector<Entity> children = null;		/**< local singleton late-instantiated as needed by children() */
    protected Vector<Entity> children() { if (null == children) children = new Vector<Entity>(1,1); return children; }  /**< local access to local singleton .children */

    protected String name;		/**< the unique name of the entity */
    public String name() { return name; }	/**< getter */
    public void setname(String name) { this.name = name; }	/**< setter */

    /**
     * Class Constructor with no initial child
     */
    public Entity (String name)
    {
	this.name = name;
    }

    /**
     * Class Constructor with an initial child to absorb
     */
    public Entity (String name, Entity e) throws ImproperChildException
    {
	this(name);
	if (canBeChild(e))
	    children().add(e);
	else
	    throw new ImproperChildException(e, this);
    }

	/** whether a given entity can be this entity's child @return true if accepted, false if refused @param e entity to check for possible descendent-hood */
    protected abstract boolean canBeChild (Entity e);
}
