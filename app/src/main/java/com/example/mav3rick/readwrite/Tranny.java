package com.example.mav3rick.readwrite;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils;
import weka.core.Instance;
import weka.filters.unsupervised.attribute.StringToWordVector;

import android.util.Log;

/**
 * Created by Sohail on 1/10/2015.
 */
public class Tranny {

    

    Instances instances;
    //FilteredClassifier classifier = new FilteredClassifier();
    NaiveBayes classifier = new NaiveBayes();
    
    public Tranny() {

    }

    public int build(String fname) {
        int flag = 0;

      
    
        Instances traindata = null;
        //StringToWordVector filter;

        Instances structure;
        ArffLoader loader = new ArffLoader();
        try {
            loader.setFile(new File("/sdcard/" + fname + ".arff"));
            traindata = loader.getDataSet();
           
            traindata.setClassIndex(traindata.numAttributes() - 1);
        } catch (IOException e) {
            flag = 1;
            e.printStackTrace();
        }
        //filter = new StringToWordVector();
        //filter.setAttributeIndices("last");

        //classifier.setFilter(filter);
        //classifier.setClassifier(new NaiveBayes());

        try {
            classifier.buildClassifier(traindata);
        } catch (Exception e) {

            flag = 2;
            e.printStackTrace();
        }

        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream("/sdcard/model.txt"));
            out.writeObject(classifier);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public String classify(String fname) {
      

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream("/sdcard/model.txt"));
            try {
                Object tmp = in.readObject();
                classifier = (NaiveBayes) tmp;
                in.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       
        String text = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/sdcard/"+fname+".arff"));
            String line;

           
            try {
                while((line = reader.readLine())!=null)
                    text = text + line;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String out;



        List attributelist = new ArrayList(5);

        List values = new ArrayList(3);

        values.add("sunny");
        values.add("overcast");
        values.add("rainy");
        Attribute attribute1 = new Attribute("outlook", values);
        attributelist.add(attribute1);

        Attribute attribute2 = new Attribute("temperature");
        attributelist.add(attribute2);

        Attribute attribute3 = new Attribute("humidity");
        attributelist.add(attribute3);


        values = new ArrayList(2);
        values.add("TRUE");
        values.add("FALSE");
        Attribute attribute4 = new Attribute("windy", values);
        attributelist.add(attribute4);


        values = new ArrayList(2);
        values.add("yes");
        values.add("no");
        Attribute attribute5 = new Attribute("play", values);
        attributelist.add(attribute5);


        instances = new Instances("Test Relation", (java.util.ArrayList<Attribute>) attributelist, 1);
        instances.setClassIndex(instances.numAttributes() - 1);



        DenseInstance instance = new DenseInstance(5);
        instance.setDataset(instances);
        //text = "sunny,20,20,FALSE,?";

        String [] stringValues = text.split(",");

        instance.setValue(attribute1, stringValues[0]);
        instance.setValue(attribute2, Integer.parseInt(stringValues[1]));
        instance.setValue(attribute3, Integer.parseInt(stringValues[2]));
        instance.setValue(attribute4, stringValues[3]);
        instances.add(instance);

      
        double pred = 0;
        try {
            pred = classifier.classifyInstance(instances.instance(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
        out = instances.classAttribute().value((int) pred);

        return out;
    }

}
