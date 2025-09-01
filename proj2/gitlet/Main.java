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

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                commit(args[1]);
                break;
            case "rm":
                rm(args[1]);
                break;
            case "log":
                log();
                break;
            case "global-log":
                globalLog();
                break;
            case "find":
                find(args[1]);
                break;
            case "status":
                status();
                break;
            case "checkout":
                int number = args.length;
                if (number == 3) {
                    checkout1(args[2]);
                }
                else if (number == 4) {
                    checkout2(args[1], args[3]);
                } else if (number == 2) {
                    checkout3(args[1]);
                }
                break;
            case "branch":
                branch(args[1]);
                break;
            case "rm-branch":
                rm_branch(args[1]);
                break;
            case "reset":
                reset(args[1]);
                break;
            case "merge":
                merge(args[1]);
                break;
            default:
                exitWithError(String.format("Unknown command: %s", args[0]));
        }
    }
}
