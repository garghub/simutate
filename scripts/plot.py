#!/usr/bin/env python
# coding: utf-8

import pandas as pd
import os
import sys
import seaborn as sns
import scipy.stats as stats
import pickle
from pathlib import Path

from matplotlib import pyplot as plt
from matplotlib.pyplot import figure

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
    plt.show()

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
    axeLeft = sns.jointplot(data=data, x=parax, y=paray, kind="reg")
    axeLeft.set_axis_labels(xlabel=label_parax, ylabel=label_paray, fontsize=12)
    pr_axeLeft, pp_axeLeft = stats.pearsonr(data[parax], data[paray])
    kr_axeLeft, kp_axeLeft = stats.kendalltau(data[parax], data[paray])
    # # if you choose to write your own legend, then you should adjust the properties then
    phantom_axeLeft, = axeLeft.ax_joint.plot([], [], linestyle="", alpha=0)
    # # here graph is not a ax but a joint grid, so we access the axis through ax_joint method
    label_axeLeft = 'pearson: r={:f}, p={:f}\nkendall: r={:f}, p={:f}'.format(pr_axeLeft, pp_axeLeft, kr_axeLeft, kp_axeLeft)
    # # label_pearson = 'r={:f}, p={:f}'.format(pr, pp)
    axeLeft.ax_joint.legend([phantom_axeLeft], [label_axeLeft])

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
    plt.savefig(dirSimilarity + "/" + filename + ".pdf", format='pdf', dpi=1500)
    plt.savefig(dirSimilarity + "/" + filename + ".png", format='png', dpi=1500)
    plt.show()
    print()

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
# dirMain = "//atlas/users/aayush.garg/c/github/mutation"
# technique = "ibir"

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

    df = df.loc[df['OCHIAI'] >= 0]
    
    df.to_pickle(dirOverallSimilarityPickle)
else:
    print("\nreading from ", dirOverallSimilarityPickle)
    df = pd.read_pickle(dirOverallSimilarityPickle)

plot_this(df, "RQ4_all___Box_plot")
