package codesquad.data;

public record UploadFile(
	Long id,
	String filename,
	byte[] data
) {

	public String getMimeType() {
		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
		return switch (extension) {
			case "png" -> "image/png";
			case "gif" -> "image/gif";
			case "jpg", "jpeg" -> "image/jpeg";
			case "bmp" -> "image/bmp";
			case "webp" -> "image/webp";
			default -> "application/octet-stream";
		};
	}
}
