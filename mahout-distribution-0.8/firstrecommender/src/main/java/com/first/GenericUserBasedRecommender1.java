package com.first;

import java.io.*;
import java.util.*;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;
import org.apache.mahout.common.RandomUtils;

public class GenericUserBasedRecommender1 {
	
	  public static void main(String[] args) throws Exception {
		  RandomUtils.useTestSeed();
	      // Create a data source from the CSV file
	      
		  String fileName;
		  double[] scores = new double[23];
	      for(int i = 2; i < 25; i++)
	      {
	    	  fileName = "data/" + i + "clusters.csv";
	    	  scores[i-2] = evaluateFile(fileName);
	      }
	      
		  System.out.println(evaluateFile("data/2clusters.csv"));
		  
	      for(int j = 0; j < scores.length; j++)
	      {
	    	  System.out.println("Error for " + (j+2) + " Clusters : " + scores[j]);
	      }
	      
	      /*
	      UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
	      UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, userSimilarity, dataModel);
	 
	      // Create a generic user based recommender with the dataModel, the userNeighborhood and the userSimilarity
	      Recommender genericRecommender =  new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
	 
	      // Generate a list of 3 recommended items for user 1001
	      List<RecommendedItem> itemRecommendations = genericRecommender.recommend(1001, 3);
	 
	      // Display the item recommendations generated by the recommendation engine
	      for (RecommendedItem recommendedItem : itemRecommendations) {
	          System.out.println(recommendedItem);
	      }
	      */
	  }
	  
	  public static double evaluateFile(String f) throws TasteException, IOException
	  {
		  File userPreferencesFile = new File(f);
		  System.out.println(f);
		  DataModel dataModel = new FileDataModel(userPreferencesFile);
	      
	      RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
	      
	      RecommenderBuilder recommenderBuilder = new RecommenderBuilder()
	      {
	    	  public Recommender buildRecommender(DataModel model) throws TasteException
	    	  {
	    		  UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
	    		  UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
	    		  return new GenericUserBasedRecommender(model, neighborhood, similarity);
	    	  }
	      };
	      
	      double score = evaluator.evaluate(recommenderBuilder, null, dataModel, 0.7, 0.1);
	      return score;
	  }
}
