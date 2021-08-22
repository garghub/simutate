#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import os

dirMain = "/home/agarg/ag/mutation"
technique = "codebert"

def ReadFileToList(dirFile):
    lst = []
    with open(dirFile, 'r') as filehandle:
        for line in filehandle:
            currentPlace = line[:-1]
            lst.append(currentPlace)
    return lst


strSyntacticFolderName = "syntactic" + "-" + technique
strSemanticFolderName = "semantic" + "-" + technique
strSimilarityFolderName = "similarity" + "-" + technique

dirSyntactic = dirMain + "/" + strSyntacticFolderName
dirSemantic = dirMain + "/" + strSemanticFolderName
dirSimilarity = dirMain + "/" + strSimilarityFolderName

strOverallSyntacticSimilarityFileName = "overallsyntacticsimilarity.txt"
strOverallSemanticSimilarityFileName = "overallsemanticsimilarity.txt"
strOverallSimilarityFileName = "overallsimilarity.txt"

dirSyntacticSimilarityFile  = dirSyntactic + "/" + strOverallSyntacticSimilarityFileName
dirSemanticSimilarityFile  = dirSemantic + "/" + strOverallSemanticSimilarityFileName
dirSimilarityFile  = dirSimilarity + "/" + strOverallSimilarityFileName

lstOverallSyntacticSimilarity = ReadFileToList(dirSyntacticSimilarityFile)
lstOverallSemanticSimilarity = ReadFileToList(dirSemanticSimilarityFile)
lstOverallSimilarity = []

for strSyntacticSimilarity in lstOverallSyntacticSimilarity:
    arrSyntacticSimilarity = strSyntacticSimilarity.split(" | ")
    strToSearch = arrSyntacticSimilarity[0] + " | " + arrSyntacticSimilarity[1]
    for strSemanticSimilarity in lstOverallSemanticSimilarity:
        if strToSearch in strSemanticSimilarity:
            strToAdd = strSemanticSimilarity + " | " + arrSyntacticSimilarity[2] + " | " + arrSyntacticSimilarity[3] + " | " + arrSyntacticSimilarity[4]
            lstOverallSimilarity.append(strToAdd)
            break

os.makedirs(dirSimilarity)
with open(dirSimilarityFile, 'w') as filehandle:
    for listitem in lstOverallSimilarity:
        filehandle.write('%s\n' % listitem)


# In[ ]:




