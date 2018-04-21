
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class InputData {
	private Scanner scan;

	public ArrayList<ArrayList<String>> readdata(String inputFile) throws Exception {
		scan = new Scanner(new File(inputFile));
		int j, i = 0;
		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		{
			StringBuffer buffer = null;
			buffer = new StringBuffer(scan.nextLine());
			Scanner trial = new Scanner(buffer.toString());
			j = 0;
			ArrayList<String> col = new ArrayList<String>();
			while (trial.hasNext()) {
				String s = trial.next();
				if (i < 2)
					if ((!s.equalsIgnoreCase("<")) && (!s.equalsIgnoreCase(">")) && (!s.equalsIgnoreCase("["))
							&& (!s.equalsIgnoreCase("]")))
						col.add(j++, s);
			}
			trial.close();
			array.add(i++, col);
		}
		{
			StringBuffer buffer = null;
			buffer = new StringBuffer(scan.nextLine());
			Scanner trial = new Scanner(buffer.toString());
			j = 0;
			ArrayList<String> col = new ArrayList<String>();
			while (trial.hasNext()) {
				String s = trial.next();
				if ((!s.equalsIgnoreCase("<")) && (!s.equalsIgnoreCase(">")) && (!s.equalsIgnoreCase("["))
						&& (!s.equalsIgnoreCase("]"))) {
					col.add(j++, s);
				}

			}
			trial.close();
			array.add(i++, col);
		}
		while (scan.hasNextLine()) {
			StringBuffer buffer = null;
			buffer = new StringBuffer(scan.nextLine());
			Scanner trial = new Scanner(buffer.toString());
			j = 0;
			ArrayList<String> col = new ArrayList<String>();
			Boolean pointer = false;
			AA: while (trial.hasNext()) {
				pointer = true;
				String s = trial.next();
				if (s.charAt(0) == '!' || s.equals(null)) {
					pointer = false;
					break AA;
				} else {
					col.add(j++, s);
				}
			}
			trial.close();
			if (pointer) {
				array.add(i++, col);
			}
		}
		scan.close();
		return array;
	}

	public ArrayList<ArrayList> readrules(String inputFile) throws Exception {
		ArrayList<Map<String, String>> rule = new ArrayList<Map<String, String>>();
		ArrayList<String> parameters = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
		String line;
		ArrayList<ArrayList> c = new ArrayList<ArrayList>();
		ArrayList<ArrayList> b = new ArrayList<ArrayList>();
		while ((line = br.readLine()) != null) {

			Map<String, String> abc = new LinkedHashMap<String, String>();
			Map<String, String> xyz = new LinkedHashMap<String, String>();
			if (!line.isEmpty()) {
				if (line.charAt(0) != '!') {
					parameters.add(line);
					line = br.readLine();
					if (line.contains("->")) {
						String[] line1 = line.split("->");
						if (line1[0].contains("&")) {
							String[] line2 = line1[0].split("&");
							for (int i = 0; i < line2.length; i++) {
								line2[i] = line2[i].trim();
								abc.put(line2[i].substring(1, line2[i].indexOf(",")),
										line2[i].substring(line2[i].indexOf(",") + 1, line2[i].indexOf(")")));
							}
						} else {
							abc.put(line1[0].substring(1, line1[0].indexOf(",")),
									line1[0].substring(line1[0].indexOf(",") + 1, line1[0].indexOf(")")));
						}
						line1[1] = line1[1].trim();
						xyz.put(line1[1].substring(1, line1[1].indexOf(",")),
								line1[1].substring(line1[1].indexOf(",") + 1, line1[1].indexOf(")")));

					} else {
						if (line.contains("&")) {
							String[] line2 = line.split("&");
							for (int i = 0; i < line2.length; i++) {
								line2[i] = line2[i].trim();
								abc.put(line2[i].substring(1, line2[i].indexOf(",")),
										line2[i].substring(line2[i].indexOf(",") + 1, line2[i].indexOf(")")));
							}
						} else {
							abc.put(line.substring(1, line.indexOf(",")),
									line.substring(line.indexOf(",") + 1, line.indexOf(")")));
						}

						line = br.readLine();
						line = line.substring(line.indexOf("("));
						xyz.put(line.substring(1, line.indexOf(",")),
								line.substring(line.indexOf(",") + 1, line.indexOf(")")));
					}

					rule.add(abc);
					rule.add(xyz);
					b.add(parameters);
					b.add(rule);
				}
			}
			b.add(parameters);
			b.add(rule);
		}

		br.close();
		return b;

	}

}
