#!/usr/bin/env python
# coding: utf-8

import pandas as pd
import os
import sys
import seaborn as sns
import scipy.stats as stats
import pickle
from pathlib import Path
import statistics

from matplotlib import pyplot as plt
from matplotlib.pyplot import figure
import numpy as np

def ReadFileToList(dirFile):
    print("\nreading ", dirFile)
    lst = []
    with open(dirFile, 'r') as filehandle:
        for line in filehandle:
            # remove linebreak which is the last character of the string
            currentPlace = line[:-1]
            # add item to the list
            lst.append(currentPlace)
    return lst

def hist_dist(df, filename, filenamesuffix):
    print("\nplotting for ", filename, " and ", filenamesuffix)
    figure(figsize=(20, 10))
    # plot 1:
    ax = plt.subplot(3, 1, 1)
    plt.scatter(df.index, df['OCHIAI'], label='OCHIAI')
    plt.scatter(df.index, df['BLEU'], label='BLEU')
    # plt.xlabel('Mutants')
    # plt.ylabel('Scale')
    # plt.title('Semantic-Syntactic co-relation')
    plt.legend()
    ax.axes.xaxis.set_visible(False)

    # plot 2:
    ax = plt.subplot(3, 1, 2)
    plt.scatter(df.index, df['OCHIAI'], label='OCHIAI')
    plt.scatter(df.index, df['JACCARD'], label='JACCARD')
    # plt.xlabel('Mutants')
    # plt.ylabel('Scale')
    plt.legend()
    ax.axes.xaxis.set_visible(False)

    # plot 2:
    ax = plt.subplot(3, 1, 3)
    plt.scatter(df.index, df['OCHIAI'], label='OCHIAI')
    plt.scatter(df.index, df['COSINE'], label='COSINE')
    # plt.xlabel('Mutants')
    # plt.ylabel('Scale')
    plt.legend()
    ax.axes.xaxis.set_visible(False)
    if filenamesuffix == "all":
        stitle = "All"
    elif filenamesuffix == "sem-gt80p":
        stitle = "Semantic similarity >= 0.8"
    elif filenamesuffix == "syn-gt80p":
        stitle = "Syntactic similarity >= 0.8"
    elif filenamesuffix == "patch-based":
        stitle = "Mutants on patch changed fns"
    plt.suptitle('Semantic vs Syntactic - ' + stitle)
    filename = filename + "-" + stitle
    plt.savefig(dirSimilarity + "/" + filename + '.pdf')
    plt.savefig(dirSimilarity + "/" + filename + '.png')
    #plt.show()

def scatter_plot(data, parax, paray, filename, filenamesuffix):
    print("\nplotting for ", filename, " and ", filenamesuffix)
    label_parax = parax
    label_paray = paray
    if "RQ3" in filenamesuffix:
        label_parax = "\u0394" + label_parax + " |M2 - M1|"
        label_paray = "\u0394" + label_paray + " |M2 - M1|"
    if "RQ4" in filenamesuffix:
        label_parax = "Syntactic Distance (1 - " + label_parax + ")"
        label_paray = "Failing tests (#)"
    #axeLeft = sns.jointplot(data=data, x=parax, y=paray, kind="reg")
    axeLeft = sns.JointGrid(data=data, x=parax, y=paray)
    axeLeft.plot(sns.regplot, sns.boxplot)
    axeLeft.set_axis_labels(xlabel=label_parax, ylabel=label_paray, fontsize=12)
    pr_axeLeft, pp_axeLeft = stats.pearsonr(data[parax], data[paray])
    kr_axeLeft, kp_axeLeft = stats.kendalltau(data[parax], data[paray])
    # # if you choose to write your own legend, then you should adjust the properties then
    phantom_axeLeft, = axeLeft.ax_joint.plot([], [], linestyle="", alpha=0)
    # # here graph is not a ax but a joint grid, so we access the axis through ax_joint method
    #label_axeLeft = 'pearson: r={:f}, p={:f}\nkendall: r={:f}, p={:f}'.format(pr_axeLeft, pp_axeLeft, kr_axeLeft, kp_axeLeft)
    label_axeLeft = 'pearson: r={:.3f}, p={:.3f}\nkendall: r={:.3f}, p={:.3f}'.format(
        round(pr_axeLeft, 3),
        round(pp_axeLeft, 3),
        round(kr_axeLeft, 3),
        round(kp_axeLeft, 3))
    # # label_pearson = 'r={:f}, p={:f}'.format(pr, pp)
    axeLeft.ax_joint.legend([phantom_axeLeft], [label_axeLeft], fontsize="15")

    #plt.tight_layout()
