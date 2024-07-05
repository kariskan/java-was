package codesquad.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Parameters {
	private Map<String, String> params;

	public Parameters(String params) {
		String[] split = params.split("&");
		Map<String, String> paramsMap = new HashMap<>();
		for (String s : split) {
			String[] keyValue = s.split("=");
			paramsMap.put(keyValue[0], keyValue[1]);
		}
		this.params = paramsMap;
	}

	public String getValueByKey(String key) {
		return Optional.ofNullable(params.get(key))
			.orElseThrow(() -> new IllegalArgumentException("Key " + key + " not found"));
	}
}
