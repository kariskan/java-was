package codesquad.domain;

public record RequestLine(
	HttpMethod method,
	Path path,
	HttpProtocol protocol
) {

	public String getUrl() {
		return path.getUrl();
	}
}
