package org.smallfoot.vw4 ;

/** @file */

import java.util.Vector;

public class VWImport
{
    public static class ITLPattern
    {
	public enum edit_type { add };
	String initiator; 
    };

    public static class Entities
    {
	String description;
	Vector<String> tags;
	Vector<ITLPattern> itl_patterns;
	public enum edit_type { add };
	String type;
	String name;
    };

    public Vector<Entities> entities;
    public String version;
}