#     if filenamesuffix == "RQ1_all___Box_plot":
#         stitle = "All"
#     elif filenamesuffix == "RQ1_sem_gt80p___Box_plot":
#         stitle = "Semantic similarity >= 0.8"
#     elif filenamesuffix == "RQ1_syn_gt80p___Box_plot":
#         stitle = "Syntactic similarity >= 0.8"
#     elif filenamesuffix == "RQ2_patch_based___Box_plot":
#         stitle = "Mutants on patch changed fns"
#     elif filenamesuffix == "RQ3_Random_Lines___Box_plot":
#         stitle = "Difference in scores based on random sentences"
#     elif filenamesuffix == "RQ3_Changed_Lines___Box_plot":
#         stitle = "Difference in scores based on patch affected sentences"
#     else:
#         stitle = filenamesuffix
#     plt.suptitle('Semantic vs Syntactic - ' + stitle)
    filename = filename + "_" + filenamesuffix
    plt.savefig(dirSimilarity + "/" + filename + ".pdf", format='pdf')
    plt.savefig(dirSimilarity + "/" + filename + ".png", format='png')
    #plt.show()
    #print()

def plot_this(df_arg, filenamesuffix):
    if len(df_arg) <= 0:
        print("\ncannot plot ", filenamesuffix, " due to empty set")
        return
    print("\nplotting ", filenamesuffix)
    #df = df.sort_values(['OCHIAI', 'BLEU'], ascending=True)
    #df = df.reset_index(drop=True)
    #hist_dist(df, "plot-" + technique, filenamesuffix)
#     scatter_plot(df, "BLEU", "OCHIAI", "scatter_plot_bleu_ochiai-" + technique, filenamesuffix)
#     scatter_plot(df, "JACCARD", "OCHIAI", "scatter_plot_jaccard_ochiai-" + technique, filenamesuffix)
#     scatter_plot(df, "COSINE", "OCHIAI", "scatter_plot_cosine_ochiai-" + technique, filenamesuffix)
    scatter_plot(df_arg, "BLEU", "OCHIAI", "Scatter-Plot-" + technique + "_bleu_ochiai", filenamesuffix)
    scatter_plot(df_arg, "JACCARD", "OCHIAI", "Scatter-Plot-" + technique + "_jaccard_ochiai", filenamesuffix)
    scatter_plot(df_arg, "COSINE", "OCHIAI", "Scatter-Plot-" + technique + "_cosine_ochiai", filenamesuffix)

def try_parse_int(string):
    '''helper to parse int from string without erroring on empty or misformed string'''
    try:
        return int(string)
    except Exception:
        return 0 

def get_locations(dirMain_arg, technique_arg, df_Compilable):
    strSimilarityFolderName = "similarity" + "-" + technique
    dirSimilarity = dirMain + "/" + strSimilarityFolderName
    strLocationsPickleName = "locations.pkl"
    dirLocationsPickle = dirSimilarity + "/" + strLocationsPickleName
    fileLocationsPickle = Path(dirLocationsPickle)
    
    if not fileLocationsPickle.is_file():
        print("\nfile not found ", dirLocationsPickle)
        dirMutants = dirMain_arg + "/" "experiment_mutants-" + technique_arg
        strLocationMapFileName = "locationmap.txt"
        dirLocationMap = dirMutants + "/" + strLocationMapFileName
        fileLocationMap = open(dirLocationMap,"r")
        lstLocations = fileLocationMap.readlines()
        df_Locations_Returned = pd.DataFrame(columns=['BUG','MUTANT','ORIGINAL', 'LOCATION'])
        
        dictLocations = {}
        print("\ncreating dictLocations")
        for str in lstLocations:
            arrStr = str.split(" | ")
            strToSearch = arrStr[0] + " | " + arrStr[1]
            dictLocations[strToSearch] = str

        dictCompilable = {}
        print("\ncreating dictCompilable")
        for index, row in df_Compilable.iterrows():
            strToSearch = row['BUG'] + " | " + row['MUTANT']
            dictCompilable[strToSearch] = ""

        for keyCompilable in dictCompilable:
            if keyCompilable in dictLocations:
                str = dictLocations[keyCompilable]
                arrStr = str.split(" | ")
                bug = arrStr[0]
                mutant = arrStr[1]
                if technique_arg == "ibir":
                    location = arrStr[3]
                else:
                    location = try_parse_int(arrStr[3])
                    if location == 0:
                        continue
                original = mutant.split("_")[0] + ".java"
                new_row = {'BUG':bug, 'MUTANT':mutant, 'ORIGINAL': original, 'LOCATION':location}
                df_Locations_Returned = df_Locations_Returned.append(new_row, ignore_index=True)
                print("added in df_Locations_Returned", new_row)
