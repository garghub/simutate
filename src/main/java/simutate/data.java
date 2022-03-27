/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simutate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author aayush.garg
 */
public class data {

    static Boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows") ? true : false;
    static String dirMain = isWindows ? "D:/ag/github/mutants_sensitivity" : "/home/agarg/ag/mutation"; ////atlas/users/aayush.garg/c/github/mutation
    static String strCommandExecutionInitial01 = isWindows ? "cmd.exe" : "bash";
    static String strCommandExecutionInitial02 = isWindows ? "/c" : "-c";
    //for this srcml is not in docker
    //static String strInitialCommandForsrcml = isWindows ? "srcml" : "docker exec ag /home/agarg/ag/srcml/downloads/srcML-master/bin/srcml";
    static String strInitialCommandForsrcml = "srcml";

    static String strAbstract = "abstract";
    static String strUnabstract = "unabstract";
    static String strProcessSourcePatches = "processsourcepatches";
    static String strSimulate = "simulate";
    static String strFlatten = "flatten";
    static String strGetAllTests = "getalltests";
    static String strCompare = "compare";
    static String strProcessLocationMapping = "processlocationmapping";
    static String strFlattenFixes = "flattenfixes";
    static String strCompareFixes = "comparefixes";
    static String strGetFailingTests = "getfailingtests";
    static String strProcessSubsumingMutantDiff = "processsubsumingmutantdiff";
    static String strConsolidateChangedLinesDataByMutants = "consolidatechangedlinesdatabymutants";
    static String strTraversingMutantsForCerebro = "traversingmutantsforcerebro";
    static String strTraversingAllMutantsForCerebroNoCSV = "traversingallmutantsforcerebronocsv";

