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
 * An EntityHBA is the representation of an HBA entity in the JSON import for VW4.  Be very careful: there is an Entity, and a VWImport::Entity
 */
public class EntityHBA extends Entity
{
    //protected String name;		/**< the unique name of the entity */
    protected String wwn;		/**< the unique WWPN of the hba */
    public String wwn() { return wwn; }		/**< getter */

    /**
     * Class Constructor with no initial child
     */
    public EntityHBA (String name, String wwn)
    {
	super(name);
	this.wwn = wwn;
    }

    protected boolean canBeChild (Entity e) { return false; }	/**< this entity has no children so this method will always be false */

	/** create a streamable JSON entity from this one @return a org.smallfoot.vw4.VWImport.Entity representation of this instance */
    protected org.smallfoot.vw4.VWImport.Entity vwentity ()
    {
        org.smallfoot.vw4.VWImport.Entity e = new org.smallfoot.vw4.VWImport.Entity();

        e.type = "hba";
        e.name = name();
	e.description = description();
        e.edit_type = org.smallfoot.vw4.VWImport.Edit_Type.add;

        return e;
    }

}
