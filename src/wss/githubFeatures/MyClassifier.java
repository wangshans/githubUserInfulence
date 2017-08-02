package wss.githubFeatures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IB1;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class MyClassifier {

	/**
	 * @param args
	 */
	public Evaluation classifier(String classifierForName,File inputFile) throws Exception{
		//Classifier c  = new NaiveBayesMultinomial();
		 try{
			 Classifier classifier;
			 ArffLoader atf = new ArffLoader();
	         atf.setFile(inputFile);
	         Instances instancesTrain = atf.getDataSet(); 
	         instancesTrain.setClassIndex(0);
	         
	         classifier = (Classifier) Class.forName(classifierForName).newInstance();
	        
//	         BufferedWriter out= new BufferedWriter(new FileWriter("Data\\result.txt",true));
//	         out.write("分类算法"+classifierForName+"\n");
//	         out.close();
	        
	         classifier.buildClassifier(instancesTrain);
	        
	         Evaluation eval = new Evaluation(instancesTrain);
	         eval.crossValidateModel(classifier, instancesTrain, 10, new Random(1));
	         return eval;  
		 }catch(Exception e) {
	         e.printStackTrace();
	     }
		 return null;
         
	}
	public void run(){
		String nb = "weka.classifiers.bayes.NaiveBayes";	// 朴素贝叶斯算法
		String mnb = "weka.classifiers.bayes.NaiveBayesMultinomial"; // 多项式贝叶斯算法
		String j48 = "weka.classifiers.trees.J48";// 决策树
		String ZeroR = "weka.classifiers.rules.ZeroR"; // Zero
		String svm = "weka.classifiers.functions.LibSVM"; // LibSVM
		String knn = "weka.classifiers.lazy.IB1";//KNN
		
		int percent = 100;
		String c = svm;
	
		File inputFile = new File("Data\\UserFeaturesN"+percent+".arff");
	//	Evaluation eval = m.MNB(inputFile);
		System.out.println("start....");
		try{
			long start = System.currentTimeMillis();
			Evaluation eval = classifier(c,inputFile);
			long end = System.currentTimeMillis();
			
			BufferedWriter out= new BufferedWriter(
	                    new FileWriter("Data\\result.csv",true));
            
        //  out.write("percent,classifier,P(0),R(0),fM(0),P(1),R(1),fM(1),WP,WR,WfM,WAUROC,Time\n");
            
            out.write(percent+","+c+",");
            out.write(eval.precision(0)+","+eval.recall(0)+","+eval.fMeasure(0)+",");
             
            out.write(eval.precision(1)+","+eval.recall(1)+","+eval.fMeasure(1)+",");
             
            out.write(eval.weightedPrecision()+","+eval.weightedRecall()+","+eval.weightedFMeasure()+","
            		+eval.weightedAreaUnderROC()+","+(end-start)/1000+"s\n");
            
            out.close();
            BufferedWriter out2= new BufferedWriter(
                    new FileWriter("Data\\NumResult.csv",true));
            out2.write("percent,classifier," +
            		"TP,FN,FP,TN,ActualPositive,ActualNegative,PredictedPositive,PredictedNegative,Total\n");
            
            out2.write(percent+","+c+",");
            
            double tp = eval.numTruePositives(0);
            double fn = eval.numFalseNegatives(0);
            double fp = eval.numFalsePositives(0);
            double tn = eval.numTrueNegatives(0);
            
            out2.write(tp+","+fn+","+fp+","+tn+",");
            out2.write((tp+fn)+","+(fp+tn)+","+(tp+fp)+","+(fn+tn)+","+(tp+fn+fp+tn)+"\n");
            out2.close();

            System.out.println(percent+" "+c+"\n");
            System.out.println("P(0),R(0),fM(0),P(1),R(1),fM(1),WP,WR,WfM,WAUROC\n");
            System.out.println(eval.precision(0)+","+eval.recall(0)+","+eval.fMeasure(0)+",");
             
            System.out.println(eval.precision(1)+","+eval.recall(1)+","+eval.fMeasure(1)+",");
             
            System.out.println(eval.weightedPrecision()+","+eval.weightedRecall()+","+eval.weightedFMeasure()+","
            		+eval.weightedAreaUnderROC()+"\n");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void test() throws Exception{
		Classifier m_classifier = new J48();
		// 训练语料文件，官方自带的 demo 里有
		File inputFile = new File("D:\\c_install_program\\Weka-3-6\\data\\cpu.with.vendor.arff");
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances instancesTrain = atf.getDataSet(); // 读入训练文件
		// 测试语料文件：随便 copy 一段训练文件出来，做分类的预测准确性校验
		inputFile = new File("D:\\c_install_program\\Weka-3-6\\data\\cpu.with.vendor_test.arff");
		atf.setFile(inputFile);
		Instances instancesTest = atf.getDataSet(); // 读入测试文件
		instancesTest.setClassIndex(0); // 设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数
		double sum = instancesTest.numInstances(), // 测试语料实例数
		right = 0.0f;
		instancesTrain.setClassIndex(0);// 分类属性：第一个字段
		m_classifier.buildClassifier(instancesTrain); // 训练
		for (int i = 0; i < sum; i++)// 测试分类结果
		{
			double predicted = m_classifier.classifyInstance(instancesTest.instance(i));
			System.out.println("预测某条记录的分类id：" + predicted + ", 分类值："
					+ instancesTest.classAttribute().value((int) predicted));
			System.out.println("测试文件的分类值： " + instancesTest.instance(i).classValue() + ", 记录："
					+ instancesTest.instance(i));
			System.out.println("--------------------------------------------------------------");

			// 如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
			if (m_classifier.classifyInstance(instancesTest.instance(i)) == instancesTest.instance(i)
					.classValue()) {
				right++;// 正确值加1
			}
		}
		// 请将文件内容的第一列 ? 换成正确答案，才能评判分类预测的结果，本例中只是单纯的预测，下面的输出没有意义。
		System.out.println("J48 classification precision:" + (right / sum));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyClassifier m = new MyClassifier();
	
		
	}

}
