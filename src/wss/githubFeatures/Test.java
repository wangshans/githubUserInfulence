package wss.githubFeatures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public void classifier() throws Exception{
		Classifier c = null;
        c = new NaiveBayes();
        //
        //data\\UserFeaturesN100.arff
		String trainFile = "F:\\weka\\Weka-3-6\\data\\weather.nominal.arff";
		ArffLoader atf = new ArffLoader();
        atf.setFile(new File(trainFile));
        Instances instancesTrain = atf.getDataSet(); 
        instancesTrain.setClassIndex(0);
        c.buildClassifier(instancesTrain);
        
        Evaluation eval = new Evaluation(instancesTrain);
        eval.crossValidateModel(c, instancesTrain, 10, new Random(1));
        int sum = instancesTrain.numInstances();
        //输出每个实例的分类结果
        System.out.println("i, actual, predicted");
        //Data\\result\\predicted.csv
        BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\predicted.csv",true));
        out.write("i, actual, predicted\n");
        for(int i=0;i<sum;i++){
        	double predicted = c.classifyInstance(instancesTrain.instance(i));
        	System.out.println(c.distributionForInstance(instancesTrain.instance(i))+"");
			String predicte = instancesTrain.classAttribute().value((int) predicted);
			double actual =  instancesTrain.instance(i).classValue();
			String actuall = instancesTrain.classAttribute().value((int) actual);
			if(predicted == 0){
				out.write(i+","+actuall+","+predicte+"\n");
				System.out.println(i+","+actuall+","+predicte);
			}
        }
        System.out.println(eval.weightedFMeasure());
        System.out.println(eval.toMatrixString());
	}
	public static void ceshi() throws Exception {
		Classifier m_classifier = new J48();
		// 训练语料文件，官方自带的 demo 里有
		File inputFile = new File("F:\\weka\\Weka-3-6\\data\\weather.nominal.arff");
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances instancesTrain = atf.getDataSet(); // 读入训练文件
		// 测试语料文件：随便 copy 一段训练文件出来，做分类的预测准确性校验
		inputFile = new File("F:\\weka\\Weka-3-6\\data\\weather.nominal.arff");
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

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Test t = new Test();
	//	t.ceshi();
		t.classifier();
	}

}
