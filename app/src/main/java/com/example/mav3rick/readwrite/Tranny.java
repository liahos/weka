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

    //J48 nb = new J48();

    Instances instances;
    //FilteredClassifier classifier = new FilteredClassifier();
    NaiveBayes classifier = new NaiveBayes();
    public Tranny() {

    }

    public int build(String fname) {
        int flag = 0;

        /*ArffLoader loader = new ArffLoader();
        Instances structure = null;

        try {
            loader.setFile(new File("/sdcard/" + fname + ".arff"));
            structure = loader.getStructure();
            structure.setClassIndex(structure.numAttributes() - 1);
        } catch (IOException e) {
            flag = 1;
            e.printStackTrace();
        }
        Instance current;
        try {
            nb.buildClassifier(structure);

           // while ((current = loader.getNextInstance(structure)) != null)
                //nb.updateClassifier(current);

        } catch (Exception e) {
            flag = 2;
            e.printStackTrace();
        }

       /* String[] options = new String[1];
        options[0] = "-U";
        try {
            tree.setOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tree.buildClassifier(structure);
        } catch (Exception e) {
            flag = 2;
            e.printStackTrace();
        }*/


        Instances traindata = null;
        //StringToWordVector filter;

        Instances structure;
        ArffLoader loader = new ArffLoader();
        try {
            loader.setFile(new File("/sdcard/" + fname + ".arff"));
            traindata = loader.getDataSet();
            //structure = loader.getStructure();
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
        /*int flag = 0;
        String [] options = new String[2];
        options[0] = "-t";
        options[1] = "/sdcard/"+fname+".arff";
        String out = null;
        try {
            out  = Evaluation.evaluateModel(classifier, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Instances unlabeled = null;
        try {
            unlabeled = new Instances(new BufferedReader(new FileReader("/sdcard/" + fname + ".arff")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
        Instances labeled = new Instances(unlabeled);

        for (int i = 0; i < unlabeled.numInstances() - 1; i++) {
            try {
                double clslabel = nb.classifyInstance(unlabeled.instance(i));
                labeled.instance(i).setClassValue(clslabel);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/sdcard" + "classified.arff"));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*ArffLoader loader= new ArffLoader();
        Instances structure = null;

        try {
            loader.setFile(new File("/sdcard/"+fname+".arff"));
            structure = loader.getStructure();
            structure.setClassIndex(structure.numAttributes() - 1);
        } catch (IOException e) {
            flag = -1;
            e.printStackTrace();
        }
        //Instances labeled = new Instances(structure);

        /*Instance current;
        Instances labeled = new Instances(structure);
        int i = 0;
        try {
            while ((current=loader.getNextInstance(structure))!= null)
            {
                try {
                    i++;
                    double clslabel = nb.classifyInstance(current);
                    current.setClassValue(clslabel);
                    labeled.set(i, current);
                } catch (Exception e) {
                    flag = 1;
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/sdcard" + "classified.arff"));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            flag = 2;
            e.printStackTrace();
        }*/

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

        /*
        String text = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/sdcard/"+fname+".arff"));
            String line;

            text = "";

            try {
                while((line = reader.readLine())!=null)
                    text = text + " " + line;
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

        Instances instances;

        FastVector fvnominal = new FastVector();

        fvnominal.addElement("spam");
        fvnominal.addElement("ham");
        Attribute attribute1 = new Attribute("class", fvnominal);

        Attribute attribute2 = new Attribute("text", (FastVector) null);

        FastVector fwekattributes = new FastVector();
        fwekattributes.addElement(attribute1);
        fwekattributes.addElement(attribute2);

        instances = new Instances("Test relation", fwekattributes, 1);
        instances.setClassIndex(0);

        DenseInstance instance = new DenseInstance(2);
        instance.setValue(attribute2, text);

        instances.add(instance);








        double pred = 0;
        try {
            pred = classifier.classifyInstance(instances.instance(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
        out = instances.classAttribute().value((int) pred);

      /*ArffLoader loader= new ArffLoader();
        Instances structure = null;

        try {
            loader.setFile(new File("/sdcard/"+fname+".arff"));
            structure = loader.getStructure();
            structure.setClassIndex(structure.numAttributes() - 1);
        } catch (IOException e) {
            flag = -1;
            e.printStackTrace();
        }
        Instance current;
        Instances labeled = new Instances(structure);
        int i = 0;
        try {
            while ((current=loader.getNextInstance(structure))!= null)
            {
                try {
                    i++;
                    double clslabel = classifier.classifyInstance(current);
                    current.setClassValue(clslabel);
                    labeled.set(i, current);
                } catch (Exception e) {
                    flag = 1;
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/sdcard" + "classified.arff"));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            flag = 2;
            e.printStackTrace();
        }*/
        String text = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/sdcard/"+fname+".arff"));
            String line;

            //text = "";

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

        List values = new ArrayList(5);

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

        //instance.setValue(attribute2, text);
        //instances.add(instance);
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