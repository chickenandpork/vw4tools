package org.smallfoot.vw4 ;

/** @file */

import java.util.Vector;

/**
 * A VWImport is a single idempotent import for the VirtualWisdom4 product.  It's basically
 * a wrapper for a number of entities and a version to define the format of the enclosed
 * entities for import.
 *
 * As a convention, Product Management recommends tagging all imported entities with
 * "import" so that a select group can be dropped in case of errors rather than deleting
 * all entities in the entire VirtualWisdom product.
 */
public class VWImport
{
    /**
     * The edit type of a JSON for VW4 Import can be either "add" or "modify"; the
     * creator needs to know ahead of time whether an entry of the same name currently
     * exists
     */
    public enum Edit_Type {
	add,	/**< add this element: no current element exists with the same name */
	modify	/**< use this value to modify an existing element with the same name */
    };
    
    /**
     * An ITLPattern is udes to define an application Entity based on the ITLs it
     * requires.  For example an "UberDatabase" application may define certain servers
     * using certain LUNs on certain storage targets
     */
    public static class ITLPattern
    {
	Edit_Type edit_type;		/**< What kind of edit are we doing? */
	String initiator; 		/**< ITLPattern can have values defined for Initiators, Targets, and LUNs, or all three (T/L to come later) */
    };

    /** An entity for import */
    public static class Entity
    {
	String description;		/**< user-readable description f the entity; constraints unknown */
	Vector<String> tags;		/**< tags for the entity which can be used to define multiple groupings for a given entity.  Better than folders: if folders were used, an entity might have only one hierarchical "folder" in which it exists, but any number of tags may be applied to an entity at a time */
	Vector<ITLPattern> itl_patterns;	/**< is an Entity is defined by ITLPatterns, they would be listed herein */
	Edit_Type edit_type;		/**< What kind of edit are we doing?  Add or Modify? */
	String type;			/**< What type of Entity is this?  (full range of values unknown) */
	String name;			/*<< What unique name does this entity have? */
    };

    protected Vector<Entity> entities = null;		/**< the entities in the single Import action */
    /**
     * singleton to provide an entity vector without having to check whether it's been
     * created yet
     */
    public Vector<Entity> entities() { if (null == entities) entities = new Vector<Entity>(); return entities; }
    public String version = "1";		/**< it's great that the import format is versioned hence extensible, perhaps when some real-life-testing highlights concerns we've discussed */
}

