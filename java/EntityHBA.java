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
public class EntityHBA extends Entity.LeafEntity
{
    /**
     * Basic Class Constructor
     */
    public EntityHBA (String name, String wwn)
    {
        super(name, wwn);
    }

    /** create a streamable JSON entity from this one @return a org.smallfoot.vw4.VWImport.Entity representation of this instance @param tag default tag to apply */
    protected org.smallfoot.vw4.VWImport.Entity vwentity (String tag)
    {
        org.smallfoot.vw4.VWImport.Entity e = new org.smallfoot.vw4.VWImport.Entity();

        if (_compatibilityVersion() < 0x040002)
            e.type = "hba";
        else
            e.type = "fcport";
        e.name = name();
        if (_compatibilityVersion() < 0x040002)
            e.add(wwn);
        else
            e.wwn = wwn;
        e.description = description();
        if (null != tag) e.tags().add(tag);
        e.edit_type = org.smallfoot.vw4.VWImport.Edit_Type.add;

        return e;
    }

    /** create a new Entity of the correct class to be a parent of this one */
    public Entity newParent (String name)
    {
        try
        {
            return new EntityHost(name, this);
        }
        catch (Entity.ImproperChildException ice)
        {
            /* ignored... @todo should run in circles, scream and shout */
        } return null;
    }

}
