import PIODataImportService.*;

/**
 * PredictionIOにSalesforceのQAListをインポートする処理
 * @author kohei.kanno
 */
public class PIOCmdLineSampleApp {
	/**
	 * mainメソッド
	 * @param args
	 * @return
	 */
	public static void main(String[] args) {
		PIODataImportService service = new PIODataImportService();

		service.addStudyList();
		System.out.println("--- List import done ---");
	}
}