#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import pandas as pd
import os
import sys
import seaborn as sns
import scipy.stats as stats

from matplotlib import pyplot as plt
from matplotlib.pyplot import figure

n = len(sys.argv)
if n < 2 :
    print("\nplease pass as argument - the name of the technique (e.g. nmt / codebert / ...)")
    exit()

dirMain = "/home/agarg/ag/mutation"
technique = sys.argv[1]

def ReadFileToList(dirFile):
    lst = []
    with open(dirFile, 'r') as filehandle:
        for line in filehandle:
            # remove linebreak which is the last character of the string
            currentPlace = line[:-1]
            # add item to the list
            lst.append(currentPlace)
    return lst

def hist_dist(df, filename, filenamesuffix):
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
    plt.show()

def scatter_plot(data, parax, paray, filename, filenamesuffix):
    
    axeLeft = sns.jointplot(data=data, x=parax, y=paray, kind="reg")
    axeLeft.set_axis_labels(xlabel=parax, ylabel=paray, fontsize=12)
    pr_axeLeft, pp_axeLeft = stats.pearsonr(data[parax], data[paray])
    kr_axeLeft, kp_axeLeft = stats.kendalltau(data[parax], data[paray])
    # # if you choose to write your own legend, then you should adjust the properties then
    phantom_axeLeft, = axeLeft.ax_joint.plot([], [], linestyle="", alpha=0)
    # # here graph is not a ax but a joint grid, so we access the axis through ax_joint method
    label_axeLeft = 'pearson: r={:f}, p={:f}\nkendall: r={:f}, p={:f}'.format(pr_axeLeft, pp_axeLeft, kr_axeLeft, kp_axeLeft)
    # # label_pearson = 'r={:f}, p={:f}'.format(pr, pp)
    axeLeft.ax_joint.legend([phantom_axeLeft], [label_axeLeft])

    #plt.tight_layout()
    if filenamesuffix == "all":
        stitle = "All"
    elif filenamesuffix == "sem-gt80p":
        stitle = "Semantic similarity >= 0.8"
    elif filenamesuffix == "syn-gt80p":
        stitle = "Syntactic similarity >= 0.8"
    elif filenamesuffix == "patch-based":
        stitle = "Mutants on patch changed fns"
    plt.suptitle('Semantic vs Syntactic - ' + stitle)
    filename = filename + "-" + filenamesuffix
    plt.savefig(dirSimilarity + "/" + filename + ".pdf", format='pdf', dpi=1500)
    plt.show()
    print()

def plot_this(df, filenamesuffix):
    #df = df.sort_values(['OCHIAI', 'BLEU'], ascending=True)
    #df = df.reset_index(drop=True)
    #hist_dist(df, "plot-" + technique, filenamesuffix)
    scatter_plot(df, "BLEU", "OCHIAI", "scatter_plot_bleu_ochiai-" + technique, filenamesuffix)
    scatter_plot(df, "JACCARD", "OCHIAI", "scatter_plot_jaccard_ochiai-" + technique, filenamesuffix)
    scatter_plot(df, "COSINE", "OCHIAI", "scatter_plot_cosine_ochiai-" + technique, filenamesuffix)


strSimilarityFolderName = "similarity" + "-" + technique
dirSimilarity = dirMain + "/" + strSimilarityFolderName
strOverallSimilarityFileName = "overallsimilarity.txt"
dirSimilarityFile  = dirSimilarity + "/" + strOverallSimilarityFileName

lstOverallSimilarity = []
fileSimilarity = open(dirSimilarityFile,"r")
lstOverallSimilarity = fileSimilarity.readlines()

df = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])
for str in lstOverallSimilarity:
    arrStr = str.split(" | ")
    new_row = {'BUG':arrStr[0]
               , 'MUTANT':arrStr[1]
               , 'OCHIAI':float(arrStr[2].replace("OCHIAI: ", ""))
               , 'BLEU':float(arrStr[3].replace("BLEU: ", ""))
               , 'JACCARD':float(arrStr[4].replace("JACCARD: ", ""))
               , 'COSINE':float(arrStr[5].replace("COSINE: ", ""))
              }
    df = df.append(new_row, ignore_index=True)
df = df.loc[df['OCHIAI'] >= 0]

plot_this(df, "all")

df_sem_gt80p = df.loc[df['OCHIAI'] >= 0.8]
plot_this(df_sem_gt80p, "sem-gt80p")

df_syn_gt80p = df.loc[(df['BLEU'] >= 0.8) & (df['JACCARD'] >= 0.8) & (df['COSINE'] >= 0.8)]
plot_this(df_syn_gt80p, "syn-gt80p")

dirMutants = dirMain + "/" "experiment_mutants-" + technique
strPatchFnMapFileName = "patchfnmap.txt"
dirPatchFnMap = dirMutants + "/" + strPatchFnMapFileName
filePatchFnMap = open(dirPatchFnMap,"r")
lstPatchChangedMutants = filePatchFnMap.readlines()
df_patch = pd.DataFrame(columns=['BUG','MUTANT','OCHIAI','BLEU','JACCARD','COSINE'])

for str in lstPatchChangedMutants:
    arrStr = str.split(" | ")
    strToSearch = arrStr[0] + " | " + arrStr[1]
    for index, row in df.iterrows():
        if (row['BUG'] + " | " + row['MUTANT']) == strToSearch:
            df_patch = df_patch.append(row, ignore_index=True)
            break

plot_this(df_patch, "patch-based")

