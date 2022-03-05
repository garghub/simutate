/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simutate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author aayush.garg
 */
public class controller {

    util objUtil;
    LinkedList<String> lstProcessedFiles;
    String dirProject;

    void init(String[] args) throws Exception {
        try {
            String task = String.valueOf(args[0]);
            String technique;

            String projectName = null;
            String projectWithPatchId = null;
            switch (task) {
                case "abstract":
                    dirProject = data.dirSrc;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    RunAbstraction(dirProject);
                    break;
                case "unabstract":
                    dirProject = data.dirSrc;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    RunUnabstraction(dirProject);
                    break;
                case "processsourcepatches":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strProcessSourcePatches + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    dirProject = data.dirPatches;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    data.dirMutSrc = data.dirMutSrc + "-" + technique;
                    if (technique.equals("nmt")) {
                        data.strTechnique = technique;
                    }
                    ProcessSourcePatches(dirProject);
                    break;
                case "simulate":
                    if (args.length < 3) {
                        System.out.println("NOTE: for task \"" + data.strSimulate + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        System.out.println("Additional 2. project name to perform simulation for (e.g. Cli)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    data.dirMutSrc = data.dirMutSrc + "-" + technique;
                    dirProject = data.dirMutSrc;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    data.strProjectNameForSimulation = String.valueOf(args[2]);
                    data.dirSimulation = data.dirSimulation + "-" + technique;
                    if (args.length >= 4) {
                        data.dirBugId = String.valueOf(args[3]);
                    }

                    data.strSimulationFileName = "simulation-" + data.strProjectNameForSimulation;
                    if (data.dirBugId != null && data.dirBugId.isEmpty() == false) {
                        data.strSimulationFileName += "-" + data.dirBugId;
                    }
                    data.strSimulationFileName += ".txt";

                    PerformSimulation(dirProject);
                    break;
                case "flatten":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strFlatten + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);

                    if (args.length >= 3) {
                        projectName = String.valueOf(args[2]);
                    }

                    if (args.length >= 4) {
                        projectWithPatchId = String.valueOf(args[3]);
                    }

                    if (technique.equals("nmt")) {
                        data.strTechnique = technique;
                    }
                    data.dirMutSrc = data.dirMutSrc + "-" + technique;
                    data.dirSyntactic = data.dirSyntactic + "-" + technique;
                    dirProject = data.dirMutSrc;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    Flatten(dirProject, projectName, projectWithPatchId);
                    break;
                case "flattenfixes":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strFlattenFixes + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);

                    if (args.length >= 3) {
                        projectName = String.valueOf(args[2]);
                    }

                    if (args.length >= 4) {
                        projectWithPatchId = String.valueOf(args[3]);
                    }

                    if (technique.equals("nmt")) {
                        data.strTechnique = technique;
                    }
                    data.dirMutSrc = data.dirMutSrc + "-" + technique;
                    data.dirSyntacticFixes = data.dirSyntacticFixes + "-" + technique;
                    dirProject = data.dirMutSrc;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    FlattenFixes(dirProject, projectName, projectWithPatchId);
                    break;
                case "getalltests":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strGetAllTests + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    data.dirMutSrc = data.dirMutSrc + "-" + technique;
                    dirProject = data.dirMutSrc;// String.valueOf(args[0]);
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    data.dirSimulation = data.dirSimulation + "-" + technique;
                    data.dirAllTests = data.dirAllTests + "-" + technique;
                    GetAllTests(dirProject);
                    break;
                case "compare":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strCompare + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. simulation directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    data.dirSimulation = data.dirSimulation + "-" + technique;
                    dirProject = data.dirSimulation;
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    Compare(dirProject);
                    break;
                case "comparefixes":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strCompareFixes + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. simulation directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    data.dirSimulation = data.dirSimulation + "-" + technique;
                    dirProject = data.dirSimulation;
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    CompareFixes(dirProject);
                    break;
                case "processlocationmapping":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strProcessLocationMapping + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    data.dirMutSrc = data.dirMutSrc + "-" + technique;
                    dirProject = data.dirMutSrc;
                    objUtil = new util(dirProject);
                    ProcessLocationMapping(dirProject);
                    break;
                case "getfailingtests":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strGetFailingTests + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. simulation directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    data.dirSimulation = data.dirSimulation + "-" + technique;
                    dirProject = data.dirSimulation;
                    objUtil = new util(dirProject);
                    data.dirSrcMLBatchFile = dirProject;
                    GetFailingTests(dirProject);
                    break;
                case "processsubsumingmutantdiff":
                    if (args.length < 2) {
                        System.out.println("NOTE: for task \"" + data.strProcessSubsumingMutantDiff + "\", please pass below as additional arguments and try again");
                        System.out.println("Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)");
                        break;
                    }
                    technique = String.valueOf(args[1]);
                    ProcessMutationOperators(technique);
                    break;
                default:
                    System.out.println("wrong choice of task. available choices : " + data.strAbstract + " / " + data.strUnabstract
                            + " / " + data.strProcessSourcePatches + " / " + data.strSimulate + " / " + data.strFlatten + " / " + data.strGetAllTests);
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.init()");
            throw ex;
        }
    }

    void RunAbstraction(String dirProject) throws Exception {
        try {
            //String dirSrcCode;
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + "is not a valid project directory path!");
                return;
            }
            //dirSrcCode = dirProject + "/" + data.strDirSrcCode;
            //if (!objUtil.FileExists(dirSrcCode)) {
            //    throw new Exception(dirSrcCode + "is not a valid project source code directory path!");
            //}
            //if (lstProcessedFiles == null) {
            //    lstProcessedFiles = objUtil.GetProcessedFiles();
            //}
            traverse(dirProject);
            objUtil.WriteListToFile(dirProject, data.strLhsFileName, objUtil.lstAbsFns);
            objUtil.WriteListToFile(dirProject, data.strLhsLocsFileName, objUtil.lstAbsFnLocs);
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.RunAbstraction()");
            throw ex;
        }
    }

