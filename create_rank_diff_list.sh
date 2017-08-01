#!/bin/bash
#dist=/program/TweetEmbeding/SMERP_doc_embed_query_doc.dist
#dist=/program/TweetEmbeding/SMERP_doc_embed_query_doc_tweet2011.dist
dist=/program/TweetEmbeding/SMERP_plus_tweet2011_doc_embed_query_doc.dist

qrel=/program/corpus/SMERP2017-dataset/SMERP_retrieval.qrel

#######################################################
rank_diff(){
    file=$1
    qrel=$2
    
    awk '{print $1"*"$3" "$4;}' $qrel | sort > /tmp/t
    
for f in {1..4};
do
    awk -v n=$f '{gsub("SMERP-T","",$1);if($1==n)printf("%s %s %f %d %f\n",$1,$2,$3,$4,$5)}' $file | \
	sort -k 3,3nr | \
	awk 'BEGIN{c=0;}{d=c-$4; if(d<0)d=d*-1;printf("%s %s %d %f %d %f %d\n",$1,$2,c,$3,$4,$5,d);c++}' > /tmp/a
	awk '{if($3<100)print $1"*"$2" "$0;}' /tmp/a 
done | 	sort | join -1 1 -2 1 - /tmp/t |\
    sort -k 2,2n -k 8,8nr |\
    sed -e 's/^[1-4]\*[^ ]* //g' 

}

########################################################

create_re_file(){
    for f in {1..4};
    do
	awk -v n=$f -v s=$2 -v m=$3 '{gsub("SMERP-T","",$1);if(n==$1)printf("%s Q0 %s %f %s\n", $1,$2,$s,m);}' $1 |\
	sort -k 4,4nr |\
	awk 'BEGIN{c=0;}{print $1,$2,$3,c,$4,$5;c++}'   
    done
}

#########################################################

rank_diff $dist $qrel > rank_diff_DE_lmjm_v2.res

#create_re_file $dist 3 wv > wv_SMERP_plus_tweet2011.res
#create_re_file $dist 5 lmjm > lmjm_SMERP_tweet2011.res

