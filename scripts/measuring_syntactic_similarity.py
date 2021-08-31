#!/usr/bin/env python
# coding: utf-8

import sys
import nltk
from nltk.tokenize import word_tokenize
from nltk.translate.bleu_score import sentence_bleu
from sklearn.feature_extraction.text import CountVectorizer
import pandas as pd
from scipy.spatial.distance import cosine
import os
from pathlib import Path

nltk.download('punkt')

def get_jaccard_sim(str1, str2): 
    a = set(str1.split()) 
    b = set(str2.split())
    c = a.intersection(b)
    return float(len(c)) / (len(a) + len(b) - len(c))

def get_cosine_sim(reference, candidate):
    documents = [reference, candidate]
    count_vectorizer = CountVectorizer()
    sparse_matrix = count_vectorizer.fit_transform(documents)
    doc_term_matrix = sparse_matrix.todense()
    df = pd.DataFrame(doc_term_matrix, 
                      columns=count_vectorizer.get_feature_names(), 
                      index=['reference', 'candidate'])
    return (1 - cosine(df[:1], df[1:2]))

#sys.argv = "/home/agarg/ag/simutate/scripts/measuring_syntactic_similarity.py fixes nmt Cli Cli_1".split()

n = len(sys.argv)
if n < 4 :
    print("\nplease pass as arguments - 1) processing is done for bugs/fixes (e.g. bugs / fixes); 2) the name of the technique (e.g. nmt / codebert / ...); and 3) project name (e.g Cli / Jsoup / ...)")
    print("\noptionally you can pass as argument - 4) bug id (e.g Cli_1 / Jsoup_3 / ...)")
    exit()

strSyntacticFolderName = "syntactic"
if sys.argv[1] != "bugs":
    strSyntacticFolderName = strSyntacticFolderName + sys.argv[1]

dirMain = "/home/agarg/ag/mutation/" + strSyntacticFolderName + "-" + sys.argv[2]
# dirMain = "D:/ag/github/mutants_sensitivity/" + strSyntacticFolderName + "-" + sys.argv[2]

project = sys.argv[3]
bugid = ""
if n >= 5:
    bugid = sys.argv[4]

lstOverallSyntacticSimilarity = []
for strProjectWithPatchId in os.listdir(dirMain):
    arrStr = strProjectWithPatchId.split("_")
    fetchedProject = arrStr[0]
    if fetchedProject != project:
        continue
    if bugid != "" and strProjectWithPatchId != bugid:
        continue
    dirProjectWithPatchId = dirMain + "/" + strProjectWithPatchId
    print("processing syntactic similarity for", dirProjectWithPatchId)
    dirSyntacticSimilarity = dirProjectWithPatchId + "/syntacticsimilarity.txt"
    fileSyntacticSimilarity = Path(dirSyntacticSimilarity)
    if fileSyntacticSimilarity.is_file():
        print(dirSyntacticSimilarity, "already exists.")
        continue
    
    if sys.argv[1] != "bugs":
        dirflattenedbuggyfns = dirProjectWithPatchId + "/flattenedfixedfns.txt"
    else:
        dirflattenedbuggyfns = dirProjectWithPatchId + "/flattenedbuggyfns.txt"
    
    fileflattenedbuggyfns = Path(dirflattenedbuggyfns)
    if not fileflattenedbuggyfns.is_file():
        continue
    
    flattenedbuggyfnsfile = open(dirflattenedbuggyfns,"r")
    flattenedbuggyfns = flattenedbuggyfnsfile.readlines()
    
    dirflattenedmutatedfns = dirProjectWithPatchId + "/flattenedmutatedfns.txt"
    fileflattenedmutatedfns = Path(dirflattenedmutatedfns)
    if not fileflattenedmutatedfns.is_file():
        continue
    
    flattenedmutatedfnsfile = open(dirflattenedmutatedfns,"r")
    flattenedmutatedfns = flattenedmutatedfnsfile.readlines()
    
    dirflatteningmapfile = dirProjectWithPatchId + "/flatteningmap.txt"
    fileflatteningmapfile = Path(dirflatteningmapfile)
    if not fileflatteningmapfile.is_file():
        continue
    
    flatteningmapfile = open(dirflatteningmapfile,"r")
    flatteningmaps = flatteningmapfile.readlines()
    
    lstSyntacticSimilarity = []
    for i in range(len(flattenedbuggyfns)):
        reference = flattenedbuggyfns[i]
        candidate = flattenedmutatedfns[i]
        flatteningmap = flatteningmaps[i]
        
        tokennizedreference = word_tokenize(reference)
        tokennizedcandidate = word_tokenize(candidate)

        bleuscore = sentence_bleu([tokennizedreference], tokennizedcandidate)

        jaccardsimilarity = get_jaccard_sim(reference, candidate)

        cosinesimilarity = get_cosine_sim(reference, candidate)
        
        arrflatteningmap = flatteningmap.split(" | ")
        mutantFileName = arrflatteningmap[0]
        lstSyntacticSimilarity.append(strProjectWithPatchId + " | " + mutantFileName + " | " + "BLEU: " + "{:.2f}".format(bleuscore) + " | " 
                                      + "JACCARD: " + "{:.2f}".format(jaccardsimilarity) + " | " 
                                      + "COSINE: " + "{:.2f}".format(cosinesimilarity))
        lstOverallSyntacticSimilarity.append(strProjectWithPatchId + " | " + mutantFileName + " | " + "BLEU: " + "{:.2f}".format(bleuscore) + " | " 
                                      + "JACCARD: " + "{:.2f}".format(jaccardsimilarity) + " | " 
                                      + "COSINE: " + "{:.2f}".format(cosinesimilarity))
    
    with open(dirSyntacticSimilarity, 'w') as filehandle:
        for listitem in lstSyntacticSimilarity:
            filehandle.write('%s\n' % listitem)
