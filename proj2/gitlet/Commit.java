package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;



/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The time record that the Commit is created. */
    private Date timestamp;
    /** The parent Commit of the commit */
    private String[] parentID;
    /** the hashing of the commit */
    private String id;
    /** map that tracked blob,and value is the hashing of the blob,key is the filename*/
    private TreeMap<String, String> map;

    public Commit(String message, Date timestamp, String[] parentID, TreeMap<String, String> map) {
        this.message = message;
        this.timestamp = timestamp;
        this.parentID = new String[2];
        this.parentID[0] = parentID[0];  // parentID[0] 是父提交
        this.parentID[1] = parentID[1];
        this.map = map;
        this.createID();
    }

    public String getId() {
        return id;
    }
    public TreeMap<String, String> getMap() {
        return map;
    }

    public void createID() {
        String mapToString = (map == null) ? "Null" : map.toString();
        if (parentID[0] == null) {
            id = Utils.sha1(message, timestamp.toString(), mapToString);
        }
        else if (parentID[1] == null) {
            id = Utils.sha1(message, timestamp.toString(), mapToString, parentID[0]);
        }
        else {
            id = Utils.sha1(message, timestamp.toString(), mapToString, parentID[0], parentID[1]);
        }
    }
    public String[] getParentID() {
        return parentID;
    }
    public String getMessage() {
        return message;
    }
    public Date getTimestamp() {
        return timestamp;
    }


}
