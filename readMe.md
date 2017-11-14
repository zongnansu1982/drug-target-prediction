# Deep Mining Heterogeneous Networks to Predict Novel Drug-Target Associations
# Objective:   
We propose a similarity-based drug-target prediction method that enhances existing association discovery methods by using a topology-based similarity measure. 

# Methods:
1. NetWork: Linked Tripartite Network (LTN)
2. Similarity measure: DeepWalk
3. Inference method: DBSI, TBSI

# Usage   
1. Generate deepwalk index with DeepWalkMethod.java
2. Predict with Prediction.java
3. For nodes that are not listed in the network (new drugs or targets), use the similarity measures in chemicstrc and genomicsqs

# Contact
For help or questions of using the application, please contact nazong@ucsd.edu

# Citation
The authors appreciate to cite our published work,

Zong, N., Kim, H., Ngo, V. and Harismendy, O., 2017. Deep mining heterogeneous networks of biomedical linked data to predict novel drug–target associations. Bioinformatics, p.btx160.
