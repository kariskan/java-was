package codesquad.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import codesquad.error.BaseException;

public class Parameters {
	private final Map<String, String> params;

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
			.orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST, "Key " + key + " not found"));
	}
}