#         for str in lstLocations:
#             arrStr = str.split(" | ")
#             bug = arrStr[0]
#             mutant = arrStr[1]
#             location = try_parse_int(arrStr[3])
#             if location == 0:
#                 continue
#             original = mutant.split("_")[0] + ".java"
#             new_row = {'BUG':bug, 'MUTANT':mutant, 'ORIGINAL': original, 'LOCATION':location}
#             df_Locations_Returned = df_Locations_Returned.append(new_row, ignore_index=True)
#             print("added ", new_row)
        df_Locations_Returned.to_pickle(dirLocationsPickle)
    else:
        print("\nreading from ", dirLocationsPickle)
        df_Locations_Returned = pd.read_pickle(dirLocationsPickle)
    
    
    return df_Locations_Returned

def get_location_based_difference_df(df_passed, df_Locations):
    df_WithLocation = pd.merge(df_passed, df_Locations, how='inner', on = ['BUG', 'MUTANT'])
    dictDfWithLocations = {}
    print("\ncreating dictDfWithLocations")
    for index, row in df_WithLocation.iterrows():
        strToSearch = row['BUG'] + " | " + row['ORIGINAL'] + " | " + "{}".format(row['LOCATION'])
        if strToSearch in dictDfWithLocations:
            dictDfWithLocations[strToSearch] = dictDfWithLocations[strToSearch] + 1
        else:
            dictDfWithLocations[strToSearch] = 1
    print("\ndictDfWithLocations creation complete")

    df_LocationBasedCount = pd.DataFrame(columns=['BUG','ORIGINAL', 'LOCATION', 'COUNT'])
    dictDfLocations = {}
    for key in dictDfWithLocations:
        count = dictDfWithLocations[key]
        arrStrKey = key.split(" | ")
        bug = arrStrKey[0]
        original = arrStrKey[1]
        location = arrStrKey[2]
        new_row = {'BUG':bug, 'ORIGINAL': original, 'LOCATION': location, 'COUNT': count}
        df_LocationBasedCount = df_LocationBasedCount.append(new_row, ignore_index=True)
    
    if technique == "ibir":
        minMutantsOnSameSentence = 2
    else:
        minMutantsOnSameSentence = 4
    df_LocationBasedCount = df_LocationBasedCount[df_LocationBasedCount['COUNT'] > minMutantsOnSameSentence].sort_values(by=['BUG', 'ORIGINAL', 'COUNT'], ascending=False)

    selecteddf = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
    bug = ""
    original = ""
    count = 1
    for index, row in df_LocationBasedCount.iterrows():
        newbug = row["BUG"]
        neworiginal = row["ORIGINAL"]
        location = row["LOCATION"]
        if (bug != newbug) and (original != neworiginal):
            bug = newbug 
            original = neworiginal
            count = 1
        if count <= 10:
            print("found a contender: ", bug, original, location)
            if technique == "ibir":
                newdf = df_WithLocation[(df_WithLocation["BUG"] == bug) 
                                        & (df_WithLocation["ORIGINAL"] == original) 
                                        & (df_WithLocation["LOCATION"] == location)]
            else:
                newdf = df_WithLocation[(df_WithLocation["BUG"] == bug) 
                                        & (df_WithLocation["ORIGINAL"] == original) 
                                        & (df_WithLocation["LOCATION"] == try_parse_int(location))].head(4)
            ochiai = None
            bleu = None
            jaccard = None
            cosine = None
            for index, row in newdf.iterrows():
                if ochiai is None:
                    ochiai = row['OCHIAI']
                    bleu = row['BLEU']
                    jaccard = row['JACCARD']
                    cosine = row['COSINE']
                else:
                    ochiai = abs(ochiai - row['OCHIAI'])
                    bleu = abs(bleu - row['BLEU'])
                    jaccard = abs(jaccard - row['JACCARD'])
                    cosine = abs(cosine - row['COSINE'])
                    new_row = {'BUG': row['BUG'], 'MUTANT': row['MUTANT'], 'OCHIAI':ochiai, 'BLEU':bleu, 'JACCARD':jaccard, 'COSINE':cosine}
                    print(new_row)
                    selecteddf = selecteddf.append(new_row, ignore_index=True)
                    ochiai = None
                    bleu = None
                    jaccard = None
                    cosine = None
        count = count + 1
    return selecteddf

#start
n = len(sys.argv)
if n < 2 :
    print("\nplease pass as argument - the name of the technique (e.g. nmt / codebert / ...)")
    exit()

dirMain = "/home/agarg/ag/mutation"
technique = sys.argv[1]
# dirMain = "D:/ag/github/mutants_sensitivity"
# technique = "nmt"