    //static String strDirSrcCode = "src/main/java";
    static String strSupportedLangExt = ".java";
    static String strTxtExt = ".txt";
    static String strExtensionCheck = ".*\\" + strSupportedLangExt + "$";
    static String strTxtExtensionCheck = ".*\\" + strTxtExt + "$";
    static String dirSrcMLBatchFile;// "/home/agarg/ag/gassert/misc";
    static String strProcessedFilesFileName = "processedfiles.txt";
    static String strProcessedFilesBkpFileName = "processedfilesbackup.txt";
    //do not need it anymore
    //static String srcMLBatchFilePath = dirSrcMLBatchFile + "/srcML.bat";
    static String tmpXMLFileName = "temp.xml";
    static String tmpCodeFileName = "temp" + strSupportedLangExt;
    static String strNxtLineSeparator = "\\r\\n";
    static String strAbstractClassCheck = "<specifier>abstract</specifier>";
    static String strAssertStmtXML = "<assert>assert<expr><operator>(</operator><literal type=\"boolean\">true</literal><operator>)</operator></expr>;</assert>";
    static String strFnEndStmtXML = "}</block></function>";
    static String strReturnXMLStartStmt = "<return>";
    static String strOrigDirName = "orig";
    static String strTestDaikonDirName = "test-daikon";
    static String strTestDaikonFileName = "CreateTestRunnerMainForDaikon.java";
    static String strPackageSentence = "package ";
    static String strGradleBuildFileName = "build.gradle";
    static String strGradleBuildBkpFileName = "build_gradle.txt";
    static String strGradleBuildFileSentenceToMatch = "    targetClasses = ['";
    static String strGradleBuildFileSentenceToCompleteRemaining = "']";
    static String strGradleBuildFileSentenceToComplete = ".*" + strGradleBuildFileSentenceToCompleteRemaining; //".*']";
    static String strClassNameInputFileName = "classname.input";
    static String strClassNameInputBkpFileName = "classname_input.txt";
    static String strCreateProcessingDirPart01 = "mkdir -p";// /mnt/ag/gassert/GAssert/subjects/Angle_Diff
    static String strCreateProcessingDirPart02 = "&& cp -r";// /mnt/ag/gassert/GAssert/subjects/jts/* /mnt/ag/gassert/GAssert/subjects/Angle_Diff";
    static String strInitialCommandForGAssert;// "cd /home/agarg/ag/gassert/GAssert/scripts && ./run_gassert.sh";
    static String strToolToRunInGAssertScript = "GASSERT";
    static String strInitialAssertionsToUseInGAssertScript = "daikon.assertions";
    static String strMinutesToRunGAssert;// = "10";
    static String dirGenOutput;// dirSrcMLBatchFile + "/genout";
    static String dirPrjOutName = "output";
    static String strOutputAssertionsFileName = "output.assertions";
    static String dirPrjLogsName = "logs";
    static String strAssertedFnFileName = "assertedfn.txt";
    static String strAssertedFileName = "assertedfile.txt";
    static String strFnSrcmlFirstWord = "<unit>";
    static String strFnSrcmlLastWord = "</unit>";
    static String strNoOut = "-noout";
    static String strNoGen = "-nogen";
    static String strDeleteProcessingDir = "rm -r";
    static String dirInputAssertions = "input-assertions";
    static Integer intMinThresholdCountForFnSentences = 5;
    static String strIdiomFileName = "idiom.txt";
    static String strInitialCommandForsrc2abs01 = "cd ";
    static String strInitialCommandForsrc2abs02 = " && java -jar D:/ag/github/src2abs/src2abs-master/target/src2abs-0.1-jar-with-dependencies.jar single method ";
    static String strLhsFileName = "lhs.txt";
    static String strLhsLocsFileName = "lhslocs.txt";
    static String strGenRhsFileName = "genrhs.txt";
    static String strMapExtensionCheck = ".*\\" + strSupportedLangExt + ".map" + "$";
    static String strAbs = "_abs";
    static String strMutants = "_mutants";
    static String strMap = "map";
    static String strMapFileName = strMap + strTxtExt;
    static String strPipe = " | ";
    static String strColonSpace = ": ";
    static String strPatchFnMap = "patchfnmap.txt";
    static String strFnMap = "fnmap.txt";
    static String strSrcPatchExt = ".src.patch";
    static String strSrcPatchExtCheck = ".*\\" + strSrcPatchExt + "$";
    static String dirSrc = dirMain + "/experiment";
    static String dirBuggySrc = dirSrc + "_bugs";
    static String strFnsBeginEndMap = "mapfnsbeginend";
    static String dirMutSrc = dirSrc + strMutants;
    static String dirPatches = dirMain + "/patches";
    static String strToLookInPatch = "@@ ";
    static String strToLookInPatchForFileLocation = "diff --git";
    static String strProjectNameForSimulation;
    static String strAllProjectsForSimulation = "all";
    static String dirBugId;
    static String strSimulation = "simulation";
    static String dirSimulation = dirMain + "/" + strSimulation;
    static String strAllTests = "alltests";
    static String dirAllTests = dirMain + "/" + strAllTests;
    static String dirSimulationForBugsOrFixes = dirSimulation + "-nmt";
    static String dirSyntactic = dirMain + "/syntactic";
    static String dirSyntacticFixes = dirMain + "/syntacticfixes";
    static String strBuggy = "b";
    static String strFixed = "f";
    static String strSimulationFileName;// = "simulation.txt";
    static String strInitialCommandForAgd4jDocker = ""; //"docker exec agd4j";
    static String strInitialCommandForDefects4j01 = "/home/agarg/ag/defects4j/defects4j/framework/bin/defects4j";
    static String strInitialCommandForDefects4j = strInitialCommandForAgd4jDocker == "" ? strInitialCommandForDefects4j01 : strInitialCommandForAgd4jDocker + " " + strInitialCommandForDefects4j01;
    static String strInitialCommandForCheckout01 = " checkout -p ";
    static String strInitialCommandForCheckout02 = " -v ";
    static String strInitialCommandForCheckout03 = " -w ";
    static String strSudoDeleteProcessingDir01 = strInitialCommandForAgd4jDocker + " "
            + strCommandExecutionInitial01 + " " + strCommandExecutionInitial02 + " '" + "rm -r";
    static String strApostrophe = "'";
    static String strInitialCommandForCdInDocker = strInitialCommandForAgd4jDocker + " "
            + strCommandExecutionInitial01 + " " + strCommandExecutionInitial02 + " '" + "cd ";
    static String strInitialCommandForCd = "cd ";
    static String strCompile = "compile";
    static String strTest = "test";
    static String strPartialCommandForCompile = " && " + strInitialCommandForDefects4j01 + " " + strCompile;
    static String strPartialCommandForTest = " && " + strInitialCommandForDefects4j01 + " " + strTest;
    static String strCompilePartialFileName = "_" + strCompile + strTxtExt;
    static String strTestPartialFileName = "_" + strTest + strTxtExt;
    static String strAllTestPartialFileName = "_" + strAllTests + strTxtExt;
    static String strSimulationListLoad = "load";
    static String strSimulationListSave = "save";
    static String strFailing = "Failing";
    static String strFailingTests = strFailing + " tests";
    static String strAllTestsFileName = "all_tests";

