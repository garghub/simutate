/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simutate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;
//import org.apache.commons.io.FileUtils;
//import org.apache.maven.shared.utils.cli.CommandLineUtils;
//import org.apache.maven.shared.utils.cli.Commandline;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author aayush.garg
 */
public class util {

    String dirProject;
    LinkedList<String> lstTestDiakonWithPackageAdded;
    LinkedList<String> lstAbsFns;
    LinkedList<String> lstAbsFnLocs;
    LinkedList<String> lstMutatedAbsFns;
    LinkedList<String> lstDiffMappedToFn;
    LinkedList<String> lstSimulation;
    Boolean requiresWrite;

    LinkedList<String> lstFlatteningMap;
    LinkedList<String> lstFlattenedMutatedFns;
    LinkedList<String> lstFlattenedBuggyFns;

    util(String dirProjectPath) {
        dirProject = dirProjectPath;
        lstAbsFns = new LinkedList();
        lstAbsFnLocs = new LinkedList();
        lstDiffMappedToFn = new LinkedList();
        requiresWrite = false;
    }

    public Boolean FileExists(String filepath) throws Exception {
        try {
            File file = new File(filepath);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.util.FileExists()");
            throw ex;
        }
    }

    public LinkedList<String> GetProcessedFiles() throws Exception {
        try {
            String strFilePath = data.dirSrcMLBatchFile + "/" + data.strProcessedFilesFileName;
            System.out.println("reading " + strFilePath + " to get updated processed files list");
            LinkedList<String> lstProcessedFiles;
            lstProcessedFiles = ReadFileToList(strFilePath);
            if (lstProcessedFiles == null) {
                lstProcessedFiles = new LinkedList();
            }
            return lstProcessedFiles;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.GetProcessedFiles()");
            throw ex;
        }
    }

    public LinkedList<String> ReadFileToList(String filepath) throws Exception {
        try {
            if (!(FileExists(filepath))) {
                System.out.println(filepath + " does not exist!");
                return null;
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(filepath));
                LinkedList list = new LinkedList();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                if (list.isEmpty()) {
                    list = null;
                }
                reader.close();
                return list;
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.util.ReadFileToList()");
            throw ex;
        }
    }

