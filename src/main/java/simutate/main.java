/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simutate;

/**
 *
 * @author aayush.garg
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //debug
//            /*
//            args = new String[]{data.strGetFailingTests, "nmt"};
            args = new String[]{data.strProcessSubsumingMutantDiff, "nmt"};
//             */
            if (args.length < 1) {
                System.out.println("please pass below as arguments and try again");
                System.out.println("1. a task to perform (e.g. " + data.strAbstract + " / " + data.strUnabstract + " / " + data.strProcessSourcePatches
                        + " / " + data.strSimulate + " / " + data.strFlatten + " / " + " / " + data.strFlattenFixes + " / " + data.strGetAllTests + " / "
                        + data.strCompare + " / " + data.strCompareFixes + " / " + data.strGetFailingTests + " / " + data.strProcessLocationMapping + " / " + data.strProcessSubsumingMutantDiff + " )");
                System.out.println("NOTE: for task \"" + data.strSimulate + "\", please pass below as additional arguments and try again");
                System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                System.out.println("Additional 2. project name to perform simulation for (e.g. Cli)");
                System.out.println("and");
                System.out.println("Also for tasks \"" + data.strFlatten + "\", \"" + data.strFlattenFixes + "\", \"" + data.strProcessSourcePatches + "\", \""
                        + data.strGetAllTests + "\", \"" + data.strCompare + "\", \"" + data.strCompareFixes + "\", \"" + data.strGetFailingTests + "\", \"" + data.strProcessLocationMapping
                        + "\", and \"" + data.strProcessSubsumingMutantDiff
                        + "\", please pass below as additional arguments and try again");
                System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                System.out.println("");
                System.out.println("Optional parameters:");
                System.out.println("Optional parameters for task \"" + data.strSimulate + "\" -");
                System.out.println("Optional 1. bug id to perform simulation for (e.g. Cli_1 / Cli_2 / ...)");
                System.out.println("Optional parameters for task \"" + data.strFlatten + "\" and \"" + data.strFlattenFixes + "\" -");
                System.out.println("Optional 1. project name to perform flattening for (e.g. Cli)");
                System.out.println("Optional 2. bug id to perform flattening for (e.g. Cli_1 / Cli_2 / ...)");
                return;
            }
            controller objController = new controller();
            objController.init(args);
        } catch (Exception ex) {
            System.out.println("error at simutate.main.main()");
            ex.printStackTrace();
        }
    }

}