    void traverse(String dirSrcCode) throws Exception {
        try {
            File folderSrcCode = new File(dirSrcCode);
            for (File fileInside : folderSrcCode.listFiles()) {
                if (fileInside.isDirectory()) {
                    traverse(fileInside.getPath());
                } else if (fileInside.getName().matches(data.strExtensionCheck)) //        && lstProcessedFiles.contains(fileInside.getPath()) == false) 
                {
                    process(fileInside.getPath());

                    //lstProcessedFiles = objUtil.GetProcessedFiles();
                    //lstProcessedFiles.add(fileInside.getPath());
                    //objUtil.UpdateProcessedFiles(lstProcessedFiles);
                } else {
                    System.out.println(fileInside.getPath() + " is not a " + data.strSupportedLangExt + " file.");
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.traverse()");
            throw ex;
        }
    }

    void process(String strCodeFilePath) throws Exception {
        try {
            objUtil.ProcessClassFile(strCodeFilePath);
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.process()");
            throw ex;
        }
    }

    void RunUnabstraction(String dirProject) throws Exception {
        try {
            objUtil.lstMutatedAbsFns = objUtil.ReadFileToList(dirProject + "/" + data.strGenRhsFileName);
            if (objUtil.lstMutatedAbsFns == null || objUtil.lstMutatedAbsFns.isEmpty()) {
                System.out.println("generated file is either missing or is empty!");
                return;
            }
            objUtil.lstAbsFns = objUtil.ReadFileToList(dirProject + "/" + data.strLhsFileName);
            objUtil.lstAbsFnLocs = objUtil.ReadFileToList(dirProject + "/" + data.strLhsLocsFileName);
            for (int i = 0; i < objUtil.lstAbsFnLocs.size(); i++) {
                String strAbsFnLoc = objUtil.lstAbsFnLocs.get(i);
                String strAbsFn = objUtil.lstAbsFns.get(i);
                String strMutatedAbsFn = objUtil.lstMutatedAbsFns.get(i);
                if (!objUtil.FileExists(strAbsFnLoc)) {
                    continue;
                }
                Unabstract(strAbsFnLoc, strMutatedAbsFn);
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.RunUnabstraction()");
            throw ex;
        }
    }

    private void Unabstract(String strAbsFnLoc, String strMutatedAbsFn) throws Exception {
        try {
            System.err.println("proceeding to unabstract " + strMutatedAbsFn);
            String strMutatedFn = strMutatedAbsFn;
            File folderAbsFnLoc = new File(strAbsFnLoc);
            File fileParent = folderAbsFnLoc.getParentFile();
            String dirParent = fileParent.getPath();
            String strParentName = fileParent.getName();
            File fileGrandParent = fileParent.getParentFile();
            String dirGrandParent = fileGrandParent.getPath();
            dirGrandParent = dirGrandParent.replace("\\", "/").replace(dirProject, dirProject + data.strMutants);
            String dirMutants = dirGrandParent + "/" + strParentName + data.strMutants;
            String strMutatedClass = null;
            String strFnSig = null;
            String strMapFilePath = dirMutants + "/" + data.strMapFileName;
            for (File file : folderAbsFnLoc.listFiles()) {
                if (file.getName().matches(data.strMapExtensionCheck)) {
                    LinkedList<String> lstMap = objUtil.ReadFileToList(file.getPath());
                    HashMap<String, String> map = GetMappingFromList(lstMap);
                    //strAbstractedName, strActualName
                    for (String strAbstractedName : map.keySet()) {
                        String strActualName = map.get(strAbstractedName);
                        strMutatedFn = strMutatedFn.replace(strAbstractedName, strActualName);
                    }
                    break;
                }
            }
            for (File file : folderAbsFnLoc.listFiles()) {
                if (file.getName().matches(data.strExtensionCheck)) {
                    String fnFileName = file.getName().replace(data.strAbs + data.strSupportedLangExt, data.strSupportedLangExt);
                    String strClassFileName = strParentName + data.strSupportedLangExt;
                    String fnFilePath = dirParent + "/" + fnFileName;
                    LinkedList<String> lstOrigFn = objUtil.ReadFileToList(fnFilePath);
                    strFnSig = objUtil.GetMethodNameWithSignatures(lstOrigFn);
                    String strOrigFn = objUtil.ConvertListToString(lstOrigFn).trim();
                    String classFilePath = dirParent + "/" + strClassFileName;
                    LinkedList<String> lstOrigClass = objUtil.ReadFileToList(classFilePath);
                    String strOrigClass = objUtil.ConvertListToString(lstOrigClass);
                    strMutatedClass = strOrigClass;
                    if (strMutatedClass.contains(strOrigFn)) {
                        strMutatedClass = strMutatedClass.replace(strOrigFn, strMutatedFn);
                    }
                    break;
                }
            }
            int i = 1;
            String strMutantName = strParentName + "_" + i + data.strSupportedLangExt;
            if (objUtil.FileExists(dirMutants + "/" + strMutantName)) {
                Boolean foundNum = false;
                while (foundNum == false) {
                    i++;
                    strMutantName = strParentName + "_" + i + data.strSupportedLangExt;
                    if (!objUtil.FileExists(dirMutants + "/" + strMutantName)) {
                        foundNum = true;
                    }
                }
            }
            String[] arrMutatedClass = strMutatedClass.split(Pattern.quote("\\r\\n"));
            LinkedList<String> lstMutatedClass = new LinkedList();
            for (String str : arrMutatedClass) {
                lstMutatedClass.add(str);
            }
            objUtil.WriteListToFile(dirMutants, strMutantName, lstMutatedClass);

            LinkedList<String> lstMap = new LinkedList();
            if (objUtil.FileExists(strMapFilePath)) {
                lstMap = objUtil.ReadFileToList(strMapFilePath);
            }
            lstMap.add(strMutantName + data.strPipe + strFnSig);
            objUtil.DeleteFile(strMapFilePath);
            objUtil.WriteListToFile(dirMutants, data.strMapFileName, lstMap);
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.Unabstract()");
            throw ex;
        }
    }

    private HashMap<String, String> GetMappingFromList(LinkedList<String> lstMap) throws Exception {
        try {
            HashMap<String, String> map = new HashMap();
            int i = 0;
            while (i < lstMap.size()) {
                String actualNames, abstractedNames;
                actualNames = lstMap.get(i);
                if ((i + 1) < lstMap.size()) {
                    abstractedNames = lstMap.get(i + 1);
                } else {
                    i = i + 2;
                    continue;
                }
                if (actualNames.isEmpty()) {
                    i = i + 2;
                    continue;
                }
                if (actualNames != null && abstractedNames != null) {
                    String[] arrActualNames = actualNames.split(Pattern.quote(","));
                    String[] arrAbstractedNames = abstractedNames.split(Pattern.quote(","));
                    for (int j = 0; j < arrActualNames.length; j++) {
                        if (j >= arrAbstractedNames.length) {
                            System.out.println("Uneven actual and abstract names mapping produced by the model");
                            System.out.println("arrActualNames: " + arrActualNames);
                            System.out.println("arrAbstractedNames: " + arrAbstractedNames);
                            break;
                        }
                        String strActualName = arrActualNames[j];
                        String strAbstractedName = arrAbstractedNames[j];
                        if (strActualName.isEmpty() || strAbstractedName.isEmpty()) {
                            continue;
                        }
                        map.put(strAbstractedName, strActualName);
                    }
                }
                i = i + 2;
            }
            return map;
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.GetMappingFromList()");
            throw ex;
        }
    }

    void ProcessSourcePatches(String dirProject) throws Exception {
        try {
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + " does not exist.");
                return;
            }
            if (!objUtil.FileExists(data.dirBuggySrc)) {
                System.out.println(data.dirBuggySrc + " does not exist.");
                return;
            }
            if (!objUtil.FileExists(data.dirMutSrc)) {
                System.out.println(data.dirMutSrc + " does not exist.");
                return;
            }
            if (objUtil.FileExists(data.dirMutSrc + "/" + data.strPatchFnMap)) {
                System.out.println(data.dirMutSrc + "/" + data.strPatchFnMap + " already exists, please delete and try again.");
                return;
            }
            traversePatchDir(dirProject);
            objUtil.WriteListToFile(data.dirMutSrc, data.strPatchFnMap, objUtil.lstDiffMappedToFn);
            System.out.println(data.dirMutSrc + "/" + data.strPatchFnMap + " has been written.");
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.ProcessSourcePatches()");
            throw ex;
        }
    }

    void traversePatchDir(String dirSrcCode) throws Exception {
        try {
            File folderSrcCode = new File(dirSrcCode);
            for (File fileInside : folderSrcCode.listFiles()) {
                if (!fileInside.isDirectory()) {
                    continue;
                }
                String projectName = fileInside.getName();
                for (File fileInsideProject : fileInside.listFiles()) {
                    String strPatchFilePath = fileInsideProject.getPath();
                    System.out.println("processing " + strPatchFilePath);
                    String patchFileName = fileInsideProject.getName();
                    if (!patchFileName.matches(data.strSrcPatchExtCheck)) {
                        continue;
                    }
                    String patchId = patchFileName.replace(data.strSrcPatchExt, "");
                    String strPrjWithPatchId = projectName + "_" + patchId;
                    String dirPrjSrc = data.dirBuggySrc + "/" + strPrjWithPatchId;
                    if (!objUtil.FileExists(dirPrjSrc)) {
                        continue;
                    }
                    LinkedList<String> lstAllPatches = objUtil.ReadFileToList(strPatchFilePath);
                    if (lstAllPatches == null || lstAllPatches.isEmpty()) {
                        continue;
                    }
                    Integer patchCount = 1;
                    HashMap<Integer, LinkedList<String>> lstMultipleFilePatches = new HashMap();
                    LinkedList<String> lstInternalPatch = new LinkedList();
                    for (int i = 0; i < lstAllPatches.size(); i++) {
                        String str = lstAllPatches.get(i);
                        if (str.contains(data.strToLookInPatchForFileLocation)) {
                            if (!lstInternalPatch.isEmpty()) {
                                lstMultipleFilePatches.put(patchCount, lstInternalPatch);
                                patchCount++;
                                lstInternalPatch = new LinkedList();
                            }
                        }
                        lstInternalPatch.add(str);
                        if ((i + 1) == lstAllPatches.size()) {
                            if (!lstInternalPatch.isEmpty()) {
                                lstMultipleFilePatches.put(patchCount, lstInternalPatch);
                                patchCount++;
                                lstInternalPatch = new LinkedList();
                            }
                        }
                    }

                    System.out.println("patch includes changes in " + lstMultipleFilePatches.size() + " files.");
                    Integer filePatchSuccess = 0;
                    for (Integer patchNum : lstMultipleFilePatches.keySet()) {
                        LinkedList<String> lstPatch = lstMultipleFilePatches.get(patchNum);
                        String patchLine01 = lstPatch.get(0);
                        String[] arrPatchLine01 = patchLine01.split(Pattern.quote(" "));
                        String srcFilePath = null;
                        for (String strInitialSemiDirOriginal : data.lstInitialSemiDirOriginal) {
                            //for matching with fixed files as done earlier
                            //srcFilePath = dirPrjSrc + arrPatchLine01[arrPatchLine01.length - 1].replace("b" + strInitialSemiDirOriginal, "/");
                            srcFilePath = dirPrjSrc + arrPatchLine01[2].replace("a" + strInitialSemiDirOriginal, "/");
                            if (objUtil.FileExists(srcFilePath)) {
                                break;
                            } else {
                                srcFilePath = null;
                            }
                        }
                        if (srcFilePath == null) {
                            System.out.println(srcFilePath + "does not exist!");
                            continue;
                        }
                        //HashMap<String, LinkedList<String>> mapPathWithSrcCode = objUtil.TraverseToGetSrcCode(dirPrjSrc, srcFilePath);
                        HashMap<String, LinkedList<String>> mapPathWithSrcCode = new HashMap();
                        LinkedList<String> lstBuggyFile = objUtil.ReadFileToList(srcFilePath);
                        if (lstBuggyFile == null || lstBuggyFile.isEmpty()) {
                            continue;
                        }
                        mapPathWithSrcCode.put(srcFilePath, lstBuggyFile);
                        if (mapPathWithSrcCode == null || mapPathWithSrcCode.isEmpty()) {
                            continue;
                        }

                        HashMap<String, LinkedList<Integer>> mapFnStartEnd = objUtil.GetFunctionsInBuggyFileStartEnd(srcFilePath, lstBuggyFile);

                        String strSrcPath = null;
                        LinkedList<String> lstSrc = null;
                        for (String key : mapPathWithSrcCode.keySet()) {
                            strSrcPath = key;
                            lstSrc = mapPathWithSrcCode.get(key);
                            break;
                        }
                        if (strSrcPath == null || strSrcPath.isEmpty() || lstSrc == null || lstSrc.isEmpty()) {
                            continue;
                        }
                        LinkedList<HashMap<Integer, Integer>> lstFirstMinusAndPlus = objUtil.GetListOfFirstMinusAndPlus(lstPatch);
                        if (lstFirstMinusAndPlus == null || lstFirstMinusAndPlus.isEmpty()) {
                            continue;
                        }

                        String strMapFilePath = strSrcPath.replace(data.dirBuggySrc, data.dirMutSrc).replace(data.strSupportedLangExt, data.strMutants) + "/" + data.strMapFileName;
                        if (objUtil.FileExists(strMapFilePath) == false) {
                            String firstHalfMapFilePath = data.dirMutSrc + "/" + strPrjWithPatchId;
                            String otherHalfMapFilePath = strMapFilePath.replace(firstHalfMapFilePath + "/", "");
                            strMapFilePath = "";
                            for (String strInitialSemiDirOriginal : data.lstInitialSemiDirOriginal) {
                                strMapFilePath = firstHalfMapFilePath + strInitialSemiDirOriginal + otherHalfMapFilePath;
                                if (objUtil.FileExists(strMapFilePath)) {
                                    break;
                                }
                                strMapFilePath = "";
                            }
                        }
                        if (strMapFilePath.isEmpty()) {
                            continue;
                        }
                        HashMap<String, String> mapMutantsWithFns = objUtil.GetMutantsWithFnsFromMapFile(strMapFilePath);
                        if (mapMutantsWithFns == null || mapMutantsWithFns.isEmpty()) {
                            continue;
                        }

                        //Below was used when I didnt had funtion begin and end line numbers, got inaccurate mapping
                        //Integer localSuccess = FindFunctionNameAndAddToList(strPrjWithPatchId, lstFirstMinusAndPlus, lstSrc, mapMutantsWithFns);
                        //
                        Integer localSuccess = FindFunctionNameUsingBeginEndAndAddToList(strPrjWithPatchId, lstFirstMinusAndPlus, mapFnStartEnd, mapMutantsWithFns);
                        if (localSuccess > 0) {
                            filePatchSuccess++;
                        }
                        //debugging
                        /*
                        for(String fnSig: objUtil.lstDiffMappedToFn){
                            System.out.println(fnSig);
                        }
                         */
                        System.out.println("processed a patch.");
                    }
                    System.out.println("processed " + filePatchSuccess + ".");
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.traversePatchDir()");
            throw ex;
        }
    }

    //Not used
    private Integer FindFunctionNameAndAddToList(String strPrjWithPatchId, LinkedList<HashMap<Integer, Integer>> lstFirstMinusAndPlus, LinkedList<String> lstSrc, HashMap<String, String> mapAvailableFns) {
        try {
            Integer count = 0;
            for (HashMap<Integer, Integer> mapFirstMinusAndPlus : lstFirstMinusAndPlus) {
                for (Integer firstMinus : mapFirstMinusAndPlus.keySet()) {
                    Integer firstPlus = mapFirstMinusAndPlus.get(firstMinus);

                    if (firstMinus == 0) {
                        firstMinus = firstPlus;
                    }
                    if (firstMinus == 0) {
                        continue;
                    }
                    Integer index = firstMinus - 1;
                    Boolean success = objUtil.FindFunctionNameAndAddToList(strPrjWithPatchId, index, lstSrc, mapAvailableFns);
                    if (success) {
                        count++;
                    }
                }
            }
            return count;
        } catch (Exception ex) {
            System.out.println("error at controller.FindFunctionNameAndAddToList()");
            ex.printStackTrace();
            return 0;
        }
    }

    private Integer FindFunctionNameUsingBeginEndAndAddToList(String strPrjWithPatchId, LinkedList<HashMap<Integer, Integer>> lstFirstMinusAndPlus,
            HashMap<String, LinkedList<Integer>> mapFnStartEnd, HashMap<String, String> mapMutantsWithFns) {
        try {
            Integer count = 0;
            for (HashMap<Integer, Integer> mapFirstMinusAndPlus : lstFirstMinusAndPlus) {
                for (Integer firstMinus : mapFirstMinusAndPlus.keySet()) {
                    Integer firstPlus = mapFirstMinusAndPlus.get(firstMinus);

                    if (firstMinus == 0) {
                        firstMinus = firstPlus;
                    }
                    if (firstMinus == 0) {
                        continue;
                    }
                    Boolean success = objUtil.FindFunctionNameUsingBeginEndAndAddToList(strPrjWithPatchId, firstMinus, mapFnStartEnd, mapMutantsWithFns);
                    if (success) {
                        count++;
                    }
                }
            }
            return count;
        } catch (Exception ex) {
            System.out.println("error at controller.FindFunctionNameUsingBeginEndAndAddToList()");
            ex.printStackTrace();
            return 0;
        }
    }

    void PerformSimulation(String dirProject) throws Exception {
        try {
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + "is not a valid mutants directory path!");
                return;
            }
            objUtil.InitializeOrUpdateSimulationResults(data.strSimulationListLoad);
            traverseMutantsDir(dirProject);
        } catch (Exception ex) {
            System.out.println("error at controller.PerformSimulation()");
            throw ex;
        }
    }

    void traverseMutantsDir(String dirSrcCode) throws Exception {
        try {
            File folderSrcCode = new File(dirSrcCode);
            for (File fileInside : folderSrcCode.listFiles()) {
                if (!fileInside.isDirectory()) {
                    continue;
                }
                String strPrjWithPatchId = fileInside.getName();
                if (!strPrjWithPatchId.contains("_")) {
                    System.out.println("skipping " + strPrjWithPatchId);
                    continue;
                }
                if (data.dirBugId != null && data.dirBugId.isEmpty() == false) {
                    if (!strPrjWithPatchId.equals(data.dirBugId)) {
                        System.out.println("skipping " + strPrjWithPatchId);
                        continue;
                    }
                }

                System.out.println("processing " + strPrjWithPatchId);
                String[] arrPrjWithPatchId = strPrjWithPatchId.split(Pattern.quote("_"));
                String projectName = arrPrjWithPatchId[0];
                if (!data.strProjectNameForSimulation.equals(data.strAllProjectsForSimulation)) {
                    //if (!(projectName.equals(data.strProjectNameForSimulation) && data.lstProjects.contains(projectName))) {
                    if (!projectName.equals(data.strProjectNameForSimulation)) {
                        System.out.println("skipping " + strPrjWithPatchId);
                        continue;
                    }
                }
                String patchId = arrPrjWithPatchId[1];
                String dirPrjSim = data.dirSimulation + "/" + strPrjWithPatchId;

                String dirPrjBuggy = dirPrjSim + "/" + data.strBuggy;
                String dirPrjFixed = dirPrjSim + "/" + data.strFixed;
                Boolean success;

                //downloading bug
                if (objUtil.FileExists(dirPrjBuggy)) {
                    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirPrjBuggy, null);
                }
                success = objUtil.GetDefects4jExecutionSuccess(dirPrjBuggy, data.strBuggy, projectName, patchId);
                if (!success) {
                    continue;
                }
                //downloading fix
                if (objUtil.FileExists(dirPrjFixed)) {
                    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirPrjFixed, null);
                }
                success = objUtil.GetDefects4jExecutionSuccess(dirPrjFixed, data.strFixed, projectName, patchId);
                if (!success) {
                    continue;
                }
                objUtil.PerformSimulationForBug(strPrjWithPatchId, dirPrjBuggy);
                objUtil.PerformSimulation(projectName, patchId, fileInside);
                objUtil.InitializeOrUpdateSimulationResults(data.strSimulationListSave);
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.traverseMutantsDir()");
            throw ex;
        }
    }

