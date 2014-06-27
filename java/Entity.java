package org.smallfoot.vw4 ;

/** @file */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.TreeMap;
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
        public ImproperChildException(String s)
        {
            super(s);
        }

        /** create a new instance with a consistent message based on the entities offered @param e child entity @param p parent entity */
        public ImproperChildException(Entity e, Entity p)
        {
            super(String.format("Entity %s cannot be a child of %s",e.getClass().getName().replaceAll("^.*\\.",""),p.getClass().getName().replaceAll("^.*\\.","")));
        }
    }

    protected Vector<Entity> children = null;		/**< local singleton late-instantiated as needed by children() */
    protected Vector<Entity> children()
    {
        if (null == children) children = new Vector<Entity>(1,1);    /**< local access to local singleton .children */
        return children;
    }

    protected String name;		/**< the unique name of the entity */
    public String name()
    {
        return name;    /**< getter */
    }
    public void setname(String name)
    {
        this.name = name;    /**< setter */
    }

    protected String description;		/**< the description of the entity showing source */
    public String description()
    {
        return description;    /**< getter */
    }
    public void setDescription(String description)
    {
        this.description = description;    /**< setter */
    }

    protected WeakReference<Entity> parent = null;		/**< convenience weak-reference to parent: I want a reference but not one that will block GC */
    boolean isOrphan()
    {
        return (null == parent);    /**< quick reference whether this object has a parent object already defined */
    }
    boolean isOrphan(TreeMap<String,Entity> list)
    {
        Entity e;

        if (null != parent) return false;

        for (String k: list.keySet())
            if ( (null != (e = list.get(k))) && (null != e.children) ) /* extra null==children to avoid unnecessary singleton instantiation via children() */
                if (e.children().contains(this))
                {
                    parent = new WeakReference<Entity>(e);
                    return false;
                }
        return true;
    }

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
        maybeAdopt(e);
    }

    public void maybeAdopt(Entity e) throws ImproperChildException
    {
        if (canBeChild(e))
        {
            children().add(e);
            e.parent = new WeakReference<Entity>(this);
        }
        else
            throw new ImproperChildException(e, this);
    }

    /** whether a given entity can be this entity's child @return true if accepted, false if refused @param e entity to check for possible descendent-hood */
    protected abstract boolean canBeChild (Entity e);

    /** create a streamable JSON entity from this one @return a org.smallfoot.vw4.VWImport.Entity representation of this instance @param tag default tag to apply */
    protected abstract org.smallfoot.vw4.VWImport.Entity vwentity (String tag);

    /** convenience: add myself and all children to the streamable exporter given as "v" @param v VWImport streamer to add myself and children to @param tag default tag to apply @return number of entities written */
    int addTo (VWImport v, String tag)
    {
        int i = 0;

        if (null == v)
        {
            System.out.println("WARNING: VWImport is null");
        }
        else
        {
            v.addEntity(vwentity(tag));
            i++;

            for (Object o: children())
                if (o instanceof Entity)
                    i += ((Entity) o).addTo(v, tag);
                else
                    System.out.println("WARNING: child of "+name()+" not written: "+o.toString());
        }

        return i;
    }

    /** create a new Entity of the correct class to be a parent of this one */
    public abstract Entity newParent (String name);


    /**
     * A LeafEntity is the common ancestor of Storage FAs and Server HBAs; this is combined only so that leaves can be treated in common
     */
    public static class LeafEntity extends Entity
    {
        protected String wwn;               /**< the unique WWPN of the hba */
        public String wwn()
        {
            return wwn;    /**< getter */
        }

        public String parentName()
        {
            if ( (null == parent) || (null == parent.get()) )
                return name();
            else
                return parent.get().name();
        }

        /**
         * Class Constructor with no initial child
         */
        public LeafEntity (String name, String wwn)
        {
            super(name);
            this.wwn = wwn;
        }

        protected boolean canBeChild (Entity e)
        {
            return false;    /**< this entity has no children so this method will always be false */
        }

        /** create a bogus function to avoid build errors */
        protected org.smallfoot.vw4.VWImport.Entity vwentity (String tag)
        {
            return null;
        }

        public Entity newParent (String name)
        {
            return null;
        }
    }
}