#RQ1

strSimilarityFolderName = "similarity" + "-" + technique
dirSimilarity = dirMain + "/" + strSimilarityFolderName
strOverallSimilarityFileName = "overallsimilarity.txt"
dirSimilarityFile  = dirSimilarity + "/" + strOverallSimilarityFileName

strOverallSimilarityPickleName = "overallsimilarity.pkl"
dirOverallSimilarityPickle = dirSimilarity + "/" + strOverallSimilarityPickleName
fileOverallSimilarityPickle = Path(dirOverallSimilarityPickle)
strPatchBasedOverallSimilarityPickleName = "patchbasedoverallsimilarity.pkl"
dirPatchBasedOverallSimilarityPickle = dirSimilarity + "/" + strPatchBasedOverallSimilarityPickleName
filePatchBasedOverallSimilarityPickle = Path(dirPatchBasedOverallSimilarityPickle)

df = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])

if not fileOverallSimilarityPickle.is_file():
    print("\nfile not found ", dirOverallSimilarityPickle)
    lstOverallSimilarity = []
    fileSimilarity = open(dirSimilarityFile,"r")
    lstOverallSimilarity = fileSimilarity.readlines()
    
    for str in lstOverallSimilarity:
        if "OCHIAI: -1.0" in str:
            continue
        arrStr = str.split(" | ")
        new_row = {'BUG':arrStr[0], 'MUTANT':arrStr[1], 'OCHIAI':float(arrStr[2].replace("OCHIAI: ", "")), 'BLEU':float(arrStr[3].replace("BLEU: ", "")), 'JACCARD':float(arrStr[4].replace("JACCARD: ", "")), 'COSINE':float(arrStr[5].replace("COSINE: ", ""))}
        df = df.append(new_row, ignore_index=True)
        print("added ", new_row)

    df = df.loc[df['OCHIAI'] >= 0]
    
    df.to_pickle(dirOverallSimilarityPickle)
else:
    print("\nreading from ", dirOverallSimilarityPickle)
    df = pd.read_pickle(dirOverallSimilarityPickle)

plot_this(df, "RQ1_all___Box_plot")

df_sem_gt80p = df.loc[df['OCHIAI'] >= 0.8]
plot_this(df_sem_gt80p, "RQ1_sem_gt80p___Box_plot")

df_syn_gt80p = df.loc[(df['BLEU'] >= 0.8) & (df['JACCARD'] >= 0.8) & (df['COSINE'] >= 0.8)]
plot_this(df_syn_gt80p, "RQ1_syn_gt80p___Box_plot")

#RQ2

dirMutants = dirMain + "/" "experiment_mutants-" + technique
strPatchFnMapFileName = "patchfnmap.txt"
dirPatchFnMap = dirMutants + "/" + strPatchFnMapFileName
filePatchFnMap = open(dirPatchFnMap,"r")
lstPatchChangedMutants = filePatchFnMap.readlines()
df_patch = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])

if not filePatchBasedOverallSimilarityPickle.is_file():
    print("\nfile not found ", dirPatchBasedOverallSimilarityPickle)
    dictPatchChangedMutants = {}
    print("\ncreating dictPatchChangedMutants")
    for str in lstPatchChangedMutants:
        arrStr = str.split(" | ")
        strToSearch = arrStr[0] + " | " + arrStr[1]
        dictPatchChangedMutants[strToSearch] = str
    
    dictDf = {}
    print("\ncreating dictDf")
    for index, row in df.iterrows():
        strToSearch = row['BUG'] + " | " + row['MUTANT']
        dictDf[strToSearch] = row
    
    for keyPatchChangedMutants in dictPatchChangedMutants:
        if keyPatchChangedMutants in dictDf:
            row = dictDf.get(keyPatchChangedMutants)
            df_patch = df_patch.append(row, ignore_index=True)
            print("added based on patch ", row)

    df_patch.to_pickle(dirPatchBasedOverallSimilarityPickle)
else:
    print("\nreading from ", dirPatchBasedOverallSimilarityPickle)
    df_patch = pd.read_pickle(dirPatchBasedOverallSimilarityPickle)

plot_this(df_patch, "RQ2_patch_based___Box_plot")

#RQ3
if technique == "nmt":
    print("\nno rq3 processing for nmt, exiting...")
