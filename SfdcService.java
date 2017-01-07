package SfdcService;

import java.util.ArrayList;
import java.util.List;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * SalesforceのデータをPartner APIを利用して取得するサービスクラスです
 * @author kohei.kanno
 */
public class SfdcService {
	private static final String USER_NAME = "name";
	private static final String USER_PASSWORD = "pw";
	private static final String AUTH_END_POINT = "URL";
	private static final Integer QUERY_BATCH_SIZE = 2000;

	private PartnerConnection connection = null;

	/**
	 * コンストラクタ
	 * @param
	 * @return SfdcService
	 */
	private SfdcService(){
		login();
	}

	/**
	 * SfdcServiceを使用します
	 * @param
	 * @return SfdcService
	 */
	public static SfdcService use(){
		return new SfdcService();
	}

	/**
	 * Salesforceにログインします
	 * @param
	 * @return
	 */
	private void login(){
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(USER_NAME);
		config.setPassword(USER_PASSWORD);
		config.setAuthEndpoint(AUTH_END_POINT);
		try {
			connection = new PartnerConnection(config);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 学習レコードを取得
	 * @param
	 * @return
	 */
	public List<Sfdc> getStudyList(){
		List<Sfdc> studyList = new ArrayList<Sfdc>();
		try{
			connection.setQueryOptions(QUERY_BATCH_SIZE);
			String soql = "Select key1,key2,key3,key4 From table";
			QueryResult qr = connection.query(soql);
			boolean done = false;
			while(!done){
				SObject[] records = qr.getRecords();
				for(int i = 0; i < records.length; i++){
					SObject sObj = records[i];
					Sfdc resultList = new Sfdc();
					resultList.setassignedTeam(String.valueOf(sObj.getField("key1")));
					resultList.setquestion(String.valueOf(sObj.getField("key2")));
					resultList.setanswer(String.valueOf(sObj.getField("key3")));
					resultList.settaskNo(String.valueOf(sObj.getField("key4")));
					studyList.add(resultList);
				}
				if(qr.isDone()){
					done = true;
				}else{
					qr = connection.queryMore(qr.getQueryLocator());
				}
			}
		}catch(ConnectionException ce){
			ce.printStackTrace();
		}
		return studyList;
	}

	

	/**
	 * 学習インナークラス
	 */
	public class Sfdc{
		private String assignedTeam;
		private String question;
		private String answer;
		private String taskNo;
		public void setassignedTeam(String assignedTeam) {
			this.assignedTeam = assignedTeam;
		}
		public void setquestion(String question) {
			this.question = question;
		}
		public void setanswer(String answer) {
			this.answer = answer;
		}
		public void settaskNo(String taskNo) {
			this.taskNo = taskNo;
		}
		public String getTaskNo() {
			return taskNo;
		}
	}

}