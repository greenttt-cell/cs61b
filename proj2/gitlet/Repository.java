package gitlet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static gitlet.Commit.createCommit;
import static gitlet.Commit.createMergeCommit;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commit directory.*/
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    /** The Blob directory */
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");

    /* TODO: fill in the rest of this class. */
    /* create a staging area using set*/
    /*branch name with HEAD commitID */
    public static TreeMap<String, String> STAGING_AREA_ADD = new TreeMap<>();/* FILEName with blobID*/
    public static TreeMap<String, String> STAGING_AREA_RM = new TreeMap<>();
    public static TreeMap<String, Commit> commitMap = new TreeMap<>();
    public static TreeMap<String, String> BRANCH = new TreeMap<>();
    private static String Master;
    private static String HEAD;
    private static String CURRENT_BRANCH;//name string
    public static String getHead() {
        return HEAD;
    }
    /*TODO: Creates a new Gitlet version-control system in the current directory
     *      create a new commit, a commit that contains no files and has the commit message initial commit
     *      a single branch: master, which initially points to this initial commit, and master will be the current branch
     *      timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970
     *      ERROR CASE :print the error message A Gitlet version-control system already exists in the current directory.
     */
    public static void init() {
        boolean check = new File(CWD, ".gitlet").exists();
        if (!check) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            Commit initialCommit = new Commit();
            Master = initialCommit.getId();
            HEAD = Master;
            commitMap.put(HEAD, initialCommit);
            BRANCH.put("master", Master);
            CURRENT_BRANCH = "master";
        }
    }

    /*ADD method
     *TODO: Adds a copy of the file as it currently exists to the staging area
     *      staging area in the .gitlet
     *      other situation: if the same as some in the current commit, not added or remove that in staging area
     *      if already-exited in the stage area, just overwrite it
     *      ERROR CASE: If the file does not exist, print the error message File does not exist.
     *                  and exit without changing anything.
     */
    public static void add(String FileName) {
        File fileAdded = new File(CWD, FileName);
        if (!fileAdded.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
            return;
        }
        Blob blob = new Blob(fileAdded);
        Commit currentCommit = commitMap.get(HEAD);
        if (currentCommit.getMap().containsKey(blob.getId())) {
            if (STAGING_AREA_ADD.containsKey(FileName)) {
                rm(FileName);
            }
        } else {
            STAGING_AREA_ADD.put(FileName, blob.getId());
        }
        writeObject(BLOB_DIR, blob);

    }

    /* commit(string message)
     * TODO: create a new commit,has same files as parent,only changed if add or remove method
     *       The staging area is cleared after a commit.
     *       !!DATE will not same as parent (the time it was made)
     *       head pointer move on
     *       hash: SHA-1 ID
     *       log message
     *       blob is the content
     *       FAILURE: no files in staging area,print No changes added to the commit.
     *                if commit message is null:print error -> Please enter a commit message.
     */
    public static void commit(String message) {
        /* error cases */
        if (STAGING_AREA_ADD == null && STAGING_AREA_RM == null) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message == null) {
            System.out.println("No changes added to the commit.");
            return;
        }

        Commit current = createCommit(message);
        if (STAGING_AREA_ADD != null) {
            for (Map.Entry<String, String> entry : STAGING_AREA_ADD.entrySet()) {
                current.getMap().put(entry.getKey(), entry.getValue());
            } STAGING_AREA_ADD.clear();
        }
        if (STAGING_AREA_RM != null) {
            for (String fileName : STAGING_AREA_RM.keySet()) {
                current.getMap().remove(fileName);
            } STAGING_AREA_RM.clear();
        }
        current.createID();
        HEAD = current.getId();
        Master = HEAD;
        writeObject(COMMIT_DIR, current);
        /* 更新branch，它仍然只保留分支的head commit*/
        String oldValue = BRANCH.get(CURRENT_BRANCH);
        BRANCH.replace(CURRENT_BRANCH, oldValue, current.getId());
    }

    /* rm(string file name)
    *TODO:if staged, unstage the file
    *       file in current commit, stage it and rm from working directory
    *       FAILURE: if not staged or tracked by current commit(head)
    *       print error message :No reason to remove the file.
     */
    public static void rm(String FileName) {
        File file = new File(CWD, FileName);
        TreeMap<String, String> current = commitMap.get(HEAD).getMap();
        if (STAGING_AREA_ADD.containsKey(FileName)) {
            STAGING_AREA_ADD.remove(FileName);
        }
        if (current.containsKey(FileName)) {
            if (!STAGING_AREA_RM.containsKey(FileName)) {
                STAGING_AREA_RM.put(FileName, current.get(FileName));
            }
            Utils.restrictedDelete(file);
        }
        if (!STAGING_AREA_ADD.containsKey(FileName) && !current.containsKey(FileName)) {
            System.out.println("No reason to remove the file.");
        }
    }

    /*log
    *TODO:  display all the commit(id, time, message(要换行)
    *       before each commit display === and after each commit an empty line
    *       merge commit display if has two parent commits
     */
    private static void printCommit(Commit commit) {
        String[] parentID = commit.getParentID();
        System.out.println("===");
        System.out.println(String.format("commit %s", commit.getId()));
        if (parentID[1] != null) {
            System.out.println(String.format("Merge: %s %s",
                    parentID[0].substring(0, 7),
                    parentID[1].substring(0, 7)));
        }
        System.out.println(String.format("Date: %s", commit.getTimestamp().toString()));
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public static void log() {
        Commit tmp = commitMap.get(HEAD);
        while (tmp != null) {
            printCommit(tmp);
            tmp = commitMap.get(tmp.getParentID()[0]);
        }
    }

    /* global-log
    TODO: Print all the commit
     */
    public static void globalLog() {
        List<String> commitName = plainFilenamesIn(COMMIT_DIR);
        if (commitName == null) {
            return;
        }
        for (String name : commitName) {
            Commit tmpCommit = readObject(join(COMMIT_DIR, name), Commit.class);
            printCommit(tmpCommit);
        }
    }

    public static void find(String commitMessage) {
        List<String> commitName = plainFilenamesIn(COMMIT_DIR);
        if (commitName == null) {
            System.out.println("Found no commit with that message.");
            return;
        }
        boolean check = false;
        for (String name : commitName) {
            Commit tmpCommit = readObject(join(COMMIT_DIR, name), Commit.class);
            if (tmpCommit.getMessage().equals(commitMessage)) {
                check = true;
                System.out.println(tmpCommit.getId());
            }
        }
        if (!check) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        System.out.println("*" + CURRENT_BRANCH);
        for (Map.Entry<String,String > entry : BRANCH.entrySet()) {
            String branch = entry.getKey();
            if (!branch.equals(CURRENT_BRANCH)) {
                System.out.println(branch);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (Map.Entry<String,String > file : STAGING_AREA_ADD.entrySet()) {
            System.out.println(file.getKey());
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (Map.Entry<String,String > file : STAGING_AREA_RM.entrySet()) {
            System.out.println(file.getKey());
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** write file content into CWD */
    private static void checkOut3Help(String FileName, Commit commit) {
        String blobID = commit.getMap().get(FileName);
        File file = join(BLOB_DIR, blobID);
        Blob blob = readObject(file, Blob.class);
        File fileWrite = join(CWD, FileName);
        writeContents(fileWrite, blob.getContent());
    }
    private static void checkOutHelp(String FileName, Commit current) {
        if (!current.getMap().containsKey(FileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        checkOut3Help(FileName, current);
    }
    /** java gitlet.Main checkout -- [file name] */
    public static void checkout1(String FileName) {
        Commit current = commitMap.get(HEAD);
        checkOutHelp(FileName, current);
    }
    /*  java gitlet.Main checkout [commit id] -- [file name]*/
    public static void checkout2(String commitID, String FileName) {
        if (!commitMap.containsKey(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit current = commitMap.get(commitID);
        checkOutHelp(FileName, current);
    }
    /* java gitlet.Main checkout [branch name]*/

    private static void checkOut3Helper(Commit currentCommit, Commit futureCommit) {
        /** check failure situation */
        for (Map.Entry<String,String > entry : futureCommit.getMap().entrySet()) {
            if (!currentCommit.getMap().containsKey(entry.getKey())) {  /* 原来的commit的没有tracked的文件现在在另一个commit中出现了*/
                if (join(CWD, entry.getKey()).exists()) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        for (Map.Entry<String,String > entry : futureCommit.getMap().entrySet()) {
            checkOut3Help(entry.getKey(), futureCommit);
        }
        TreeMap<String, String> mapCompare = futureCommit.getMap();
        for (String fileName : currentCommit.getMap().keySet()) {
            if (!mapCompare.containsKey(fileName)) {
                restrictedDelete(fileName);
            }
        }
        STAGING_AREA_ADD.clear();
        STAGING_AREA_RM.clear();
    }
    public static void checkout3(String branchName) {
        if (!BRANCH.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (branchName.equals(CURRENT_BRANCH)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        Commit futureCommit = commitMap.get(BRANCH.get(branchName));
        Commit currentCommit = commitMap.get(HEAD);
        checkOut3Helper(currentCommit, futureCommit);
        CURRENT_BRANCH = branchName;
        HEAD = futureCommit.getId();
    }

    /* branch command*/
    public static void branch(String branchName) {
        if (BRANCH.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Commit current = commitMap.get(HEAD);
        BRANCH.put(branchName, current.getId());
    }
    /* rm-branch */
    public static void rm_branch(String branchName) {
        if (!BRANCH.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(CURRENT_BRANCH)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        BRANCH.remove(branchName);
    }

    public static void reset(String commitId) {
        if (!commitMap.containsKey(commitId)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        checkOut3Helper(commitMap.get(HEAD), commitMap.get(commitId));
        HEAD = commitId;
    }

    /**
     * TODO:(non-trivial cases)
     *     ? file changed in given branch(since split point), not changed in current commit,
     *      should be changed to version of given branch and staged
     *      /if file changed in current branch but not given branch: stay same as in current branch
     *      /if changed in same way(content is same) in both branches, 就这样融合 REMOVE??
     *      /if file created only in current branch after split point, just remain
     *      /if file created only in given branch after split point, should check out and stage it;
     *     / if file in split point, but unmodified in the current branch, disappear in the given branch should be removed (and untracked).
     *     /                        but disappear in the current branch, unmodified in the given branch should be disappear
*           mergecommit 最后是通过staging area和现有branch来生成，不用手工put来维护
     */
    public static void merge(String branchName) {
        /* failure cases */
        if (!BRANCH.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(CURRENT_BRANCH)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        if (STAGING_AREA_RM != null || STAGING_AREA_ADD != null) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        /* 还有2个failure没有写 */
        for (String fileName : plainFilenamesIn(CWD)) {
            boolean currentRes = commitMap.get(HEAD).getMap().containsKey(fileName);
            boolean givenRes = commitMap.get(BRANCH.get(branchName)).getMap().containsKey(fileName);
            if (!currentRes && givenRes) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
                return;
            }
        }
        Commit currentCommit = commitMap.get(HEAD);
        Commit givenCommit = commitMap.get(BRANCH.get(branchName));
        String splitPoint = searchBFS(currentCommit, givenCommit);
        Commit splitCommit = commitMap.get(splitPoint);
        /*two trivial cases */
        if (splitPoint.equals(givenCommit.getId())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitPoint.equals(currentCommit.getId())) {
            checkout3(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        /* other more complicated cases TODO: build sets containing files modified*/
        //Commit mergeCommit =//
        Set<String> currentFile = new HashSet<>();
        Set<String> givenFile = new HashSet<>();
        TreeMap<String, String> currentMap = currentCommit.getMap();
        TreeMap<String, String> givenMap = givenCommit.getMap();
        TreeMap<String, String> mergeMap = new TreeMap<>();
        for (Map.Entry<String,String > entry : splitCommit.getMap().entrySet()) {
            String fileName = entry.getKey();
            String splitId = entry.getValue();

            if (currentMap.containsKey(fileName) && givenMap.containsKey(fileName)) {
                String currentId = currentMap.get(fileName);
                String givenId = givenMap.get(fileName);
                if (!givenId.equals(splitId) && currentId.equals(splitId)) {
                    checkout2(givenId, fileName);
                    add(fileName);
                }
                else if (givenId.equals(splitId) && !currentId.equals(splitId)) {
                    mergeMap.put(fileName, currentMap.get(fileName));
                    //add(fileName);
                }
                else if (!givenId.equals(splitId) && givenId.equals(currentId)) {
                    mergeMap.put(fileName, currentId);
                }
                else if (!givenId.equals(splitId)) {//三个互不相同
                    String currentContent = fileContent(currentCommit, fileName);
                    String givenContent = fileContent(givenCommit, fileName);
                    conflictHelp(currentContent, givenContent, fileName);
                }
            }
            else if (currentMap.containsKey(fileName) && !givenMap.containsKey(fileName)) {
                String currentId = currentMap.get(fileName);
                if (currentId.equals(splitId)) {
                    rm(fileName);
                }
                else {
                    String currentContent = fileContent(currentCommit, fileName);
                    String givenContent = "";
                    conflictHelp(currentContent, givenContent, fileName);
                }
            }
            else {
                String givenId = givenMap.get(fileName);
                if (!givenId.equals(splitId)) {
                    String currentContent = "";
                    String givenContent = fileContent(givenCommit, fileName);
                    conflictHelp(currentContent, givenContent, fileName);
                }
            }
        }

        /*找出只有current 有的文件，保持不变*/
        for (Map.Entry<String,String > entry : currentCommit.getMap().entrySet()) {
            String currentId = entry.getValue();
            String fileName = entry.getKey();
            if (!givenMap.containsKey(fileName) && !splitCommit.getMap().containsKey(fileName)) {
                mergeMap.put(fileName, currentId);
            }
        }
        for (Map.Entry<String,String > entry : givenCommit.getMap().entrySet()) {
            String givenId = entry.getValue();
            String fileName = entry.getKey();
            /* 找出只有given 有的文件， 其他两个没有的*/
            if (!currentMap.containsKey(fileName) && !splitCommit.getMap().containsKey(fileName)) {
                checkout2(currentCommit.getId(),fileName);
                add(fileName);
            }
            /*找出只有split没有的文件，处理current和given内容不同时候的conflict*/
            if (currentMap.containsKey(fileName) && !splitCommit.getMap().containsKey(fileName)) {
                String currentId = currentMap.get(fileName);
                if (!currentId.equals(givenId)) {
                    String currentContent = fileContent(currentCommit, fileName);
                    String givenContent = fileContent(givenCommit, fileName);
                    conflictHelp(currentContent, givenContent, fileName);
                }
            }
        }

        /*最后生成完整的merge commit*/
        for (Map.Entry<String,String > entry : STAGING_AREA_ADD.entrySet()) {
            mergeMap.put(entry.getKey(), entry.getValue());
        }
        STAGING_AREA_ADD.clear();
        String message = String.format("Merged %s into %s.", branchName, CURRENT_BRANCH);
        Commit mergeCommit = createMergeCommit(givenCommit, currentCommit, message, mergeMap);
        HEAD = mergeCommit.getId();
    }
    private static void conflictHelp(String current, String given, String fileName) {
        String content = String.format("<<<<<<< HEAD\n%s\n=======\n%s\n>>>>>>>", current, given);
        File fileToWrite = join(CWD, fileName);
        writeContents(fileToWrite, content);
        add(fileName);
    }
    /*get the content (string format) of the file*/
    private static String fileContent(Commit commit, String fileName) {
        String blobId = commit.getMap().get(fileName);
        File file = join(BLOB_DIR, blobId);
        Blob blob = readObject(file, Blob.class);
        return new String(blob.getContent(), StandardCharsets.UTF_8);
    }
    private static void printMergeConflict(Commit given, Commit current, String fileName) {
        System.out.println("<<<<<<< HEAD");
        //content current branch
        System.out.println(fileContent(current, fileName));
        System.out.println("=======");
        //given content
        System.out.println(fileContent(given, fileName));
        System.out.println(">>>>>>>");
    }
    /* build a BFS algorithm to find the split point*/
    private static String searchBFS(Commit current, Commit given) {
        Queue<String> queue1 = new LinkedList<>();
        Queue<String> queue2 = new LinkedList<>();
        Set<String> visited1 = new HashSet<>();
        Set<String> visited2 = new HashSet<>();
        queue1.add(current.getId());
        queue2.add(given.getId());
        return searchHelp(queue1, queue2, visited1, visited2);
    }
    private static String searchHelp(Queue<String> queue1, Queue<String> queue2, Set<String> visited1, Set<String> visited2) {
        if (queue1.isEmpty() && queue2.isEmpty()) {
            return null;
        }
        else if (queue1.isEmpty()) {
            return searchHelpEmpty(queue2, visited1, visited2);
        }
        else if (queue2.isEmpty()) {
            return searchHelpEmpty(queue1, visited2, visited1);
        }
        else {
            while (!queue1.isEmpty() || !queue2.isEmpty()) {
                Queue<String> tmp1 = new LinkedList<>();
                String result1 = forHelp(queue1, tmp1, visited1, visited2);
                if (result1 != null) {
                    return result1;
                }
                queue1.addAll(tmp1);
                Queue<String> tmp2 = new LinkedList<>();
                String result2 = forHelp(queue1, tmp2, visited1, visited2);
                if (result2 != null) {
                    return result2;
                }
                queue2.addAll(tmp2);
            }
        } return null;
    }
    private static String searchHelpEmpty(Queue<String> queue, Set<String> visited1, Set<String> visited2) {
        while (!queue.isEmpty()) {
            String id2 = queue.poll();
            if (visited1.contains(id2)) {
                return id2;
            }
            visited2.add(id2);
            String[] parentId2 = commitMap.get(id2).getParentID();
            for (int i = 0; i < 2; i += 1) {
                if (parentId2[i] != null) {
                    queue.add(parentId2[i]);
                }
            }
        }
        return null;
    }
    private static void addParentToQueue (Queue<String> queue, String[] parentId) {
        for (int i = 0; i < 2; i += 1) {
            if (parentId[i] != null) {
                queue.add(parentId[i]);
            }
        }
    }
    private static String forHelp(Queue<String> queue1, Queue<String> tmp1, Set<String> visited1, Set<String> visited2) {
        int queue1_size = queue1.size();
        for (int i = 0; i < queue1_size; i += 1) {
            String id1 = queue1.poll();
            if (visited2.contains(id1)) {
                return id1;
            }
            visited1.add(id1);
            addParentToQueue(tmp1, commitMap.get(id1).getParentID());
        }
        queue1.addAll(tmp1);
    }
 }
