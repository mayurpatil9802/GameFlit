package com.example.Game_Details.Entity;

public class GameSpecs {
	private String gameName;
	private Requirements minimum;
	private Requirements recommended;
	private String imageUrl;

	// Getters & setters

	public static class Requirements {
		private String cpu;
		private String gpu;
		private String ram;
		private String fileSize;
		private String os;

		// Getters & setters
		public String getCpu() { return cpu; }
		public void setCpu(String cpu) { this.cpu = cpu; }

		public String getGpu() { return gpu; }
		public void setGpu(String gpu) { this.gpu = gpu; }

		public String getRam() { return ram; }
		public void setRam(String ram) { this.ram = ram; }

		public String getFileSize() { return fileSize; }
		public void setFileSize(String fileSize) { this.fileSize = fileSize; }

		public String getOs() { return os; }
		public void setOs(String os) { this.os = os; }
	}

	public String getGameName() { return gameName; }
	public void setGameName(String gameName) { this.gameName = gameName; }

	public Requirements getMinimum() { return minimum; }
	public void setMinimum(Requirements minimum) { this.minimum = minimum; }

	public Requirements getRecommended() { return recommended; }
	public void setRecommended(Requirements recommended) { this.recommended = recommended; }

	public String getImageUrl() { return imageUrl; }
	public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
