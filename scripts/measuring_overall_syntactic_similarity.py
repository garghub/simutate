#!/usr/bin/env python
# coding: utf-8

import sys
import os
from pathlib import Path

def ReadFileToList(dirFile):
    print("\nreading ", dirFile)
    lst = []
    with open(dirFile, 'r') as filehandle:
        for line in filehandle:
            currentPlace = line[:-1]
            lst.append(currentPlace)
    return lst

n = len(sys.argv)
if n < 2 :
    print("\nplease pass as argument - the name of the technique (e.g. nmt / codebert / ...)")
    exit()

dirMain = "/home/agarg/ag/mutation/syntactic-" + sys.argv[1]

lstOverallSyntacticSimilarity = []
for strProjectWithPatchId in os.listdir(dirMain):
    print("processing syntactic similarity for", strProjectWithPatchId)
    dirProjectWithPatchId = dirMain + "/" + strProjectWithPatchId
    dirSyntacticSimilarity = dirProjectWithPatchId + "/syntacticsimilarity.txt"
    fileSyntacticSimilarity = Path(dirSyntacticSimilarity)
    if not fileSyntacticSimilarity.is_file():
        continue
    lstSyntacticSimilarity = ReadFileToList(dirSyntacticSimilarity)
    
    for strSyntacticSimilarity in lstSyntacticSimilarity:
        lstOverallSyntacticSimilarity.append(strSyntacticSimilarity)

with open(dirMain + "/overallsyntacticsimilarity.txt", 'w') as filehandle:
    for listitem in lstOverallSyntacticSimilarity:
        filehandle.write('%s\n' % listitem)