else:    
    strLocationBasedOverallDifferencePickleName = "locationbasedoveralldifference.pkl"
    dirLocationBasedOverallDifferencePickle = dirSimilarity + "/" + strLocationBasedOverallDifferencePickleName
    fileLocationBasedOverallDifferencePickle = Path(dirLocationBasedOverallDifferencePickle)

    df_Locations_Returned = get_locations(dirMain, technique, df)

    if not fileLocationBasedOverallDifferencePickle.is_file():
        print("\nfile not found ", dirLocationBasedOverallDifferencePickle)
        df_passed = df
        df_LocationBasedDifference = get_location_based_difference_df(df_passed, df_Locations_Returned)
        df_LocationBasedDifference.to_pickle(dirLocationBasedOverallDifferencePickle)
    else:
        print("\nreading from ", dirLocationBasedOverallDifferencePickle)
        df_LocationBasedDifference = pd.read_pickle(dirLocationBasedOverallDifferencePickle)

    plot_this(df_LocationBasedDifference, "RQ3_Random_Lines___Box_plot")

    strPatchLocationBasedOverallDifferencePickleName = "patchlocationbasedoveralldifference.pkl"
    dirPatchLocationBasedOverallDifferencePickle = dirSimilarity + "/" + strPatchLocationBasedOverallDifferencePickleName
    filePatchLocationBasedOverallDifferencePickle = Path(dirPatchLocationBasedOverallDifferencePickle)

    if not filePatchLocationBasedOverallDifferencePickle.is_file():
        print("\nfile not found ", dirPatchLocationBasedOverallDifferencePickle)
        df_passed = df_patch
        df_PatchLocationBasedDifference = get_location_based_difference_df(df_passed, df_Locations_Returned)
        df_PatchLocationBasedDifference.to_pickle(dirPatchLocationBasedOverallDifferencePickle)
    else:
        print("\nreading from ", dirPatchLocationBasedOverallDifferencePickle)
        df_PatchLocationBasedDifference = pd.read_pickle(dirPatchLocationBasedOverallDifferencePickle)

    plot_this(df_PatchLocationBasedDifference, "RQ3_Changed_Lines___Box_plot")

#RQ4

strSimilarityFolderName = "similarityfixes" + "-" + technique
dirSimilarity = dirMain + "/" + strSimilarityFolderName
strOverallSimilarityFileName = "overallsimilarity.txt"
dirSimilarityFile  = dirSimilarity + "/" + strOverallSimilarityFileName

strOverallSimilarityPickleName = "overallsimilarity.pkl"
dirOverallSimilarityPickle = dirSimilarity + "/" + strOverallSimilarityPickleName
fileOverallSimilarityPickle = Path(dirOverallSimilarityPickle)

df = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])

if not fileOverallSimilarityPickle.is_file():
    print("\nfile not found ", dirOverallSimilarityPickle)
    lstOverallSimilarity = []
    fileSimilarity = open(dirSimilarityFile,"r")
    lstOverallSimilarity = fileSimilarity.readlines()
    
    for str in lstOverallSimilarity:
        if "OCHIAI: -1" in str:
            continue
        arrStr = str.split(" | ")
        new_row = {'BUG':arrStr[0], 'MUTANT':arrStr[1], 'OCHIAI':float(arrStr[2].replace("OCHIAI: ", "")), 'BLEU':(1.0 - float(arrStr[3].replace("BLEU: ", ""))), 'JACCARD':(1.0 - float(arrStr[4].replace("JACCARD: ", ""))), 'COSINE':(1.0 - float(arrStr[5].replace("COSINE: ", "")))}
        df = df.append(new_row, ignore_index=True)
        print("added ", new_row)

    df = df.loc[df['OCHIAI'] > 0]
    
    df.to_pickle(dirOverallSimilarityPickle)
else:
    print("\nreading from ", dirOverallSimilarityPickle)
    df = pd.read_pickle(dirOverallSimilarityPickle)

plot_this(df, "RQ4_all___Box_plot")

#RQ2Quartiles

strSimilarityFolderName = "similarity" + "-" + technique
dirSimilarity = dirMain + "/" + strSimilarityFolderName
strOverallSimilarityPickleName = "overallsimilarity.pkl"
dirOverallSimilarityPickle = dirSimilarity + "/" + strOverallSimilarityPickleName
df = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
print("\nreading from ", dirOverallSimilarityPickle)
df = pd.read_pickle(dirOverallSimilarityPickle)

strOverallSimilarityQ1PickleName = "overallsimilarityq1.pkl"
dirOverallSimilarityQ1Pickle = dirSimilarity + "/" + strOverallSimilarityQ1PickleName
fileOverallSimilarityQ1Pickle = Path(dirOverallSimilarityQ1Pickle)

strOverallSimilarityQ2PickleName = "overallsimilarityq2.pkl"
dirOverallSimilarityQ2Pickle = dirSimilarity + "/" + strOverallSimilarityQ2PickleName
fileOverallSimilarityQ2Pickle = Path(dirOverallSimilarityQ2Pickle)

