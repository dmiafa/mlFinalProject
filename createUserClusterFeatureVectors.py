import scipy.sparse as sparse
import scipy.io as sio
import numpy as np

#spectral, KMeans or gmm
TYPEOFCLUSTERING = "spectral"

LIMIT = 100
songPosition = {}
songMap = {}

f= sio.loadmat('/home/dmitriy/workspace/MLFinalProject/MatlabFiles/finalSongIDs.mat')
f1= f['finalSongIDs']
for songID in range(0,f1.shape[0]):
	songPosition[int(songID)] = f1[songID][0][0]

sMap = open('songIndexMap.txt','r')
try:
	for line in sMap:
		splitMap = line.split(',')
		songMap[splitMap[0]] = int(splitMap[1])
finally:
	sMap.close()

for clusters in range(24,50):
	clusterDictionary = {}
	clusterLimit = [0]*clusters
	clustering = open('james_songIDandCluster/'+TYPEOFCLUSTERING+str(LIMIT)+"/"+str(clusters)+'clusters.csv', 'r')
	try:
		for line in clustering:
			splitIDandCluster = line.split(',')
			songStringID = songPosition[int(splitIDandCluster[0])]
			songMappedID = songMap[songStringID]
			clusterDictionary[int(songMappedID)] = int(splitIDandCluster[1])
			#FOR PRINTING CLUSTERS
			# if clusterLimit[int(splitIDandCluster[1])] < 5:
			# 	print songStringID, splitIDandCluster[1]
			# 	clusterLimit[int(splitIDandCluster[1])] +=1
	finally:
		clustering.close()

	user = open('userFeatureVectors.txt', 'r')
	output = open('userClusterFeatureVectors/'+TYPEOFCLUSTERING+str(LIMIT)+"/"+str(clusters)+'clusters.csv', 'w')
	try:
		counter = 0
		for line in user:
			if counter>LIMIT:
				break
			clusterCount = [0]*clusters
			splitLine = line.split(',(')
			for song in range(1,len(splitLine)):
				splitSong = splitLine[song][:-1].split(',')
				songCluster = clusterDictionary[int(splitSong[0])]
				if splitSong[1].isdigit():
					clusterCount[songCluster]+=int(splitSong[1])
				else:
					strippedPlayCount = splitSong[1][:-1]
					clusterCount[songCluster]+=int(strippedPlayCount)
			for i in range(0,len(clusterCount)):
				output.write(str(counter)+","+str(i)+','+str(clusterCount[i])+"\n")
			counter +=1 
	finally:
		user.close()
		output.close()
