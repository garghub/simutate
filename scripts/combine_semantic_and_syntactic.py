#!/usr/bin/env python
# coding: utf-8

import os
import sys

n = len(sys.argv)
if n < 2 :
    print("\nplease pass as argument - the name of the technique (e.g. nmt / codebert / ...)")
    exit()

dirMain = "/home/agarg/ag/mutation"
technique = sys.argv[1]

def ReadFileToList(dirFile):
    print("\nreading ", dirFile)
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

dictOverallSyntacticSimilarity = {}
print("\ncreating dictOverallSyntacticSimilarity")
for strSyntacticSimilarity in lstOverallSyntacticSimilarity:
    arrSyntacticSimilarity = strSyntacticSimilarity.split(" | ")
    strToSearch = arrSyntacticSimilarity[0] + " | " + arrSyntacticSimilarity[1]
    dictOverallSyntacticSimilarity[strToSearch] = strSyntacticSimilarity

dictOverallSemanticSimilarity = {}
print("\ncreating dictOverallSemanticSimilarity")
for strSemanticSimilarity in lstOverallSemanticSimilarity:
    arrSemanticSimilarity = strSemanticSimilarity.split(" | ")
    strToSearch = arrSemanticSimilarity[0] + " | " + arrSemanticSimilarity[1]
    dictOverallSemanticSimilarity[strToSearch] = strSemanticSimilarity

lstOverallSimilarity = []

for keySyntacticSimilarity in dictOverallSyntacticSimilarity:
    print("\nprocessing ", keySyntacticSimilarity)
    if keySyntacticSimilarity in dictOverallSemanticSimilarity:
        strSemanticSimilarity = dictOverallSemanticSimilarity.get(keySyntacticSimilarity)
        strSyntacticSimilarity = dictOverallSyntacticSimilarity.get(keySyntacticSimilarity)
        arrSyntacticSimilarity = strSyntacticSimilarity.split(" | ")
        strToAdd = strSemanticSimilarity + " | " + arrSyntacticSimilarity[2] + " | " + arrSyntacticSimilarity[3] + " | " + arrSyntacticSimilarity[4]
        lstOverallSimilarity.append(strToAdd)
        print("\nadded ", strToAdd)
	
#for strSyntacticSimilarity in lstOverallSyntacticSimilarity:
#    print("\nprocessing ", strSyntacticSimilarity)
#    arrSyntacticSimilarity = strSyntacticSimilarity.split(" | ")
#    strToSearch = arrSyntacticSimilarity[0] + " | " + arrSyntacticSimilarity[1]
#    for strSemanticSimilarity in lstOverallSemanticSimilarity:
#        if strToSearch in strSemanticSimilarity:
#            strToAdd = strSemanticSimilarity + " | " + arrSyntacticSimilarity[2] + " | " + arrSyntacticSimilarity[3] + " | " + arrSyntacticSimilarity[4]
#            lstOverallSimilarity.append(strToAdd)
#            print("\nadded ", strToAdd)
#            break

print("\ncreating ", dirSimilarity)
os.makedirs(dirSimilarity)
print("\nwriting ", dirSimilarityFile)
with open(dirSimilarityFile, 'w') as filehandle:
    for listitem in lstOverallSimilarity:
        filehandle.write('%s\n' % listitem)
