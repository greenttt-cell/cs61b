package gitlet;

import static gitlet.Repository.*;
import static gitlet.Utils.exitWithError;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            exitWithError("Must have at least one argument");
        }

        Repository repo = new Repository();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                repo.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                repo.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                repo.commit(args[1]);
                break;
            case "rm":
                repo.rm(args[1]);
                break;
            case "log":
                repo.log();
                break;
            case "global-log":
                repo.globalLog();
                break;
            case "find":
                repo.find(args[1]);
                break;
            case "status":
                repo.status();
                break;
            case "checkout":
                int number = args.length;
                if (number == 3) {
                    repo.checkout1(args[2]);
                }
                else if (number == 4) {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands");
                        break;
                    }
                    repo.checkout2(args[1], args[3]);
                } else if (number == 2) {
                    repo.checkout3(args[1]);
                }
                break;
            case "branch":
                repo.branch(args[1]);
                break;
            case "rm-branch":
                repo.rm_branch(args[1]);
                break;
            case "reset":
                repo.reset(args[1]);
                break;
            case "merge":
                repo.merge(args[1]);
                break;
            default:
                exitWithError(String.format("Unknown command: %s", args[0]));
        }
    }
}
