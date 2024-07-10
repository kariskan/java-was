package codesquad.domain;

public class Path {
	private final String url;
	private final Parameters parameters;

	public Path(String path) {
		String[] split = path.split("\\?");
		this.url = split[0];
		if (split.length > 1) {
			this.parameters = new Parameters(split[1]);
		} else {
			this.parameters = null;
		}
	}

	public String getUrl() {
		return url;
	}

	public Parameters getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "Path{" +
			"url='" + url + '\'' +
			", parameters=" + parameters +
			'}';
	}
}
