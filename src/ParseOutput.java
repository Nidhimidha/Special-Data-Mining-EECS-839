import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseOutput {
	public static void main(String[] args) throws Exception {
		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		int noOfColumns;
		int noOfRows;
		int noOfAttributes;
		ArrayList<ArrayList<String>> dataTable = new ArrayList<ArrayList<String>>();
		ArrayList<Boolean> checkNumeric = new ArrayList<Boolean>();
		ArrayList<Boolean> checkSymbols = new ArrayList<Boolean>();
		ArrayList<ArrayList<Set<Integer>>> conceptValueSets = new ArrayList<ArrayList<Set<Integer>>>();
		ArrayList<ArrayList<String>> conceptStringlist = new ArrayList<ArrayList<String>>();
		Boolean runAgain = true;
		Scanner scan_string = new Scanner(System.in);
		while (runAgain) {

			String dataFileName = null;
			String ruleFileName = null;
			String matchingFactorFlag = "";
			String strengthFactorFlag = "";
			String specificityFactorFlag = "";
			String supportFactorFlag = "";
			String conceptStatsFlag = "";
			String conceptClassifiedFlag = "";
			boolean quesflag = true;
			int totalIncorrectCases=0;
			boolean t = true;
			if (t) {

				boolean input_file_exists = false;
				while (input_file_exists != true) {
					System.out.println("Please enter the name of input data file: ");
					dataFileName = scan_string.next();
					File f = new File(dataFileName);
					input_file_exists = f.exists();
					if (!input_file_exists)

						System.out.println("NO such file exists.Please recheck and enter the file name: \n ");
				}

				boolean output_file_exists = false;
				while (output_file_exists != true) {
					System.out.println("Please enter the name of rule file: ");

					ruleFileName = scan_string.next();
					File f = new File(ruleFileName);
					output_file_exists = f.exists();
					if (!output_file_exists)

						System.out.println("NO such file exists.Please recheck and enter the file name: \n ");
				}

				while (quesflag) {
					System.out.println("Do you wish to use the matching_factor? Please enter Y/N");
					matchingFactorFlag = scan_string.next();
					if (matchingFactorFlag.equalsIgnoreCase("Y") || matchingFactorFlag.equalsIgnoreCase("N")) {
						quesflag = false;
					}
				}

				quesflag = true;
				while (quesflag) {
					System.out.println("Do you wish to use strength or conditional\n"
							+ "probability as the strength_factor? Please enter s/p");
					strengthFactorFlag = scan_string.next();
					if (strengthFactorFlag.equalsIgnoreCase("s") || strengthFactorFlag.equalsIgnoreCase("p")) {
						quesflag = false;
					}

				}

				quesflag = true;
				while (quesflag) {
					System.out.println(
							"Do you wish to use the factor associated with\n" + "specificity or not? Please enter Y/N");
					specificityFactorFlag = scan_string.next();
					if (specificityFactorFlag.equalsIgnoreCase("Y") || specificityFactorFlag.equalsIgnoreCase("N")) {
						quesflag = false;
					}
				}

				quesflag = true;
				while (quesflag) {
					System.out.println("Do you wish to use the support of other rules or not? Please enter Y/N");
					supportFactorFlag = scan_string.next();
					if (supportFactorFlag.equalsIgnoreCase("Y") || supportFactorFlag.equalsIgnoreCase("N")) {
						quesflag = false;
					}
				}

				quesflag = true;
				while (quesflag) {
					System.out.println("Do you wish to know concept statistics? Please enter Y/N");
					conceptStatsFlag = scan_string.next();
					if (conceptStatsFlag.equalsIgnoreCase("Y") || conceptStatsFlag.equalsIgnoreCase("N")) {
						quesflag = false;
					}
				}

				quesflag = true;
				while (quesflag) {
					System.out.println(
							"Do you wish to know how cases associated with concepts were classified? Please enter Y/N");
					conceptClassifiedFlag = scan_string.next();
					if (conceptClassifiedFlag.equalsIgnoreCase("Y") || conceptClassifiedFlag.equalsIgnoreCase("N")) {
						quesflag = false;
					}
				}

			}

			System.out.println("*********************************************************************");
			System.out.println("GENERAL STATISTICS");
			System.out.println("*********************************************************************");
			InputData f = new InputData();
			array = f.readdata(dataFileName);
			noOfColumns = array.get(0).size();
			for (int i = 1; i < array.size(); i++) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int j = 0; j < noOfColumns; j++) {
					temp.add(j, array.get(i).get(j).toString());
				}
				dataTable.add(i - 1, temp);
			}
			noOfRows = dataTable.size() - 1;
			noOfAttributes = noOfColumns - 1;

			System.out.println("The total number of cases : " + noOfRows);
			System.out.println("The total number of attributes : " + noOfAttributes);

			InputData g = new InputData();
			ArrayList<ArrayList> rulesList = g.readrules(ruleFileName);
			ArrayList<String> parameters = rulesList.get(0);
			ArrayList<Map<String, String>> rule = rulesList.get(1);
			System.out.println("The total number of rules : " + parameters.size());
			ArrayList<Map<String, String>> ruleConditionsList = new ArrayList<Map<String, String>>();
			ArrayList<Map<String, String>> ruleDecisionList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < rule.size(); i++) {
				if (i == 0 || i % 2 == 0) {
					ruleConditionsList.add(rule.get(i));
				} else {
					ruleDecisionList.add(rule.get(i));
				}
			}

			HashMap<Integer, String> map = new HashMap<Integer, String>();
			for (int i = 0; i < ruleDecisionList.size(); i++) {
				String value = ruleDecisionList.get(i).entrySet().iterator().next().getValue();
				map.put(i, value);
			}
			Collection<String> list = map.values();
			for (Iterator<String> itr = list.iterator(); itr.hasNext();) {
				if (Collections.frequency(list, itr.next()) > 1) {
					itr.remove();
				}
			}
			ArrayList<ArrayList<Set<Integer>>> conceptRuleSets = new ArrayList<ArrayList<Set<Integer>>>();
			ArrayList<Set<Integer>> conceptColumn1 = new ArrayList<Set<Integer>>();
			Set<String> temporary_list1 = new LinkedHashSet<String>();
			for (Entry<Integer, String> e : map.entrySet()) {
				temporary_list1.add(e.getValue());
			}

			for (String w : temporary_list1) {
				Set<Integer> concept_set = new LinkedHashSet<Integer>();
				for (int k = 0; k < parameters.size(); k++)
					if (w.equalsIgnoreCase(ruleDecisionList.get(k).entrySet().iterator().next().getValue()))
						concept_set.add(k);
				conceptColumn1.add(concept_set);
			}
			conceptRuleSets.add(0, conceptColumn1);

			int n = 1;
			Map<String, String> conditionMap = new HashMap<String, String>();
			for (Map<String, String> a : ruleConditionsList) {
				for (Entry<String, String> entry : a.entrySet()) {
					String value = conditionMap.get(entry.getKey());
					if (value == null) {
						conditionMap.put(entry.getKey(), entry.getValue());
					} else {
						boolean flag = false;
						AA: for (String key : conditionMap.keySet()) {
							if (key.startsWith(entry.getKey())) {
								if (conditionMap.get(key).equalsIgnoreCase(entry.getValue())) {
									flag = true;
									break AA;
								}
							}
						}
						if (flag == false) {
							conditionMap.put(entry.getKey() + String.valueOf(n), entry.getValue());
							n++;
						}
					}
				}
			}
			System.out.println("The total number of conditions:  " + conditionMap.size());

			// numericAndSymbols
			Pattern p = Pattern.compile("(^(\\d+)(((\\.)?(\\d+)){0,1}$))");
			boolean isSymbolic;
			boolean isNumeric;
			for (int j = 0; j < noOfAttributes; j++) {
				isSymbolic = false;
				isNumeric = false;
				for (int i = 1; i < noOfRows && !isSymbolic && !isNumeric; i++) {
					if ((dataTable.get(i).get(j).equalsIgnoreCase("?"))
							|| (dataTable.get(i).get(j).equalsIgnoreCase("*"))
							|| (dataTable.get(i).get(j).equalsIgnoreCase("-")))
						continue;
					Matcher match = p.matcher(dataTable.get(i).get(j));
					if (!match.find()) {

						isSymbolic = true;
					} else {
						isNumeric = true;
					}
				}
				if (isNumeric) {
					checkNumeric.add(j, true);
					checkSymbols.add(j, false);
				} else if (isSymbolic) {
					checkNumeric.add(j, false);
					checkSymbols.add(j, true);
				}

			}
			for (int j = noOfColumns - 1; j < noOfColumns; j++) {
				ArrayList<Set<Integer>> conceptColumn = new ArrayList<Set<Integer>>();
				Set<String> temporary_list = new LinkedHashSet<String>();
				for (int k = 1; k <= noOfRows; k++)
					temporary_list.add(dataTable.get(k).get(j).toString());
				ArrayList<String> temp = new ArrayList<String>();
				int count1 = 0;
				for (String w : temporary_list) {
					Set<Integer> concept_set = new LinkedHashSet<Integer>();
					for (int k = 1; k <= noOfRows; k++)
						if (w.equalsIgnoreCase(dataTable.get(k).get(j)))
							concept_set.add(k);
					conceptColumn.add(concept_set);
					temp.add(count1++, w);
				}
				conceptValueSets.add(0, conceptColumn);
				conceptStringlist.add(0, temp);
			}
			String conceptName = dataTable.get(0).get(noOfAttributes);

			ArrayList<Map<String, String>> dataSetConcepts = new ArrayList<Map<String, String>>();
			for (int i = 1; i < dataTable.size(); i++) {
				Map<String, String> dataSetConceptsMap = new LinkedHashMap<String, String>();
				dataSetConceptsMap.put(dataTable.get(0).get(noOfAttributes), dataTable.get(i).get(noOfAttributes));
				dataSetConcepts.add(dataSetConceptsMap);
			}

			for (int i = 0; i < dataTable.size(); i++) {
				dataTable.get(i).remove(noOfAttributes);
			}

			ArrayList<Map<String, String>> dataSet = new ArrayList<Map<String, String>>();
			for (int i = 1; i < dataTable.size(); i++) {
				Map<String, String> dataTableMap = new LinkedHashMap<String, String>();
				for (int j = 0; j < dataTable.get(i).size(); j++) {
					dataTableMap.put(dataTable.get(0).get(j), dataTable.get(i).get(j));
				}
				dataSet.add(dataTableMap);
			}

			int correctCompleteCases = 0;
			int incorrectCompleteCases = 0;

			ArrayList<Integer> matchedCases = new ArrayList<Integer>();

			for (int j = 0; j < dataSet.size(); j++) {
				for (int i = 0; i < ruleConditionsList.size(); i++) {

					Map<String, String> conditions = ruleConditionsList.get(i);
					boolean correctCompleteCasesFlag = false;
					Set<String> keys = conditions.keySet();
					Iterator<String> iter1 = keys.iterator();
					A: while (iter1.hasNext()) {
						Set<String> key = dataSet.get(j).keySet();
						String s1 = iter1.next();
						Iterator<String> iter = key.iterator();
						int k = 0;
						while (iter.hasNext()) {
							String s = iter.next();
							if (s.contains(s1)) {
								String ruleVal = conditions.get(s1);
								String dataVal = dataSet.get(j).get(s);

								if (!dataVal.equals("?")) {
									if (checkNumeric.get(k)) {
										String[] ruleVals = ruleVal.split("\\.\\.");
										if (dataVal.equals("*") || dataVal.equals("-")) {
											dataVal = ruleVals[0];
										}
										if (Float.parseFloat(dataVal) >= Float.parseFloat(ruleVals[0])
												&& Float.parseFloat(dataVal) <= Float.parseFloat(ruleVals[1])) {
											correctCompleteCasesFlag = true;
											break;
										} else {
											correctCompleteCasesFlag = false;
											break A;
										}

									} else {
										if (dataVal.equals("*") || dataVal.equals("-")) {
											dataVal = ruleVal;
										}
										if (dataVal.equalsIgnoreCase(ruleVal)) {
											correctCompleteCasesFlag = true;
											break;
										} else {
											correctCompleteCasesFlag = false;
											break A;
										}
									}

								} else {
									correctCompleteCasesFlag = false;
									break A;
								}
							}
							k++;
						}
					}
					if (correctCompleteCasesFlag) {
						Map.Entry<String, String> entry = ruleDecisionList.get(i).entrySet().iterator().next();
						if (entry.getValue().equalsIgnoreCase(dataSetConcepts.get(j).get(entry.getKey()))) {
							correctCompleteCases++;
						} else {
							incorrectCompleteCases++;
						}
						matchedCases.add(j);
						break;
					}

				}
			}

			System.out.println("COMPLETE MATCHING:\n" + "The total number of cases that are incorrectly classified: "
					+ incorrectCompleteCases + "\n" + "The total number of cases that are correctly classified: "
					+ correctCompleteCases);

			
			totalIncorrectCases = totalIncorrectCases+incorrectCompleteCases;
			int correctPartialCases = 0;
			int incorrectPartialCases = 0;

			for (int j = 0; j < dataSet.size(); j++) {
				Map<Integer, Float> partiallyMatchedRules = new HashMap<Integer, Float>();
				if (!matchedCases.contains(j)) {
					for (int i = 0; i < ruleConditionsList.size(); i++) {
						Map<String, String> conditions = ruleConditionsList.get(i);
						int partiallyMatched = 0;
						Set<String> keys = conditions.keySet();
						Iterator<String> iter1 = keys.iterator();
						while (iter1.hasNext()) {
							Set<String> key = dataSet.get(j).keySet();
							String s1 = iter1.next();
							Iterator<String> iter = key.iterator();
							int k = 0;
							while (iter.hasNext()) {
								String s = iter.next();
								if (s.contains(s1)) {
									String ruleVal = conditions.get(s1);
									String dataVal = dataSet.get(j).get(s);

									if (!dataVal.equals("?")) {
										if (checkNumeric.get(k)) {
											String[] ruleVals = ruleVal.split("\\.\\.");
											if (dataVal.equals("*") || dataVal.equals("-")) {
												dataVal = ruleVals[0];
											}
											if (Float.parseFloat(dataVal) >= Float.parseFloat(ruleVals[0])
													&& Float.parseFloat(dataVal) <= Float.parseFloat(ruleVals[1])) {
												partiallyMatched++;
											}
										} else {
											if (dataVal.equals("*") || dataVal.equals("-")) {
												dataVal = ruleVal;
											}
											if (dataVal.equalsIgnoreCase(ruleVal)) {
												partiallyMatched++;
											}
										}
									}
									break;
								}
								k++;
							}
						}
						if (partiallyMatched > 0) {
							Float PMF = (float) partiallyMatched / (float) keys.size();
							partiallyMatchedRules.put(i, PMF);
						}
					}

					Map<Integer, Float> specificityMap = new HashMap<Integer, Float>();
					Map<Integer, Float> conditionalProbMap = new HashMap<Integer, Float>();
					Map<Integer, Float> strengthMap = new HashMap<Integer, Float>();
					for (int l = 0; l < parameters.size(); l++) {
						if (partiallyMatchedRules.containsKey(l)) {
							String param = parameters.get(l);
							String specificity = param.substring(0, param.indexOf(","));
							specificityMap.put(l, Float.parseFloat(specificity));
							param = param.substring(param.indexOf(" ") + 1);
							String strength = param.substring(0, param.indexOf(","));
							strengthMap.put(l, Float.parseFloat(strength));
							String conditional = param.substring(param.indexOf(",") + 1);
							Float conditionalProb = Float.parseFloat(strength) / Float.parseFloat(conditional);
							conditionalProbMap.put(l, conditionalProb);
						}
					}
					Map<Integer, Float> newSortedSpecificityMap = sortMap(specificityMap);
					Map<Integer, Float> newSortedStrengthMap = sortMap(strengthMap);
					Map<Integer, Float> newSortedConditionalMap = sortMap(conditionalProbMap);

					Map<String, Integer> strengthRuleMap = new HashMap<String, Integer>();
					Map<String, Integer> specificityRuleMap = new HashMap<String, Integer>();

					if (strengthFactorFlag.equals("s")) {
						strengthRuleMap = getStrength(ruleDecisionList, newSortedStrengthMap, strengthRuleMap);
					} else if (strengthFactorFlag.equals("p")) {
						strengthRuleMap = getStrength(ruleDecisionList, newSortedConditionalMap, strengthRuleMap);
					}
					specificityRuleMap = getStrength(ruleDecisionList, newSortedSpecificityMap, specificityRuleMap);
					Map.Entry<String, String> entry = dataSetConcepts.get(j).entrySet().iterator().next();
					if (strengthRuleMap.size() > 1) {

						if (specificityFactorFlag.equalsIgnoreCase("Y")) {
							if (specificityRuleMap.size() == 1) {
								if (specificityRuleMap.entrySet().iterator().next().getKey()
										.equalsIgnoreCase(entry.getValue())) {
									correctPartialCases++;
									matchedCases.add(j);
								} else {
									incorrectPartialCases++;
									matchedCases.add(j);
								}
							} else if (specificityRuleMap.size() > 1) {
								if (supportFactorFlag.equalsIgnoreCase("Y")) {
									if (matchingFactorFlag.equalsIgnoreCase("y")) {
										Map<String, Float> supportMap = new HashMap<String, Float>();
										for (Set<Integer> a : conceptRuleSets.get(0)) {
											float support = 0;
											int o = 0;
											for (int m : a) {
												if (partiallyMatchedRules.containsKey(m)) {
													support += specificityMap.get(m) * strengthMap.get(m)
															* partiallyMatchedRules.get(m);
												}
												o = m;
											}
											supportMap.put(map.get(o), support);
										}
										List<String> maxKeys = getMaximumValue(supportMap);
										if (maxKeys.size() == 1) {
											if (maxKeys.get(0).equalsIgnoreCase(entry.getValue())) {
												correctPartialCases++;
												matchedCases.add(j);
											} else {
												incorrectPartialCases++;
												matchedCases.add(j);
											}
										} else {
											correctPartialCases++;
											matchedCases.add(j);
										}

									} else if (matchingFactorFlag.equalsIgnoreCase("N")) {

										Map<String, Float> supportMap = new HashMap<String, Float>();
										for (Set<Integer> a : conceptRuleSets.get(0)) {
											float support = 0;
											int o = 0;
											for (int m : a) {
												if (partiallyMatchedRules.containsKey(m)) {
													support += specificityMap.get(m) * strengthMap.get(m);
												}
												o = m;
											}
											supportMap.put(map.get(o), support);
										}
										List<String> maxKeys = getMaximumValue(supportMap);
										if (maxKeys.size() == 1) {
											if (maxKeys.get(0).equalsIgnoreCase(entry.getValue())) {
												correctPartialCases++;
												matchedCases.add(j);
											} else {
												incorrectPartialCases++;
												matchedCases.add(j);
											}
										} else {
											correctPartialCases++;
											matchedCases.add(j);
										}

									}

								} else if (supportFactorFlag.equalsIgnoreCase("N")) {
									Entry<Integer, Float> entry1 = newSortedStrengthMap.entrySet().iterator().next();
									int key = entry1.getKey();
									Map<String, String> cncpt = ruleDecisionList.get(key);
									if (cncpt.get(conceptName).equalsIgnoreCase(entry.getValue())) {
										correctPartialCases++;
										matchedCases.add(j);
									} else {
										incorrectPartialCases++;
										matchedCases.add(j);
									}
								}

							}

						} else if (specificityFactorFlag.equalsIgnoreCase("N")) {

							Entry<Integer, Float> entry1 = newSortedStrengthMap.entrySet().iterator().next();
							int key = entry1.getKey();
							Map<String, String> cncpt = ruleDecisionList.get(key);
							if (cncpt.get(conceptName).equalsIgnoreCase(entry.getValue())) {
								correctPartialCases++;
								matchedCases.add(j);
							} else {
								incorrectPartialCases++;
								matchedCases.add(j);
							}

						}

					} else if (strengthRuleMap.size() == 1) {

						if (specificityFactorFlag.equalsIgnoreCase("Y")) {
							if (specificityRuleMap.size() > 1) {
								if (strengthRuleMap.entrySet().iterator().next().getKey()
										.equalsIgnoreCase(entry.getValue())) {
									correctPartialCases++;
									matchedCases.add(j);
								} else {
									incorrectPartialCases++;
									matchedCases.add(j);
								}
							} else if (specificityRuleMap.size() == 1) {
								if (strengthRuleMap.entrySet().iterator().next().getKey()
										.equalsIgnoreCase(specificityRuleMap.entrySet().iterator().next().getKey())) {
									if (strengthRuleMap.entrySet().iterator().next().getKey()
											.equalsIgnoreCase(entry.getValue())) {
										correctPartialCases++;
										matchedCases.add(j);
									} else {
										incorrectPartialCases++;
										matchedCases.add(j);
									}

								} else {
									// need to see if specificity index is smaller or strength index is smaller
									Entry<Integer, Float> entry1 = newSortedStrengthMap.entrySet().iterator().next();
									int key = entry1.getKey();
									Map<String, String> cncpt = ruleDecisionList.get(key);
									if (cncpt.get(conceptName).equalsIgnoreCase(entry.getValue())) {
										correctPartialCases++;
										matchedCases.add(j);
									} else {
										incorrectPartialCases++;
										matchedCases.add(j);
									}

								}

							}
						} else if (specificityFactorFlag.equalsIgnoreCase("N")) {
							if (strengthRuleMap.entrySet().iterator().next().getKey()
									.equalsIgnoreCase(entry.getValue())) {
								correctPartialCases++;
								matchedCases.add(j);
							} else {
								incorrectPartialCases++;
								matchedCases.add(j);
							}
						}

					}
				}
			}
			int notClassified = 0;

			System.out.println("PARTIAL MATCHING:\n" + "The total number of cases that are incorrectly classified: "
					+ incorrectPartialCases + "\n" + "The total number of cases that are correctly classified: "
					+ correctPartialCases);
			totalIncorrectCases = totalIncorrectCases + incorrectPartialCases;

			for (int j = 0; j < dataSet.size(); j++) {
				if (!matchedCases.contains(j)) {
					notClassified++;
				}
			}
			System.out.println();
			System.out.println("The total number of cases that are not classified: " + notClassified);
			float errorRate = (float) (notClassified+totalIncorrectCases) / (float) noOfRows;
			DecimalFormat df = new DecimalFormat("####0.00");
			System.out.println("Error rate: " + df.format(errorRate));
			System.out.println();
			ArrayList<Integer> matchedCases1 = new ArrayList<Integer>();

			Map<String, List<List<Integer>>> associatedConcepts = new HashMap<String, List<List<Integer>>>();
			ArrayList<Map<String, String>> conceptStat = new ArrayList<Map<String, String>>();

			for (int a = 0; a < conceptValueSets.get(0).size(); a++) {
				List<List<Integer>> concatAllTypes = new ArrayList<List<Integer>>();
				List<Integer> completeCorrect = new ArrayList<Integer>();
				List<Integer> completeIncorrect = new ArrayList<Integer>();
				Map<String, String> conceptStatMap = new HashMap<String, String>();

				int correctCompleteCases1 = 0;
				int incorrectCompleteCases1 = 0;

				for (int j = 0; j < dataSet.size(); j++) {
					for (int i = 0; i < ruleConditionsList.size(); i++) {
						if (conceptValueSets.get(0).get(a).contains(j + 1)) {
							Map<String, String> conditions = ruleConditionsList.get(i);
							boolean correctCompleteCasesFlag = false;
							Set<String> keys = conditions.keySet();
							Iterator<String> iter1 = keys.iterator();
							A: while (iter1.hasNext()) {
								Set<String> key = dataSet.get(j).keySet();
								String s1 = iter1.next();
								Iterator<String> iter = key.iterator();
								int k = 0;
								while (iter.hasNext()) {
									String s = iter.next();
									if (s.contains(s1)) {
										String ruleVal = conditions.get(s1);
										String dataVal = dataSet.get(j).get(s);

										if (!dataVal.equals("?")) {
											if (checkNumeric.get(k)) {
												String[] ruleVals = ruleVal.split("\\.\\.");
												if (dataVal.equals("*") || dataVal.equals("-")) {
													dataVal = ruleVals[0];
												}
												if (Float.parseFloat(dataVal) >= Float.parseFloat(ruleVals[0])
														&& Float.parseFloat(dataVal) <= Float.parseFloat(ruleVals[1])) {
													correctCompleteCasesFlag = true;
													break;
												} else {
													correctCompleteCasesFlag = false;
													break A;
												}

											} else {
												if (dataVal.equals("*") || dataVal.equals("-")) {
													dataVal = ruleVal;
												}
												if (dataVal.equalsIgnoreCase(ruleVal)) {
													correctCompleteCasesFlag = true;
													break;
												} else {
													correctCompleteCasesFlag = false;
													break A;
												}
											}

										} else {
											correctCompleteCasesFlag = false;
											break A;
										}
									}
									k++;
								}
							}
							if (correctCompleteCasesFlag) {
								Map.Entry<String, String> entry = ruleDecisionList.get(i).entrySet().iterator().next();
								if (entry.getValue().equalsIgnoreCase(dataSetConcepts.get(j).get(entry.getKey()))) {
									correctCompleteCases1++;
									completeCorrect.add(j);
								} else {
									incorrectCompleteCases1++;
									completeIncorrect.add(j);
								}
								matchedCases1.add(j);
								break;
							}
						}
					}
				}
				conceptStatMap.put(conceptStringlist.get(0).get(a),
						String.valueOf(correctCompleteCases1) + "," + String.valueOf(incorrectCompleteCases1));
				conceptStat.add(conceptStatMap);

				concatAllTypes.add(completeIncorrect);
				concatAllTypes.add(completeCorrect);
				associatedConcepts.put(conceptStringlist.get(0).get(a), concatAllTypes);
			}

			for (int a = 0; a < conceptValueSets.get(0).size(); a++) {
				int correctPartialCases1 = 0;
				int incorrectPartialCases1 = 0;
				List<Integer> partialCorrect = new ArrayList<Integer>();
				List<Integer> partialIncorrect = new ArrayList<Integer>();

				for (int j = 0; j < dataSet.size(); j++) {
					Map<Integer, Float> partiallyMatchedRules = new HashMap<Integer, Float>();
					if (!matchedCases1.contains(j) && conceptValueSets.get(0).get(a).contains(j + 1)) {
						for (int i = 0; i < ruleConditionsList.size(); i++) {
							Map<String, String> conditions = ruleConditionsList.get(i);
							int partiallyMatched = 0;
							Set<String> keys = conditions.keySet();
							Iterator<String> iter1 = keys.iterator();
							while (iter1.hasNext()) {
								Set<String> key = dataSet.get(j).keySet();
								String s1 = iter1.next();
								Iterator<String> iter = key.iterator();
								int k = 0;
								while (iter.hasNext()) {
									String s = iter.next();
									if (s.contains(s1)) {
										String ruleVal = conditions.get(s1);
										String dataVal = dataSet.get(j).get(s);

										if (!dataVal.equals("?")) {
											if (checkNumeric.get(k)) {
												String[] ruleVals = ruleVal.split("\\.\\.");
												if (dataVal.equals("*") || dataVal.equals("-")) {
													dataVal = ruleVals[0];
												}
												if (Float.parseFloat(dataVal) >= Float.parseFloat(ruleVals[0])
														&& Float.parseFloat(dataVal) <= Float.parseFloat(ruleVals[1])) {
													partiallyMatched++;
												}
											} else {
												if (dataVal.equals("*") || dataVal.equals("-")) {
													dataVal = ruleVal;
												}
												if (dataVal.equalsIgnoreCase(ruleVal)) {
													partiallyMatched++;
												}
											}
										}
										break;
									}
									k++;
								}
							}
							if (partiallyMatched > 0) {
								Float PMF = (float) partiallyMatched / (float) keys.size();
								partiallyMatchedRules.put(i, PMF);
							}
						}

						Map<Integer, Float> specificityMap = new HashMap<Integer, Float>();
						Map<Integer, Float> conditionalProbMap = new HashMap<Integer, Float>();
						Map<Integer, Float> strengthMap = new HashMap<Integer, Float>();
						for (int l = 0; l < parameters.size(); l++) {
							if (partiallyMatchedRules.containsKey(l)) {
								String param = parameters.get(l);
								String specificity = param.substring(0, param.indexOf(","));
								specificityMap.put(l, Float.parseFloat(specificity));
								param = param.substring(param.indexOf(" ") + 1);
								String strength = param.substring(0, param.indexOf(","));
								strengthMap.put(l, Float.parseFloat(strength));
								String conditional = param.substring(param.indexOf(",") + 1);
								Float conditionalProb = Float.parseFloat(strength) / Float.parseFloat(conditional);
								conditionalProbMap.put(l, conditionalProb);
							}
						}
						Map<Integer, Float> newSortedSpecificityMap = sortMap(specificityMap);
						Map<Integer, Float> newSortedStrengthMap = sortMap(strengthMap);
						Map<Integer, Float> newSortedConditionalMap = sortMap(conditionalProbMap);

						Map<String, Integer> strengthRuleMap = new HashMap<String, Integer>();
						Map<String, Integer> specificityRuleMap = new HashMap<String, Integer>();

						if (strengthFactorFlag.equals("s")) {
							strengthRuleMap = getStrength(ruleDecisionList, newSortedStrengthMap, strengthRuleMap);
						} else if (strengthFactorFlag.equals("p")) {
							strengthRuleMap = getStrength(ruleDecisionList, newSortedConditionalMap, strengthRuleMap);
						}
						specificityRuleMap = getStrength(ruleDecisionList, newSortedSpecificityMap, specificityRuleMap);
						Map.Entry<String, String> entry = dataSetConcepts.get(j).entrySet().iterator().next();
						if (strengthRuleMap.size() > 1) {

							if (specificityFactorFlag.equalsIgnoreCase("Y")) {
								if (specificityRuleMap.size() == 1) {
									if (specificityRuleMap.entrySet().iterator().next().getKey()
											.equalsIgnoreCase(entry.getValue())) {
										correctPartialCases1++;
										partialCorrect.add(j);
										matchedCases1.add(j);
									} else {
										incorrectPartialCases1++;
										partialIncorrect.add(j);
										matchedCases1.add(j);
									}
								} else if (specificityRuleMap.size() > 1) {
									if (supportFactorFlag.equalsIgnoreCase("Y")) {
										if (matchingFactorFlag.equalsIgnoreCase("y")) {
											Map<String, Float> supportMap = new HashMap<String, Float>();
											for (Set<Integer> b : conceptRuleSets.get(0)) {
												float support = 0;
												int o = 0;
												for (int m : b) {
													if (partiallyMatchedRules.containsKey(m)) {
														support += specificityMap.get(m) * strengthMap.get(m)
																* partiallyMatchedRules.get(m);
													}
													o = m;
												}
												supportMap.put(map.get(o), support);
											}
											List<String> maxKeys = getMaximumValue(supportMap);
											if (maxKeys.size() == 1) {
												if (maxKeys.get(0).equalsIgnoreCase(entry.getValue())) {
													correctPartialCases1++;
													partialCorrect.add(j);
													matchedCases1.add(j);
												} else {
													incorrectPartialCases1++;
													partialIncorrect.add(j);
													matchedCases1.add(j);
												}
											} else {
												correctPartialCases1++;
												partialCorrect.add(j);
												matchedCases1.add(j);
											}

										} else if (matchingFactorFlag.equalsIgnoreCase("N")) {

											Map<String, Float> supportMap = new HashMap<String, Float>();
											for (Set<Integer> b : conceptRuleSets.get(0)) {
												float support = 0;
												int o = 0;
												for (int m : b) {
													if (partiallyMatchedRules.containsKey(m)) {
														support += specificityMap.get(m) * strengthMap.get(m);
													}
													o = m;
												}
												supportMap.put(map.get(o), support);
											}
											List<String> maxKeys = getMaximumValue(supportMap);
											if (maxKeys.size() == 1) {
												if (maxKeys.get(0).equalsIgnoreCase(entry.getValue())) {
													correctPartialCases1++;
													partialCorrect.add(j);
													matchedCases1.add(j);
												} else {
													incorrectPartialCases1++;
													partialIncorrect.add(j);
													matchedCases1.add(j);
												}
											} else {
												correctPartialCases1++;
												partialCorrect.add(j);
												matchedCases1.add(j);
											}

										}

									} else if (supportFactorFlag.equalsIgnoreCase("N")) {
										Entry<Integer, Float> entry1 = newSortedStrengthMap.entrySet().iterator()
												.next();
										int key = entry1.getKey();
										Map<String, String> cncpt = ruleDecisionList.get(key);
										if (cncpt.get(conceptName).equalsIgnoreCase(entry.getValue())) {
											correctPartialCases1++;
											partialCorrect.add(j);
											matchedCases1.add(j);
										} else {
											incorrectPartialCases1++;
											partialIncorrect.add(j);
											matchedCases1.add(j);
										}
									}

								}

							} else if (specificityFactorFlag.equalsIgnoreCase("N")) {

								Entry<Integer, Float> entry1 = newSortedStrengthMap.entrySet().iterator().next();
								int key = entry1.getKey();
								Map<String, String> cncpt = ruleDecisionList.get(key);
								if (cncpt.get(conceptName).equalsIgnoreCase(entry.getValue())) {
									correctPartialCases1++;
									partialCorrect.add(j);
									matchedCases1.add(j);
								} else {
									incorrectPartialCases1++;
									partialIncorrect.add(j);
									matchedCases1.add(j);
								}

							}

						} else if (strengthRuleMap.size() == 1) {

							if (specificityFactorFlag.equalsIgnoreCase("Y")) {
								if (specificityRuleMap.size() > 1) {
									if (strengthRuleMap.entrySet().iterator().next().getKey()
											.equalsIgnoreCase(entry.getValue())) {
										correctPartialCases1++;
										partialCorrect.add(j);
										matchedCases1.add(j);
									} else {
										incorrectPartialCases1++;
										partialIncorrect.add(j);
										matchedCases1.add(j);
									}
								} else if (specificityRuleMap.size() == 1) {
									if (strengthRuleMap.entrySet().iterator().next().getKey().equalsIgnoreCase(
											specificityRuleMap.entrySet().iterator().next().getKey())) {
										if (strengthRuleMap.entrySet().iterator().next().getKey()
												.equalsIgnoreCase(entry.getValue())) {
											correctPartialCases1++;
											partialCorrect.add(j);
											matchedCases1.add(j);
										} else {
											incorrectPartialCases1++;
											partialIncorrect.add(j);
											matchedCases1.add(j);
										}

									} else {
										Entry<Integer, Float> entry1 = newSortedStrengthMap.entrySet().iterator()
												.next();
										int key = entry1.getKey();
										Map<String, String> cncpt = ruleDecisionList.get(key);
										if (cncpt.get(conceptName).equalsIgnoreCase(entry.getValue())) {
											correctPartialCases1++;
											partialCorrect.add(j);
											matchedCases1.add(j);
										} else {
											incorrectPartialCases1++;
											partialIncorrect.add(j);
											matchedCases1.add(j);
										}

									}

								}
							} else if (specificityFactorFlag.equalsIgnoreCase("N")) {
								if (strengthRuleMap.entrySet().iterator().next().getKey()
										.equalsIgnoreCase(entry.getValue())) {
									correctPartialCases1++;
									partialCorrect.add(j);
									matchedCases1.add(j);
								} else {
									incorrectPartialCases1++;
									partialIncorrect.add(j);
									matchedCases1.add(j);
								}
							}

						}
					}
				}
				for (Map<String, String> m : conceptStat) {
					Set<String> key = m.keySet();
					if (key.contains(conceptStringlist.get(0).get(a))) {
						String val = m.get(conceptStringlist.get(0).get(a));
						val = val.concat("," + String.valueOf(correctPartialCases1) + ","
								+ String.valueOf(incorrectPartialCases1));
						m.put(conceptStringlist.get(0).get(a), val);
					}
				}

				List<List<Integer>> concatTypes = associatedConcepts.get(conceptStringlist.get(0).get(a));
				concatTypes.add(partialIncorrect);
				concatTypes.add(partialCorrect);
				associatedConcepts.put(conceptStringlist.get(0).get(a), concatTypes);

			}

			if (conceptStatsFlag.equalsIgnoreCase("y")) {
				System.out.println("*********************************************************************");
				System.out.println("CONCEPT STATISTICS");
				System.out.println("*********************************************************************");

				for (Map<String, String> m : conceptStat) {
					Map.Entry<String, String> entry = m.entrySet().iterator().next();

					System.out.println("Concept (" + conceptName + ", " + entry.getKey() + "):");
					String val = entry.getValue();
					String[] vals = val.split(",");

					System.out.println("COMPLETE MATCHING:\n"
							+ "The total number of cases that are incorrectly classified: " + vals[1] + "\n"
							+ "The total number of cases that are correctly classified: " + vals[0]);

					System.out.println("PARTIAL MATCHING:\n"
							+ "The total number of cases that are incorrectly classified: " + vals[3] + "\n"
							+ "The total number of cases that are correctly classified: " + vals[2]);

					for (int i = 0; i < conceptStringlist.get(0).size(); i++) {
						if (entry.getKey().equalsIgnoreCase(conceptStringlist.get(0).get(i))) {
							System.out.println("The total number of cases in the concept: "
									+ conceptValueSets.get(0).get(i).size());
						}
					}
					System.out.println();
				}
			}

			if (conceptClassifiedFlag.equalsIgnoreCase("y")) {
				System.out.println("*********************************************************************");
				System.out.println("HOW CASES ASSOCIATED WITH CONCEPTS ARE CLASSIFIED ");
				System.out.println("*********************************************************************");

				Set<String> keys = associatedConcepts.keySet();
				Iterator<String> iter1 = keys.iterator();
				while (iter1.hasNext()) {
					String s1 = iter1.next();
					System.out.println("Concept (" + conceptName + ", " + s1 + "):");
					List<List<Integer>> concatResult = associatedConcepts.get(s1);
					for (int i = 0; i < concatResult.size(); i++) {
						if (i == 2) {
							System.out.println("COMPLETE MATCHING:");
							System.out.print("List of cases that are incorrectly classified: ");
							if (concatResult.get(0).isEmpty()) {
								System.out.print("NONE");
							} else {
								for (int l = 0; l < concatResult.get(0).size(); l++) {
									System.out.print(concatResult.get(0).get(l) + 1);
									if (l != concatResult.get(0).size() - 1)
										System.out.print(",");
								}

							}
							System.out.println();
						}
						if (i == 3) {
							System.out.print("List of cases that are correctly classified: ");
							if (concatResult.get(1).isEmpty()) {
								System.out.print("NONE");
							} else {
								for (int l = 0; l < concatResult.get(1).size(); l++) {
									System.out.print(concatResult.get(1).get(l) + 1);
									if (l != concatResult.get(1).size() - 1)
										System.out.print(",");
								}

							}
							System.out.println();
						}
						if (i == 0) {
							System.out.println("PARTIAL MATCHING:");
							System.out.print("List of cases that are incorrectly classified: ");
							if (concatResult.get(2).isEmpty()) {
								System.out.print("NONE");
							} else {
								for (int l = 0; l < concatResult.get(2).size(); l++) {
									System.out.print(concatResult.get(2).get(l) + 1);
									if (l != concatResult.get(2).size() - 1)
										System.out.print(",");
								}

							}
							System.out.println();
						}
						if (i == 1) {
							System.out.print("List of cases that are correctly classified: ");
							if (concatResult.get(3).isEmpty()) {
								System.out.print("NONE");
							} else {
								for (int l = 0; l < concatResult.get(3).size(); l++) {
									System.out.print(concatResult.get(3).get(l) + 1);
									if (l != concatResult.get(3).size() - 1)
										System.out.print(",");
								}
							}
							System.out.println();
						}
					}
					System.out.println();
				}

			}

			quesflag = true;
			while (quesflag) {
				System.out.println("Whether you wish to exit the program? y/n");
				String againRun = scan_string.next();
				if (againRun.equalsIgnoreCase("y") || againRun.equalsIgnoreCase("n")) {
					if (againRun.equalsIgnoreCase("y")) {
						runAgain = false;
					}else {
						dataTable = new ArrayList<ArrayList<String>>();
					    checkNumeric = new ArrayList<Boolean>();
						checkSymbols = new ArrayList<Boolean>();
						conceptValueSets = new ArrayList<ArrayList<Set<Integer>>>();
						conceptStringlist = new ArrayList<ArrayList<String>>();
						runAgain = true;
					}
					quesflag = false;
				}
			}
		}
		scan_string.close();
		System.out.println("PROGRAM FINISHED");
	}

	private static Map<String, Integer> getStrength(ArrayList<Map<String, String>> ruleDecisionList,
			Map<Integer, Float> newSortedStrengthMap, Map<String, Integer> strengthRuleMap) {
		for (int i = 0; i < ruleDecisionList.size(); i++) {
			if (newSortedStrengthMap.keySet().contains(i)) {
				Map.Entry<String, String> entry = ruleDecisionList.get(i).entrySet().iterator().next();
				if (strengthRuleMap.size() != 0 && strengthRuleMap.containsKey(entry.getValue())) {
					int val = strengthRuleMap.get(entry.getValue());
					strengthRuleMap.put(entry.getValue(), val + 1);
				} else {
					strengthRuleMap.put(entry.getValue(), 1);
				}

			}
		}
		return strengthRuleMap;
	}

	private static List<String> getMaximumValue(Map<String, Float> specificityRuleMap) {
		float maxValue = 0;
		List<String> maxKeys = new ArrayList<>();
		for (Map.Entry<String, Float> entry : specificityRuleMap.entrySet()) {
			if (maxValue == 0 || maxValue == entry.getValue()) {
				maxValue = entry.getValue();
				maxKeys.add(entry.getKey());
			} else if (entry.getValue() > maxValue) {
				maxValue = entry.getValue();
				maxKeys.clear();
				maxKeys.add(entry.getKey());
			}
		}
		return maxKeys;
	}

	private static Map<Integer, Float> sortMap(Map<Integer, Float> specificityMap) {
		List<Integer> mapKeys = new ArrayList<>(specificityMap.keySet());
		List<Float> mapValues = new ArrayList<>(specificityMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		LinkedHashMap<Integer, Float> sortedSpecificityMap = new LinkedHashMap<Integer, Float>();

		Iterator<Float> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Float val = valueIt.next();
			Iterator<Integer> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Integer key = keyIt.next();
				Float comp1 = specificityMap.get(key);
				Float comp2 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedSpecificityMap.put(key, val);
					break;
				}
			}
		}

		Map<Integer, Float> newSortedSpecificityMap = new HashMap<Integer, Float>();

		ArrayList<Integer> keys = new ArrayList<Integer>(sortedSpecificityMap.keySet());
		for (int i = keys.size() - 1; i >= 0; i--) {
			if (i == 0) {
				newSortedSpecificityMap.put(keys.get(i), sortedSpecificityMap.get(keys.get(i)));
			} else {
				if (sortedSpecificityMap.get(keys.get(i - 1)) < sortedSpecificityMap.get(keys.get(i))) {
					newSortedSpecificityMap.put(keys.get(i), sortedSpecificityMap.get(keys.get(i)));
					break;
				} else if (Math.abs(sortedSpecificityMap.get(keys.get(i - 1))
						- sortedSpecificityMap.get(keys.get(i))) < 0.00000001) {
					newSortedSpecificityMap.put(keys.get(i), sortedSpecificityMap.get(keys.get(i)));
				}

			}
		}
		return newSortedSpecificityMap;
	}
}