strOverallSimilarityQ3PickleName = "overallsimilarityq3.pkl"
dirOverallSimilarityQ3Pickle = dirSimilarity + "/" + strOverallSimilarityQ3PickleName
fileOverallSimilarityQ3Pickle = Path(dirOverallSimilarityQ3Pickle)

strOverallSimilarityQ4PickleName = "overallsimilarityq4.pkl"
dirOverallSimilarityQ4Pickle = dirSimilarity + "/" + strOverallSimilarityQ4PickleName
fileOverallSimilarityQ4Pickle = Path(dirOverallSimilarityQ4Pickle)

if not (fileOverallSimilarityQ1Pickle.is_file() and fileOverallSimilarityQ2Pickle.is_file()
        and fileOverallSimilarityQ3Pickle.is_file() and fileOverallSimilarityQ4Pickle.is_file()
       ):
    df1 = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
    df2 = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
    df3 = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
    df4 = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
    lstUniqueBugs = df["BUG"].unique()

    for strBug in lstUniqueBugs:
        print("processing for ", strBug)
        df_bug = df[df["BUG"] == strBug]
        df_bugSorted = df_bug.sort_values(by="BLEU", ignore_index=True)
        df1_bug, df2_bug, df3_bug, df4_bug = np.split(df_bugSorted, [int(.25 * len(df_bugSorted))
                                                                     , int(.5 * len(df_bugSorted))
                                                                     , int(.75 * len(df_bugSorted))])

        for index, row in df1_bug.iterrows():
            df1 = df1.append(row, ignore_index=True)

        for index, row in df2_bug.iterrows():
            df2 = df2.append(row, ignore_index=True)

        for index, row in df3_bug.iterrows():
            df3 = df3.append(row, ignore_index=True)

        for index, row in df4_bug.iterrows():
            df4 = df4.append(row, ignore_index=True)
    
    df1.to_pickle(dirOverallSimilarityQ1Pickle)
    df2.to_pickle(dirOverallSimilarityQ2Pickle)
    df3.to_pickle(dirOverallSimilarityQ3Pickle)
    df4.to_pickle(dirOverallSimilarityQ4Pickle)
else:
    print("\nreading from ", dirOverallSimilarityPickle)
    df = pd.read_pickle(dirOverallSimilarityPickle)
    print("\nreading from ", dirOverallSimilarityQ1Pickle)
    df1 = pd.read_pickle(dirOverallSimilarityQ1Pickle)
    print("\nreading from ", dirOverallSimilarityQ2Pickle)
    df2 = pd.read_pickle(dirOverallSimilarityQ2Pickle)
    print("\nreading from ", dirOverallSimilarityQ3Pickle)
    df3 = pd.read_pickle(dirOverallSimilarityQ3Pickle)
    print("\nreading from ", dirOverallSimilarityQ4Pickle)
    df4 = pd.read_pickle(dirOverallSimilarityQ4Pickle)
        
#data = [df["OCHIAI"] , df1["OCHIAI"], df2["OCHIAI"], df3["OCHIAI"], df4["OCHIAI"], df5["OCHIAI"]]
data = [df[df["OCHIAI"] > 0]["OCHIAI"] , 
        df1[df1["OCHIAI"] > 0]["OCHIAI"], 
        df2[df2["OCHIAI"] > 0]["OCHIAI"], 
        df3[df3["OCHIAI"] > 0]["OCHIAI"], 
        df4[df4["OCHIAI"] > 0]["OCHIAI"]]
headers = ["Overall", "Q1", "Q2", "Q3", "Q4"]
df_dummy = pd.concat(data, axis=1, keys=headers)

plt.figure()
boxplot = df_dummy.boxplot(column=['Overall', "Q1", "Q2", "Q3", "Q4"])
boxplot.set_ylabel('OCHIAI [> 0.0]')
filename = "Scatter-Plot-" + technique + "_bleu_ochiai_RQ2_Quartiles___Box_plot"
plt.savefig(dirSimilarity + "/" + filename + '.pdf')
plt.savefig(dirSimilarity + "/" + filename + '.png')

#RQ3Quartiles
strOverallInfo = ""
strQ1Info = ""
strQ2Info = ""
strQ3Info = ""
strQ4Info = ""
df_OchiaiAll = pd.DataFrame(columns=['QUARTILE', 'BUG', 'PERCENTAGEOFOCHIAIGT1'])
# len(lstUniqueBugs), df, df1, df2, df3, df4

dirMutantsOchiaiCsv = dirSimilarity + "/df_MutantsOchiai.csv"
fileMutantsOchiaiCsv = Path(dirMutantsOchiaiCsv)
df_MutantsOchiaiCsv = pd.DataFrame()

