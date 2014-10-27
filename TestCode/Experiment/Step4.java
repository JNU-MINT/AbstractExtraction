package Experiment;

import model.Document;
import model.Word;

/**
 * 算法步骤四
 * 特征值抽取
 * @author qiusd
 */
public class Step4 {
	
	public Document excuteStep4(Document doc)
			throws Exception {
		FeatureComputer featureComputer = new FeatureComputer();
		doc = featureComputer.compute(doc);
		return doc;
	}
	
	public static void main(String[] args) throws Exception {
		XMLDocHandler docHandler = new XMLDocHandler();
		// 记得配置路径
		Document doc = docHandler.getDoc("C:\\Data\\AbstractExtraction\\experiment\\US-08341762-B2.xml");
		Step1Word step1 = new Step1Word();
		doc = step1.ExcuteStep1(doc);
		Step2 step2 = new Step2();
		doc = step2.excuteStep2(doc);
		Step3 step3 = new Step3();
		doc = step3.excuteStep3(doc);
		Step4 step4 = new Step4();
		doc = step4.excuteStep4(doc);
		for (Word word : doc.abstractWords) {
			System.out.println(word.text + word.linkFeature);
		}
		
	}

}
