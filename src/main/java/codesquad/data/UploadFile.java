package codesquad.data;

public record UploadFile(
	Long id,
	String filename,
	byte[] data
) {

	public String getExtension() {
		return filename.substring(filename.lastIndexOf('.')).toLowerCase();
	}
}
