package codesquad.domain;

import codesquad.error.BaseException;
import java.util.Arrays;

public enum ContentType {

	HTML("text/html", ".html"),
	CSS("text/css", ".css"),
	JS("text/javascript", ".js"),
	ICO("image/x-icon", ".ico"),
	PNG("image/png", ".png"),
	JPG("image/jpeg", ".jpeg"),
	SVG("image/svg+xml", ".svg");

	private final String mimeType;
	private final String extension;

	ContentType(String mimeType, String extension) {
		this.mimeType = mimeType;
		this.extension = extension;
	}

	public static ContentType from(String extension) {
		return Arrays.stream(values())
			.filter(contentType -> contentType.getExtension().equalsIgnoreCase(extension))
			.findFirst()
			.orElseThrow(() -> new BaseException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "unsupported content type"));
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getExtension() {
		return extension;
	}
}
