package svm;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;


public class svmTest {
	public static void main(String[] args) {
		svm_node pa0 = new svm_node();
		pa0.index = 0;
		pa0.value = 0.0;
		svm_node pa1 = new svm_node();
		pa1.index = 1;
		pa1.value = 0.0;
		
		svm_node pb0 = new svm_node();
		pb0.index = 0;
		pb0.value = 1.0;
		svm_node pb1 = new svm_node();
		pb1.index = 1;
		pb1.value = 1.0;
		
		svm_node pc0 = new svm_node();
		pc0.index = 0;
		pc0.value = -10.0;
		svm_node pc1 = new svm_node();
		pc1.index = 1;
		pc1.value = -10.0;
		
		svm_node[] pa = { pa0, pa1 }; // 点a
		svm_node[] pb = { pb0, pb1 }; // 点b
		svm_node[] pc = { pc0, pc1 }; // 点b
		svm_node[][] datas = { pa, pb, pc }; // 训练集的向量表
		double[] lables = { 1, 1, 0 }; // a,b 对应的lable

		// 定义svm_problem对象
		svm_problem problem = new svm_problem();
		problem.l = 3; // 向量个数,训练集的个数
		problem.x = datas; // 训练集向量表
		problem.y = lables; // 对应的lable数组

		// 定义svm_parameter对象
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.LINEAR;
		//单位为M
		param.cache_size = 512;
		param.eps = 0.00001;
		param.C = 1;

		// 训练SVM分类模型
		System.out.println(svm.svm_check_parameter(problem, param)); // 如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
		svm_model model = svm.svm_train(problem, param); // svm.svm_train()训练出SVM分类模型

		// 定义测试数据点c
		svm_node pd0 = new svm_node();
		pd0.index = 0;
		pd0.value = 100;
		svm_node pd1 = new svm_node();
		pd1.index = 0;
		pd1.value = 0.0;
		svm_node[] pd = { pd0, pd1 };

		// 预测测试数据的lable
		System.out.println(svm.svm_predict(model, pd));
	}
}