    static String strFlatteningMapFileName = "flatteningmap.txt";
    static String strLocationMapFileName = "locationmap.txt";
    static String strFlattenedMutatedFnsFileName = "flattenedmutatedfns.txt";
    static String strFlattenedBuggyFnsFileName = "flattenedbuggyfns.txt";
    static String strFlattenedFixedFnsFileName = "flattenedfixedfns.txt";
    static String strTechnique;

    static String strOchiai = "OCHIAI";
    static String strSemantic = "semantic";
    static String strSemanticFixes = "semanticfixes";
    static String strSemanticSimilarityFileName = "semanticsimilarity.txt";
    static String strOverallSemanticSimilarityFileName = "overallsemanticsimilarity.txt";

    static String strFnStartTag = "<function>";
    static String strFnEndTag = "</function>";

    static String strNone = "None";
    static String strCsvExt = ".csv";

    /*static LinkedList<String> lstProjects = new LinkedList<String>(Arrays.asList(
            "Cli", "Codec", "Collections", "Compress", "Csv",
            "Gson", "JacksonCore", "JacksonDatabind", "JacksonXml", "Jsoup",
            "JxPath", "Lang", "Math", "Mockito", "Time"
    ));*/
    static LinkedList<String> lstInitialSemiDirOriginal = new LinkedList<String>(Arrays.asList(
            "/gson/src/main/java/", "/src/main/java/", "/src/java/", "/src/"
    ));
    static LinkedList<String> lstAccessModifiers = new LinkedList<String>(Arrays.asList(
            "private", "public", "protected", "final", "{"
    ));

    static String dirMutOperators = dirMain + "/mutation_operators";
    static String csvSubsumingMutants = "tools_complement_mutants_id.csv";
    static String _elementSymbolSeparator = ",";
    static String strMoved = "moved";
    static String headerOriginal = "original";
    static String headerMutant = "mutant";
    static String strEndOfMethod = " }";
    static String dirAll = dirMain + "/similarity-all";
    static String csvConsolidatedData = "all_compilable_mutants.csv";
    static String headerChangedLines = "Changed_Lines";
    static String dirCerebro = dirMain + "/cerebro";
    static String dirMethods = dirCerebro + "/methods";
    static String strToolSpecificMutantsDataCSVName01 = "all_mutants_data_";
    static String headerCerebroPred = "Cerebro_Pred";
    static String strOperatorForCerebro = "OPERATOR4CEREBRO";
    static String dirJsonSets = dirCerebro + "/jsonsets";
    static String jsonFileExtension = ".json";
    static String strMutOperatorPrefix = "MST";
    static String strMutOperatorSuffix = "MSP";
}
