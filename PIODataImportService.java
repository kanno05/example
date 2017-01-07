package PIODataImportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import io.prediction.APIResponse;
import io.prediction.EventClient;
import io.prediction.FutureAPIResponse;
import SfdcService.*;
/**
 * Predict.*ionIOのEventServerにEventを送信するサービスクラス
 * @author kohei.kanno
 */
public class PIODataImportService {
	private static final String APP_URL = "APP_URL";
    private static final String ACCESS_KEY = "key";




	/**
	 * QAListを追加
	 * @param
	 * @return
	 */
	public void addStudyList(){
		EventClient client = new EventClient(ACCESS_KEY, APP_URL);
		List<FutureAPIResponse> futureAPIResponses = new ArrayList<>();

		try {
			for(SfdcService.Sfdc customer : SfdcService.use().getStudyList()){
				Map<String, Object> emptyUserProperties = new HashMap<String, Object>();
					FutureAPIResponse future = client.setUserAsFuture(customer.getTaskNo(), emptyUserProperties);
					futureAPIResponses.add(future);
					Futures.addCallback(future.getAPIResponse(), getFutureCallback("task" + customer.getTaskNo()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
	}

	/**
	 * EventServerとのコールバック処理です
	 * @param name
	 * @return FutureCallback<APIResponse>
	 */
	private FutureCallback<APIResponse> getFutureCallback(final String name) {
		return new FutureCallback<APIResponse>(){
			@Override
			public void onSuccess(APIResponse response) {
				System.out.println(name + " added: " + response.getMessage());
			}

			@Override
			public void onFailure(Throwable thrown) {
				System.out.println("failed to add " + name + ": " + thrown.getMessage());
			}
		};
	}
}