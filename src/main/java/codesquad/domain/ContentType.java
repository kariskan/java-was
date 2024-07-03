package codesquad.domain;

import java.util.Arrays;

public enum ContentType {

	HTML( "text/html"),
	CSS( "text/css"),
	JS( "text/javascript"),
	ICO( "image/x-icon"),
	PNG( "image/png"),
	JPG( "image/jpeg"),
	SVG( "image/svg+xml");

	private final String mimeType;

	ContentType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public static ContentType from(String extension) {
		return Arrays.stream(values())
			.filter(contentType -> contentType.name().equalsIgnoreCase(extension))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("unsupported content type"));
	}
}
