package org.islamkosh.metadata;


import java.util.HashMap;
import java.util.Map;

/**
 * <h3>basic hashmap based data container which is used to collect the data</h3>
 * <p>all other data collectors must use this object to collect the data</p>
 * @author imtiaz
 * @version 1.0
 */
public class Metadata {
	
	private static String PRIMARY_KEY_LABEL="id"; 
	private String id; // this has to be unique
	/**
	 * A map of all metadata attributes.
	 */
	private Map<String, String[]> metadata = null;

	/**
	 * Constructs a new, empty data.
	 */
	public Metadata(String primaryKey) {
		this.id = primaryKey;
		metadata = new HashMap<String, String[]>();
		set(PRIMARY_KEY_LABEL, this.id);
	}
	
	/**
	 * You can edit the primary key
	 * @param primaryKey
	 */
	public void setPrimaryKey(String primaryKey) {
		this.id = primaryKey;
		set(PRIMARY_KEY_LABEL, this.id);
	}
	
	/**
	 * Returns true if named value is multivalued.
	 * 
	 * @param name
	 *            name of metadata
	 * @return true is named value is multivalued, false if single value or null
	 */
	public boolean isMultiValued(final String name) {
		return metadata.get(name) != null && metadata.get(name).length > 1;
	}

	/**
	 * Returns an array of the names contained in the metadata.
	 * 
	 * @return Metadata names
	 */
	public String[] names() {
		return metadata.keySet().toArray(new String[metadata.keySet().size()]);
	}

	/**
	 * Get the value associated to a metadata name. If many values are
	 * assiociated to the specified name, then the first one is returned.
	 * 
	 * @param name
	 *            of the metadata.
	 * @return the value associated to the specified metadata name.
	 */
	public String get(final String name) {
		String[] values = metadata.get(name);
		if (values == null) {
			return null;
		} else {
			return values[0];
		}
	}

	/**
	 * Get the values associated to a metadata name.
	 * 
	 * @param name
	 *            of the metadata.
	 * @return the values associated to a metadata name.
	 */
	public String[] getValues(final String name) {
		return _getValues(name);
	}

	private String[] _getValues(final String name) {
		String[] values = metadata.get(name);
		if (values == null) {
			values = new String[0];
		}
		return values;
	}

	/**
	 * Add a metadata name/value mapping. Add the specified value to the list of
	 * values associated to the specified metadata name.
	 * 
	 * @param name
	 *            the metadata name.
	 * @param value
	 *            the metadata value.
	 */
	public void add(final String name, final String value) {
		String[] values = metadata.get(name);
		if (values == null) {
			set(name, value);
		} else {
			String[] newValues = new String[values.length + 1];
			System.arraycopy(values, 0, newValues, 0, values.length);
			newValues[newValues.length - 1] = value;
			metadata.put(name, newValues);
		}
	}



	/**
	 * Set metadata name/value. Associate the specified value to the specified
	 * metadata name. If some previous values were associated to this name, they
	 * are removed.
	 * 
	 * @param name
	 *            the metadata name.
	 * @param value
	 *            the metadata value.
	 */
	public void set(String name, String value) {
		metadata.put(name, new String[] { value });
	}

	/**
	 * Remove a metadata and all its associated values.
	 * 
	 * @param name
	 *            metadata name to remove
	 */
	public void remove(String name) {
		metadata.remove(name);
	}

	/**
	 * Returns the number of metadata names in this metadata.
	 * 
	 * @return number of metadata names
	 */
	public int size() {
		return metadata.size();
	}

	/** Remove all mappings from metadata. */
	public void clear() {
		metadata.clear();
	}

	public boolean equals(Object o) {

		if (o == null) {
			return false;
		}

		Metadata other = null;
		try {
			other = (Metadata) o;
		} catch (ClassCastException cce) {
			return false;
		}

		if (other.size() != size()) {
			return false;
		}

		String[] names = names();
		for (int i = 0; i < names.length; i++) {
			String[] otherValues = other._getValues(names[i]);
			String[] thisValues = _getValues(names[i]);
			if (otherValues.length != thisValues.length) {
				return false;
			}
			for (int j = 0; j < otherValues.length; j++) {
				if (!otherValues[j].equals(thisValues[j])) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		String[] names = names();
		buf.append("Metadata: {\n");
		for (int i = 0; i < names.length; i++) {
			String[] values = _getValues(names[i]);
			for (int j = 0; j < values.length; j++) {
				buf.append(names[i]).append("=").append(values[j]).append("\n");
			}
		}
		buf.append("}\n");
		return buf.toString();
	}

}

