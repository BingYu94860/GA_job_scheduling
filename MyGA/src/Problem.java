import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Problem {
	private int V = 0;
	private int P = 0;
	private int E = 0;
	double[][] cost = null;
	int[][] line = null;

	private double[][] processor = null;

	// String fileName = "n4_00.dag";
	void readFromFile(String fileName) {
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			if (fstream != null)
				System.out.println("open success");
			String strLine;
			while ((strLine = br.readLine()) != null)
				if (!(strLine.startsWith("/") || strLine.startsWith("*") || strLine.startsWith(" "))) {
					P = Integer.parseInt(strLine);
					processor = new double[P][];
					for (int i = 0; i < P; i++)
						processor[i] = new double[P];
					break;
				}
			while ((strLine = br.readLine()) != null)
				if (!(strLine.startsWith("/") || strLine.startsWith("*") || strLine.startsWith(" "))) {
					V = Integer.parseInt(strLine);
					cost = new double[V][];
					for (int i = 0; i < V; i++)
						cost[i] = new double[P];
					break;
				}
			while ((strLine = br.readLine()) != null)
				if (!(strLine.startsWith("/") || strLine.startsWith("*") || strLine.startsWith(" "))) {
					E = Integer.parseInt(strLine);
					line = new int[E][];
					for (int i = 0; i < E; i++)
						line[i] = new int[3];
					break;
				}
			for (int i = 0; i < P; i++) {
				while ((strLine = br.readLine()) != null) {

					if (!(strLine.startsWith("/") || strLine.startsWith("*") || strLine.startsWith(" "))) {
						String[] splitStr = strLine.split(" ");
						for (int j = 0; j < P; j++)
							processor[i][j] = Double.parseDouble(splitStr[j]);
						break;
					}
				}
			}
			for (int i = 0; i < V; i++) {
				while ((strLine = br.readLine()) != null) {
					if (!(strLine.startsWith("/") || strLine.startsWith("*") || strLine.startsWith(" "))) {
						String[] splitStr = strLine.split(" ");
						for (int j = 0; j < P; j++)
							cost[i][j] = Double.parseDouble(splitStr[j]);
						break;
					}
				}
			}
			for (int i = 0; i < E; i++) {
				while ((strLine = br.readLine()) != null) {
					if (!(strLine.startsWith("/") || strLine.startsWith("*") || strLine.startsWith(" "))) {
						String[] splitStr = strLine.split(" ");
						line[i][0] = Integer.parseInt(splitStr[0]);
						line[i][1] = Integer.parseInt(splitStr[1]);
						line[i][2] = Integer.parseInt(splitStr[2]);
						break;
					}
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}// End of void readFromFile()
}// End of class ProblemRead
