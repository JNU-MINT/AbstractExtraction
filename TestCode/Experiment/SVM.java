package Experiment;

import java.util.ArrayList;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import model.Word;

public class SVM {

	private svm_problem problem;
	private svm_parameter param;
	private svm_model model;

	public SVM() {
		param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.cache_size = 512;
		param.eps = 0.00001;
		param.degree = 3;
		//可以对这两个参数进行调整
		param.gamma = 16;
		param.C = 256;
	}

	public void train(ArrayList<Word> trainWords) {
		problem = new svm_problem();
		// 向量个数,即训练集的个数
		problem.l = trainWords.size();
		System.out.println(trainWords.size() + "words");
		
		ArrayList<svm_node[]> dataList = new ArrayList<>();
		ArrayList<Double> lableList = new ArrayList<>();
		svm_node[][] dataArray;
		double[] lableArray;
		//data第一维
		for (Word word : trainWords) {
			ArrayList<svm_node> nodeList = new ArrayList<>();
			svm_node node = new svm_node();
			node.index = 0;
			node.value = word.linkFeature;
			nodeList.add(node);
			node = new svm_node();
			node.index = 1;
			node.value = word.tfidf;
			nodeList.add(node);
			node = new svm_node();
			node.index = 2;
			node.value = word.position;
			nodeList.add(node);
			node = new svm_node();
			node.index = 3;
			node.value = word.wordLength;
			nodeList.add(node);
			node = new svm_node();
			svm_node[] nodeArray = new svm_node[nodeList.size()];
			for (int i = 0; i < nodeArray.length; i++) {
				nodeArray[i] = nodeList.get(i);
			}
			dataList.add(nodeArray);
			lableList.add(word.lable);
		}
		
		//data第二维
		dataArray = new svm_node[trainWords.size()][dataList.get(0).length];
		for (int i = 0; i < dataArray.length; i++) {
			dataArray[i] = dataList.get(i);
		}
		
		//label
		lableArray = new double[lableList.size()];
		for (int i = 0; i < lableArray.length; i++) {
			lableArray[i] = lableList.get(i);
		}
		problem.x = dataArray;
		problem.y = lableArray;
		System.out.println(svm.svm_check_parameter(problem, param));
		
		double precision, recall, f1score;
		int a = 0, b = 0, c = 0, d = 0;
		for(int paraG = 1; paraG <= 6; paraG++) {
			for(int paraC = 6; paraC <= 11; paraC++) {
				if (paraG >= 3 && paraC >= 8) {
					continue;
				}
				param.gamma = Math.pow(2.0, paraG);
				param.C = Math.pow(2.0, paraC);
				double[] target = new double[problem.l];
				svm.svm_cross_validation(problem, param, 10, target);
				a = 0;
				b = 0;
				c = 0;
				d = 0;
				for (int i = 0; i < target.length; i++) {
					if (target[i] == 1 && lableArray[i] == 1) {
						a++;
					}
					if(target[i] == 1 && lableArray[i] == 0) {
						b++;
					}
					if(target[i] == 0 && lableArray[i] == 1) {
						c++;
					}
					if(target[i] == 0 && lableArray[i] == 0) {
						d++;
					}
				}
				System.out.print("a == " + a + " b == " + b + " c == " + c + " d == " + d);
				System.out.println("  gamma == " + param.gamma + "  C ==" + param.C);
				precision = (double) a/(a+b);
				recall = (double) a/(a+c);
				f1score = 2.0 * precision * recall / (precision + recall);
				System.out.println("precision = " + precision + " recall = " + recall + " f1score = " + f1score );
			}
		}
		
		//textrank
		a = 0;
		b = 0;
		c = 0;
		d = 0;
		for (Word word : trainWords) {
			if (word.textRank <= 3.0 && word.textRank >= 1.0 && word.lable == 1) {
				a ++;
			}
			else if (word.textRank <= 3.0 && word.textRank >= 1.0 && word.lable == 0) {
				b ++;
			}
			else if (word.lable == 1) {
				c ++;
			}
			else if (word.lable == 0) {
				d ++;
			}
		}
		System.out.print("a == " + a + " b == " + b + " c == " + c + " d == " + d);
		precision = (double) a/(a+b);
		recall = (double) a/(a+c);
		f1score = 2.0 * precision * recall / (precision + recall);
		System.out.println("precision = " + precision + " recall = " + recall + " f1score = " + f1score );
		
		//tfidf
		a = 0;
		b = 0;
		c = 0;
		d = 0;
		for (Word word : trainWords) {
			if (word.tfidf > 0.2 && word.lable == 1) {
				a ++;
			}
			else if (word.tfidf > 0.2 && word.lable == 0) {
				b ++;
			}
			else if (word.lable == 1) {
				c ++;
			}
			else if (word.lable == 0) {
				d ++;
			}
		}
		System.out.print("a == " + a + " b == " + b + " c == " + c + " d == " + d);
		precision = (double) a/(a+b);
		recall = (double) a/(a+c);
		f1score = 2.0 * precision * recall / (precision + recall);
		System.out.println("precision = " + precision + " recall = " + recall + " f1score = " + f1score );
		
	}

	public ArrayList<Word> getTestResult(ArrayList<Word> wordList) {

		for (Word word : wordList) {
			ArrayList<svm_node> nodeList = new ArrayList<>();
			svm_node node = new svm_node();
			node.index = 0;
			node.value = word.linkFeature;
			nodeList.add(node);
			node = new svm_node();
			node.index = 1;
			node.value = word.tfidf;
			nodeList.add(node);
			node = new svm_node();
			node.index = 2;
			node.value = word.position;
			nodeList.add(node);
			node = new svm_node();
			node.index = 3;
			node.value = word.wordLength;
			nodeList.add(node);
			svm_node[] nodeArray = new svm_node[nodeList.size()];
			for (int i = 0; i < nodeArray.length; i++) {
				nodeArray[i] = nodeList.get(i);
			}
			double result = svm.svm_predict(model, nodeArray);
			word.prediction = result;
			if (word.lable == result && result == 1) {
				System.out.println("_____" + word.text);
			}
			if (word.textRank == result && result == 1) {
				System.out.println(word.text);
			}
		}
		return wordList;
	}

}