    void Flatten(String dirProject, String projectName, String projectWithPatchId) throws Exception {
        try {
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + " does not exist.");
                return;
            }
            if (!objUtil.FileExists(data.dirSimulationForBugsOrFixes)) {
                System.out.println(data.dirSimulationForBugsOrFixes + " does not exist.");
                return;
            }

            File folderMain = new File(dirProject);
            for (File folderProject : folderMain.listFiles()) {
                if (!folderProject.isDirectory()) {
                    continue;
                }

                String strProjectWithPatchId = folderProject.getName();
                String[] arrPrjWithPatchId = strProjectWithPatchId.split(Pattern.quote("_"));
                String strProjectName = arrPrjWithPatchId[0];
                if (projectName != null && projectName.trim().isEmpty() == false) {
                    if (strProjectName.equals(projectName) == false) {
                        continue;
                    }
                }
                if (projectWithPatchId != null && projectWithPatchId.trim().isEmpty() == false) {
                    if (strProjectWithPatchId.equals(projectWithPatchId) == false) {
                        continue;
                    }
                }
                String dirProjectWithPatchId = folderProject.getPath().replace("\\", "/");
                System.out.println("flattening " + dirProjectWithPatchId);
                String dirProjectWithPatchIdSyntactic = data.dirSyntactic + "/" + strProjectWithPatchId;
                data.dirSrcMLBatchFile = dirProjectWithPatchIdSyntactic;
                if (objUtil.FileExists(dirProjectWithPatchIdSyntactic + "/" + data.strFlatteningMapFileName)) {
                    continue;
                }

                objUtil.lstFlatteningMap = new LinkedList();
                objUtil.lstFlattenedMutatedFns = new LinkedList();
                objUtil.lstFlattenedBuggyOrFixedFns = new LinkedList();
                TraverseForFlattening(strProjectWithPatchId, dirProjectWithPatchId, data.strBuggy);

                objUtil.WriteListToFile(dirProjectWithPatchIdSyntactic, data.strFlatteningMapFileName, objUtil.lstFlatteningMap);
                objUtil.WriteListToFile(dirProjectWithPatchIdSyntactic, data.strFlattenedMutatedFnsFileName, objUtil.lstFlattenedMutatedFns);
                objUtil.WriteListToFile(dirProjectWithPatchIdSyntactic, data.strFlattenedBuggyFnsFileName, objUtil.lstFlattenedBuggyOrFixedFns);
                System.out.println("will continue processing within 10 secs...");
                Thread.sleep(10 * 1000);
            }
        } catch (Exception ex) {
            System.out.println("error at controller.Flatten()");
            throw ex;
        }
    }

    void TraverseForFlattening(String strProjectWithPatchId, String dirMutants, String strBuggyOrFixed) throws Exception {
        try {
            File folderMutants = new File(dirMutants);
            for (File fileInside : folderMutants.listFiles()) {
                if (fileInside.isDirectory()) {
                    TraverseForFlattening(strProjectWithPatchId, fileInside.getPath(), strBuggyOrFixed);
                } else if (fileInside.getName().equals(data.strMapFileName)) {
                    String dirMap = fileInside.getPath().replace("\\", "/");
                    LinkedList<String> lstMap = objUtil.ReadFileToList(dirMap);
                    if (lstMap == null || lstMap.isEmpty()) {
                        continue;
                    }
                    String dirParent = fileInside.getParent().replace("\\", "/");
                    File folderParent = new File(dirParent);
                    String strParentName = folderParent.getName();
                    String strBuggyOrFixedFileName = strParentName.replace(data.strMutants, "") + data.strSupportedLangExt;
                    String strSemiPathToBuggyOrFixed = dirParent.replace(dirProject + "/" + strProjectWithPatchId + "/", "").replace("/" + strParentName, "");
                    String dirBuggyOrFixedFile = "";
                    dirBuggyOrFixedFile = data.dirSimulationForBugsOrFixes + "/" + strProjectWithPatchId + "/" + strBuggyOrFixed + "/" + strSemiPathToBuggyOrFixed + "/" + strBuggyOrFixedFileName;
                    if (!objUtil.FileExists(dirBuggyOrFixedFile)) {
                        for (String strInitialSemiDirOriginal : data.lstInitialSemiDirOriginal) {
                            dirBuggyOrFixedFile = data.dirSimulationForBugsOrFixes + "/" + strProjectWithPatchId + "/" + strBuggyOrFixed + strInitialSemiDirOriginal + strSemiPathToBuggyOrFixed + "/" + strBuggyOrFixedFileName;
                            if (objUtil.FileExists(dirBuggyOrFixedFile)) {
                                break;
                            }
                            dirBuggyOrFixedFile = "";
                        }
                    }
                    if (dirBuggyOrFixedFile == null || dirBuggyOrFixedFile.isEmpty()) {
                        continue;
                    }

                    //Was done earlier to get all functions
                    /*
                    HashMap<String, String> mapFlattenedBuggyFns = objUtil.GetAllFlattenedFns(dirBuggyFile, lstMap);
                    if (mapFlattenedBuggyFns == null || mapFlattenedBuggyFns.isEmpty()) {
                        continue;
                    }
                     */
                    String strFlattenedFile = objUtil.GetFlattenedFile(dirBuggyOrFixedFile);
                    if (strFlattenedFile == null || strFlattenedFile.isEmpty()) {
                        continue;
                    }

                    for (String strMap : lstMap) {
                        if (strMap == null || strMap.trim().isEmpty()) {
                            continue;
                        }
                        String[] arrMap = strMap.split(Pattern.quote(data.strPipe));
                        if (arrMap.length < 2) {
                            continue;
                        }
                        String strMutantFileName = arrMap[0];
                        String strFnPhrase = arrMap[1];
                        String dirMutant = dirParent + "/" + strMutantFileName;
                        if (!objUtil.FileExists(dirMutant)) {
                            continue;
                        }
                        String strMapStringToBeWritten = strMap + data.strPipe + strSemiPathToBuggyOrFixed + data.strPipe + strBuggyOrFixedFileName;
                        String strFlattenedMutatedFile = objUtil.GetFlattenedFile(dirMutant);
                        String strFlattenedBuggyOrFixedFile = strFlattenedFile; //mapFlattenedBuggyFns.get(strFnPhrase);
                        if (strMapStringToBeWritten == null || strMapStringToBeWritten.isEmpty()
                                || strFlattenedMutatedFile == null || strFlattenedMutatedFile.isEmpty()
                                || strFlattenedBuggyOrFixedFile == null || strFlattenedBuggyOrFixedFile.isEmpty()) {
                            continue;
                        }
                        objUtil.lstFlatteningMap.add(strMapStringToBeWritten);
                        objUtil.lstFlattenedMutatedFns.add(strFlattenedMutatedFile);
                        objUtil.lstFlattenedBuggyOrFixedFns.add(strFlattenedBuggyOrFixedFile);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("error at controller.TraverseForFlattening()");
            throw ex;
        }
    }

    void GetAllTests(String dirProject) throws Exception {
        try {
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + "is not a valid mutants directory path!");
                return;
            }
            if (!objUtil.FileExists(data.dirSimulation)) {
                System.out.println(data.dirSimulation + "is not a valid mutants directory path!");
                return;
            }
            objUtil.lstSimulation = new LinkedList();
            traverseMutantsDirToGetAllTests(dirProject);
        } catch (Exception ex) {
            System.out.println("error at controller.GetAllTests()");
            throw ex;
        }
    }

    void traverseMutantsDirToGetAllTests(String dirSrcCode) throws Exception {
        try {
            File folderSrcCode = new File(dirSrcCode);
            for (File fileInside : folderSrcCode.listFiles()) {
                if (!fileInside.isDirectory()) {
                    continue;
                }
                String strPrjWithPatchId = fileInside.getName();
                if (!strPrjWithPatchId.contains("_")) {
                    System.out.println("skipping " + strPrjWithPatchId);
                    continue;
                }
                System.out.println("processing " + strPrjWithPatchId);
                String[] arrPrjWithPatchId = strPrjWithPatchId.split(Pattern.quote("_"));
                String projectName = arrPrjWithPatchId[0];
                String patchId = arrPrjWithPatchId[1];
                String dirPrjSim = data.dirSimulation + "/" + strPrjWithPatchId;

                String dirPrjBuggy = dirPrjSim + "/" + data.strBuggy;

                //downloading bug
                if (objUtil.FileExists(dirPrjBuggy)) {
                    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirPrjBuggy, null);
                }
                Boolean success = objUtil.GetDefects4jExecutionSuccess(dirPrjBuggy, data.strBuggy, projectName, patchId);
                if (!success) {
                    continue;
                }
                //running simulation for bug without writing test output
                objUtil.CompileAndTest(strPrjWithPatchId, dirPrjBuggy, null);
                //checking if all_tests file has been generated
                if (objUtil.FileExists(data.dirSimulation + "/" + strPrjWithPatchId + "/" + data.strBuggy + "/" + data.strAllTestsFileName)) {
                    LinkedList<String> lstAllTests = objUtil.ReadFileToList(data.dirSimulation + "/" + strPrjWithPatchId + "/" + data.strBuggy + "/" + data.strAllTestsFileName);
                    //saving it
                    objUtil.WriteListToFile(data.dirAllTests + "/" + strPrjWithPatchId, data.strBuggy + data.strAllTestPartialFileName, lstAllTests);
                    System.out.println("all tests file written to " + data.dirAllTests + "/" + strPrjWithPatchId + "/" + data.strBuggy + data.strAllTestPartialFileName);
                } else {
                    System.out.println("file not available at " + data.dirSimulation + "/" + strPrjWithPatchId + "/" + data.strBuggy + "/" + data.strAllTestsFileName);
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.traverseMutantsDirToGetAllTests()");
            throw ex;
        }
    }

    private void Compare(String dirProject) throws Exception {
        try {
            File folderSimulation = new File(dirProject);
            LinkedList<String> lstOverallSemanticSimilarity = new LinkedList();
            for (File folderProjectWithPatchId : folderSimulation.listFiles()) {
                LinkedList<String> lstSemanticSimilarity = new LinkedList();
                String strProjectWithPatchId = folderProjectWithPatchId.getName();
                String dirProjectWithPatchId = folderProjectWithPatchId.getPath().replace("\\", "/");
                String strBugSimulationFileName = data.strBuggy + data.strTestPartialFileName;
                String strBugSimulationFilePath = dirProjectWithPatchId + "/" + strBugSimulationFileName;
                if (!objUtil.FileExists(strBugSimulationFilePath)) {
                    continue;
                }
                Boolean bugOutputAvailable = true;
                Set<String> brokenTestsByBug = new HashSet();
                LinkedList<String> lstBuggySimulation = objUtil.ReadFileToList(strBugSimulationFilePath);
                if (lstBuggySimulation == null || lstBuggySimulation.isEmpty()) {
                    bugOutputAvailable = false;
                }
                if (bugOutputAvailable) {
                    String strFailingTestsSentence = lstBuggySimulation.remove(0);
                    if (lstBuggySimulation == null || lstBuggySimulation.isEmpty()) {
                        bugOutputAvailable = false;
                    }
                }
                if (bugOutputAvailable) {
                    brokenTestsByBug = new HashSet<String>(lstBuggySimulation);
                }
                for (File fileMutantSimulation : folderProjectWithPatchId.listFiles()) {
                    Double ochiaiScore = -1.0;
                    Boolean mutantOutputAvailable = true;
                    String strMutantSimulationFileName = fileMutantSimulation.getName();
                    String strMutantSimulationFilePath = fileMutantSimulation.getPath();
                    if (!strMutantSimulationFileName.matches(data.strTxtExtensionCheck)) {
                        continue;
                    }
                    if (strMutantSimulationFileName.equals(strBugSimulationFileName)) {
                        continue;
                    }
                    LinkedList<String> lstMutantSimulation = objUtil.ReadFileToList(strMutantSimulationFilePath);
                    if (lstMutantSimulation == null || lstMutantSimulation.isEmpty()) {
                        mutantOutputAvailable = false;
                    }
                    if (mutantOutputAvailable) {
                        String strFailingTestsSentence = lstMutantSimulation.remove(0);
                        if (lstMutantSimulation == null || lstMutantSimulation.isEmpty()) {
                            mutantOutputAvailable = false;
                        }
                        if (bugOutputAvailable && mutantOutputAvailable) {
                            ochiaiScore = objUtil.calculateOchiai(brokenTestsByBug, lstMutantSimulation);
                        }
                    }
                    String strMutantFileName = strMutantSimulationFileName.replace(data.strTestPartialFileName, data.strSupportedLangExt);
                    String strToAdd = strProjectWithPatchId + data.strPipe + strMutantFileName + data.strPipe + data.strOchiai + data.strColonSpace + ochiaiScore;
                    lstSemanticSimilarity.add(strToAdd);
                    lstOverallSemanticSimilarity.add(strToAdd);
                    System.out.println(strToAdd);
                }
                String dirSemantic = dirProjectWithPatchId.replace(data.strSimulation, data.strSemantic);
                if (objUtil.FileExists(dirSemantic + "/" + data.strSemanticSimilarityFileName)) {
                    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirSemantic + "/" + data.strSemanticSimilarityFileName, null);
                }
                objUtil.WriteListToFile(dirSemantic, data.strSemanticSimilarityFileName, lstSemanticSimilarity);
            }
            String dirOverallSemantic = dirProject.replace(data.strSimulation, data.strSemantic);
            if (objUtil.FileExists(dirOverallSemantic + "/" + data.strOverallSemanticSimilarityFileName)) {
                objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOverallSemantic + "/" + data.strOverallSemanticSimilarityFileName, null);
            }
            objUtil.WriteListToFile(dirOverallSemantic, data.strOverallSemanticSimilarityFileName, lstOverallSemanticSimilarity);
        } catch (Exception ex) {
            System.out.println("error at controller.Compare()");
            throw ex;
        }
    }

    void ProcessLocationMapping(String dirProject) throws Exception {
        try {
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + " does not exist.");
                return;
            }

            File folderMain = new File(dirProject);
            objUtil.lstLocationMap = new LinkedList();
            for (File folderProject : folderMain.listFiles()) {
                if (!folderProject.isDirectory()) {
                    continue;
                }

                String strProjectWithPatchId = folderProject.getName();
                String dirProjectWithPatchId = folderProject.getPath().replace("\\", "/");
                System.out.println("processing location mapping for " + dirProjectWithPatchId);
                TraverseForLocationMapping(strProjectWithPatchId, dirProjectWithPatchId);
            }
            if (objUtil.FileExists(dirProject + "/" + data.strLocationMapFileName)) {
                objUtil.DeleteFile(dirProject + "/" + data.strLocationMapFileName);
            }
            objUtil.WriteListToFile(dirProject, data.strLocationMapFileName, objUtil.lstLocationMap);
        } catch (Exception ex) {
            System.out.println("error at controller.ProcessLocationMapping()");
            throw ex;
        }
    }

    void TraverseForLocationMapping(String strProjectWithPatchId, String dirMutants) throws Exception {
        try {
            File folderMutants = new File(dirMutants);
            for (File fileInside : folderMutants.listFiles()) {
                if (fileInside.isDirectory()) {
                    TraverseForLocationMapping(strProjectWithPatchId, fileInside.getPath());
                } else if (fileInside.getName().equals(data.strMapFileName)) {
                    String dirMap = fileInside.getPath().replace("\\", "/");
                    LinkedList<String> lstMap = objUtil.ReadFileToList(dirMap);
                    if (lstMap == null || lstMap.isEmpty()) {
                        continue;
                    }

                    for (String strMap : lstMap) {
                        if (strMap == null || strMap.trim().isEmpty()) {
                            continue;
                        }
                        String[] arrMap = strMap.split(Pattern.quote(data.strPipe));
                        String strMutantFileName = arrMap[0];
                        String strFnPhrase = arrMap[1];
                        String strLocation = arrMap[arrMap.length - 1];
                        String strMapStringToBeWritten = strProjectWithPatchId + data.strPipe + strMutantFileName + data.strPipe + strFnPhrase + data.strPipe + strLocation;
                        if (strMapStringToBeWritten == null || strMapStringToBeWritten.isEmpty()) {
                            continue;
                        }
                        objUtil.lstLocationMap.add(strMapStringToBeWritten);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("error at controller.TraverseForLocationMapping()");
            throw ex;
        }
    }

    void FlattenFixes(String dirProject, String projectName, String projectWithPatchId) throws Exception {
        try {
            if (!objUtil.FileExists(dirProject)) {
                System.out.println(dirProject + " does not exist.");
                return;
            }
            if (!objUtil.FileExists(data.dirSimulationForBugsOrFixes)) {
                System.out.println(data.dirSimulationForBugsOrFixes + " does not exist.");
                return;
            }

            File folderMain = new File(dirProject);
            for (File folderProject : folderMain.listFiles()) {
                if (!folderProject.isDirectory()) {
                    continue;
                }

                String strProjectWithPatchId = folderProject.getName();
                String[] arrPrjWithPatchId = strProjectWithPatchId.split(Pattern.quote("_"));
                String strProjectName = arrPrjWithPatchId[0];
                if (projectName != null && projectName.trim().isEmpty() == false) {
                    if (strProjectName.equals(projectName) == false) {
                        continue;
                    }
                }
                if (projectWithPatchId != null && projectWithPatchId.trim().isEmpty() == false) {
                    if (strProjectWithPatchId.equals(projectWithPatchId) == false) {
                        continue;
                    }
                }
                String dirProjectWithPatchId = folderProject.getPath().replace("\\", "/");
                System.out.println("flattening " + dirProjectWithPatchId);
                String dirProjectWithPatchIdSyntactic = data.dirSyntacticFixes + "/" + strProjectWithPatchId;
                data.dirSrcMLBatchFile = dirProjectWithPatchIdSyntactic;
                if (objUtil.FileExists(dirProjectWithPatchIdSyntactic + "/" + data.strFlatteningMapFileName)) {
                    continue;
                }

                objUtil.lstFlatteningMap = new LinkedList();
                objUtil.lstFlattenedMutatedFns = new LinkedList();
                objUtil.lstFlattenedBuggyOrFixedFns = new LinkedList();
                TraverseForFlattening(strProjectWithPatchId, dirProjectWithPatchId, data.strFixed);

                objUtil.WriteListToFile(dirProjectWithPatchIdSyntactic, data.strFlatteningMapFileName, objUtil.lstFlatteningMap);
                objUtil.WriteListToFile(dirProjectWithPatchIdSyntactic, data.strFlattenedMutatedFnsFileName, objUtil.lstFlattenedMutatedFns);
                objUtil.WriteListToFile(dirProjectWithPatchIdSyntactic, data.strFlattenedFixedFnsFileName, objUtil.lstFlattenedBuggyOrFixedFns);
                System.out.println("will continue processing within 10 secs...");
                Thread.sleep(10 * 1000);
            }
        } catch (Exception ex) {
            System.out.println("error at controller.FlattenFixes()");
            throw ex;
        }
    }

    private void CompareFixes(String dirProject) throws Exception {
        try {
            File folderSimulation = new File(dirProject);
            LinkedList<String> lstOverallSemanticSimilarity = new LinkedList();
            for (File folderProjectWithPatchId : folderSimulation.listFiles()) {
                if (folderProjectWithPatchId.isDirectory() == false) {
                    continue;
                }
                LinkedList<String> lstSemanticSimilarity = new LinkedList();
                String strProjectWithPatchId = folderProjectWithPatchId.getName();
                String dirProjectWithPatchId = folderProjectWithPatchId.getPath().replace("\\", "/");
                String strBugSimulationFileName = data.strBuggy + data.strTestPartialFileName;
                for (File fileMutantSimulation : folderProjectWithPatchId.listFiles()) {
                    Integer ochiaiScore = -1;
                    Boolean mutantOutputAvailable = true;
                    String strMutantSimulationFileName = fileMutantSimulation.getName();
                    String strMutantSimulationFilePath = fileMutantSimulation.getPath();
                    if (!strMutantSimulationFileName.matches(data.strTxtExtensionCheck)) {
                        continue;
                    }
                    if (strMutantSimulationFileName.equals(strBugSimulationFileName)) {
                        continue;
                    }
                    LinkedList<String> lstMutantSimulation = objUtil.ReadFileToList(strMutantSimulationFilePath);
                    if (lstMutantSimulation == null || lstMutantSimulation.isEmpty()) {
                        mutantOutputAvailable = false;
                    }
                    if (mutantOutputAvailable) {
                        String strFailingTestsSentence = lstMutantSimulation.get(0);
                        if (strFailingTestsSentence.contains(data.strFailingTests + data.strColonSpace)) {
                            try {
                                ochiaiScore = Integer.parseInt(strFailingTestsSentence.replace(data.strFailingTests + data.strColonSpace, ""));
                            } catch (NumberFormatException nfex) {
                            }
                        }
                    }
                    String strMutantFileName = strMutantSimulationFileName.replace(data.strTestPartialFileName, data.strSupportedLangExt);
                    String strToAdd = strProjectWithPatchId + data.strPipe + strMutantFileName + data.strPipe + data.strOchiai + data.strColonSpace + ochiaiScore;
                    lstSemanticSimilarity.add(strToAdd);
                    lstOverallSemanticSimilarity.add(strToAdd);
                    System.out.println(strToAdd);
                }
                String dirSemanticFixes = dirProjectWithPatchId.replace(data.strSimulation, data.strSemanticFixes);
                if (objUtil.FileExists(dirSemanticFixes + "/" + data.strSemanticSimilarityFileName)) {
                    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirSemanticFixes + "/" + data.strSemanticSimilarityFileName, null);
                }
                objUtil.WriteListToFile(dirSemanticFixes, data.strSemanticSimilarityFileName, lstSemanticSimilarity);
            }
            String dirOverallSemanticFixes = dirProject.replace(data.strSimulation, data.strSemanticFixes);
            if (objUtil.FileExists(dirOverallSemanticFixes + "/" + data.strOverallSemanticSimilarityFileName)) {
                objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOverallSemanticFixes + "/" + data.strOverallSemanticSimilarityFileName, null);
            }
            objUtil.WriteListToFile(dirOverallSemanticFixes, data.strOverallSemanticSimilarityFileName, lstOverallSemanticSimilarity);
        } catch (Exception ex) {
            System.out.println("error at controller.CompareFixes()");
            throw ex;
        }
    }

    private void GetFailingTests(String dirProject) throws Exception {
        try {
            File folderSimulation = new File(dirProject);
            //LinkedList<String> lstOverallSemanticSimilarity = new LinkedList();
            for (File folderProjectWithPatchId : folderSimulation.listFiles()) {
                if (folderProjectWithPatchId.isDirectory() == false) {
                    continue;
                }
                LinkedList<String> lstFailingTests = new LinkedList();
                lstFailingTests.add("Bug_ID" + "," + "Mutant_Name" + "," + "Mutant_ID" + "," + "Num_Failing_Tests" + "," + "Failing_Tests");
                String strProjectWithPatchId = folderProjectWithPatchId.getName();
                String dirProjectWithPatchId = folderProjectWithPatchId.getPath().replace("\\", "/");
                String strBugSimulationFileName = data.strBuggy + data.strTestPartialFileName;
                for (File fileMutantSimulation : folderProjectWithPatchId.listFiles()) {
                    String strFailingTests = data.strNone;
                    Integer intFailingTests = -1;
                    Boolean mutantOutputAvailable = true;
                    String strMutantSimulationFileName = fileMutantSimulation.getName();
                    String strMutantSimulationFilePath = fileMutantSimulation.getPath();
                    if (!strMutantSimulationFileName.matches(data.strTxtExtensionCheck)) {
                        continue;
                    }
                    if (strMutantSimulationFileName.equals(strBugSimulationFileName)) {
                        continue;
                    }
                    LinkedList<String> lstMutantSimulation = objUtil.ReadFileToList(strMutantSimulationFilePath);
                    if (lstMutantSimulation == null || lstMutantSimulation.isEmpty()) {
                        mutantOutputAvailable = false;
                    }
                    if (mutantOutputAvailable) {
                        String strFailingTestsSentence = lstMutantSimulation.get(0);
                        if (strFailingTestsSentence.contains(data.strFailingTests + data.strColonSpace)) {
                            try {
                                intFailingTests = Integer.parseInt(strFailingTestsSentence.replace(data.strFailingTests + data.strColonSpace, ""));
                                if (intFailingTests > 0) {
                                    for (int i = 1; i < lstMutantSimulation.size(); i++) {
                                        String failingTest = lstMutantSimulation.get(i).replace("-", "").replace("::", ".").trim();
                                        if (failingTest.isEmpty()) {
                                            continue;
                                        }
                                        if (strFailingTests.equals(data.strNone)) {
                                            strFailingTests = failingTest;
                                        } else {
                                            strFailingTests += "," + failingTest;
                                        }
                                    }
                                }
                            } catch (NumberFormatException nfex) {
                            }
                        }
                    }
                    if (strFailingTests.equals(data.strNone) == false) {
                        strFailingTests = "\"" + strFailingTests + "\"";
                    }
                    String strMutantFileName = strMutantSimulationFileName.replace(data.strTestPartialFileName, data.strSupportedLangExt);
                    String strMutantID = strMutantFileName.replace(data.strSupportedLangExt, "").split(Pattern.quote("_"))[1];
                    String strToAdd = strProjectWithPatchId + "," + strMutantFileName + "," + strMutantID + "," + intFailingTests + "," + strFailingTests;
                    lstFailingTests.add(strToAdd);
                    //lstOverallSemanticSimilarity.add(strToAdd);
                    System.out.println(strToAdd);
                }
                String dirFailingTests = dirProjectWithPatchId.replace(data.strSimulation, data.strFailingTests.replace(" ", "").toLowerCase());
                String strCsvFileName = strProjectWithPatchId + data.strCsvExt;
                if (objUtil.FileExists(dirFailingTests + "/" + strCsvFileName)) {
                    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirFailingTests + "/" + strCsvFileName, null);
                }
                dirFailingTests = dirFailingTests.replace("/" + strProjectWithPatchId, "");
                objUtil.WriteListToFile(dirFailingTests, strCsvFileName, lstFailingTests);
            }
            //String dirOverallSemanticFixes = dirProject.replace(data.strSimulation, data.strSemanticFixes);
            //if (objUtil.FileExists(dirOverallSemanticFixes + "/" + data.strOverallSemanticSimilarityFileName)) {
            //    objUtil.ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOverallSemanticFixes + "/" + data.strOverallSemanticSimilarityFileName, null);
            //}
            //objUtil.WriteListToFile(dirOverallSemanticFixes, data.strOverallSemanticSimilarityFileName, lstOverallSemanticSimilarity);
        } catch (Exception ex) {
            System.out.println("error at controller.GetFailingTests()");
            throw ex;
        }
    }

    private void ProcessMutationOperators(String strTechnique) throws Exception {
        try {
            data.dirMutSrc = data.dirMutSrc + "-" + strTechnique;
            dirProject = data.dirMutSrc;
            objUtil = new util(dirProject);
            ArrayList<ArrayList<String>> arrayListSubsumingMutants = objUtil.ReadArrayListFromCSV(data.dirMutOperators + "/" + data.csvSubsumingMutants);
            String technique = "";
            switch (strTechnique) {
                case "ibir":
                    technique = strTechnique;
                    break;
                case "codebert":
                    technique = "mubert";
                    break;
                case "nmt":
                    technique = "deepmutation";
            }
            arrayListSubsumingMutants = objUtil.ProcessMutationOperators(technique, arrayListSubsumingMutants);
            if (arrayListSubsumingMutants != null) {
                String csvNewName = data.csvSubsumingMutants.replace(".csv", "_processed.csv");
                objUtil.WriteArrayListToCSV(data.dirMutOperators, csvNewName, arrayListSubsumingMutants);
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.controller.ProcessMutationOperators()");
            ex.printStackTrace();
            throw ex;
        }
    }
}
