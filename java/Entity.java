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
    protected static Integer compatibilityVersion = null;		/**< singleton-used compatibility: if (re)set to null, _compatibilityVersion() recalculates */
    /**
     * calculate the compatibilityVersion as required
     *
     * @return integer value of compatibilityVersion
     *
     * @jvmopt <b>compat.target</b>
     * (values: X.Y.Z as a version.  For example: 4.0.1, 4.1, 4, 4.2)
     * can be used to tell the JSON output to use specific features available on later versions of the VirtualWisdom4 product.  Initially controls whether fcport entities are created but may expand.
     */
    protected static int _compatibilityVersion()
    {
        if (null == compatibilityVersion)
        {
            String s;
            if (null != (s= System.getProperties().getProperty("compat.target")))
            {
                compatibilityVersion = 0;

                String parts[] = s.split("\\.");
                for (int i = 0; i < 3; i++)
                {
                    try
                    {
                        Integer r = Integer.parseInt(parts[i].replaceAll("[^0-9]",""));
                        compatibilityVersion *= 0x100;
                        compatibilityVersion += r.intValue();
                    }
                    catch (ArrayIndexOutOfBoundsException aioobe)
                    {
                        compatibilityVersion *= 0x100;
                    }
                    catch (NumberFormatException nfe)
                    {
                        compatibilityVersion *= 0x100;
                    }
                }

                if (0 == compatibilityVersion) compatibilityVersion = 0x040002;
            }
            else
                compatibilityVersion = 0x040002;
        }
        return compatibilityVersion;
    }

    /** convert the compatibilityVersion into a string @return string value of compatibilityVersion */
    public static String compatibilityVersion()
    {
        return String.format ("%d.%d.%d",_compatibilityVersion() / 0x10000 % 0x100, _compatibilityVersion() / 0x100 % 0x100, _compatibilityVersion() % 0x100);
    }
    /**
     * set a new compatibility string to set a different target version
     *
     * This, with compatibilityVersion() and _compatibilityVersion(), is a lot of scaffolding
     * around compatibility outputs; I'm both trying to give some versatility here, plus
     * experimenting myself with properties-vs-commandlines-vs-both.  At the end of the day,
     * both methods work to set a version for output, initially a determiner whether fcport
     * entities are produced.  There's future possibility for growth, including
     * config/properties files, afforded by this massive 4-part scaffolding with delusions of
     * grandeur.
     *
     * @param newVersion the new value to use as a version
     *
     * @return previous config version
     */
    public static String compatibilityVersion(String newVersion)
    {
        String current = compatibilityVersion();
        compatibilityVersion = null;
        System.getProperties().setProperty("compat.target", newVersion);
        return current;
    }

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
    /** get a list of child entities (local access to local singleton .children) @return list of zero or more child entities but never null */
    protected Vector<Entity> children()
    {
        if (null == children) children = new Vector<Entity>(1,1);
        return children;
    }

    protected String name;		/**< the unique name of the entity */
    /** unique name of the entity: getter for internal variable @return the name */
    public String name()
    {
        return name;
    }
    /** set the unique name of the entity: setter @param name the name to set */
    public void setname(String name)
    {
        this.name = name;
    }

    protected String description;		/**< the description of the entity showing source */
    /** the description of the entity showing source @return description of the entity */
    public String description()
    {
        return description;
    }
    /** set the description of the entity to show its source: setter pattern @param description the description to set */
    public void setDescription(String description)
    {
        this.description = description;
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
     *
     * @param name initial name of the new entity
     */
    public Entity (String name)
    {
        this.name = name;
    }

    /**
     * Class Constructor with an initial child to absorb
     *
     * @param name initial name of the new entity
     * @param e Entity to consider for adoption as child
     */
    public Entity (String name, Entity e) throws ImproperChildException
    {
        this(name);
        maybeAdopt(e);
    }

    /**
     * Convenience function: Entity should either adopt a given child "e" or throw an exception.  This allows very simple coding of streamlining the adoption in a cleaner iteration loop.
     *
     * @param e Entity to consider for adoption as child
     */
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

    /**
     * whether a given entity can be this entity's child
     *
     * @param e entity to check for possible descendent-hood
     * @return true if this entity accepts children of "e"'s descendent type
     */
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

    /**
     * create a new Entity of the correct class to be a parent of this one.  This function is used to polymorphically create the parentage of an entity such as the host holding an HBA
     *
     * @return new parent for this entity
     * @param name initial name of the new entity
     */
    public abstract Entity newParent (String name);


    /**
     * A LeafEntity is the common ancestor of Storage FAs and Server HBAs; this is combined only so that leaves can be treated in common
     */
    public static class LeafEntity extends Entity
    {
        protected String wwn;               /**< the unique WWPN of the hba */
        /** the unique WWPN of the hba: getter for internal variable @return the WWPN */
        public String wwn()
        {
            return wwn;
        }

        /**
         * convenience function: if this entity has a parent, show the parent's name,
             * otherwise show this entity's name.  Used during OrderedTuples written via
             * VirtualWisdom4ClientTool.writeOrderedTuples(String, EntitySelector), this
             * allows a simpler, consistent coding when OrderedTuples is being exported.
             *
             * @return name of parent entity is existent, otherwise name of this entity
             */
        public String parentName()
        {
            if ( (null == parent) || (null == parent.get()) )
                return name();
            else
                return parent.get().name();
        }

        /**
         * Class Constructor with an initial child to absorb
         *
         * @param name initial name of the new entity
         * @param wwn initial child entity for this Leaf Entity
         */
        public LeafEntity (String name, String wwn)
        {
            super(name);
            this.wwn = wwn;
        }

        /**
         * whether a given entity can be this entity's child @return true if accepted,
         * false if refused.  LeafEntity has no children so this method will always be false
         *
         * @param e entity to check for possible descendent-hood
         * @return true if this entity accepts children of "e"'s descendent type (always false for Leaf Entity)
         */
        protected boolean canBeChild (Entity e)
        {
            return false;
        }

        /** create a bogus function to avoid build errors */
        protected org.smallfoot.vw4.VWImport.Entity vwentity (String tag)
        {
            return null;
        }

        /**
         * create a new Entity of the correct class to be a parent of this one: this
         * function is stubbed to return null so that this class can be a static class and
         * directly extensible.  Not really 100% good practice, but the descendency of
         * this LeafEntity is intended to collect functionality.  This class is wrapped
         * inside an Entity to retain a clear affinity, but thence needs to be static to be
         * extensible.  "She swallowed the cat to catch the spider, she swallowed the
         * spider to catch the fly.. "
         *
         * Descendents will override this method
         *
         * @return new parent for this entity
         * @param name initial name of the new entity
         */
        public Entity newParent (String name)
        {
            return null;
        }
    }
}