lstUniqueBugs = df["BUG"].unique()
quartileCount = 1
for dataset in [df, df1, df2, df3, df4]:
    quartile = None
    totalMutantsForInfo = 0
    mutantsWithOchiai1ForInfo = 0
    lstPercentageOfOchiaiGt1 = []
    for strBug in lstUniqueBugs:
        totalmutants = 0
        mutantsWithOchiai1 = 0
        df_bug = dataset[dataset["BUG"] == strBug]
        if len(df_bug) == 0:
            continue
        for index, row in df_bug.iterrows():
            totalmutants = totalmutants + 1
            if row["OCHIAI"] >= 1:
                mutantsWithOchiai1 = mutantsWithOchiai1 + 1

        percentageOfOchiaiGt1 = (mutantsWithOchiai1 * 100) / totalmutants
        lstPercentageOfOchiaiGt1.append(percentageOfOchiaiGt1)

        if quartileCount == 1:
            quartile = "Overall"
        elif quartileCount == 2:
            quartile = "Q1"
        elif quartileCount == 3:
            quartile = "Q2"
        elif quartileCount == 4:
            quartile = "Q3"
        elif quartileCount == 5:
            quartile = "Q4"

        new_row = {'QUARTILE': quartile, 'BUG': strBug
                   , 'PERCENTAGEOFOCHIAIGT1': percentageOfOchiaiGt1}
        df_OchiaiAll = df_OchiaiAll.append(new_row, ignore_index=True)
        
        totalMutantsForInfo = totalMutantsForInfo + totalmutants
        mutantsWithOchiai1ForInfo = mutantsWithOchiai1ForInfo + mutantsWithOchiai1
    
    strInfo = "\"" + str(mutantsWithOchiai1ForInfo) + ":" + str(totalMutantsForInfo) + "\""
    if quartileCount == 1:
        strOverallInfo = strInfo
    elif quartileCount == 2:
        strQ1Info = strInfo
    elif quartileCount == 3:
        strQ2Info = strInfo
    elif quartileCount == 4:
        strQ3Info = strInfo
    elif quartileCount == 5:
        strQ4Info = strInfo
    
    print(lstPercentageOfOchiaiGt1)
    new_row = {'QUARTILE': quartile, 'MEANPERCENTAGEOFMUTANTSWITHOCHIAIGT1': statistics.mean(lstPercentageOfOchiaiGt1)
                  , 'MEDIANPERCENTAGEOFMUTANTSWITHOCHIAIGT1': statistics.median(lstPercentageOfOchiaiGt1)
              }
    print(new_row)
    df_MutantsOchiaiCsv = df_MutantsOchiaiCsv.append(new_row, ignore_index=True)
    quartileCount = quartileCount + 1
df_MutantsOchiaiCsv.to_csv(dirMutantsOchiaiCsv, index = False)

#dont do it
#df_OchiaiAll = df_OchiaiAll[df_OchiaiAll["PERCENTAGEOFOCHIAIGT1"] > 0]

data = [df_OchiaiAll[df_OchiaiAll["QUARTILE"] == "Overall"]["PERCENTAGEOFOCHIAIGT1"] , 
        df_OchiaiAll[df_OchiaiAll["QUARTILE"] == "Q1"]["PERCENTAGEOFOCHIAIGT1"], 
        df_OchiaiAll[df_OchiaiAll["QUARTILE"] == "Q2"]["PERCENTAGEOFOCHIAIGT1"], 
        df_OchiaiAll[df_OchiaiAll["QUARTILE"] == "Q3"]["PERCENTAGEOFOCHIAIGT1"], 
        df_OchiaiAll[df_OchiaiAll["QUARTILE"] == "Q4"]["PERCENTAGEOFOCHIAIGT1"]]
headers = ["Overall", "Q1", "Q2", "Q3", "Q4"]
df_dummy = pd.concat(data, axis=1, keys=headers)

plt.figure()
boxplot = df_dummy.boxplot(column=['Overall', "Q1", "Q2", "Q3", "Q4"])
boxplot.set_ylabel('Semantically Same Mutants (%)')
filename = "Scatter-Plot-" + technique + "_bleu_ochiai_RQ3_Quartiles___Box_plot"
plt.savefig(dirSimilarity + "/" + filename + '.pdf')
plt.savefig(dirSimilarity + "/" + filename + '.png')

#RQ3QuartilesPart2
dirBugOchiaiCsv = dirSimilarity + "/df_BugsOchiai.csv"
fileBugOchiaiCsv = Path(dirBugOchiaiCsv)

