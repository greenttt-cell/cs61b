package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import static gitlet.Repository.commitMap;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
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
    /** map that tracked blob,and value is the hashing of the blob*/
    private TreeMap<String, String> map;

    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parentID = (String[]) new Object[2];
        map = new TreeMap<>();
        id = Utils.sha1(message, timestamp.toString(), map.toString(), "");
        commitMap.put(id, this);
    }

    public String getId() {
        return id;
    }
    public TreeMap<String, String> getMap() {
        return map;
    }
    public static Commit createCommit(String message) {
        Commit parent1 = commitMap.get(Repository.getHead());
        Commit current = new Commit();
        current.message = message;
        current.parentID[0] = parent1.id;
        current.timestamp = new Date();
        current.map = new TreeMap<>(parent1.map);
        current.createID();
        return current;
    }

    public void createID() {
        if (parentID[0] == null) {
            id = Utils.sha1(message, timestamp.toString(), map.toString(), "", "");
        }
        else if (parentID[1] == null) {
            id = Utils.sha1(message, timestamp.toString(), map.toString(), parentID[0], "");
        }
        else {
            id = Utils.sha1(message, timestamp.toString(), map.toString(), parentID[0], parentID[1]);
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

    public static Commit createMergeCommit(Commit given, Commit current, String message, TreeMap<String, String> mergeMap) {
        Commit merge = new Commit();
        merge.timestamp = new Date();
        merge.message = message;
        merge.parentID[0] = current.getId();
        merge.parentID[1] = given.getId();
        merge.map = mergeMap;
        merge.createID();
        return merge;
    }
}
