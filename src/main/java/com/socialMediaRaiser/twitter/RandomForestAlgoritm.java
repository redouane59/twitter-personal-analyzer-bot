package com.socialMediaRaiser.twitter;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RandomForestAlgoritm {

    public static RandomForest forest;

    public RandomForestAlgoritm(){
        try {
            process();
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    public static void process() throws Exception {
        int classIndex = 8;

        File file = new File("C:/Users/Perso/Downloads/training.csv");
        File file2 = new File("C:/Users/Perso/Downloads/test.csv");

        CSVLoader loader = new CSVLoader();
        loader.setSource(file);
        Instances trainingDataSet = loader.getDataSet();
        trainingDataSet.setClassIndex(classIndex);

        loader = new CSVLoader();
        loader.setSource(file2);
        Instances testingDataSet = loader.getDataSet();
        testingDataSet.setClassIndex(classIndex);

        forest = new RandomForest();
        forest.setNumIterations(100);

        forest.buildClassifier(trainingDataSet);

        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(forest, testingDataSet);

        /** Print the algorithm summary */
     /*   System.out.println("false negative : " + eval.numFalseNegatives(0));
        System.out.println("false positives : " + eval.numFalsePositives(0));
        System.out.println("true negative : " + eval.numTrueNegatives(1));
        System.out.println("true positives : " + eval.numTruePositives(1));
        System.out.println("** Decision Tress Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.print(" the expression for the input data as per alogorithm is ");
        System.out.println(forest);
        System.out.println(eval.toMatrixString());
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.confusionMatrix()); */
    }

    public static boolean getPrediction(User user) {
        Attribute followers = new Attribute("Followers");
        Attribute followings = new Attribute("Followings");
        Attribute nbDaySinceLastTweet = new Attribute("NbDaySinceLastTweet");
        Attribute commonFollowers = new Attribute("CommonFollowers");
        Attribute dateOfFollow = new Attribute("DateOfFollow");
        Attribute tweets = new Attribute("Tweets");
        Attribute fav = new Attribute("Fav");
        Attribute yearsSinceCreation = new Attribute("YearsSinceCreation");

        List<String> fvClassVal = new ArrayList<>(2);
        fvClassVal.add("true");
        fvClassVal.add("false");
        Attribute Class = new Attribute("FollowBack", fvClassVal);

        ArrayList<Attribute> fvWekaAttributes = new ArrayList(7);
        fvWekaAttributes.add(followers);
        fvWekaAttributes.add(followings);
        fvWekaAttributes.add(nbDaySinceLastTweet);
        fvWekaAttributes.add(commonFollowers);
        fvWekaAttributes.add(dateOfFollow);
        fvWekaAttributes.add(tweets);
        fvWekaAttributes.add(fav);
        fvWekaAttributes.add(yearsSinceCreation);
        fvWekaAttributes.add(Class);
        // Declare Instances which is required since I want to use classification/Prediction
        Instances dataset = new Instances("whatever", fvWekaAttributes, 0);
        //Creating a double array and defining values
        //Followers	Followings	NbDaySinceLastTweet	CommonFollowers	DateOfFollow	Tweets	Fav	YearsSinceCreation	FollowBack
        double[] attValues = {user.getFollowersCount(), user.getFollowingsCount(), user.getDaysBetweenFollowAndLastUpdate()
                , user.getCommonFollowers(), 0.0, user.getStatusesCount(), user.getFavouritesCount(),
                user.getYearsBetweenFollowAndCreation()};

        //Create the new instance i1
        Instance i1 = new DenseInstance(1.0, attValues);
        //Add the instance to the dataset (Instances) (first element 0)
        dataset.add(i1);
        //Define class attribute position
        dataset.setClassIndex(dataset.numAttributes()-1);

        //Will print 0 if it's a "yes", and 1 if it's a "no"
        double result = 0;
        try {
            result = forest.classifyInstance(dataset.instance(0));
            return result == 0.0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
