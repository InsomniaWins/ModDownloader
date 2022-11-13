package ingram.andrew;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ModDownloader extends Application {
	
	private Label directoryLabel = null;
	private File selectedDirectory = null;
	private Alert downloadAlert;
	private ProgressBar downloadProgress;
	private ProgressBar modDownloadProgress;
	private Label modDownloadProgressLabel;
	private Label downloadProgressLabel;
	private ArrayList<Mod> mods = new ArrayList<Mod>();
	private int downloadsCompleted = 0;
	private int totalDownloads = 0;
	private Label modStatusLabel;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		defineMods();
		
		BorderPane rootNode = new BorderPane();
		Scene mainScene = new Scene(rootNode);
		
		DirectoryChooser outputSelector = new DirectoryChooser();
		outputSelector.setTitle("Select your mods folder!");
		
		VBox centerVBox = new VBox();
		centerVBox.setSpacing(7);
		centerVBox.setAlignment(Pos.CENTER);
		rootNode.setCenter(centerVBox);
		
		HBox directoryHBox = new HBox();
		directoryHBox.setAlignment(Pos.CENTER);
		directoryHBox.getChildren().add(new Label("Selected Mods Folder: "));
		directoryLabel = new Label("NO MODS FOLDER SELECTED!");
		directoryLabel.setFont(Font.font("Ariel", FontWeight.BOLD, FontPosture.REGULAR, 16));
		directoryHBox.getChildren().add(directoryLabel);
		
		centerVBox.getChildren().add(directoryHBox);
		
		HBox buttonHBox = new HBox(32);
		buttonHBox.setAlignment(Pos.CENTER);
		centerVBox.getChildren().add(buttonHBox);
		
		Button selectDirectoryButton = new Button("Choose \"mods\" Folder");
		selectDirectoryButton.setMinWidth(150);
		selectDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				outputSelector.setInitialDirectory(selectedDirectory);
				selectedDirectory = outputSelector.showDialog(stage);
				updateDirectoryLabel();
			}
		});
		buttonHBox.getChildren().add(selectDirectoryButton);
		
		Button downloadButton = new Button("Download Mods");
		downloadButton.setMinWidth(150);
		downloadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				if (selectedDirectory == null) {
					
					Alert noDirAlert = new Alert(Alert.AlertType.ERROR, "Please select a \"mods\" folder before downloading!");
					noDirAlert.setTitle("No Directory Selected");
					noDirAlert.show();
					return;
				}
				
				downloadAlert.setContentText("Are you sure you want to download mods to:\n" + selectedDirectory.toString());
				Optional<ButtonType> option = downloadAlert.showAndWait();
				if (option.get() == ButtonType.OK) {
					
					downloadButton.setDisable(true);
					selectDirectoryButton.setDisable(true);
					
					downloadMods();
					
					
				} else {
					
				}
			}
		});
		buttonHBox.getChildren().add(downloadButton);
		
		downloadProgressLabel = new Label("Download Progress:");
		centerVBox.getChildren().add(downloadProgressLabel);
		
		downloadProgress = new ProgressBar(0.5);
		downloadProgress.setMinWidth(290);
		centerVBox.getChildren().add(downloadProgress);
		
		modDownloadProgressLabel = new Label("");
		centerVBox.getChildren().add(modDownloadProgressLabel);
		
		modStatusLabel = new Label("");
		centerVBox.getChildren().add(modStatusLabel);
		
		modDownloadProgress = new ProgressBar(0.5);
		modDownloadProgress.setMinWidth(290);
		centerVBox.getChildren().add(modDownloadProgress);
		
		downloadProgressLabel.setVisible(false);
		downloadProgress.setVisible(false);
		modDownloadProgressLabel.setVisible(false);
		modDownloadProgress.setVisible(false);
		
		downloadAlert = new Alert(Alert.AlertType.CONFIRMATION);
		downloadAlert.setTitle("Confirm Download");
		
		stage.setScene(mainScene);
		stage.setWidth(500);
		stage.setHeight(500);
		stage.show();
		stage.setTitle("Mod Downloader 1.0 (Christmas 2022) 1.12.2");
		
		
	}
	
	@Override
	public void stop() {
	}
	
	public void defineMods() {
		
		mods.add(new Mod("OptiFine_1.12.2_HD_U_G5.jar","https://drive.google.com/uc?export=download&id=1RAeg0_w-sKI-OaJAbQmGqF7x1mLAlrTX", 2669107));
		
		mods.add(new Mod("aeble_v1.4.3.jar","https://mediafilez.forgecdn.net/files/3191/890/aeble_v1.4.3.jar",36276));
		mods.add(new Mod("aether-1.12.2-v1.5.3.2.jar","https://mediafilez.forgecdn.net/files/3280/119/aether-1.12.2-v1.5.3.2.jar",14733287));
		mods.add(new Mod("appliedenergistics2-rv6-stable-7.jar","https://mediafilez.forgecdn.net/files/2747/63/appliedenergistics2-rv6-stable-7.jar",4062630));
		mods.add(new Mod("ArtemisLib-1.12.2-v1.0.6.jar","https://mediafilez.forgecdn.net/files/2741/812/ArtemisLib-1.12.2-v1.0.6.jar", 21318));
		mods.add(new Mod("AutoRegLib-1.3-32.jar","https://mediafilez.forgecdn.net/files/2746/11/AutoRegLib-1.3-32.jar", 108306));
		mods.add(new Mod("Baubles-1.12-1.5.2.jar","https://mediafilez.forgecdn.net/files/2518/667/Baubles-1.12-1.5.2.jar", 108450));
		mods.add(new Mod("bettercaves-1.12.2-2.0.4.jar","https://mediafilez.forgecdn.net/files/3003/242/bettercaves-1.12.2-2.0.4.jar", 193604));
		mods.add(new Mod("BetterMineshaftsForge-1.12.2-2.2.1.jar","https://mediafilez.forgecdn.net/files/3247/154/BetterMineshaftsForge-1.12.2-2.2.1.jar", 305086));
		
		mods.add(new Mod("betternether-0.1.8.6.jar","https://mediafilez.forgecdn.net/files/2859/893/betternether-0.1.8.6.jar", 1769238));
		mods.add(new Mod("bewitchment-1.12.2-0.0.22.64.jar","https://mediafilez.forgecdn.net/files/3256/343/bewitchment-1.12.2-0.0.22.64.jar", 4919122));
		mods.add(new Mod("BiomesOPlenty-1.12.2-7.0.1.2445-universal.jar","https://mediafilez.forgecdn.net/files/3558/882/BiomesOPlenty-1.12.2-7.0.1.2445-universal.jar", 4746208));
		mods.add(new Mod("Clumps-3.1.2.jar","https://mediafilez.forgecdn.net/files/2666/198/Clumps-3.1.2.jar",13792));
		mods.add(new Mod("CodeChickenLib-1.12.2-3.2.3.358-universal.jar","https://mediafilez.forgecdn.net/files/2779/848/CodeChickenLib-1.12.2-3.2.3.358-universal.jar",704330));
		mods.add(new Mod("CoFHCore-1.12.2-4.6.6.1-universal.jar","https://mediafilez.forgecdn.net/files/2920/433/CoFHCore-1.12.2-4.6.6.1-universal.jar",848187));
		mods.add(new Mod("CoFHWorld-1.12.2-1.4.0.1-universal.jar","https://mediafilez.forgecdn.net/files/2920/434/CoFHWorld-1.12.2-1.4.0.1-universal.jar",505987));
		mods.add(new Mod("Controlling-3.0.10.jar","https://mediafilez.forgecdn.net/files/3025/548/Controlling-3.0.10.jar",29043));
		
		mods.add(new Mod("CreativeCore_v1.10.70_mc1.12.2.jar","https://mediafilez.forgecdn.net/files/3626/833/CreativeCore_v1.10.70_mc1.12.2.jar", 1320474));
		mods.add(new Mod("CustomMobSpawner-3.11.5.jar","https://mediafilez.forgecdn.net/files/2859/433/CustomMobSpawner-3.11.5.jar", 722270));
		mods.add(new Mod("DNCreeper-1.0.jar","https://mediafilez.forgecdn.net/files/2475/164/DNCreeper-1.0.jar", 168073));
		mods.add(new Mod("DrZharks MoCreatures Mod-12.0.5.jar","https://mediafilez.forgecdn.net/files/2628/698/DrZharks+MoCreatures+Mod-12.0.5.jar", 22440403));
		mods.add(new Mod("DungeonsMod-1.12.2-1.0.8.jar","https://mediafilez.forgecdn.net/files/3435/76/DungeonsMod-1.12.2-1.0.8.jar", 893433));
		mods.add(new Mod("DynamicSurroundings-1.12.2-3.6.3.jar","https://mediafilez.forgecdn.net/files/3497/269/DynamicSurroundings-1.12.2-3.6.3.jar", 15778870));
		mods.add(new Mod("EnderStorage-1.12.2-2.4.6.137-universal.jar","https://mediafilez.forgecdn.net/files/2755/787/EnderStorage-1.12.2-2.4.6.137-universal.jar", 223250));
		mods.add(new Mod("extrautils2-1.12-1.9.9.jar","https://mediafilez.forgecdn.net/files/2678/374/extrautils2-1.12-1.9.9.jar", 3226408));
		
		mods.add(new Mod("geckolib-forge-1.12.2-3.0.31.jar","https://mediafilez.forgecdn.net/files/4020/277/geckolib-forge-1.12.2-3.0.31.jar", 3702893));
		mods.add(new Mod("Hwyla-1.8.26-B41_1.12.2.jar","https://mediafilez.forgecdn.net/files/2568/751/Hwyla-1.8.26-B41_1.12.2.jar", 453778));
		mods.add(new Mod("IceAndFireRotN-1.9.1-1.2.1.jar","https://mediafilez.forgecdn.net/files/3809/927/IceAndFireRotN-1.9.1-1.2.1.jar", 20484280));
		mods.add(new Mod("jei_1.12.2-4.16.1.302.jar","https://mediafilez.forgecdn.net/files/3043/174/jei_1.12.2-4.16.1.302.jar", 653210));
		mods.add(new Mod("journeymap-1.12.2-5.7.1.jar","https://mediafilez.forgecdn.net/files/2916/2/journeymap-1.12.2-5.7.1.jar", 6990968));
		mods.add(new Mod("llibrary-1.7.20-1.12.2.jar","https://mediafilez.forgecdn.net/files/3116/493/llibrary-1.7.20-1.12.2.jar", 884550));
		mods.add(new Mod("Mantle-1.12-1.3.3.55.jar","https://mediafilez.forgecdn.net/files/2713/386/Mantle-1.12-1.3.3.55.jar", 843486));
		mods.add(new Mod("minerva-library-1.0.13.jar","https://mediafilez.forgecdn.net/files/2728/693/minerva-library-1.0.13.jar", 133294));
		
		mods.add(new Mod("Neat 1.4-17.jar","https://mediafilez.forgecdn.net/files/2595/310/Neat+1.4-17.jar", 18527));
		mods.add(new Mod("OreLib-1.12.2-3.6.0.1.jar","https://mediafilez.forgecdn.net/files/2820/815/OreLib-1.12.2-3.6.0.1.jar", 341728));
		mods.add(new Mod("Patchouli-1.0-23.6.jar","https://mediafilez.forgecdn.net/files/3162/874/Patchouli-1.0-23.6.jar", 472464));
		mods.add(new Mod("Placebo-1.12.2-1.6.0.jar","https://mediafilez.forgecdn.net/files/2694/382/Placebo-1.12.2-1.6.0.jar", 76902));
		mods.add(new Mod("ProjectE-1.12.2-PE1.4.1.jar","https://mediafilez.forgecdn.net/files/2702/991/ProjectE-1.12.2-PE1.4.1.jar", 1812973));
		mods.add(new Mod("Quark-r1.6-179.jar","https://mediafilez.forgecdn.net/files/2924/91/Quark-r1.6-179.jar", 3877577));
		mods.add(new Mod("RedstoneFlux-1.12-2.1.1.1-universal.jar","https://mediafilez.forgecdn.net/files/2920/436/RedstoneFlux-1.12-2.1.1.1-universal.jar", 31802));
		
		mods.add(new Mod("SereneSeasons-1.12.2-1.2.18-universal.jar","https://mediafilez.forgecdn.net/files/2799/213/SereneSeasons-1.12.2-1.2.18-universal.jar", 185187));
		mods.add(new Mod("TConstruct-1.12.2-2.13.0.183.jar","https://mediafilez.forgecdn.net/files/2902/483/TConstruct-1.12.2-2.13.0.183.jar", 4023372));
		mods.add(new Mod("Thaumcraft-1.12.2-6.1.BETA26.jar","https://mediafilez.forgecdn.net/files/2629/23/Thaumcraft-1.12.2-6.1.BETA26.jar", 11360786));
		mods.add(new Mod("ThaumicJEI-1.12.2-1.6.0-27.jar","https://mediafilez.forgecdn.net/files/2705/304/ThaumicJEI-1.12.2-1.6.0-27.jar", 59213));
		mods.add(new Mod("ThermalDynamics-1.12.2-2.5.6.1-universal.jar","https://mediafilez.forgecdn.net/files/2920/505/ThermalDynamics-1.12.2-2.5.6.1-universal.jar", 728082));
		mods.add(new Mod("ThermalExpansion-1.12.2-5.5.7.1-universal.jar","https://mediafilez.forgecdn.net/files/2926/431/ThermalExpansion-1.12.2-5.5.7.1-universal.jar", 2345139));
		mods.add(new Mod("ThermalFoundation-1.12.2-2.6.7.1-universal.jar","https://mediafilez.forgecdn.net/files/2926/428/ThermalFoundation-1.12.2-2.6.7.1-universal.jar", 2219736));
		mods.add(new Mod("tinkersjei-1.2.jar","https://mediafilez.forgecdn.net/files/2701/203/tinkersjei-1.2.jar", 12255));
		
		mods.add(new Mod("TinkerToolLeveling-1.12.2-1.1.0.jar","https://mediafilez.forgecdn.net/files/2630/860/TinkerToolLeveling-1.12.2-1.1.0.jar", 95850));
		mods.add(new Mod("Toast Control-1.12.2-1.8.1.jar","https://mediafilez.forgecdn.net/files/2707/353/Toast+Control-1.12.2-1.8.1.jar", 21668));
		mods.add(new Mod("xptome-1.12.2-v2.0.1.jar","https://mediafilez.forgecdn.net/files/3648/989/xptome-1.12.2-v2.0.1.jar", 16743));
	}
	
	public void updateDirectoryLabel() {
		if (selectedDirectory == null) {
			directoryLabel.setText("NO MODS FOLDER SELECTED!");
		} else {
			directoryLabel.setText(selectedDirectory.toString());
		}
	}
	
	public void downloadMods() {
		downloadsCompleted = 0;
		totalDownloads = mods.size();
		
		downloadProgress.setVisible(true);
		downloadProgressLabel.setVisible(true);
		modDownloadProgress.setVisible(true);
		modDownloadProgressLabel.setVisible(true);
		
		downloadProgress.setProgress(0);
		modDownloadProgress.setProgress(0);
		
		Task downloadTask = new DownloadTask();
		Thread downloadThread = new Thread(downloadTask);
		downloadThread.setDaemon(true);
		downloadThread.start();
	}
	
	public class DownloadTask extends Task {
		
		public DownloadTask() {
		}
		
		@Override
		protected Object call() throws Exception {
			
			for (Mod mod : mods) {
				String fileURL = mod.getModLink();
				String filePath = mod.getFileName();
				URL downloadURL = new URL(fileURL);
				
				URLConnection conn = (URLConnection) downloadURL.openConnection();
				conn.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8");
				conn.setRequestProperty("accept-encoding", "gzip, deflate, br");
				conn.setRequestProperty("accept-language", "en-US,en;q=0.6");
				conn.setRequestProperty("cookie", "CurseForge.AuthorUpSell=true; ResponsiveSwitch.DesktopMode=1; AWSALB=Z6HPwbUfULHc2DXvLohbW8LQ/Y6EdEtim/O66I0388S8oPTvuKKTYc/QNKEly4bEIWZUJP/n0U2JOuWa4d2r4IxOCVaWfb18MEDRMzi3kjuA9BpYD88LJE8YCwpw; AWSALBCORS=Z6HPwbUfULHc2DXvLohbW8LQ/Y6EdEtim/O66I0388S8oPTvuKKTYc/QNKEly4bEIWZUJP/n0U2JOuWa4d2r4IxOCVaWfb18MEDRMzi3kjuA9BpYD88LJE8YCwpw; Unique_ID_v2=390823f871fd4f92a825b5dda8594e22; __cf_bm=4TTgv6Dd8wEhurG1goyr1lpJJ9cjTwZ4sNyFgJ5hAKY-1668218094-0-ASI8YV3FadFLiKMhM+GSYnERLmbfEXlDzx51mL8o2tm/yJ+iL2P+Ja7RJ4cNoxfC9KJRGYvoJAoVXJBOeaYw4s9xY94zWF4KPz/gOWCHWQTz");
				conn.setRequestProperty("sec-fetch-dest", "document");
				conn.setRequestProperty("sec-fetch-mode", "navigate");
				conn.setRequestProperty("sec-fetch-site", "none");
				conn.setRequestProperty("sec-fetch-user", "?1");
				conn.setRequestProperty("sec-gpc", "1");
				conn.setRequestProperty("upgrade-insecure-requests", "1");
				conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
				//conn.setRequestProperty("", "");
				//conn.setRequestProperty("", "");
				conn.setDoInput(true);
				
				System.out.println("Downloading file: " + selectedDirectory + "\\" + filePath);
				Platform.runLater(() -> {
					modDownloadProgressLabel.setText(filePath);
					modStatusLabel.setText("Starting Download . . .");
				});
				
				int byteAmount = mod.getFileSize();
				
				BufferedInputStream in = null;
				
				try {
					InputStream inputStream = conn.getInputStream();
					in = new BufferedInputStream(inputStream);
				} catch (IOException e) {
					System.out.println("IO EXCEPTION");
					e.printStackTrace();
				}
				
				
				FileOutputStream out = new FileOutputStream(selectedDirectory + "\\" + filePath);
				byte[] dataBuffer = new byte[1024];
				int bytesRead;
				int totalBytesRead = 0;
				
				Platform.runLater(() -> {
					modStatusLabel.setText("Downloading . . .");
				});
				
				while ((bytesRead = in.read(dataBuffer,0,1024)) != -1) { 
					
					totalBytesRead += bytesRead;
					
					double modDownloadProgressValue = totalBytesRead / (double) byteAmount;
					
					Platform.runLater(() -> {
						modDownloadProgress.setProgress(modDownloadProgressValue);
					});
					
					out.write(dataBuffer, 0, bytesRead);
				}
				
				
				in.close();
				out.close();
				
				modDownloadFinished();
			}
			return null;
		}
		
		@Override
		protected void failed() {
			
			System.out.println("Failed to download!");
			
		}
		
		@Override
		protected void succeeded() {
			Platform.runLater(() -> {
				downloadProgress.setProgress(downloadsCompleted / (double) totalDownloads);
				modDownloadProgress.setProgress(0.0);
				modDownloadProgress.setVisible(false);
				modDownloadProgressLabel.setVisible(false);
				downloadProgress.setVisible(false);
				downloadProgressLabel.setVisible(false);
				modStatusLabel.setText("");
				
				Alert finishAlert = new Alert(Alert.AlertType.INFORMATION);
				finishAlert.setTitle("Success!");
				finishAlert.setContentText("Mods have finished downloading successfully!");
				finishAlert.show();
			});
		}
	}
	
	public void modDownloadFinished() {
		System.out.println("Download Complete!");
		downloadsCompleted += 1;
		Platform.runLater(() -> {
			downloadProgress.setProgress(downloadsCompleted / (double) mods.size());
			modDownloadProgress.setProgress(0.0);
		});
	}
	
	private class Mod {
		private String fileName;
		private String modLink;
		private int fileSize;
		
		public Mod(String fileName, String modLink, int fileSize) {
			this.fileName = fileName;
			this.modLink = modLink;
			this.fileSize = fileSize;
		}
		
		public String getFileName() {
			return fileName;
		}
		
		public String getModLink() {
			return modLink;
		}
		
		public int getFileSize() {
			return fileSize;
		}
		
	}
}