if not (fileBugOchiaiCsv.is_file()):
    lstUniqueBugs = df["BUG"].unique()
    df_BugsOchiai = pd.DataFrame(columns=['QUARTILE', 'PERCENTAGEOFBUGSWITHOCHIAIGT1'])
    quartileCount = 1
    for dataset in [df, df4, df1, df2, df3, df4]:
        quartile = None
        totalBugs = 0
        bugsWithOchiai1 = 0
        strbugsWithOchiai1 = ""
        for strBug in lstUniqueBugs:
            df_bug = dataset[dataset["BUG"] == strBug]
            if len(df_bug) == 0:
                continue
            totalBugs = totalBugs + 1
            for index, row in df_bug.iterrows():
                if quartileCount == 2:
                    if row["OCHIAI"] >= 1 and row["BLEU"] >= 1:
                        strSyntacticFolderName = "syntactic" + "-" + technique
                        dirSyntactic = dirMain + "/" + strSyntacticFolderName + "/" + row["BUG"]
                        dirFlatteningMap = dirSyntactic + "/flatteningmap.txt"
                        fileFlatteningMap = Path(dirFlatteningMap)
                        if not fileFlatteningMap.is_file():
                            print(dirFlatteningMap,  "not found")
                            continue
                        fileFlatteningMap = open(dirFlatteningMap,"r")
                        lstFlatteningMap = fileFlatteningMap.readlines()
                        i = -1
                        for i in range(len(lstFlatteningMap)):
                            strFlatteningMap = lstFlatteningMap[i]
                            if row["MUTANT"] in strFlatteningMap:
                                break
                            else:
                                i = -1
                        if i == -1:
                            print("Not matched, iterated over entire list")
                            continue

                        dirFlattenedBuggyFns = dirSyntactic + "/flattenedbuggyfns.txt"
                        fileFlattenedBuggyFns = Path(dirFlattenedBuggyFns)
                        if not fileFlattenedBuggyFns.is_file():
                            print(dirFlattenedBuggyFns,  "not found")
                            continue
                        fileFlattenedBuggyFns = open(dirFlattenedBuggyFns,"r")
                        lstFlattenedBuggyFns = fileFlattenedBuggyFns.readlines()
                        if len(lstFlattenedBuggyFns) <= i:
                            print(lstFlattenedBuggyFns,  "is shorter than the index")
                            continue
                        strflattenedBuggy = lstFlattenedBuggyFns[i]

                        dirFlattenedMutatedFns = dirSyntactic + "/flattenedmutatedfns.txt"
                        fileFlattenedMutatedFns = Path(dirFlattenedMutatedFns)
                        if not fileFlattenedMutatedFns.is_file():
                            print(dirFlattenedMutatedFns,  "not found")
                            continue
                        fileFlattenedMutatedFns = open(dirFlattenedMutatedFns,"r")
                        lstFlattenedMutatedFns = fileFlattenedMutatedFns.readlines()
                        if len(lstFlattenedMutatedFns) <= i:
                            print(lstFlattenedMutatedFns,  "is shorter than the index")
                            continue
                        strflattenedMutated = lstFlattenedMutatedFns[i]

                        if strflattenedBuggy == strflattenedMutated:
                            print(strBug, " has a mutant same as the bug!")
                            bugsWithOchiai1 = bugsWithOchiai1 + 1
                            strbugsWithOchiai1 = strbugsWithOchiai1 + strBug + ":" + row["MUTANT"] + ","
                            break
                else:
                    if row["OCHIAI"] >= 1:
                        bugsWithOchiai1 = bugsWithOchiai1 + 1
                        break

        percentageOfBugsWithOchiaiGt1 = (bugsWithOchiai1 * 100) / totalBugs

        if quartileCount == 1:
            quartile = "SemEquiv"
            strInformation = strOverallInfo
        if quartileCount == 2:
            quartile = "SynEquiv"
            strInformation = strbugsWithOchiai1
        elif quartileCount == 3:
            quartile = "Q1"
            strInformation = strQ1Info
        elif quartileCount == 4:
            quartile = "Q2"
            strInformation = strQ2Info
        elif quartileCount == 5:
            quartile = "Q3"
            strInformation = strQ3Info
        elif quartileCount == 6:
            quartile = "Q4"
            strInformation = strQ4Info

        new_row = {'QUARTILE': quartile, 'PERCENTAGEOFBUGSWITHOCHIAIGT1': percentageOfBugsWithOchiaiGt1
                  , 'NUMBUGSWITHOCHIAI1': bugsWithOchiai1, 'TOTALBUGS': totalBugs
                  , 'INFO': strInformation}
        df_BugsOchiai = df_BugsOchiai.append(new_row, ignore_index=True)
        quartileCount = quartileCount + 1

    df_BugsOchiai.to_csv(dirBugOchiaiCsv, index = False)