    public void ProcessClassFile(String strCodeFilePath) {
        try {
            String strCleanedXML, strCleanedCode;
            System.out.println("processing " + strCodeFilePath + " ...");
            File codeFile = new File(strCodeFilePath);
            String codeFileName = codeFile.getName();
            String className = codeFileName.replace(data.strSupportedLangExt, "");
            LinkedList<String> lstOrigCode = ReadFileToList(strCodeFilePath);
//            String strFileXML = ExecuteBashFile(data.srcMLBatchFilePath, strCodeFilePath);
//            String strFileXML = RunGAssert(data.srcMLBatchFilePath, strCodeFilePath);
            String strFileXML = ExecuteProcess(data.strInitialCommandForsrcml, strCodeFilePath);
            if (strFileXML == null) {
                return;
            }

            String[] arrCleanedCodeWithXML = GetCleanedCodeWithXML(strFileXML);
            strCleanedXML = arrCleanedCodeWithXML[0];
            strCleanedCode = arrCleanedCodeWithXML[1];

            //String packageName = GetPackageName(strCleanedXML);
            //Boolean isClassAbstract = IsClassAbstract(strCleanedXML);
            //if (isClassAbstract) {
            //    System.out.println(strCodeFilePath + " is an abstract class, processing next class file!");
            //    return;
            //}
            LinkedList<String> listFnXMLs = GetFnXMLS(strCleanedXML);

            //ProcessFunctions(strCodeFilePath, strCleanedXML, strCleanedCode, packageName, className, listFnXMLs);
            ProcessFunctions(strCodeFilePath, strCleanedXML, strCleanedCode, className, listFnXMLs);

            //System.out.println(className + " processing finished, restoring original file");
            //WriteListToFileByBackingUp(new File(strCodeFilePath).getParent(), className + data.strSupportedLangExt, className + "_temp.txt", lstMutantCode);
            //System.out.println("restoring original file completed");
        } catch (Exception ex) {
            System.out.println("error at simutate.util.ProcessClassFile()");
            System.out.println(strCodeFilePath);
            ex.printStackTrace();
        }
    }

//    public String ExecuteBashFile(String batchFilePath, String codeFilePath) throws Exception {
//        try {
//            Commandline commandLine = new Commandline();
//
//            File executable = new File(batchFilePath);
//            commandLine.setExecutable(executable.getAbsolutePath());
//            commandLine.addArguments(new String[]{codeFilePath});
//            CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
//            CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
//            int returnCode = CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
//            if (returnCode != 0) {
//                /*
//                if (data._logging) {
//                    System.out.println("SrcML returned abnormal value | " + codeFilePath + " returned: " + stderr.getOutput() + ". Re-trying...");
//                }
//                 */
//                commandLine = new Commandline();
//
//                executable = new File(batchFilePath);
//                commandLine.setExecutable(executable.getAbsolutePath());
//                commandLine.addArguments(new String[]{codeFilePath});
//                stdout = new CommandLineUtils.StringStreamConsumer();
//                stderr = new CommandLineUtils.StringStreamConsumer();
//                returnCode = CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
//                if (returnCode != 0) {
//                    System.out.println("codeassertiongenerator.controller.ExecuteBashFile()");
//                    System.out.println(batchFilePath + " recieved same abnormal value even after re-trying!");
//                    return null;
//                }
//            }
//            return stdout.getOutput();
//        } catch (Exception ex) {
//            System.out.println("error at codeassertiongenerator.util.ExecuteBashFile()");
//            throw ex;
//        }
//    }
    public String ExecuteProcess(String batchFilePath, String codeFilePath) throws Exception {
        try {
            String strOutput = "";
            String commandToExec = batchFilePath == "" ? codeFilePath : batchFilePath + " " + codeFilePath;
            ProcessBuilder builder = new ProcessBuilder(data.strCommandExecutionInitial01, data.strCommandExecutionInitial02, commandToExec);
            System.out.println("running command: " + data.strCommandExecutionInitial01 + " " + data.strCommandExecutionInitial02 + " " + commandToExec);
            Process p = builder.start();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (strOutput.isEmpty()) {
                    strOutput = line;
                } else {
                    strOutput += "\r\n" + line;
                }
            }

            Integer exitCode = p.waitFor();
//            System.out.println("codeassertiongenerator.util.RunGAssert() returning below");
//            System.out.println(strOutput);
            System.out.println("\n" + commandToExec + " exited with error code: " + exitCode);
            return strOutput;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.ExecuteProcess()");
            throw ex;
        }
    }

    public String GetPackageName(String strFileXML) throws Exception {
        try {
            String packageName = "";

            String strPackageNameXML = ReturnEssentialTextFromXML(strFileXML, "<package>", "</package>", true);
            String strPackageSentence = ConvertsrcMLToString(strPackageNameXML);

            packageName = strPackageSentence.split(Pattern.quote(" "))[1].replace(";", "");

            return packageName;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.GetPackageName()");
            throw ex;
        }
    }

    // startTag = "<function>", endTag = "</function>"
    public String ReturnEssentialTextFromXML(String strFileXML, String startTag, String endTag, Boolean removeSrcMLStartCommand) throws Exception {
        try {
            String stringReturned = "";
            if (strFileXML == null || strFileXML.isEmpty()) {
                return stringReturned;
            }

            if (removeSrcMLStartCommand) {
                strFileXML = strFileXML.substring(strFileXML.indexOf('<'));
            }

            if (startTag.isEmpty()) {
                stringReturned = strFileXML;
            } else {
                stringReturned = strFileXML.subSequence(0, strFileXML.indexOf(data.strSupportedLangExt + "\">") + (data.strSupportedLangExt + "\">").length()).toString();
                String[] strings = strFileXML.split(Pattern.quote(startTag));
                for (int i = 0; i < strings.length; i++) {
                    if (strings[i].contains(endTag)) {
                        stringReturned += "\r\n" + startTag;
                        stringReturned += strings[i].subSequence(0, strings[i].indexOf(endTag) + endTag.length()).toString();
                    }
                }
                stringReturned += "\r\n" + "</unit>";
            }
            stringReturned = RemoveUnwantedTextFromXML(stringReturned, "<comment", "</comment>", true);

            return stringReturned;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.ReturnEssentialTextFromXML()");
            throw ex;
        }
    }

    // startTag = "<comment", endTag = "</comment>"
    public String RemoveUnwantedTextFromXML(String string, String startTag, String endTag, Boolean removeSrcMLStartCommand) throws Exception {
        try {
            String stringWithoutComments = "";
            if (string != null && string.isEmpty() == false) {
                if (removeSrcMLStartCommand) {
                    string = string.substring(string.indexOf('<'));
                }

                if (startTag.isEmpty()) {
                    return string;
                } else {
                    String[] strings = string.split(Pattern.quote(startTag));
                    stringWithoutComments = strings[0];
                    for (int i = 1; i < strings.length; i++) {
                        if (strings[i].contains(endTag)) {
                            strings[i] = strings[i].substring(strings[i].indexOf(endTag) + endTag.length());
                        }
                        stringWithoutComments += strings[i];
                    }
                }
            }
            return stringWithoutComments;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.RemoveUnwantedTextFromXML()");
            throw ex;
        }
    }

    public String ConvertsrcMLToString(String xml) throws Exception {
        try {
            String tempFilePath = data.dirSrcMLBatchFile;
            String tempXMLFileName = data.tmpXMLFileName;
            LinkedList<String> listXML = new LinkedList();
            listXML.add(xml);
            DeleteFile(tempFilePath + "/" + tempXMLFileName);
            WriteListToFile(tempFilePath, tempXMLFileName, listXML);
//            String returnedString = ExecuteBashFile(data.srcMLBatchFilePath, tempFilePath + "/" + tempXMLFileName);
//            String returnedString = RunGAssert(data.srcMLBatchFilePath, tempFilePath + "/" + tempXMLFileName);
            String returnedString = ExecuteProcess(data.strInitialCommandForsrcml, tempFilePath + "/" + tempXMLFileName);
            DeleteFile(tempFilePath + "/" + tempXMLFileName);
            if (returnedString == null) {
                System.out.println("received null while converting XML to code!");
                return "";
            }
//            returnedString = returnedString.substring(returnedString.indexOf(".xml") + (".xml").length());

            String[] arrayReturnedString = returnedString.split(Pattern.quote("\r\n"));
            returnedString = "";
            for (String string : arrayReturnedString) {
                String newString = string.replaceAll("[\\t ]", " ").replaceAll("\\s{2,}", " ").trim();
                if (newString.isEmpty()) {
                    continue;
                }
                if (returnedString.isEmpty()) {
                    returnedString = newString;
                } else {
                    returnedString += "\r\n" + newString;
                }
            }

            return returnedString;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.ConvertsrcMLToString()");
            throw ex;
        }
    }

    public void DeleteFile(String filePath) throws Exception {
        try {
            File file = new File(filePath);
            Files.deleteIfExists(file.toPath());
            if (FileExists(filePath) == false) {
                System.out.println(filePath + " deleted");
            } else {
                System.out.println(filePath + " not deleted! giving it another try!");
                file = new File(filePath);
                Files.deleteIfExists(file.toPath());
                if (FileExists(filePath) == false) {
                    System.out.println(filePath + " deleted");
                } else {
                    System.out.println("**************************************************************************************************************************");
                    System.out.println(filePath + " still not deleted!");
                    System.out.println("**************************************************************************************************************************");
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.util.DeleteFile()");
            throw ex;
        }
    }

    public void WriteListToFile(String filePath, String fileName, LinkedList fileContent) throws Exception {
        try {
            if (fileContent == null) {
                System.out.println("util.WriteListToFile() | List found null | Aborting writing file!");
                return;
            }
            if (fileContent.isEmpty()) {
                System.out.println("util.WriteListToFile() | List found empty | Aborting writing file!");
                return;
            }
            if (FileExists(filePath + "/" + fileName)) {
                System.out.println(filePath + "/" + fileName + " already exists.");
            } else {
                if (CreateDirectory(filePath)) {
                    PrintWriter writer = new PrintWriter(filePath + "/" + fileName, "UTF-8");
                    Iterator it = fileContent.iterator();
                    while (it.hasNext()) {
                        writer.println(it.next());
                    }
                    writer.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.util.WriteListToFile()");
            throw ex;
        }
    }

    public void WriteBlankListToFile(String filePath, String fileName) throws Exception {
        try {
            LinkedList<String> fileContent = new LinkedList();
            fileContent.add("");
            if (FileExists(filePath + "/" + fileName)) {
                System.out.println(filePath + "/" + fileName + " already exists.");
            } else {
                if (CreateDirectory(filePath)) {
                    PrintWriter writer = new PrintWriter(filePath + "/" + fileName, "UTF-8");
                    Iterator it = fileContent.iterator();
                    while (it.hasNext()) {
                        writer.println(it.next());
                    }
                    writer.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.util.WriteBlankListToFile()");
            throw ex;
        }
    }

    public Boolean CreateDirectory(String dirPath) throws Exception {
        try {
            File file = new File(dirPath);
            if (FileExists(dirPath)) {
                return true;
            } else {
                if (file.mkdirs()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            System.out.println("error at simutate.util.CreateDirectory()");
            throw ex;
        }
    }

    String ConvertListToString(LinkedList<String> list) throws Exception {
        try {
            String returnString = "";
            for (String str : list) {
                if (returnString.isEmpty()) {
                    returnString = str;
                } else {
                    returnString += data.strNxtLineSeparator + str;
                }
            }
            return returnString;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.ConvertListToString()");
            throw ex;
        }
    }

    public String[] GetCleanedCodeWithXML(String strFileXML) throws Exception {
        try {
            String[] arrReturn;
            String strCleanedCode = "";

            String strCleanedCodeXML = ReturnEssentialTextFromXML(strFileXML, "", "", true);
            strCleanedCode = ConvertsrcMLToString(strCleanedCodeXML);

            arrReturn = new String[]{strCleanedCodeXML, strCleanedCode};

            return arrReturn;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.GetCleanedCodeAndXML()");
            throw ex;
        }
    }

    public void WriteStringToFile(String filePath, String fileName, String string) throws Exception {
        try {
            LinkedList<String> listString = new LinkedList();
            listString.add(string);
            WriteListToFile(filePath, fileName, listString);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.WriteStringToFile()");
            throw ex;
        }
    }

    public Boolean IsClassAbstract(String strFileXML) throws Exception {
        try {
            Boolean isAbstract;
            String strClassNameXML = ReturnEssentialTextFromXML(strFileXML, "<class>", "</name>", true);
            if (strClassNameXML.contains(data.strAbstractClassCheck)) {
                isAbstract = Boolean.TRUE;
            } else {
                isAbstract = Boolean.FALSE;
            }
            return isAbstract;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.IsClassAbstract()");
            throw ex;
        }
    }

    public LinkedList<String> GetFnXMLS(String strFileXML) throws Exception {
        try {
            LinkedList<String> listReturned = new LinkedList();
            String startTag = "<function>";
            String endTag = "</function>";
            strFileXML = strFileXML.substring(strFileXML.indexOf('<'));

            String[] strings = strFileXML.split(Pattern.quote(startTag));
            for (int i = 0; i < strings.length; i++) {
                if (strings[i].contains(endTag)) {
                    String strFunction = startTag;
                    strFunction += strings[i].subSequence(0, strings[i].indexOf(endTag) + endTag.length()).toString();
                    listReturned.add(strFunction);
                }
            }
            return listReturned;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.GetFnXMLS()");
            throw ex;
        }
    }

    public HashMap<String, String> GetAllFunctions(String strCleanedXML, LinkedList<String> listFnXMLs) throws Exception {
        try {
            HashMap<String, String> mapReturn = new HashMap();
            Integer fnCcount = listFnXMLs.size();
            Integer processingFnCount = 0;
            for (String strFnXML : listFnXMLs) {
                String strFnName;
                processingFnCount++;
                System.out.println("proceeding to function# " + processingFnCount + " out of " + fnCcount + "...");
                if (strCleanedXML.contains(strFnXML) == false) {
                    System.out.println("function not matched in cleaned XML!");
                    continue;
                }
                //if (ShouldProcessFunction(strFnXML)) {
                //    System.out.println("processing function# " + processingFnCount + " out of " + fnCcount + "...");
                //} else {
                //    System.out.println("function is either private or static, skipping this function!");
                //    continue;
                //}
                String strFnCode = ProcessFunction(strFnXML);
                strFnName = GetFnNameFromFnXML(strFnXML);
                if (strFnName.isEmpty()) {
                    System.out.println("unable to find function name, processing next function!");
                } else {
                    if (mapReturn.containsKey(strFnName)) {
                        Boolean foundNewStrFnName = false;
                        int i = 2;
                        String newStrFnName = null;
                        while (!foundNewStrFnName) {
                            newStrFnName = strFnName + "_another" + i;
                            if ((mapReturn.containsKey(newStrFnName)) == false) {
                                foundNewStrFnName = true;
                            } else {
                                i++;
                            }
                        }
                        strFnName = newStrFnName;
                    }
                    mapReturn.put(strFnName, strFnCode);
                }
            }
            System.out.println("finished processing all " + fnCcount + " functions");
            return mapReturn;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.GetAllFunctions()");
            throw ex;
        }
    }

    public void ProcessFunctions(String strCodeFilePath, String strCleanedXML, String strCleanedCode,
            //String packageName,
            String className, LinkedList<String> listFnXMLs) throws Exception {
        try {

            File file = new File(strCodeFilePath);
            String dirFns = file.getParent() + "/" + className;
            if (FileExists(dirFns)) {
                System.out.println(strCodeFilePath + "/" + className + data.strSupportedLangExt + " has already been processed before!");
                return;
            }
            CreateDirectory(dirFns);
            WriteStringToFile(dirFns, className + data.strSupportedLangExt, strCleanedCode);
            //File folderProject = new File(dirProject);
            //String projectName = folderProject.getName();
            HashMap<String, String> mapFns = GetAllFunctions(strCleanedXML, listFnXMLs);
            if (mapFns == null || mapFns.isEmpty()) {
                System.out.println("unable to get functions from source code file!");
                return;
            }
            for (String fnName : mapFns.keySet()) {
                String strFnCode = mapFns.get(fnName);
                WriteStringToFile(dirFns, fnName + data.strSupportedLangExt, strFnCode);
                String dirFn = dirFns + "/" + fnName;
                CreateDirectory(dirFn);
                WriteBlankListToFile(dirFn, data.strIdiomFileName);
                String fnFilePath = dirFns + "/" + fnName + data.strSupportedLangExt;
                String absFnFilePath = dirFn + "/" + fnName + data.strAbs + data.strSupportedLangExt;
                String idiomFilePath = dirFn + "/" + data.strIdiomFileName;
                String strsrc2absquery = data.strInitialCommandForsrc2abs01 + dirFn + data.strInitialCommandForsrc2abs02 + fnFilePath + " " + absFnFilePath + " " + idiomFilePath;
                ExecuteProcessAndGetIntErrorCode(strsrc2absquery);
                LinkedList<String> listAbsFn = ReadFileToList(absFnFilePath);
                if (listAbsFn == null && listAbsFn.isEmpty()) {
                    continue;
                }
                String strAbsFn = ConvertListToString(listAbsFn);
                String[] arrAbsFn = strAbsFn.split(Pattern.quote(" "));
                if (arrAbsFn.length < 50) {
                    lstAbsFns.add(strAbsFn);
                    lstAbsFnLocs.add(dirFn);
                }
            }

        } catch (Exception ex) {
            System.out.println("error at simutate.util.ProcessFunctions()");
            throw ex;
        }
    }

    public String GetAssertedFunctionXML(String strFnXML) throws Exception {
        try {
            String strAssertedFnXML = null;
            String[] arrFnXML = strFnXML.split(Pattern.quote("\r\n"));

            if (arrFnXML[arrFnXML.length - 1].contains(data.strFnEndStmtXML) == false) {
                System.out.println("last statement of function doesnt have '}', will proces next function!");
                return strAssertedFnXML;
            }
            if (arrFnXML[arrFnXML.length - 1].contains(data.strReturnXMLStartStmt)) {
                String strInsertedAssertStmtXMLBeforeLastReturn = InsertAssertStmtXMLBeforeInputStmt(arrFnXML[arrFnXML.length - 1], data.strReturnXMLStartStmt);
                arrFnXML[arrFnXML.length - 1] = strInsertedAssertStmtXMLBeforeLastReturn;
            } else if (arrFnXML[arrFnXML.length - 2].contains(data.strReturnXMLStartStmt)) {
                String strInsertedAssertStmtXMLBeforeLastReturn = InsertAssertStmtXMLBeforeInputStmt(arrFnXML[arrFnXML.length - 2], data.strReturnXMLStartStmt);
                arrFnXML[arrFnXML.length - 2] = strInsertedAssertStmtXMLBeforeLastReturn;
            } else {
                String strInsertedAssertStmtXMLBeforeLastReturn = InsertAssertStmtXMLBeforeInputStmt(arrFnXML[arrFnXML.length - 1], data.strFnEndStmtXML);
                arrFnXML[arrFnXML.length - 1] = strInsertedAssertStmtXMLBeforeLastReturn;
            }
            for (String str : arrFnXML) {
                if (strAssertedFnXML == null) {
                    strAssertedFnXML = str;
                } else {
                    strAssertedFnXML += "\r\n" + str;
                }
            }
            return strAssertedFnXML;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.GetAssertedFunctionXML()");
            throw ex;
        }
    }

    public String InsertAssertStmtXMLBeforeInputStmt(String strStmtWithReturnXML, String strInputStmtXML) throws Exception {
        try {
            String strInsertedAssertStmtXMLBeforeLastReturn = strStmtWithReturnXML.substring(0, strStmtWithReturnXML.lastIndexOf(strInputStmtXML))
                    + data.strAssertStmtXML + "\r\n"
                    + strStmtWithReturnXML.substring(strStmtWithReturnXML.lastIndexOf(strInputStmtXML));
//            String[] arrInterim = strStmtWithReturnXML.split(Pattern.quote(strInputStmtXML));
//            for (int i = 0; i <= arrInterim.length - 2; i++) {
//                strInsertedAssertStmtXMLBeforeLastReturn += arrInterim[i] + strInputStmtXML;
//            }
//            //Below is done because if we split a sentenceXML with strFnEndStmtXML, there is nothing left after the last bracket,
//            //hence we include the last value in the array and then add assert statement.
//            if(strInputStmtXML.equals(data.strFnEndStmtXML)){
//                strInsertedAssertStmtXMLBeforeLastReturn += arrInterim[arrInterim.length - 1];
//            }
//            strInsertedAssertStmtXMLBeforeLastReturn += data.strAssertStmtXML + "\r\n" + strInputStmtXML + arrInterim[arrInterim.length - 1];
            return strInsertedAssertStmtXMLBeforeLastReturn;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.InsertAssertStmtXMLBeforeInputStmt()");
            throw ex;
        }
    }

    void UpdateProcessedFiles(LinkedList<String> lstProcessedFiles) throws Exception {
        try {
            WriteListToFileByBackingUp(data.dirSrcMLBatchFile, data.strProcessedFilesFileName, data.strProcessedFilesBkpFileName, lstProcessedFiles);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.UpdateProcessedFiles()");
            throw ex;
        }
    }

    public void WriteListToFileByBackingUp(String filePath, String fileName, String bkpFileName, LinkedList fileContent) throws Exception {
        try {
            if (fileContent == null) {
                System.out.println("util.WriteListToFileByBackingUp() | List found null | Aborting writing file!");
                return;
            }
            if (fileContent.isEmpty()) {
                System.out.println("util.WriteListToFileByBackingUp() | List found empty | Aborting writing file!");
                return;
            }
            if (FileExists(filePath + "/" + fileName)) {
                File file = new File(filePath + "/" + fileName);
                File bkpFile = new File(filePath + "/" + bkpFileName);
                file.renameTo(bkpFile);

            }
            if (CreateDirectory(filePath)) {
                PrintWriter writer = new PrintWriter(filePath + "/" + fileName, "UTF-8");
                Iterator it = fileContent.iterator();
                while (it.hasNext()) {
                    writer.println(it.next());
                }
                writer.close();
            }
            DeleteFile(filePath + "/" + bkpFileName);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.WriteListToFileByBackingUp()");
            throw ex;
        }
    }

    public void WriteListToFileByBackingUp2(String filePath, String fileName, String bkpFileName, LinkedList fileContent) throws Exception {
        try {
            if (fileContent == null) {
                System.out.println("util.WriteListToFileByBackingUp() | List found null | Aborting writing file!");
                return;
            }
            if (fileContent.isEmpty()) {
                System.out.println("util.WriteListToFileByBackingUp() | List found empty | Aborting writing file!");
                return;
            }
            if (FileExists(filePath + "/" + fileName)) {
                File file = new File(filePath + "/" + fileName);
                File bkpFile = new File(filePath + "/" + bkpFileName);
                file.renameTo(bkpFile);

            }
            if (CreateDirectory(filePath)) {
                PrintWriter writer = new PrintWriter(filePath + "/" + fileName);
                Iterator it = fileContent.iterator();
                while (it.hasNext()) {
//                    writer.println(it.next());
                    writer.print(it.next());
                }
                writer.close();
            }
            DeleteFile(filePath + "/" + bkpFileName);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.WriteListToFileByBackingUp2()");
            throw ex;
        }
    }

    public void ProcessAssertedClassCode(String strCodeFilePath, String packageName, String className, String strAssertedCleanCode) throws Exception {
        try {
            WriteStringToFileByBackingUp(new File(strCodeFilePath).getParent(), className + data.strSupportedLangExt, className + "_temp.txt", strAssertedCleanCode);

            String dirOrigPath = dirProject + "/" + data.strOrigDirName;
            File dirOrig = new File(dirOrigPath);
            for (File unknownFile : dirOrig.listFiles()) {
                DeleteFile(unknownFile.getPath());
            }
            WriteStringToFile(dirOrigPath, className + data.strSupportedLangExt, strAssertedCleanCode);

            ProcessPackageNameAdditions(packageName, className);

            WriteStringToFileByBackingUp2(dirProject, data.strClassNameInputFileName, data.strClassNameInputBkpFileName, packageName + "." + className);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.ProcessAssertedClassCode()");
            throw ex;
        }
    }

    public void WriteStringToFileByBackingUp(String filePath, String fileName, String bkpFileName, String string) throws Exception {
        try {
            LinkedList<String> listString = new LinkedList();
            listString.add(string);
            WriteListToFileByBackingUp(filePath, fileName, bkpFileName, listString);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.WriteStringToFileByBackingUp()");
            throw ex;
        }
    }

    public void WriteStringToFileByBackingUp2(String filePath, String fileName, String bkpFileName, String string) throws Exception {
        try {
            LinkedList<String> listString = new LinkedList();
            listString.add(string);
            WriteListToFileByBackingUp2(filePath, fileName, bkpFileName, listString);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.WriteStringToFileByBackingUp2()");
            throw ex;
        }
    }

    public void ProcessPackageNameAdditions(String packageName, String className) throws Exception {
        try {
            String dirTestDaikon = dirProject + "/" + data.strTestDaikonDirName;
            ProcessPackageNameAdditionInTestDaikonFile(dirTestDaikon, packageName);
            File fileTestDaikon = new File(dirTestDaikon);
            for (File unknownFile : fileTestDaikon.listFiles()) {
                DeleteFile(unknownFile.getPath());
            }
            String[] arrPackageName = packageName.split(Pattern.quote("."));
            String strNewDirStructure = "";
            for (String strPN : arrPackageName) {
                strNewDirStructure += "/" + strPN;
            }
            WriteListToFile(dirTestDaikon + strNewDirStructure, data.strTestDaikonFileName, lstTestDiakonWithPackageAdded);

            String strGradleBuildFilePath = dirProject + "/" + data.strGradleBuildFileName;
            LinkedList<String> lstGradleBuildUntouched = ReadFileToList(strGradleBuildFilePath);
            LinkedList<String> lstGradleBuildWithPackageNameAdded = new LinkedList();
            for (String strGB : lstGradleBuildUntouched) {
                if (strGB.contains(data.strGradleBuildFileSentenceToMatch)) {
                    lstGradleBuildWithPackageNameAdded.add(data.strGradleBuildFileSentenceToMatch + packageName + data.strGradleBuildFileSentenceToComplete);
                    //below code is when we want to add package_name(.)class_name in settings.gradle file for PITEST to consider only particular class
                    //it doesnt work hence not adding class_name
                    //lstGradleBuildWithPackageNameAdded.add(data.strGradleBuildFileSentenceToMatch + packageName + "." + className + data.strGradleBuildFileSentenceToCompleteRemaining); //data.strGradleBuildFileSentenceToComplete);
                } else {
                    lstGradleBuildWithPackageNameAdded.add(strGB);
                }
            }
            WriteListToFileByBackingUp(dirProject, data.strGradleBuildFileName, data.strGradleBuildBkpFileName, lstGradleBuildWithPackageNameAdded);
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.ProcessPackageNameAdditions()");
            throw ex;
        }
    }

    public void ProcessPackageNameAdditionInTestDaikonFile(String probableFilePath, String packageName) throws Exception {
        try {
            File file = new File(probableFilePath);
            for (File unknownFile : file.listFiles()) {
                if (unknownFile.isDirectory()) {
                    ProcessPackageNameAdditionInTestDaikonFile(unknownFile.getPath(), packageName);
                    DeleteFile(unknownFile.getPath());
                } else if (unknownFile.getName().equals(data.strTestDaikonFileName)) {
                    LinkedList<String> lstTestDiakonUntouched = ReadFileToList(unknownFile.getPath());
                    lstTestDiakonWithPackageAdded = new LinkedList();

                    lstTestDiakonWithPackageAdded.add(data.strPackageSentence + packageName + ";");
                    for (int i = 1; i < lstTestDiakonUntouched.size(); i++) {
                        lstTestDiakonWithPackageAdded.add(lstTestDiakonUntouched.get(i));
                    }
                    DeleteFile(unknownFile.getPath());
                } else {
                    DeleteFile(unknownFile.getPath());
                }
            }
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.ProcessPackageNameAdditions()");
            throw ex;
        }
    }

    public Integer RunGAssert(String newProcessDirPath, String oldProcessDirPath, String[] args) throws Exception {
        try {
            Integer retBoolean;
            String commandToExec = data.strCreateProcessingDirPart01 + " " + newProcessDirPath + " " + data.strCreateProcessingDirPart02
                    + " " + oldProcessDirPath + "/* " + newProcessDirPath + " && ";
            commandToExec += data.strInitialCommandForGAssert;
            for (String arg : args) {
                commandToExec += " " + arg;
            }
            System.out.println("sleeping for 3 minutes...");
            Thread.sleep(3 * 60 * 1000);
            retBoolean = ExecuteProcessAndGetIntErrorCode(commandToExec);
            System.out.println("sleeping for 3 minutes...");
            Thread.sleep(3 * 60 * 1000);
            return retBoolean;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.RunGAssert()");
            throw ex;
        }
    }

    public Integer ExecuteProcessAndGetIntErrorCode(String commandToExec) throws Exception {
        try {
            ProcessBuilder builder = new ProcessBuilder(data.strCommandExecutionInitial01, data.strCommandExecutionInitial02, commandToExec);
            System.out.println("running command: " + data.strCommandExecutionInitial01 + " " + data.strCommandExecutionInitial02 + " " + commandToExec);
            //ProcessBuilder builder = new ProcessBuilder("bash", "-c", commandToExec);
            Process p = builder.start();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                //required, do not delete below print statement
                System.out.println(line);
            }

            Integer exitCode = p.waitFor();
            System.out.println("\n" + commandToExec + " exited with error code: " + exitCode);
            return exitCode;
        } catch (Exception ex) {
            System.out.println("error at util.ExecuteProcessAndGetIntErrorCode()");
            throw ex;
        }
    }

    public String GetPerfectResultsDirPath(String projectName, String packageName, String className, Integer processingFnCount) throws Exception {
        try {
            String[] arrPackageName = packageName.split(Pattern.quote("."));
            String strPackagePath = "";
            for (String strPN : arrPackageName) {
                strPackagePath += "/" + strPN;
            }
            String strNewDirPath = data.dirGenOutput + "/" + projectName + strPackagePath + "/" + className + "/" + processingFnCount;
            return strNewDirPath;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.GetPerfectResultsDirPath()");
            throw ex;
        }
    }

    public Boolean IsFnAlreadyProcessed(String projectName, String packageName, String className, Integer processingFnCount) throws Exception {
        try {
            String strPathPerfect = GetPerfectResultsDirPath(projectName, packageName, className, processingFnCount);
            String strPathNoOut = strPathPerfect + data.strNoOut;
            String strPathNoGen = strPathPerfect + data.strNoGen;
            System.out.println("checking if either of the below directories exit");
            System.out.println("1." + strPathPerfect);
            System.out.println("2." + strPathNoOut);
            System.out.println("3." + strPathNoGen);
            if (FileExists(strPathPerfect) || FileExists(strPathNoOut) || FileExists(strPathNoGen)) {
                System.out.println("directory exists, function already processed!");
                return true;
            } else {
                System.out.println("none of the directories exist, function not processed yet");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.IsFnAlreadyProcessed()");
            throw ex;
        }
    }

    public void SaveOutputs(String projectName, String packageName, String className, Integer processingFnCount,
            Integer fnCcount, String strAssertedFnXML, String strAssertedCleanCode, String newProcessDirPath) {
        try {

            String strNewDirPath = GetPerfectResultsDirPath(projectName, packageName, className, processingFnCount);
            String stroutAssertionsFilePath = newProcessDirPath + "/" + data.dirPrjOutName + "/" + data.strOutputAssertionsFileName;
            LinkedList<String> lstGenAssertions = ReadFileToList(stroutAssertionsFilePath);

            if (lstGenAssertions == null) {
                System.out.println("processing function# " + processingFnCount + " out of " + fnCcount + " returned no output assertion!");
                strNewDirPath += data.strNoOut;
            } else if (lstGenAssertions.size() == 1) {
                if (lstGenAssertions.get(0).trim().isEmpty()) {
                    System.out.println("processing function# " + processingFnCount + " out of " + fnCcount + " returned no output assertion!");
                    strNewDirPath += data.strNoGen;
                }
            }
            if (CreateDirectory(strNewDirPath + "/" + data.dirPrjOutName)) {
                System.out.println("copying files in " + data.dirPrjOutName);
                CopyFile(newProcessDirPath + "/" + data.dirPrjOutName, strNewDirPath + "/" + data.dirPrjOutName);

                System.out.println("copying files in " + data.dirPrjLogsName);
                CreateDirectory(strNewDirPath + "/" + data.dirPrjLogsName);
                CopyFile(newProcessDirPath + "/" + data.dirPrjLogsName, strNewDirPath + "/" + data.dirPrjLogsName);

                System.out.println("copying files in " + data.dirInputAssertions);
                CreateDirectory(strNewDirPath + "/" + data.dirInputAssertions);
                CopyFile(newProcessDirPath + "/" + data.dirInputAssertions, strNewDirPath + "/" + data.dirInputAssertions);

                String strAssertedCleanedFnCode = ConvertsrcMLToString(strAssertedFnXML);
                WriteStringToFile(strNewDirPath, data.strAssertedFnFileName, strAssertedCleanedFnCode);
                WriteStringToFile(strNewDirPath, data.strAssertedFileName, strAssertedCleanCode);
//                CleanupDir(dirProject + "/" + data.dirPrjOutName);
//                CleanupDir(dirProject + "/" + data.dirPrjLogsName);
            } else {
                System.out.println("unable to create " + strNewDirPath + " !");
            }
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.ProcessFunctions()");
            ex.printStackTrace();
        }
    }

    public void CopyFile(String strSourcePath, String strTargetPath) throws Exception {
        try {
//            File sourceLocation = new File(strSourcePath);
//            File targetLocation = new File(strTargetPath);
//            FileUtils.copyDirectory(sourceLocation, targetLocation);
            File sourceLocation = new File(strSourcePath);
            if (!sourceLocation.exists()) {
                System.out.println(strSourcePath + " does not exist, unable to copy files!");
                return;
            }
            for (File newFile : sourceLocation.listFiles()) {
                LinkedList<String> lstNewFile = ReadFileToList(newFile.getPath());
                WriteListToFile(strTargetPath, newFile.getName(), lstNewFile);
            }
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.CopyFile()");
            throw ex;
        }
    }

    public void CleanupDir(String dirPath) throws Exception {
        try {
            File dir = new File(dirPath);
            for (File unknownFile : dir.listFiles()) {
                if (unknownFile.isDirectory()) {
                    CleanupDir(unknownFile.getPath());
                } else {
                    DeleteFile(unknownFile.getPath());
                }
            }
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.CleanupDir()");
            throw ex;
        }
    }

    public String GetFnNameFromFnXML(String strFnXML) throws Exception {
        String strFnName = "";
        try {
            System.out.println("proceeding to get function name");
            Document docFn = ConvertStringToXMLDocument(strFnXML);
            NodeList lstNodes = docFn.getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < lstNodes.getLength(); i++) {
                Node node = lstNodes.item(i);
                if (node.getNodeName().equals("name")) {
                    strFnName = node.getTextContent();
                }
                if (strFnName.isEmpty() == false) {
                    break;
                }
            }
            System.out.println("function name is " + strFnName);
            return strFnName;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.GetFnNameFromFnXML()");
            ex.printStackTrace();
            return "";
        }
    }

    public String ProcessFunction(String strFnXML) throws Exception {
        try {
            System.out.println("converting function xml to function code");

            String fnXMLReadyToBeConverted = data.strFnSrcmlFirstWord + strFnXML + data.strFnSrcmlLastWord;
            String strFnCode = ConvertsrcMLToString(fnXMLReadyToBeConverted);
            return strFnCode;
        } catch (Exception ex) {
            System.out.println("error at simutate.util.ProcessFunction()");
            throw ex;
        }
    }

    public Boolean ShouldProcessFunction(String strFnXML) throws Exception {
        Boolean shouldUse = true;
        String specifier1 = "";
        String specifier2 = "";
        try {
            System.out.println("checking if function should be processed");

            String fnXMLReadyToBeConverted = data.strFnSrcmlFirstWord + strFnXML + data.strFnSrcmlLastWord;
            String strFnCode = ConvertsrcMLToString(fnXMLReadyToBeConverted);
            String[] arrFnCode = strFnCode.split(Pattern.quote("\r\n"));
            if (arrFnCode.length < data.intMinThresholdCountForFnSentences) {
                System.out.println("function does not pass the minimum sentence count threshold of " + data.intMinThresholdCountForFnSentences + " !");
                shouldUse = false;
                return shouldUse;
            }
            Document docFn = ConvertStringToXMLDocument(strFnXML);
            NodeList lstNodes = docFn.getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < lstNodes.getLength(); i++) {
                Node node = lstNodes.item(i);
                if (node.getNodeName().equals("specifier")) {
                    if (specifier1.isEmpty()) {
                        specifier1 = node.getTextContent();
                    } else {
                        specifier2 = node.getTextContent();
                    }
                }
                if (specifier1.equals("private") || specifier1.equals("static") || specifier2.equals("static")) {
                    shouldUse = false;
                    break;
                }
            }
            return shouldUse;
        } catch (Exception ex) {
            System.out.println("error at codeassertiongenerator.util.GetSpecifierFromFnXML()");
            throw ex;
        }
    }

    public Document ConvertStringToXMLDocument(String xmlString) throws Exception {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception ex) {
            System.out.println("codeassertiongenerator.util.ConvertStringToXMLDocument()");
            throw ex;
        }
    }

    HashMap<String, LinkedList<String>> TraverseToGetSrcCode(String dirPrjSrc, String srcFileName) {
        HashMap<String, LinkedList<String>> retMap = new HashMap();
        LinkedList<String> lstSrc = null;
        try {
            String strDir = dirPrjSrc;
            Boolean foundSrcFile = false;
            while (foundSrcFile == false) {
                File fileDir = new File(strDir);
                for (File fileInsideDir : fileDir.listFiles()) {
                    String fileInsideDirName = fileInsideDir.getName();
                    String fileInsideDirPath = fileInsideDir.getPath().replace("\\", "/");
                    if (fileInsideDir.isDirectory()) {
                        strDir = fileInsideDir.getPath();
                    } else if (fileInsideDirName.matches(data.strExtensionCheck)) {
                        if (!fileInsideDirName.equals(srcFileName)) {
                            continue;
                        }
                        foundSrcFile = true;
                        lstSrc = ReadFileToList(fileInsideDirPath);
                        retMap.put(fileInsideDirPath, lstSrc);
                    }
                }
            }
            return retMap;
        } catch (Exception ex) {
            System.out.println("util.TraverseToGetSrcCode()");
            ex.printStackTrace();
            return retMap;
        }
    }

    LinkedList<HashMap<Integer, Integer>> GetListOfFirstMinusAndPlus(LinkedList<String> lstPatch) {
        LinkedList<HashMap<Integer, Integer>> retList = new LinkedList();
        try {
            Integer firstMinus = 0;
            Integer firstPlus = 0;
            Integer changedLineNum = 0;
            Integer counter = 0;
            for (String line : lstPatch) {
                if (line.contains(data.strToLookInPatch) && line.indexOf(data.strToLookInPatch) == 0) {
                    if (firstMinus != 0 || firstPlus != 0) {
                        HashMap<Integer, Integer> mapFirstMinusAndPlus = new HashMap();
                        mapFirstMinusAndPlus.put(firstMinus, firstPlus);
                        retList.add(mapFirstMinusAndPlus);
                        firstMinus = 0;
                        firstPlus = 0;
                        changedLineNum = 0;
                        counter = 0;
                    }
                    String[] arrLine = line.split(Pattern.quote(" "));
                    String changedLinePhrase = arrLine[1];
                    String[] arrChangedLinePhrase = changedLinePhrase.split(Pattern.quote(","));
                    changedLineNum = Integer.parseInt(arrChangedLinePhrase[0].replace("+", "").replace("-", ""));
                } else if (changedLineNum != 0) {
                    if (line.startsWith("+")) {
                        if (firstPlus == 0) {
                            firstPlus = changedLineNum + counter;
                            if (firstPlus == firstMinus + 1) {
                                firstPlus = firstMinus;
                            }
                        }
                    } else if (line.startsWith("-")) {
                        if (firstMinus == 0) {
                            firstMinus = changedLineNum + counter;
                            if (firstPlus != 0) {
                                firstMinus = firstPlus;
                            }
                        }
                    }
                    counter++;
                }
            }
            //to add the last patch which will not be added otherwise due to end of list
            if (firstMinus != 0 || firstPlus != 0) {
                HashMap<Integer, Integer> mapFirstMinusAndPlus = new HashMap();
                mapFirstMinusAndPlus.put(firstMinus, firstPlus);
                retList.add(mapFirstMinusAndPlus);
                firstMinus = 0;
                firstPlus = 0;
                changedLineNum = 0;
                counter = 0;
            }
            return retList;
        } catch (Exception ex) {
            System.out.println("util.GetListOfFirstMinusAndPlus()");
            ex.printStackTrace();
            return retList;
        }
    }

    HashMap<String, String> GetFnsFromMapFile(String strMapFilePath) {
        HashMap<String, String> mapAvailableFns = new HashMap();
        try {
            LinkedList<String> lst = ReadFileToList(strMapFilePath);
            if (lst == null || lst.isEmpty()) {
                return mapAvailableFns;
            }
            for (String str : lst) {
                if (str.isEmpty()) {
                    continue;
                }
                String[] arr = str.split(Pattern.quote(data.strPipe));
                mapAvailableFns.put(arr[1], str);
            }
            return mapAvailableFns;
        } catch (Exception ex) {
            System.out.println("util.GetFnsFromMapFile()");
            ex.printStackTrace();
            return mapAvailableFns;
        }
    }

    Boolean FindFunctionNameAndAddToList(String strPrjWithPatchId, Integer index, LinkedList<String> lstSrc, HashMap<String, String> mapAvailableFns) {
        try {
            int i = 0;
            Boolean foundFnName = false;
            while (foundFnName == false) {
                String trimmedSentence;
                boolean boolCond02 = true;
                if ((index - i >= 0)) {
                    trimmedSentence = lstSrc.get(index - i).trim();
                    if (data.strTechnique != null && data.strTechnique.isEmpty() == false) {
                        //nmt
                        if (mapAvailableFns.containsKey(trimmedSentence)) {
                            String strToAdd = strPrjWithPatchId + data.strPipe + mapAvailableFns.get(trimmedSentence);
                            if (!lstDiffMappedToFn.contains(strToAdd)) {
                                lstDiffMappedToFn.add(strToAdd);
                            }
                            foundFnName = true;
                            break;
                        }
                    } else {
                        //other
                        if (trimmedSentence.length() > 1) {
                            for (String strAccessModifier : data.lstAccessModifiers) {
                                trimmedSentence = trimmedSentence.replace(strAccessModifier, "");
                            }
                            trimmedSentence = trimmedSentence.replaceAll("\\s{2,}", " ").replace(" ", "").trim();
                            for (String strAvailableFn : mapAvailableFns.keySet()) {
                                String trimmedAvailableFn = strAvailableFn;
                                for (String strAccessModifier : data.lstAccessModifiers) {
                                    trimmedAvailableFn = trimmedAvailableFn.replace(strAccessModifier, "");
                                }
                                trimmedAvailableFn = strAvailableFn.replaceAll("\\s{2,}", " ").replace(" ", "").trim();
                                if (trimmedAvailableFn.contains(trimmedSentence)) {
                                    String[] arrAvailableFn = mapAvailableFns.get(strAvailableFn).split(data.strPipe);
                                    String strToAdd = strPrjWithPatchId + data.strPipe + arrAvailableFn[0] + data.strPipe + strAvailableFn;
                                    if (!lstDiffMappedToFn.contains(strToAdd)) {
                                        lstDiffMappedToFn.add(strToAdd);
                                    }
                                    foundFnName = true;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    boolCond02 = false;
                }
                if (boolCond02 == false) {
                    break;
                }
                i++;
            }
            return foundFnName;
        } catch (Exception ex) {
            System.out.println("error at util.FindFunctionNameAndAddToList()");
            ex.printStackTrace();
            return false;
        }
    }

    Boolean FindFunctionNameInBothDirectionsAndAddToList(String strPrjWithPatchId, Integer index, LinkedList<String> lstSrc, HashMap<String, String> mapAvailableFns) {
        try {
            int i = 0;
            Boolean foundFnName = false;
            while (foundFnName == false) {
                String trimmedSentence;
                boolean boolCond01 = true;
                boolean boolCond02 = true;
                if ((lstSrc.size() - 1 >= index + i) && (index + i >= 0)) {
                    trimmedSentence = lstSrc.get(index + i).trim();
                    if (mapAvailableFns.containsKey(trimmedSentence)) {
                        String strToAdd = strPrjWithPatchId + data.strPipe + mapAvailableFns.get(trimmedSentence);
                        if (!lstDiffMappedToFn.contains(strToAdd)) {
                            lstDiffMappedToFn.add(strToAdd);
                        }
                        foundFnName = true;
                        break;
                    }
                } else {
                    boolCond01 = false;
                }
                if ((index - i >= 0) && (lstSrc.size() - 1 >= index - i)) {
                    trimmedSentence = lstSrc.get(index - i).trim();
                    if (mapAvailableFns.containsKey(trimmedSentence)) {
                        String strToAdd = strPrjWithPatchId + data.strPipe + mapAvailableFns.get(trimmedSentence);
                        if (!lstDiffMappedToFn.contains(strToAdd)) {
                            lstDiffMappedToFn.add(strToAdd);
                        }
                        foundFnName = true;
                        break;
                    }
                } else {
                    boolCond02 = false;
                }
                if (boolCond01 == false && boolCond02 == false) {
                    break;
                }
                i++;
            }
            return foundFnName;
        } catch (Exception ex) {
            System.out.println("error at util.FindFunctionNameInBothDirectionsAndAddToList()");
            ex.printStackTrace();
            return false;
        }
    }

    Boolean GetDefects4jExecutionSuccess(String dirPrjBuggy, String projectName, String patchId) {
        {
            try {
                if (!FileExists(dirPrjBuggy)) {
                    CreateDirectory(dirPrjBuggy);
                }
                String strCommandForDefects4j = data.strInitialCommandForDefects4j + data.strInitialCommandForCheckout01 + projectName
                        + data.strInitialCommandForCheckout02 + patchId + data.strBuggy
                        + data.strInitialCommandForCheckout03 + dirPrjBuggy;
                //String[] arrString = ExecuteProcessGetErrorCodeAndSaveOutput(strCommandForDefects4j);
                //Integer errorCode = Integer.parseInt(arrString[0]);

                Integer errorCode = ExecuteProcessGetErrorCodeAndSaveOutput(strCommandForDefects4j, null);
                if (errorCode != 0) {
                    return false;
                }
                File folderPrjBuggy = new File(dirPrjBuggy);
                if (folderPrjBuggy.listFiles().length == 0) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception ex) {
                System.out.println("error in util.GetDefects4jExecutionSuccess()");
                ex.printStackTrace();
                return false;
            }
        }
    }

    void PerformSimulationForBug(String strPrjWithPatchId, String dirPrjBuggy) {
        try {

            String strToAdd = strPrjWithPatchId + data.strPipe + data.strBuggy;
            Boolean shouldSimulate = true;
            for (String entry : lstSimulation) {
                if (entry.contains(strToAdd)) {
                    shouldSimulate = false;
                    break;
                }
            }
            if (shouldSimulate) {
                String strCompileTestResult = GetCompileAndTestResult(strPrjWithPatchId, dirPrjBuggy, data.strBuggy + data.strTestPartialFileName);
                if (!strCompileTestResult.isEmpty()) {
                    strToAdd += data.strPipe + strCompileTestResult;
                }
                AddEntryToResults(strToAdd);
            }

        } catch (Exception ex) {
            System.out.println("error at util.PerformSimulationForBug()");
            ex.printStackTrace();
        }
    }

    void PerformSimulation(String projectName, String patchId, File fileInside) {
        try {
            for (File fileInsideProject : fileInside.listFiles()) {
                if (fileInsideProject.isDirectory()) {
                    PerformSimulation(projectName, patchId, fileInsideProject);
                } else {
                    String dirMutant = fileInsideProject.getPath();
                    String strMutantFileName = fileInsideProject.getName();
                    if (!strMutantFileName.matches(data.strExtensionCheck)) {
                        continue;
                    }
                    System.out.println("processing " + strMutantFileName);
                    String[] arrMutantFileName = strMutantFileName.replace(data.strSupportedLangExt, "").split(Pattern.quote("_"));
                    String strOriginalFileName = arrMutantFileName[0] + data.strSupportedLangExt;
                    String strMutantID = arrMutantFileName[1];
                    String strPrjWithPatchId = projectName + "_" + patchId;
                    String strToAdd = strPrjWithPatchId + data.strPipe + strMutantFileName;
                    Boolean shouldSimulate = true;
                    for (String entry : lstSimulation) {
                        if (entry.contains(strToAdd)) {
                            if (entry.trim().equals(strToAdd.trim())) {
                                RemoveEntryFromResults(entry);
                                System.out.println("re-processing " + strMutantFileName);
                            } else {
                                shouldSimulate = false;
                            }
                            break;
                        }
                    }
                    if (shouldSimulate == false) {
                        System.out.println("already processed " + strMutantFileName);
                        continue;
                    }
                    String semiDirOriginal = dirMutant.replace("\\", "/").replace(data.dirMutSrc + "/" + strPrjWithPatchId + "/", "")
                            .replace(strMutantFileName, strOriginalFileName).replace("/" + arrMutantFileName[0] + data.strMutants, "");
                    String dirFixedPrj = data.dirSimulation + "/" + strPrjWithPatchId + "/" + data.strFixed;

                    //checking if test results file already exists so we do not need to compile and test mutant
                    String strTestFileName = strMutantFileName.replace(data.strSupportedLangExt, "") + data.strTestPartialFileName;
                    String strCompileTestResult;
                    if (FileExists(data.dirSimulation + "/" + strPrjWithPatchId + "/" + strTestFileName)) {
                        strCompileTestResult = GetCompileAndTestResult(strPrjWithPatchId, dirFixedPrj, strTestFileName);
                        strToAdd += data.strPipe + strCompileTestResult;
                        AddEntryToResults(strToAdd);
                        continue;
                    }
                    //file not found so we have to process
                    /*String strInitialSemiDirOriginal = data._mapInitialSemiDirOriginal.get(projectName);
                    String dirOriginal = dirFixedPrj + strInitialSemiDirOriginal + semiDirOriginal;
                    String dirContainingOriginal = dirOriginal.replace("/" + strOriginalFileName, "");
                    if (!FileExists(dirOriginal)) {
                        System.out.println(dirOriginal + " does not exist, skipping...");
                        lstSimulation.add(strToAdd);
                        continue;
                    }*/
                    String strInitialSemiDirOriginal = null;
                    String dirOriginal = null;
                    String dirContainingOriginal = null;
                    for (String newStrInitialSemiDirOriginal : data.lstInitialSemiDirOriginal) {
                        strInitialSemiDirOriginal = newStrInitialSemiDirOriginal;
                        dirOriginal = dirFixedPrj + strInitialSemiDirOriginal + semiDirOriginal;
                        dirContainingOriginal = dirOriginal.replace("/" + strOriginalFileName, "");
                        if (FileExists(dirOriginal)) {
                            break;
                        } else {
                            strInitialSemiDirOriginal = null;
                            dirOriginal = null;
                            dirContainingOriginal = null;
                            continue;
                        }
                    }

                    if (dirOriginal == null || dirContainingOriginal == null) {
                        System.out.println(dirOriginal + " does not exist, skipping...");
                        AddEntryToResults(strToAdd);
                        continue;
                    }

                    LinkedList<String> lstOrig = ReadFileToList(dirOriginal);
                    LinkedList<String> lstMut = ReadFileToList(dirMutant);
                    System.out.println("replacing " + dirOriginal + " with " + dirMutant);
                    //String[] arrString = ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOriginal);
                    //Integer errorCode = Integer.parseInt(arrString[0]);
                    Integer errorCode = ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOriginal, null);
                    if (errorCode != 0) {
                        AddEntryToResults(strToAdd);
                        continue;
                    }
                    WriteListToFile(dirContainingOriginal, strOriginalFileName, lstMut);
                    //String strTestFileName = strMutantFileName.replace(data.strSupportedLangExt, "") + data.strTestPartialFileName;
                    strCompileTestResult = GetCompileAndTestResult(strPrjWithPatchId, dirFixedPrj, strTestFileName);
                    strToAdd += data.strPipe + strCompileTestResult;
                    System.out.println("restoring " + dirOriginal);
                    //arrString = ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOriginal);
                    //errorCode = Integer.parseInt(arrString[0]);
                    errorCode = ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOriginal, null);
                    if (errorCode != 0) {
                        ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + dirOriginal, null);
                    }
                    WriteListToFile(dirContainingOriginal, strOriginalFileName, lstOrig);
                    AddEntryToResults(strToAdd);
                }
            }
        } catch (Exception ex) {
            System.out.println("error at util.PerformSimulation()");
            ex.printStackTrace();
        }
    }

    /*public String[] ExecuteProcessGetErrorCodeAndSaveOutput(String commandToExec) throws Exception {
        try {
            String strOutput = "";
            ProcessBuilder builder = new ProcessBuilder(data.strCommandExecutionInitial01, data.strCommandExecutionInitial02, commandToExec);
            System.out.println("running command: " + data.strCommandExecutionInitial01 + " " + data.strCommandExecutionInitial02 + " " + commandToExec);
            Process p = builder.start();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            Integer count = 0;
            Boolean shouldWait = true;
            Boolean readSuccess = false;
            while (shouldWait) {
                if (reader.ready()) {
                    while ((line = reader.readLine()) != null) {
                        //System.out.println(line);
                        if (strOutput.isEmpty()) {
                            strOutput = line;
                        } else {
                            strOutput += "\r\n" + line;
                        }
                    }
                    shouldWait = false;
                    readSuccess = true;
                } else {
                    if (count >= 10) {
                        System.out.println("\ntimeout\n");
                        shouldWait = false;
                    } else {
                        count++;
                        Thread.sleep(3 * 1000);
                    }
                }
            }
            Integer exitCode;
            if (readSuccess) {
                exitCode = p.waitFor();
            } else {
                exitCode = 1;
            }

            System.out.println(commandToExec + " exited with error code: " + exitCode);
            return new String[]{exitCode.toString(), strOutput};
        } catch (Exception ex) {
            System.out.println("error at util.ExecuteProcessGetErrorCodeAndSaveOutput()");
            throw ex;
        }
    }*/
    public Integer ExecuteProcessGetErrorCodeAndSaveOutput(String commandToExec, String dirOutputFile) throws Exception {
        try {
            if (dirOutputFile != null && dirOutputFile.isEmpty() == false) {
                commandToExec += " > " + dirOutputFile;
            }
            ProcessBuilder builder = new ProcessBuilder(data.strCommandExecutionInitial01, data.strCommandExecutionInitial02, commandToExec);
            System.out.println("running command: " + data.strCommandExecutionInitial01 + " " + data.strCommandExecutionInitial02 + " " + commandToExec);
            Process p = builder.start();
            Boolean success = p.waitFor(5, TimeUnit.MINUTES);
            Integer exitCode = 1;
            if (success) {
                exitCode = p.exitValue();
            } else {
                System.out.println("\ntimeout\n");
                if (p.isAlive()) {
                    p.destroyForcibly();
                }
            }
            System.out.println("exit code: " + exitCode);
            return exitCode;
        } catch (Exception ex) {
            System.out.println("error at util.ExecuteProcessGetErrorCodeAndSaveOutput()");
            throw ex;
        }
    }

    public String GetCompileAndTestResult(String strPrjWithPatchId, String dirFixedPrj, String strTestFileName) {
        try {
            String strToAdd;
            if (FileExists(data.dirSimulation + "/" + strPrjWithPatchId + "/" + strTestFileName)) {
                System.out.println("found test results at " + data.dirSimulation + "/" + strPrjWithPatchId + "/" + strTestFileName);
                LinkedList<String> lstTestResults = ReadFileToList(data.dirSimulation + "/" + strPrjWithPatchId + "/" + strTestFileName);
                if (lstTestResults == null || lstTestResults.isEmpty() || lstTestResults.get(0).contains(data.strFailing) == false) {
                    strToAdd = data.strCompile + data.strColonSpace + "0";
                    strToAdd += data.strPipe + data.strTest + data.strColonSpace + "0";
                } else {
                    strToAdd = data.strCompile + data.strColonSpace + "1";
                    strToAdd += data.strPipe + data.strTest + data.strColonSpace + "1";
                    strToAdd += data.strPipe + lstTestResults.get(0);
                }
                return strToAdd;
            }
            strToAdd = CompileAndTest(strPrjWithPatchId, dirFixedPrj, strTestFileName);
            return strToAdd;
        } catch (Exception ex) {
            System.out.println("error at util.GetCompileAndTestResult()");
            ex.printStackTrace();
            return "";
        }
    }

    public String CompileAndTest(String strPrjWithPatchId, String dirFixedPrj, String strTestFileName) {
        String strToAdd = "";
        try {
            //String strCompileFileName = strMutantFileName.replace(data.strSupportedLangExt, "") + data.strCompilePartialFileName;
            String strCompilationCommand = data.strInitialCommandForCd + dirFixedPrj + data.strPartialCommandForCompile;
            //String[] arrString = ExecuteProcessGetErrorCodeAndSaveOutput(strCompilationCommand);
            //Integer errorCode = Integer.parseInt(arrString[0]);
            Integer errorCode = ExecuteProcessGetErrorCodeAndSaveOutput(strCompilationCommand, null);
            if (errorCode == 0) {
                strToAdd = data.strCompile + data.strColonSpace + "1";
            } else {
                strToAdd = data.strCompile + data.strColonSpace + "0";
            }
            //String compilationResults = arrString[1];
            //WriteStringToFile(data.dirSimulation + "/" + strPrjWithPatchId, strCompileFileName, compilationResults);

            String strTestCommand = data.strInitialCommandForCd + dirFixedPrj + data.strPartialCommandForTest;
            /*arrString = ExecuteProcessGetErrorCodeAndSaveOutput(strTestCommand);
            errorCode = Integer.parseInt(arrString[0]);
            if (errorCode == 0) {
                strToAdd += data.strPipe + data.strTest + data.strColonSpace + "1";
            } else {
                strToAdd += data.strPipe + data.strTest + data.strColonSpace + "0";
            }
            String testResults = arrString[1];
            WriteStringToFile(data.dirSimulation + "/" + strPrjWithPatchId, strTestFileName, testResults);*/
            String dirOutputFile;
            if (strTestFileName == null || strTestFileName.isEmpty()) {
                dirOutputFile = null;
            } else {
                dirOutputFile = data.dirSimulation + "/" + strPrjWithPatchId + "/" + strTestFileName;
            }

            errorCode = ExecuteProcessGetErrorCodeAndSaveOutput(strTestCommand, dirOutputFile);
            if (errorCode == 0) {
                strToAdd += data.strPipe + data.strTest + data.strColonSpace + "1";
            } else {
                strToAdd += data.strPipe + data.strTest + data.strColonSpace + "0";
            }

            if (strTestFileName != null && strTestFileName.isEmpty() == false) {
                LinkedList<String> lstTestResults = ReadFileToList(dirOutputFile);
                if (lstTestResults != null && lstTestResults.isEmpty() == false) {
                    strToAdd += data.strPipe + lstTestResults.get(0);
                }
            }
            return strToAdd;
        } catch (Exception ex) {
            System.out.println("error at util.CompileAndTest()");
            ex.printStackTrace();
            return strToAdd;
        }
    }

    void InitializeOrUpdateSimulationResults(String option) throws Exception {
        try {
            switch (option) {
                case "load":
                    lstSimulation = new LinkedList();
                    if (FileExists(data.dirSimulation + "/" + data.strSimulationFileName)) {
                        lstSimulation = ReadFileToList(data.dirSimulation + "/" + data.strSimulationFileName);
                    }
                    break;
                case "save":
                    if (!lstSimulation.isEmpty() && requiresWrite) {
                        if (FileExists(data.dirSimulation + "/" + data.strSimulationFileName)) {
                            ExecuteProcessGetErrorCodeAndSaveOutput(data.strDeleteProcessingDir + " " + data.dirSimulation + "/" + data.strSimulationFileName, "");
                        }
                        WriteListToFile(data.dirSimulation, data.strSimulationFileName, lstSimulation);
                        requiresWrite = false;
                        System.out.println("will continue processing within 30 secs...");
                        Thread.sleep(30 * 1000);
                    }
                    break;
            }

        } catch (Exception ex) {
            System.out.println("error at util.InitializeOrUpdateSimulationResults()");
            throw ex;
        }
    }

    void AddEntryToResults(String strToAdd) {
        try {
            lstSimulation.add(strToAdd);
            requiresWrite = true;
        } catch (Exception ex) {
            System.out.println("error at util.AddEntryToResults()");
            throw ex;
        }
    }

    private void RemoveEntryFromResults(String entry) {
        try {
            lstSimulation.remove(entry);
            requiresWrite = true;
        } catch (Exception ex) {
            System.out.println("error at util.RemoveEntryFromResults()");
            throw ex;
        }
    }

    public String GetFlattenedFn(String strCodeFilePath, String strFnPhrase) {
        try {
            String strFlattenedFn = "";
            String strCleanedXML, strCleanedCode;
            System.out.println("processing " + strCodeFilePath + " ...");
            File codeFile = new File(strCodeFilePath);
            String codeFileName = codeFile.getName();
            String className = codeFileName.replace(data.strSupportedLangExt, "");
            String strFileXML = ExecuteProcess(data.strInitialCommandForsrcml, strCodeFilePath);
            if (strFileXML == null) {
                return null;
            }

            String[] arrCleanedCodeWithXML = GetCleanedCodeWithXML(strFileXML);
            strCleanedXML = arrCleanedCodeWithXML[0];
            strCleanedCode = arrCleanedCodeWithXML[1];

            LinkedList<String> listFnXMLs = GetFnXMLS(strCleanedXML);
            strFlattenedFn = FlattenFunction(strCleanedXML, listFnXMLs, strFnPhrase);
            return strFlattenedFn;
        } catch (Exception ex) {
            System.out.println("error at util.GetFlattenedFn()");
            System.out.println(strCodeFilePath);
            ex.printStackTrace();
            return null;
        }
    }

    /* Orig
    public String FlattenFunction(String strCleanedXML, LinkedList<String> listFnXMLs, String strFnPhrase) throws Exception {
        try {
            String retStr = "";
            HashMap<String, String> mapFns = GetAllFunctions(strCleanedXML, listFnXMLs);
            if (mapFns == null || mapFns.isEmpty()) {
                System.out.println("unable to get functions from source code file!");
                return null;
            }
            for (String strFn : mapFns.values()) {
                String strSingleSentencedFn = strFn.replace("\r\n", " ").replaceAll("\\s{2,}", " ").trim();
                String strNoSpaceFn = strSingleSentencedFn.replace(" ", "");
                String strNoSpaceFnPhrase = strFnPhrase.replaceAll("\\s{2,}", " ").replace(" ", "").trim();
                if (strNoSpaceFn.contains(strNoSpaceFnPhrase)) {
                    retStr = strSingleSentencedFn;
                    break;
                }
            }
            return retStr;
        } catch (Exception ex) {
            System.out.println("error at util.FlattenFunction()");
            ex.printStackTrace();
            return null;
        }
    }
     */
    public String FlattenFunction(String strCleanedXML, LinkedList<String> listFnXMLs, String strFnPhrase) throws Exception {
        try {
            String retStr = "";
            HashMap<String, String> mapFns = new HashMap();

            for (String strFnXML : listFnXMLs) {
                if (strCleanedXML.contains(strFnXML) == false) {
                    System.out.println("function not matched in cleaned XML!");
                    continue;
                }
                String strFnCode = ProcessFunction(strFnXML);
                String strSingleSentencedFn = strFnCode.replace("\r\n", " ").replaceAll("\\s{2,}", " ").trim();
                String strNoSpaceFn = strSingleSentencedFn.replace(" ", "");
                String strNoSpaceFnPhrase = strFnPhrase.replaceAll("\\s{2,}", " ").replace(" ", "").trim();
                if (strNoSpaceFn.contains(strNoSpaceFnPhrase)) {
                    retStr = strSingleSentencedFn;
                    break;
                }
            }
            return retStr;
        } catch (Exception ex) {
            System.out.println("error at util.FlattenFunction()");
            ex.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> GetAllFlattenedFns(String strCodeFilePath, LinkedList<String> lstMap) {
        try {
            HashMap<String, String> retMap = new HashMap();
            String strCleanedXML, strCleanedCode;
            System.out.println("processing " + strCodeFilePath + " ...");
            File codeFile = new File(strCodeFilePath);
            String codeFileName = codeFile.getName();
            String className = codeFileName.replace(data.strSupportedLangExt, "");
            String strFileXML = ExecuteProcess(data.strInitialCommandForsrcml, strCodeFilePath);
            if (strFileXML == null) {
                return null;
            }

            String[] arrCleanedCodeWithXML = GetCleanedCodeWithXML(strFileXML);
            strCleanedXML = arrCleanedCodeWithXML[0];
            strCleanedCode = arrCleanedCodeWithXML[1];

            if (data.strTechnique != null && data.strTechnique.isEmpty() == false) {
                //To add spaces
                strCleanedXML = strCleanedXML.replace("><", "> <").replace("[]", "[ ]").replace("()", "( )");
            }

            LinkedList<String> listFnXMLs = GetFnXMLS(strCleanedXML);
            LinkedList<String> lstFlattenedFn = FlattenAllFunctions(strCodeFilePath, strCleanedXML, strCleanedCode, className, listFnXMLs);
            if (lstFlattenedFn == null || lstFlattenedFn.isEmpty()) {
                return null;
            }
            for (String strMap : lstMap) {
                String[] arrMap = strMap.split(Pattern.quote(data.strPipe));
                String strFnPhrase = arrMap[1];
                for (String strFn : lstFlattenedFn) {
                    String strNoSpaceFn = strFn.replace(" ", "");
                    String strNoSpaceFnPhrase = strFnPhrase.replaceAll("\\s{2,}", " ").replace(" ", "").trim();
                    if (strNoSpaceFn.contains(strNoSpaceFnPhrase)) {
                        retMap.put(strFnPhrase, strFn);
                        lstFlattenedFn.remove(strFn);
                        break;
                    }
                }
            }
            return retMap;
        } catch (Exception ex) {
            System.out.println("error at util.GetAllFlattenedFns()");
            System.out.println(strCodeFilePath);
            ex.printStackTrace();
            return null;
        }
    }

    public LinkedList<String> FlattenAllFunctions(String strCodeFilePath, String strCleanedXML, String strCleanedCode,
            String className, LinkedList<String> listFnXMLs) throws Exception {
        try {
            LinkedList<String> retLst = new LinkedList();
            HashMap<String, String> mapFns = GetAllFunctions(strCleanedXML, listFnXMLs);
            if (mapFns == null || mapFns.isEmpty()) {
                System.out.println("unable to get functions from source code file!");
                return null;
            }
            for (String strFn : mapFns.values()) {
                String strSingleSentencedFn = strFn.replace("\r\n", " ").replaceAll("\\s{2,}", " ").trim();
                retLst.add(strSingleSentencedFn);
            }
            return retLst;
        } catch (Exception ex) {
            System.out.println("error at util.FlattenAllFunctions()");
            ex.printStackTrace();
            return null;
        }
    }

    public double calculateOchiai(Set<String> brokenTestsByBug, Collection<String> brokenTestsByMutant) {
        try {
            assert brokenTestsByBug != null && !brokenTestsByBug.isEmpty();
            if (brokenTestsByMutant == null || brokenTestsByMutant.isEmpty()) {
                return 0d;
            }
            Set<String> intersection = intersection(brokenTestsByBug, brokenTestsByMutant);
            double prod = brokenTestsByBug.size() * brokenTestsByMutant.size();
            if (prod == 0d || intersection.isEmpty()) {
                return 0d;
            }
            return ((double) intersection.size()) / Math.sqrt(prod);
        } catch (Exception ex) {
            System.out.println("error at util.calculateOchiai()");
            throw ex;
        }
    }

    public <T> Set<T> intersection(Collection<T> list1, Collection<T> list2) {
        try {
            Set<T> set = new HashSet<>();
            if (list1 != null && list2 != null && !list1.isEmpty() && !list2.isEmpty()) {
                for (T t : list1) {
                    if (list2.contains(t)) {
                        set.add(t);
                    }
                }
            }
            return set;
        } catch (Exception ex) {
            System.out.println("error at util.intersection()");
            throw ex;
        }
    }
}
