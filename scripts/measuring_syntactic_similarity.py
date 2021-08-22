#!/usr/bin/env python
# coding: utf-8

# In[ ]:


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

dirMain = "/home/agarg/ag/mutation/syntactic-codebert"
lstOverallSyntacticSimilarity = []
for strProjectWithPatchId in os.listdir(dirMain):
    print("processing syntactic similarity for", strProjectWithPatchId)
    dirProjectWithPatchId = dirMain + "/" + strProjectWithPatchId
    
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

        bleuscore = sentence_bleu([tokennizedreference], tokennizedcandidate, weights=(1, 0, 0, 0))

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
    
    with open(dirProjectWithPatchId + "/syntacticsimilarity.txt", 'w') as filehandle:
        for listitem in lstSyntacticSimilarity:
            filehandle.write('%s\n' % listitem)
    
with open(dirMain + "/overallsyntacticsimilarity.txt", 'w') as filehandle:
    for listitem in lstOverallSyntacticSimilarity:
        filehandle.write('%s\n' % listitem)


# In[ ]:




