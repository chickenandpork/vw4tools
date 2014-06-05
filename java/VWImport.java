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
@com.fasterxml.jackson.annotation.JsonPropertyOrder(alphabetic=true)
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
     * An ITLPattern is used to define an application Entity based on the ITLs it
     * requires.  For example an "UberDatabase" application may define certain servers
     * using certain LUNs on certain storage targets
     */
    @com.fasterxml.jackson.annotation.JsonPropertyOrder(alphabetic=true)
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public static class ITLPattern
    {
	public Edit_Type edit_type;		/**< What kind of edit are we doing? */
	public String initiator; 		/**< initiator portion of the ITL pattern */
	public String target; 			/**< target portion of the ITL pattern */
	public String lun; 			/**< lun portion of the ITL pattern */
    };

    /** An entity for import */
    @com.fasterxml.jackson.annotation.JsonPropertyOrder(alphabetic=true)
    public static class Entity
    {
	public String description;		/**< user-readable description of the entity; constraints unknown */
	public Vector<String> tags;		/**< tags for the entity which can be used to define multiple groupings for a given entity.  Better than folders: if folders were used, an entity might have only one hierarchical "folder" in which it exists, but any number of tags may be applied to an entity at a time */
        @com.fasterxml.jackson.annotation.JsonGetter("tags")
	public Vector<String> tags() { if (null == tags) tags = new Vector<String>(); return tags; }		/**< singleton access to tags */
	protected Vector<ITLPattern> itl_patterns;	/**< is an Entity is defined by ITLPatterns, they would be listed herein */
	/** singleton access to itl_patterns */
        @com.fasterxml.jackson.annotation.JsonGetter("itl_patterns")
	public Vector<ITLPattern> itl_patterns() {if (null == itl_patterns) itl_patterns = new Vector<ITLPattern>(); return itl_patterns; }
	public Edit_Type edit_type;		/**< What kind of edit are we doing?  Add or Modify? */
	public String type;			/**< What type of Entity is this?  (full range of values unknown) */
	public String name;			/**< What unique name does this entity have? */
    };

    protected Vector<Entity> entities = null;		/**< the entities in the single Import action */
    /**
     * singleton to provide an entity vector without having to check whether it's been
     * created yet
     */
    @com.fasterxml.jackson.annotation.JsonGetter("entities")
    public Vector<Entity> entities() { if (null == entities) entities = new Vector<Entity>(); return entities; }
    public String getVersion() { return "1"; }		/**< it's great that the import format is versioned hence extensible, perhaps when some real-life-testing highlights concerns we've discussed */
}

